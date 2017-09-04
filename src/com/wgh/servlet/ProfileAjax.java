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
	
	  public JSONObject getAllUsers()//��ȡ���г�Ա������
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
	  
	  public JSONObject searchUser(String searchValue)//ģ������
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
	
	  public JSONObject getUserAllProfile(String username)//���ݳ�Ա��������������Ϣ
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
		
		if(mes.Type.equals("getAllUsers"))//��ȡ�����û�
		{
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getAllUsersResult" ;
			  toSendMessage.Content = getAllUsers();
			  response.getWriter().write(toSendMessage.toString());
		}
		else if (mes.Type.equals("getGroups"))//��ȡ����С���б�
		{
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupsResult" ;
			  toSendMessage.Content = getAllGroupList();
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("getUserIdentifyByType"))//ģ�������û�
		{
			  String searchValue=mes.Content.getString("value");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserIdentifyByTypeResult" ;
			  toSendMessage.Content = searchUser(searchValue);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("getUserIdentify"))//���ݳ�Ա��������������Ϣ
		{
			  System.out.println("Enter getUserIdentifyResult");
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getUserIdentifyResult" ;
			  toSendMessage.Content = getUserAllProfile(username);
			  response.getWriter().write(toSendMessage.toString());
			  System.out.println("getUserIdentifyResult=>"+toSendMessage.toString());
		}
		else if(mes.Type.equals("modifyUserIdentify"))//�޸ĳ�Ա��Ϣ
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
		else if(mes.Type.equals("getGroupMembers"))//��ȡС���Ա
		{
			  String groupname=mes.Content.getString("groupname");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getGroupMembersResult" ;
			  toSendMessage.Content = getGroupMembers(groupname);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("deleteGroup"))//ɾ��С��
		{
			  String groupname=mes.Content.getString("groupname");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "deleteGroupResult" ;
			  toSendMessage.Content = deleteGroup(groupname);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("deleteUser"))//ɾ���û�
		{
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "deleteUserResult" ;
			  toSendMessage.Content = deleteUser(username);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if(mes.Type.equals("priviligeNormalUser"))//���ó����û�
		{
			  String username=mes.Content.getString("username");
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "priviligeNormalUserResult" ;
			  toSendMessage.Content = setUserAdmin(username);
			  response.getWriter().write(toSendMessage.toString());
		}
		else if (mes.Type.equals("getMyIdentify"))//��ȡ�Լ��������Ϣ
		  {
			  Message toSendMessage = new Message();
			  toSendMessage.Type = "getMyIdentifyResult" ;
			  toSendMessage.Content = (new JSONObject()).put("username", myUsername).put("userimage", "");
			  response.getWriter().write(toSendMessage.toString());
		  }
	}
	
	public JSONObject modifyUserProfile(JSONObject content) {//�޸�һ����Ա��������Ϣ
		  JSONObject ret = new JSONObject ();
		  if(DBOperation.modifyUserProfile(content))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
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
		  
		  return ret;
	}
	
	public JSONObject deleteGroup(String groupname) {//ɾ��С��
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.deleteGroup(groupname))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
	public JSONObject deleteUser(String username) {//ɾ���û�
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.deleteUser(username))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
	private JSONObject setUserAdmin(String username) {//�����û�Ϊ����Ա
		  JSONObject ret = new JSONObject ();
		  //ret.put("result", "success");
		  if(DBOperation.SetUserAdmin(username,1))
			  ret.put("result", "success");
		  else
			  ret.put("result", "fail");
		  return ret;
	}
	
}
