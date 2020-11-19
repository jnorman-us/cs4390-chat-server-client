package main.messages;

public class ChatMessage extends Message{
    public ChatMessage() {
        super("CHAT", new String[] {"SESSION-ID", "CHAT-MESSAGE"});
    }
}
