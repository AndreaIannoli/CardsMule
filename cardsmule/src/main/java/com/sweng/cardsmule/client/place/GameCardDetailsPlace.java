package com.sweng.cardsmule.client.place;

import com.google.gwt.place.shared.Place;
import com.sweng.cardsmule.shared.models.CardsmuleGame;

public class GameCardDetailsPlace extends Place{
    private final int idCard;
    private final CardsmuleGame game;

    public GameCardDetailsPlace(int idCard, CardsmuleGame game) {
        this.idCard = idCard;
        this.game = game;
    }
    
    public int getIdCard() {
        return idCard;
    }

    public CardsmuleGame getGame() {
        return game;
    }
}
