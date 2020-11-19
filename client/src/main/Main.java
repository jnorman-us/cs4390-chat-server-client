package main;

import main.workers.TCPWorker;
import main.workers.UDPWorker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import static main.receivers.ChallengeReceiver.MD5;


public class Main {
    public static void main(String args[])
    {
        // PART 1: get user login info
        try {
            //DECLARE & INITIALIZE VARIABLES
            Scanner scan = new Scanner(System.in);
            DatagramPacket datagramPacket = null;
            String defaultServerIPString = InetAddress.getLocalHost().toString();   //IP address of the server if server and client running on same machine
            defaultServerIPString = defaultServerIPString.substring(defaultServerIPString.indexOf('/') + 1);
            InetAddress serverIP;
            String clientID;
            String clientPW;

            //Get IP address of Server
            //System.out.print("\nIf client and server are running on the same machine, server IP address is " + defaultServerIPString + "\nWhat is the IP address of the server?: " );
            System.out.print("If client and server are running on the same machine, you can connect to server via local loop address: 127.0.0.1\n\tWhat is the IP address of the server?: ");
            defaultServerIPString = scan.nextLine();
            defaultServerIPString.trim();
            serverIP = InetAddress.getByName(defaultServerIPString);

            //Get Client-ID from user
            System.out.print("What is your client ID? i.e. firstName-lastName: ");
            clientID = scan.nextLine();
            clientID = clientID.trim();

            //Get Client Password from user
            System.out.print("What is your password?: ");
            clientPW = scan.nextLine();

            //==========================================

            //Start UDP worker to begin authentication phases w/ server
            UDPWorker udp_worker = new UDPWorker(8000, clientID, clientPW, serverIP);
            udp_worker.run();


            //once udp_worker finishes running, we can get the RAND-COOKIE and PORT-NUMBER
            int portNum = Integer.parseInt(udp_worker.getPort_number_to_return());
            String randCookie = udp_worker.getRand_cookie_to_return();
            String CK_A = MD5(randCookie, clientPW);
            InetAddress serverIP_arg = serverIP;
            TCPWorker tcp_worker = new TCPWorker(CK_A, randCookie, portNum, serverIP_arg);

            //run the TCP worker
            tcp_worker.run();

            //UDP Worker should return the RAND-COOKIE and PORT-NUMBER so TCPWorker can start connection
            //start TCPWorker to connect to the server and start chatting.

            System.out.println("Chat terminated.");


        } catch(Exception exception) {
            System.out.println("Unknown Error, terminating program");
            exception.printStackTrace();
        }

    }

}
