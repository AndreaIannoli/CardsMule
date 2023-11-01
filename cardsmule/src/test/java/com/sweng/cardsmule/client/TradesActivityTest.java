package com.sweng.cardsmule.client;

import com.google.gwt.place.shared.Place;

import com.google.gwt.place.shared.PlaceController;
import com.sweng.cardsmule.client.activities.TradesActivity;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.TradePlace;
import com.sweng.cardsmule.client.place.TradesPlace;
import com.sweng.cardsmule.client.views.TradeView;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.easymock.EasyMock.*;
import java.util.*;


public class TradesActivityTest {
	 	IMocksControl ctrl;
	    TradeView mockView;
	    PlaceController mockPlaceController;
	    TradesActivity tradesActivity;

	    @BeforeEach
	    public void initialize() {
	        ctrl = createStrictControl();
	        mockView = ctrl.createMock(TradeView.class);
	        TradeCardsServiceAsync mockRpcService = ctrl.createMock(TradeCardsServiceAsync.class);
	        mockPlaceController = ctrl.createMock(PlaceController.class);
	        tradesActivity = new TradesActivity(mockView, mockRpcService, new User(), mockPlaceController);
	    }

	    @Test
	    public void testForGoTo() {
	        mockPlaceController.goTo(isA(Place.class));
	        expectLastCall();
	        ctrl.replay();
	        tradesActivity.goTo(new TradePlace(1));
	        ctrl.verify();
	    }
	    
	    
}
