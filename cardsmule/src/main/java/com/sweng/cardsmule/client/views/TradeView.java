package com.sweng.cardsmule.client.views;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.models.Offer;

public interface TradeView extends IsWidget {
	void setFromYouOfferList(List<Offer> offer);

    void setToYouOfferList(List<Offer> offer);

    void resetOfferLists();

    void setPresenter(Presenter presenter);
	interface Presenter {
        void goTo(Place place);
    }
}
