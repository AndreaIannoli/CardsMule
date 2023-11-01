package com.sweng.cardsmule.shared;

import java.io.Serializable;
import java.util.List;

import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public class OfferPayload implements Serializable {
	private String senderEmail;
    private String receiverEmail;
    private List<OwnedCardFetched> senderCards;
    private List<OwnedCardFetched> receiverCards;

    public OfferPayload(String senderEmail, String receiverEmail, List<OwnedCardFetched> senderCards, List<OwnedCardFetched> receiverCards) {
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.senderCards = senderCards;
        this.receiverCards = receiverCards;
    }

    public OfferPayload() {
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public List<OwnedCardFetched> getSenderCards() {
        return senderCards;
    }

    public List<OwnedCardFetched> getReceiverCards() {
        return receiverCards;
    }
}
