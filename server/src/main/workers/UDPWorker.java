package main.workers;

import main.Main;
import main.receivers.HelloReceiver;
import main.receivers.Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPWorker implements Runnable
{
    // UDP server instance
    private Main main;

    private DatagramSocket socket;
    private Receiver[] receivers;

    public UDPWorker(Main main, int port) throws IOException
    {
        this.main = main;
        socket = new DatagramSocket();
        receivers = new Receiver[]{ new HelloReceiver() };
    }

    @Override
    public void run()
    {
        byte[] received = new byte[65535];
        DatagramPacket datagramPacket = null;

        while(true)
        {
            datagramPacket = new DatagramPacket(received, received.length);
            try {
                socket.receive(datagramPacket);

                Receiver receiver = receive(received);
            } catch (IOException exception) {
                System.out.println("IO Exception");
                continue;
            }


        }
    }

    public Receiver receive(byte[] received)
    {
        if(received == null)
            return null;

        StringBuilder raw = new StringBuilder();
        int i = 0;
        while(received[i] != 0);
        {
            raw.append(received[i]);
            i ++;
        }
        String toParse = raw.toString();

        for(Receiver receiver : receivers)
        {
            if(receiver.parse(toParse))
            {
                return receiver;
            }
        }
        return null;
    }
}
