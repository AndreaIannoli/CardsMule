package com.sweng.cardsmule.shared;


import com.google.gson.Gson;

import com.sweng.cardsmule.server.ServerData;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.server.gsonserializer.*;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;

public class OwnedCardTest {

    private SwengCard card;
    private OwnedCard oCard;

    @BeforeEach
    public void initialize() {
        card = ServerData.createPokemonServerCard();
        oCard = new OwnedCard(card.getId(), Grade.Excellent, CardsmuleGame.POKEMON, "email" ,"description");
    }

    @Test
    public void testGetCardId() {
        OwnedCard oCard2 = new OwnedCard(card.getId(), Grade.Good, CardsmuleGame.MAGIC, "email2", "descrizione2");
        Assertions.assertAll(() -> {
            Assertions.assertNotEquals(card.getId(), oCard.getId());
            Assertions.assertNotEquals(card.getId(), oCard2.getId());
        });
    }

    @ParameterizedTest
    @EnumSource(CardsmuleGame.class)
    public void testCardType(CardsmuleGame game) {
        OwnedCard oCard2 = new OwnedCard(card.getId(), Grade.Poor, game,  "email2", "descrizione2");
        Assertions.assertEquals(game, oCard2.getCardGame());
    }

    @Test
    public void testGetGrade() {
        Assertions.assertEquals(Grade.Excellent, oCard.getGrade());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals("description", oCard.getDescription());
    }

    @Test
    public void testEqualsSerializer() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<OwnedCard> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, oCard);

        byte[] data = out.copyBytes();
        GsonSerializer<OwnedCard> deserializer = new GsonSerializer<>(gson);
        OwnedCard deserializedOCard = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(oCard, deserializedOCard);
    }
}
