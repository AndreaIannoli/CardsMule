package com.sweng.cardsmule.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import java.util.List;

public interface NewTradeView extends IsWidget {

    void setData(String title, String cancelTextButton, String acceptTextButton);

    void setSenderDeck(boolean isClickable, List<OwnedCardFetched> ownedCardDecorators, String selectedCardId, String senderEmail);

    void setReceiverDeck(boolean isClickable, List<OwnedCardFetched> ownedCardDecorators, String selectedCardId, String receiverEmail);

    void setPresenter(NewTradePresenter newExchangePresenter);

    void setPresenter(TradePresenter exchangePresenter);

    void setAcceptButtonEnabled(boolean enabled);

    void showAlert(String message);

    void setNewTradeButtons();

    void setTradeButtons();

    void resetHandlers();

    interface NewTradePresenter {
        void createOffer(List<OwnedCard> senderDeckSelectedCards, List<OwnedCard> receiverDeckSelectedCards);
    }

    interface TradePresenter {

        void acceptTradeOffer();

        void refuseOrWithdrawOffer();

        void goTo(Place place);
    }
}
