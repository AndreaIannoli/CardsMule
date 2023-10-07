package com.sweng.cardsmule.shared;
import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.InputException;




@RemoteServiceRelativePath("users")
public interface CardService extends RemoteService{
	List<SwengCard> getGameCards(CardsmuleGame game) throws InputException;
    SwengCard getGameCard(CardsmuleGame game, int cardId) throws InputException;

}
