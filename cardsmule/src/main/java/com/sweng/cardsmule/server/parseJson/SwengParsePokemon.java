package com.sweng.cardsmule.server.parseJson;

import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class SwengParsePokemon implements SwengParseCards {

    public SwengPokemonCard execute(JsonObject json) {
        // fields
        String name = json.has("name") ? json.get("name").getAsString() : "unknown";
        String description = json.has("description") ? json.get("description").getAsString() : "unknown";
        String types = json.has("types") ? json.get("types").getAsString() : "unknown";
        String illustrator = json.has("illustrator") ? json.get("illustrator").getAsString() : "unknown";
        String image = json.has("image") ? json.get("image").getAsString() : "";
        String rarity = json.has("rarity") ? json.get("rarity").getAsString() : "unknown";

        JsonObject variantsObject = json.getAsJsonObject("variants");
        List<String> variants = new ArrayList<>();
        String[] variantNames = {"firstEdition", "holo", "normal", "reverse", "wPromo"};
        for (String variantName : variantNames) {
            if (variantsObject != null && variantsObject.get(variantName).getAsBoolean()) {
                variants.add(variantName);
            }
        }

        return new SwengPokemonCard(name, description, types, illustrator, image, rarity, variants.toArray(new String[0]));
    }
}