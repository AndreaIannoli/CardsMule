package com.sweng.cardsmule.client.authentication;

public class User {
    String token;
    String username;
    String email;
	
	public void setCredentials(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email= email;
    }
	
    public String getToken() {
        return token;
    }
    
    public void resetToken() {
    	this.token = null;
    }
    

    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
