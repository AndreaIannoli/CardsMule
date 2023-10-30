package com.sweng.cardsmule.client;
import com.google.gwt.place.shared.PlaceController;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.client.activities.GameCardDetailsActivity;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.server.ServerData;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.authentication.*;
import com.sweng.cardsmule.shared.models.Grade;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class GameCardDetailsActivityTest {
	private static final int CARD_ID = 111;
    IMocksControl ctrl;
    GameCardDetailsPlace mockPlace;
    GameCardDetailsView mockView;
    CardServiceAsync mockCardService;
    CollectionServiceAsync mockCollectionService;
    PlaceController placeController;
    GameCardDetailsActivity cardActivity;
    AuthenticationServiceAsync mockAuthenticationService;
    
    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockPlace = new GameCardDetailsPlace(CARD_ID, CardsmuleGame.MAGIC);
        mockView = ctrl.createMock(GameCardDetailsView.class);
        mockCardService = ctrl.mock(CardServiceAsync.class);
        mockCollectionService = ctrl.mock(CollectionServiceAsync.class);
        mockAuthenticationService = ctrl.mock(AuthenticationServiceAsync.class);
        placeController = ctrl.createMock(PlaceController.class);
        cardActivity = new GameCardDetailsActivity( mockView, mockPlace,mockCardService, mockCollectionService, mockAuthenticationService, new User(), placeController);
    }


    private static Stream<Arguments> provideDifferentTypeOfErrors() {
        return Stream.of(
                Arguments.of(new AuthenticationException("Invalid token")),
                Arguments.of(new InputException("Invalid description")),
                Arguments.of(new RuntimeException())
        );
    }

    @Test
    public void testFetchCardForOnSuccess() throws InputException {
        SwengCard card = ServerData.createPokemonServerCard();
        mockCardService.getGameCard(isA(CardsmuleGame.class), anyInt(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<SwengCard> callback = (AsyncCallback<SwengCard>) args[args.length - 1];
            callback.onSuccess(card);
            return null;
        });
        mockView.setData(card);
        expectLastCall();
        ctrl.replay();
        cardActivity.fetchCard();
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testAddCardToCollectionForOnFailure(Exception error) {
        mockCollectionService.addOwnedCardToCollection(anyString(), isA(CardsmuleGame.class), anyString(), anyInt(), isA(Grade.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(error);
            return null;
        });
        mockView.displayAlert(anyString());
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "descrizione");
        ctrl.verify();
    }

    @Test
    public void testAddCardToCollectionForOnSuccessTrue() {
        mockCollectionService.addOwnedCardToCollection(anyString(), isA(CardsmuleGame.class), anyString(), anyInt(), isA(Grade.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockView.getDeckSelected();
        expectLastCall().andReturn("Owned");
        mockView.displayAlert(anyString());
        mockView.hideModal();
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "descrizione");
        ctrl.verify();
    }

    @Test
    public void testAddCardToCollectionForOnSuccessFalse() {
        mockCollectionService.addOwnedCardToCollection(anyString(), isA(CardsmuleGame.class), anyString(), anyInt(), isA(Grade.class), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });
        mockView.displayAlert(anyString());
        ctrl.replay();
        cardActivity.addCardToDeck("Owned", "1", "descrizione");
        ctrl.verify();
    }
}
