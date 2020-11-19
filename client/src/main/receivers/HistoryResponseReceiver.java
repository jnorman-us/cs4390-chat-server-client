package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.TCPResponse;

public class HistoryResponseReceiver extends Receiver {
    public HistoryResponseReceiver()
    {
        super("HISTORY-RESP", new String[] {"CLIENT-ID", "CHAT-MESSAGE", "SESSION-ID", "LAST"});
    }

    public String getSendingClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID");
    }
    public String getChatMessage(JSONData data)
    {
        return data.data.get("CHAT-MESSAGE");
    }
    public String getSessionID(JSONData data)
    {
        return data.data.get("SESSION-ID");
    }
    public String getLast(JSONData data)
    {
        return data.data.get("LAST");
    }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {

        if(getLast(data).toUpperCase().equals("TRUE")) {
            return new TCPResponse(false, "HISTORY_VIEWED");
        }
        else {
            System.out.println("SessionID: " + getSessionID(data) + "\t Sender: " +
                    getSendingClientID(data) + "\tMessage: " + getChatMessage(data));
            return new TCPResponse(false, "");
        }
    }


}
