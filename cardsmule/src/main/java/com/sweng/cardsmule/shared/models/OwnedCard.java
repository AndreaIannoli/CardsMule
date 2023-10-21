package com.sweng.cardsmule.shared.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class OwnedCard extends UserCard implements Serializable{
	private static final long serialVersionUID = -7343121676086446226L;
    private String id;
    private String description;
    private int referenceCardId;
	private static final AtomicInteger uniqueId = new AtomicInteger();
	
	public OwnedCard(int referenceCardId, Grade grade, CardsmuleGame cardGame, String userEmail, String description) {
		super(referenceCardId, grade, cardGame, userEmail);
		this.id = uniqueId.getAndIncrement() + "";
		this.description = description;
		this.referenceCardId=referenceCardId;
	}
	
	public OwnedCard() {
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}
	public int getCardId() {
		return referenceCardId;
	}
	
	public CardsmuleGame getGameType() {

		 return id.charAt(0) == 'm' ? CardsmuleGame.MAGIC :

		 id.charAt(0) == 'y' ? CardsmuleGame.YUGIOH :

		 id.charAt(0) == 'p' ? CardsmuleGame.POKEMON :

		 null;

		 }
}
