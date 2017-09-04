package com.wgh.websocket;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.websocket.Session;

public class UserSessions {
	private static UserSessions user = new UserSessions();
	private TreeMap<String,UserSession> TM=null;
	private TreeMap<String,TreeMap<String,UserSession>> Groups=null;
	
	
	// 利用private调用构造函数，防止被外界产生新的instance对象
		private UserSessions() {
			this.TM = new TreeMap<String,UserSession>();
			this.Groups = new TreeMap<String,TreeMap<String,UserSession>>();
		}

		// 外界使用的instance对象
		public static UserSessions getInstance() {
			return user;
		}

		// 增加用户
		public boolean addUser(UserSession user) {
			if (user != null) {
				if (TM.containsKey(user.UserName))
				{
					
					return false;
				}
				TM.put(user.UserName, user);
				return true;
			} else {
				return false;
			}
		}
		
		public boolean addUserGroup(String username,String groupname)
		{
			if (TM.containsKey(username))
			{
				UserSession us=TM.get(username);
				us.AddGroup(groupname);
				return true;
			}else
				return false;
		}
		
		public boolean UserExisted(String username) {
			return TM.containsKey(username);
		}

		// 获取用户列表
		public TreeMap<String, UserSession> getList() {
			return TM;
		}

		// 移除用户
		public void removeUser(String username) {
			if (username != null) {
				if (TM.containsKey(username)){
					TM.remove(username);
				}
			}
		}
		
		public void removeUser(Session mysession)
		{
			Iterator iter=TM.entrySet().iterator();
			while(iter.hasNext()){
			    Map.Entry mapEntry=(Map.Entry) iter.next();
			    if (((UserSession)mapEntry.getValue()).session.getId().equals(mysession.getId()))
			    {
			    	String key=(String)mapEntry.getKey();
			    	TM.remove(key);
			    	
			    	//System.out.println("log out succ "+key);
			    	
			    	break;
			    }
			}
		}
		
		public void SendtoUser(String username,String content) throws IOException
		{
			Object u=TM.get(username);
			((UserSession)u).session.getBasicRemote().sendText(content);
		}
		
		public void SendtoGroup(String groupname,String content) throws IOException
		{
			Iterator iter=TM.entrySet().iterator();
			while(iter.hasNext()){
			    Map.Entry mapEntry=(Map.Entry) iter.next();
			    /*
			    if (((UserSession)mapEntry.getValue()).GroupName.equals(groupname))
			    {
			    	((UserSession)mapEntry.getValue()).session.getBasicRemote().sendText(content);
			    }
			    */
			    Vector GroupNames=((UserSession)mapEntry.getValue()).GroupNames;
			    for (Object i : GroupNames)
			    {
			    	if (((String)i).equals(groupname))
			    	{
			    		((UserSession)mapEntry.getValue()).session.getBasicRemote().sendText(content);
			    		break;
			    	}
			    }
			}
		}
		
		public void SendtoAll(String content) throws IOException
		{
			Iterator iter=TM.entrySet().iterator();
			while(iter.hasNext()){
			    Map.Entry mapEntry=(Map.Entry) iter.next();
			    ((UserSession)mapEntry.getValue()).session.getBasicRemote().sendText(content);
			}
		}
	
}
