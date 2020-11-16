package main.objects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Subscriber
{
    public static int last_port = 8001;

    public String clientID;
    public String K_A; // the client's secret key

    public String randomCookie;
    public int port;

    // connected means if the socket was properly accepted AND
    // the client had the proper RAND-COOKIE
    private AtomicBoolean connected;

    public Subscriber(String id, String K_A)
    {
        clientID = id;
        this.K_A = K_A;

        randomCookie = "";
        port = -1;
        connected = new AtomicBoolean(false);
    }

    public boolean checkRandomCookie(String toCheck)
    {
        return randomCookie.equals(toCheck);
    }

    public boolean checkXRES(String res)
    {
        // run the MD5 algorithm here to calculate XRES
        String XRES = MD5(randomCookie, K_A);
        // then confirm that XRES == res
        System.out.println(XRES + " " + res);
        return (XRES.equals(res));
    }

    public String MD5(String rand, String ka)
    {
        String key = rand + " " + ka;

        System.out.println(key);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(key.getBytes());
            byte[] digest = md.digest();
            BigInteger no = new BigInteger(1, digest);
            String ht = no.toString(16);
            while (ht.length() < 32) {
                ht = "0" + ht;
            }
            return ht;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateRandomCookie()
    {
        final int length = 10;

        randomCookie = "";

        for(int i = 0; i < length; i ++)
        {
            randomCookie += (int)(Math.random() * 10);
        }
    }

    public void generatePortNumber()
    {
        port = last_port;
        last_port ++;
    }

    public void connect()
    {
        connected.set(true);
    }

    public void disconnect()
    {
        connected.set(false);
    }

    public boolean connected()
    {
        return connected.get();
    }

    public boolean equals(Subscriber other)
    {
        return clientID.equals(other.clientID);
    }

    public String toString()
    {
        return clientID + ": <connected: " + connected + ">";
    }
}
