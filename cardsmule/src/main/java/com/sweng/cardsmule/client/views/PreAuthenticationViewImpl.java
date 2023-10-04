package com.sweng.cardsmule.client.views;

import java.util.Date;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.sweng.cardsmule.client.place.LoginPlace;
import com.sweng.cardsmule.client.place.RegistrationPlace;
import com.sweng.cardsmule.client.views.*;
import com.sweng.cardsmule.client.widgets.NavWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

public class PreAuthenticationViewImpl extends Composite implements PreAuthenticationView{
    private static final PreAuthenticationViewImplUIBinder uiBinder = GWT.create(PreAuthenticationViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    Button btnLogin;
    @UiField
    Button btnRegister;
    
	@UiTemplate("PreAuthenticationViewImpl.ui.xml")
	interface PreAuthenticationViewImplUIBinder extends UiBinder<Widget, PreAuthenticationViewImpl> {
	}
	
    public PreAuthenticationViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    	btnLogin.addClickHandler(e -> onLoginClick());
    	btnRegister.addClickHandler(e -> onRegistrationClick());
    }
    
    @Override
    public void setAuthToken(String token) {
        final long DURATION = 1000 * 60 * 60 * 24 * 7;
        Date expires = new Date(System.currentTimeMillis() + DURATION);
        Cookies.setCookie("token", token, expires, null, "/", false);
    }

    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
    public void onLoginClick() {
    	this.presenter.goTo(new LoginPlace());
    }
    
    public void onRegistrationClick() {
    	this.presenter.goTo(new RegistrationPlace());
    }
}
