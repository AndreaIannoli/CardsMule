package com.sweng.cardsmule.shared;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.shared.models.OwnedCard;

public interface TradeCardsServiceAsync {
	void addOffer(String token, String receiverUserEmail, List<OwnedCard> senderOwnedCards, 
			List<OwnedCard> receiverOwnedCards, AsyncCallback<Boolean> callback);

    void getOffer(String token, int proposalId, AsyncCallback<OfferPayload> callback);

    void acceptOffer(String token, int offerId, AsyncCallback<Boolean> async);

    void getOfferListReceived(String token, AsyncCallback<List<Offer>> callback);

    void getOfferListSent(String token, AsyncCallback<List<Offer>> callback);

    void refuseOrWithdrawOffer(String token, int offerId, AsyncCallback<Boolean> async);
}
