package main.objects;

public class Chat
{
    private String message;
    private long time;
    private Subscriber sender;
    private Subscriber receiver;
    private Session session;
    private enum StatusMessages{SUCCESS, FAILED};
    private StatusMessages Status;

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
    public StatusMessages getStatus() { return Status; }

    public void setStatus(String status)
    {
        if(status.equalsIgnoreCase("SUCCESS"))
            Status = StatusMessages.SUCCESS;
        else if(status.equalsIgnoreCase("FAILED"))
            Status = StatusMessages.FAILED;
    }


}
