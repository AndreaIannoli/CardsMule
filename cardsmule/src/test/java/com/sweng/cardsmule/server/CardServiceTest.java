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


public class CardServiceTest {
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
                Arguments.of(CardsmuleGame.MAGIC, SwengCardMagic.class, DummyData.createMagicDummyMap()),
                Arguments.of(CardsmuleGame.POKEMON, SwengPokemonCard.class, DummyData.createPokemonDummyMap()),
                Arguments.of(CardsmuleGame.YUGIOH, SwengYuGiOhCard.class, DummyData.createYuGiOhDummyMap())
        );
    }

    private static Stream<Arguments> provideMockData() {
        return Stream.of(
                Arguments.of(CardsmuleGame.MAGIC, DummyData.createMagicDummyMap()),
                Arguments.of(CardsmuleGame.POKEMON, DummyData.createPokemonDummyMap()),
                Arguments.of(CardsmuleGame.YUGIOH, DummyData.createYuGiOhDummyMap())
        );
    }

    @ParameterizedTest
    @MethodSource("provideClassAndMockData")
    public void testGetGameCardsListContainsCorrectSubClassesForGameParameter(CardsmuleGame game, Class<?> clazz, Map<Integer, SwengCard> expectedMap) throws InputException {
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
    public void testGetGameCardsForNullParameter() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCards(null));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGetGameCardsExpectedListForGameParameter(CardsmuleGame game, Map<Integer, SwengCard> expectedMap) throws InputException {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        List<SwengCard> cards = cardService.getGameCards(game);
        ctrl.verify();
        Assertions.assertEquals(new ArrayList<>(expectedMap.values()), cards);
    }

    @Test
    public void testGetGameCardForNullGameParameter() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCard(null, 2));
    }

    @Test
    public void testGetGameCardForLessThan0CardIdParameter() {
        Assertions.assertThrows(InputException.class, () -> cardService.getGameCard(CardsmuleGame.MAGIC, -5));
    }

    @ParameterizedTest
    @MethodSource("provideMockData")
    public void testGetGameCardForIdParameter(CardsmuleGame game, Map<Integer, SwengCard> expectedMap) throws InputException {
        expect(mockDB.getCachedMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                .andReturn(expectedMap);
        ctrl.replay();
        SwengCard actualCard = cardService.getGameCard(game, 2);
        ctrl.verify();
        Assertions.assertEquals(expectedMap.get(2), actualCard);
    }

    @Test
    public void testGetNameCard() {
        Assertions.assertEquals("No Name Found", CardServiceImpl.getNameCard(1111, new HashMap<>()));
    }


}
