package mauropiva.reti2018.esercitazioni.controllers;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import jdk.nashorn.internal.runtime.ECMAException;
import mauropiva.reti2018.esercitazioni.beans.AuthAnswer;
import mauropiva.reti2018.esercitazioni.beans.AuthInput;
import mauropiva.reti2018.esercitazioni.beans.HelloAnswer;
import mauropiva.reti2018.esercitazioni.services.UsersDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import mauropiva.reti2018.esercitazioni.beans.ShallowBean;

import javax.jws.soap.SOAPBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping(path="/chat")
public class MainController {


    @Autowired
    UsersDB usersDB;

    public static final String BASE_VERSION = "v1";

    private final Timer helloTimer;

    public MainController(MetricRegistry metricRegistry) {
        this.helloTimer = metricRegistry.timer("mauropiva.reti2018.esercitazioni.metrics.hello");
    }

    @RequestMapping(value =  "/hello", method = GET)
    public HelloAnswer hello(@RequestHeader(value="user-agent") String userAgent, @RequestParam("numeroPreferito") String numeroPreferito) throws Exception {
        Timer.Context timer = helloTimer.time();
        HelloAnswer response = new HelloAnswer("KO",0L);
        try {
            if(!userAgent.equals("chat-client"))
                throw new Exception("Solo i client giusti possono accedere alla chat :(");

            int numPref = Integer.parseInt(numeroPreferito);
            if(numPref>=0&&numPref<=9)
                response = new HelloAnswer("OK",System.currentTimeMillis());

        }
        finally {
            timer.stop();
        }
        return response;
    }

    @RequestMapping(value =  "/auth", method = POST, consumes = "application/json")
    public AuthAnswer auth(@RequestHeader(value="user-agent") String userAgent,@RequestBody AuthInput authInput) throws Exception {
        Timer.Context timer = helloTimer.time();
        AuthAnswer response = new AuthAnswer("KO","");
        try {
            if (!userAgent.equals("chat-client"))
                throw new Exception("Solo i client giusti possono accedere alla chat :(");


            //EFFETTUIAMO LE VERIFICHE SULL'AUTENTICAZIONE
            //1 VALIDIAMO authInput
            if (authInput.getNickname().length() == 0 || authInput.getMatricola().length() == 0 ||
                    authInput.getNonce().length() == 0 || authInput.getPassword().length() == 0)
                throw new Exception("Input errato");

            //2 VALIDIAMO NONCE
            Long nonce = Long.parseLong(authInput.getNonce());
            if(System.currentTimeMillis()-nonce<-36000000||System.currentTimeMillis()-nonce>36000000)
                throw new Exception("Nonce non valido");

            //Nuovo utente?
            if(usersDB.authInfos.containsKey(authInput.getNickname())){
                //No, verifichiamo la password ed in caso sia corretta concediamo un nuovo token
                if(authInput.getPassword().equals(usersDB.authInfos.get(authInput.getNickname()).getPassword()))
                {
                    String token = usersDB.generateToken(authInput.getNickname()+"#-#"+authInput.getPassword());
                    response = new AuthAnswer("OK",token);
                }else{
                    throw new Exception("Password errata :D");
                }

            }
            else{
                usersDB.authInfos.put(authInput.getNickname(),authInput);
                String token = usersDB.generateToken(authInput.getNickname()+"#-#"+authInput.getPassword());
                response = new AuthAnswer("OK",token);
            }

        }
        finally {
            timer.stop();
        }
        return response;
    }

    @RequestMapping(value =  "/friends", method = GET)
    public Set<String> friends(@RequestHeader(value="user-agent") String userAgent, @RequestHeader(value="authorization") String authorization) throws Exception {
        Timer.Context timer = helloTimer.time();
        Set<String> friends = new HashSet<>();
        try {
            if(!userAgent.equals("chat-client"))
                throw new Exception("Solo i client giusti possono accedere alla chat :(");
            if(!UsersDB.checkToken(authorization))
                throw new Exception("Wrong Token");
            if(usersDB.authInfos.containsKey(UsersDB.getNickFromToken(authorization)))
                friends = usersDB.authInfos.keySet();
            else
                throw new Exception();
        }
        finally {
            timer.stop();
        }
        return friends;
    }

}