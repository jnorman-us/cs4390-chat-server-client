package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.ResponseMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;

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
        return "";
    }

    public UDPResponse action(Main main, Subscriber nobody, JSONData data) {
        //send ResponseMessage to Server
        String response = authenticate(data);   //result of MD5 hash algorithm

        ResponseMessage responseMessage = new ResponseMessage();
        HashMap<String, String> message_data = new HashMap<>();
        message_data.put("CLIENT-ID", clientID);
        message_data.put("RES", response);
        return new UDPResponse(responseMessage.stringify(message_data));
    }
}


