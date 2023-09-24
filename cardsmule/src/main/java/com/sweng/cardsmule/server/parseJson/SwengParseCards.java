package com.sweng.cardsmule.server.parseJson;

import com.sweng.cardsmule.shared.models.SwengCard;
import com.google.gson.JsonObject;

public interface SwengParseCards {
    SwengCard execute(JsonObject json);
}