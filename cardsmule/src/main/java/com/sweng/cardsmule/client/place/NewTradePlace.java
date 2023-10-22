package com.sweng.cardsmule.client.place;

import com.google.gwt.place.shared.Place;

public class NewTradePlace extends Place {
    private final String selectedCardId;
    private final String receiverUserEmail;

    public NewTradePlace(String selectedCardId, String receiverUserEmail) {
        this.selectedCardId = selectedCardId;
        this.receiverUserEmail = receiverUserEmail;
    }
    public String getSelectedCardId() {
        return selectedCardId;
    }

    public String getReceiverUserEmail() {
        return receiverUserEmail;
    }
}
