package com.wgh.database;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

public class DBOperation {
	static Connection conn;
	
	public static void ConnectionOpen(){
		conn = getConn();
	}
	
	public static void ConnectionClose(){
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static Connection getConn() {
		String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://119.29.5.72/wwwdata";
	    String username = "java";
	    String password = "1234";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	
	public static boolean IsUserExists(String username) {
	    //Connection conn = getConn();
	    String sql = "select * from employees where username = \""+username+ "\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()!=false){
	        	pstmt.close();
		        //conn.close();
	        	return true;
	        }else{
	        	pstmt.close();
		        //conn.close();
	        	return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}
	
	public static boolean IsUserVerified(String username) {
		if (IsUserExists(username)==false)
	    	return false;
	    //Connection conn = getConn();
	    String sql = "select * from employees where username = \""+username+ "\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        rs.next();
	        if(rs.getInt(10)==0){
	        	pstmt.close();
		        //conn.close();
	        	return false;
	        }
	        else{
	        	pstmt.close();
		        //conn.close();
	        	return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static boolean CreateUser(Employee ee) {
	    
	    if (IsUserExists(ee.getName())==true)
	    	return false;
	    
	    //Connection conn = getConn();
		PreparedStatement pstmt;
	    //String sql = "insert into employees (username,password) values "+"(\""+ee.getName()+"\", "+"\""+ee.getPassword()+"\");" ;
	    String sql = "insert into employees (username,password,nickname,email,department,gender,telephone,verifycode) values(?,?,?,?,?,?,?,?)";
		try {
			 pstmt = (PreparedStatement) conn.prepareStatement(sql);
		     pstmt.setString(1, ee.getName());
		     pstmt.setString(2, ee.getPassword());
		     if(ee.getNickname()==null)
		    	 pstmt.setString(3, null);
		     else
		    	 pstmt.setString(3, ee.getNickname());
		     
		     if(ee.getEmail()==null)
		    	 pstmt.setString(4, null);
		     else
		    	 pstmt.setString(4, ee.getEmail());
		     
		     if(ee.getDepartment()==null)
		    	 pstmt.setString(5, null);
		     else
		    	 pstmt.setString(5, ee.getDepartment());
		     
		     if(ee.getGender()==null)
		    	 pstmt.setString(6, null);
		     else
		    	 pstmt.setString(6, ee.getGender());
		     
		     if(ee.getTelephone()==null)
		    	 pstmt.setString(7, null);
		     else
		    	 pstmt.setString(7, ee.getTelephone());
		     
		     if(ee.getVerifycode()==null)
		    	 pstmt.setString(8, null);
		     else
		    	 pstmt.setString(8, ee.getVerifycode());
		     
		     pstmt.executeUpdate();
		     pstmt.close();
		     //conn.close();
		     return true;
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return false;
	}
	
	public static Employee getUserProfile(String username) {
		if (IsUserExists(username)==false)
	    	return null;
		
	    Connection conn = getConn();
	    String sql = "select * from employees where username = \""+username+ "\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        rs.next();
	        Employee ee= new Employee(rs.getString(2),rs.getString(3));
	        ee.setID(""+rs.getInt(1));
	        ee.setNickname(rs.getString(4));
	        ee.setEmail(rs.getString(5));
	        ee.setDepartment(rs.getString(6));
	        ee.setGender(rs.getString(7));
	        ee.setTelephone(rs.getString(8));
	        ee.setState(rs.getInt(9));
	        ee.setVerified(rs.getInt(10));
	        ee.setVerifycode(rs.getString(11));
	        pstmt.close();
	        conn.close();
	        return ee;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static Boolean UpdateUserProfile(Employee ee) {
	    //Connection conn = getConn();
		if (IsUserExists(ee.getName())==false)
	    	return false;
		
	    PreparedStatement pstmt;
	    try { 
	        String sql = "update employees set password=\"" + ee.getPassword() + "\" where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(ee.getNickname()!=null){
		        sql = "update employees set nickname=\"" + ee.getNickname() + "\" where username= \"" +ee.getName()+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }else{
	        	sql = "update employees set nickname = null where username= \"" +ee.getName()+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }
	        
	        if(ee.getEmail()!=null){
	        	sql = "update employees set email=\"" + ee.getEmail() + "\" where username= \"" +ee.getName()+"\"";
	        	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        	pstmt.executeUpdate();
	        }else{
	        	sql = "update employees set email = null where username= \"" +ee.getName()+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }
	        
	        if(ee.getDepartment()!=null)
	        	sql = "update employees set department=\"" + ee.getDepartment() + "\" where username= \"" +ee.getName()+"\"";
	        else
	        	sql = "update employees set department = null where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(ee.getGender()!=null)
	        	sql = "update employees set gender=\"" + ee.getGender() + "\" where username= \"" +ee.getName()+"\"";
	        else
	        	sql = "update employees set gender = null where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(ee.getTelephone()!=null)
	        	sql = "update employees set telephone=\"" + ee.getTelephone() + "\" where username= \"" +ee.getName()+"\"";
	        else
	        	sql = "update employees set telephone = null where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        sql = "update employees set state = " + ee.getState() + " where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        sql = "update employees set verfied = " + ee.getVerified() + " where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(ee.getVerifycode()!=null)
	        	sql = "update employees set verifycode = \"" + ee.getVerifycode() + "\" where username= \"" +ee.getName()+"\"";
	        else
	        	sql = "update employees set verifycode = null where username= \"" +ee.getName()+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        pstmt.close();
	        //conn.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static Boolean SetUserLoginState(String username,int state) {
	    //Connection conn = getConn();
		if (IsUserExists(username)==false)
	    	return false;
		
	    PreparedStatement pstmt;
	    try {
	        String sql = "update employees set state = " + state + " where username = \"" + username+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate(); 
	        pstmt.close();
	        //conn.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static boolean GetUserLoginState(String username){
		if (IsUserExists(username)==false)
	    	return false;
		
	    //Connection conn = getConn();
	    String sql = "select * from employees where username = \""+username+ "\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        rs.next();
	        if(rs.getInt(9)==0){
	        	pstmt.close();
	 	        //conn.close();
	        	return false;
	        }
	        	
	        else {
	        	pstmt.close();
	 	        //conn.close();
	        	return true;
	        }
	     
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static boolean IsUserpasswordCorrect(String username,String password) {
	    //Connection conn = getConn();
		if (IsUserExists(username)==false)
	    	return false;
		
	    String sql = "select * from employees where username = \""+username+ "\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        rs.next();
	        String temp=rs.getString(3);
	        if(temp.isEmpty()){
	        	return false;
	        }
	        	
	        if(temp.equals(password)){
	        	pstmt.close();
		        //conn.close();
	        	return true;
	        }
	        else{
	        	pstmt.close();
		        //conn.close();
	        	return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static JSONObject getAllUserList(){
		String sql = "select * from employees";
		int row=0;
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				++row;
			}
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		
		JSONObject currentUserList = new JSONObject();
		currentUserList.put("amount", row);
	    PreparedStatement pstmt;
	    String tempName=null;
	    int tempState=0;
	    String state="offline";
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	            tempName=rs.getString(2);
	            tempState=rs.getInt(9);
	            if(tempState==0)
	            	state="offline";
	            else
	            	state="online";
	            currentUserList.put(""+i, (new JSONObject()).put("username", tempName).put("userstate", state));
	            ++i;
	            System.out.println(tempName+"\t"+state);
	        }
	            System.out.println("===================================");
	            pstmt.close();
	            return currentUserList;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean StoreChatRecord(String msgFrom, String msgTo, long msgTime,String type,String content){
			PreparedStatement pstmt;
		    String sql = "insert into records (msgfrom,msgto,msgtime,type,content) values(?,?,?,?,?)";
			try {
				 pstmt = (PreparedStatement) conn.prepareStatement(sql);
			     pstmt.setString(1, msgFrom);
			     pstmt.setString(2, msgTo);	
		    	 pstmt.setString(3, ""+msgTime);
			     
			     if(type==null)
			    	 pstmt.setString(4, null);
			     else
			    	 pstmt.setString(4, type);
			     
			     if(content==null)
			    	 pstmt.setString(5, null);
			     else
			    	 pstmt.setString(5, content);
			     
			     pstmt.executeUpdate();
			     pstmt.close();

			     return true;
			    } catch (SQLException e) {
			        e.printStackTrace();
			    }
			    return false;
	}
	
	public static JSONObject getAllRecords(String msgFrom, String msgTo){
		String sql = "select * from records";
		int amount=0;
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				if(rs0.getString(2).equals(msgFrom) && rs0.getString(3).equals(msgTo))
					++amount;
				else if(rs0.getString(3).equals(msgFrom) && rs0.getString(2).equals(msgTo))
					++amount;
			}
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return null;
		}
		
		JSONObject allrecords = new JSONObject();
		allrecords.put("amount", amount+"");
	    PreparedStatement pstmt;
	    String tempFrom=null;
	    String tempTo=null;
	    long time=0;
	    String tempType=null;
	    String tempContent = null;
	    
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	        	if((rs.getString(2).equals(msgFrom) && rs.getString(3).equals(msgTo))||(rs.getString(3).equals(msgFrom) && rs.getString(2).equals(msgTo))){
	        		tempFrom = rs.getString(2);
	        		tempTo=rs.getString(3);
	        		time=rs.getLong(4);
	        		tempType=rs.getString(5);
	        		tempContent= rs.getString(6);
	        		allrecords.put(""+i, (new JSONObject()).put("from", tempFrom).put("to", tempTo).put("date", time+"").put("message_content", tempContent));
		            ++i;
		            System.out.println(tempFrom+"\t"+tempTo+"\t"+time+"\t"+tempContent);
	        	}
	        }
	            System.out.println("===================================");
	            pstmt.close();
	            return allrecords;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null;
	}
	
	public static boolean IsUserInGroup(String group,String user) {
	    String sql = "select * from groups where username = \""+user+ "\" AND groupname= \""+group+"\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()!=false){
	        	pstmt.close();
	        	return true;
	        }else{
	        	pstmt.close();
	        	return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}
	
	public static boolean StoreGroupMember(String group, String name){
		if(IsUserInGroup(group,name)==true){
			return false;
		}
		
		PreparedStatement pstmt;
	    String sql = "insert into groups (groupname,username) values(?,?)";
		try {
			 pstmt = (PreparedStatement) conn.prepareStatement(sql);
		     
		     if(group==null)
		    	 return false;
		     else
		    	 pstmt.setString(1, group);
		     
		     if(name==null)
		    	 return false;
		     else
		    	 pstmt.setString(2, name);
		     
		     pstmt.executeUpdate();
		     pstmt.close();
		     return true;
		} catch (SQLException e) {
		        e.printStackTrace();
	    }
	    return false;
	}
	
	public static JSONObject getAllGroupList(String username){
		String sql = "select groupname from groups group by groupname;";
		int amount=0;
		JSONObject allrecords = new JSONObject();
		
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				++amount;
			}
			allrecords.put("amount", amount+"");
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			allrecords.put("amount", "0");
			return allrecords;
		}
		
		
		
	    PreparedStatement pstmt;
	    String groupname=null;
	    int joined=0;
	    
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	        	groupname=rs.getString(1);
	        	if(IsUserInGroup(groupname,username)==true)
	        		joined=1;
	        	else
	        		joined = 0;
	        	allrecords.put(""+i, (new JSONObject()).put("groupname", groupname).put("groupimage", "").put("selfJoined", joined+""));
	            ++i;
	            System.out.println(i+"\t"+groupname+"\t"+ joined);
	        }
	        System.out.println("===================================");
	        pstmt.close();
	        return allrecords;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return allrecords;
	}
	
	public static boolean StoreChatRecord_group(String fromUsername, String toGroupname,long recordTime,String type,String content){
		PreparedStatement pstmt;
	    String sql = "insert into records_group (fromUsername,toGroupname,recordTime,type,content) values(?,?,?,?,?)";
		try {
			 pstmt = (PreparedStatement) conn.prepareStatement(sql);
		     pstmt.setString(1, fromUsername);
		     pstmt.setString(2, toGroupname);	
	    	 pstmt.setString(3, ""+recordTime);
		     
		     if(type==null)
		    	 pstmt.setString(4, null);
		     else
		    	 pstmt.setString(4, type);
		     
		     if(content==null)
		    	 pstmt.setString(5, null);
		     else
		    	 pstmt.setString(5, content);
		     
		     pstmt.executeUpdate();
		     pstmt.close();

		     return true;
		} catch (SQLException e) {
	        e.printStackTrace();
	    }
		return false;
	}
	
	public static JSONObject getAllRecords_group(String toGroupName){
		String sql = "select * from records_group";
		int amount=0;
		JSONObject allrecords = new JSONObject();
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				if(rs0.getString(3).equals(toGroupName))
					++amount;
			}
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return allrecords;
		}
		allrecords.put("amount", amount+"");
		
	    PreparedStatement pstmt;
	    String tempFrom=null;
	    String tempTo=null;
	    long time=0L;
	    String tempType=null;
	    String tempContent = null;
	    
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	        	if( rs.getString(3).equals(toGroupName)){
	        		tempFrom = rs.getString(2);
	        		tempTo=rs.getString(3);
	        		time=rs.getLong(4);
	        		tempType=rs.getString(5);
	        		tempContent= rs.getString(6);
	        		allrecords.put(""+i, (new JSONObject()).put("from", tempFrom).put("to", tempTo).put("date", time+"").put("message_content", tempContent));
		            ++i;
		            System.out.println(tempFrom+"\t"+tempTo+"\t"+time+"\t"+tempContent);
	        	}
	        }
	            System.out.println("===================================");
	            pstmt.close();
	            return allrecords;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return allrecords;
	}
	
	public static JSONObject getAllUsers(){
		String sql = "select * from employees";
		int row=0;
		JSONObject currentUserList = new JSONObject();
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				++row;
			}
			currentUserList.put("amount", row+"");
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return currentUserList;
		}
		
	    PreparedStatement pstmt;
	    String name=null;
	    String sex=null;
	    String nickname = null;
	    String password = null;
	    String email= null;
	    String telephone =null;
	    String department = null;

	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	            name=rs.getString(2);
	            password = rs.getString(3);
	            nickname = rs.getString(4);
	            email = rs.getString(5);
	            department = rs.getString(6);
	            sex=rs.getString(7);
	            telephone = rs.getString(8);

	            currentUserList.put(""+i, (new JSONObject()).put("username", name).put("sex", sex).put("nickname", nickname).put("password",password).put("email", email).put("telephone", telephone).put("department", department));
	            ++i;
	            System.out.println(name+" "+sex+" "+nickname+" "+ password+" "+email+" "+telephone+" "+ department);
	        }
	            System.out.println("===================================");
	            pstmt.close();
	            return currentUserList;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return currentUserList;
	}
	
	public static JSONObject searchUser(String value){
		String sql = "select * from employees where username like \"%"+value+"%\" OR nickname like \"%"+value+"%\" OR email like \"%"+value+"%\";";
		int row=0;
		JSONObject currentUserList = new JSONObject();
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				++row;
			}
			currentUserList.put("amount", row+"");
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return currentUserList;
		}
		
	    PreparedStatement pstmt;
	    String name=null;
	    String sex=null;
	    String nickname = null;
	    String password = null;
	    String email= null;
	    String telephone =null;
	    String department = null;

	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	            name=rs.getString(2);
	            password = rs.getString(3);
	            nickname = rs.getString(4);
	            email = rs.getString(5);
	            department = rs.getString(6);
	            sex=rs.getString(7);
	            telephone = rs.getString(8);

	            currentUserList.put(""+i, (new JSONObject()).put("username", name).put("sex", sex).put("nickname", nickname).put("password",password).put("email", email).put("telephone", telephone).put("department", department));
	            ++i;
	            System.out.println(name+" "+sex+" "+nickname+" "+ password+" "+email+" "+telephone+" "+ department);
	        }
	            System.out.println("===================================");
	            pstmt.close();
	            return currentUserList;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return currentUserList;
	}
	
	public static JSONObject getUserAllProfile(String username){
		JSONObject user = new JSONObject();
		if (IsUserExists( username)==false){
			user.put("username", "").put("sex", "").put("nickname", "").put("password","").put("email", "").put("telephone", "").put("department", "");
			return user;
		}else{
			Employee ee = getUserProfile( username);
			user.put("username", ee.getName()).put("sex", ee.getGender()).put("nickname", ee.getNickname()).put("password",ee.getPassword()).put("email", ee.getEmail()).put("telephone", ee.getTelephone()).put("department", ee.getDepartment());
			 System.out.println(ee.getName()+" "+ee.getGender()+" "+ee.getNickname()+" "+ ee.getPassword()+" "+ee.getEmail()+" "+ee.getTelephone()+" "+ ee.getDepartment());
			return user;
		}
	}
	
	public static Boolean modifyUserProfile(JSONObject user) {

		if (IsUserExists(user.getString("username"))==false)
	    	return false;
		
	    PreparedStatement pstmt;
	    try { 
	        String sql = "update employees set password=\"" + user.getString("password") + "\" where username= \"" +user.getString("username")+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(user.getString("nickname")!=null){
		        sql = "update employees set nickname=\"" + user.getString("nickname") + "\" where username= \"" +user.getString("username")+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }else{
	        	sql = "update employees set nickname = null where username= \"" +user.getString("username")+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }
	        
	        if(user.getString("email")!=null){
	        	sql = "update employees set email=\"" + user.getString("email") + "\" where username= \"" +user.getString("username")+"\"";
	        	pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        	pstmt.executeUpdate();
	        }else{
	        	sql = "update employees set email = null where username= \"" +user.getString("username")+"\"";
		        pstmt = (PreparedStatement) conn.prepareStatement(sql);
		        pstmt.executeUpdate();
	        }
	        
	        if(user.getString("department")!=null)
	        	sql = "update employees set department=\"" + user.getString("department") + "\" where username= \"" +user.getString("username")+"\"";
	        else
	        	sql = "update employees set department = null where username= \"" +user.getString("username")+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(user.getString("sex")!=null)
	        	sql = "update employees set gender=\"" + user.getString("sex") + "\" where username= \"" +user.getString("username")+"\"";
	        else
	        	sql = "update employees set gender = null where username= \"" +user.getString("username")+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        if(user.getString("telephone")!=null)
	        	sql = "update employees set telephone=\"" + user.getString("telephone") + "\" where username= \"" +user.getString("username")+"\"";
	        else
	        	sql = "update employees set telephone = null where username= \"" +user.getString("username")+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        
	        pstmt.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	public static JSONObject getGroupMembers(String groupname){
		String sql = "select username from groups where groupname = \""+groupname+"\"" ;
		int amount=0;
		JSONObject allrecords = new JSONObject();
		
		PreparedStatement pstmt0;
		try {
			pstmt0 = (PreparedStatement)conn.prepareStatement(sql);
			ResultSet rs0 = pstmt0.executeQuery();
			while(rs0.next()){
				++amount;
			}
			allrecords.put("amount", amount+"").put("name", groupname);
			pstmt0.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
			return allrecords;
		}
		
	    PreparedStatement pstmt;
	    String name=null;
	    
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println("===================================");
	        int i=0;
	        while (rs.next()) {
	        	name=rs.getString(1);
	        	allrecords.put(""+i, name);
	            ++i;
	            System.out.println(i+":"+name);
	        }
	        System.out.println("===================================");
	        pstmt.close();
	        return allrecords;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return allrecords;
	}
	
	public static boolean isGroupExisting(String groupname){
	    String sql = "select * from groups where groupname= \""+groupname+"\";";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement)conn.prepareStatement(sql);
	        ResultSet rs = pstmt.executeQuery();
	        if(rs.next()!=false){
	        	pstmt.close();
	        	return true;
	        }else{
	        	pstmt.close();
	        	return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    
	    return false;
	}
	
	public static boolean deleteGroup(String groupname){
		if(isGroupExisting(groupname)==false){
			return false;
		}
		
		String sql0 = "delete from records_group where toGroupname=\"" + groupname + "\"";
		    PreparedStatement pstmt0;
		    try {
		        pstmt0 = (PreparedStatement) conn.prepareStatement(sql0);
		        pstmt0.executeUpdate();
		        pstmt0.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		
	    String sql = "delete from groups where groupname=\"" + groupname+"\"";
	    PreparedStatement pstmt;
	    try {
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate();
	        pstmt.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public static boolean deleteUser(String username){
		if(IsUserExists(username)==false){
			return false;
		}
		
		String sql0 = "delete from records_group where fromUsername=\"" + username + "\"";
		    PreparedStatement pstmt0;
		    try {
		        pstmt0 = (PreparedStatement) conn.prepareStatement(sql0);
		        pstmt0.executeUpdate();
		        pstmt0.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return false;
		    }
		
	    String sql1 = "delete from groups where username=\"" + username+"\"";
	    PreparedStatement pstmt1;
	    try {
	        pstmt1 = (PreparedStatement) conn.prepareStatement(sql1);
	        pstmt1.executeUpdate();
	        pstmt1.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	    
	    String sql2 = "delete from employees where username=\"" + username+"\"";
	    PreparedStatement pstmt2;
	    try {
	        pstmt2 = (PreparedStatement) conn.prepareStatement(sql2);
	        pstmt2.executeUpdate();
	        pstmt2.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	    
	    String sql3 = "delete from records where msgfrom=\"" + username+"\" OR msgto = \""+username+"\";";
	    PreparedStatement pstmt3;
	    try {
	        pstmt3 = (PreparedStatement) conn.prepareStatement(sql3);
	        pstmt3.executeUpdate();
	        pstmt3.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();   
	    }
	    return false;
	}

	public static boolean isUserAdmin(String username){
		if(IsUserVerified(username)==true){
			return true;
		}else
			return false;
	}
	
	public static boolean SetUserAdmin(String username,int state) {
		if (IsUserExists(username)==false)
	    	return false;
		
	    PreparedStatement pstmt;
	    try {
	        String sql = "update employees set verfied = " + state + " where username = \"" + username+"\"";
	        pstmt = (PreparedStatement) conn.prepareStatement(sql);
	        pstmt.executeUpdate(); 
	        pstmt.close();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
