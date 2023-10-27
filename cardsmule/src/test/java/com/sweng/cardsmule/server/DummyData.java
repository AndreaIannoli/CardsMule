package com.sweng.cardsmule.server;
import java.util.*;
import java.util.stream.Collectors;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import java.util.LinkedHashMap;
// Create dummy cards with distinct fields for testing purposes
public class DummyData {
    public static SwengPokemonCard createPokemonDummyCard() {
        return new SwengPokemonCard("Charizard", "The flame Pokemon", "Flame",
                "Ken Sugimori", "http://www.charizard-image.jpg", "Rare",
                "holo");
    }
    public static SwengYuGiOhCard createYuGiOhDummyCard() {
        return new SwengYuGiOhCard("Exodia the Forbidden One", "One of the most powerful monsters in the game",
                "Creature", "Divine-Beast", "http://www.exodia-image.jpg", "http://www.exodia-thumbnail.jpg"
        );
    }
    public static Map<Integer, SwengCardMagic> createMagicDummyMap() {
        return new LinkedHashMap<Integer, SwengCardMagic>() {{
            put(0, new SwengCardMagic("Lightning Bolt", "Deal 3 damage to any target", "Cast",
                    "Christopher Rush", "Rare", "hasFoil")
            );
            put(1, new SwengCardMagic("Counterspell", "Counter target spell", "Instant",
                    "Mark Poole", "Uncommon", "isAlternative")
            );
            put(2, new SwengCardMagic("Plague Wind", "Deal X damage to each creature and each player, where X is the number of creatures you control.", "Sorcery",
                    "Ron Spencer", "Mythic Rare", "isFullArt")
            );
            put(3, new SwengCardMagic("Angel of Mercy", "When Angel of Mercy enters the battlefield, you gain 3 life", "Creature",
                    "Volkan Baa", "Mega Rare", "isPromo")
            );
            put(4, new SwengCardMagic("Heart of Light", "Enchant creature (Target a creature as you cast this. This card enters the battlefield attached to that creature.)", "Counter",
                    "Luca Zontini", "Unique", "isReprint")
            );
        }};
    }

    public static Map<Integer, SwengPokemonCard> createPokemonDummyMap() {
        return new HashMap<Integer, SwengPokemonCard>() {{
            put(0, new SwengPokemonCard("Pikachu", "The electric mouse Pokemon", "Electric",
                    "Atsuko Nishida", "http://www.pikachu-image.jpg", "Common",
                    "firstEdition"
            ));
            put(1, createPokemonDummyCard());
            put(2, new SwengPokemonCard("Blastoise", "The shellfish Pokemon", "Water",
                    "Mitsuhiro Arita", "http://www.blastoise-image.jpg", "Uncommon",
                    "normal"
            ));
            put(3, new SwengPokemonCard("Mewtwo", "The Genetic Pokemon", "Psychic",
                    "Akira Egawa", "http://www.mewtwo-image.jpg", "Ultra Rare",
                    "reverse"
            ));
            put(4, new SwengPokemonCard("Hitmonlee", "The legs freely contract and stretch", "Fighting",
                    "Shigenori Negishi", "https://assets.tcgdex.net/en/swsh/swsh1/94", "Unique",
                    "wPromo"
            ));
        }};
    }
    public static Map<Integer, SwengYuGiOhCard> createYuGiOhDummyMap() {
        return new HashMap<Integer, SwengYuGiOhCard>() {
            {
                put(0, new SwengYuGiOhCard("Dark Magician", "A powerful sorcerer", "Monster",
                        "Spellcaster", "http://www.darkmagician-image.jpg", "http://www.darkmagician-thumbnail.jpg"
                ));
                put(1, createYuGiOhDummyCard());
                put(2, new SwengYuGiOhCard("Monster Reborn", "Special Summon a monster from either player's graveyard", "Spell",
                        "Monster", "http://www.monsterreborn-image.jpg", "http://www.monsterreborn-thumbnail.jpg"
                ));
                put(3, new SwengYuGiOhCard("Pot of Greed", "Draw 2 cards", "Draw",
                        "Pot", "http://www.potofgreed-image.jpg", "http://www.potofgreed-thumbnail.jpg"
                ));
            }
        };
    }
    public static List<SwengCard> createMagicDummyList() {
        return new ArrayList<>(createMagicDummyMap().values());
    }
    public static List<SwengCard> createPokemonDummyList() {
        return new ArrayList<>(createPokemonDummyMap().values());
    }
    public static List<SwengCard> createYuGiOhDummyList() {
        return new ArrayList<>(createYuGiOhDummyMap().values());
    }
    public static List<OwnedCard> createPhysicalCardDummyList(int n) {
        List<OwnedCard> list = new ArrayList<>();
        for (int i = 0; i < n; i++)
            list.add(new OwnedCard((i + 3000), Grade.getRandomGrade(), CardsmuleGame.randomGame(), "email", "This is a valid description."));
        return list;
    }
    public static List<OwnedCardFetched> createPhysicalCardWithNameDummyList(int n) {
        return createPhysicalCardDummyList(n).stream().map(oCard -> new OwnedCardFetched(oCard, "test")).collect(Collectors.toList());
    }
}