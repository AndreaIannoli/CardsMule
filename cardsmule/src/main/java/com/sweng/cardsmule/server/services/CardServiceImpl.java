package com.sweng.cardsmule.server.services;

import com.sweng.cardsmule.server.mapDB.DBImplements;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.google.gson.Gson;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;

public class CardServiceImpl {
    private final MapDB db;
    private final GsonSerializer<SwengCard> serializer = new GsonSerializer<>(new Gson());
    
    
    public CardServiceImpl() {
        db = new DBImplements();
    }

    public CardServiceImpl(MapDB db) {
        this.db = db;
    }
    
    
    
}
