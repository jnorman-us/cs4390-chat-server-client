package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.ChatStartedMessage;
import main.messages.UnreachableMessage;
import main.objects.*;
import main.workers.TCPWorker;

import java.util.HashMap;

public class ChatReceiver extends Receiver
{
    public ChatReceiver()
    {
        super("CHAT", new String[] { "CHAT-MESSAGE" });
    }

    public String getChatMessage(JSONData data)
    {
        return data.data.get("CHAT-MESSAGE");
    }

    @Override
    public UDPResponse action(Main main, Subscriber sender, JSONData data)
    {
        if(!sender.connected())
            return new TCPResponse(true, "message got?");

        Session this_session = main.getSession(sender);

        if(this_session != null) {
            Subscriber receiver = this_session.getOther(sender);

            Chat newMessage = new Chat(getChatMessage(data), sender, receiver);

            //Creating the JSON packet
            HashMap<String, String> their_message_data = new HashMap<>();
            their_message_data.put("CHAT-MESSAGE", getChatMessage(data));

            ChatMessage chatMessage = new ChatMessage();
            TCPWorker their_worker = main.getTCPWorker(receiver);
            their_worker.send(chatMessage.stringify(their_message_data));

            // now attempt to send that message to sender
            return new TCPResponse(false, " ");
        }

        UnreachableMessage unreachableMessage = new UnreachableMessage();
        return new TCPResponse(false, "message failed to send");
    }
}
