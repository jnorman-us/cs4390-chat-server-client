package main.workers;

import main.objects.TCPResponse;
import main.receivers.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.Base64;

public class TCPWorker {
    String CK_A;
    String randCookie;
    int portNum;
    InetAddress serverIP;

    private SecretKeySpec aesKey;
    private Cipher cipher;

    private Receiver[] receivers = new Receiver[] {
            new ConnectedReceiver(),          //parse and respond to connection requests
            new ChatStartedReceiver(),
            new ChatMessageReceiver(),
            new UnreachableReceiver(),
            new EndNotifReceiver(),
            new HistoryResponseReceiver()
    };

    public TCPWorker(String CK_A, String rand_cookie, int port_num, InetAddress server_ip) {
        this.CK_A = CK_A;
        randCookie = rand_cookie;
        portNum = port_num;
        serverIP = server_ip;

        try {
            aesKey = new SecretKeySpec(this.CK_A.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
        }
    }


    public void run() {
        try {
            //Attempt to start TCP connection, in the future maybe contact TCPWorker in order to start communication
            Socket TCPSocket = new Socket(serverIP, portNum);
            PrintWriter writer = new PrintWriter(TCPSocket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));

            String fromServer;
            String TCPConnectRequest = "{\"receiver\": \"CONNECT\", \"RAND-COOKIE\": " + "\"" + randCookie + "\"" + "}";

            if (TCPConnectRequest != null) {
                System.out.println("Client: " + TCPConnectRequest);
                writer.println(encrypt(TCPConnectRequest));
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
                    //System.out.println(parsed);
                    parsed = reader.readLine();
                    //System.out.println("Received from server: " + parsed);
                    parsed = decrypt(parsed);
                    //System.out.println("\t➤ " + parsed);
                }
                sendMessageToSelf = false;

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
                            else if(response.toString().contains("RETURN_TO_CONNECTED_STATE") || response.toString().contains("HISTORY_VIEWED")
                                    || response.toString().contains("UNREACHABLE")) {
                                //if receive END_NOTIF message from server, EndNotifReceiver should make client enter "Connected" state.
                                // This ends the chat session w/ current client.
                                //to enter "Connected" state, send a CONNECTED message to yourself.
                                parsed = "{\"receiver\": \"CONNECTED\"}";    //send CONNECTED message to self to return to "Connected" state
                                sendMessageToSelf = true;
                            }
                            else {
                                writer.println(encrypt(response.message));
/*
                                //if user typed "end chat", an EndRequestMessage was sent to the server. If you detect an EndRequestMessage being sent to the server, go back to connected state.
                                if(response.toString().contains("END_REQUEST")) {
                                    //to enter "Connected" state, send a CONNECT message to yourself.
                                    parsed = "{\"receiver\": \"CONNECTED\"}";    //send CONNECTED message to self to return to "Connected" state
                                    sendMessageToSelf = true;
                                }

 */
                                if(response.kick)
                                {
                                    //requestStop();
                                }

                            }

                        }
                    }
                }
                else return;
            }
            // requestStop();


            //send chat request message
            //parse the incoming message for CONNECTED
            //after receiving CONNECTED message, start chatting process.
        } catch (IOException e) {
            System.out.println("Failed to establish TCP connection w/ server");
        }
    }

    public String encrypt(String message)
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            byte[] values = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(values);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String encrypted)
    {
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);

            byte[] values = Base64.getDecoder().decode(encrypted);
            return new String(cipher.doFinal(values));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
