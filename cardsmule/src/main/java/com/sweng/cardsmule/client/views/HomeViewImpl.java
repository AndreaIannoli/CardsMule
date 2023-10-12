package com.sweng.cardsmule.client.views;

import java.util.Map;



import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.sweng.cardsmule.client.widgets.NavWidget;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sweng.cardsmule.client.SuccessAsyncCallBack;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.handlers.HandleCard;
import com.sweng.cardsmule.client.handlers.HandleNavBar;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.widgets.NavWidget;
import com.sweng.cardsmule.shared.AuthenticationService;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.CredentialsPayload;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.client.widgets.SwengCardWidget;

import java.util.List;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;


import com.sweng.cardsmule.client.views.HomeView;

public class HomeViewImpl extends Composite implements HomeView, HandleCard, HandleNavBar{
    private static final HomeViewImplUIBinder uiBinder = GWT.create(HomeViewImplUIBinder.class);

    Presenter presenter;
    @UiField
    TextBox SearchBar;
    @UiField
    RadioButton magicRadio;
    @UiField
    RadioButton pokemonRadio;
    @UiField
    RadioButton YuGiOhRadio;
    @UiField
    HTMLPanel checkboxesPanel;
    @UiField
    HTMLPanel cardsPanel;
    @UiField
    public ListBox specialAttributeOptions;
    @UiField
    public ListBox artistOrName;
    @UiField
    SpanElement attributeSpan ;
    @UiField
    public ListBox typeOptions;
    
    public List<CheckBox> checkBoxes;
    @UiField
    Button applyFiltersButton;
    @UiField
    Button cleanFiltersButton;
    

    
    private final NavWidget navbarWidget = new NavWidget(this);
    Map<CardsmuleGame,List<String>> attributeList;
    Map<CardsmuleGame, List<String>> gameCharacters;
    private boolean isGameChanged;
    
	@UiTemplate("HomeViewImpl.ui.xml")
	interface HomeViewImplUIBinder extends UiBinder<Widget, HomeViewImpl> {
	}
	
    public HomeViewImpl() {
    	
		initWidget(uiBinder.createAndBindUi(this));
    	magicRadio.addValueChangeHandler(e->isGameChanged(CardsmuleGame.MAGIC));
    	pokemonRadio.addValueChangeHandler(e->isGameChanged(CardsmuleGame.POKEMON));
    	YuGiOhRadio.addValueChangeHandler(e->isGameChanged(CardsmuleGame.YUGIOH));
        cleanFiltersButton.addClickHandler(e -> cleanFilters());
        applyFiltersButton.addClickHandler(e -> applyFilters());


    	attributeList = new HashMap<>();
    	attributeList.put(CardsmuleGame.MAGIC, Arrays.asList("Name","Artist"));
    	attributeList.put(CardsmuleGame.POKEMON, Arrays.asList("Name","Artist"));
    	attributeList.put(CardsmuleGame.YUGIOH,Collections.singletonList("Name"));
    	
    	gameCharacters = new HashMap<>();
    	gameCharacters.put(CardsmuleGame.MAGIC, Arrays.asList("hasFoil", "isAlternative", "isFullArt", "isPromo", "isReprint"));
    	gameCharacters.put(CardsmuleGame.POKEMON, Arrays.asList("firstEdition", "holo", "normal", "reverse", "wPromo"));
    	gameCharacters.put(CardsmuleGame.YUGIOH, Collections.emptyList());
    }
    
    public void changeGame(CardsmuleGame game) {
    	attributeSpan.setInnerHTML((game == CardsmuleGame.MAGIC || game == CardsmuleGame.POKEMON) ? "Rarity" : "Race");
    	cardsPanel.clear();
    	artistOrName.clear();
    	checkboxesPanel.clear();
    	checkBoxes = new ArrayList<>();
    	for (String textField : attributeList.get(game))
    		artistOrName.addItem(textField);
    	for (String booleanField : gameCharacters.get(game)) {
            CheckBox checkBox = new CheckBox(booleanField);
            checkboxesPanel.add(checkBox);
            checkBoxes.add(checkBox);
        }
    }

    @Override
    public void displayAlert(String message) {
        Window.alert(message);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        RootPanel root2= RootPanel.get("nav");
    	navbarWidget.setStyleName("nav");
    	root2.add(navbarWidget);
    }
    
    
    @Override
    public void resetFields() {
        SearchBar.setText("");
        pokemonRadio.setChecked(false);
        magicRadio.setChecked(false);
        YuGiOhRadio.setChecked(false);
    }

	@Override
	public void setAttributesAndtypes(List<SwengCard> cards) {
		cardsPanel.clear();
		Set<String> specialAttributes = new HashSet<>();
        Set<String> types = new HashSet<>();
        cards.forEach(card -> {
        	types.add(card.getType());
            if (card instanceof SwengCardMagic) {
            	specialAttributes.add(((SwengCardMagic) card).getRarity());
            } else if (card instanceof SwengPokemonCard) {
            	specialAttributes.add(((SwengPokemonCard) card).getRarity());
            } else if (card instanceof SwengYuGiOhCard) {
            	specialAttributes.add(((SwengYuGiOhCard) card).getRace());
            }
        });

        cards.forEach(card -> {
            cardsPanel.add(new SwengCardWidget(this, card));
        });
        if (!isGameChanged) {
            setFilters(specialAttributes, types);
            isGameChanged = true;
        }
	}
	
	public void setFilters(Set<String> specialAttributes , Set<String> types) {
		typeOptions.clear();
		specialAttributeOptions.clear();
		typeOptions.addItem("all");
		specialAttributeOptions.addItem("all");
		specialAttributes.forEach(specialAttribute -> specialAttributeOptions.addItem(specialAttribute));
		types.forEach(type -> typeOptions.addItem(type));
	}
	
	private void cleanFilters() {
		 specialAttributeOptions.setItemSelected(0, true);
		 typeOptions.setItemSelected(0, true);
		
		 artistOrName.setItemSelected(0, true);
		
		 SearchBar.setText("");
		
		 checkBoxes.forEach(checkBox -> checkBox.setValue(false));
		
		 setAttributesAndtypes(presenter.filteredCards(
			 specialAttributeOptions.getSelectedValue(),
			 typeOptions.getSelectedValue(),
			 artistOrName.getSelectedValue(),
			 SearchBar.getText(),
			 Collections.emptyList(),
			 Collections.emptyList()
		 ));
	}
	
	private void applyFilters() {
        List<String> booleanInputNames = new ArrayList<>();
        List<Boolean> booleanInputValues = new ArrayList<>();
        for (CheckBox checkBox : checkBoxes) {
            booleanInputNames.add(checkBox.getText());
            booleanInputValues.add(checkBox.getValue());
        }
        setAttributesAndtypes(presenter.filteredCards(
                specialAttributeOptions.getSelectedValue(),
                typeOptions.getSelectedValue(),
                artistOrName.getSelectedValue(),
                SearchBar.getText(),
                booleanInputNames,
                booleanInputValues
        ));
	}
	
	private void isGameChanged(CardsmuleGame game) {
		changeGame(game);
        isGameChanged = false;
        presenter.fetchCardsValues(game);
	}

	@Override
	public void onCardDetailsOpen(int idCard, CardsmuleGame game) {
		presenter.goTo(new GameCardDetailsPlace(idCard, game));
	}
	
	
	
	@Override
	public void onClickLogout() {
		
		
		presenter.goTo(new PreAuthenticationPlace());	
	}



	
}