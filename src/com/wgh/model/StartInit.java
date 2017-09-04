package com.wgh.model;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.wgh.database.DBOperation;

public class StartInit implements ServletContextListener {  
    
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("系统停止2...");  
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("系统初始化开始");  
		System.out.println("建立数据库连接");  
		DBOperation.ConnectionOpen();
		System.out.println("完成数据库连接");  
	}  
      
}  