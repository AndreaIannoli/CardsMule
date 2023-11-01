package com.sweng.cardsmule.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.handlers.HandleAddCardToCollection;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;

public class AddCardToCollectionWidget extends Composite{
	private static final AddCardToCollectionUiBinder uiBinder = GWT.create(AddCardToCollectionUiBinder.class);
    @UiField
    Button addToCollectionButton;
    @UiField
    ListBox collectionListBox;
   

    public AddCardToCollectionWidget(HandleAddCardToCollection parent) {
        initWidget(uiBinder.createAndBindUi(this));
        addToCollectionButton.addClickHandler(clickEvent -> parent.onClickAddToDeck());
        
    }

    public String getDeckName() {
        return collectionListBox.getSelectedValue();
    }

    interface AddCardToCollectionUiBinder extends UiBinder<Widget, AddCardToCollectionWidget> {
    }
}
