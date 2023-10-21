package com.sweng.cardsmule.shared;

import java.util.List;

import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.shared.models.OwnedCard;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.GeneralException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;

@RemoteServiceRelativePath("exchanges")
public interface TradeCardsService extends RemoteService {
	boolean addOffer(String token, String receiverUserEmail, List<OwnedCard> senderOwnedCards, List<OwnedCard> receiverOwnedCards) throws GeneralException;

    OfferPayload getOffer(String token, int offerId) throws GeneralException;

    boolean acceptOffer(String token, int offerId) throws AuthenticationException, OfferNotFoundException;

    List<Offer> getOfferListReceived(String token) throws GeneralException;

    List<Offer> getOfferListSent(String token) throws GeneralException;

    boolean refuseOrWithdrawOffer(String token, int proposalId) throws GeneralException;
}
