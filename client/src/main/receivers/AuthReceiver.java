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
public class AuthReceiver extends Receiver
{
    public AuthReceiver()
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
    public TCPResponse action(Main main, Subscriber nobody, JSONData data)
    {
        /*

        try {
            String hostName = "antimatter";
            int portNumber = Integer.parseInt(getPort(data));

            //Attempt to start TCP connection, in the future maybe contact TCPWorker in order to start communication
            Socket TCPSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(TCPSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));

            String fromServer;
            String TCPConnectRequest = "{\"receiver\": \"CONNECT\", \"RAND-COOKIE\": " + getCookie(data) + "}";

            if (TCPConnectRequest != null) {
                System.out.println("Client: " + TCPConnectRequest);
                out.println(TCPConnectRequest);
            }

            if((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }

        } catch(IOException e) {System.out.println("Failed to establish TCP connection w/ server");}

        */



        return null;
    }
            /*
            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;
            }

             */

}
