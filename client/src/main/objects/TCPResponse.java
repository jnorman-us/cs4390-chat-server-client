package main.objects;

public class TCPResponse extends UDPResponse
{
    public boolean kick;

    public TCPResponse(boolean kick, String message)
    {
        super(message);

        this.kick = kick;
    }

    public String toString()
    {
        return "Kicking: " + kick + "\n"
                + "Message: " + message;
    }
}
