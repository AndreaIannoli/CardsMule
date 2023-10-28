package com.sweng.cardsmule.client;

import java.util.Arrays;
import java.util.List;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.shared.throwables.InputException;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.place.shared.PlaceController;
import com.sweng.cardsmule.client.activities.HomeActivity;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.server.DummyData;
import com.sweng.cardsmule.shared.CardServiceAsync;
import java.util.*;
import java.util.stream.Stream;

import static org.easymock.EasyMock.*;


public class HomeActivityTest {
	IMocksControl ctrl;
    HomeView mockView;
    CardServiceAsync mockRpcService;
    PlaceController placeController;
    HomeActivity homeActivity;
    User user;
    private final static List<String> MAGIC_FIELDS = Arrays.asList("hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint");
    private final static List<String> POKEMON_FIELDS = Arrays.asList("firstEdition", "holo", "normal", "reverse", "wPromo");

    @BeforeEach
    public void initialize() {
        ctrl = createStrictControl();
        mockView = ctrl.createMock(HomeView.class);
        mockRpcService = ctrl.createMock(CardServiceAsync.class);
        placeController = ctrl.createMock(PlaceController.class);
        homeActivity = new HomeActivity(mockView, user,placeController, mockRpcService);
    }

    @Test
    public void testForGoTo() {
        placeController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        homeActivity.goTo(new GameCardDetailsPlace(1, null));
        ctrl.verify();
    }

    

    @Test
    public void testStopForCacheCardList() {
        homeActivity.onStop();
    }

    @Test
    public void testFetchGameCardsForNullParameter() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> homeActivity.fetchCardsValues(null));
    }

    @SuppressWarnings("unchecked")
    //aggiunto un throw
    public void setupFetchGameCardsTest(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
        mockRpcService.getGameCards(eq(game), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<List<SwengCard>> callback = (AsyncCallback<List<SwengCard>>) args[args.length - 1];
            callback.onSuccess(mockCards);
            return null;
        });
        mockView.setAttributesAndtypes(mockCards);
        expectLastCall();
        ctrl.replay();
        homeActivity.fetchCardsValues(game);
        ctrl.verify();
    }

    private static Stream<Arguments> provideMockCards() {
        return Stream.of(
                Arguments.of(CardsmuleGame.MAGIC, DummyData.createMagicDummyList()),
                Arguments.of(CardsmuleGame.POKEMON, DummyData.createPokemonDummyList()),
                Arguments.of(CardsmuleGame.YUGIOH, DummyData.createYuGiOhDummyList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFetchGameCardsExpectedListForGameParameter(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
        setupFetchGameCardsTest(game, mockCards);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForAllParameters(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertArrayEquals(homeActivity.filteredCards("all", "all",
                "Name", "",
                Collections.emptyList(), Collections.emptyList()).toArray(), mockCards.toArray());
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForNameParameter(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
        SwengCard expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards("all", "all",
                "Name", expectedCard.getName(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }


    private static Stream<Arguments> provideMockCardsAndBooleanFields() {
        Map<Integer, SwengCardMagic> MagicDummy = DummyData.createMagicDummyMap();
        Set<Integer> magicKeys = MagicDummy.keySet();
        Integer[] magicKeysArray = magicKeys.toArray(new Integer[0]);
        List<SwengCard> MagicDummyList = new ArrayList<>(MagicDummy.values());
        return Stream.of(
                Arguments.of(
                		CardsmuleGame.MAGIC, MagicDummyList, MagicDummy.get(magicKeysArray[0]),
                        MAGIC_FIELDS, Arrays.asList(true, false, false, false, false)),
                Arguments.of(
                		CardsmuleGame.MAGIC, MagicDummyList, MagicDummy.get(magicKeysArray[1]),
                        MAGIC_FIELDS, Arrays.asList(false, true, false, false, false)),
                Arguments.of(
                		CardsmuleGame.MAGIC, MagicDummyList, MagicDummy.get(magicKeysArray[2]),
                        MAGIC_FIELDS, Arrays.asList(false, false, true, false, false)),
                Arguments.of(
                		CardsmuleGame.MAGIC, MagicDummyList, MagicDummy.get(magicKeysArray[3]),
                        MAGIC_FIELDS, Arrays.asList(false, false, false, true, false)),
                Arguments.of(
                		CardsmuleGame.MAGIC, MagicDummyList, MagicDummy.get(magicKeysArray[4]),
                        MAGIC_FIELDS, Arrays.asList(false, false, false, false, true)),
                Arguments.of(
                		CardsmuleGame.POKEMON, DummyData.createPokemonDummyList(), DummyData.createPokemonDummyList().get(0),
                        POKEMON_FIELDS, Arrays.asList(true, false, false, false, false)),
                Arguments.of(
                		CardsmuleGame.POKEMON, DummyData.createPokemonDummyList(), DummyData.createPokemonDummyList().get(1),
                        POKEMON_FIELDS, Arrays.asList(false, true, false, false, false)),
                Arguments.of(
                		CardsmuleGame.POKEMON, DummyData.createPokemonDummyList(), DummyData.createPokemonDummyList().get(2),
                        POKEMON_FIELDS, Arrays.asList(false, false, true, false, false)),
                Arguments.of(
                		CardsmuleGame.POKEMON, DummyData.createPokemonDummyList(), DummyData.createPokemonDummyList().get(3),
                        POKEMON_FIELDS, Arrays.asList(false, false, false, true, false)),
                Arguments.of(
                		CardsmuleGame.POKEMON, DummyData.createPokemonDummyList(), DummyData.createPokemonDummyList().get(4),
                        POKEMON_FIELDS, Arrays.asList(false, false, false, false, true)),
                Arguments.of(
                		CardsmuleGame.YUGIOH, DummyData.createYuGiOhDummyList(), DummyData.createYuGiOhDummyList().get(0),
                        Collections.emptyList(), Collections.emptyList())
        );
    }

    @Test
    public void testFilterGameCardsFor_Magic_and_Artist_Parameters() throws InputException {
        List<SwengCard> mockCards = DummyData.createMagicDummyList();
        SwengCard expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(CardsmuleGame.MAGIC, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards("all", "all",
                "Artist", ((SwengCardMagic) expectedCard).getArtist(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForSpecialAttributeParameter(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
    	SwengCard expectedCard = mockCards.get(mockCards.size() - 1);
        String specialAttribute = expectedCard instanceof SwengCardMagic ?
                ((SwengCardMagic) expectedCard).getRarity() : expectedCard instanceof SwengPokemonCard ?
                ((SwengPokemonCard) expectedCard).getRarity() : ((SwengYuGiOhCard) expectedCard).getRace();

        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards(specialAttribute, "all",
                "Description", "",
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCards")
    public void testFilterGameCardsForTypeParameter(CardsmuleGame game, List<SwengCard> mockCards) throws InputException {
    	SwengCard expectedCard = mockCards.get(mockCards.size() - 1);
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards("all", expectedCard.getType(),
                "Description", "",
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @Test
    public void testFilterGameCardsFor_Pokemon_and_Artist_Parameters() throws InputException {
        List<SwengCard> mockCards = DummyData.createPokemonDummyList();
        SwengCard expectedCard = mockCards.get(0);
        setupFetchGameCardsTest(CardsmuleGame.POKEMON, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards("all", "all",
                "Artist", ((SwengPokemonCard) expectedCard).getArtist(),
                Collections.emptyList(), Collections.emptyList()).get(0), expectedCard);
    }

    @ParameterizedTest
    @MethodSource("provideMockCardsAndBooleanFields")
    public void testFilterGameCardsForBooleanParameters(CardsmuleGame game, List<SwengCard> mockCards, SwengCard expectedCard,
                                                        List<String> booleanNames, List<Boolean> booleanValues) throws InputException {
        setupFetchGameCardsTest(game, mockCards);
        Assertions.assertEquals(homeActivity.filteredCards("all", "all",
                "Name", "",
                booleanNames, booleanValues).get(0), expectedCard);
    }
}
