package com.sweng.cardsmule.shared;

import java.io.Serializable;

public class CredentialsPayload implements Serializable {
    private static final long serialVersionUID = -3190112327777933693L;
	private String token;
    private String username;
    private String email;
    
    public CredentialsPayload() {
    }

    public CredentialsPayload(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }
    
    public CredentialsPayload(String token, String username) {
        this.token = token;
        this.username = username;
    }
    
    
    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
    
    public String getEmail() {
    	return email;
    }
}
