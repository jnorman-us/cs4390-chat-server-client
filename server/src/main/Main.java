package main;

import main.receivers.HelloReceiver;
import main.workers.TCPWorker;
import main.workers.UDPWorker;

import java.io.IOException;
import java.util.ArrayList;

public class Main
{
    public static void main(String args[])
    {
        // object instance to keep track of all of the running TCPWorkers
        Main main = new Main();

        HelloReceiver test = new HelloReceiver();

        System.out.println(test.parse("{\"receiver\": \"HELLO\", \"CLIENT-ID-A\": \"Wine_Craft\"}"));
        System.out.println(test.json_data);
    }

    private UDPWorker udp_worker; // one UDP server
    private ArrayList<TCPWorker> tcp_workers; // that opens up several TCP servers

    public Main()
    {
        try {
            this.udp_worker = new UDPWorker(this,5000);
        } catch(IOException exception) {
            System.out.println("Failed to setup UDP Worker!");
            System.exit(0);
        }
        this.tcp_workers = new ArrayList<TCPWorker>();

        Thread udp_thread = new Thread(this.udp_worker);
        udp_thread.start();
    }

    public void createTCPWorker()
    {

    }
}
