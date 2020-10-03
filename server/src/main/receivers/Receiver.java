package main.receivers;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

public class Receiver
{
    protected String receiver;
    protected String[] fields;

    protected JSONParser parser;
    protected JSONObject json_obj;

    public Map<String, String> json_data;

    public Receiver(String receiver, String[] fields) {
        this.receiver = receiver;
        this.fields = fields;

        parser = new JSONParser();

        json_data = new HashMap<String, String>();
    }

    public boolean parse(String toParse)
    {
        json_data = new HashMap<String, String>();

        try {
            json_obj = (JSONObject) parser.parse(toParse);

            if (!json_obj.containsKey("receiver")) {
                throw new IndexOutOfBoundsException();
            } else if (!receiver.equals(json_obj.get("receiver"))) {
                throw new IndexOutOfBoundsException();
            }

            for (String field : fields) {
                if (json_obj.containsKey(field)) {
                    json_data.put(field, json_obj.get(field).toString());
                } else
                    throw new IndexOutOfBoundsException();
            }
        } catch (ParseException e) {
            return false;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }
}
