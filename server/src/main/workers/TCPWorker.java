package main.workers;

import main.Main;
import main.objects.Subscriber;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable
{
    private Main main;
    private Subscriber subscriber;

    private Thread thread;
    private ServerSocket serverSocket;
    private Socket socket;
    private DataInputStream in;
    private AtomicBoolean running;

    public TCPWorker(Main main, Subscriber subscriber) throws IOException
    {
        this.main = main;
        this.subscriber = subscriber;

        if(subscriber.port < 0)
            throw new IOException();
        serverSocket = new ServerSocket(subscriber.port);
        running = new AtomicBoolean(false);
    }

    public void start()
    {
        thread = new Thread(this);
        running.set(true);
        thread.start();
    }

    public void stop()
    {
        running.set(false);
        try {
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
            System.out.println("accepted");
            //in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            String line = "";
            while(running.get())
            {
                //System.out.println(in.readUTF());

                if(socket.isClosed())
                    break;
            }
        } catch(IOException exception) {
            System.out.println("TCP server on " + serverSocket.getLocalPort() + " experienced IO exception");
        }

        System.out.println("Stopping the TCP server on " + serverSocket.getLocalPort());
    }
}
