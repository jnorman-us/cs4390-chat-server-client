package main.workers;

import main.Main;
import main.messages.Message;
import main.objects.TCPResponse;
import main.objects.Subscriber;
import main.receivers.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPWorker implements Runnable
{
    private static Receiver[] receivers = new Receiver[]{
            new ConnectReceiver(),
            new ChatRequestReceiver(),
            new ChatReceiver(),
            new EndRequestReceiver(),
            new HistoryRequestReceiver()
    };

    private Main main;
    private Subscriber subscriber;

    private Thread thread;
    private AtomicBoolean running;

    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter writer;

    private SecretKeySpec aesKey;
    private Cipher cipher;

    public TCPWorker(Main main, Subscriber subscriber) throws IOException
    {
        this.main = main;
        this.subscriber = subscriber;

        if(subscriber.port < 0)
            throw new IOException();
        serverSocket = new ServerSocket(subscriber.port);

        thread = new Thread(this);
        running = new AtomicBoolean(false);

        try {
            aesKey = new SecretKeySpec(this.subscriber.CK_A.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public void start()
    {
        running.set(true);
        thread.start();
    }

    public boolean isRunning()
    {
        return running.get();
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
                parsed = reader.readLine();
                //System.out.println("TCPWorker for " + subscriber.clientID + " received: " + parsed);
                parsed = decrypt(parsed);
                //System.out.println("\t➤ " + parsed);

                if(parsed != null)
                {
                    JSONData data = new JSONData(parsed);

                    for(Receiver receiver : receivers)
                    {
                        if(receiver.receivable(data))
                        {
                            TCPResponse response = (TCPResponse)receiver.action(main, subscriber, data);
                            byte[] messageBytes = response.message.getBytes();
                            send(response.message);

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
        //System.out.println("TCPWorker for " + subscriber.clientID + " sending: " + message);
        message = encrypt(message);
        //System.out.println("\t➤ " + message);
        if(socket != null && socket.isConnected())
            writer.println(message);
    }

    public String encrypt(String message)
    {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);

            byte[] values = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(values);
        } catch (InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String decrypt(String encrypted)
    {
        try {
            cipher.init(Cipher.DECRYPT_MODE, aesKey);

            byte[] values = Base64.getDecoder().decode(encrypted);
            return new String(cipher.doFinal(values));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String toString()
    {
        return (isRunning() ? "Active" : "Inactive") + "Worker belonging to: " + subscriber.toString() + " on port " + subscriber.port;
    }
}
