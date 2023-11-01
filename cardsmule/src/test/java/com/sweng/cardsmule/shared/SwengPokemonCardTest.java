package com.sweng.cardsmule.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.cardsmule.shared.models.SwengPokemonCard;

public class SwengPokemonCardTest implements SwengCardTestConst {
	private SwengPokemonCard pokemonCard;
    private SwengPokemonCard differentPokemonCard;

    @BeforeEach
    public void initialize() {
        pokemonCard = new SwengPokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo");
        differentPokemonCard = new SwengPokemonCard("alpha", "alpha", "alpha", "alpha", "alpha",
                "alpha");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, pokemonCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, pokemonCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, pokemonCard.getType());
    }

    @Test
    public void testGetVariants() {
        Assertions.assertAll(() -> {
            Assertions.assertTrue(pokemonCard.getState().contains("firstEdition"));
            Assertions.assertTrue(pokemonCard.getState().contains("holo"));
            Assertions.assertTrue(pokemonCard.getState().contains("normal"));
            Assertions.assertTrue(pokemonCard.getState().contains("reverse"));
            Assertions.assertTrue(pokemonCard.getState().contains("wPromo"));
            Assertions.assertFalse(pokemonCard.getState().contains("test"));
        });
    }

    @Test
    public void testGetArtist() {
        Assertions.assertEquals(genericArtist, pokemonCard.getArtist());
    }

    @Test
    public void testGetImageUrl() {
        Assertions.assertEquals(cardImageUrl, pokemonCard.getImageUrl());
    }

    @Test
    public void testGetRarity() {
        Assertions.assertEquals(genericRarity, pokemonCard.getRarity());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(pokemonCard.equals(pokemonCard));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(pokemonCard.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(pokemonCard.equals(differentPokemonCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(pokemonCard.equals(new SwengPokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo")));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(pokemonCard.hashCode() == differentPokemonCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(pokemonCard.hashCode() == new SwengPokemonCard(cardName, cardDesc, cardType, genericArtist, cardImageUrl,
                genericRarity, "firstEdition", "holo", "normal", "reverse", "wPromo").hashCode());
    }
}
