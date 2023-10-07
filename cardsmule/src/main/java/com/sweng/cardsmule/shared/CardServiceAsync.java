package com.sweng.cardsmule.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.InputException;

import java.util.List;
public interface CardServiceAsync {
    void getGameCards(CardsmuleGame game, AsyncCallback<List<SwengCard>> callback)throws InputException;
    void getGameCard(CardsmuleGame game, int cardId, AsyncCallback<SwengCard> callback)throws InputException;

}
