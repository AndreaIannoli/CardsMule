package com.sweng.cardsmule.client.views;

import java.util.Date;


import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.views.*;
import com.sweng.cardsmule.client.widgets.NavWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class LoginViewImpl extends Composite implements LoginView{
    private static final LoginViewImplUIBinder uiBinder = GWT.create(LoginViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;
    @UiField
    Button btnLogin;
    @UiField
    Button btnBack;
    
	@UiTemplate("LoginViewImpl.ui.xml")
	interface LoginViewImplUIBinder extends UiBinder<Widget, LoginViewImpl> {
	}
	
    public LoginViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    	username.getElement().setAttribute("placeholder", "Username");
    	password.getElement().setAttribute("placeholder", "Password");
    	btnBack.addClickHandler(e -> onBackClick());
    	btnLogin.addClickHandler(e -> presenter.signIn(username.getText(), password.getText()));
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
    
    public void onBackClick() {
    	this.presenter.goTo(new PreAuthenticationPlace());
    }
    
    @Override
    public void resetFields() {
        username.setText("");
        password.setText("");
    }
}
