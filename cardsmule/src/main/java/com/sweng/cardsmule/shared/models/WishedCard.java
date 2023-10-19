package com.sweng.cardsmule.shared.models;

import java.io.Serializable;

public class WishedCard extends UserCard implements Serializable{
	private static final long serialVersionUID = 5619439952671468330L;
	
	public WishedCard() {}
	
	public WishedCard(int referenceCardId, Grade grade, CardsmuleGame cardGame, String userEmail) {
		super(referenceCardId, grade, cardGame, userEmail);
	}
}
