package com.sweng.cardsmule.shared.throwables;

public class InputException extends GeneralException {

    private static final long serialVersionUID = -5839756177404814542L;

    public InputException(String errorMessage) {
        super(errorMessage);
    }

    public InputException() {
    }
}