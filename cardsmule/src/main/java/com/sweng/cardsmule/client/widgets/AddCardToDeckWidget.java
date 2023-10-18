package com.sweng.cardsmule.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.DialogBox;

import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.handlers.HandleAddCardToDeck;

public class AddCardToDeckWidget extends DialogBox {
	 	@UiField
	    GradeWidget grade;
	    @UiField
	    TextArea description;
	    @UiField
	    Button noBtn;
	    @UiField
	    Button yesBtn;
	    private static final AddCardToDeckModalUiBinder uiBinder = GWT.create(AddCardToDeckModalUiBinder.class);

	    public AddCardToDeckWidget(HandleAddCardToDeck parent) {
	        setWidget(uiBinder.createAndBindUi(this));
	        setAutoHideEnabled(true);
	        setModal(true);
	        setAnimationEnabled(true);
	        setGlassEnabled(true);
	        setStyleName("my-Modal");
	        noBtn.addClickHandler(clickEvent -> hide());
	        yesBtn.addClickHandler(clickEvent -> parent.onClickModalYes(grade.getSelection(), description.getValue()));
	    }

	    @Override
	    public void hide() {
	        grade.clearSelection();
	        description.setText("");
	        super.hide();
	    }

	    interface AddCardToDeckModalUiBinder extends UiBinder<Widget, AddCardToDeckWidget> {
	    }
}
