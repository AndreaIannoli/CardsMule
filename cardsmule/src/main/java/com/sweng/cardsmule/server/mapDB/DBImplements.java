package com.sweng.cardsmule.server.mapDB;

import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.function.Function;


public class DBImplements implements MapDB, MapDBConst {

    private static DB getDB(ServletContext ctx, String dbType) {
        synchronized (ctx) {//solo un thread alla volta puù eseguire quel codice
            DB db = (DB) ctx.getAttribute(dbType + "_CTX_ATTRIBUTE");
            if (db == null) {
                if (dbType.equals(DB_MEMORY)) {
                    db = DBMaker.memoryDB().make();
                } else if (dbType.equals(DB_FILE)) {
                    db = DBMaker.fileDB(DB_NAME).transactionEnable().closeOnJvmShutdown().make();
                }
                ctx.setAttribute(dbType + "_CTX_ATTRIBUTE", db);
            }
            return db;
        }
    }

    @Override
    public <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                         Serializer<V> valueSerializer) {
        return getDB(ctx, DB_MEMORY).hashMap(mapName, keySerializer, valueSerializer).createOrOpen();
    }

    @Override
    public <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                             Serializer<V> valueSerializer) {
        return getDB(ctx, DB_FILE).hashMap(mapName, keySerializer, valueSerializer).createOrOpen();
    }

    @Override
    public <K, V, T> T writeOperation(ServletContext ctx, String mapName, Serializer<K> keySerializer, Serializer<V> valueSerializer, Function<Map<K, V>, T> operation) {
        DB db = getDB(ctx, DB_FILE);
        try {
            T value = operation.apply(db.hashMap(mapName, keySerializer, valueSerializer).createOrOpen());
            db.commit();
            return value;
        } catch (Exception e) {
            db.rollback();
            return null;
        }
    }

    @Override
    public boolean writeOperation(ServletContext ctx, Runnable runnable) {
        DB db = getDB(ctx, DB_FILE);
        try {
            runnable.run();
            db.commit();
            return true;
        } catch (Exception e) {
            db.rollback();
            return false;
        }
    }
}