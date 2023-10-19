package com.sweng.cardsmule.client.widgets;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.handlers.HandleNavBar;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.event.dom.client.ClickHandler;

public class NavWidget extends Composite {
	private static NavUiBinder uiBinder = GWT.create(NavUiBinder.class);
	@UiField
	Button btnHome;

	@UiField
	Button btnDeck;

	@UiField
	Button btnScambi;

	@UiField
	Button btnLogout;
	//Button button;

	@UiTemplate("Nav.ui.xml")
	interface NavUiBinder extends UiBinder<Widget, NavWidget> {
	}
	@UiConstructor
	public NavWidget(HandleNavBar parent) {
		initWidget(uiBinder.createAndBindUi(this));
		//button = new Button("Logout", (ClickHandler) event -> parent.onClickLogout());

		/*btnGit.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnGit.getElement().getStyle().setWidth(70.0, Unit.PX);
		btnLogin.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnLogin.getElement().getStyle().setWidth(70.0, Unit.PX);
		btnRegister.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnRegister.getElement().getStyle().setWidth(70.0, Unit.PX);*/
		
		btnLogout.addClickHandler((ClickHandler) event -> parent.onClickLogout());
		btnHome.addClickHandler((ClickHandler) event -> parent.onClickHome());
	}
	
	
	
	

	/*@UiHandler("btnHome")
	void doClickHome(ClickEvent event) {
		RootPanel.get("container").clear();
		RootPanel.get("container").add(new NavWidget());
	}

	@UiHandler("btnGit")
	void doClickContatti(ClickEvent event) {
		RootPanel.get("container").clear();
	}

	@UiHandler("btnLogin")
	void doClickSubmit(ClickEvent event) {
		RootPanel.get("container").clear();
	}

	@UiHandler("btnRegister")
	void doClickDip(ClickEvent event) {
		RootPanel.get("container").clear();
	}*/


}