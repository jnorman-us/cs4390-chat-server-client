package main.workers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPWorker {

    int RANDCOOKIE;
    int PORTNUM;

    public TCPWorker(int rand_cookie, int port_num) {
        RANDCOOKIE = rand_cookie;
        PORTNUM = port_num;
    }


    public void run() {
        try {
            String hostName = "antimatter";
            //int portNumber = Integer.parseInt(getPort(data));

            //Attempt to start TCP connection, in the future maybe contact TCPWorker in order to start communication
            Socket TCPSocket = new Socket(hostName, PORTNUM);
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

    }

}
