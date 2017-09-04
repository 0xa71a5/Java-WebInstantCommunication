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
  
  public static JSONObject getAllUserList()//获取所有用户的信息
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
  
  public JSONObject getAllGroupList()//获取所有群组的消息
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 group
	  //currentGroupList.put("amount", "2");
	  //currentGroupList.put("0",(new JSONObject ()).put("groupname", "group1").put("groupimage", "").put("selfJoined", "0"));
	  //currentGroupList.put("1",(new JSONObject ()).put("groupname", "group2").put("groupimage", "").put("selfJoined", "0"));
	  currentGroupList = DBOperation.getAllGroupList(myUsername);
	  return currentGroupList;
  }
  
  public Vector<String> getMyGroups()//获取我的所有组的名字
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
  
  public JSONObject getGroupMsgList()//获取与某个群组的消息
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 msg
	  currentGroupList.put("amount", "2");
	  currentGroupList.put("0",(new JSONObject ()).put("from", "lxc").put("date", "2017-09-01 20:50").put("content", "Hello world!"));
	  currentGroupList.put("1",(new JSONObject ()).put("from", "cw").put("date", "2017-09-01 20:51").put("content", "This is a test!"));
	  return currentGroupList;
  }
 
  public JSONObject getUserMsgList(String targetUsername)//获取与某个人的消息
  {
	  JSONObject currentGroupList = new JSONObject ();
	  //for test , simulate 2 msg
	  currentGroupList.put("amount", "2");
	  currentGroupList.put("0",(new JSONObject ()).put("from", "lxc2").put("to", myUsername).put("date", "2017-09-01 20:50").put("content", "Hello world!"));
	  currentGroupList.put("1",(new JSONObject ()).put("from", myUsername).put("to","lxc2" ).put("date", "2017-09-01 20:51").put("content", "Hello again!"));
	  return currentGroupList;
  }
  
  public JSONObject getUserHistoryMsg(String targetUsername)//请求历史时间段内的与某人的特定数据
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
  
  public JSONObject getGroupHistoryMsg(String targetGroupname)//请求历史时间段内的与某人的特定数据
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
  
  public JSONObject createNewGroup(String groupname)//创建群组
  {
	  JSONObject ret = new JSONObject ();
	  if(DBOperation.StoreGroupMember(groupname, myUsername))
		  ret.put("result", "success");
	  else
		  ret.put("result", "fail");
	  ret.put("groupname", groupname);
	  return ret;
  }
  
  public JSONObject joinGroup(String groupname)//加入群组
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
	  if (mes.Type.equals("Authenticate"))//请求身份认证
	  {
		  String username=jcontent.getString("username");
		  String jessonid=jcontent.getString("jessonid");
		  String groupname=jcontent.getString("groupname");
		  if (GlobalObject.JudgeValid(username,jessonid))//判断凭据正确  确认身份认证成功 同时发送个人身份信息
		  {
			  myLoginState=true;//设置我的登录状态为真
			  myUsername=username;
			  myJessonid=jessonid;
			  myGroupname=groupname;
			  
			  if (UserSessions.getInstance().addUser(new UserSession(session,username,groupname)))
			  {
				  Vector<String> mygroups=getMyGroups();
				  for(int i=0;i<mygroups.size();i++)
				  {
					  UserSessions.getInstance().addUserGroup(myUsername, mygroups.get(i));//将当前的用户的所有群组加入到对象中
					  System.out.println("Websocket servlet add group:"+mygroups.get(i));
				  }
				  
				  
				  System.out.println("add User Succ! -> sessionid="+session.getId()+"  username="+username);
				  DBOperation.SetUserLoginState(username, 1);//设置当前用户登陆状态为真
				  
				  String isAdmin="0";
				  if(DBOperation.isUserAdmin(myUsername)==true)
					  isAdmin="1";
				  
				  Message toSendMessage = new Message();
				  toSendMessage.Type = "getMyIdentifyResult" ;
				  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "").put("isAdmin", isAdmin);
				  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
				  //将当前用户的登陆信息发送给所有人
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
				  System.out.println("用户已经登陆聊天窗口  拒绝该链接！");
				  Message toSendMessage = new Message();
				  toSendMessage.Type = "redirectWindowResult" ;
				  toSendMessage.Content = (new JSONObject()).put("url", "login?error=1");
				  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
			  }
		  }
		  else
		  {
			  myLoginState=false;//设置我的登录状态为假
			  System.out.println("User Verification error!");
			  
		  }
	  }
	  else if (mes.Type.equals("getMyIdentify"))//获取自己的身份信息
	  {
		  Message toSendMessage = new Message();
		  toSendMessage.Type = "getMyIdentifyResult" ;
		  String isAdmin="0";
		  if(DBOperation.isUserAdmin(myUsername)==true)
			  isAdmin="1";
		  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "").put("isAdmin", isAdmin);
		  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
	  }
	  else if (mes.Type.equals("getUserIdentify"))//获取目标用户的身份信息
	  {
		  String targetUsername=mes.Content.getString("username");//获取目标用户名
		  Message toSendMessage = new Message();
		  toSendMessage.Type = "getUserIdentifyResult" ;
		  String loginState;
		  if(DBOperation.GetUserLoginState(targetUsername))//这里还需要检测查询的用户是否存在
			  loginState="online";
		  else
			  loginState="offline";
		  toSendMessage.Content = (new JSONObject()).put("username", targetUsername).put("userimage", "").put("userstate", "online");
		  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
	  }	
	  else if (mes.Type.equals("getAllUsers"))//请求获取所有用户信息列表
	  {
		  if(myLoginState==true)
		  {
			  
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getAllUsersResult" ;
			  toSendMessage.Content = getAllUserList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroups"))//请求获取所有群组
	  {
		  if(myLoginState==true)
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupsResult" ;
			  toSendMessage.Content = getAllGroupList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroupMsg"))//请求获取与某个群组的消息
	  {
		  if(myLoginState==true)
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMsgResult" ;
			  toSendMessage.Content = getGroupMsgList();
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());
		  }
	  }
	  else if(mes.Type.equals("getGroupMsg"))//请求获取与某个群组的消息
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
	  else if(mes.Type.equals("getUserMsg"))//请求获取与某个人的消息
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
	  else if(mes.Type.equals("sendMsg"))//发送消息给群组 或者 个人
	  {
		  if(myLoginState==true)
		  {
			  String msgDestType =  mes.Content.getString("object_type");//分析是发给群组或者是个人
			  if(msgDestType.equals("group"))//发送给群组
			  {
				  String targetGroupname = mes.Content.getString("to");
				  String payloadMsg = mes.Content.getString("msg");
				  sendMsgToGroup(myUsername,targetGroupname,"word",payloadMsg);
			  }
			  else if(msgDestType.equals("user"))//发送给个人
			  {
				  String targetUsername = mes.Content.getString("to");
				  String payloadMsg = mes.Content.getString("msg");
				  sendMsgToUser(myUsername,targetUsername,"word",payloadMsg);
			  }
		  }
	  }
	  else if(mes.Type.equals("sendMsgPost"))//发布公告
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
	  else if(mes.Type.equals("getUserHistoryMsg"))//请求历史时间段内的与某人的特定聊天数据
	  {
		  if(myLoginState==true)
		  {
			  String targetUsername = mes.Content.getString("username");
			  String begintime = mes.Content.getString("begintime");
			  String endtime = mes.Content.getString("endtime");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserHistoryMsgResult" ;
			  toSendMessage.Content = getUserHistoryMsg(targetUsername);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
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
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
		  }
	  }
	  else if(mes.Type.equals("createNewGroup"))//创建新的群组（实际上就是关联自己与这个群组）
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "createNewGroupResult" ;
			  toSendMessage.Content = createNewGroup(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
		  }
	  }
	  else if(mes.Type.equals("joinGroup"))//创建新的群组（实际上就是关联自己与这个群组）
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "joinGroupResult" ;
			  toSendMessage.Content = joinGroup(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
			  
			  UserSessions.getInstance().addUserGroup(myUsername, targetGroupname);
		  }
	  }
	  else if(mes.Type.equals("getGroupMembers"))//获取小组所有成员
	  {
		  if(myLoginState==true)
		  {
			  String targetGroupname = mes.Content.getString("groupname");

			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMembersResult" ;
			  toSendMessage.Content = getGroupMembers(targetGroupname);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
		  }
	  }
	  else if(mes.Type.equals("search"))//搜索用户
	  {
		  if(myLoginState==true)
		  {
			  String targetUsername = mes.Content.getString("criteria");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "searchResult" ;
			  toSendMessage.Content = DBOperation.searchUser(targetUsername);
			  UserSessions.getInstance().SendtoUser(myUsername, toSendMessage.toString());//结果返回给我自己
		  }
	  }
	  
	  
  }
  
  @OnOpen
  public void onOpen(Session session) {
	myLoginState=false;//初始状态的时候设置当前状态为非认证状态
    System.out.println("Client connected");
  }

  @OnClose
  public void onClose(Session session) {
    System.out.println("Connection closed");
    UserSessions.getInstance().removeUser(session);
    if(myLoginState==true)//如果我已经登陆  那么就设置我的登陆状态为下线
    {
    	DBOperation.SetUserLoginState(myUsername, 0);//设置当前用户登陆状态为离线
    }
  }
  
  public JSONObject getGroupMembers(String groupname) {//获取小组所有成员
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