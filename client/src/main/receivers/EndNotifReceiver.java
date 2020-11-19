package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class EndNotifReceiver extends Receiver{
    public EndNotifReceiver()
    {
        super("END-NOTIF", new String[] {"SESSION-ID"});
    }

    public String getSessionID(JSONData data) { return data.data.get("SESSION-ID"); }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println("CHAT SESSION ENDED. THE SESSION ID WAS: " + getSessionID(data));
        return new TCPResponse(false, "RETURN_TO_CONNECTED_STATE");
    }
}
