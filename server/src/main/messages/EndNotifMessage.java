package main.messages;

public class EndNotifMessage extends Message
{
    public EndNotifMessage()
    {
        super("END-NOTIF", new String[] { "SESSION-ID"});
    }
}
