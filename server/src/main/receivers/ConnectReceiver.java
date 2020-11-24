package main.receivers;

import main.Main;
import main.messages.ConnectedMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;

import java.util.HashMap;

public class ConnectReceiver extends Receiver
{
    public ConnectReceiver()
    {
        super("CONNECT", new String[] { "RAND-COOKIE" });
    }

    public String getRandCookie(JSONData data)
    {
        return data.data.get("RAND-COOKIE");
    }

    @Override
    public UDPResponse action(Main main, Subscriber subscriber, JSONData data)
    {
        String rand_cookie = getRandCookie(data);

        if(subscriber.checkRandomCookie(rand_cookie))
        {
            subscriber.connect();

            ConnectedMessage connectedMessage = new ConnectedMessage();
            return new TCPResponse(false, connectedMessage.stringify(new HashMap<>()));
        }
        return new TCPResponse(true, "get out");
    }
}
