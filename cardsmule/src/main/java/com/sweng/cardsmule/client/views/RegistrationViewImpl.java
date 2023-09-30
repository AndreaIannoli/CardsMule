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

public class RegistrationViewImpl extends Composite implements RegistrationView{
    private static final RegistrationViewImplUIBinder uiBinder = GWT.create(RegistrationViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    TextBox email;
    @UiField
    TextBox username;
    @UiField
    PasswordTextBox password;
    @UiField
    Button btnRegister;
    @UiField
    Button btnBack;
    
	@UiTemplate("RegistrationViewImpl.ui.xml")
	interface RegistrationViewImplUIBinder extends UiBinder<Widget, RegistrationViewImpl> {
	}
	
    public RegistrationViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    	email.getElement().setAttribute("placeholder", "Email");
    	username.getElement().setAttribute("placeholder", "Username");
    	password.getElement().setAttribute("placeholder", "Password");
    	btnBack.addClickHandler(e -> onBackClick());
    	btnRegister.addClickHandler(e -> presenter.signUp(email.getText(), username.getText(), password.getText()));
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
    	email.setText("");
        username.setText("");
        password.setText("");
    }
}
