package com.sweng.cardsmule.client.views;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.CardsImages;
import com.sweng.cardsmule.client.handlers.HandleAddCardToCollection;
import com.sweng.cardsmule.client.widgets.UsersListWidget;

import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.client.widgets.AddCardToCollectionWidget;
import com.sweng.cardsmule.client.widgets.AddCardToDeckWidget;
import com.google.gwt.user.client.ui.DialogBox;
import com.sweng.cardsmule.client.handlers.HandleAddCardToDeck;



public class GameCardDetailsViewImpl extends Composite implements GameCardDetailsView, HandleAddCardToCollection,HandleAddCardToDeck{
    private static final GameCardDetailsImplUIBinder uiBinder = GWT.create(GameCardDetailsImplUIBinder.class);
    @UiField
    SpanElement cardGame;
    @UiField
    HeadingElement cardName;
    @UiField
    Image cardImage;
    @UiField
    DivElement cardDescription;
    @UiField
    DivElement cardOptionType;
    @UiField
    DivElement optionArtist;
    @UiField
    DivElement cardOptionArtist;
    @UiField
    DivElement optionRarity;
    @UiField
    DivElement cardOptionRarity;
    @UiField
    DivElement optionRace;
    @UiField
    DivElement cardOptionRace;
    @UiField
    DivElement optionOtherProperties;
    @UiField
    HTMLPanel addCardToDeckContainer;
    @UiField
    HTMLPanel userLists;
    @UiField
    Button updateBtn;
    Presenter presenter;
    
    UsersListWidget ownedList;
    UsersListWidget wishedList;
    AddCardToCollectionWidget addCardToCollectionWidget= new AddCardToCollectionWidget(this);
    DialogBox dialog = new AddCardToDeckWidget(this);
    
    
    interface GameCardDetailsImplUIBinder extends UiBinder<Widget, GameCardDetailsViewImpl> {
    }
    
    public GameCardDetailsViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
        updateBtn.addClickHandler(clickEvent -> presenter.update() );
    }
    
    public void setData(SwengCard data) {
        CardsmuleGame game;
        String imageUrl = "";
        String artist = "";
        String rarity = "";
        String race = "";
        StringBuilder otherProperties = new StringBuilder();

        if (data instanceof SwengCardMagic) {
            game = CardsmuleGame.MAGIC;
            artist = ((SwengCardMagic) data).getArtist();
            rarity = ((SwengCardMagic) data).getRarity();
        } else if (data instanceof SwengPokemonCard) {
            game = CardsmuleGame.POKEMON;
            imageUrl = ((SwengPokemonCard) data).getImageUrl();
            artist = ((SwengPokemonCard) data).getArtist();
            rarity = ((SwengPokemonCard) data).getRarity();
        } else if (data instanceof SwengYuGiOhCard) {
            game = CardsmuleGame.YUGIOH;
            imageUrl = ((SwengYuGiOhCard) data).getImageUrl();
            race = ((SwengYuGiOhCard) data).getRace();
        } else {
            game = null;
        }

        for (String variant : data.getState()) {
            otherProperties.append("<div>").append(variant).append("</div>");
        }

        cardImage.addErrorHandler((errorEvent) -> cardImage.setUrl(GWT.getHostPageBaseURL() + CardsImages.getPath(game)));
        cardGame.setInnerHTML(game != null ? game.name() : "");
        cardName.setInnerHTML(data.getName());
        cardImage.setUrl(imageUrl);
        cardDescription.setInnerHTML(data.getDescription());
        cardOptionType.setInnerHTML(data.getType());
        cardOptionArtist.setInnerHTML(artist);
        cardOptionRarity.setInnerHTML(rarity);
        cardOptionRace.setInnerHTML(race);
        removeUnusedProp();
        if (!artist.isEmpty()) optionArtist.getStyle().clearDisplay();
        if (!rarity.isEmpty()) optionRarity.getStyle().clearDisplay();
        if (!race.isEmpty()) optionRace.getStyle().clearDisplay();
        optionOtherProperties.setInnerHTML(String.valueOf(otherProperties));
        
    }
    
    private void removeUnusedProp() {
        optionRarity.setAttribute("style", "display: none");
        optionRace.setAttribute("style", "display: none");
        optionArtist.setAttribute("style", "display: none");
	}

	@Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
    
    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

	@Override
	public void setWishList(List<WishedCard> wisherList) {
        wishedList.setTable(wisherList, own -> new Button("Exchange", (ClickHandler) e ->
        displayAlert("GoToExchange")));
	}

	@Override
	public void setOwnList(List<OwnedCard> ownerList) {
        ownedList.setTable(ownerList, own -> new Button("Exchange", (ClickHandler) e ->
        displayAlert("GoToExchange")));
	}
	@Override
    public void onClickAddToDeck() {
        dialog.center();
        dialog.setModal(true);
        if (addCardToCollectionWidget.getDeckName().equals("Owned")) {
            dialog.setText("Do you own this card?");
        } else if (addCardToCollectionWidget.getDeckName().equals("Wished")) {
            dialog.setText("Do you wish this card?");
        } else {
            dialog.setText("YOU MUST SELECT A DECK!");
        }
        dialog.show();
    }
	
    @Override
    public void createUserWidgets(boolean isLoggedIn) {
    	addCardToDeckContainer.clear();
        userLists.clear();
        // create AddCartToDeckWidget
        if (isLoggedIn) {
        	addCardToDeckContainer.add(addCardToCollectionWidget);
        }
        // create UserListWidget 'Exchange' buttons
        ownedList = new UsersListWidget("Owned by", isLoggedIn);
        wishedList = new UsersListWidget("Wished by", isLoggedIn);
        userLists.add(ownedList);
        userLists.add(wishedList);
    }

	@Override
	public void onClickModalYes(String grade, String description) {
		presenter.addCardToDeck(addCardToCollectionWidget.getDeckName(), grade, description);
		
	}
	@Override
    public void hideModal() {
        dialog.hide();
    }

	@Override
	public String getDeckSelected() {
		// TODO Auto-generated method stub
		return addCardToCollectionWidget.getDeckName();
	}
}
