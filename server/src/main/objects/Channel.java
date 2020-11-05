package main.objects;

public class Channel {
    private Subscriber[] subscribers;       //People involved in chat
    private Chat[] chats;                   //All chats in channel

    public Channel(Subscriber sub1, Subscriber sub2)
    {
        this.subscribers = new Subscriber[]{sub1, sub2};
    }
    public Channel(Subscriber[] subscribers)
    {
        this.subscribers = subscribers;
    }

    //Get methods
    public Subscriber[] getSubscribers() { return subscribers; }
    public Chat[] getAllChats() { return chats; }
    //Consider adding more specific get methods for returning chats
}
