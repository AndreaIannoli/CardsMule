package com.sweng.cardsmule.server.parseJson;

import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.google.gson.JsonObject;

public class SwengParseYiGiOhCard implements SwengParseCards{
	
    public SwengYuGiOhCard execute(JsonObject json) {
        //fields
        String name = json.has("name") ? json.get("name").getAsString() : "unknown";
        String description = json.has("desc") ? json.get("desc").getAsString() : "unknown";
        String types = json.has("type") ? json.get("type").getAsString() : "unknown";
        String race = json.has("race") ? json.get("race").getAsString() : "unknown";
        String imageUrl = json.has("image_url") ? json.get("image_url").getAsString() : "";
        String smallImageUrl = json.has("small_image_url") ? json.get("small_image_url").getAsString() : "";

        return new SwengYuGiOhCard(name, description, types, race, imageUrl, smallImageUrl);
    }
}