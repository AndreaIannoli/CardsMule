package com.sweng.cardsmule.server;

import com.google.gson.Gson;

import com.sweng.cardsmule.server.parseJson.*;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

public class parserServerTest {
    Gson gson;
    @BeforeEach
    public void initialize() {
        gson = new Gson();
    }
    
    

    @Test
    public void StrategyParseTest() throws FileNotFoundException {
    	SwengParseJson jsonParser = new SwengParseJson(new SwengParseYiGiOhCard(), gson);
        SwengCard[] YuGiOhCardCards = jsonParser.parseJSON("src/main/resources/Json/yugioh_cards.json");
        
        Assertions.assertTrue(YuGiOhCardCards[0] instanceof SwengYuGiOhCard);
        Assertions.assertTrue(YuGiOhCardCards[99] instanceof SwengYuGiOhCard);
        Assertions.assertTrue(YuGiOhCardCards[199] instanceof SwengYuGiOhCard);
        
        jsonParser.setParseStrategy(new SwengParsePokemon());
        SwengCard[] pokemonCards = jsonParser.parseJSON("src/main/resources/Json/pokemon_cards.json");
        Assertions.assertTrue(pokemonCards[0] instanceof SwengPokemonCard);
        Assertions.assertTrue(pokemonCards[99] instanceof SwengPokemonCard);
        Assertions.assertTrue(pokemonCards[199] instanceof SwengPokemonCard);
        

        jsonParser.setParseStrategy(new SwengParseMagic());
        SwengCard[] magicCards = jsonParser.parseJSON("src/main/resources/Json/magic_cards.json");
        Assertions.assertTrue(magicCards[0] instanceof SwengCardMagic);
        Assertions.assertTrue(magicCards[99] instanceof SwengCardMagic);
        Assertions.assertTrue(magicCards[199] instanceof SwengCardMagic);


    }
    
    @Test
    public void YuGiOhJSONParseTest() throws FileNotFoundException {
        SwengParseCards parseCard = new SwengParseYiGiOhCard();
        SwengParseJson parser = new SwengParseJson(parseCard, gson);

        SwengCard[] yuGiOhCards = parser.parseJSON("src/main/resources/Json/yugioh_cards.json");

        Assertions.assertEquals(yuGiOhCards[2].getName(), "Beetrooper Assault Roller");
        Assertions.assertEquals(yuGiOhCards[2].getType(), "Effect Monster");
        Assertions.assertEquals(yuGiOhCards[2].getDescription(), "You can Special Summon this card (from your hand) by banishing 1 Insect monster from your GY. "
        		+ "You can only Special Summon \"Beetrooper Assault Roller\" once per turn this way. Gains 200 ATK for each Insect monster you control, except this card. When this "
        		+ "card is destroyed by battle: You can add 1 \"Beetrooper\" monster from your Deck to your hand, except \"Beetrooper Assault Roller\". You can only use this effect of "
        		+ "\"Beetrooper Assault Roller\" once per turn.");
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[2]).getRace(), "Insect");
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[2]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/51578214.jpg");
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[2]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/51578214.jpg");

        Assertions.assertEquals(yuGiOhCards[4].getName(), "Ring of Magnetism");
        Assertions.assertEquals(yuGiOhCards[4].getType(), "Spell Card");
        Assertions.assertEquals(yuGiOhCards[4].getDescription(), "You can only equip this card to a monster on your side of the field. "
        		+ "Decrease the ATK and DEF of a monster equipped with this card by 500 points. "
        		+ "In addition, all the monsters on your opponent's side of the field can only attack the monster equipped with this card, if they attack.");        
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[4]).getRace(), "Equip");
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[4]).getImageUrl(), "https://images.ygoprodeck.com/images/cards/20436034.jpg");
        Assertions.assertEquals(((SwengYuGiOhCard) yuGiOhCards[4]).getSmallImageUrl(), "https://images.ygoprodeck.com/images/cards_small/20436034.jpg");

        Assertions.assertEquals(yuGiOhCards.length, 200);
    }
    
    
    @Test
    public void PokemonJSONParseTest() throws FileNotFoundException {
    	SwengParseCards parseCard = new SwengParsePokemon();
    	SwengParseJson parser = new SwengParseJson(parseCard, gson);

        SwengCard[] pokemonCards = parser.parseJSON("src/main/resources/Json/pokemon_cards.json");

        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[2]).getArtist(), "Wataru Kawahara"); // illustrator
        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[2]).getImageUrl(), "https://assets.tcgdex.net/en/pl/pl4/83");
        Assertions.assertEquals(pokemonCards[2].getName(), "Bench Shield");
        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[2]).getRarity(), "Uncommon");
        Assertions.assertFalse((pokemonCards[2]).getState().contains("firstEdition"));
        Assertions.assertTrue((pokemonCards[2]).getState().contains("holo"));
        Assertions.assertTrue((pokemonCards[2]).getState().contains("normal"));
        Assertions.assertTrue((pokemonCards[2]).getState().contains("reverse"));
        Assertions.assertFalse((pokemonCards[2]).getState().contains("wPromo"));

        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[3]).getArtist(), "tetsuya koizumi"); // illustrator
        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[3]).getImageUrl(), "https://assets.tcgdex.net/en/sm/sm8/70");
        Assertions.assertEquals(pokemonCards[3].getName(), "Bruxish");
        Assertions.assertEquals(((SwengPokemonCard) pokemonCards[3]).getRarity(), "Common");
        Assertions.assertFalse((pokemonCards[3]).getState().contains("firstEdition"));
        Assertions.assertTrue((pokemonCards[3]).getState().contains("holo"));
        Assertions.assertTrue((pokemonCards[3]).getState().contains("normal"));
        Assertions.assertTrue((pokemonCards[3]).getState().contains("reverse"));
        Assertions.assertFalse((pokemonCards[3]).getState().contains("wPromo"));
        Assertions.assertEquals(pokemonCards[3].getType(), "Water");

        Assertions.assertEquals(pokemonCards.length, 200);
    }

    @Test
    public void MagicJSONParseTest() throws FileNotFoundException {
    	SwengParseCards parseCard = new SwengParseMagic();
    	SwengParseJson parser = new SwengParseJson(parseCard, gson);
        SwengCard[] magicCards = parser.parseJSON("src/main/resources/Json/magic_cards.json");

        Assertions.assertEquals(((SwengCardMagic) magicCards[1]).getArtist(), "Pete Venters");
        Assertions.assertEquals(magicCards[1].getName(), "Ancestor's Chosen");
        Assertions.assertEquals(magicCards[1].getDescription(), "First strike (This creature deals combat damage before creatures without first strike.)\nWhen Ancestor's Chosen enters the battlefield, you gain 1 life for each card in your graveyard.");
        Assertions.assertEquals(magicCards[1].getType(), "Creature");
        Assertions.assertEquals(((SwengCardMagic) magicCards[1]).getRarity(), "uncommon");
        Assertions.assertTrue(magicCards[1].getState().contains("hasFoil"));
        Assertions.assertTrue(magicCards[1].getState().contains("isAlternative"));
        Assertions.assertFalse(magicCards[1].getState().contains("isFullArt"));
        Assertions.assertFalse(magicCards[1].getState().contains("isPromo"));
        Assertions.assertTrue(magicCards[1].getState().contains("isReprint"));

        Assertions.assertEquals(((SwengCardMagic) magicCards[7]).getArtist(), "John Avon");
        Assertions.assertEquals(magicCards[7].getName(), "Angelic Wall");
        Assertions.assertEquals(magicCards[7].getDescription(), "Defender (This creature can't attack.)\nFlying");
        Assertions.assertEquals(magicCards[7].getType(), "Creature");
        Assertions.assertEquals(((SwengCardMagic) magicCards[7]).getRarity(), "common");
        Assertions.assertFalse(magicCards[7].getState().contains("hasFoil"));
        Assertions.assertFalse(magicCards[7].getState().contains("isAlternative"));
        Assertions.assertFalse(magicCards[7].getState().contains("isFullArt"));
        Assertions.assertFalse(magicCards[7].getState().contains("isPromo"));
        Assertions.assertTrue(magicCards[7].getState().contains("isReprint"));

        Assertions.assertEquals(magicCards.length, 201);
    }



}
