package main;

import main.workers.TCPWorker;
import main.workers.UDPWorker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class Main {
    public static void main(String args[])
    {
        try {
            //Start UDP worker to take care of login
            UDPWorker udp_worker = new UDPWorker(8000);
            udp_worker.run();

            //once udp_worker finishes running, we can get the RAND-COOKIE and PORT-NUMBER
            int portNum = Integer.parseInt(udp_worker.getPort_number_to_return());
            int randCookie = Integer.parseInt(udp_worker.getRand_cookie_to_return());
            TCPWorker tcp_worker = new TCPWorker(randCookie, portNum);
            tcp_worker.run();


            //UDP Worker should return the RAND-COOKIE and PORT-NUMBER so TCPWorker can start connection
            //start TCPWorker to connect to the server and start chatting.

        } catch(IOException exception) {
            System.out.println("Unknown Error, terminating program");
            exception.printStackTrace();
        }

    }

}
