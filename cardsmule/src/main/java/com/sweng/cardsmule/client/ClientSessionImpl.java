package com.sweng.cardsmule.client;

import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.client.views.PreAuthenticationView;
import com.sweng.cardsmule.client.views.PreAuthenticationViewImpl;
import com.sweng.cardsmule.client.views.RegistrationView;
import com.sweng.cardsmule.client.views.RegistrationViewImpl;
import com.sweng.cardsmule.client.views.TradeView;
import com.sweng.cardsmule.client.views.TradeViewImpl;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.client.views.DecksManagerView;
import com.sweng.cardsmule.client.views.DecksManagerViewImpl;
import com.sweng.cardsmule.client.views.GameCardDetailsViewImpl;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.client.views.HomeViewImpl;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.sweng.cardsmule.client.views.LoginViewImpl;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.client.views.NewTradeViewImpl;


public class ClientSessionImpl implements ClientSession{
    private static final EventBus eventBus = new SimpleEventBus();
    private static final PlaceController placeController = new PlaceController(eventBus);
    private static final User user = new User();
    private static final PreAuthenticationView preAuthenticationView = new PreAuthenticationViewImpl();
    private static final LoginView loginView = new LoginViewImpl();
    private static final RegistrationView registrationView = new RegistrationViewImpl();
    private static final HomeView homeView = new HomeViewImpl();
    private static final GameCardDetailsView gameCardDetailsView = new GameCardDetailsViewImpl();
    private static final DecksManagerView decksManagerView = new DecksManagerViewImpl();
    private static final NewTradeView newTradeView = new NewTradeViewImpl();
    private static final TradeView tradeView = new TradeViewImpl();
    
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

	@Override
	public GameCardDetailsView getCardDetailsView() {
		return gameCardDetailsView;
	}
	
	@Override
	public DecksManagerView getDecksManagerView() {
		return decksManagerView;
	}

	@Override
	public NewTradeView getNewTradeView() {
		// TODO Auto-generated method stub
		return newTradeView;
	}

	@Override
	public TradeView getTradeView() {
		// TODO Auto-generated method stub
		return tradeView;
	}
}
