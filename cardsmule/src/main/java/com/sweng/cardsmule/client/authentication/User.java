package com.sweng.cardsmule.client.authentication;

public class User {
    String token;
    String username;
	
	public void setCredentials(String token, String username) {
        this.token = token;
        this.username = username;
    }
	
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return token != null;
    }
}
