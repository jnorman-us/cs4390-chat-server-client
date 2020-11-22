package main.objects;

public class Chat
{
    private String message;
    private long time;
    private Subscriber sender;
    private Subscriber receiver;
    private Session session;

    //Create new chat with message, sender and receiver
    public Chat(String message, Subscriber sender, Subscriber receiver, Session session)
    {
        this.message = message;
        this.time = System.currentTimeMillis();
        this.sender = sender;
        this.receiver = receiver;
        this.session = session;
    }

    //Get methods
    public String getMessage() { return message; }
    public long getTime() { return time; }
    public Session getSession() { return session; }
    public Subscriber getSender() { return sender; }
    public Subscriber getReceiver() { return receiver; }


    public String toString()
    {
        return "<" + session.getId() + "> " + sender.clientID + ": " + message + " ➤ " + receiver.clientID;
    }
}
