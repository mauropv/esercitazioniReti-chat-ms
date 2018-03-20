package mauropiva.reti2018.esercitazioni.services;

import mauropiva.reti2018.esercitazioni.beans.AuthInput;
import mauropiva.reti2018.esercitazioni.beans.MessageAnswer;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by mauropiva on 17/03/18.
 */
@Service
public class UsersDB {

    public HashMap<String,AuthInput> authInfos = new HashMap<>();
    public HashMap<String,Set<MessageAnswer>> messagesDatabase = new HashMap<>();

    public static String generateToken(String s) {

        s="clientkey"+System.currentTimeMillis()+"#-#"+s;
        s = new StringBuilder(s).reverse().toString();
        byte[] encodedBytes = Base64.getEncoder().encode(s.getBytes());
        return new String(encodedBytes);


    }

    public static boolean checkToken(String s){
        try {

            byte[] encodedBytes = s.getBytes();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
            s = new String(decodedBytes);
            s = new StringBuilder(s).reverse().toString();
            if (s.startsWith("clientkey") && s.split("#-#").length == 3) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static String getNickFromToken(String s){
        byte[] encodedBytes =s.getBytes();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
        s = new StringBuilder(new String(decodedBytes)).reverse().toString();
        return s.split("#-#")[1];

    }


    public static void main(String[] args){
        String toCode = "clientkeyCIAONE#-#aaa#-#";
        String encoded = generateToken(toCode);
        System.out.println(encoded);
        System.out.print(checkToken(encoded));
    }
}
