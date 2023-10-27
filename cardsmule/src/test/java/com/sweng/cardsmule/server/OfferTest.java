package com.sweng.cardsmule.server;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.cardsmule.shared.models.*;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class OfferTest {
    ServletConfig mockConfig;
    ServletContext mockCtx;
    OwnedCard dummySenderPhysicalCard = new OwnedCard(111, Grade.getRandomGrade(), CardsmuleGame.randomGame(), "email" , "This is a valid description");
    OwnedCard dummyReceiverPhysicalCard = new OwnedCard(222, Grade.getRandomGrade(), CardsmuleGame.randomGame(),  "email2" ,"This is a valid description");
    Map<Integer, Offer> dummyProposalMap = new HashMap<Integer, Offer>() {{
        put(1, new Offer("test2@test.it", "test@test.it",
                Collections.singletonList(dummySenderPhysicalCard),
                Collections.singletonList(dummyReceiverPhysicalCard)));
        put(2, new Offer("test3@test.it", "test@test.it",
                Collections.singletonList(new OwnedCard(333, Grade.getRandomGrade(), CardsmuleGame.randomGame(), "email3", "This is a valid description")),
                Collections.singletonList(dummyReceiverPhysicalCard)));
    }};

    @BeforeEach
    public void initialize() {
        mockConfig = createStrictMock(ServletConfig.class);
        mockCtx = createStrictMock(ServletContext.class);
    }

    private Collection createDummyOwnedDeck() {
    	Collection deck = new Collection("Owned", true);
        for (OwnedCard pCard : DummyData.createPhysicalCardDummyList(5))
            deck.addOwnedCard(pCard);
        return deck;
    }

    @Test
    public void testAcceptProposalForValidParameters() throws ServletException, OfferNotFoundException, AuthenticationException {
        // init fakes, mocks
        MapDBTest fakeDB = new MapDBTest(dummyProposalMap,
                // deck map
                new HashMap<>() {{
                    Deck receiverOwnedDeck = createDummyOwnedDeck();
                    Deck senderOwnedDeck = createDummyOwnedDeck();
                    Deck receiverWishedDeck = new Deck("Wished", true);
                    Deck receiverCustomDeck = new Deck("Custom", false);
                    senderOwnedDeck.addPhysicalCard(dummySenderPhysicalCard);
                    receiverOwnedDeck.addPhysicalCard(dummyReceiverPhysicalCard);
                    receiverWishedDeck.addPhysicalCard(new PhysicalCard(dummySenderPhysicalCard.getGameType(), dummySenderPhysicalCard.getCardId(), Status.VeryDamaged, "Any card is fine"));
                    receiverCustomDeck.addPhysicalCard(dummyReceiverPhysicalCard);
                    put("test@test.it", new LinkedHashMap<>() {{
                        put("Owned", receiverOwnedDeck);
                        put("Wished", receiverWishedDeck);
                        put("Custom", receiverCustomDeck);
                    }});
                    put("test2@test.it", new LinkedHashMap<>() {{
                        put("Owned", senderOwnedDeck);
                        put("Wished", new Deck("Wished", true));
                    }});
                }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertTrue(exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);

        Assertions.assertAll(() -> {
            Assertions.assertNull(fakeDB.getOfferMap().get(1));
            Assertions.assertTrue(fakeDB.getCollectionMap().get("test@test.it").get("Owned").getPhysicalCards().stream().anyMatch(pCard -> pCard.equals(dummySenderPhysicalCard)));
            Assertions.assertTrue(fakeDB.getCollectionMap().get("test2@test.it").get("Owned").getPhysicalCards().stream().anyMatch(pCard -> pCard.equals(dummyReceiverPhysicalCard)));
            Assertions.assertTrue(fakeDB.getCollectionMap().get("test@test.it").get("Wished").getPhysicalCards().isEmpty());
            Assertions.assertTrue(fakeDB.getCollectionMap().get("test@test.it").get("Custom").getPhysicalCards().isEmpty());
            Assertions.assertTrue(fakeDB.getOfferMap().isEmpty());
        });
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedSenderPhysicalCard() throws ServletException {
        // init fakes, mocks
    	MapDBTest fakeDB = new MapDBTest(dummyProposalMap, new HashMap<>() {{
            Deck senderDeck = createDummyOwnedDeck();
            Deck receiverDeck = createDummyOwnedDeck();
            receiverDeck.addPhysicalCard(dummyReceiverPhysicalCard);
            put("test@test.it", new LinkedHashMap<>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@test.it", new LinkedHashMap<>() {{
                put("Owned", senderDeck);
            }});
        }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedReceiverPhysicalCard() throws ServletException {
        // init fakes, mocks
    	MapDBTest fakeDB = new MapDBTest(dummyProposalMap, new HashMap<>() {{
            Deck senderDeck = createDummyOwnedDeck();
            Deck receiverDeck = createDummyOwnedDeck();
            senderDeck.addPhysicalCard(dummySenderPhysicalCard);
            put("test@test.it", new LinkedHashMap<>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@test.it", new LinkedHashMap<>() {{
                put("Owned", senderDeck);
            }});
        }});
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl(fakeDB);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptProposal("validToken", 1));
        verify(mockConfig, mockCtx);
    }
}
