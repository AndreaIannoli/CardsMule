package com.sweng.cardsmule.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.models.SwengCard;

public interface GameCardDetailsView extends IsWidget {
    void setData(SwengCard data);
    
    void displayAlert(String message);
    
    void setPresenter(Presenter presenter);
	
    interface Presenter {
        void goTo(Place place);

        void fetchCard();
    }
}
