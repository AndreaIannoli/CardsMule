package com.sweng.cardsmule.client;

import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.FieldVerifier;
//import com.sweng.cardsmule.client.Home;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.google.gwt.activity.shared.ActivityManager;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.place.shared.PlaceHistoryHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.sweng.cardsmule.shared.AuthenticationService;
import com.sweng.cardsmule.client.router.AppActivityMapper;
import com.sweng.cardsmule.client.router.AppPlaceHistoryMapper;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Cardsmule implements EntryPoint {
    private final SimplePanel appWidget = new SimplePanel();
    private AuthenticationServiceAsync authenticationService;
    private User user;
    private PlaceController placeController;
    
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
        ClientSession clientSession = new ClientSessionImpl();
        EventBus eventBus = clientSession.getEventBus();
        placeController = clientSession.getPlaceController();
        authenticationService = GWT.create(AuthenticationService.class);
        user = clientSession.getUser();
        validateUserToken();

        // Start ActivityManager for the main widget with our ActivityMapper
        ActivityMapper activityMapper = new AppActivityMapper(clientSession);
        ActivityManager activityManager = new ActivityManager(activityMapper, eventBus);
        activityManager.setDisplay(appWidget);

        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper = new AppPlaceHistoryMapper(user);
        PlaceHistoryHandler historyHandler = new PlaceHistoryHandler(historyMapper);
        historyHandler.register(placeController, eventBus, new PreAuthenticationPlace());

        RootPanel root = RootPanel.get("main");
        appWidget.setStyleName("main");
        
        root.add(appWidget);

        // Goes to place represented on URL or default place
        historyHandler.handleCurrentHistory();
	}
	
    private void validateUserToken() {
        authenticationService.me(Cookies.getCookie("token"), new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
                Cookies.removeCookie("token");
            }

            @Override
            public void onSuccess(String result) {
                user.setCredentials(Cookies.getCookie("token"), result);
            }
        });
    }
}
