package main.receivers;

import main.Main;
import main.messages.AuthFailMessage;
import main.messages.AuthSuccessMessage;
import main.objects.Subscriber;

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
    public String action(Main main, JSONData data)
    {
        Subscriber subscriber = main.getSubscriber(getClientID(data));

        if(subscriber != null)
        {
            subscriber.generateRandomCookie();
            subscriber.generatePortNumber();

            if(subscriber.generateTCPWorker(main))
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
        return authFailMessage.stringify(new HashMap<>());
    }
}
