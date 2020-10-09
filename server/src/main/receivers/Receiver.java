package main.receivers;

import main.Main;

public abstract class Receiver
{
    public String receiver;
    public String[] fields;

    public Receiver(String receiver, String[] fields)
    {
        this.receiver = receiver;
        this.fields = fields;
    }

    public boolean receivable(JSONData data)
    {
        return data.receivable(this);
    }

    public abstract String action(Main main, JSONData data);
}
