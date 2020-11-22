package main;

import main.objects.Chat;
import main.objects.Session;
import main.objects.Subscriber;
import main.workers.TCPWorker;
import main.workers.UDPWorker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main
{
    public static void main(String args[])
    {
        // object instance to keep track of all of the running TCPWorkers
        Main main = new Main();

        // hard encoded, but maybe we will have a text file to import these?
        main.subscribers.put("austin-li", new Subscriber("austin-li", "yo123"));
        main.subscribers.put("joseph-norman", new Subscriber("joseph-norman", "toe123"));
        main.subscribers.put("josh-guzman", new Subscriber("josh-guzman", "bro123"));
        main.subscribers.put("kevin-salinda", new Subscriber("kevin-salinda", "moe123"));

        main.subscribers.put("user1", new Subscriber("user1", "pass"));
        main.subscribers.put("user2", new Subscriber("user2", "pass"));
        main.subscribers.put("user3", new Subscriber("user3", "pass"));
        main.subscribers.put("user4", new Subscriber("user4", "pass"));
        main.subscribers.put("user5", new Subscriber("user5", "pass"));
        main.subscribers.put("user6", new Subscriber("user6", "pass"));
        main.subscribers.put("user7", new Subscriber("user7", "pass"));
        main.subscribers.put("user8", new Subscriber("user8", "pass"));
        main.subscribers.put("user9", new Subscriber("user9", "pass"));
        main.subscribers.put("user10", new Subscriber("user10", "pass"));


        Scanner reader = new Scanner(System.in);

        while(true)
        {
            reader.nextLine();
            System.out.println(main);
        }
    }

    // keeps track of all of the registered subscribers to this messaging platform
    private HashMap<String, Subscriber> subscribers;
    private HashMap<String, Session> sessions;
    private int sessionID;

    //ArrayList of all chat history
    private ArrayList <Chat> chatHistory = new ArrayList<Chat>();

    private UDPWorker udp_worker; // one UDP server
    private HashMap<String, TCPWorker> tcp_workers;

    public Main()
    {
        subscribers = new HashMap<>();
        sessions = new HashMap<>();
        sessionID = 0;

        try {
            udp_worker = new UDPWorker(this,8000);
        } catch(IOException exception) {
            System.out.println("Failed to setup UDP Worker!");
            System.exit(0);
        }
        tcp_workers = new HashMap<>();

        udp_worker.start();
    }

    //Add a chat to server's chat history
    public void addToChatHistory(Chat message) { chatHistory.add(message); }

    //Return chat all history
    public ArrayList<Chat> returnChatHistory() { return chatHistory; }
    //Return chat history from two subscribers
    public ArrayList<Chat> returnChatHistory(Subscriber sub1, Subscriber sub2)
    {
        ArrayList<Chat> returnList = new ArrayList<Chat>();
        for(int i = 0; i < chatHistory.size(); i++)
        {
            if((chatHistory.get(i).getSender().equals(sub1) && chatHistory.get(i).getReceiver().equals(sub2))
            || (chatHistory.get(i).getSender().equals(sub2) && chatHistory.get(i).getReceiver().equals(sub1))){
                returnList.add(chatHistory.get(i));
            }
        }
        return returnList;
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
        return tcp_workers.get(subscriber.clientID);
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

    public boolean createSession(Subscriber a, Subscriber b)
    {
        String newSessionID = "" + sessionID ++;
        sessions.put(newSessionID, new Session(newSessionID, a, b));
        return true;
    }

    public Session getSession(String id)
    {
        if(sessions.containsKey(id))
        {
            return sessions.get(id);
        }
        return null;
    }

    public Session getSession(Subscriber subscriber)
    {
        for(Session session : sessions.values())
        {
            if(session.hasSubscriber(subscriber))
                return session;
        }
        return null;
    }

    public void endSession(String id)
    {
        sessions.remove(id);
    }

    // debugging function that prints the entire status of the program
    // i.e. who the subscribers are, if they're connected,
    public String toString()
    {
        String toReturn = "";
        toReturn += "Subscribers: -----------\n";
        for(Subscriber subscriber : subscribers.values())
        {
            toReturn += "\t➤ " + subscriber.toString() + "\n";
        }

        toReturn += "TCP Workers: -----------\n";
        for(TCPWorker tcpWorker : tcp_workers.values())
        {
            toReturn += "\t➤ " + tcpWorker.toString() + "\n";
        }

        toReturn += "Sessions: --------------\n";
        for(Session session : sessions.values())
        {
            toReturn += "\t➤ " + session.toString() + "\n";
        }

        toReturn += "Chats: --------------\n";
        for(Chat chat : chatHistory)
        {
            toReturn += "\t➤ " + chat.toString() + "\n";
        }
        return toReturn;
    }
}
