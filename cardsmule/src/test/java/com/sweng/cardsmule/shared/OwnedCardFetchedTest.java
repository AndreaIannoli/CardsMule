package com.sweng.cardsmule.shared;

import com.sweng.cardsmule.server.DummyData;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.models.SwengCard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OwnedCardFetchedTest {
	private SwengCard card;
    private OwnedCard ownedCard;
    private OwnedCardFetched ownedCardWithName;

    @BeforeEach
    public void initialize() {
        card = DummyData.createPokemonDummyCard();
        String sampleDesc = "this is a valid test description";
        ownedCard = new OwnedCard(card.getId(), Grade.Good, CardsmuleGame.POKEMON,  "test@test.it", sampleDesc);
        ownedCardWithName = new OwnedCardFetched(ownedCard, card.getName());
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(card.getName(), ownedCardWithName.getName());
    }

    @Test
    public void testEqualsForOwnedCard() {
        Assertions.assertTrue(ownedCardWithName.equals(ownedCard));
    }

    @Test
    public void testEqualsForSameFieldsDifferentOwnedCardFetched() {
        Assertions.assertTrue(ownedCardWithName.equals(new OwnedCardFetched(ownedCard, card.getName())));
    }
}
