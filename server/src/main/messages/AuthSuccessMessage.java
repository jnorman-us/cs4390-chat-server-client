package main.messages;

public class AuthSuccessMessage extends Message
{
    public AuthSuccessMessage()
    {
        super("AUTH-SUCCESS", new String[] { "RAND-COOKIE", "PORT-NUMBER"});
    }
}
