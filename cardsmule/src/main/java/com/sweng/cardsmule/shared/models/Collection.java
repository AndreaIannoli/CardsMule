package com.sweng.cardsmule.shared.models;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class Collection implements Serializable{
	private static final long serialVersionUID = 6420706384078663122L;
	private String name;
    private Set<OwnedCard> ownedCards;
    private boolean isDeck;
    
    public Collection(String name) {
    	this.name = name;
    	ownedCards = new LinkedHashSet<>();
    	this.isDeck = false;
    }
    
    public Collection(String name, boolean isDeck) {
    	this.name = name;
    	ownedCards = new LinkedHashSet<>();
    	this.isDeck = isDeck;
    }
    
    public Collection() {
    }
    
    public String getName() {
        return name;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collection collection = (Collection) o;
        return name.equals(collection.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

	public Set<OwnedCard> getOwnedCards() {
		return ownedCards;
	}
	
    public boolean containsOwnedCard(OwnedCard ownedCard) {
        return ownedCards.contains(ownedCard);
    }
    
    public boolean addOwnedCard(OwnedCard ownedCard) {
        return ownedCards.add(ownedCard);
    }

    public boolean removeOwnedCard(OwnedCard ownedCard) {
        return ownedCards.remove(ownedCard);
    }

	public boolean isDeck() {
		return isDeck;
	}
}
