package mauropiva.reti2018.esercitazioni.controllers;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.runtime.ECMAException;
import mauropiva.reti2018.esercitazioni.beans.*;
import mauropiva.reti2018.esercitazioni.services.UsersDB;
import mauropiva.reti2018.esercitazioni.services.VotiDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.jws.soap.SOAPBinding;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


@RestController
@RequestMapping(path="/chat")
public class MainController {


    @Autowired
    UsersDB usersDB;

    @Autowired
    VotiDB votiDB;

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
            if(System.currentTimeMillis()-nonce<-100000||System.currentTimeMillis()-nonce>100000)
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

    @RequestMapping(value =  "/allMessagesForMe", method = GET)
    public Set<MessageAnswer> allMessagesForMe(@RequestHeader(value="user-agent") String userAgent, @RequestHeader(value="authorization") String authorization, @RequestParam("nickname") String nickname) throws Exception {
        Timer.Context timer = helloTimer.time();
        Set<MessageAnswer> friends = new HashSet<>();
        try {
            if(!userAgent.equals("chat-client"))
                throw new Exception("Solo i client giusti possono accedere alla chat :(");
            if(!UsersDB.checkToken(authorization))
                throw new Exception("Wrong Token");
            if(!usersDB.authInfos.containsKey(UsersDB.getNickFromToken(authorization)))
                throw new Exception("Wrong Token");

            if(UsersDB.getNickFromToken(authorization).equals(nickname)){
                friends = usersDB.messagesDatabase.remove(nickname);
            }else{
                throw new Exception();
            }

        }
        finally {
            timer.stop();
        }
        return friends;
    }


    @RequestMapping(value =  "/sendMessage", method = PUT, consumes = "application/json")
    @ResponseStatus( HttpStatus.CREATED )
    public void sendMessage(@RequestHeader(value="user-agent") String userAgent, @RequestHeader(value="authorization") String authorization, @RequestBody SendMessageInput sendMessageInput) throws Exception {
        Timer.Context timer = helloTimer.time();
        try {
            if(!userAgent.equals("chat-client"))
                throw new Exception("Solo i client giusti possono accedere alla chat :(");
            if(!UsersDB.checkToken(authorization)||!sendMessageInput.getNickname().equals(UsersDB.getNickFromToken(authorization)))
                throw new Exception("Wrong Token");
            if(!usersDB.authInfos.containsKey(sendMessageInput.getDstnickname())) {
                throw new Exception("Destinatario inesistente :(");
            }

            if(!usersDB.messagesDatabase.containsKey(sendMessageInput.getDstnickname())){
                usersDB.messagesDatabase.put(sendMessageInput.getDstnickname(),new HashSet<MessageAnswer>());
            }
            usersDB.messagesDatabase.get(sendMessageInput.getDstnickname()).add(new MessageAnswer(sendMessageInput.getNickname(),sendMessageInput.getText(),sendMessageInput.getTimestamp()));
        }
        finally {
            timer.stop();
        }
    }

    //@ApiModelProperty(hidden=true)
    @ApiIgnore
    @CrossOrigin(origins = "*")
    @RequestMapping(value =  "/sendVal", method = POST, consumes = "application/json")
    public void sendVal(@RequestBody SurveyInput surveyInput) throws Exception {
        Timer.Context timer = helloTimer.time();

        try {

            votiDB.voti.put(surveyInput.getUserid(),surveyInput.getChoosen());

        }
        finally {
            timer.stop();
        }

    }


    @ApiIgnore
    //@ApiModelProperty(hidden=true)
    @RequestMapping(value =  "/delVal", method = GET)
    public void delVal() throws Exception {
        Timer.Context timer = helloTimer.time();

        try {

            votiDB.voti.clear();

        }
        finally {
            timer.stop();
        }

    }

    @ApiIgnore
    //@ApiModelProperty(hidden=true)
    @CrossOrigin(origins = "*")
    @RequestMapping(value =  "/getVal", method = GET)
    public VotiAnswer getVal() throws Exception {
        Timer.Context timer = helloTimer.time();
        VotiAnswer answer = new VotiAnswer(""+0,""+0,""+0,""+0,""+0);
        try {
            int a=0,b=0,c=0,d=0,e=0;

            for(String voto: votiDB.voti.values()){
                if(voto.equals("A")) a++;
                if(voto.equals("B")) b++;
                if(voto.equals("C")) c++;
                if(voto.equals("D")) d++;
                if(voto.equals("E")) e++;
            }
            answer = new VotiAnswer(""+a,""+b,""+c,""+d,""+e);
        }
        finally {
            timer.stop();
        }
        return  answer;
    }



}