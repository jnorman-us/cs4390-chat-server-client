package main.receivers;

import main.Main;
import main.objects.Response;

public class ConnectReceiver extends Receiver
{
    public ConnectReceiver()
    {
        super("CONNECT", new String[] { "RAND-COOKIE" });
    }

    @Override
    public Response action(Main main, JSONData data)
    {
        return new Response(false, "");
    }
}
