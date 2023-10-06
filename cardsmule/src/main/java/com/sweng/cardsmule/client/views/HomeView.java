package com.sweng.cardsmule.client.views;
import java.util.List;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;
import com.sweng.cardsmule.shared.models.SwengCard;
public interface HomeView extends IsWidget{
	
	 void setPresenter(Presenter presenter);
	 void displayAlert(String message);
	 void resetFields();
	 void setAttributesAndtypes(List<SwengCard> cards);

	 interface Presenter {
	        void goTo(Place place);
	    }
}
