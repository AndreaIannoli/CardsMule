package com.sweng.cardsmule.shared.models;

public class OwnedCardFetched extends OwnedCard{
    private static final long serialVersionUID = -7014908512227177447L;
	String name;
	
    public OwnedCardFetched(OwnedCard ownedCard, String name) {
    	super(ownedCard.getReferenceCardId(), ownedCard.getGrade(), ownedCard.getCardGame(), ownedCard.getUserEmail(), ownedCard.getDescription());
    	this.name = name;
    }
    
    public OwnedCardFetched() {}
    
    public String getName() {
        return name;
    }
}
