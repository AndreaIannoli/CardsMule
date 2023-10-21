package com.sweng.cardsmule.client.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class OfferListWidget {
	private static final OfferListUiBinder uiBinder = GWT.create(OfferListUiBinder.class);
    private static final String NO_PROPOSALS_TEXT = "No proposals";
    String otherUser;
    int size;

    public OfferListWidget(String title, String otherUser) {
        this.otherUser = otherUser;
        this.size = 0;
        /*setupTable();
        initWidget(uiBinder.createAndBindUi(this));
        tableHeading.setInnerText(title);
        setNoItemsText(NO_PROPOSALS_TEXT);*/
    }
    interface OfferListUiBinder extends UiBinder<Widget, OfferListWidget> {
    }
}
