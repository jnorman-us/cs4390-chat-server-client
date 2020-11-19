package main.receivers;

import main.Main;
import main.messages.ChatRequestMessage;
import main.messages.HistoryRequestMessage;
import main.objects.Subscriber;
import main.objects.TCPResponse;

import java.util.HashMap;
import java.util.Scanner;

public class ConnectedReceiver extends Receiver {
    public ConnectedReceiver()
    {
        super("CONNECTED", new String[] {});
    }


    public TCPResponse action(Main main, Subscriber subscriber, JSONData data) {
        Scanner scan = new Scanner(System.in);
        String userInput = "";
        String clientID = "";
        boolean chatRequested = false;

        System.out.println("YOU ARE CONNECTED TO THE SERVER.");

        System.out.println("➤\tTo INITIATE a chat session: enter Chat <client-id>");
        System.out.println("➤\t\tIf someone else sent you a chat request, press ENTER to begin chatting.");
        System.out.println("➤\tTo get Chat History:  History <client-id>");
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
        else if (userInput.toLowerCase().contains("history")) {
            clientID = userInput.substring(userInput.indexOf("y") + 2);
            clientID.trim();
            System.out.println("requesting history from server... " + clientID);

            // INITIATE chat response
            HistoryRequestMessage chatRequestMessage = new HistoryRequestMessage();
            HashMap<String, String> message_data = new HashMap<>();
            message_data.put("CLIENT-ID-B", clientID);

            return new TCPResponse(false, chatRequestMessage.stringify(message_data));

        }
        else {
            System.out.println("waiting to receive a connection request...");
            return new TCPResponse(false, "");
        }

    }
}
