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
		//��ȡ�û�post��ע������		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String nickname = request.getParameter("nickname");
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String department = request.getParameter("department");
		String telephone = request.getParameter("telephone");
		String checkcode =request.getParameter("checkcode");
		
		System.out.println("username="+username+"\tpassword="+password+"\tnickname="+nickname+"\temail="+email+"\tgender="+gender+"\tdepartment="+department+"\ttele="+telephone+"\tcheckcode="+checkcode);
		//��ע�����Ϣ���뵽���ݿ���
		Employee ee = new Employee(username,password);
		ee.setGender(gender);//�����û��Ա�
		ee.setTelephone(telephone);//�����û��绰
		ee.setNickname(nickname);//�����û��ǳ�
		ee.setDepartment(department);//���ò���
		ee.setEmail(email);//���������
		ee.setVerifycode("0000");//�����û������֤�� ���ڷ��ʼ�ȷ��ע����  ��ʱ������Ϊ0000
		ee.setState(0);//���õ�½״̬Ϊ0 ��ʾ��ǰδ��½
		ee.setVerified(0);//�������Ϊδ��֤״̬
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
