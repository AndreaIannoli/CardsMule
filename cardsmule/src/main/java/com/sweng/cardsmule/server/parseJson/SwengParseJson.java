package com.sweng.cardsmule.server.parseJson;

import com.sweng.cardsmule.shared.models.SwengCard;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class SwengParseJson {
    private SwengParseCards strategy;
    private final Gson gson;

    public SwengParseJson(SwengParseCards  strategy, Gson gson) {
        this.strategy = strategy;
        this.gson = gson;
    }

    public void setParseStrategy(SwengParseCards  strategy) {
        this.strategy = strategy;
    }

    public SwengCard[] parseJSON(String filePath) throws FileNotFoundException {
        JsonArray jsonArray = gson.fromJson(new FileReader(filePath), JsonArray.class);

        SwengCard[] cards = new SwengCard[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            cards[i] = strategy.execute(jsonArray.get(i).getAsJsonObject());
        }
        return cards;
    }
}