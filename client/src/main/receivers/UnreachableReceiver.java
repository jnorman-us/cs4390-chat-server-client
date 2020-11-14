package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class UnreachableReceiver extends Receiver {


    public UnreachableReceiver()
    {
        super("UNREACHABLE", new String[]{"CLIENT-ID-B"});
    }
    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID-B");
    }
    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println("CLIENT " + getClientID(data) + "is UNREACHABLE");
        System.out.println("To INITIATE a connection to another client, enter Chat <client-id>");
        return null;
    }
}
