package com.sweng.cardsmule.shared;

import java.io.Serializable;

public class LoginSession implements Serializable {
    private static final long serialVersionUID = -4661136558563918355L;
	private String username;
    private long loginTime;
    private String email;

    public LoginSession(String username, String email, long loginTime) {
        this.username = username;
        this.email=email;
        this.loginTime = loginTime;
        
    }

    public LoginSession() {
    }

    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }

    public long getLoginTime() {
        return loginTime;
    }
}
