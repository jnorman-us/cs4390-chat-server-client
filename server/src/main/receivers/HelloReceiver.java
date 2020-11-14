package main.receivers;

import main.Main;
import main.messages.ChallengeMessage;
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

            HashMap<String, String> message_data = new HashMap<>();
            message_data.put("RAND", subscriber.randomCookie);

            ChallengeMessage templateChallengeMessage = new ChallengeMessage();
            if(templateChallengeMessage.sendAble(message_data))
            {
                return new UDPResponse(templateChallengeMessage.stringify(message_data));
            }
            return new UDPResponse("Failed to send");
        }
        return new UDPResponse("NOT A REGISTERED SUBSCRIBER");
    }
}
