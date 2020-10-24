package main.objects;

public class Subscriber
{
    public static int last_port = 8001;

    public String clientID;

    public String rand_cookie;
    public int port;

    public Subscriber(String id)
    {
        clientID = id;

        rand_cookie = "";
        port = -1;
    }

    // might have repeated cookies? there's an astronomically low chance for that
    public void generateRandomCookie()
    {
        final int length = 10;
        rand_cookie = "";

        for(int i = 0; i < length; i ++)
        {
            rand_cookie += (int)(Math.random() * 10);
        }
    }

    public void generatePortNumber()
    {
        port = last_port;
        last_port ++;
    }
}
