package com.sweng.cardsmule.client.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.PreAuthenticationView;
import com.sweng.cardsmule.client.views.RegistrationView;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;

public class PreAuthenticationActivity extends AbstractActivity implements PreAuthenticationView.Presenter{
	private final PreAuthenticationView view;
	private final User user;
	private final PlaceController placeController;
	
	public PreAuthenticationActivity(PreAuthenticationView view, User user, PlaceController placeController) {
		this.view = view;
		this.user = user;
		this.placeController = placeController;
	}
	
	@Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }

    @Override
    public void onStop() {
    }
	
	@Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
