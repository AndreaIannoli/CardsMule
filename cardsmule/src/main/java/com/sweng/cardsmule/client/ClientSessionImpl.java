package com.sweng.cardsmule.client;

import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.client.views.PreAuthenticationView;
import com.sweng.cardsmule.client.views.PreAuthenticationViewImpl;
import com.sweng.cardsmule.client.views.RegistrationView;
import com.sweng.cardsmule.client.views.RegistrationViewImpl;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.client.views.HomeViewImpl;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.sweng.cardsmule.client.views.LoginViewImpl;


public class ClientSessionImpl implements ClientSession{
    private static final EventBus eventBus = new SimpleEventBus();
    private static final PlaceController placeController = new PlaceController(eventBus);
    private static final User user = new User();
    private static final PreAuthenticationView preAuthenticationView = new PreAuthenticationViewImpl();
    private static final LoginView loginView = new LoginViewImpl();
    private static final RegistrationView registrationView = new RegistrationViewImpl();
    private static final HomeView homeView = new HomeViewImpl();
    
    @Override
    public EventBus getEventBus() {
        return eventBus;
    }

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public User getUser() {
		return user;
	}

	@Override
	public PreAuthenticationView getPreAuthenticationView() {
		return preAuthenticationView;
	}

	@Override
	public LoginView getLoginView() {
		return loginView;
	}

	@Override
	public RegistrationView getRegistrationView() {
		return registrationView;
	}
	@Override
	public HomeView getHomeView() {
		return homeView;
	}
}
