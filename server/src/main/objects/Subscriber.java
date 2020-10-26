package main.objects;

import java.util.concurrent.atomic.AtomicBoolean;

public class Subscriber
{
    public static int last_port = 8001;

    public String clientID;

    public String rand_cookie;
    public int port;

    private AtomicBoolean connected;

    public Subscriber(String id)
    {
        clientID = id;

        rand_cookie = "";
        port = -1;
        connected = new AtomicBoolean(false);
    }

    // might have repeated cookies? there's an astronomically low chance for that
    public void generateRandomCookie()
    {
        final int length = 3;
        rand_cookie = "";

        for(int i = 0; i < length; i ++)
        {
            rand_cookie += (int)(Math.random() * 10);
        }
    }

    public boolean checkRandomCookie(String toCheck)
    {
        return rand_cookie.equals(toCheck);
    }

    public void generatePortNumber()
    {
        port = last_port;
        last_port ++;
    }

    public void connect()
    {
        connected.set(true);
    }

    public void disconnect()
    {
        connected.set(false);
    }

    public boolean connected()
    {
        return connected.get();
    }

}
