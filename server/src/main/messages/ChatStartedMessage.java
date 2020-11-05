package main.messages;

public class ChatStartedMessage extends Message
{
    public ChatStartedMessage()
    {
        super("CHAT-STARTED", new String[] { "CLIENT-ID-B" });
    }
}
