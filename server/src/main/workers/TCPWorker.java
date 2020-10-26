package main.workers;

import main.Main;
import main.objects.Response;
import main.objects.Subscriber;
import main.receivers.ConnectReceiver;
import main.receivers.JSONData;
import main.receivers.Receiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable
{
    private static Receiver[] receivers = new Receiver[]{ new ConnectReceiver() };

    private Main main;
    private Subscriber subscriber;

    private Thread thread;
    private AtomicBoolean running;

    private ServerSocket serverSocket;
    private Socket socket;

    public TCPWorker(Main main, Subscriber subscriber) throws IOException
    {
        this.main = main;
        this.subscriber = subscriber;

        if(subscriber.port < 0)
            throw new IOException();
        serverSocket = new ServerSocket(subscriber.port);

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
        try {
            System.out.println("Closing the socket");
            serverSocket.close();
        } catch(IOException exception) {
            System.out.println("Failed to close TCP server on " + serverSocket.getLocalPort());
        }
    }

    @Override
    public void run()
    {
        System.out.println("Started the TCP server on " + serverSocket.getLocalPort());

        try {
            socket = serverSocket.accept();
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String parsed = "";

            while(running.get())
            {
                parsed = reader.readLine();
                if(parsed != null)
                {
                    JSONData data = new JSONData(parsed);

                    for(Receiver receiver : receivers)
                    {
                        if(receiver.receivable(data))
                        {
                            Response response = receiver.action(main, data);

                            System.out.println(response);
                        }
                    }
                }
                else main.stopTCPWorker(subscriber);
            }


        } catch(IOException exception) {
            System.out.println("TCP server on " + serverSocket.getLocalPort() + " experienced IO exception");
        }

        System.out.println("Stopping the TCP server2 on " + serverSocket.getLocalPort());
    }
}
