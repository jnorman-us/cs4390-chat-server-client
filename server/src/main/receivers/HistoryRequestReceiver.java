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
        super("HISTORY_REQ", new String[] { "CLIENT-ID-B" });
    }

    public String getClientID(JSONData data) { return data.data.get("CLIENT-ID-B"); }

    @Override
    public UDPResponse action(Main main, Subscriber sender, JSONData data)
    {
        if(!sender.connected())
            return new TCPResponse(true, "not connected");

        ArrayList<Chat> chatHistory = new ArrayList<Chat>();
        chatHistory = main.returnChatHistory(sender, main.getSubscriber(getClientID(data)));

        for(int i = 0; i < chatHistory.size(); i++)
        {
            //Creating the JSON packet
            HashMap<String, String> past_chat = new HashMap<>();
            past_chat.put("CLIENT-ID", sender.clientID);
            past_chat.put("CHAT-MESSAGE", chatHistory.get(i).getMessage());

            HistoryResponseMessage historyResponse = new HistoryResponseMessage();
            TCPWorker worker = main.getTCPWorker(sender);
            worker.send(historyResponse.stringify(past_chat));
        }
        return new TCPResponse(false, "");
    }
}
