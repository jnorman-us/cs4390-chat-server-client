package main.receivers;

import main.Main;
import main.messages.ChatMessage;
import main.messages.ChatRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;
import main.objects.UDPResponse;

import java.util.HashMap;
import java.util.Scanner;


//WORK IN PROGRESS
public class ChatStartedReceiver extends Receiver {
    public ChatStartedReceiver()
    {
        super("CHAT-STARTED", new String[]{"CLIENT-ID-B"});

    }
    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID-B");
    }

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        System.out.println("Chat started with " + getClientID(data) + ". Send the first message when you're ready.");

        // {"receiver":"CHAT","CHAT-MESSAGE":""}

        System.out.print("Me: ");
        Scanner scan = new Scanner(System.in);  //take user input for chat message
        String userMessage = scan.nextLine();

        //System.out.println(getClientID(data) + userInput);

        //send chat message to other client (via the server)
        ChatMessage chatMessage = new ChatMessage();
        HashMap<String, String> message_data = new HashMap<>();
        message_data.put("CHAT-MESSAGE", userMessage);
        return new TCPResponse(false, chatMessage.stringify(message_data));

    }
}
