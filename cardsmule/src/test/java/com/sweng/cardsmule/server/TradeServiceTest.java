package com.sweng.cardsmule.server;
import org.easymock.IMocksControl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.GeneralException;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.services.TradeServiceImpl;
import com.sweng.cardsmule.shared.OfferPayload;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.throwables.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.*;

import static org.easymock.EasyMock.*;

public class TradeServiceTest {
	@Nested
    class WithMockDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;
        private IMocksControl ctrl;
        private MapDB mockDB;
        private TradeServiceImpl tradeService;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            mockDB = ctrl.createMock(MapDB.class);
            tradeService = new TradeServiceImpl(mockDB);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            tradeService.init(mockConfig);
        }

        private void setupForValidToken() {
            Map<String, Account> mockLoginMap = new HashMap<String, Account>() {{
            	put("validToken1", new Account("test@test.it", "hash", "password"));
                put("validToken2", new Account("test2@test.it",  "hash", "password" ));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(mockLoginMap);
        }

        private void setupForInvalidToken() {
            Map<String, Account> loginInfoMap = new HashMap<String, Account>() {{
                put("validToken1", new Account("test@test.it", "hash", "password"));
                put("validToken2", new Account("test2@test.it",  "hash", "password" ));
                put("validToken3", new Account("test3@test.it",  "hash", "password" ));
            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                   .andReturn(loginInfoMap);
        }

        private void setupForValidEmail() {
            Map<String, Account> userMap = new HashMap<>();
            userMap.put("valid@receiverUserEmail.it", new Account("valid@receiverUserEmail.it", "hash", "password"));
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(userMap);
        }

        private void setupForOfferMap() {
            Map<Integer, Offer> offerMap = new HashMap<Integer, Offer>() {{
                Offer p1 = new Offer("UserMail1", "UserMail2", new ArrayList<>(), new ArrayList<>());
                put(p1.getId(), p1);
                Offer p2 = new Offer("UserMail1", "UserMail3", new ArrayList<>(), new ArrayList<>());
                put(p2.getId(), p2);
                Offer p3 = new Offer("UserMail1", "UserMail4", new ArrayList<>(), new ArrayList<>());
                put(p3.getId(), p3);
                Offer p4 = new Offer("UserMail2", "UserMail3", new ArrayList<>(), new ArrayList<>());
                put(p4.getId(), p4);
                Offer p5 = new Offer("UserMail2", "UserMail4", new ArrayList<>(), new ArrayList<>());
                put(p5.getId(), p5);
                Offer p6 = new Offer("UserMail3", "UserMail4", new ArrayList<>(), new ArrayList<>());
                put(p6.getId(), p6);

            }};
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(offerMap);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAddOfferForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.addOffer(input, "valid@receiverUserEmail.it", ServerData.createOwnedCardServerList(2), ServerData.createOwnedCardServerList(2)));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        public void testAddOfferForInvalidReceiverUserEmail(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.addOffer("validToken", input, ServerData.createOwnedCardServerList(2), ServerData.createOwnedCardServerList(2)));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetOfferCardsForInvalidUserToken(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.getOffer(input, 0));
            ctrl.verify();
        }


        @Test
        public void testGetOfferCardsForInvalidProposalId() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.getOffer("validToken", -1));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetOfferListReceivedForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.getOfferListSent(input));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testGetOfferListSentForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.getOfferListSent(input));
            ctrl.verify();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testAcceptProposalForInvalidToken(String input) {
            setupForInvalidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.acceptOffer(input, 1));
            ctrl.verify();
        }


        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"invalidToken"})
        public void testRefuseOrWithDrawOfferForInvalidUserToken(String input) {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.refuseOrWithdrawOffer(input, 0));
            ctrl.verify();
        }



        @Test
        public void testRefuseOrWithDrawOfferForInvalidOfferId() {
            setupForValidToken();
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> tradeService.refuseOrWithdrawOffer("validToken", -1));
            ctrl.verify();
        }

    }


}
