package com.sweng.cardsmule.shared;

import java.io.Serializable;

public class LoginSession implements Serializable {
    private static final long serialVersionUID = -4661136558563918355L;
	private String username;
    private long loginTime;

    public LoginSession(String username, long loginTime) {
        this.username = username;
        this.loginTime = loginTime;
    }

    public LoginSession() {
    }

    public String getUsername() {
        return username;
    }

    public long getLoginTime() {
        return loginTime;
    }
}
