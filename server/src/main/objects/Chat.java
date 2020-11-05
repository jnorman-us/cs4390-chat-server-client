package main.objects;

public class Chat
{
    private Subscriber sender;
    private String chat;

    public Chat(Subscriber sender, String chat)
    {
        this.sender = sender;
        this.chat = chat;
    }

    public Subscriber getSender()
    {
        return sender;
    }

    public String getChat()
    {
        return chat;
    }
}
