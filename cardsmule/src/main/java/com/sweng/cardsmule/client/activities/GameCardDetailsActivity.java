package com.sweng.cardsmule.client.activities;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.views.GameCardDetailsView;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.BaseAsyncCallback;

public class GameCardDetailsActivity extends AbstractActivity implements GameCardDetailsView.Presenter{
    private final GameCardDetailsPlace place;
    private final GameCardDetailsView view;
    private final CardServiceAsync cardService;
    private final User user;
    private final PlaceController placeController;
	
    public GameCardDetailsActivity(GameCardDetailsView view, GameCardDetailsPlace place, CardServiceAsync cardService, User user, PlaceController placeController) {
        this.view = view;
        this.place = place;
        this.cardService = cardService;
        this.user = user;
        this.placeController = placeController;
        //user.attach(this);
    }
    
    @Override
    public void start(AcceptsOneWidget containerWidget, EventBus eventBus) {
        view.setPresenter(this);
        containerWidget.setWidget(view.asWidget());
        fetchCard();
    }
    
    
    public void fetchCard() {
        try {
			cardService.getGameCard(place.getGame(), place.getIdCard(), new BaseAsyncCallback<SwengCard>() {
			    @Override
			    public void onSuccess(SwengCard result) {
			        view.setData(result);
			    }
			});
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
}
