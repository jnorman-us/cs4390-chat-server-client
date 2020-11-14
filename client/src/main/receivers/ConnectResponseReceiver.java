package main.receivers;

import main.Main;
import main.messages.ChatRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;

import java.util.HashMap;
import java.util.Scanner;

public class ConnectResponseReceiver extends Receiver {
    public ConnectResponseReceiver()
    {
        super("CONNECTED", new String[] {});
    }

    public String getMessage(JSONData data) {return null;}

    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        Scanner scan = new Scanner(System.in);
        String userInput = "";
        String clientID = "";
        boolean chatRequested = false;

        System.out.println("NOW CONNECTED TO THE SERVER.");

        System.out.println("1) To INITIATE a connection to another client, enter Chat <client-id>");
        System.out.println("2) To LISTEN for a connection instead, enter listen");
        userInput = scan.nextLine();

        //if user submits a chat request, split the string to get the client that the user wants to connect to
        if (userInput.toLowerCase().contains("chat")) {
            clientID = userInput.substring(userInput.indexOf("t") + 2);
            clientID.trim();
            chatRequested = true;
            System.out.println("attempting to connect to client: " + clientID);

            // INITIATE chat response
            ChatRequestMessage chatRequestMessage = new ChatRequestMessage();

            HashMap<String, String> message_data = new HashMap<>();
            message_data.put("CLIENT-ID-B", clientID);

            return new TCPResponse(false, chatRequestMessage.stringify(message_data));
        }
        else {
            return null;
        }

    }
}
