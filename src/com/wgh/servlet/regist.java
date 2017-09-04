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

import com.wgh.database.*;

public class regist  extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("login&regist.html").forward(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//获取用户post的注册数据		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String department = request.getParameter("department");
		String telephone = request.getParameter("telephone");
		String checkcode =request.getParameter("checkcode");
		
		System.out.println("username="+username+"\tpassword="+password+"\tnickname="+nickname+"\temail="+email+"\tgender="+gender+"\tdepartment="+department+"\ttele="+telephone+"\tcheckcode="+checkcode);
		//将注册的信息插入到数据库中
		Employee ee = new Employee(username,password);
		ee.setGender(gender);//设置用户性别
		ee.setTelephone(telephone);//设置用户电话
		ee.setNickname(nickname);//设置用户昵称
		ee.setDepartment(department);//设置部门
		ee.setEmail(email);//设置邮箱号
		ee.setVerifycode("0000");//设置用户身份验证码 用在发邮件确认注册上  暂时先设置为0000
		ee.setState(0);//设置登陆状态为0 表示当前未登陆
		ee.setVerified(0);//设置身份为未验证状态
		if(DBOperation.CreateUser(ee))
		{
			System.out.println("Update user profile success ,username="+username);
		}
		else
		{
			System.out.println("Update user profile failed ,username="+username);
		}
		response.sendRedirect("login");
	}
}
