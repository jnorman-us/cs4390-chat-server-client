package main.messages;

public class ChatRequestMessage extends Message{
    public ChatRequestMessage()
    {
        super("CHAT-REQUEST", new String[] {"CLIENT-ID-B"});
    }
}
