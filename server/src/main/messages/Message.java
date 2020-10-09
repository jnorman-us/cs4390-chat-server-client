package main.messages;

import org.json.simple.JSONObject;

import java.util.HashMap;

public abstract class Message
{
    protected String receiver;
    protected String[] fields;

    public Message(String receiver, String[] fields)
    {
        this.receiver = receiver;
        this.fields = fields;
    }

    public boolean sendAble(HashMap<String, String> data)
    {
        for(String field : fields)
        {
            if(!data.containsKey(field))
                return false;
        }
        return true;
    }

    // maybe convert this to a method that sends
    public String stringify(HashMap<String, String> data)
    {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("receiver", receiver);
        for(String field : fields)
        {
            jsonObject.put(field, data.get(field));
        }
        return jsonObject.toJSONString();
    }
}
