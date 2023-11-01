package com.sweng.cardsmule.shared.throwables;

public class OfferNotFoundException extends GeneralException  {
	
	private static final long serialVersionUID = 2680161982858496927L;

	public OfferNotFoundException(String errorMessage) {
        super(errorMessage);
    }

    public OfferNotFoundException() {
    }
}
