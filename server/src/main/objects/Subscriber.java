package main.objects;

import main.Main;
import main.workers.TCPWorker;

import java.io.IOException;

public class Subscriber
{
    public static int last_port = 5001;

    public boolean online;
    public String clientID;

    public TCPWorker worker;
    public String rand_cookie;
    public int port;

    public Subscriber(String id)
    {
        online = false;
        clientID = id;

        worker = null;
        rand_cookie = "";
        port = -1;
    }

    public void generateRandomCookie()
    {
        rand_cookie = "this is totally not random";
    }

    public void generatePortNumber()
    {
        port = last_port;
        last_port ++;
    }

    public boolean generateTCPWorker(Main main)
    {
        try {
            worker = new TCPWorker(main, port);
        } catch(IOException exception) {
            return false;
        }
        return true;
    }
}
