package main.receivers;

import main.Main;
import main.messages.AuthFailMessage;
import main.messages.AuthSuccessMessage;
import main.objects.Subscriber;
import main.objects.UDPResponse;

import java.util.HashMap;

public class ResponseReceiver extends Receiver
{
    public ResponseReceiver()
    {
        super("RESPONSE", new String[]{ "CLIENT-ID", "RES" });
    }

    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID");
    }

    public String getResponse(JSONData data)
    {
        return data.data.get("RES");
    }

    @Override
    public UDPResponse action(Main main, Subscriber nobody, JSONData data)
    {
        Subscriber subscriber = main.getSubscriber(getClientID(data));
        String response = getResponse(data);

        if(subscriber != null && subscriber.checkXRES(response))
        {
            // random cookie is generated in the HELLO step, as the CHALLENGE
            subscriber.generatePortNumber();

            // attempt to generate a TCP worker for this subscriber
            if(main.createTCPWorker(subscriber))
            {
                HashMap<String, String> message_data = new HashMap<>();
                message_data.put("RAND-COOKIE", subscriber.randomCookie);
                message_data.put("PORT-NUMBER", "" + subscriber.port);

                AuthSuccessMessage authSuccessMessage = new AuthSuccessMessage();
                if(authSuccessMessage.sendAble(message_data))
                {
                    return new UDPResponse(authSuccessMessage.stringify(message_data));
                }
                return new UDPResponse("Failed to send");
            }
        }

        AuthFailMessage authFailMessage = new AuthFailMessage();
        return new UDPResponse(authFailMessage.stringify(new HashMap<>()));
    }
}
