package com.wgh.websocket;

import java.util.Map;
import org.json.JSONObject;

public class Message {
	public String Type;
	public JSONObject Content;
	public Message()
	{
		Type="";
		Content=new JSONObject();
	}
	public String toString()
	{
		JSONObject toRetJson = new JSONObject () ;
		toRetJson.put("Type", Type);
		toRetJson.put("Content", Content);
		return toRetJson.toString();
	}  
}
