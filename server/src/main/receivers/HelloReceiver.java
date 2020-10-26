package main.receivers;

import main.Main;
import main.messages.AuthFailMessage;
import main.messages.AuthSuccessMessage;
import main.objects.TCPResponse;
import main.objects.Subscriber;
import main.objects.UDPResponse;

import java.util.HashMap;

public class HelloReceiver extends Receiver
{
    public HelloReceiver()
    {
        super("HELLO", new String[]{ "CLIENT-ID-A" });
    }

    public String getClientID(JSONData data)
    {
        return data.data.get("CLIENT-ID-A");
    }

    @Override
    public UDPResponse action(Main main, Subscriber nobody, JSONData data)
    {
        Subscriber subscriber = main.getSubscriber(getClientID(data));

        if(subscriber != null)
        {
            subscriber.generateRandomCookie();
            subscriber.generatePortNumber();

            // attempt to generate a TCP worker for this subscriber
            if(main.createTCPWorker(subscriber))
            {
                HashMap<String, String> message_data = new HashMap<>();
                message_data.put("RAND-COOKIE", subscriber.rand_cookie);
                message_data.put("PORT-NUMBER", "" + subscriber.port);

                AuthSuccessMessage authSuccessMessage = new AuthSuccessMessage();
                if(authSuccessMessage.sendAble(message_data))
                {
                    return new UDPResponse(authSuccessMessage.stringify(message_data));
                }
            }
        }
        AuthFailMessage authFailMessage = new AuthFailMessage();
        return new UDPResponse(authFailMessage.stringify(new HashMap<>()));
    }
}
