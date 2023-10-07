package com.sweng.cardsmule.client.activities;


import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.shared.models.*;
import com.sweng.cardsmule.client.BaseAsyncCallback;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.List;

public class HomeActivity extends AbstractActivity implements HomeView.Presenter {
    private final CardServiceAsync rpcService;
    private final HomeView view;
    private final User user;
    private final PlaceController placeController;
    private List<SwengCard> card;
    


    public HomeActivity(HomeView view,User user,PlaceController placeController, CardServiceAsync rpcService) {
        this.view = view;
        this.user = user;
        this.placeController = placeController;
        this.rpcService=rpcService;
        
    }

    @Override
    public void start(AcceptsOneWidget acceptsOneWidget, EventBus eventBus) {
        view.setPresenter(this);
        acceptsOneWidget.setWidget(view.asWidget());
 
    }
    

    @Override
    public void onStop() {
        
    }

    public void goTo(Place place) {
        placeController.goTo(place);
    }

	public void fetchCardsValues(CardsmuleGame game) {
		if(game == null) {
    		throw new IllegalArgumentException("game cannot be null");
    	}
    	rpcService.getGameCards(game, new BaseAsyncCallback<List<SwengCard>>() {
            @Override
            public void onSuccess(List<SwengCard> result) {
            	//All'interno del callback, nell'implementazione del metodo onSuccess(List<Card> result), 
            	//i risultati ottenuti dalla chiamata al servizio vengono passati alla vista (view.setData(result)) 
            	//per aggiornare la visualizzazione con le nuove carte ottenute.
                view.setAttributesAndtypes(result);
                //La linea cards = result; assegna la lista di carte ottenute dalla chiamata al servizio remoto 
                //(contenute nella variabile result) alla variabile cards. Questa assegnazione sovrascrive il valore 
                //precedente di cards con la nuova lista di carte ottenute dalla chiamata al servizio
                card = result;
            }
        });
		
	}
}