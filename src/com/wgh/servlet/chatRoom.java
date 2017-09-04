package com.wgh.servlet;

import java.io.*;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.*;


public class chatRoom  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		HttpSession session = request.getSession();	
		System.out.println("Enter chatroom handler get");
		if(session.getAttribute("loginState")==null || !session.getAttribute("loginState").equals("True"))//Judge if user logined
		{
			session.setAttribute("loginState", "False");
			System.out.println("User not login ,redirect to /login");
			response.sendRedirect("login?error=0");
		}
		else					
			request.getRequestDispatcher("chatroom_old2.html").forward(request, response);
			//request.getRequestDispatcher("chatroom.html").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("chatroom.html").forward(request, response);
	}
}
