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
public class VotiDB {

    public HashMap<String,String> voti = new HashMap<>();

}
