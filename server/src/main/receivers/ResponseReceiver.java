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

    @Override
    public UDPResponse action(Main main, Subscriber nobody, JSONData data)
    {
        //Subscriber subscriber = main.getSubscriber(getClientID(data));

        if (subscriber != null)
        {
            subscriber.generateRandomCookie();
            subscriber.generatePortNumber();

            if (subscriber.generateTCPWorker(main))
            {
                HashMap<String, String> message_data = new HashMap<>();
                message_data.put("RAND-COOKIE", subscriber.rand_cookie);
                message_data.put("PORT-NUMBER", "" + subscriber.port);

                AuthSuccessMessage authSuccessMessage = new AuthSuccessMessage();
                if(authSuccessMessage.sendAble(message_data))
                {
                    return authSuccessMessage.stringify(message_data);
                }
            }
        }
        AuthFailMessage authFailMessage = new AuthFailMessage();
        return new UDPResponse(authFailMessage.stringify(new HashMap<>()));
    }

    @Override
    public UDPResponse action(Main main, Subscriber subscriber, JSONData data) {
        return null;
    }
}
