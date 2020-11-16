package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.ResponseMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class ChallengeReceiver extends Receiver {
    private String clientID;    //ID of the client, this value will be part of the response message
    private String clientPW;    //password of client, this value is used in MD5 algorithm to generate the response

    public ChallengeReceiver(String clientID_arg, String clientPW_arg)
    {
        super("CHALLENGE", new String[]{"RAND"});
        clientID = clientID_arg;
        clientPW = clientPW_arg;
    }

    public String getRAND(JSONData data)
    {
        return data.data.get("RAND");
    }

    //Run MD5 hashing algorithm using RAND and clientPW as the parameters to the MD5 algorithm
    private String authenticate(JSONData data) {
        String K_A = clientPW;
        String RAND = getRAND(data);
        return MD5(RAND, K_A);
    }

    public UDPResponse action(Main main, Subscriber nobody, JSONData data) {
        //send ResponseMessage to Server
        String response = authenticate(data);   //result of MD5 hash algorithm

        System.out.println(response);

        ResponseMessage responseMessage = new ResponseMessage();
        HashMap<String, String> message_data = new HashMap<>();
        message_data.put("CLIENT-ID", clientID);
        message_data.put("RES", response);
        return new UDPResponse(responseMessage.stringify(message_data));
    }

    public String MD5(String rand, String ka)
    {
        String key = rand + " " + ka;
        System.out.println(key);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            BigInteger no = new BigInteger(1, digest);
            String ht = no.toString(16);
            while (ht.length() < 32) {
                ht = "0" + ht;
            }
            return ht;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}


