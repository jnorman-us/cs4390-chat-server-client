package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;

public class ShoutReceiver extends Receiver
{
    public ShoutReceiver()
    {
        super("SHOUT", new String[] { "MESSAGE" });
    }

    @Override
    public UDPResponse action(Main main, Subscriber subscriber, JSONData data)
    {
        if(subscriber.connected())
        {
            return new TCPResponse(false, "Heya");
        }
        return new TCPResponse(false, "connect first");
    }
}
