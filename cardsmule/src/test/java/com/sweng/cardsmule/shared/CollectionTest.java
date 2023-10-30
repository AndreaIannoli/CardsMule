package com.sweng.cardsmule.shared;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sweng.cardsmule.server.ServerData;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;

import java.util.Set;

public class CollectionTest {

    private Collection coll;
    private OwnedCard oCard;
    private OwnedCard oCard2;

    @BeforeEach
    public void initialize() {
    	coll = new Collection("CollectionName");
        SwengCard card = ServerData.createPokemonServerCard();
        oCard = new OwnedCard(card.getId(), Grade.Excellent, CardsmuleGame.POKEMON, "email", "decrizione");
        oCard2 = new OwnedCard(card.getId(), Grade.Good, CardsmuleGame.POKEMON, "email2", "descrizione2");
    }

    @Test
    public void testName() {
        Assertions.assertEquals("CollectionName", coll.getName());
    }

    @Test
    public void testAddOwnedCard() {
        Assertions.assertTrue(coll.addOwnedCard(oCard));
        Assertions.assertFalse(coll.addOwnedCard(oCard));
    }

    @Test
    public void testContainsOwnedCard() {
    	coll.addOwnedCard(oCard);
        Assertions.assertTrue(coll.containsOwnedCard(oCard));
        Assertions.assertFalse(coll.containsOwnedCard(oCard2));
    }

    @Test
    public void testGetOwnedCards() {
    	coll.addOwnedCard(oCard);
    	coll.addOwnedCard(oCard2);
        Set<OwnedCard> cards = coll.getOwnedCards();

        Assertions.assertEquals(2, cards.size());
        Assertions.assertTrue(cards.contains(oCard));
        Assertions.assertTrue(cards.contains(oCard2));
    }

    @Test
    public void testRemoveOwnedCard() {
    	coll.addOwnedCard(oCard);
    	coll.addOwnedCard(oCard2);
        Assertions.assertTrue(coll.removeOwnedCard(oCard));
        Assertions.assertEquals(1, coll.getOwnedCards().size());
        Assertions.assertFalse(coll.removeOwnedCard(oCard));
    }
}
