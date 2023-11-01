package com.sweng.cardsmule.server;
import com.google.gson.Gson;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.server.mapDB.DBImplements;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import static org.easymock.EasyMock.*;
interface MockObject {
    int getNum();
    void setNum(int num);
}
public class MapDBTest {
    final static String PATH = "mapDB.db";
    final static String MAP_NAME = "testMapName";
    final static String MAP_KEY = "test";
    private static Stream<Arguments> provideMockObjects() {
        return Stream.of(
                Arguments.of(new Mock1(1)),
                Arguments.of(new Mock2(1))
        );
    }
    private static DB close_and_reopen_db(DB db) {
        db.commit();
        System.out.println("Closing and reopening db");
        db.close();
        return DBMaker.fileDB(PATH).make();
    }
    @AfterEach()
    public void deleteFile() {
        File f = new File(PATH);
        if (f.delete()) {
            System.out.println("DB test file deleted successfully");
        } else {
            System.out.println("Failed to delete the DB test file");
        }
    }
    // hypothesis: every deserialization will produce an independent copy.
    @ParameterizedTest
    @MethodSource("provideMockObjects")
    public void testMapDBForNotUpdateMockObject(MockObject mockObj) {
        GsonSerializer<MockObject> serializer = new GsonSerializer<>(new Gson());
        DB db = DBMaker.fileDB(PATH).make();
        Map<String, MockObject> map = db.hashMap(MAP_NAME).keySerializer(Serializer.STRING).valueSerializer(serializer).createOrOpen();
        map.clear();
        map.put(MAP_KEY, mockObj);
        db = close_and_reopen_db(db);
        map = db.hashMap(MAP_NAME).keySerializer(Serializer.STRING).valueSerializer(serializer).createOrOpen();
        Objects.requireNonNull(map.get(MAP_KEY)).setNum(3);
        db = close_and_reopen_db(db);
        map = db.hashMap(MAP_NAME).keySerializer(Serializer.STRING).valueSerializer(serializer).createOrOpen();
        Assertions.assertEquals(1, Objects.requireNonNull(map.get("test")).getNum());
        db.close();
    }
    
    
}
class Mock1 implements MockObject {
    private int num;
    public Mock1(int num) {
        this.num = num;
    }
    @Override
    public int getNum() {
        return num;
    }
    @Override
    public void setNum(int num) {
        this.num = num;
    }
}

class Mock2 implements MockObject, Serializable {
    private static final long serialVersionUID = 3263613519301667750L;
    private int num;
    public Mock2(int num) {
        this.num = num;
    }
    @Override
    public int getNum() {
        return num;
    }
    @Override
    public void setNum(int num) {
        this.num = num;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mock2)) return false;
        Mock2 that = (Mock2) o;
        return getNum() == that.getNum();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getNum());
    }
}
