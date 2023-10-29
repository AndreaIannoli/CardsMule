package com.sweng.cardsmule.server;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.services.CollectionServiceImpl;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.CollectionNotFoundException;
import com.sweng.cardsmule.shared.throwables.AlreadyExistingOfferException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.activities.DecksManagerActivity;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;
import com.sweng.cardsmule.shared.models.*;
import com.sweng.cardsmule.shared.CollectionVariationPayload;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;
import com.sweng.cardsmule.server.TestDBCreation;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.*;

import static org.easymock.EasyMock.*;

public class CollectionServiceTest {
    @Nested
    class WithMockDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;
        private IMocksControl ctrl;
        private MapDB mockDB;
        private CollectionServiceImpl deckService;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            mockDB = ctrl.createMock(MapDB.class);
            deckService = new CollectionServiceImpl(mockDB);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            deckService.init(mockConfig);
        }
  @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAddPhysicalCardToDeckForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () ->
                    deckService.addOwnedCardToCollection(input, CardsmuleGame.MAGIC, "Owned", 111, Grade.Poor, "This is a valid description.")
            );
            ctrl.verify();
        }

        private void setupForValidToken() {
            Map<String, Account> mockLoginMap = new HashMap() {{
                put("validToken", new Account("test@test1.it", "test", "passw"));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockLoginMap);
        }

        private void setupForInvalidToken() {
            Map<String, Account> mockLoginMap = new HashMap() {{
                put("validToken1", new Account("test@test1.it", "test", "passw"));
                put("validToken2", new Account("test@test1.it", "test", "passw"));
                put("validToken3", new Account("test@test1.it", "test", "passw"));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockLoginMap);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetUserDeckNamesForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> deckService.getUserCollectionNames(input));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetDeckByNameForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> deckService.getDeck(input, "Owned"));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"test", "test@", "test@test", "test@test."})
        public void testGetUserOwnedDeckForInvalidEmail(String input) {
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> deckService.getUserCollection(input));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testEditPhysicalCardForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> deckService.editOwnedCard(input,
                    "Owned", new OwnedCard(1111, Grade.getRandomGrade(), CardsmuleGame.randomGame()," ","This is a valid description.")));
            ctrl.verify();
        }


    @Nested
    class WithFakeDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;

        @BeforeEach
        public void initialize() throws ServletException {
            mockConfig = createStrictMock(ServletConfig.class);
            mockCtx = createStrictMock(ServletContext.class);
        }

        private CollectionServiceImpl initializeDeckService(Map<String, Map<String, DecksManagerActivity>> deckMap) throws ServletException {
            TestDBCreation fakeDB = new TestDBCreation(new HashMap(), new HashMap<>());
            CollectionServiceImpl deckService = new CollectionServiceImpl(fakeDB);
            deckService.init(mockConfig);
            return deckService;
        }


        @Test
        public void testRemoveCustomDeckForNotExistingDeck() throws AuthenticationException, ServletException {
            CollectionServiceImpl deckService = initializeDeckService(new HashMap() {{
                put("test@test.it", new LinkedHashMap<>());
            }});

            expect(mockConfig.getServletContext()).andReturn(mockCtx).times(2);
            replay(mockConfig, mockCtx);
            Assertions.assertFalse(deckService.removeDeck("validToken", "testDeckName"));
            verify(mockConfig, mockCtx);
        }
}}}

