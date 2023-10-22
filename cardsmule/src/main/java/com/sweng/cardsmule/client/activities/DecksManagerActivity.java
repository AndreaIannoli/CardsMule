package com.sweng.cardsmule.client.activities;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.views.DecksManagerView;
import com.sweng.cardsmule.shared.CollectionServiceAsync;
import com.sweng.cardsmule.shared.CollectionVariationPayload;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.throwables.AlreadyExistingOfferException;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.CollectionNotFoundException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.client.BaseAsyncCallback;

public class DecksManagerActivity extends AbstractActivity implements DecksManagerView.Presenter {
    private final CollectionServiceAsync rpcService;
    private final User user;
    private final DecksManagerView view;
    private final PlaceController placeController;
    
    public DecksManagerActivity(CollectionServiceAsync rpcService, DecksManagerView view, User user, PlaceController placeController) {
    	this.rpcService = rpcService;
        this.view = view;
        this.user = user;
        this.placeController = placeController;
    }

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
        view.setPresenter(this);
        panel.setWidget(view.asWidget());
        fetchUserDeckNames();
	}
	
    @Override
    public void onStop() {
        view.resetData();
    }

	@Override
	public void createDeck(String deckName) {
		if (checkDeckNameValidity(deckName)) {
            view.displayAlert("Deck name is mandatory");
            return;
        }

        rpcService.addCollection(user.getToken(), deckName, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.displayAlert(((AuthenticationException) caught).getMessage());
                } else {
                    view.displayAlert("Server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                if (result) {
                    view.displayAddedDeck(deckName);
                } else {
                    view.displayAlert("This deck name is already in use");
                }
            }
        });
	}

	@Override
	public void deleteDeck(String deckName, Consumer<Boolean> isRemoved) {
		if (checkDeckNameValidity(deckName)) {
            view.displayAlert("Deck name is mandatory");
            return;
        }

        rpcService.removeDeck(user.getToken(), deckName, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.displayAlert(((AuthenticationException) caught).getMessage());
                } else {
                    view.displayAlert("Server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(Boolean result) {
                isRemoved.accept(result);
            }
        });
	}

	@Override
	public void removeOwnedCardFromDeck(String deckName, OwnedCard ownedCard) {
		if (checkDeckNameValidity(deckName)) {
            view.displayAlert("Deck name is mandatory");
            return;
        }
        if (ownedCard == null) {
            view.displayAlert("Invalid owned card, is null");
            return;
        }
        rpcService.removeOwnedCardFromCollection(user.getToken(), deckName, ownedCard, new AsyncCallback<List<CollectionVariationPayload>>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.displayAlert(((AuthenticationException) caught).getMessage());
                } else if (caught instanceof InputException) {
                    view.displayAlert(((InputException) caught).getMessage());
                } else if (caught instanceof CollectionNotFoundException) {
                    view.displayAlert(((CollectionNotFoundException) caught).getMessage());
                } else if (caught instanceof AlreadyExistingOfferException) {
                    view.displayAlert(((AlreadyExistingOfferException) caught).getMessage());
                } else {
                    view.displayAlert("Server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(List<CollectionVariationPayload> result) {
                view.replaceData(result);
            }
        });
	}

	@Override
	public void addOwnedCardsToDeck(String deckName, List<OwnedCard> ownedCards,
			Consumer<List<OwnedCardFetched>> updateCustomDeck) {
		if (checkDeckNameValidity(deckName)) {
			view.displayAlert("Deck name is mandatory");
            return;
        }
        if (ownedCards.isEmpty()) {
            view.displayAlert("Owned cards list is empty");
            return;
        }

        rpcService.addOwnedCardsToDeck(user.getToken(), deckName, ownedCards, new AsyncCallback<List<OwnedCardFetched>>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.displayAlert(((AuthenticationException) caught).getMessage());
                } else if (caught instanceof InputException) {
                    view.displayAlert(((InputException) caught).getMessage());
                } else if (caught instanceof CollectionNotFoundException) {
                    view.displayAlert(((CollectionNotFoundException) caught).getMessage());
                } else {
                    view.displayAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(List<OwnedCardFetched> result) {
                updateCustomDeck.accept(result);
            }
        });
		
	}
	
	private boolean checkDeckNameValidity(String deckName) {
        return deckName == null || deckName.isEmpty();
    }
	
    private void fetchUserDeckNames() {
        rpcService.getUserCollectionNames(user.getToken(), new BaseAsyncCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                view.setData(result);
            }
        });
    }
    
	@Override
	public void fetchUserDeck(String deckName, BiConsumer<List<OwnedCardFetched>, String> setDeckData) {
		rpcService.getDeck(user.getToken(), deckName, new BaseAsyncCallback<List<OwnedCardFetched>>() {
            @Override
            public void onSuccess(List<OwnedCardFetched> result) {
                setDeckData.accept(result, null);
            }
        });
	}

	@Override
	public void updateOwnedCard(String deckName, OwnedCard editedOwnedCard) {
		if (checkDeckNameValidity(deckName)) {
            view.displayAlert("Invalid deck name");
            return;
        }
        if (!deckName.equals("Owned") && !deckName.equals("Wished")) {
            view.displayAlert("Sorry, you can only edit physical cards in Default decks.");
            return;
        }
        if (editedOwnedCard == null) {
            view.displayAlert("Invalid physical card");
            return;
        }
        rpcService.editOwnedCard(user.getToken(), deckName, editedOwnedCard, new AsyncCallback<List<CollectionVariationPayload>>() {
            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof AuthenticationException) {
                    view.displayAlert(((AuthenticationException) caught).getMessage());
                } else if (caught instanceof InputException) {
                    view.displayAlert(((InputException) caught).getMessage());
                } else if (caught instanceof AlreadyExistingOfferException) {
                    view.displayAlert(((AlreadyExistingOfferException) caught).getMessage());
                } else {
                    view.displayAlert("Internal server error: " + caught.getMessage());
                }
            }

            @Override
            public void onSuccess(List<CollectionVariationPayload> result) {
                view.replaceData(result);
            }
        });
	}

	@Override
    public void goTo(Place place) {
        placeController.goTo(place);
    }
    
}
