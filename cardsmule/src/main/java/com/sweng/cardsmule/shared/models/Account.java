package com.sweng.cardsmule.shared.models;

import java.io.Serializable;

public class Account implements Serializable {
    private static final long serialVersionUID = 2183464605930613608L;
	private String email;
	private String username;
    private String password;

    public Account(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public Account() {
    }

    public String getEmail() {
        return email;
    }
    
    public String getUsername() {
    	return username;
    }

    public String getPassword() {
        return password;
    }
}
