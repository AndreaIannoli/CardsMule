package com.sweng.cardsmule.shared.throwables;

public class CollectionNotFoundException extends GeneralException {
	private static final long serialVersionUID = 7460928263719004921L;

	public CollectionNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public CollectionNotFoundException() {
    }
}