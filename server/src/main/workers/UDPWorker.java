package main.workers;

import main.receivers.JSONData;
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
        socket = new DatagramSocket(port);
        receivers = new Receiver[]{ new HelloReceiver() };
    }

    @Override
    public void run()
    {
        byte[] received = new byte[65535];
        DatagramPacket datagramPacket = null;

        while(true)
        {
            System.out.println(socket.getLocalAddress());
            datagramPacket = new DatagramPacket(received, received.length);
            try {
                socket.receive(datagramPacket);

                String parsed = parse(received);
                JSONData data = new JSONData(parsed);

                System.out.println(parsed);

                for(Receiver receiver : receivers)
                {
                    if(receiver.receivable(data))
                    {
                        receiver.action(main, data);
                    }
                }
            } catch (IOException exception) {
                System.out.println("IO Exception");
                continue;
            }
        }
    }

    public String parse(byte[] received)
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

        return raw.toString();
    }
}
