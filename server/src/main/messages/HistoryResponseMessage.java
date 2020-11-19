package main.messages;

//Sent by the server to the client requesting the history
public class HistoryResponseMessage extends Message
{
    public HistoryResponseMessage()
    {
        super("HISTORY_RESP", new String[] { "CLIENT-ID", "CHAT-MESSAGE", "SESSION-ID", "LAST" });
    }
}
