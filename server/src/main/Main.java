package main;

import main.messages.AuthSuccessMessage;
import main.objects.Subscriber;
import main.receivers.JSONData;
import main.workers.UDPWorker;

import java.io.IOException;
import java.util.HashMap;

public class Main
{
    public static void main(String args[])
    {
        // object instance to keep track of all of the running TCPWorkers
        Main main = new Main();
        main.subscribers.put("austin-li", new Subscriber("austin-li"));
        main.subscribers.put("joseph-norman", new Subscriber("joseph-norman"));
        main.subscribers.put("josh-guzman", new Subscriber("josh-guzman"));
        main.subscribers.put("kevin-salinda", new Subscriber("kevin-salinda"));
    }

    private HashMap<String, Subscriber> subscribers;
    private UDPWorker udp_worker; // one UDP server

    public Main()
    {
        subscribers = new HashMap<>();

        try {
            udp_worker = new UDPWorker(this,5000);
        } catch(IOException exception) {
            System.out.println("Failed to setup UDP Worker!");
            System.exit(0);
        }

        Thread udp_thread = new Thread(udp_worker);
        udp_thread.start();
    }

    public Subscriber getSubscriber(String clientID)
    {
        if(subscribers.containsKey(clientID))
        {
            return subscribers.get(clientID);
        }
        return null;
    }
}
