package com.sweng.cardsmule.client.activities;


import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.models.*;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;


public class HomeActivity extends AbstractActivity implements HomeView.Presenter {
    private final HomeView view;
    private final User user;
    private final PlaceController placeController;
    private final CardServiceAsync cardService;
    


    public HomeActivity(HomeView view,User user,PlaceController placeController,CardServiceAsync cardService) {
        this.view = view;
        this.user = user;
        this.placeController = placeController;
        this.cardService= cardService;
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
 
    }

    @Override
    public void onStop() {
        
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }
}