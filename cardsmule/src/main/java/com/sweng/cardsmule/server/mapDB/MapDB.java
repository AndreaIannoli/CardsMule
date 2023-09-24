package com.sweng.cardsmule.server.mapDB;

import org.mapdb.Serializer;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.function.Function;

public interface MapDB {
    <K, V> Map<K, V> getCachedMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                  Serializer<V> valueSerializer);

    <K, V> Map<K, V> getPersistentMap(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                                      Serializer<V> valueSerializer);

    <K, V, T> T writeOperation(ServletContext ctx, String mapName, Serializer<K> keySerializer,
                               Serializer<V> valueSerializer, Function<Map<K, V>, T> operation);

    boolean writeOperation(ServletContext ctx, Runnable runnable);
}