package main.objects;

import java.util.ArrayList;

public class Session
{
    private String id;
    private Subscriber[] subscribers;

    public Session(String id, Subscriber a, Subscriber b)
    {
        this.id = id;

        subscribers = new Subscriber[2];
        subscribers[0] = a;
        subscribers[1] = b;
    }

    public String getId()
    {
        return id;
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
        return "Session<id: " + id + ">, <1: " + subscribers[0].clientID + " >, <2: " + subscribers[1].clientID + ">";
    }
}
