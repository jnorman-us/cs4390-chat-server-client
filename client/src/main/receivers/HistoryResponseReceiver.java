package main.receivers;

import main.Main;
import main.messages.HistoryRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class HistoryResponseReceiver extends Receiver {
    public HistoryResponseReceiver()
    {
        super("HISTORY_RESP", new String[] {"SENDING-CLIENT-ID", "CHAT-MESSAGE"});
    }

    public String getSendingClientID(JSONData data)
    {
        return data.data.get("SENDING-CLIENT-ID");
    }
    public String getChatMessage(JSONData data)
    {
        return data.data.get("CHAT-MESSAGE");
    }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println(getSendingClientID(data) + ": " + getChatMessage(data));
        return new TCPResponse(false, "");
    }


}
