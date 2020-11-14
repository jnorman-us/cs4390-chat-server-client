package main.receivers;

import main.Main;
import main.objects.Subscriber;
import main.objects.UDPResponse;

public class AuthFailReceiver extends Receiver {
    public AuthFailReceiver() {
        super("AUTH-FAIL", new String[]{});
    }
    public UDPResponse action(Main main, Subscriber nobody, JSONData data) {
        System.out.println("AUTHENTICATION FAILED");
        return null;
    }

}
