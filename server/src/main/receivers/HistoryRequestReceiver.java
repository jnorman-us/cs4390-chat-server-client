package main.receivers;


import main.Main;
import main.messages.HistoryResponseMessage;
import main.objects.Chat;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;
import main.workers.TCPWorker;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryRequestReceiver extends Receiver
{
    public HistoryRequestReceiver()
    {
        super("HISTORY-REQ", new String[] { "CLIENT-ID-B" });
    }

    public String getClientID(JSONData data) { return data.data.get("CLIENT-ID-B"); }

    @Override
    public UDPResponse action(Main main, Subscriber sender, JSONData data)
    {
        if(!sender.connected())
            return new TCPResponse(true, "not connected");

        Subscriber receiver = main.getSubscriber(getClientID(data));

        if(receiver != null)
        {
            ArrayList<Chat> chatHistory = new ArrayList<Chat>();
            chatHistory = main.returnChatHistory(sender, receiver);

            for (int i = 0; i < chatHistory.size(); i++) {
                //Creating the JSON packet
                HashMap<String, String> past_chat = new HashMap<>();
                past_chat.put("CLIENT-ID", chatHistory.get(i).getSender().clientID);
                past_chat.put("CHAT-MESSAGE", chatHistory.get(i).getMessage());
                past_chat.put("SESSION-ID", chatHistory.get(i).getSession().getId());
                past_chat.put("LAST", "false");

                HistoryResponseMessage historyResponse = new HistoryResponseMessage();
                TCPWorker worker = main.getTCPWorker(sender);
                worker.send(historyResponse.stringify(past_chat));
            }
        }

        HashMap<String, String> last_chat = new HashMap<>();
        last_chat.put("CLIENT-ID", "");
        last_chat.put("CHAT-MESSAGE", "");
        last_chat.put("SESSION-ID", "");
        last_chat.put("LAST", "true");

        HistoryResponseMessage historyResponse = new HistoryResponseMessage();
        return new TCPResponse(false, historyResponse.stringify(last_chat));
    }
}
