package main.messages;

public class ChatStartedMessage extends Message
{
    public ChatStartedMessage()
    {
        super("CHAT-STARTED", new String[] { "SESSION-ID", "CLIENT-ID-B" });
    }
}
