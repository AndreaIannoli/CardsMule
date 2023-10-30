package com.sweng.cardsmule.server;


import org.junit.jupiter.api.Assertions;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.cardsmule.shared.models.*;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;
import com.sweng.cardsmule.server.services.*;

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
    OwnedCard dummySenderPhysicalCard = new OwnedCard(111, Grade.getRandomGrade(), CardsmuleGame.randomGame(), "test@DB.com" , "This is a valid description");
    OwnedCard dummyReceiverPhysicalCard = new OwnedCard(222, Grade.getRandomGrade(), CardsmuleGame.randomGame(),  "test2@DB.com" ,"This is a valid description");
    Map<Integer, Offer> dummyProposalMap = new HashMap<Integer, Offer>() {{
        put(1, new Offer("test2@DB.com", "test@DB.com",
                Collections.singletonList(dummySenderPhysicalCard),
                Collections.singletonList(dummyReceiverPhysicalCard)));
        put(2, new Offer("test3@DB.com", "test@DB.com",
                Collections.singletonList(new OwnedCard(333, Grade.getRandomGrade(), CardsmuleGame.randomGame(), "test3@DB.com", "This is a valid description")),
                Collections.singletonList(dummyReceiverPhysicalCard)));
    }};

    @BeforeEach
    public void initialize() {
        mockConfig = createStrictMock(ServletConfig.class);
        mockCtx = createStrictMock(ServletContext.class);
    }

    private Collection createDummyOwnedDeck() {
    	Collection deck = new Collection("Owned", true);
        for (OwnedCard pCard : ServerData.createOwnedCardServerList(5))
            deck.addOwnedCard(pCard);
        return deck;
    }

    @Test
    public void testAcceptProposalForValidParameters() throws ServletException, OfferNotFoundException, AuthenticationException {
        // init fakes, mocks
        TestDBCreation dbC = new TestDBCreation(dummyProposalMap,
                // deck map
                new HashMap<String, LinkedHashMap<String, Collection>>() {{
                    Collection receiverOwnedDeck = createDummyOwnedDeck();
                    Collection senderOwnedDeck = createDummyOwnedDeck();
                    Collection receiverWishedDeck = new Collection("Wished", true);
                    Collection receiverCustomDeck = new Collection("Custom", false);
                    senderOwnedDeck.addOwnedCard(dummySenderPhysicalCard);
                    receiverOwnedDeck.addOwnedCard(dummyReceiverPhysicalCard);
                    receiverWishedDeck.addOwnedCard(new OwnedCard(dummySenderPhysicalCard.getReferenceCardId(), Grade.Poor, dummySenderPhysicalCard.getGameType(), "test@DB.com","Any card is fine"));
                    receiverCustomDeck.addOwnedCard(dummyReceiverPhysicalCard);
                    put("test@DB.com", new LinkedHashMap<String, Collection>() {{
                        put("Owned", receiverOwnedDeck);
                        put("Wished", receiverWishedDeck);
                        put("Custom", receiverCustomDeck);
                    }});
                    put("test2@DB.com", new LinkedHashMap<String, Collection>() {{
                        put("Owned", senderOwnedDeck);
                        put("Wished", new Collection("Wished", true));
                    }});
                }});
        TradeServiceImpl exchangeService = new TradeServiceImpl(dbC);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertTrue(exchangeService.acceptOffer("validToken", 1));
        verify(mockConfig, mockCtx);

        Assertions.assertAll(() -> {
            Assertions.assertNull(dbC.getOfferMap().get(1));
            Assertions.assertTrue(dbC.getCollectionMap().get("test@DB.com").get("Owned").getOwnedCards().stream().anyMatch(pCard -> pCard.equals(dummySenderPhysicalCard)));
            Assertions.assertTrue(dbC.getCollectionMap().get("test2@DB.com").get("Owned").getOwnedCards().stream().anyMatch(pCard -> pCard.equals(dummyReceiverPhysicalCard)));
            Assertions.assertTrue(dbC.getCollectionMap().get("test@DB.com").get("Wished").getOwnedCards().isEmpty());
            Assertions.assertTrue(dbC.getCollectionMap().get("test@DB.com").get("Custom").getOwnedCards().isEmpty());
            Assertions.assertTrue(dbC.getOfferMap().isEmpty());
        });
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedSenderPhysicalCard() throws ServletException {
        // init fakes, mocks
    	TestDBCreation dbC = new TestDBCreation(dummyProposalMap, new HashMap<String, LinkedHashMap<String, Collection>>() {{
            Collection senderDeck = createDummyOwnedDeck();
            Collection receiverDeck = createDummyOwnedDeck();
            receiverDeck.addOwnedCard(dummyReceiverPhysicalCard);
            put("test@DB.com", new LinkedHashMap<String, Collection>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@DB.com", new LinkedHashMap<String, Collection>() {{
                put("Owned", senderDeck);
            }});
        }});
        TradeServiceImpl exchangeService = new TradeServiceImpl(dbC);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptOffer("validToken", 1));
        //verify(mockConfig, mockCtx);
    }

    @Test
    public void testAcceptProposalForNoLongerOwnedReceiverPhysicalCard() throws ServletException {
        // init fakes, mocks
    	TestDBCreation dbC = new TestDBCreation(dummyProposalMap, new HashMap<String, LinkedHashMap<String, Collection>>() {{
            Collection senderDeck = createDummyOwnedDeck();
            Collection receiverDeck = createDummyOwnedDeck();
            senderDeck.addOwnedCard(dummySenderPhysicalCard);
            put("test@DB.com", new LinkedHashMap<String, Collection>() {{
                put("Owned", receiverDeck);
            }});
            put("test2@DB.com", new LinkedHashMap<String, Collection>() {{
                put("Owned", senderDeck);
            }});
        }});
        TradeServiceImpl exchangeService = new TradeServiceImpl(dbC);
        exchangeService.init(mockConfig);

        // expects
        expect(mockConfig.getServletContext()).andReturn(mockCtx).times(3);
        replay(mockConfig, mockCtx);
        Assertions.assertThrows(RuntimeException.class, () -> exchangeService.acceptOffer("validToken", 1));
        //verify(mockConfig, mockCtx);
    }
}
