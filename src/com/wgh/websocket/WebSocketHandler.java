package com.wgh.websocket;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.json.*;

import com.wgh.database.DBOperation;
import com.wgh.model.GlobalObject;
import com.wgh.model.TimeHandler;

import org.json.*;

@ServerEndpoint("/WebChat")
public class WebSocketHandler {
	//for test
  public String myUsername;
  public String myJessonid;
  public String myGroupname;
  public boolean myLoginState;
  //for test
  
  public static JSONObject getAllUserList()//��ȡ�����û�����Ϣ
  {
	  JSONObject currentUserList = new JSONObject ();
	  //for test , simulate 4 users
	  /*
	  currentUserList.put("amount", "4");
	  currentUserList.put("0",(new JSONObject ()).put("username", "lxc").put("userstate", "online").put("userimage", ""));
	  currentUserList.put("1",(new JSONObject ()).put("username", "lxc2").put("userstate", "online").put("userimage", ""));
	  currentUserList.put("2",(new JSONObject ()).put("username", "lxc3").put("userstate", "offline").put("userimage", ""));
	  currentUserList.put("3",(new JSONObject ()).put("username", "admin").put("userstate", "online").put("userimage", ""));
	  */
	  currentUserList=DBOperation.getAllUserList();	  
	  return currentUserList;
  }
  
  public JSONObject getAllGroupList()//��ȡ����Ⱥ�����Ϣ
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 group
	  //currentGroupList.put("amount", "2");
	  //currentGroupList.put("0",(new JSONObject ()).put("groupname", "group1").put("groupimage", "").put("selfJoined", "0"));
	  //currentGroupList.put("1",(new JSONObject ()).put("groupname", "group2").put("groupimage", "").put("selfJoined", "0"));
	  currentGroupList = DBOperation.getAllGroupList(myUsername);
	  return currentGroupList;
  }
  
  public Vector<String> getMyGroups()//��ȡ�ҵ������������
  {
	  Vector<String> mygroups=new Vector<String>();
	  JSONObject all_groups = DBOperation.getAllGroupList(myUsername);
	  int amount= Integer.parseInt(all_groups.getString("amount"));
	  for(int i=0;i<amount;i++)
	  {
		  JSONObject temp=all_groups.getJSONObject(i+"");
		  if(temp.getString("selfJoined").equals("1"))
			mygroups.addElement(temp.getString("groupname"));
	  }
	  return mygroups;
  }
  
  public JSONObject getGroupMsgList()//��ȡ��ĳ��Ⱥ�����Ϣ
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 msg
	  currentGroupList.put("amount", "2");
	  currentGroupList.put("0",(new JSONObject ()).put("from", "lxc").put("date", "2017-09-01 20:50").put("content", "Hello world!"));
	  currentGroupList.put("1",(new JSONObject ()).put("from", "cw").put("date", "2017-09-01 20:51").put("content", "This is a test!"));
	  return currentGroupList;
  }
 
  public JSONObject getUserMsgList(String targetUsername)//��ȡ��ĳ���˵���Ϣ
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 msg
	  currentGroupList.put("amount", "2");
	  currentGroupList.put("0",(new JSONObject ()).put("from", "lxc2").put("to", myUsername).put("date", "2017-09-01 20:50").put("content", "Hello world!"));
	  currentGroupList.put("1",(new JSONObject ()).put("from", myUsername).put("to","lxc2" ).put("date", "2017-09-01 20:51").put("content", "Hello again!"));
	  return currentGroupList;
  }
  
  public JSONObject getUserHistoryMsg(String targetUsername)//������ʷʱ����ڵ���ĳ�˵��ض�����
  {
	  JSONObject ret = new JSONObject ();
	  /*
	  ret.put("amount", "2");
	  ret.put("name", targetUsername);
	  ret.put("0",(new JSONObject ()).put("from", targetUsername).put("to", myUsername).put("date", "2017-09-01 20:50").put("message_content", "Hello world 1!"));
	  ret.put("1",(new JSONObject ()).put("from", myUsername).put("to",targetUsername ).put("date", "2017-09-01 20:51").put("message_content", "Hello again 2!"));
	  */
	  ret = DBOperation.getAllRecords(myUsername,targetUsername);
	  ret.put("name", targetUsername);
	  
	  return ret;
  }
  
  public JSONObject getGroupHistoryMsg(String targetGroupname)//������ʷʱ����ڵ���ĳ�˵��ض�����
  {
	  JSONObject ret = new JSONObject ();
	  /*
	  ret.put("amount", "2");
	  ret.put("name", targetUsername);
	  ret.put("0",(new JSONObject ()).put("from", targetUsername).put("to", myUsername).put("date", "2017-09-01 20:50").put("message_content", "Hello world 1!"));
	  ret.put("1",(new JSONObject ()).put("from", myUsername).put("to",targetUsername ).put("date", "2017-09-01 20:51").put("message_content", "Hello again 2!"));
	  */
	  ret = DBOperation.getAllRecords_group(targetGroupname);
	  ret.put("name", targetGroupname);
	  
	  return ret;
  }
  
  public JSONObject createNewGroup(String groupname)//����Ⱥ��
  {
	  JSONObject ret = new JSONObject ();
	  if(DBOperation.StoreGroupMember(groupname, myUsername))
		  ret.put("result", "success");
	  else
		  ret.put("result", "fail");
	  ret.put("groupname", groupname);
	  return ret;
  }
  
  public JSONObject joinGroup(String groupname)//����Ⱥ��
  {
	  JSONObject ret = new JSONObject ();
	  if(DBOperation.StoreGroupMember(groupname, myUsername))
		  ret.put("result", "success");
	  else
		  ret.put("result", "fail");
	  ret.put("groupname", groupname);
	  return ret;
  }
  
  public boolean sendMsgToGroup(String fromUsername,String toGroupname,String message_type,String message_content) throws IOException
  {
	  String payloadMsg = message_content;
	  Message toSendMessage = new Message();
	  String msgFrom=myUsername; 
	  String msgTo=toGroupname;
	  long msgTime=TimeHandler.getNowTimeStamp();
	  String type="chtg";
	  String content=payloadMsg;
	  DBOperation.StoreChatRecord_group(msgFrom,msgTo,msgTime,type,payloadMsg);
	  
	  toSendMessage.Type = "getNewMessageResult" ;
	  toSendMessage.Content.put("from", myUsername).put("to", toGroupname).put("message_type", "word").put("time", TimeHandler.TimeStamp2Date(msgTime,"")).put("message_content", payloadMsg).put("from_type", "group"); 
	  UserSessions.getInstance().SendtoGroup(toGroupname, toSendMessage.toString());
	  return true;
  }

  public boolean sendMsgToUser(String fromUsername,String targetUsername,String message_type,String message_content) throws IOException
  {
	  Message toSendMessage = new Message();
	  String msgFrom=myUsername; 
	  String msgTo=targetUsername;
	  long msgTime=TimeHandler.getNowTimeStamp();
	  String type="cht";
	  String content=message_content;
	  DBOperation.StoreChatRecord(msgFrom,msgTo,msgTime,type,message_content);  
	  toSendMessage.Type = "getNewMessageResult" ;
	  toSendMessage.Content.put("from", myUsername).put("to", targetUsername).put("message_type", "word").put("time", "2017-09-01 21:02:00").put("message_content", message_content).put("from_type", "user");
	  UserSessions.getInstance().SendtoUser(targetUsername, toSendMessage.toString());
	  return true;
  }
  
  public boolean sendPost(String message_content) throws IOException
  {
	  Message toSendMessage = new Message();
	  String msgFrom=myUsername; 
	  String msgTo="all";
	  long msgTime=TimeHandler.getNowTimeStamp();
	  String type="cht";
	  String content=message_content;
	  DBOperation.StoreChatRecord(msgFrom,msgTo,msgTime,type,message_content);  
	  toSendMessage.Type = "getNewMessageResult" ;
	  toSendMessage.Content.put("from", myUsername).put("to", msgTo).put("message_type", "post").put("time", "2017-09-01 21:02:00").put("message_content", message_content).put("from_type", "user");
	  UserSessions.getInstance().SendtoAll(toSendMessage.toString());
	  return true;
  }
  
  public static boolean updateUserListToAll() throws IOException
  {
	  Message toSendMessage = new Message();
	  toSendMessage.Type = "getAllUsersResult" ;
	  toSendMessage.Content = getAllUserList();
	  UserSessions.getInstance().SendtoAll( toSendMessage.toString());
	  return true;
  }
  
  @OnMessage
  public void onMessage(String message, Session session) 
    throws IOException, InterruptedException {
	  System.out.println("websocket receive:"+message);
	  JSONObject jobj=new JSONObject(new JSONTokener(message));
	  JSONObject jcontent=jobj.getJSONObject("Content");
	  Message mes=new Message();
	  mes.Type=jobj.getString("Type");
	  mes.Content=jcontent;
	  System.out.println("Msg Type="+mes.Type);
	  if (mes.Type.equals("Authenticate"))//���������֤
	  {
		  String username=jcontent.getString("username");
		  String jessonid=jcontent.getString("jessonid");
		  String groupname=jcontent.getString("groupname");
		  if (GlobalObject.JudgeValid(username,jessonid))//�ж�ƾ����ȷ  ȷ�������֤�ɹ� ͬʱ���͸��������Ϣ
		  {
			  myLoginState=true;//�����ҵĵ�¼״̬Ϊ��
			  myUsername=username;
			  myJessonid=jessonid;
			  myGroupname=groupname;
			  
			  if (UserSessions.getInstance().addUser(new UserSession(session,username,groupname)))
			  {
				  Vector<String> mygroups=getMyGroups();
				  for(int i=0;i<mygroups.size();i++)
				  {
					  UserSessions.getInstance().addUserGroup(myUsername, mygroups.get(i));//����ǰ���û�������Ⱥ����뵽������
					  System.out.println("Websocket servlet add group:"+mygroups.get(i));
				  }
				  
				  
				  System.out.println("add User Succ! -> sessionid="+session.getId()+"  username="+username);
				  DBOperation.SetUserLoginState(username, 1);//���õ�ǰ�û���½״̬Ϊ��
				  
				  String isAdmin="0";
				  if(DBOperation.isUserAdmin(myUsername)==true)
					  isAdmin="1";
				  
				  Message toSendMessage = new Message();
				  toSendMessage.Type = "getMyIdentifyResult" ;
				  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "").put("isAdmin", isAdmin);
				  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
				  //����ǰ�û��ĵ�½��Ϣ���͸�������
				  toSendMessage = new Message();
				  toSendMessage.Type = "newUserOnlineResult" ;
				  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "");
				  UserSessions.getInstance().SendtoAll(toSendMessage.toString());
				  
				  toSendMessage = new Message();
				  toSendMessage.Type = "getAllUsersResult" ;
				  toSendMessage.Content = getAllUserList();
				  UserSessions.getInstance().SendtoAll( toSendMessage.toString());
				  
				  
			  }
			  else
			  {
				  System.out.println("�û��Ѿ���½���촰��  �ܾ������ӣ�");
				  Message toSendMessage = new Message();
				  toSendMessage.Type = "redirectWindowResult" ;
				  toSendMessage.Content = (new JSONObject()).put("url", "login?error=1");
				  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
			  }
		  }
		  else
		  {
			  myLoginState=false;//�����ҵĵ�¼״̬Ϊ��
			  System.out.println("User Verification error!");
			  
		  }
	  }
	  else if (mes.Type.equals("getMyIdentify"))//��ȡ�Լ��������Ϣ
	  {
		  Message toSendMessage = new Message();
		  toSendMessage.Type = "getMyIdentifyResult" ;
		  String isAdmin="0";
		  if(DBOperation.isUserAdmin(myUsername)==true)
			  isAdmin="1";
		  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "").put("isAdmin", isAdmin);
		  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
	  }
	  else if (mes.Type.equals("getUserIdentify"))//��ȡĿ���û��������Ϣ
	  {
		  String targetUsername=mes.Content.getString("username");//��ȡĿ���û���
		  Message toSendMessage = new Message();
		  toSendMessage.Type = "getUserIdentifyResult" ;
		  String loginState;
		  if(DBOperation.GetUserLoginState(targetUsername))//���ﻹ��Ҫ����ѯ���û��Ƿ����
			  loginState="online";
		  else
			  loginState="offline";
		  toSendMessage.Content = (new JSONObject()).put("username", targetUsername).put("userimage", "").put("userstate", "online");
		  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
	  }	
	  else if (mes.Type.equals("getAllUsers"))//�����ȡ�����û���Ϣ�б�
	  {
		  if(myLoginState==true)
		  {
			  
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getAllUsersResult" ;
			  toSendMessage.Content = getAllUserList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroups"))//�����ȡ����Ⱥ��
	  {
		  if(myLoginState==true)
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupsResult" ;
			  toSendMessage.Content = getAllGroupList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroupMsg"))//�����ȡ��ĳ��Ⱥ�����Ϣ
	  {
		  if(myLoginState==true)
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMsgResult" ;
			  toSendMessage.Content = getGroupMsgList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroupMsg"))//�����ȡ��ĳ��Ⱥ�����Ϣ
	  {
		  if(myLoginState==true)
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMsgResult" ;
			  toSendMessage.Content = getGroupMsgList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  /*
	  else if(mes.Type.equals("getUserMsg"))//�����ȡ��ĳ���˵���Ϣ
	  {
		  if(myLoginState==true)
		  {
			  String targetUsername = mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupUserResult" ;
			  toSendMessage.Content = getUserMsgList(targetUsername);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  */
	  else if(mes.Type.equals("sendMsg"))//������Ϣ��Ⱥ�� ���� ����
	  {
		  if(myLoginState==true)
		  {
			  String msgDestType =  mes.Content.getString("object_type");//�����Ƿ���Ⱥ������Ǹ���
			  if(msgDestType.equals("group"))//���͸�Ⱥ��
			  {
				  String targetGroupname = mes.Content.getString("to");
				  String payloadMsg = mes.Content.getString("msg");
				  sendMsgToGroup(myUsername,targetGroupname,"word",payloadMsg);
			  }
			  else if(msgDestType.equals("user"))//���͸�����
			  {
				  String targetUsername = mes.Content.getString("to");
				  String payloadMsg = mes.Content.getString("msg");
				  sendMsgToUser(myUsername,targetUsername,"word",payloadMsg);
			  }
		  }
	  }
	  else if(mes.Type.equals("sendMsgPost"))//��������
	  {
		  String payloadMsg = mes.Content.getString("msg");
		  if(DBOperation.isUserAdmin(myUsername))
		  {
			  System.out.println("Admin send post");
			  sendPost(payloadMsg);
		  }
		  else
			  System.out.println("Not Admin try to send post");
	  }
	  else if(mes.Type.equals("getUserHistoryMsg"))//������ʷʱ����ڵ���ĳ�˵��ض���������
	  {
		  if(myLoginState==true)
		  {
			  String targetUsername = mes.Content.getString("username");
			  String begintime = mes.Content.getString("begintime");
			  String endtime = mes.Content.getString("endtime");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserHistoryMsgResult" ;
			  toSendMessage.Content = getUserHistoryMsg(targetUsername);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
		  }
	  }
	  else if(mes.Type.equals("getGroupHistoryMsg"))
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");
			  String begintime = mes.Content.getString("begintime");
			  String endtime = mes.Content.getString("endtime");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupHistoryMsgResult" ;
			  toSendMessage.Content = getGroupHistoryMsg(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
		  }
	  }
	  else if(mes.Type.equals("createNewGroup"))//�����µ�Ⱥ�飨ʵ���Ͼ��ǹ����Լ������Ⱥ�飩
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "createNewGroupResult" ;
			  toSendMessage.Content = createNewGroup(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
		  }
	  }
	  else if(mes.Type.equals("joinGroup"))//�����µ�Ⱥ�飨ʵ���Ͼ��ǹ����Լ������Ⱥ�飩
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "joinGroupResult" ;
			  toSendMessage.Content = joinGroup(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
			  
			  UserSessions.getInstance().addUserGroup(myUsername, targetGroupname);
		  }
	  }
	  else if(mes.Type.equals("getGroupMembers"))//��ȡС�����г�Ա
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMembersResult" ;
			  toSendMessage.Content = getGroupMembers(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
		  }
	  }
	  else if(mes.Type.equals("search"))//�����û�
	  {
		  if(myLoginState==true)
		  {
			  String targetUsername = mes.Content.getString("criteria");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "searchResult" ;
			  toSendMessage.Content = DBOperation.searchUser(targetUsername);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//������ظ����Լ�
		  }
	  }
	  
	  
  }
  
  @OnOpen
  public void onOpen(Session session) {
	myLoginState=false;//��ʼ״̬��ʱ�����õ�ǰ״̬Ϊ����֤״̬
    System.out.println("Client connected");
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("Connection closed");
    UserSessions.getInstance().removeUser(session);
    if(myLoginState==true)//������Ѿ���½  ��ô�������ҵĵ�½״̬Ϊ����
    {
    	DBOperation.SetUserLoginState(myUsername, 0);//���õ�ǰ�û���½״̬Ϊ����
    }
  }
  
  public JSONObject getGroupMembers(String groupname) {//��ȡС�����г�Ա
	  JSONObject ret = new JSONObject ();
	  /*
	  ret.put("amount", "2");
	  ret.put("name", groupname);
	  ret.put("0", "admin");
	  ret.put("1", "lxc");
	  */
	  ret = DBOperation.getGroupMembers(groupname);
	  //ret.put("name", "groupname");
	  return ret;
}

  
}