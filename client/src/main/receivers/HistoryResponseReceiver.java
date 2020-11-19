package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class HistoryResponseReceiver extends Receiver {
    public HistoryResponseReceiver()
    {
        super("HISTORY-RESP", new String[] {"CLIENT-ID", "CHAT-MESSAGE", "SESSION-ID"});
    }

    public String getSendingClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID");
    }
    public String getChatMessage(JSONData data)
    {
        return data.data.get("CHAT-MESSAGE");
    }
    public String getSessionIDMessage(JSONData data)
    {
        return data.data.get("SESSION-ID");
    }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println(getSendingClientID(data) + ": " + getChatMessage(data));
        return new TCPResponse(false, "HISTORY_VIEWED");
    }


}
