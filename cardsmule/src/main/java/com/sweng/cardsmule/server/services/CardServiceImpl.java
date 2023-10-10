package com.sweng.cardsmule.server.services;

import com.sweng.cardsmule.server.mapDB.DBImplements;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.shared.CardService;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.GeneralException;

import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.shared.throwables.GeneralException;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;


import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.mapdb.Serializer;

import com.google.gson.Gson;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;

public class CardServiceImpl extends RemoteServiceServlet implements MapDBConst, CardService{
    private final MapDB db;
    private final GsonSerializer<SwengCard> serializer = new GsonSerializer<>(new Gson());
    
    
    public CardServiceImpl() {
        db = new DBImplements();
    }

    public CardServiceImpl(MapDB db) {
        this.db = db;
    }
    
    
    
    
    public static String getCardMap(CardsmuleGame game) {
        return game == CardsmuleGame.MAGIC ? MAP_MAGIC :
                game == CardsmuleGame.POKEMON ? MAP_POKEMON :
                	MAP_YUGIOH;
    }

    public static String getNameCard(int idCard, Map<Integer, SwengCard> cardMap) {
        try {
            return cardMap.get(idCard).getName();
        } catch (NullPointerException e) {
            return "No Name Found";
        }
    }

    public static void checkGameValidity(CardsmuleGame game) throws InputException {
        if (game == null)
            throw new InputException("game cannot be null");
    }

    public static void checkCardIdValidity(int cardId) throws InputException {
        if (cardId <= 0) {
            throw new InputException("Invalid cardId provided. cardId must be a positive integer greater than 0");
        }
    }

   
    public List<SwengCard> getGameCards(CardsmuleGame game) throws InputException {
        checkGameValidity(game);
        Map<Integer, SwengCard> map = db.getCachedMap(getServletContext(), getCardMap(game),
                Serializer.INTEGER, serializer);
        System.out.println("INIZIO CARTE DI " + game);
        for(Integer i: map.keySet()) {
        	System.out.println(map.get(i));
        }
        System.out.println("FINE CARTE DI " + game);
        return new ArrayList<>(map.values());
    }

	public SwengCard getGameCard(CardsmuleGame game, int cardId) throws InputException {
        checkGameValidity(game);
        checkCardIdValidity(cardId);
        Map<Integer, SwengCard> map = db.getCachedMap(getServletContext(), getCardMap(game),
                Serializer.INTEGER, serializer);
        return map.get(cardId);
    }


    
    
    
}
