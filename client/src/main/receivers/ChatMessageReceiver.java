package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.EndRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;

import java.util.HashMap;
import java.util.Scanner;

public class ChatMessageReceiver extends Receiver {
    public ChatMessageReceiver() {
        super("CHAT", new String[]{"SESSION-ID", "CHAT-MESSAGE"});

    }

    public String getSessionID(JSONData data) { return data.data.get("SESSION-ID"); }

    //get the reply of the other client
    public String getChatMessage(JSONData data)
    {
        return data.data.get("CHAT-MESSAGE");
    }

    /*
     * 1) Print received message
     * 2) take user input for reply message to send
     * 3) send reply message
     */
    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        // {"receiver":"CHAT","CHAT-MESSAGE":""}

        // 1) print received message
        System.out.println(getChatMessage(data));

        // 2) take user input for reply message to send

        System.out.print("Me: ");
        Scanner scan = new Scanner(System.in);  //take user input for chat message
        String userMessage = scan.nextLine();

        //ACCOUNT FOR END REQUESTS
        if((userMessage.toLowerCase().trim()).equals("end chat")) {
            System.out.println("CHAT ENDED... SessionID was " + getSessionID(data));

            //send EndRequestMessage to server
            EndRequestMessage endRequestMessage = new EndRequestMessage();
            HashMap<String, String> message_data = new HashMap<>();
            message_data.put("SESSION-ID", getSessionID(data));
            return new TCPResponse(false, endRequestMessage.stringify(message_data));
        }

        //send chat message to other client (via the server)
        ChatMessage chatMessage = new ChatMessage();
        HashMap<String, String> message_data = new HashMap<>();
        message_data.put("SESSION-ID", getSessionID(data));
        message_data.put("CHAT-MESSAGE", userMessage);
        return new TCPResponse(false, chatMessage.stringify(message_data));

    }
}
