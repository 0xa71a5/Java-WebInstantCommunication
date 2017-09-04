package com.wgh.websocket;

import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

public class UserSession {
	public Session session;
	public String UserName;
	//public String GroupName
	public Vector GroupNames;
	
	public UserSession(Session S,String UN,String GN)
	{
		session=S;
		UserName=UN;
		GroupNames=new Vector();
		//GroupName=GN;
		GroupNames.add(GN);
	}
	
	public UserSession(Session S,String UN,Vector GNs)
	{
		session=S;
		UserName=UN;
		GroupNames=GNs;
	}
	
	public UserSession()
	{
		//SetSession(null,null,null);
		session=null;
		UserName=null;
		GroupNames=new Vector();
	}
	
	public void SetSession(Session S,String UN,String GN)
	{
		session=S;
		UserName=UN;
		//GroupName=GN;
		GroupNames=new Vector();
		GroupNames.add(GN);
	}
	
	public void SetSession(Session S,String UN,Vector GNs)
	{
		session=S;
		UserName=UN;
		GroupNames=GNs;
	}
	
	public void AddGroup(String GN)
	{
		if (GroupNames==null)
			GroupNames=new Vector();
		GroupNames.add(GN);
	}
	
	public boolean equal(UserSession A)
	{
		return (UserName.equals(A.UserName))||(session.getId().equals(A.session.getId()));
	}
}
