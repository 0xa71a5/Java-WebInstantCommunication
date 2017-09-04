package com.wgh.database;

public class Employee {
	private String _id;
	private String _username;
	private String _password;
	private String _nickname;
	private String _email;
	private String _department;
	private String _gender;
	private String _telephone;
	private int _state;
	private int _verified;
	private String _verifycode;
	
	public Employee (String username, String password){
		this._id = null;
		this._username = username;
		this._password = password;
		this._nickname = null;
		this._email =null;
		this._department = null;
		this._gender = null;
		this._telephone = null;
		this._state = 0;
		this._verified = 0;
		this._verifycode = null;
	}
	
	public String getID(){
		return _id;
	}
	
    public void setID(String id) {
        this._id = id;
    }

    public String getName() {
        return _username;
    }

    public void setName(String Name) {
        this._username = Name;
    }

    public String getGender() {
        return _gender;
    }

    public void setGender(String Sex) {
        this._gender = Sex;
    }

    public String getPassword() {
        return _password;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email = email;
    }
    
    public String getDepartment() {
        return _department;
    }

    public void setDepartment(String department) {
        this._department = department;
    }
    
    public String getTelephone() {
        return _telephone;
    }

    public void setTelephone(String telephone) {
        this._telephone = telephone;
    }
    
    public int getState() {
        return _state;
    }

    public void setState(int state) {
        this._state = state;
    }
    
    public String getNickname() {
        return _nickname;
    }

    public void setNickname(String nickname) {
        this._nickname = nickname;
    }
    
    public int getVerified(){
    	return _verified;
    }
    
    public void setVerified(int verfied){
    	this._verified = verfied;
    }
    
    public String getVerifycode(){
    	return _verifycode;
    }
    
    public void setVerifycode(String vc){
    	this._verifycode = vc;
    }
}
