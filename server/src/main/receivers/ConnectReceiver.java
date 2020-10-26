package main.receivers;

import main.Main;
import main.messages.ConnectedMessage;
import main.objects.Response;

import java.util.HashMap;

public class ConnectReceiver extends Receiver
{
    public ConnectReceiver()
    {
        super("CONNECT", new String[] { "RAND-COOKIE" });
    }

    @Override
    public Response action(Main main, JSONData data)
    {
        ConnectedMessage connectedMessage = new ConnectedMessage();
        return new Response(false, connectedMessage.stringify(new HashMap<>()));
    }
}
