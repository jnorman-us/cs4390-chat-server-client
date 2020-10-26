package main.objects;

public class UDPResponse
{
    public String message;

    public UDPResponse(String message)
    {
        this.message = message;
    }

    public String toString()
    {
        return "Message: " + message;
    }
}
