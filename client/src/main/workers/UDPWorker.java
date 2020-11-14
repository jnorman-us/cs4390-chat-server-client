package main.workers;

import main.objects.UDPResponse;
import main.receivers.*;
import main.messages.HelloMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class UDPWorker implements Runnable
{
    // member variables
    private DatagramSocket socket;
    private String rand_cookie_to_return;
    private String port_number_to_return;
    private InetAddress udpServerIP;
    private String clientID;
    private String clientPW;
    private int udpPort;
    private Receiver[] receivers;
    /*
    private Receiver[] receivers = new Receiver[] {
            new ChallengeReceiver(clientID, clientPW),
            new AuthSuccessReceiver(),
            new AuthFailReceiver()
    };

     */

    //member functions
    public String getRand_cookie_to_return() {
        return rand_cookie_to_return;
    }
    public String getPort_number_to_return() {
        return port_number_to_return;
    }
    public InetAddress getUdpServerIP() {
        return udpServerIP;
    }

    public UDPWorker(int port_arg, String clientID_arg, String clientPW_arg, InetAddress udpServerIP_arg) throws IOException
    {
        socket = new DatagramSocket();
        udpPort = port_arg;
        clientID = clientID_arg;
        clientPW = clientPW_arg;
        udpServerIP = udpServerIP_arg;
        receivers = new Receiver[] {
                new ChallengeReceiver(clientID, clientPW),
                new AuthSuccessReceiver(),
                new AuthFailReceiver()
        };
    }
    public int getUdpPort() {
        return udpPort;
    }

    private void sendHelloMessage() throws IOException {
        //Create HelloMessage
        HashMap<String, String> message_data = new HashMap<>(); //hashmap message_data stores the JSON message
        message_data.put("CLIENT-ID-A", "" + clientID);
        HelloMessage helloMessage = new HelloMessage();

        //confirm that helloMessage fits JSON format
        if (helloMessage.sendAble(message_data))
        {
            //send HelloMessage
            String s = helloMessage.stringify(message_data);
            byte[] buf = new byte[65535];
            buf = s.getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, udpServerIP, getUdpPort()); //server uses port 8000 to accept UDP connections
            //System.out.println("created UDP packet w/ buffer size: " + buf.length + ", address: " + udpServerIP + ", port: " + getUdpPort());
            socket.send(datagramPacket);

        } else {
            System.out.println("HELLO packet is not formatted correctly");
        }
    }


    /*
     * Sends HELLO message to server, receives and processes server response
     *
     */
    public void run() {
        //Gather Login information from user
       // boolean loginSuccessful = false;
       // while(!loginSuccessful) {
            try {
                /*
                //DECLARE & INITIALIZE VARIABLES
                Scanner scan = new Scanner(System.in);
                DatagramPacket datagramPacket = null;
                String defaultServerIPString = InetAddress.getLocalHost().toString();   //IP address of the server if server and client running on same machine
                defaultServerIPString = defaultServerIPString.substring(defaultServerIPString.indexOf('/') + 1);

                //Get IP address of Server
                System.out.print("\nIf client and server are running on the same machine, server IP address is " + defaultServerIPString + "\nWhat is the IP address of the server?: " );
                defaultServerIPString = scan.nextLine();
                defaultServerIPString.trim();
                udpServerIP = InetAddress.getByName(defaultServerIPString);

                //Get Client-ID from user
                System.out.print("What is your client ID? i.e. austin-li: ");
                clientID = scan.nextLine();
                clientID.trim();

                //Get Client Password from user
                System.out.print("What is your password?: ");
                clientPW = scan.nextLine();

                 */

                //send HelloMessage to server
                sendHelloMessage();
            } catch (IOException e) {

                System.out.println("Something went wrong while sending HELLO packet. ");
            }

            //Challenge, response, and AUTHSUCCESS/AUTHFAILURE handled by the loop below in their respective receiver classes

            byte[] received = new byte[65535];
            DatagramPacket datagramPacket = null;
            //String parsed = "";

            //parsed = parse(received);

            //REPLACE WHILE LOOP CONDITION W/ OTHER CONDITION
            while(true)
            {
                datagramPacket = new DatagramPacket(received, received.length);
                try {
                    socket.receive(datagramPacket);

                    String parsed = parse(received);
                    JSONData data = new JSONData(parsed);

                    for(Receiver receiver : receivers)
                    {
                        if(receiver.receivable(data))
                        {
                            UDPResponse response = receiver.action(null, null, data);
                            //IF AUTH-SUCCESS RECEIVED, stop sending packets and stop udp worker.
                            if (response.message == "AUTHENTICATED") {
                                    //return data to main somehow
                                    port_number_to_return = ((AuthSuccessReceiver) receiver).getPort(data);
                                    rand_cookie_to_return = ((AuthSuccessReceiver) receiver).getCookie(data);
                                    return;
                            }
                            byte[] bytes_to_send = response.message.getBytes();
                            DatagramPacket sendPacket = new DatagramPacket(
                                    bytes_to_send,
                                    bytes_to_send.length,
                                    datagramPacket.getAddress(),
                                    datagramPacket.getPort()
                            );

                            socket.send(sendPacket);
                        }
                    }
                    Arrays.fill(received, (byte)0);
                } catch (IOException exception) {
                    System.out.println("IO Exception");
                    break;
                }
            }

/*
            // receive CHALLENGE packet
            try {
                ChallengeReceiver challengeReceiver = new ChallengeReceiver(clientID,  );
                byte[] received = new byte[65535];
                DatagramPacket datagramPacket = null;
                datagramPacket = new DatagramPacket(received, received.length);
                socket.receive(datagramPacket);

                String parsed = parse(received);
                JSONData data = new JSONData(parsed);
                System.out.println(parsed);

                //parse data from JSON and send response to the server. See AUTHReceiver class
                if (parsed.contains("AUTH-SUCCESS")) {
                    //UDPResponse response = receiver.action(null, null, data);
                    //AuthReceiver does NOT send a response. instead, rand_cookie and port_number are handed to main
                    rand_cookie_to_return = receiver.getCookie(data);
                    port_number_to_return = receiver.getPort(data);
                    loginSuccessful = true;
                } else {
                    System.out.println("AUTHENTICATION FAILED. PLEASE TRY AGAIN.\n");
                }

            } catch (IOException e) {
                System.out.println("Something went wrong with receiving AUTH packet");
            }


            // Client responds w/ a RESPONSE (Res) to authenticate itself.



            //receive AuthSuccess Message
            try {
                AuthSuccessReceiver authSuccessReceiver = new AuthSuccessReceiver();
                byte[] received = new byte[65535];
                DatagramPacket datagramPacket = null;
                datagramPacket = new DatagramPacket(received, received.length);
                socket.receive(datagramPacket);

                String parsed = parse(received);
                JSONData data = new JSONData(parsed);
                System.out.println(parsed);

                //parse data from JSON and send response to the server. See AUTHReceiver class
                if (parsed.contains("AUTH-SUCCESS")) {
                    //UDPResponse response = receiver.action(null, null, data);
                    //AuthReceiver does NOT send a response. instead, rand_cookie and port_number are handed to main
                    rand_cookie_to_return = authSuccessReceiver.getCookie(data);
                    port_number_to_return = authSuccessReceiver.getPort(data);
                    loginSuccessful = true;
                } else {
                    System.out.println("AUTHENTICATION FAILED. PLEASE TRY AGAIN.\n");
                }

            } catch (IOException e) {
                System.out.println("Something went wrong with receiving AUTH packet");
            }
*/
      //  }

    }



    //parse the received bytes and convert them into a string
    public String parse(byte[] received)
    {
        if(received == null)
            return null;

        StringBuilder raw = new StringBuilder();
        int i = 0;
        while(received[i] != (byte)0)
        {
            raw.append((char)received[i]);
            i ++;
        }

        return raw.toString();
    }
}
