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

import com.wgh.database.DBOperation;
import com.wgh.model.GlobalObject;

public class wsGet  extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    String myUsername=(String)request.getSession().getAttribute("username");
	    if(myUsername==null)myUsername="";
		System.out.println(myUsername+" Get admin html");
		//System.out.println(myUsername+" is adminintator?"+DBOperation.isUserAdmin(myUsername));
		//if(DBOperation.isUserAdmin(myUsername))
			request.getRequestDispatcher("admin.html").forward(request, response);
		//else
		//	response.sendRedirect("login?error=1");
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Post admin html");
		request.getRequestDispatcher("admin.html").forward(request, response);
	}
}
