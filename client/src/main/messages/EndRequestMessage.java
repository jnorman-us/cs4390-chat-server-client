package main.messages;

public class EndRequestMessage extends Message {
    public EndRequestMessage () {
        super("END_REQUEST", new String[] {"SESSION-ID"});
    }
}
