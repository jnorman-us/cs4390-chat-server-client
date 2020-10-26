package main.workers;

import main.Main;
import main.objects.TCPResponse;
import main.objects.UDPResponse;
import main.receivers.HelloReceiver;
import main.receivers.JSONData;
import main.receivers.Receiver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class UDPWorker implements Runnable
{
    private static Receiver[] receivers = new Receiver[] { new HelloReceiver() };
    // UDP server instance
    private Main main;

    private Thread thread;
    private AtomicBoolean running;

    private DatagramSocket socket;

    public UDPWorker(Main main, int port) throws IOException
    {
        this.main = main;
        socket = new DatagramSocket(port);

        thread = new Thread(this);
        running = new AtomicBoolean(false);
    }

    public void start()
    {
        running.set(true);
        thread.start();
    }

    public void stop()
    {
        running.set(false);
        socket.close();
    }

    @Override
    public void run()
    {
        byte[] received = new byte[65535];
        DatagramPacket datagramPacket = null;

        while(running.get())
        {
            datagramPacket = new DatagramPacket(received, received.length);
            try {
                socket.receive(datagramPacket);

                String parsed = parse(received);
                JSONData data = new JSONData(parsed);

                for(Receiver receiver : receivers)
                {
                    if(receiver.receivable(data))
                    {
                        UDPResponse response = receiver.action(main, null, data);
                        byte[] bytes_to_send = response.message.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(
                                bytes_to_send,
                                bytes_to_send.length,
                                datagramPacket.getAddress(),
                                datagramPacket.getPort()
                        );
                        socket.send(sendPacket);
                    }
                }
                Arrays.fill(received, (byte)0);
            } catch (IOException exception) {
                System.out.println("IO Exception");
                break;
            }
        }
    }

    public String parse(byte[] received)
    {
        if(received == null)
            return null;

        StringBuilder raw = new StringBuilder();
        int i = 0;
        while(received[i] != (byte)0)
        {
            raw.append((char)received[i]);
            i ++;
        }

        return raw.toString();
    }
}
