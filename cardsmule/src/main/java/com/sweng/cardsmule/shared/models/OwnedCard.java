package com.sweng.cardsmule.shared.models;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import com.sweng.cardsmule.shared.GUID;

public class OwnedCard extends UserCard implements Serializable{
	private static final long serialVersionUID = 8585293339499177179L;
	private String id;
    private String description;
	private static final AtomicInteger uniqueId = new AtomicInteger();

	
	public OwnedCard(int referenceCardId, Grade grade, CardsmuleGame cardGame, String userEmail, String description) {
		super(referenceCardId, grade, cardGame, userEmail);
		this.id = GUID.get() + "";
		this.description = description;
	}
	
	public OwnedCard(String id, int referenceCardId, Grade grade, CardsmuleGame cardGame, String userEmail, String description) {
		super(referenceCardId, grade, cardGame, userEmail);
		this.id = id;
		this.description = description;
	}
	
	public OwnedCard() {
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		OwnedCard other = (OwnedCard) obj;
		return Objects.equals(id, other.id);
	}

	public String getDescription() {
		return description;
	}

	public String getId() {
		return id;
	}

   public CardsmuleGame getGameType() {
        return id.charAt(0) == 'm' ? CardsmuleGame.MAGIC :
                id.charAt(0) == 'y' ? CardsmuleGame.YUGIOH :
                        id.charAt(0) == 'p' ? CardsmuleGame.POKEMON :
                                null;
    }
   
   public OwnedCard copyWithModifiedStatusAndDescription(Grade newGrade, String newDescription) {
       return new OwnedCard(getId(), getReferenceCardId(), newGrade, getCardGame(), getUserEmail(), newDescription);
   }

}
