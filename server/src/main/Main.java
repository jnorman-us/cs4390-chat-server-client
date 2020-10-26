package main;

import main.objects.Subscriber;
import main.workers.TCPWorker;
import main.workers.UDPWorker;

import java.io.IOException;
import java.util.HashMap;

public class Main
{
    public static void main(String args[])
    {
        // object instance to keep track of all of the running TCPWorkers
        Main main = new Main();

        // hard encoded, but maybe we will have a text file to import these?
        main.subscribers.put("austin-li", new Subscriber("austin-li"));
        main.subscribers.put("joseph-norman", new Subscriber("joseph-norman"));
        main.subscribers.put("josh-guzman", new Subscriber("josh-guzman"));
        main.subscribers.put("kevin-salinda", new Subscriber("kevin-salinda"));
    }

    // keeps track of all of the registered subscribers to this messaging platform
    private HashMap<String, Subscriber> subscribers;

    private UDPWorker udp_worker; // one UDP server
    private HashMap<String, TCPWorker> tcp_workers;

    public Main()
    {
        subscribers = new HashMap<>();
        tcp_workers = new HashMap<>();

        try {
            udp_worker = new UDPWorker(this,8000);
        } catch(IOException exception) {
            System.out.println("Failed to setup UDP Worker!");
            System.exit(0);
        }

        udp_worker.start();
    }

    public boolean createTCPWorker(Subscriber subscriber)
    {
        if(tcp_workers.containsKey(subscriber.clientID))
        {
            stopTCPWorker(subscriber);
        }

        try {
            TCPWorker newWorker = new TCPWorker(this, subscriber);
            tcp_workers.put(subscriber.clientID, newWorker);
            newWorker.start();
        } catch(IOException exception) {
            return false;
        }
        return true;
    }

    public TCPWorker getTCPWorker(Subscriber subscriber)
    {
        return tcp_workers.get(subscriber);
    }

    public void stopTCPWorker(Subscriber subscriber)
    {
        if(tcp_workers.containsKey((subscriber.clientID)))
        {
            tcp_workers.get(subscriber.clientID).stop();
            tcp_workers.remove(subscriber.clientID);
        }
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
