package main.objects;

import java.util.ArrayList;

public class Session
{
    private Subscriber[] subscribers;
    private ArrayList<Chat> history;

    public Session(Subscriber a, Subscriber b)
    {
        subscribers = new Subscriber[2];
        subscribers[0] = a;
        subscribers[1] = b;

        history = new ArrayList<>();
    }

    public void sendChat(Chat chat)
    {
        history.add(chat);
    }

    public ArrayList<Chat> getHistory()
    {
        return history;
    }

    public Subscriber getOther(Subscriber me)
    {
        if(subscribers[0].equals(me))
            return subscribers[1];
        else if(subscribers[1].equals(me))
            return subscribers[0];
        return null;
    }

    public boolean hasSubscriber(Subscriber subscriber)
    {
        for(Subscriber s : subscribers)
        {
            if(s.equals(subscriber))
            {
                return true;
            }
        }
        return false;
    }

    public String toString()
    {
        return "Susbcriber 1: " + subscribers[0].toString() + "\nSubscriber 2: " + subscribers[1].toString();
    }
}
