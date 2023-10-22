package com.sweng.cardsmule.client.place;

import com.google.gwt.place.shared.Place;

public class TradePlace extends Place{
	private final Integer offerId;

    public TradePlace(Integer offerId) {
        this.offerId = offerId;
    }

    public Integer getOfferId() {
        return offerId;
    }
}
