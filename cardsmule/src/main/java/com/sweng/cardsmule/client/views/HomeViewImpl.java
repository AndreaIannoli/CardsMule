package com.sweng.cardsmule.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;

import com.sweng.cardsmule.client.views.*;

public class HomeViewImpl extends Composite implements HomeView{
    private static final HomeViewImplUIBinder uiBinder = GWT.create(HomeViewImplUIBinder.class);
    Presenter presenter;
    @UiField
    TextBox SearchBar;
    @UiField
    RadioButton magicRadio;
    @UiField
    RadioButton pokemonRadio;
    @UiField
    RadioButton YuGiOhRadio;
    
    
	@UiTemplate("HomeViewImpl.ui.xml")
	interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
	}
	
    public HomeViewImpl() {
    	initWidget(uiBinder.createAndBindUi(this));
    	
    }

    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
    
    @Override
    public void resetFields() {
        SearchBar.setText("");
        pokemonRadio.setChecked(false);
        magicRadio.setChecked(false);
        YuGiOhRadio.setChecked(false);
    }
}