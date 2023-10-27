package com.sweng.cardsmule.shared;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sweng.cardsmule.shared.models.SwengCardMagic;

public class SwengMagicCardTest implements SwengCardTestConst {
	private SwengCardMagic magicCard;
    private SwengCardMagic differentMagicCard;

    @BeforeEach
    public void initialize() {
        magicCard = new SwengCardMagic(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint");
        differentMagicCard = new SwengCardMagic("alpha", "alpha", "alpha", "alpha",
                "alpha");
    }

    @Test
    public void testGetName() {
        Assertions.assertEquals(cardName, magicCard.getName());
    }

    @Test
    public void testGetDescription() {
        Assertions.assertEquals(cardDesc, magicCard.getDescription());
    }

    @Test
    public void testGetType() {
        Assertions.assertEquals(cardType, magicCard.getType());
    }

    @Test
    public void testGetVariants() {
        Assertions.assertAll(() -> {
            Assertions.assertTrue(magicCard.getState().contains("hasFoil"));
            Assertions.assertTrue(magicCard.getState().contains("isAlternative"));
            Assertions.assertTrue(magicCard.getState().contains("isFullArt"));
            Assertions.assertTrue(magicCard.getState().contains("isPromo"));
            Assertions.assertTrue(magicCard.getState().contains("isReprint"));
            Assertions.assertFalse(magicCard.getState().contains("test"));
        });
    }

    @Test
    public void testGetArtist() {
        Assertions.assertEquals(genericArtist, magicCard.getArtist());
    }

    @Test
    public void testGetRarity() {
        Assertions.assertEquals(genericRarity, magicCard.getRarity());
    }

    @Test
    public void testEqualsForItself() {
        Assertions.assertTrue(magicCard.equals(magicCard));
    }

    @Test
    public void testEqualsForDifferentFieldsObject() {
        Assertions.assertFalse(magicCard.equals(new Object()));
    }

    @Test
    public void testEqualsForDifferentFieldsCard() {
        Assertions.assertFalse(magicCard.equals(differentMagicCard));
    }

    @Test
    public void testEqualsForEqualFieldsCard() {
        Assertions.assertTrue(magicCard.equals(new SwengCardMagic(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint")));
    }

    @Test
    public void testHashCodeForDifferentFieldsCard() {
        Assertions.assertFalse(magicCard.hashCode() == differentMagicCard.hashCode());
    }

    @Test
    public void testHashCodeForEqualFieldsCard() {
        Assertions.assertTrue(magicCard.hashCode() == new SwengCardMagic(cardName, cardDesc, cardType, genericArtist,
                genericRarity, "hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint").hashCode());
    }
}
