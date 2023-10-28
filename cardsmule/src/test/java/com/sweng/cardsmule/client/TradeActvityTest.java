package com.sweng.cardsmule.client;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.client.activities.TradeActivity;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.TradePlace;
import com.sweng.cardsmule.client.place.TradesPlace;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class TradeActvityTest {
	 IMocksControl ctrl;
	    NewTradeView mockView;
	    PlaceController placeController;
	    TradeActivity tradeActivity;
	    TradeCardsServiceAsync mockTradeService;

	    private static Stream<Arguments> provideDifferentTypeOfErrorsForRefuse() {
	        return Stream.of(
	                Arguments.of(new AuthenticationException("Invalid token")),
	                Arguments.of(new InputException("Invalid proposal Id")),
	                Arguments.of(new OfferNotFoundException("Not existing proposal")),
	                Arguments.of(new RuntimeException("Internal server error"))
	        );
	    }

	    @BeforeEach
	    public void initialize() {
	        ctrl = createStrictControl();
	        TradePlace mockPlace = new TradePlace(0);
	        mockView = ctrl.createMock(NewTradeView.class);
	        mockTradeService = ctrl.createMock(TradeCardsServiceAsync.class);
	        User mockUser = new User();
	        mockUser.setCredentials("token", "hash", "test@test.it");
	        placeController = ctrl.createMock(PlaceController.class);
	        tradeActivity = new TradeActivity(mockPlace, mockView, mockTradeService, mockUser, placeController);
	    }

	    @Test
	    public void testForGoTo() {
	        placeController.goTo(isA(Place.class));
	        expectLastCall();
	        ctrl.replay();
	        tradeActivity.goTo(new TradePlace(null));
	        ctrl.verify();
	    }

	    @ParameterizedTest
	    @MethodSource("provideDifferentTypeOfErrorsForRefuse")
	    public void testRefuseOrWithdrawOfferForFailure(Exception error) {
	        // expects
	    	mockTradeService.refuseOrWithdrawOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onFailure(error);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.refuseOrWithdrawOffer();
	        ctrl.verify();
	    }

	    @Test
	    public void testRefuseOrWithdrawOfferForFalseResult() {
	        // expects
	    	mockTradeService.refuseOrWithdrawOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(false);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.refuseOrWithdrawOffer();
	        ctrl.verify();
	    }

	    @Test
	    public void testRefuseOrWithdrawOfferForTrueResult() {
	        // expects
	    	mockTradeService.refuseOrWithdrawOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(true);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.refuseOrWithdrawOffer();
	        ctrl.verify();
	    }

	    private static Stream<Arguments> provideDifferentTypeOfErrorsForAccept() {
	        return Stream.of(
	                Arguments.of(new AuthenticationException("Invalid token")),
	                Arguments.of(new OfferNotFoundException("Not existing proposal")),
	                Arguments.of(new RuntimeException("Internal server error"))
	        );
	    }

	    @ParameterizedTest
	    @MethodSource("provideDifferentTypeOfErrorsForAccept")
	    public void testAcceptOfferForFailure(Exception error) {
	        // expects
	    	mockTradeService.acceptOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onFailure(error);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.acceptTradeOffer();
	        ctrl.verify();
	    }

	    @Test
	    public void testAcceptOfferForFalseResult() {
	        // expects
	    	mockTradeService.acceptOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(false);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.acceptTradeOffer();
	        ctrl.verify();
	    }

	    @Test
	    public void testAcceptOfferForTrueResult() {
	        // expects
	    	mockTradeService.acceptOffer(anyString(), anyInt(), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(true);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        tradeActivity.acceptTradeOffer();
	        ctrl.verify();
	    }
}
