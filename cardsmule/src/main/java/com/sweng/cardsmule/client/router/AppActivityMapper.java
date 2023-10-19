package com.sweng.cardsmule.client.router;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.sweng.cardsmule.client.ClientSession;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.LoginPlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.place.RegistrationPlace;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.client.activities.GameCardDetailsActivity;
import com.sweng.cardsmule.client.activities.HomeActivity;
import com.sweng.cardsmule.client.activities.LoginActivity;
import com.sweng.cardsmule.client.activities.PreAuthenticationActivity;
import com.sweng.cardsmule.client.activities.RegistrationActivity;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.shared.AuthenticationService;
import com.sweng.cardsmule.shared.CardService;
import com.sweng.cardsmule.shared.CollectionService;

public class AppActivityMapper implements ActivityMapper {
    private final ClientSession clientSession;

    public AppActivityMapper(ClientSession clientSession) {
        this.clientSession = clientSession;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof PreAuthenticationPlace)
            return new PreAuthenticationActivity(clientSession.getPreAuthenticationView(), clientSession.getUser(), clientSession.getPlaceController());
        else if (place instanceof LoginPlace)
        	return new LoginActivity(clientSession.getLoginView(), clientSession.getUser(), clientSession.getPlaceController(), GWT.create(AuthenticationService.class));
        else if (place instanceof RegistrationPlace)
        	return new RegistrationActivity(clientSession.getRegistrationView(), clientSession.getUser(), clientSession.getPlaceController(), GWT.create(AuthenticationService.class));
        else if (place instanceof HomePlace)
        	return new HomeActivity(clientSession.getHomeView(), clientSession.getUser(), clientSession.getPlaceController(), GWT.create(CardService.class));
        else if (place instanceof GameCardDetailsPlace)
        	return new GameCardDetailsActivity(clientSession.getCardDetailsView(), (GameCardDetailsPlace) place, GWT.create(CardService.class), GWT.create(CollectionService.class), GWT.create(AuthenticationService.class), clientSession.getUser(), clientSession.getPlaceController());
        return null;
    }
}
