package main.messages;

public class EndRequestMessage extends Message {
    public EndRequestMessage () {
        super("END-REQUEST", new String[] {"SESSION-ID"});
    }
}
