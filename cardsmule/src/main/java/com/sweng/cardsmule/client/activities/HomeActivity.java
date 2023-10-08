package com.sweng.cardsmule.client.activities;


import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.shared.models.*;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.BaseAsyncCallback;
import com.sweng.cardsmule.client.views.HomeView;
import com.sweng.cardsmule.shared.CardServiceAsync;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import java.util.List;
import java.util.stream.*;


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
    //Da controllare
	public void fetchCardsValues(CardsmuleGame game)  {
		if(game == null) {
    		throw new IllegalArgumentException("game cannot be null");
    	}
		try {
			rpcService.getGameCards(game,new BaseAsyncCallback<List<SwengCard>>(){

				@Override
				public void onSuccess(List<SwengCard> result) {
					view.setAttributesAndtypes(result);
					card = result;
					System.out.println(card);
				}
				
			});
		} catch (InputException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

			
			
		
	}
	
	
	
	
	
	
	
	
	
	
	
	

	/*public List<SwengCard> filteredCards(String selectedValue, String value, String textOptionsSelectedValue, String text, List<String> booleanInputNames, List<Boolean> booleanInputValues) {
		return card.stream()
		.filter(card -> {
		if (!textInputValue.isEmpty()) {
		  String cardText = "";
		  switch (textInputName) {
		      case "Name":
		          cardText = card.getName();
		          break;
		      case "Description":
		          cardText = card.getDescription();
		          break;
		      case "Artist":
		          if (card instanceof MagicCard) {
		              cardText = ((MagicCard) card).getArtist();
		          } else if (card instanceof PokemonCard) {
		              cardText = ((PokemonCard) card).getArtist();
		          }
		          break;
		  }
		  if (!cardText.toLowerCase().contains(textInputValue.toLowerCase())) {
		      return false;
		  }
		}
		if (!specialAttributeValue.equals("all")) {
		  if ((card instanceof MagicCard && !specialAttributeValue.equals(((MagicCard) card).getRarity())) ||
		          (card instanceof PokemonCard && !specialAttributeValue.equals(((PokemonCard) card).getRarity())) ||
		          (card instanceof YuGiOhCard && !specialAttributeValue.equals(((YuGiOhCard) card).getRace()))) {
		      return false;
		  }
		}
		if (!typeValue.equals("all") && !typeValue.equals(card.getType())) {
		  return false;
		}
		if (!(booleanInputNames.isEmpty() && booleanInputValues.isEmpty())) {
		  for (int i = 0; i < booleanInputNames.size(); i++) {
		      String name = booleanInputNames.get(i);
		      Boolean value = booleanInputValues.get(i);
		      if (value && !card.getVariants().contains(name)) {
		          return false;
		      }
		  }
		}
		return true;
		})
		.collect(Collectors.toList());
		}*/

}