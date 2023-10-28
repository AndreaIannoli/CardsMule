package com.sweng.cardsmule.client;

import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.client.activities.DecksManagerActivity;
import com.sweng.cardsmule.client.views.DecksManagerView;
import com.sweng.cardsmule.server.DummyData;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.CollectionNotFoundException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.CollectionVariationPayload;


import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import com.sweng.cardsmule.client.authentication.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;


import static org.easymock.EasyMock.*;



public class DecksManagerActivityTest {
	IMocksControl ctrl;
    DecksManagerView mockDecksView;
    DecksManagerActivity decksActivity;
    CollectionServiceAsync mockRpcService;
    PlaceController placeController;

    
    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockDecksView = ctrl.createMock(DecksManagerView.class);
        mockRpcService = ctrl.mock(CollectionServiceAsync.class);
        User user = new User();
        placeController = ctrl.createMock(PlaceController.class);
        decksActivity = new DecksManagerActivity( mockRpcService, mockDecksView, user, placeController);
    }

    @Test
    public void testFetchUserCollectionForSuccess() {
        mockRpcService.getDeck(anyString(),anyString(), isA(BaseAsyncCallback.class));
        List<OwnedCardFetched> ocards = new ArrayList<OwnedCardFetched>() {{
            add(new OwnedCardFetched(
                    new OwnedCard(111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"),
                    "Test Card"));
            
            add(new OwnedCardFetched(
                    new OwnedCard(111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"),
                    "Test Card"));
        }};

        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<OwnedCardFetched>> callback = (AsyncCallback<List<OwnedCardFetched>>) args[args.length - 1];
            callback.onSuccess(ocards);
            return null;
        });
        ctrl.replay();
        decksActivity.fetchUserDeck("Owned", Assertions::assertNotNull);
        ctrl.verify();
    }

    private static Stream<Arguments> provideDifferentTypeOfErrors() {
        return Stream.of(
                Arguments.of(new AuthenticationException("Invalid token")),
                Arguments.of(new InputException("Invalid description")),
                Arguments.of(new CollectionNotFoundException("Deck not found")),
                Arguments.of(new OfferNotFoundException("Physical card edit/remove is not allowed as it already exists in a proposal.")),
                Arguments.of(new RuntimeException())
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testRemoveOwnedCardFromCollectionForInvalidCollectionName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.removeOwnedCardFromDeck(input, new OwnedCard(111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"));
        ctrl.verify();
    }

    @Test
    public void testRemoveOwnedCardFromCollectionForNullOwnedCard() {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.removeOwnedCardFromDeck("Owned", null);
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testRemoveOwnedCardFromCollectionForFailure(Exception e) {
        mockRpcService.removeOwnedCardFromCollection(anyString(), anyString(), isA(OwnedCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CollectionVariationPayload>> callback = (AsyncCallback<List<CollectionVariationPayload>>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());

        ctrl.replay();
        decksActivity.removeOwnedCardFromDeck("Owned", new OwnedCard(111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"));
        ctrl.verify();
    }

    static List<CollectionVariationPayload> provideModifiedDecks(OwnedCard editedOCard) {
    	OwnedCardFetched editedOCardWithName = new OwnedCardFetched(editedOCard, "test");

        List<OwnedCardFetched> mockPCards1 = DummyData.createPhysicalCardWithNameDummyList(5);
        List<OwnedCardFetched> mockPCards2 = DummyData.createPhysicalCardWithNameDummyList(5);

        mockPCards1.addAll(mockPCards2);
        mockPCards1.add(editedOCardWithName);
        mockPCards2.add(editedOCardWithName);

        return Arrays.asList(
                new CollectionVariationPayload("Owned", mockPCards1),
                new CollectionVariationPayload("Custom", mockPCards2)
        );
    }

    @Test
    public void testRemoveOwnedCardFromCollectionForSuccess() {
        OwnedCard removedPCard = new OwnedCard(111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description")
                .copyWithModifiedStatusAndDescription(Grade.Excellent, "This is an edited description");
        List<CollectionVariationPayload> modifiedDecks = provideModifiedDecks(removedPCard);
        mockRpcService.removeOwnedCardFromCollection(anyString(), anyString(), isA(OwnedCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CollectionVariationPayload>> callback = (AsyncCallback<List<CollectionVariationPayload>>) args[args.length - 1];
            callback.onSuccess(modifiedDecks);
            return null;
        });
        mockDecksView.replaceData(isA(List.class));

        ctrl.replay();
        decksActivity.removeOwnedCardFromDeck("Owned", removedPCard);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testCreateCustomCollectionForInvalidDeckName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.createDeck(input);
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testCreateCustomCollectionForValidCollectionNameForFailure(Exception e) {
        mockRpcService.addCollection(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.createDeck("custom_deck");
        ctrl.verify();
    }



    @Test
    public void testCreateCustomCollectionForValidCollectionNameForSuccessAndTrueReturn() {
        String collectionName = "custom_deck";
        mockRpcService.addCollection(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });
        mockDecksView.displayAddedDeck(collectionName);
        ctrl.replay();
        decksActivity.createDeck(collectionName);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullSource
    public void testDeleteCustomCollectionForInvalidCollectionName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.deleteDeck(input, (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testDeleteCustomCollectionForValidCollectionNameForFailure(Exception e) {
        mockRpcService.removeDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());

        ctrl.replay();
        decksActivity.deleteDeck("Test", (Boolean bool) -> {
        });
        ctrl.verify();
    }

    @Test
    public void testDeleteCustomCollectionForValidCollectionForSuccessAndFalseReturn() {
        mockRpcService.removeDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(false);
            return null;
        });

        ctrl.replay();
        decksActivity.deleteDeck("Test", Assertions::assertFalse);
        ctrl.verify();
    }

    @Test
    public void testDeleteCustomCollectionForValidCollectionForSuccessAndTrueReturn() {
        mockRpcService.removeDeck(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<Boolean> callback = (AsyncCallback<Boolean>) args[args.length - 1];
            callback.onSuccess(true);
            return null;
        });

        ctrl.replay();
        decksActivity.deleteDeck("Test", Assertions::assertTrue);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void testAddOwnedCardsToCustomCollectionForInvalidDeckName(String input) {
        List<OwnedCard> mockOCards = Arrays.asList(
                new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"),
                new OwnedCard(2222, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description")
        );
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.addOwnedCardsToDeck(input, mockOCards, (pCardsWithName -> {
        }));
        ctrl.verify();
    }

    @Test
    public void testAddOwnedCardsToCustomCollectionForEmptyList() {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.addOwnedCardsToDeck("test", Collections.emptyList(), (oCardsWithName) -> {
        });
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testAddOwnedCardsToCustomCollectionForValidParametersForFailure(Exception e) {
        // init mocks
        List<OwnedCard> mockOCards = Arrays.asList(
                new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"),
                new OwnedCard(2222, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description")
        );

        // expects
        mockRpcService.addOwnedCardsToDeck(anyString(), anyString(), isA(List.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<OwnedCardFetched>> callback = (AsyncCallback<List<OwnedCardFetched>>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());

        ctrl.replay();
        decksActivity.addOwnedCardsToDeck("test", mockOCards, (oCardsWithName) -> {
        });
        ctrl.verify();
    }

    @Test
    public void testAddOwnedCardsToCustomCollectionForValidParametersForSuccess() {
        // init mocks
        OwnedCard mockOCard1 = new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description");
        OwnedCard mockOCard2 = new OwnedCard(2222, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description");
        List<OwnedCard> mockOCards = Arrays.asList(mockOCard1, mockOCard2);
        List<OwnedCardFetched> mockOCardsWithName = Arrays.asList(
                new OwnedCardFetched(mockOCard1, "Charizard"),
                new OwnedCardFetched(mockOCard2, "Blastoise"));
        Consumer<List<OwnedCardFetched>> consumer = ctrl.createMock(Consumer.class);

        // expects
        mockRpcService.addOwnedCardsToDeck(anyString(), anyString(), isA(List.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<OwnedCardFetched>> callback = (AsyncCallback<List<OwnedCardFetched>>) args[args.length - 1];
            callback.onSuccess(mockOCardsWithName);
            return null;
        });
        consumer.accept(mockOCardsWithName);

        ctrl.replay();
        decksActivity.addOwnedCardsToDeck("test", mockOCards, consumer);
        ctrl.verify();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"Custom"})
    public void testUpdateOwnedCardForInvalidDeckName(String input) {
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.updateOwnedCard(input, new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"));
        ctrl.verify();
    }

    @Test
    public void testUpdateOwnedCardForNullOwnedCard() {
        mockDecksView.displayAlert("Invalid physical card");
        ctrl.replay();
        decksActivity.updateOwnedCard("Owned", null);
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideDifferentTypeOfErrors")
    public void testUpdateOwnedCardForValidParametersForFailure(Exception e) {
        mockRpcService.editOwnedCard(anyString(), anyString(), isA(OwnedCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CollectionVariationPayload>> callback = (AsyncCallback<List<CollectionVariationPayload>>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockDecksView.displayAlert(anyString());
        ctrl.replay();
        decksActivity.updateOwnedCard("Owned", new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description"));
        ctrl.verify();
    }

    @Test
    public void testUpdateOwnedCardForValidParametersForSuccess() {
        // init mocks
        OwnedCard editedOCard = new OwnedCard(1111, Grade.Good, CardsmuleGame.MAGIC,"test@test.it",   "This is a valid description")
                .copyWithModifiedStatusAndDescription(Grade.Excellent, "This is an edited description");
        List<CollectionVariationPayload> modifiedDecks = provideModifiedDecks(editedOCard);

        // expects
        mockRpcService.editOwnedCard(anyString(), anyString(), isA(OwnedCard.class), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<CollectionVariationPayload>> callback = (AsyncCallback<List<CollectionVariationPayload>>) args[args.length - 1];
            callback.onSuccess(modifiedDecks);
            return null;
        });
        mockDecksView.replaceData(isA(List.class));

        ctrl.replay();
        decksActivity.updateOwnedCard("Owned", editedOCard);
        ctrl.verify();
    }
}
