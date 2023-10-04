package com.sweng.cardsmule.shared.throwables;

public class AuthenticationException extends GeneralException {
	private static final long serialVersionUID = -747833952591914235L;
    

    public AuthenticationException() {}

	public AuthenticationException(String exceptionText) {
        super(exceptionText);
    }
}
