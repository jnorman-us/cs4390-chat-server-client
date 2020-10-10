package main.messages;

import org.json.simple.JSONObject;

import java.util.Map;

public abstract class Message
{
    protected String receiver; // the value of the "receiver" that will be sent as JSON
    protected String[] fields; // the keys of the data that will be sent as JSON

    // called by the Children to set which receiver and fields will be sent in the JSON
    public Message(String receiver, String[] fields)
    {
        this.receiver = receiver;
        this.fields = fields;
    }

    // returns true if the data has each of the keys in this.fields expected to be sent
    // as JSON
    public boolean sendAble(Map<String, String> data)
    {
        for(String field : fields)
        {
            if(!data.containsKey(field))
                return false;
        }
        return true;
    }

    // this function actually converts the data into a JSON string. Required that you
    // run sendAble before to verify the data actually fits under this message's format
    public String stringify(Map<String, String> data)
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
