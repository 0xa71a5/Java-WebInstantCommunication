package com.wgh.servlet;

import com.wgh.database.DBOperation;
import com.wgh.model.GlobalObject;
import com.wgh.websocket.WebSocketHandler;

import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.http.Cookie;

public class leave  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		System.out.println("Enter leave handler get");
		System.out.println(session.getAttribute("username")+"leave ");
		String username=(String) session.getAttribute("username");
		if(username!=null && !username.equals(""))
			{
				DBOperation.SetUserLoginState(username, 0);
				WebSocketHandler.updateUserListToAll();
			}
		response.sendRedirect("login");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect("login");
	}
}
