package com.sweng.cardsmule.server;


import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.shared.LoginSession;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.Offer;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

class TestDBCreation implements MapDB, MapDBConst {
    Map<String, Account> userMap = new HashMap<String, Account>() {{
        put("test@test.it", new Account("test@DB.com", "usernameTest",BCrypt.hashpw("12345678", BCrypt.gensalt())));
        put("test2@test.it", new Account("test2@DB.com", "usernameTest2",BCrypt.hashpw("12345678", BCrypt.gensalt())));
    }};

    Map<String, LoginSession> loginMap = new HashMap<String, LoginSession>() {
        {
            put("validToken", new LoginSession("usernameTest", "test@DB.com", System.currentTimeMillis() - 10000));
            put("validToken2", new LoginSession("usernameTest2", "test2@DB.com", System.currentTimeMillis() - 20000));
            put("validToken3", new LoginSession("usernameTest3", "test3@DB.com", System.currentTimeMillis() - 30000));
        }
    };

    Map<Integer, Offer> offerMap;
    Map<String, Map<String, Collection>> collectionMap;

    public TestDBCreation(Map<Integer, Offer> offerMap, Map<String, Map<String, Collection>> collectionMap) {
        this.offerMap = offerMap;
        this.collectionMap = collectionMap;
    }

    public Map<Integer, Offer> getOfferMap() {
        return offerMap;
    }

    public Map<String, Map<String, Collection>> getCollectionMap() {
        return collectionMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        switch (mapName) {
            case MAP_MAGIC:
                return (Map<K, V>) DummyData.createMagicDummyMap();
            case MAP_POKEMON:
                return (Map<K, V>) DummyData.createPokemonDummyMap();
            case MAP_YUGIOH:
                return (Map<K, V>) DummyData.createYuGiOhDummyMap();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <K, V> Map<K, V> getMapName(String mapName) {
        switch (mapName) {
            case MAP_USER:
                return (Map<K, V>) userMap;
            case MAP_LOGIN:
                return (Map<K, V>) loginMap;
            case MAP_DECK:
                return (Map<K, V>) collectionMap;
            case MAP_PROPOSAL:
                return (Map<K, V>) offerMap;
        }
        return null;
    }

    @Override
    public <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        return getMapName(mapName);
    }

    @Override
    public <K, V, T> T writeOperation(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer, Function<Map<K, V>, T> operation) {
        return operation.apply(getMapName(mapName));
    }

    @Override
    public boolean writeOperation(ServletContext ctx, Runnable runnable) {
        runnable.run();
        return true;
    }
}