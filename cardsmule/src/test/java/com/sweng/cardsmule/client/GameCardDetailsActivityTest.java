package com.sweng.cardsmule.client;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sweng.cardsmule.client.activities.GameCardDetailsActivity;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.CollectionServiceAsync;

import org.easymock.IMocksControl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class GameCardDetailsActivityTest {
	private static final int CARD_ID = 111;
    IMocksControl ctrl;
    GameCardDetailsPlace mockPlace;
    GameCardDetailsView mockView;
    CardServiceAsync mockCardService;
    CollectionServiceAsync mockDeckService;
    PlaceController placeController;
    GameCardDetailsActivity cardActivity;
    
    
}
