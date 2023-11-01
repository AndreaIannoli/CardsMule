package com.sweng.cardsmule.shared;

import com.google.gson.Gson;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AcceptTest {
	private String senderEmail;
    private String receiverEmail;
    private List<OwnedCard> senderCards;
    private List<OwnedCard> receiversCards;
    private OwnedCard senderCard1;
    private OwnedCard senderCard2;
    private OwnedCard receiverCard1;
    private OwnedCard receiverCard2;
    
    Offer prop;

    @BeforeEach
    public void initialize() {
        final String validDesc = "this is a valid description!!!";

        senderCard1 = new OwnedCard(111, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it", validDesc);
        senderCard2 = new OwnedCard(222, Grade.Excellent, CardsmuleGame.MAGIC, "test@test.it", validDesc);
        receiverCard1 = new OwnedCard(333, Grade.Excellent, CardsmuleGame.MAGIC, "test@test.it", validDesc);
        receiverCard2 = new OwnedCard(444, Grade.Mint, CardsmuleGame.MAGIC, "test@test.it", validDesc);

        senderCards = new ArrayList<OwnedCard>() {{
            add(senderCard1);
            add(senderCard2);
        }};

        receiversCards = new ArrayList<OwnedCard>() {{
            add(receiverCard1);
            add(receiverCard2);
        }};

        senderEmail = "sender@test.it";
        receiverEmail = "receiver@test.it";
        prop = new Offer(senderEmail, receiverEmail, senderCards, receiversCards);
    }

    @Test
    public void testIfGetIdReturnsUniqueIds() {
        Offer prop2 = new Offer(senderEmail, receiverEmail, senderCards, receiversCards);
        Assertions.assertNotEquals(prop.getId(), prop2.getId());
    }

    @Test
    public void testGetSenderEmail() {
        Assertions.assertEquals(senderEmail, prop.getSenderUserEmail());
    }

    @Test
    public void testGetReceiverEmail() {
        Assertions.assertEquals(receiverEmail, prop.getReceiverUserEmail());
    }

    @Test
    public void testGetSenderPhysicalCards() {
        Assertions.assertEquals(senderCards, prop.getSenderOwnedCards());
    }

    @Test
    public void testGetReceiverPhysicalCards() {
        Assertions.assertEquals(receiversCards, prop.getReceiverOwnedCards());
    }

    @Test
    public void testEqualsAfterSerializeAndDeserialize() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Offer> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, prop);

        byte[] data = out.copyBytes();
        GsonSerializer<Offer> deserializer = new GsonSerializer<>(gson);
        Offer deserializedProposal = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(prop, deserializedProposal);
    }
}
