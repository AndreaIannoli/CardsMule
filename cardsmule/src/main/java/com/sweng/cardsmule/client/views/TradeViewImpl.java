package com.sweng.cardsmule.client.views;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.handlers.HandleNavBar;
import com.sweng.cardsmule.client.handlers.HandleOfferList;
import com.sweng.cardsmule.client.place.DecksManagerPlace;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.place.TradePlace;
import com.sweng.cardsmule.client.place.TradesPlace;
import com.sweng.cardsmule.client.widgets.OfferListWidget;
import com.sweng.cardsmule.shared.models.Offer;

public class TradeViewImpl extends Composite implements TradeView, HandleOfferList, HandleNavBar {
    private static final OfferViewImplUIBinder uiBinder = GWT.create(OfferViewImplUIBinder.class);
    Presenter presenter;
    @UiField (provided = true)
    OfferListWidget fromYouOfferList;
    @UiField (provided = true)
    OfferListWidget toYouOfferList;
    
    
    public TradeViewImpl() {
    	fromYouOfferList = new OfferListWidget("From you", "Receiver");
    	toYouOfferList = new OfferListWidget("To you", "Sender");
        initWidget(uiBinder.createAndBindUi(this));
    }

	@Override
	public void onClickOfferRow(int selectedOfferId) {
		presenter.goTo(new TradePlace(selectedOfferId));
		
	}

	@Override
	public void setFromYouOfferList(List<Offer> offer) {
		offer.forEach(offers -> {
            String proposalDate = DateTimeFormat.getFormat("dd/MM/yyyy").format(new Date(offers.getDate()));
            fromYouOfferList.addRow(offers.getId(), proposalDate, offers.getReceiverUserEmail(), this);
        });
		
	}

	@Override
	public void setToYouOfferList(List<Offer> offer) {
		offer.forEach(offers -> {
            String proposalDate = DateTimeFormat.getFormat("dd/MM/yyyy").format(new Date(offers.getDate()));
            toYouOfferList.addRow(offers.getId(), proposalDate, offers.getSenderUserEmail(), this);
        });
		
	}

	@Override
	public void resetOfferLists() {
		fromYouOfferList.resetTable();
		toYouOfferList.resetTable();
		
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		
	}
	@Override
	public void onClickLogout() {
		presenter.goTo(new PreAuthenticationPlace());	
	}

	@Override
	public void onClickHome() {
		presenter.goTo(new HomePlace());		
	}

	@Override
	public void onClickDeck() {
		presenter.goTo(new DecksManagerPlace());
	}
	@Override
	public void onClickTrades() {
		presenter.goTo(new TradesPlace());
	}
	interface OfferViewImplUIBinder extends UiBinder<Widget, TradeViewImpl> {
    }
}
