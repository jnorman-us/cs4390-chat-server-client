package main.receivers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

/*
*   Helper Class for the receivers that takes in a String that was sent to the client.
*   It parses it to check if it was properly formatted as a JSON. Then this data can be accepted into a Receiver.
*/
public class JSONData
{
    // once the string is parsed in, this boolean will be true if the string was properly parsed in
    public boolean valid;

    public String receiver; // the receiver that the parsed string claimed to have
    public HashMap<String, String> data; // the payload stored inside the string

    // this constructor takes in the String and parses it from the String JSON into a HashMap
    // if the JSON is incorrectly formatted, then it will not parse properly and will be marked as valid=false
    public JSONData(String toParse)
    {
        JSONParser parser = new JSONParser();

        // sets up the defaults for these values
        valid = true;
        receiver = "";
        data = new HashMap<>();

        try {
            JSONObject json_obj = (JSONObject) parser.parse(toParse);

            if (json_obj.containsKey("receiver"))
            {
                // grabs the value at the key "receiver" inside the JSON
                receiver = json_obj.get("receiver").toString();
            }
            else {
                valid = false; // not valid if there is no receiver
                return;
            }

            // fills up the data HashMap with the <key, value> pairs in the JSON,
            // except for what's already in the receiver
            for(Object key : json_obj.keySet())
            {
                if(key.equals("receiver")) continue;
                data.put(key.toString(), json_obj.get(key).toString());
            }
        } catch (ParseException exception) {
            // not valid if the string was not of proper JSON format
            valid = false;
        }
    }

    // function used either in TCPWorker/UDPWorker to check if the JSON data is properly formatted to be received
    // by the Receiver Class Instance
    public boolean receivable(Receiver receiver)
    {
        if(!valid) return false;
        if(!this.receiver.equals(receiver.receiver)) return false;
        for(String field : receiver.fields)
        {
            if(!data.containsKey(field))
                return false;
        }
        return true;
    }

    // debugging function to spit the receiver and the contents of data in order to be printed
    public String toString()
    {
        return "Receiver: " + receiver + "\n" + data.toString();
    }
}
