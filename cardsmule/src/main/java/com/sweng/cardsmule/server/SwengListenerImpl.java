package com.sweng.cardsmule.server;

import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.server.parseJson.SwengParseMagic;
import com.sweng.cardsmule.server.parseJson.SwengParsePokemon;
import com.sweng.cardsmule.server.parseJson.SwengParseYiGiOhCard;
import com.sweng.cardsmule.server.parseJson.SwengParseJson;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.server.mapDB.DBImplements;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.google.gson.Gson;
import org.mapdb.Serializer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.FileNotFoundException;
import java.util.Map;

public class SwengListenerImpl implements ServletContextListener, MapDBConst {
    private final MapDB db;
    private final String path;

    public SwengListenerImpl() {
        db = new DBImplements();
        path = "./WEB-INF/classes/Json/";
    }

    public SwengListenerImpl(MapDB db, String path) {
        this.db = db;
        this.path = path;
    }

    private void uploadDataToDB(Map<Integer, SwengCard> map, SwengCard[] cards) {
        for (SwengCard card : cards) {
            map.put(card.getId(), card);
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) throws RuntimeException {
        System.out.println("Context initialized.");
        System.out.println("*** Loading data from file. ***");

        Gson gson = new Gson();
        GsonSerializer<SwengCard> cardSerializer = new GsonSerializer<>(gson);

        Map<Integer, SwengCard> yuGiOhMap = db.getCachedMap(sce.getServletContext(), MAP_YUGIOH,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, SwengCard> magicMap = db.getCachedMap(sce.getServletContext(), MAP_MAGIC,
                Serializer.INTEGER, cardSerializer);
        Map<Integer, SwengCard> pokemonMap = db.getCachedMap(sce.getServletContext(), MAP_POKEMON,
                Serializer.INTEGER, cardSerializer);

        SwengParseJson parser = new SwengParseJson(new SwengParseYiGiOhCard(), gson);
        try {
        	System.out.println("Working Directory = " + System.getProperty("user.dir"));
            uploadDataToDB(yuGiOhMap, parser.parseJSON(path + "yugioh_cards.json"));
            parser.setParseStrategy(new SwengParseMagic());
            uploadDataToDB(magicMap, parser.parseJSON(path + "magic_cards.json"));
            parser.setParseStrategy(new SwengParsePokemon());
            uploadDataToDB(pokemonMap, parser.parseJSON(path + "pokemon_cards.json"));
        } catch (FileNotFoundException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
        System.out.println("*** Data Loaded. ***");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}