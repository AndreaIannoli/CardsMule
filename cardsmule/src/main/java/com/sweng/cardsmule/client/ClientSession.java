package com.sweng.cardsmule.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.DecksManagerView;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.client.views.PreAuthenticationView;
import com.sweng.cardsmule.client.views.RegistrationView;
import com.sweng.cardsmule.client.views.TradeView;

public interface ClientSession {
    EventBus getEventBus();

    PlaceController getPlaceController();
    
    User getUser();
    
    PreAuthenticationView getPreAuthenticationView();
    
    LoginView getLoginView();
    
    RegistrationView getRegistrationView();
    
    HomeView getHomeView();
    
    GameCardDetailsView getCardDetailsView();
    
    DecksManagerView getDecksManagerView();
    
    NewTradeView getNewTradeView();

    TradeView getTradeView();
}
