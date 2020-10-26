package main.workers;

import main.Main;
import main.objects.TCPResponse;
import main.objects.Subscriber;
import main.receivers.ConnectReceiver;
import main.receivers.JSONData;
import main.receivers.Receiver;
import main.receivers.ShoutReceiver;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable
{
    private static Receiver[] receivers = new Receiver[]{
            new ConnectReceiver(),
            new ShoutReceiver()
    };

    private Main main;
    private Subscriber subscriber;

    private Thread thread;
    private AtomicBoolean running;
    private AtomicBoolean accepted;

    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter writer;

    public TCPWorker(Main main, Subscriber subscriber) throws IOException
    {
        this.main = main;
        this.subscriber = subscriber;

        if(subscriber.port < 0)
            throw new IOException();
        serverSocket = new ServerSocket(subscriber.port);

        thread = new Thread(this);
        running = new AtomicBoolean(false);
        accepted = new AtomicBoolean(false);
    }

    public void start()
    {
        running.set(true);
        accepted.set(false);
        thread.start();
    }

    public void stop()
    {
        running.set(false);
        accepted.set(false);
        try {
            System.out.println("Closing the socket");
            serverSocket.close();
            if(isAccepted())
                socket.close();
        } catch(IOException exception) {
            System.out.println("Failed to close TCP server on " + serverSocket.getLocalPort());
        }
    }

    // tells the main that this worker wants to stop
    public void requestStop()
    {
        main.stopTCPWorker(subscriber);
    }

    @Override
    public void run()
    {
        System.out.println("Started the TCP server on " + serverSocket.getLocalPort());

        try {
            socket = serverSocket.accept();
            accepted.set(true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

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
                            TCPResponse response = (TCPResponse)receiver.action(main, subscriber, data);
                            writer.println(response.message);

                            if(response.kick)
                            {
                                requestStop();
                            }
                        }
                    }
                }
                else requestStop();
            }
            requestStop();
        } catch(IOException exception) {
            System.out.println("TCP server on " + serverSocket.getLocalPort() + " experienced IO exception");
        }

        System.out.println("Stopping the TCP server2 on " + serverSocket.getLocalPort());
    }

    public boolean send(String message)
    {
        if(accepted.get())
        {
            writer.println(message);
            return true;
        }
        return false;
    }

    public boolean isRunning()
    {
        return running.get();
    }

    public boolean isAccepted()
    {
        return accepted.get();
    }
}
