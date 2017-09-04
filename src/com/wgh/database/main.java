package com.wgh.database;

public class main {

	public static void main(String[] args) {
		//Construct the connection to DB
		DBOperation.ConnectionOpen();
		
		//Check whether there is a user called a specific name. Return true if it is successful.
		if (DBOperation.IsUserExists("Alice")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		if (DBOperation.IsUserExists("J")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//No longer use again.
		if (DBOperation.IsUserVerified("Ben")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		if (DBOperation.IsUserVerified("Alice")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//Create a user in database. Return true if it is successful.
		Employee ee = new Employee("Linda","21p9");
		ee.setGender("female");
		ee.setTelephone("12345678901");
		ee.setNickname("12d9");
		DBOperation.CreateUser(ee);
		
		DBOperation.CreateUser(new Employee("Alice","912"));
		
		//Get user's profile. Return true if it is successful.
		//Return null if fails.
		Employee ee1 = DBOperation.getUserProfile("Lore");
		Employee ee2 = DBOperation.getUserProfile("Drake");
		
		//Set user login state. Return true if it is successful.
		if(DBOperation.SetUserLoginState("Drake", 1)==true)
			System.out.println("true");
		//Get user login state. Return true if it is successful.
		if(DBOperation.GetUserLoginState("Drake")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		if(DBOperation.GetUserLoginState("Peter")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//Update user's information,which uses username to judge. Return true if it is successful.
		Employee ee3 = new Employee("Peter","19'saz");
		ee3.setState(0);
		ee3.setVerified(1);
		DBOperation.UpdateUserProfile(ee);
		
		//Check user's passowrd. Return true if it is successful.
		if(DBOperation.IsUserpasswordCorrect("Tom", "abc123")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		if(DBOperation.IsUserpasswordCorrect("Tom", "sajdl")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//Get all user infomation.
		DBOperation.getAllUserList();
		
		//Store chat message
		DBOperation.StoreChatRecord("Alice", "Tom", 100002233, "msg", "OK!");
		
		//Get all records
		DBOperation.getAllRecords("Alice","Tom");
		DBOperation.getAllRecords("Tom","Alice");
		DBOperation.getAllRecords("Peter","Tom");
		
		//Judge whether a user is in a specific group.
		if(DBOperation.IsUserInGroup("g1", "Tom")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		if(DBOperation.IsUserInGroup("g1", "Lili")==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//Store a member in a group
		DBOperation.StoreGroupMember("g4", "Lee");
		if(DBOperation.IsUserInGroup("g4", null)==true)
			System.out.println("true");
		else 
			System.out.println("false");
		
		//Get all group list.
		
		//Close the connection to DB
		DBOperation.ConnectionClose();
	}
}
