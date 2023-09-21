package com.sweng.cardsmule.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiField;

public class Home extends Composite {
	private static HomeUiBinder uiBinder = GWT.create(HomeUiBinder.class);

	@UiTemplate("Home.ui.xml")
	interface HomeUiBinder extends UiBinder<Widget, Home> {
	}

	public Home() {
		initWidget(uiBinder.createAndBindUi(this));
		btnHome.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnHome.getElement().getStyle().setWidth(70.0, Unit.PX);
		btnGit.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnGit.getElement().getStyle().setWidth(70.0, Unit.PX);
		btnLogin.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnLogin.getElement().getStyle().setWidth(70.0, Unit.PX);
		btnRegister.getElement().getStyle().setHeight(50.0, Unit.PX);
		btnRegister.getElement().getStyle().setWidth(70.0, Unit.PX);
	}

	@UiHandler("btnHome")
	void doClickHome(ClickEvent event) {
		RootPanel.get("container").clear();
		RootPanel.get("container").add(new Home());
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
	}

	@UiField
	Button btnHome;

	@UiField
	Button btnGit;

	@UiField
	Button btnLogin;

	@UiField
	Button btnRegister;
}