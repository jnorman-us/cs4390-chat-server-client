package main.receivers;

import main.Main;
    //import main.messages.AuthFailMessage;
    //import main.messages.AuthSuccessMessage;
import main.objects.TCPResponse;
import main.objects.Subscriber;
import main.objects.UDPResponse;

import java.io.*;
import java.net.Socket;

import java.net.Socket;
import java.util.HashMap;

//This class responds to AUTH-SUCCESS. AuthFailReceiver responds to AUTH-FAIL
public class AuthSuccessReceiver extends Receiver
{
    public AuthSuccessReceiver()
    {
        super("AUTH-SUCCESS", new String[]{ "RAND-COOKIE", "PORT-NUMBER"});
    }

    public String getCookie(JSONData data)
    {
        return data.data.get("RAND-COOKIE");
    }
    public String getPort(JSONData data)
    {
        return data.data.get("PORT-NUMBER");
    }


    @Override
    public UDPResponse action(Main main, Subscriber nobody, JSONData data)
    {
        System.out.println("Authentication Successful!");
        //Need to somehow establish TCP connection.
        // 1) break out of loop in UDP Worker
        // create a BreakResponse?

        // 2) send info to main, so that TCPWorker can begin its work
        return new UDPResponse("AUTHENTICATED");
    }
}
