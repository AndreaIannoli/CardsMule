package com.sweng.cardsmule.client.widgets;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;

import com.google.gwt.uibinder.client.UiField;
import com.sweng.cardsmule.client.views.HomeViewImpl;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.SwengCard;
import com.sweng.cardsmule.shared.models.SwengCardMagic;
import com.sweng.cardsmule.shared.models.SwengPokemonCard;
import com.sweng.cardsmule.shared.models.SwengYuGiOhCard;



public class SwengCardWidget extends Composite {
    private static final CardUIBinder uiBinder = GWT.create(CardUIBinder.class);

	
	DivElement nameDiv;
    @UiField
    DivElement detailsDiv;
    @UiField
    DivElement propertiesDiv;
    @UiField
    Image image;
    @UiField
    PushButton detailsButton;
    
    @UiTemplate("SwengcardWidget.ui.xml")
	interface CardUIBinder extends UiBinder<Widget, SwengCardWidget> {
	}
    
    public  SwengCardWidget(SwengCard card) {
        initWidget(uiBinder.createAndBindUi(this));
        nameDiv.setInnerHTML(card.getName());
        image.setPixelSize(90, 131);

        String details = createDetailHTML("Type", card.getType());
        StringBuilder properties = new StringBuilder();
        String imageUrl = "";
        CardsmuleGame game;

        if (card instanceof SwengYuGiOhCard) {
            imageUrl = ((SwengYuGiOhCard) card).getImageUrl();
            details += createDetailHTML("Race", ((SwengYuGiOhCard) card).getRace());
            game = CardsmuleGame.YUGIOH;
        } else if (card instanceof SwengPokemonCard) {
            imageUrl = ((SwengPokemonCard) card).getImageUrl();
            details += createDetailHTML("Artist", ((SwengPokemonCard) card).getArtist());
            details += createDetailHTML("Rarity", ((SwengPokemonCard) card).getRarity());
            game = CardsmuleGame.POKEMON;
        } else if (card instanceof SwengCardMagic) {
            details += createDetailHTML("Artist", ((SwengCardMagic) card).getArtist());
            details += createDetailHTML("Rarity", ((SwengCardMagic) card).getRarity());
            game = CardsmuleGame.MAGIC;
        } else {
            game = null;
        }

        for (String variant : card.getState()) {
            properties.append("<div>").append(variant).append("</div>");
        }

        image.setUrl(imageUrl);
        detailsDiv.setInnerHTML(details);
        propertiesDiv.setInnerHTML(String.valueOf(properties));
        //detailsButton.addClickHandler(clickEvent -> parent.onOpenDetailsClick(game, card.getId()));
        //image.addErrorHandler((errorEvent) -> image.setUrl(GWT.getHostPageBaseURL() + DefaultImagePathLookupTable.getPath(game)));
    }

    private String createDetailHTML(String detail, String text) {
        return "<div>" +
                "<div style=\"font-weight: bold\">" + detail + ":</div>" +
                text +
                "</div>";
    }

   
	
}