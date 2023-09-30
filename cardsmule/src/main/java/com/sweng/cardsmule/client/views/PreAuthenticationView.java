package com.sweng.cardsmule.client.views;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.IsWidget;

public interface PreAuthenticationView extends IsWidget {
    void setPresenter(Presenter presenter);

    void displayAlert(String message);

    void setAuthToken(String token);

    interface Presenter {
        void goTo(Place place);
    }
}
