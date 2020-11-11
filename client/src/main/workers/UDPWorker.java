package main.workers;

import main.Main;
import main.objects.TCPResponse;
import main.objects.UDPResponse;
import main.receivers.AuthReceiver;
import main.receivers.JSONData;
import main.receivers.Receiver;
import main.messages.HelloMessage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class UDPWorker implements Runnable
{
    private DatagramSocket socket;
    private String rand_cookie_to_return;
    private String port_number_to_return;
    private InetAddress serverIP;

    public UDPWorker(int port) throws IOException
    {
       socket = new DatagramSocket();
    }
    public String getRand_cookie_to_return() {
        return rand_cookie_to_return;
    }
    public String getPort_number_to_return() {
        return port_number_to_return;
    }
    public InetAddress getServerIP() {
        return serverIP;
    }


    /*
     * Sends HELLO message to server, receives and processes server response
     *
     */
    @Override
    public void run() {
        //Send Hello message to server when client starts
        boolean loginSuccessful = false;
        while(!loginSuccessful) {
            try {
                //DECLARE & INITIALIZE VARIABLES
                //DatagramSocket socket = new DatagramSocket();
                Scanner scan = new Scanner(System.in);
                DatagramPacket datagramPacket = null;
                byte[] buf = new byte[65535];
                String defaultServerIPString = InetAddress.getLocalHost().toString();   //IP address of the server if server and client running on same machine
                defaultServerIPString = defaultServerIPString.substring(defaultServerIPString.indexOf('/') + 1);


                System.out.print("\nIf client and server are running on the same machine, server IP address is " + defaultServerIPString + "\nWhat is the IP address of the server?: " );
                defaultServerIPString = scan.next();
                serverIP = InetAddress.getByName(defaultServerIPString);

                System.out.print("What is your client ID? i.e. austin-li: ");
                String clientID = scan.next();                                  //get clientID from the user


                HashMap<String, String> message_data = new HashMap<>(); //hashmap message_data stores the JSON message
                message_data.put("CLIENT-ID-A", "" + clientID);

                HelloMessage helloMessage = new HelloMessage();
                if (helloMessage.sendAble(message_data)) //confirm that helloMessage fits JSON format
                {
                    //send the HELLO message
                    String s = helloMessage.stringify(message_data);
                    buf = s.getBytes();
                    //InetAddress address = InetAddress.getByName("antimatter"); //computer hostname
                    datagramPacket = new DatagramPacket(buf, buf.length, serverIP, 8000);
                    System.out.println("created UDP packet w/ buffer size: " + buf.length + ", address: " + serverIP + ", port: " + 8000);
                    socket.send(datagramPacket);

                /*
                //legacy code
                byte[] buf2 = new byte[65535];
                datagramPacket = new DatagramPacket(buf2, buf2.length);
                socket.receive(datagramPacket);

                // display response
                String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("received from server: " + received);

                 */
                } else {
                    System.out.println("HELLO packet is not formatted correctly");
                }
            } catch (IOException e) {
                System.out.println("Something went wrong with login and/or sending HELLO packet. Please ensure the IP address is correct");
            }

            //receive AUTH packet
            try {
                AuthReceiver receiver = new AuthReceiver();
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

            /*
            if(receiver.receivable(data)) {
                byte[] bytes_to_send = parsed.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                        bytes_to_send,
                        bytes_to_send.length,
                        datagramPacket.getAddress(),
                        datagramPacket.getPort()
                );
                socket.send(sendPacket);
            }

             */
            } catch (IOException e) {
                System.out.println("Something went wrong with receiving AUTH packet");
            }
        }


    }



/*
            datagramPacket = new DatagramPacket(received, received.length);
            try {
                socket.receive(datagramPacket);

                String parsed = parse(received);
                JSONData data = new JSONData(parsed);

                for(Receiver receiver : receivers)
                {
                    if(receiver.receivable(data))
                    {
                        UDPResponse response = receiver.action(main, null, data);
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

 */


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
