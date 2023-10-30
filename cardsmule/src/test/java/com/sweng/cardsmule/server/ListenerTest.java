package com.sweng.cardsmule.server;

import org.easymock.EasyMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapdb.Serializer;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.services.*;
import com.sweng.cardsmule.shared.models.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class ListenerTest {
    MapDB mockDB;
    ServletContext mockCtx;

    @BeforeEach
    public void initialize() {
        mockDB = EasyMock.createStrictMock(MapDB.class);
        mockCtx = createStrictMock(ServletContext.class);
    }

    @Test
    public void testExceptedFile() {
        ServletContextListener listener = new SwengListenerImpl(mockDB, "");

        Map<Integer, SwengCard> expectedMap = new HashMap<>();
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap).times(3);
        replay(mockDB);

        listener.contextInitialized(new ServletContextEvent(mockCtx));

        verify(mockDB);

        Assertions.assertEquals(0, expectedMap.size());
    }

    @Test
    public void testJSONCards() {
        ServletContextListener listener = new SwengListenerImpl(mockDB, "src/main/resources/Json/");

        Map<Integer, SwengCard> yugiohMap = new HashMap<>();
        Map<Integer, SwengCard> magicMap = new HashMap<>();
        Map<Integer, SwengCard> pokemonMap = new HashMap<>();

        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(yugiohMap);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(magicMap);
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(pokemonMap);
        replay(mockDB);

        listener.contextInitialized(new ServletContextEvent(mockCtx));

        verify(mockDB);

        Assertions.assertAll(() -> {
            Assertions.assertEquals(200, yugiohMap.size());
            Assertions.assertEquals(201, magicMap.size());
            Assertions.assertEquals(200, pokemonMap.size());
        });
    }

    @Test
    public void testDestroyed() {
        ServletContextListener listener = new SwengListenerImpl();
        Assertions.assertDoesNotThrow(() -> listener.contextDestroyed(new ServletContextEvent(mockCtx)));
    }
}
