package main.messages;

public class HistoryRequestMessage extends Message {
        public HistoryRequestMessage() {
            super("HISTORY-REQ", new String[]{"CLIENT-ID-B"});
        }
}
