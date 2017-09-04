package com.wgh.servlet;

import com.wgh.database.DBOperation;
import com.wgh.model.GlobalObject;
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

public class login  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		System.out.println("Enter login handler get");
		request.getRequestDispatcher("login&regist.html").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Logger log = Logger.getLogger("InstantCommunication"); 
		log.setLevel(Level.INFO);

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		log.info("username="+username+"\tpassword="+password);
		if(username==null)
			log.warning("username == null ,true");
		
		if(DBOperation.IsUserpasswordCorrect(username, password)==true)
		{
			HttpSession session = request.getSession();
			//设置各种环境变量  创建用户名索引等等
			session.setAttribute("username", username);
			session.setAttribute("password", password);
			session.setAttribute("groupname", "testgroup");
			session.setAttribute("loginState", "True");
			
			String jessonid=GlobalObject.GenerateJessonid(username);
			session.setAttribute("jessonid",jessonid );
			response.addCookie(new Cookie("jessonid", jessonid));
			response.addCookie(new Cookie("username", username));
			response.addCookie(new Cookie("groupname", "testgroup"));
			response.addCookie(new Cookie("loginState", "True"));
			
			GlobalObject.StoreMap(username, jessonid);//存储全局的username身份索引
			System.out.print("Password correct ,redirect to chatRoom\n");
			
			response.sendRedirect("chatRoom");
		}
		else
		{
			HttpSession session = request.getSession();
			session.setAttribute("loginState", "False");
			System.out.print("Password wrong ,redirect to login\n");
			response.sendRedirect("login?error=1");
		}
		//request.getRequestDispatcher("index.html").forward(request, response);
	}
}
