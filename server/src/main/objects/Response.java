package main.objects;

public class Response
{
    public boolean kick;
    public String message;

    public Response(boolean kick, String message)
    {
        this.kick = kick;
        this.message = message;
    }

    public String toString()
    {
        return "Kicking: " + kick + "\n"
                + "Message: " + message;
    }
}
