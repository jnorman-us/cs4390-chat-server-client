package main.messages;

public class UnreachableMessage extends Message
{
    public UnreachableMessage()
    {
        super("UNREACHABLE", new String[] { "CLIENT-ID-B" });
    }
}
