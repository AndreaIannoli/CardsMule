package com.sweng.cardsmule.server.gsonserializer;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.lang.reflect.Type;
//La serializzazione e la deserializzazione sono processi per convertire oggetti in una rappresentazione binaria in modo che possano essere 
//memorizzati su disco come nel nostro caso con MapDB e quindi ripristinati in un oggetto quando necessario
public class GsonSerializer<T> implements Serializer<T> {

    private final Gson gson;
    private Type type;

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public GsonSerializer(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull T value) throws IOException {
        if (type != null) {
            String json = gson.toJson(value, type);
            out.writeUTF(json);
        } else {
            JsonObject jsonObj = gson.toJsonTree(value).getAsJsonObject();
            jsonObj.addProperty("classType", value.getClass().getName());
            out.writeUTF(jsonObj.toString());
        }
    }

    @Override
    public T deserialize(DataInput2 in, int available) throws IOException {
        JsonObject jsonObj = gson.fromJson(in.readUTF(), JsonObject.class);
        JsonElement classType = jsonObj.get("classType");
        if (classType == null) {
            return gson.fromJson(jsonObj, type);
        }
        return gson.fromJson(jsonObj, (Type) getObjectClass(classType.getAsString()));
    }

    private Class<?> getObjectClass(String classType) {
        try {
            return Class.forName(classType);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unable to deserialize class of type " + classType);
        }
    }
}