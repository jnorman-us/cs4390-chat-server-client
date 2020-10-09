package main.receivers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;

/*
*   Helper Class for the receivers that parses all of the JSON data received
*   from the TCP/UDP and
*/
public class JSONData
{
    public boolean valid;

    public String receiver;
    public HashMap<String, String> data;

    public JSONData(String toParse)
    {
        JSONParser parser = new JSONParser();

        valid = true;
        receiver = "";
        data = new HashMap<>();

        try {
            JSONObject json_obj = (JSONObject) parser.parse(toParse);

            if (json_obj.containsKey("receiver"))
            {
                receiver = json_obj.get("receiver").toString();
            }
            else {
                valid = false;
                return;
            }

            for(Object key : json_obj.keySet())
            {
                if(key.equals("receiver")) continue;
                data.put(key.toString(), json_obj.get(key).toString());
            }
        } catch (ParseException exception) {
            valid = false;
        }
    }

    public boolean receivable(Receiver receiver)
    {
        if(!this.receiver.equals(receiver.receiver)) return false;
        for(String field : receiver.fields)
        {
            if(!data.containsKey(field))
                return false;
        }
        return true;
    }

    public String toString()
    {
        return "Receiver: " + receiver + "\n" + data.toString();
    }
}
