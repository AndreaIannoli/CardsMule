package com.sweng.cardsmule.shared.throwables;

import java.io.Serializable;

public class GeneralException extends Exception implements Serializable {
    private static final long serialVersionUID = -515408899816697344L;
	private String exceptionText;
	
    public GeneralException() {}

    public GeneralException(String exceptionText) {
        this.exceptionText = exceptionText;
    }

    public String getExceptionText() {
        return exceptionText;
    }
}
