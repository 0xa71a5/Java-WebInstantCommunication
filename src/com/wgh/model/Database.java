package com.wgh.model;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.wgh.model.GlobalObject;

public class Database {
	public Connection conn;
	
	public Database()
	{
		conn=null;
	}
	
	public void BuildConnection()
	{
		conn=getConn();
		if(conn!=null)
		{
			System.out.print("Connection to sql server built success!\n");
		}
	}
	
	public void SelectTest()
	{
		String command="select * from students";
		PreparedStatement pstmt;
		try {
	        pstmt = (PreparedStatement)conn.prepareStatement(command);
	        ResultSet rs = pstmt.executeQuery();
	        int col = rs.getMetaData().getColumnCount();
	        System.out.println("============================");
	        while (rs.next()) {
	            for (int i = 1; i <= col; i++) {
	                System.out.print(rs.getString(i) + "\t");
	             }
	            System.out.println("");
	        }
	            System.out.println("============================");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	private static Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://119.29.5.72:3306/wwwdata";
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

	public static void main(String val[]) throws NoSuchAlgorithmException
	{
		Random random = new Random();
		String username="lxc";
		
		String jessonid=GlobalObject.GenerateJessonid(username);
		System.out.println(jessonid+"\t"+jessonid.length());
	}
}


