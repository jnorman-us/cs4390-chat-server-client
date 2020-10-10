package main.receivers;

import main.Main;

public abstract class Receiver
{
    public String receiver; // the value of the "receiver" that this Receiver is expecting
    public String[] fields; // the keys of the data that this receiver is expecting

    // called by the Children to set which receiver and fields is expected in the JSON
    public Receiver(String receiver, String[] fields)
    {
        this.receiver = receiver;
        this.fields = fields;
    }

    // returns if the JSONData is properly formatted to be received by this Instance of the Receiver Class
    public boolean receivable(JSONData data)
    {
        return data.receivable(this);
    }

    public abstract String action(Main main, JSONData data);
}
