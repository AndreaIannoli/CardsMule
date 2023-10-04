package com.sweng.cardsmule.client.router;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.LoginPlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.place.RegistrationPlace;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper, RouteConstants {
    private static final String DELIMITER = "/";
    private final Place defaultPlace = new PreAuthenticationPlace();
    private final User user;

    public AppPlaceHistoryMapper(User user) {
        this.user = user;
    }

    @Override
    public Place getPlace(String token) {
        if (token.isEmpty()) {
            return defaultPlace;
        } else if (token.equals(loginLink) && !user.isLoggedIn()) {
            return new LoginPlace();
        } else if (token.equals(registrationLink) && !user.isLoggedIn()) {
        	return new RegistrationPlace();
        } else if (token.equals(homeLink)&& user.isLoggedIn() ) {
        	return new HomePlace();
        }
        
        return defaultPlace;
    }

    @Override
    public String getToken(Place place) {
        if (place instanceof PreAuthenticationPlace) {
            return preAuthenticationLink;
        } else if (place instanceof LoginPlace) {
            return loginLink;
        } else if (place instanceof RegistrationPlace) {
        	return registrationLink;
        } else if (place instanceof HomePlace) {
        	return homeLink;
        }
        else {
            return "";
        }
        
    }
}