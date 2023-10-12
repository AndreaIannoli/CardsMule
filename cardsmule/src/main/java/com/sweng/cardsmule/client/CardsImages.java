package com.sweng.cardsmule.client;

import com.sweng.cardsmule.shared.models.CardsmuleGame;

import java.util.HashMap;
import java.util.Map;

public class CardsImages{
	
	  private static final Map<CardsmuleGame, String> lookupTable = new HashMap<CardsmuleGame, String>() {{
	        put(CardsmuleGame.MAGIC, "cradsImage/magic.png");
	        put(CardsmuleGame.POKEMON, "cradsImage/pokemon.png");
	        //put(CardsmuleGame.YUGIOH, "placeholders/yugioh-placeholder.png");
	    }};

	    public static String getPath(CardsmuleGame game) {
	        return lookupTable.get(game);
	    }
}