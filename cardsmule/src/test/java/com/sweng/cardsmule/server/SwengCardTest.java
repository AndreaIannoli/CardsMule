package com.sweng.cardsmule.server;

import org.easymock.IMocksControl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapdb.Serializer;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.services.CardServiceImpl;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.shared.throwables.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;


public class SwengCardTest {
    private IMocksControl ctrl;
    private MapDB mockDB;
    private CardServiceImpl cardService;

    @BeforeEach
    public void initialize() throws ServletException {
        ctrl = createStrictControl();
        mockDB = ctrl.createMock(MapDB.class);
        cardService = new CardServiceImpl(mockDB);
        ServletConfig mockConfig = ctrl.createMock(ServletConfig.class);
        ServletContext mockCtx = ctrl.createMock(ServletContext.class);
        expect(mockConfig.getServletContext()).andReturn(mockCtx);
        cardService.init(mockConfig);
    }

    private static Stream<Arguments> provideClassAndMockData() {
        return Stream.of(
                Arguments.of(CardsmuleGame.MAGIC, SwengCardMagic.class, ServerData.createMagicServerMap()),
                Arguments.of(CardsmuleGame.POKEMON, SwengPokemonCard.class, ServerData.createPokemonServerMap()),
                Arguments.of(CardsmuleGame.YUGIOH, SwengYuGiOhCard.class, ServerData.createYuGiOhServerMap())
        );
    }

    private static Stream<Arguments> provideMockData() {
        return Stream.of(
                Arguments.of(CardsmuleGame.MAGIC, ServerData.createMagicServerMap()),
                Arguments.of(CardsmuleGame.POKEMON, ServerData.createPokemonServerMap()),
                Arguments.of(CardsmuleGame.YUGIOH, ServerData.createYuGiOhServerMap())
        );
    }

    @ParameterizedTest
    @MethodSource("provideClassAndMockData")
    public void cardListTest(CardsmuleGame game, Class<?> clazz, Map<Integer, SwengCard> expectedMap) throws InputException {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        List<SwengCard> cards = cardService.getGameCards(game);
        ctrl.verify();
        Assertions.assertTrue(cards.stream().allMatch(card ->
                card.getClass() == clazz
        ));
    }

    @Test
    public void testNullGameCard() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCards(null));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGameSwengCard(CardsmuleGame game, Map<Integer, SwengCard> expectedMap) throws InputException {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        List<SwengCard> cards = cardService.getGameCards(game);
        ctrl.verify();
        Assertions.assertEquals(new ArrayList<>(expectedMap.values()), cards);
    }

    @Test
    public void GameCardNull() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCard(null, 2));
    }

    @Test
    public void testExceptionGameCard() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCard(CardsmuleGame.MAGIC, -10));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGameCardId(CardsmuleGame game, Map<Integer, SwengCard> expectedMap) throws InputException {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        SwengCard actualCard = cardService.getGameCard(game, 2);
        ctrl.verify();
        Assertions.assertEquals(expectedMap.get(2), actualCard);
    }

    @Test
    public void testGameCardName() {
        Assertions.assertEquals("No Name Found", CardServiceImpl.getNameCard(1111, new HashMap<>()));
    }


}
