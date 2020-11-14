package main.messages;

public class ChallengeMessage extends Message
{
    public ChallengeMessage()
    {
        super("CHALLENGE", new String[] { "RAND" });
    }
}
