package main.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class TCPWorker {

    int RANDCOOKIE;
    int PORTNUM;
    InetAddress SERVERIP;


    public TCPWorker(int rand_cookie, int port_num, InetAddress server_ip) {
        RANDCOOKIE = rand_cookie;
        PORTNUM = port_num;
        SERVERIP = server_ip;
    }


    public void run() {
        try {
            //Attempt to start TCP connection, in the future maybe contact TCPWorker in order to start communication
            Socket TCPSocket = new Socket(SERVERIP, PORTNUM);
            PrintWriter out = new PrintWriter(TCPSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(TCPSocket.getInputStream()));

            String fromServer;
            String TCPConnectRequest = "{\"receiver\": \"CONNECT\", \"RAND-COOKIE\": " + RANDCOOKIE + "}";

            if (TCPConnectRequest != null) {
                System.out.println("Client: " + TCPConnectRequest);
                out.println(TCPConnectRequest);
            }

            if((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
            }
        } catch(IOException e) {System.out.println("Failed to establish TCP connection w/ server");}

        try {
            //send chat request message


            //parse the incoming message for CONNECTED

            //after receiving CONNECTED message, start chatting process.



        } catch(Exception e) {System.out.println("Encountered error while attempting to setup chat connection w/ other client");}

    }

}
