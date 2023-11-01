package com.sweng.cardsmule.server;


import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.sweng.cardsmule.shared.CardTestConstant;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.server.gsonserializer.*;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.OwnedCard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class GsonSerializerTest implements CardTestConstant {

    private static Stream<Arguments> provideSubClasses() {
        return Stream.of(
                Arguments.of(new SwengCardMagic(cardName, cardDesc, cardType, genericArtist, genericRarity,
                        "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint")),
                Arguments.of(new SwengPokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl, genericRarity,
                        "firstEdition", "holo", "normal", "reverse", "wPromo")),
                Arguments.of(new SwengYuGiOhCard(cardName, cardDesc, cardType, yuGiOhRace, cardImageUrl, yuGiOhSmallImageUrl))
        );
    }

    @ParameterizedTest
    @MethodSource("provideSubClasses")
    public void testSerializerForCards(SwengCard card) throws IOException {
        Gson gson = new Gson();
        GsonSerializer<SwengCard> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        serializer.serialize(out, card);

        byte[] data = out.copyBytes();
        GsonSerializer<SwengCard> deserializer = new GsonSerializer<>(gson);
        SwengCard deserializedCard = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertEquals(card, deserializedCard);
    }

    @Test
    public void testSerializerForUnknownClass() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<SwengCard> serializer = new GsonSerializer<>(gson);

        byte[] data;
        try (DataOutput2 out = new DataOutput2()) {
            out.writeUTF("{\"classType\":\"com.aadm.NonExistentClass\"}");
            data = out.copyBytes();
        }
        Assertions.assertThrows(JsonParseException.class, () -> serializer.deserialize(new DataInput2.ByteArray(data), 0));
    }

    @Test
    public void testSerializerForMapOfStrings() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        GsonSerializer<Map<String, String>> serializer = new GsonSerializer<>(gson, type);
        DataOutput2 out = new DataOutput2();

        Map<String, String> stringMap = new HashMap<String, String>() {{
            put("test1", "test1");
            put("test2", "test2");
            put("test3", "test3");
        }};
        serializer.serialize(out, stringMap);

        byte[] data = out.copyBytes();
        GsonSerializer<Map<String, String>> deserializer = new GsonSerializer<>(gson, type);
        Map<String, String> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals("test1", deserializedDeckMap.get("test1"));
            Assertions.assertEquals("test2", deserializedDeckMap.get("test2"));
            Assertions.assertEquals("test3", deserializedDeckMap.get("test3"));
        });
    }

    @Test
    public void testSerializerForDeck() throws IOException {
        Gson gson = new Gson();
        GsonSerializer<Collection> serializer = new GsonSerializer<>(gson);

        DataOutput2 out = new DataOutput2();
        Collection ownedDeck = new Collection("Owned");
        OwnedCard mockOCard1 = new OwnedCard(111, Grade.Excellent, CardsmuleGame.MAGIC, "owner@email.com", "descrizione");
        OwnedCard mockOCard2 = new OwnedCard(222, Grade.Poor, CardsmuleGame.POKEMON, "owner@email.com", "descrizione");
        ownedDeck.addOwnedCard(mockOCard1);
        ownedDeck.addOwnedCard(mockOCard2);
        serializer.serialize(out, ownedDeck);

        byte[] data = out.copyBytes();
        GsonSerializer<Collection> deserializer = new GsonSerializer<>(gson);
        Collection deserializedDeck = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(ownedDeck, deserializedDeck);
            Assertions.assertEquals(2, ownedDeck.getOwnedCards().size());
            Assertions.assertTrue(ownedDeck.getOwnedCards().contains(mockOCard1));
            Assertions.assertTrue(ownedDeck.getOwnedCards().contains(mockOCard2));
        });
    }

    @Test
    public void testSerializerForMapOfMapOfDecks() throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Collection>>>() {
        }.getType();

        GsonSerializer<Map<String, Map<String, Collection>>> serializer = new GsonSerializer<>(gson, type);
        DataOutput2 out = new DataOutput2();

        Map<String, Map<String, Collection>> deckMap = new HashMap<>();
        Map<String, Collection> mockDecks = new HashMap<String, Collection>() {{
            put("Owned", new Collection("Owned"));
            put("Wished", new Collection("Wished"));
        }};
        OwnedCard mockOCard1 = new OwnedCard(111, Grade.Excellent, CardsmuleGame.MAGIC, "owner@email.com",  "descrizione");
        OwnedCard mockOCard2 = new OwnedCard(222, Grade.Poor, CardsmuleGame.POKEMON,  "owner@email.com", "descrizione");
        mockDecks.get("Owned").addOwnedCard(mockOCard1);
        mockDecks.get("Wished").addOwnedCard(mockOCard2);
        deckMap.put("test@test.it", mockDecks);
        serializer.serialize(out, deckMap);

        byte[] data = out.copyBytes();
        GsonSerializer<Map<String, Map<String, Collection>>> deserializer = new GsonSerializer<>(gson, type);
        Map<String, Map<String, Collection>> deserializedDeckMap = deserializer.deserialize(new DataInput2.ByteArray(data), 0);

        Assertions.assertAll(() -> {
            Assertions.assertNotNull(deserializedDeckMap.get("test@test.it"));
            Assertions.assertTrue(deserializedDeckMap.get("test@test.it").get("Owned").containsOwnedCard(mockOCard1));
            Assertions.assertTrue(deserializedDeckMap.get("test@test.it").get("Wished").containsOwnedCard(mockOCard2));
        });
    }
}
