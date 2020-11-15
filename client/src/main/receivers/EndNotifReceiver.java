package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class EndNotifReceiver extends Receiver{
    public EndNotifReceiver()
    {
        super("END_NOTIF", new String[] {});
    }
    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println("CHAT ENDED");
        return new TCPResponse(false, "CHAT_ENDED");
    }
}
