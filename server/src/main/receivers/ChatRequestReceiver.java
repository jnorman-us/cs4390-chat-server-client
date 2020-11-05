package main.receivers;

import main.Main;
import main.messages.ChatStartedMessage;
import main.messages.UnreachableMessage;
import main.objects.Session;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;
import main.workers.TCPWorker;

import java.util.HashMap;

public class ChatRequestReceiver extends Receiver
{
    public ChatRequestReceiver()
    {
        super("CHAT-REQUEST", new String[] { "CLIENT-ID-B" });
    }

    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID-B");
    }

    @Override
    public UDPResponse action(Main main, Subscriber me, JSONData data)
    {
        Subscriber them = main.getSubscriber(getClientID(data));
        System.out.println(me + " " + them);

        if(!me.connected())
            return new TCPResponse(true, "who the fuck are you?");

        if(them != null && them.connected() && !them.equals(me))
        {
            Session my_session = main.getSession(me);
            Session their_session = main.getSession(them);

            if(my_session == null && their_session == null)
            {
                Session our_session = new Session(me, them);
                main.createSession(our_session);

                // now attempt to send that message to the other guy
                HashMap<String, String> their_message_data = new HashMap<>();
                their_message_data.put("CLIENT-ID-B", me.clientID);

                ChatStartedMessage chatStartedMessage = new ChatStartedMessage();
                TCPWorker their_worker = main.getTCPWorker(them);
                their_worker.send(chatStartedMessage.stringify(their_message_data));

                // now attempt to send that message to ourselves
                HashMap<String, String> our_message_data = new HashMap<>();
                our_message_data.put("CLIENT-ID-B", them.clientID);
                return new TCPResponse(false, chatStartedMessage.stringify(our_message_data));
            }
            if(my_session != null && their_session == null)
                return new TCPResponse(false, "the fuck, you're already in a chat");
        }

        HashMap<String, String> message_data = new HashMap<>();
        message_data.put("CLIENT-ID-B", them.clientID);

        UnreachableMessage unreachableMessage = new UnreachableMessage();
        return new TCPResponse(false, unreachableMessage.stringify(message_data));
    }
}
