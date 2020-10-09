package main.workers;

import main.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPWorker implements Runnable
{
    private Main main;
    private ServerSocket serverSocket;

    private Socket clientSocket;

    public TCPWorker(Main main, int port) throws IOException
    {
        this.main = main;
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run()
    {
        System.out.println("Started the TCP server on " + serverSocket.getLocalPort());
    }
}
