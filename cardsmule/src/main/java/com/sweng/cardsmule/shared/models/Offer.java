package com.sweng.cardsmule.shared.models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;


public class Offer implements Serializable{
	private static final long serialVersionUID = 997487558313555532L;
	//è stato tolto uno static nel caso si spaccasse a riga 12
    private static final AtomicInteger uniqueId = new AtomicInteger();
    private int id;
    private String senderUserEmail;
    private String receiverUserEmail;
    private List<OwnedCard> senderOwnedCardCards;
    private List<OwnedCard> receiverOwnedCardCards;
    private long date;

    public Offer(String senderUserEmail, String receiverUserEmail, List<OwnedCard> senderOwnedCardCards, List<OwnedCard> receiverOwnedCardCards) {
        this.id = uniqueId.getAndIncrement();
        this.senderUserEmail = senderUserEmail;
        this.receiverUserEmail = receiverUserEmail;
        this.senderOwnedCardCards = senderOwnedCardCards;
        this.receiverOwnedCardCards = receiverOwnedCardCards;
        this.date = System.currentTimeMillis();
    }

    public Offer() {
    }

    public int getId() {
        return id;
    }

    public String getSenderUserEmail() {
        return senderUserEmail;
    }

    public String getReceiverUserEmail() {
        return receiverUserEmail;
    }

    public List<OwnedCard> getSenderOwnedCards() {
        return senderOwnedCardCards;
    }

    public List<OwnedCard> getReceiverOwnedCards() {
        return receiverOwnedCardCards;
    }

    public long getDate() {
        return date;
    }

    
    ///da guardare e capire questa roba, entrambe i metodi
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Offer overture = (Offer) o;
        return id == overture.id &&
                ((Offer) o).getSenderUserEmail().equals(overture.getSenderUserEmail()) &&
                ((Offer) o).getReceiverUserEmail().equals(overture.getReceiverUserEmail()) &&
                ((Offer) o).getSenderOwnedCards().equals(overture.getSenderOwnedCards()) &&
                ((Offer) o).getReceiverOwnedCards().equals(overture.getReceiverOwnedCards());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, senderUserEmail, receiverUserEmail, senderOwnedCardCards, receiverOwnedCardCards, date);
    }
}
