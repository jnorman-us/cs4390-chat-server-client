package main.receivers;

import org.json.simple.parser.ParseException;

public class HelloReceiver extends Receiver
{
    public HelloReceiver()
    {
        super("HELLO", new String[]{ "CLIENT-ID-A" });
    }

    public String getClientIDA()
    {
        return json_data.get("CLIENT-ID-A");
    }
}
