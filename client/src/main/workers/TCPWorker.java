package main.workers;

import main.objects.TCPResponse;
import main.receivers.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPWorker {

    String randCookie;
    int portNum;
    InetAddress serverIP;

    private Receiver[] receivers = new Receiver[] {
            new ConnectedReceiver(),          //parse and respond to connection requests
            new ChatStartedReceiver(),
            new ChatMessageReceiver(),
            new UnreachableReceiver()
    };

    public TCPWorker(String rand_cookie, int port_num, InetAddress server_ip) {
        randCookie = rand_cookie;
        portNum = port_num;
        serverIP = server_ip;


    }


    public void run() {
        try {
            //Attempt to start TCP connection, in the future maybe contact TCPWorker in order to start communication
            Socket TCPSocket = new Socket(serverIP, portNum);
            PrintWriter writer = new PrintWriter(TCPSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));

            String fromServer;
            String TCPConnectRequest = "{\"receiver\": \"CONNECT\", \"RAND-COOKIE\": " + randCookie + "}";

            if (TCPConnectRequest != null) {
                System.out.println("Client: " + TCPConnectRequest);
                writer.println(TCPConnectRequest);
            }

            /*
            if ((fromServer = reader.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }
            */

            //if the server replies with CHAT-STARTED, then start the chat



            //send messages back and forth
            String parsed = "";          //string to store parsed JSON from the server that we have parsed into a string
            boolean connectedToServer = true;
            boolean sendMessageToSelf = false;

            while(connectedToServer)
            {
                //skip receiving next message from the server if you are sending a message to yourself
                if(!sendMessageToSelf) {
                    System.out.println(parsed);
                    parsed = reader.readLine();
                    System.out.println(parsed);
                    sendMessageToSelf = false;
                }

                if(parsed != null)
                {
                    JSONData data = new JSONData(parsed);

                    for(Receiver receiver : receivers)
                    {
                        if(receiver.receivable(data))
                        {
                            TCPResponse response = (TCPResponse)receiver.action(null, null, data);
                            if(response == null) {
                                // don't output anything
                                parsed = "";
                            }
                            else if(response.toString().contains("CHAT_ENDED")) {
                                //if receive END_NOTIF message from server, return to "Connected" state. This ends the chat session w/ current client.
                                //to enter "Connected" state, send a CONNECT message to yourself.
                                parsed = "{\"receiver\": \"CONNECTED\"}";    //send CONNECTED message to self to return to "Connected" state
                                sendMessageToSelf = true;
                            }

                            else {
                                writer.println(response.message);

                                //if user typed "end chat", an EndRequestMessage was sent to the server. If you detect an EndRequestMessage being sent to the server, go back to connected state.
                                if(response.toString().contains("END_NOTIF")) {
                                    //to enter "Connected" state, send a CONNECT message to yourself.
                                    parsed = "{\"receiver\": \"CONNECTED\"}";    //send CONNECTED message to self to return to "Connected" state
                                    sendMessageToSelf = true;
                                }

                                if(response.kick)
                                {
                                    //requestStop();
                                }

                            }

                        }
                    }
                }
                //else requestStop();
            }
           // requestStop();


            //send chat request message
            //parse the incoming message for CONNECTED
            //after receiving CONNECTED message, start chatting process.
        } catch (IOException e) {
            System.out.println("Failed to establish TCP connection w/ server");
        }
    }

}
