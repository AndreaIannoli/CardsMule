package com.sweng.cardsmule.client;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.client.activities.NewTradeActivity;
import com.sweng.cardsmule.client.views.NewTradeView;
import com.sweng.cardsmule.shared.TradeCardsServiceAsync;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.authentication.*;
import com.sweng.cardsmule.client.place.NewTradePlace;
import com.sweng.cardsmule.shared.CollectionServiceAsync;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class NewTradeActivityTest {
	   IMocksControl ctrl;
	    NewTradeView mockView;
	    PlaceController placeController;
	    NewTradeActivity newTradeActivity;
	    TradeCardsServiceAsync mockTradeService;

	    @BeforeEach
	    public void initialize() {
	        ctrl = createStrictControl();
	        NewTradePlace mockPlace = new NewTradePlace("y132154654", "receiverTest@test.it");
	        mockView = ctrl.createMock(NewTradeView.class);
	        CollectionServiceAsync mockCollectionService = ctrl.createMock(CollectionServiceAsync.class);
	        mockTradeService = ctrl.createMock(TradeCardsServiceAsync.class);
	        User mockUser = new User();
	        mockUser.setCredentials("token","hash", "sender@test.it");
	        placeController = ctrl.createMock(PlaceController.class);
	        newTradeActivity = new NewTradeActivity(mockPlace, mockView, mockCollectionService, mockTradeService, mockUser, placeController);
	    }

	    @Test
	    public void testCreateOfferForInvalidLists() {
	        List<OwnedCard> senderList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(111, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};
	        List<OwnedCard> receiverList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(222, Grade.Good, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};

	        mockView.showAlert(anyString());
	        expectLastCall().times(3);
	        ctrl.replay();
	        Assertions.assertAll(() -> {
	            newTradeActivity.createOffer(Collections.emptyList(), Collections.emptyList());
	            newTradeActivity.createOffer(senderList, Collections.emptyList());
	            newTradeActivity.createOffer(Collections.emptyList(), receiverList);
	        });
	        ctrl.verify();
	    }

	    private static Stream<Arguments> provideDifferentTypeOfErrors() {
	        return Stream.of(
	                Arguments.of(new AuthenticationException("Invalid token")),
	                Arguments.of(new InputException("Invalid receiver email")),
	                Arguments.of(new RuntimeException("Internal server error"))
	        );
	    }

	    @ParameterizedTest
	    @MethodSource("provideDifferentTypeOfErrors")
	    public void testCreateOfferForFailure(Exception error) {
	        // init
	        List<OwnedCard> senderList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(111, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};
	        List<OwnedCard> receiverList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(222, Grade.Good, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};

	        // expects
	        mockTradeService.addOffer(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onFailure(error);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        newTradeActivity.createOffer(senderList, receiverList);
	        ctrl.verify();
	    }

	    @Test
	    public void testCreateOfferForFalseResult() {
	        // init
	        List<OwnedCard> senderList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(111, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};
	        List<OwnedCard> receiverList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(222, Grade.Good, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};

	        // expects
	        mockTradeService.addOffer(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(false);
	            return null;
	        });
	        mockView.showAlert(anyString());

	        ctrl.replay();
	        newTradeActivity.createOffer(senderList, receiverList);
	        ctrl.verify();
	    }

	    @Test
	    public void testCreateOfferForTrueResult() {
	        // init
	        List<OwnedCard> senderList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(111, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};
	        List<OwnedCard> receiverList = new ArrayList<OwnedCard>() {{
	            add(new OwnedCard(222, Grade.Good, CardsmuleGame.MAGIC, "test@test.it",   "descrizione"));
	        }};

	        // expects
	        mockTradeService.addOffer(anyString(), anyString(), isA(List.class), isA(List.class), isA(AsyncCallback.class));
	        expectLastCall().andAnswer(() -> {
	            Object[] args = getCurrentArguments();
	            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
	            callback.onSuccess(true);
	            return null;
	        });
	        mockView.showAlert(anyString());
	        placeController.goTo(isA(Place.class));

	        ctrl.replay();
	        newTradeActivity.createOffer(senderList, receiverList);
	        ctrl.verify();
	    }
}
