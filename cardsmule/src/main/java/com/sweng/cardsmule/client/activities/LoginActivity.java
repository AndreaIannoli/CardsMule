package com.sweng.cardsmule.client.activities;

import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.client.views.PreAuthenticationView;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.CredentialsPayload;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

public class LoginActivity extends AbstractActivity implements LoginView.Presenter{
	private final LoginView view;
	private final User user;
	private final PlaceController placeController;
	private final AuthenticationServiceAsync authenticationService;
	
	public LoginActivity(LoginView view, User user, PlaceController placeController, AuthenticationServiceAsync authenticationService) {
		this.view = view;
		this.user = user;
		this.placeController = placeController;
		this.authenticationService = authenticationService;
	}
	
	@Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
    }

    @Override
    public void onStop() {
        view.resetFields();
    }
	
	@Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }

	@Override
	public void signIn(String username, String password) {
		if(username.isEmpty() || password.length() < 8) {
			view.displayAlert("Invalid credentials");
		} else {
			AsyncCallback<CredentialsPayload> asyncCallback = new AsyncCallback<CredentialsPayload>() {
                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof AuthenticationException) {
                        view.displayAlert(((AuthenticationException) caught).getExceptionText());
                    } else {
                        view.displayAlert("Unexpected error occurred");
                    }
                }

                @Override
                public void onSuccess(CredentialsPayload result) {
                    view.setAuthToken(result.getToken());
                    user.setCredentials(result.getToken(), result.getUsername());
                    goTo(new PreAuthenticationPlace());
                }
            };
            authenticationService.signIn(username, password, asyncCallback);
		}
	}
}
