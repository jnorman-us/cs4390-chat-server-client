package main.receivers;

import main.Main;
import main.messages.EndNotifMessage;
import main.objects.Session;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;
import main.workers.TCPWorker;

import java.util.HashMap;

public class EndRequestReceiver extends Receiver
{
    public EndRequestReceiver()
    {
        super("END-REQUEST", new String[] { "SESSION-ID" });
    }

    public String getSessionID(JSONData data)
    {
        return data.data.get("SESSION-ID");
    }

    @Override
    public UDPResponse action(Main main, Subscriber sender, JSONData data)
    {
        if(!sender.connected())
            return new TCPResponse(true, "not connected");

        Session this_session = main.getSession(getSessionID(data));
        if(this_session != null)
        {
            Subscriber receiver = this_session.getOther(sender);

            main.endSession(this_session.getId());

            HashMap<String, String> their_message_data = new HashMap<>();
            their_message_data.put("SESSION-ID", this_session.getId());

            EndNotifMessage endNotifMessage = new EndNotifMessage();
            TCPWorker their_worker = main.getTCPWorker(receiver);
            their_worker.send(endNotifMessage.stringify(their_message_data));

            HashMap<String, String> our_message_data = new HashMap<>();
            our_message_data.put("SESSION-ID", this_session.getId());
            return new TCPResponse(false, endNotifMessage.stringify(our_message_data));
        }
        return new TCPResponse(false, "no such session");
    }
}
