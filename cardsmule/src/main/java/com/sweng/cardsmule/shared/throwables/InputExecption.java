package com.sweng.cardsmule.shared.throwables;

public class InputExecption extends GeneralException {

    private static final long serialVersionUID = -5839756177404814542L;

    public InputExecption(String errorMessage) {
        super(errorMessage);
    }

    public InputExecption() {
    }
}