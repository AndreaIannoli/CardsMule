package com.sweng.cardsmule.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;

import java.util.List;
public interface CardServiceAsync {
    void getGameCards(CardsmuleGame game, AsyncCallback<List<SwengCard>> callback);
}
