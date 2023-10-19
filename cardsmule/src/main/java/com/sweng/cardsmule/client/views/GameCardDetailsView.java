package com.sweng.cardsmule.client.views;

import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.WishedCard;

public interface GameCardDetailsView extends IsWidget {
    void setData(SwengCard data);
    
    void displayAlert(String message);
    
    void hideModal();
    
    String getDeckSelected();
    
    void setPresenter(Presenter presenter);
	
    interface Presenter {
        void goTo(Place place);
        void addCardToDeck(String deckName, String grade, String description);
        void update();
        void fetchCard();
    }
    
    void setWishList(List<WishedCard> wisherList);

    void setOwnList(List<OwnedCard> ownerList);
    
    void createUserWidgets(boolean isLoggedIn);
}
