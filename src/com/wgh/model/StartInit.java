package com.wgh.model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.wgh.database.DBOperation;

public class StartInit implements ServletContextListener {  
    
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("ϵͳֹͣ2...");  
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("ϵͳ��ʼ����ʼ");  
		System.out.println("�������ݿ�����");  
		DBOperation.ConnectionOpen();
		System.out.println("������ݿ�����");  
	}  
      
}  