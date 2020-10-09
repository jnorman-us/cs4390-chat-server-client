package main.receivers;

import main.Main;

public class ConnectReceiver extends Receiver
{
    public ConnectReceiver()
    {
        super("CONNECT", new String[] { "RAND-COOKIE" });
    }

    @Override
    public String action(Main main, JSONData data)
    {
        return "";
    }
}
