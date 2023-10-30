package com.sweng.cardsmule.server;


import org.mapdb.Serializer;

import org.mindrot.jbcrypt.BCrypt;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.Offer;

import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

class TestDBCreation implements MapDB, MapDBConst {
    Map<String, Account> userMap = new HashMap<String, Account>() {{
        put("test@DB.com", new Account("test@DB.com", "usernameTest","12345678"));
        put("test2@DB.com", new Account("test2@DB.com", "usernameTest2","12345678"));
    }};

    Map<String, Account> loginMap = new HashMap<String, Account>() {
        {
            put("validToken", new Account("usernameTest", "test@DB.com", "12345678"));
            put("validToken2", new Account("usernameTest2", "test2@DB.com", "12345678"));
            put("validToken3", new Account("usernameTest3", "test3@DB.com", "12345678"));
        }
    };

    Map<Integer, Offer> offerMap;
    HashMap<String, LinkedHashMap<String, Collection>> collectionMap;

    public TestDBCreation(Map<Integer, Offer> offerMap, HashMap<String, LinkedHashMap<String, Collection>> hashMap) {
        this.offerMap = offerMap;
        this.collectionMap = hashMap;
    }

    public Map<Integer, Offer> getOfferMap() {
        return offerMap;
    }

    public HashMap<String, LinkedHashMap<String, Collection>> getCollectionMap() {
        return collectionMap;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer) {
        switch (mapName) {
            case MAP_MAGIC:
                return (Map<K, V>) ServerData.createMagicServerMap();
            case MAP_POKEMON:
                return (Map<K, V>) ServerData.createPokemonServerMap();
            case MAP_YUGIOH:
                return (Map<K, V>) ServerData.createYuGiOhServerMap();
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