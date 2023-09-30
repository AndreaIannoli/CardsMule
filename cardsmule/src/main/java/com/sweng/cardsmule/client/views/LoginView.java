package com.sweng.cardsmule.client.views;

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface LoginView extends IsWidget {
    void setPresenter(Presenter presenter);

    void displayAlert(String message);

    void setAuthToken(String token);
    
    void resetFields();

    interface Presenter {
    	void signIn(String username, String password);
    	
        void goTo(Place place);
    }
}
