package main.messages;

/*
 * Response to CHALLENGE message
 */
public class ResponseMessage extends Message {
    public ResponseMessage() {
        super("RESPONSE", new String[]{"CLIENT-ID", "RES"});
    }
}
