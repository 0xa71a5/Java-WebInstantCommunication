package com.wgh.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.wgh.database.DBOperation;
import com.wgh.websocket.Message;
import com.wgh.websocket.UserSessions;

public class ProfileAjax  extends HttpServlet{	
	  public String myUsername;  
	
	  public JSONObject getAllUsers()//获取所有成员的姓名
	  {
		  JSONObject ret = new JSONObject ();
		  //for test , simulate 2 group
		  /*
		  ret.put("amount", "2");
		  ret.put("0",(new JSONObject ()).put("username", "admin"));
		  ret.put("1",(new JSONObject ()).put("username", "lxc"));
		  */
		  ret = DBOperation.getAllUsers();
		  return ret;
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
	  
	  public JSONObject searchUser(String searchValue)//模糊搜索
	  {
		  JSONObject ret = new JSONObject ();
		  //for test , simulate 2 group
		  /*
		  ret.put("amount", "2");
		  ret.put("0",(new JSONObject ()).put("username", "admin").put("sex", "male").put("nickname", "admin").put("password", "admin").put("email", "admin@test.com").put("telephone", "15845892569").put("department", "dep1"));
		  ret.put("0",(new JSONObject ()).put("username", "lxc").put("sex", "male").put("nickname", "lxc").put("password", "123123").put("email", "lxc@test.com").put("telephone", "15845485569").put("department", "dep2"));
		  */
		  ret = DBOperation.searchUser(searchValue);
		  return ret;
	  }
	
	  public JSONObject getUserAllProfile(String username)//根据成员姓名返回所有信息
	  {
		  JSONObject ret = new JSONObject ();
		  //ret.put("username", username).put("sex", "male").put("nickname", username).put("password", "admin").put("email", "admin@test.com").put("telephone", "15845892569").put("department", "dep1");		  
		  //getUserAllProfile()
		  ret = DBOperation.getUserAllProfile(username);
		  return ret;
	  }
	  
	  
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		response.getWriter().write("Hello world! Get method!");
	}
	
	public static byte[] getRequestPostBytes(HttpServletRequest request)  
            throws IOException {  
        int contentLength = request.getContentLength();  
        if(contentLength<0){  
            return null;  
        }  
        byte buffer[] = new byte[contentLength];  
        for (int i = 0; i < contentLength;) {  
  
            int readlen = request.getInputStream().read(buffer, i,  
                    contentLength - i);  
            if (readlen == -1) {  
                break;  
            }  
            i += readlen;  
        }  
        return buffer;  
    }  
	
	public static String getRequestPostStr(HttpServletRequest request)  
            throws IOException {  
        byte buffer[] = getRequestPostBytes(request);  
        String charEncoding = request.getCharacterEncoding();  
        if (charEncoding == null) {  
            charEncoding = "UTF-8";  
        }  
        return new String(buffer, charEncoding);  
    }  
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		myUsername=(String) request.getSession().getAttribute("username");
		
		String message=getRequestPostStr(request);
		System.out.println("Recv from "+myUsername+":"+message);
		JSONObject jobj=new JSONObject(new JSONTokener(message));
		JSONObject jcontent=jobj.getJSONObject("Content");
		Message mes=new Message();
		mes.Type=jobj.getString("Type");
		mes.Content=jcontent;
		System.out.println("Msg Type="+mes.Type);
		
		if(mes.Type.equals("getAllUsers"))//获取所有用户
		{
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getAllUsersResult" ;
			  toSendMessage.Content = getAllUsers();
			  response.getWriter().write(toSendMessage.toString());
		}
		else if (mes.Type.equals("getGroups"))//获取所有小组列表
		{
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupsResult" ;
			  toSendMessage.Content = getAllGroupList();
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("getUserIdentifyByType"))//模糊搜索用户
		{
			  String searchValue=mes.Content.getString("value");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserIdentifyByTypeResult" ;
			  toSendMessage.Content = searchUser(searchValue);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("getUserIdentify"))//根据成员姓名返回所有信息
		{
			  System.out.println("Enter getUserIdentifyResult");
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserIdentifyResult" ;
			  toSendMessage.Content = getUserAllProfile(username);
			  response.getWriter().write(toSendMessage.toString());
			  System.out.println("getUserIdentifyResult=>"+toSendMessage.toString());
		}
		else if(mes.Type.equals("modifyUserIdentify"))//修改成员信息
		{
			/*
			  String username=mes.Content.getString("username");
			  String sex=mes.Content.getString("sex");
			  String nickname=mes.Content.getString("nickname");
			  String password=mes.Content.getString("password");
			  String email=mes.Content.getString("email");
			  String telephone=mes.Content.getString("telephone");
			  String department=mes.Content.getString("department");
			  */
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "modifyUserIdentifyResult" ;
			  toSendMessage.Content = modifyUserProfile(mes.Content);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("getGroupMembers"))//获取小组成员
		{
			  String groupname=mes.Content.getString("groupname");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMembersResult" ;
			  toSendMessage.Content = getGroupMembers(groupname);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("deleteGroup"))//删除小组
		{
			  String groupname=mes.Content.getString("groupname");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "deleteGroupResult" ;
			  toSendMessage.Content = deleteGroup(groupname);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("deleteUser"))//删除用户
		{
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "deleteUserResult" ;
			  toSendMessage.Content = deleteUser(username);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("priviligeNormalUser"))//设置超级用户
		{
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "priviligeNormalUserResult" ;
			  toSendMessage.Content = setUserAdmin(username);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if (mes.Type.equals("getMyIdentify"))//获取自己的身份信息
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getMyIdentifyResult" ;
			  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "");
			  response.getWriter().write(toSendMessage.toString());
		  }
	}
	
	public JSONObject modifyUserProfile(JSONObject content) {//修改一个成员的所有信息
		  JSONObject ret = new JSONObject ();
		  if(DBOperation.modifyUserProfile(content))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
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
		  
		  return ret;
	}
	
	public JSONObject deleteGroup(String groupname) {//删除小组
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.deleteGroup(groupname))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
	public JSONObject deleteUser(String username) {//删除用户
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.deleteUser(username))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
	private JSONObject setUserAdmin(String username) {//设置用户为管理员
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.SetUserAdmin(username,1))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
}
