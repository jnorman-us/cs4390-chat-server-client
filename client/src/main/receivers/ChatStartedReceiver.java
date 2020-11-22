package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.EndRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;

import java.util.HashMap;
import java.util.Scanner;


//WORK IN PROGRESS
public class ChatStartedReceiver extends Receiver {
    public ChatStartedReceiver()
    {
        super("CHAT-STARTED", new String[]{"SESSION-ID", "CLIENT-ID-B"});

    }

    public String getSessionID(JSONData data) { return data.data.get("SESSION-ID"); }
    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID-B");
    }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println("Chat started with " + getClientID(data) + ". Send the first message when you're ready." +
                "\n\tSince we are using block Read Sockets, you must first send a message before you can receive a message");

        // {"receiver":"CHAT","CHAT-MESSAGE":""}

        System.out.print("Me: ");
        Scanner scan = new Scanner(System.in);  //take user input for chat message
        String userMessage = scan.nextLine();

        //ACCOUNT FOR END REQUESTS
        if((userMessage.toLowerCase().trim()).equals("end chat")) {
            System.out.println("ending chat... SessionID was " + getSessionID(data));

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
