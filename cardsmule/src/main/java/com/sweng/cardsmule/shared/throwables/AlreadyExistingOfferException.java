package com.sweng.cardsmule.shared.throwables;

public class AlreadyExistingOfferException extends GeneralException {
    private static final long serialVersionUID = -843261170954351248L;

	public AlreadyExistingOfferException(String errorMessage) {
        super(errorMessage);
    }

    public AlreadyExistingOfferException() {
    }
}