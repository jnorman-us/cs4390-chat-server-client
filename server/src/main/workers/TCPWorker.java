package main.workers;

import main.Main;
import main.messages.Message;
import main.objects.TCPResponse;
import main.objects.Subscriber;
import main.receivers.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable
{
    private static Receiver[] receivers = new Receiver[]{
            new ConnectReceiver(),
            new ShoutReceiver(),
            new ChatRequestReceiver()
    };

    private Main main;
    private Subscriber subscriber;

    private Thread thread;
    private AtomicBoolean running;

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
            if(socket != null && socket.isConnected())
                socket.close();
            subscriber.disconnect();
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

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            String parsed = "";

            while(running.get())
            {
                System.out.println(parsed);
                parsed = reader.readLine();
                System.out.println(parsed);
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

    public void send(String message)
    {
        if(socket != null && socket.isConnected())
            writer.println(message);
    }

    public boolean isRunning()
    {
        return running.get();
    }

    public String toString()
    {
        return (isRunning() ? "Active" : "Inactive") + "Worker belonging to: " + subscriber.toString() + " on port " + subscriber.port;
    }
}
