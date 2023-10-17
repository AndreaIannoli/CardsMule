package com.sweng.cardsmule.shared.models;

import java.io.Serializable;

import org.mapdb.Serializer;

import com.sweng.cardsmule.server.services.CardServiceImpl;

public abstract class UserCard implements Serializable{
    private static final long serialVersionUID = 4380922756641868270L;
	private int referenceCardId;
    private Grade grade;
    private CardsmuleGame cardGame;
	private String userEmail;
	
	public UserCard(int referenceCardId, Grade grade, CardsmuleGame cardGame, String userEmail) {
		this.referenceCardId = referenceCardId;
		this.grade = grade;
		this.cardGame = cardGame;
		this.userEmail = userEmail;
	}
	
	public UserCard() {
	}

	public int getReferenceCardId() {
		return referenceCardId;
	}

	public Grade getGrade() {
		return grade;
	}

	public CardsmuleGame getCardGame() {
		return cardGame;
	}

	public String getUserEmail() {
		return userEmail;
	}
}
