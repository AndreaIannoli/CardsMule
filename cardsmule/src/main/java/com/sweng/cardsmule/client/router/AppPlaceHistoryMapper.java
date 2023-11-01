package com.sweng.cardsmule.client.router;
	
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceHistoryMapper;
import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.place.GameCardDetailsPlace;
import com.sweng.cardsmule.client.place.HomePlace;
import com.sweng.cardsmule.client.place.LoginPlace;
import com.sweng.cardsmule.client.place.NewTradePlace;
import com.sweng.cardsmule.client.place.PreAuthenticationPlace;
import com.sweng.cardsmule.client.place.RegistrationPlace;
import com.sweng.cardsmule.client.place.TradePlace;
import com.sweng.cardsmule.client.place.TradesPlace;
import com.sweng.cardsmule.client.place.DecksManagerPlace;
import com.sweng.cardsmule.shared.models.CardsmuleGame;

public class AppPlaceHistoryMapper implements PlaceHistoryMapper, RouteConstants {
    private static final String DELIMITER = "/";
    private final Place defaultPlace = new PreAuthenticationPlace();
    private final User user;

    public AppPlaceHistoryMapper(User user) {
        this.user = user;
    }

    @Override
    public Place getPlace(String token) {
        if (token.isEmpty()) {
            return defaultPlace;
        } else if (token.equals(loginLink) && !user.isLoggedIn()) {
            return new LoginPlace();
        } else if (token.equals(registrationLink) && !user.isLoggedIn()) {
        	return new RegistrationPlace();
        } else if (token.equals(homeLink) && user.isLoggedIn() ) {
        	return new HomePlace();
        } else if(token.equals(decksManagerLink) && user.isLoggedIn()) {
        	return new DecksManagerPlace();
        }else if(token.equals(tradesLink) && user.isLoggedIn()) {
        	return new TradesPlace();
        }
        else {
        	try {
        		String[] parts = token.split(DELIMITER);
        		if (parts[0].equals(cardDetailsLink)) {
                    CardsmuleGame game = CardsmuleGame.valueOf(parts[1].toUpperCase());
                    int cardId = Integer.parseInt(parts[2]);
                    return new GameCardDetailsPlace(cardId, game);
                }else if (parts[0].equals(newTradeLink) && user.isLoggedIn()) {
                    String receiverUserEmail = parts[1];
                    String selectedCardId = parts[2];
                    return new NewTradePlace(selectedCardId, receiverUserEmail);
                } else if (parts[0].equals(tradeLink) && user.isLoggedIn()) {
                    return new TradePlace(Integer.parseInt(parts[1]));
                }
        	} catch (Exception exception) {
        		return defaultPlace;
        	}
        }
        
        return defaultPlace;
    }

    @Override
    public String getToken(Place place) {
        if (place instanceof PreAuthenticationPlace) {
            return preAuthenticationLink;
        } else if (place instanceof LoginPlace) {
            return loginLink;
        } else if (place instanceof RegistrationPlace) {
        	return registrationLink;
        } else if(place instanceof DecksManagerPlace) {
        	return decksManagerLink;
        } else if (place instanceof HomePlace) {
        	return homeLink;
        }else if (place instanceof TradesPlace) {
            return tradesLink;
        } else if (place instanceof TradePlace) {
        	TradePlace tradePlace = (TradePlace) place;
            return tradeLink + DELIMITER + tradePlace.getOfferId();
        } else if (place instanceof NewTradePlace) {
        	NewTradePlace newTradePlace = (NewTradePlace) place;
            return newTradeLink + DELIMITER + newTradePlace.getReceiverUserEmail() + DELIMITER + newTradePlace.getSelectedCardId();
        } else if (place instanceof GameCardDetailsPlace) {
            GameCardDetailsPlace gameCardDetails = (GameCardDetailsPlace) place;
            return cardDetailsLink + DELIMITER + gameCardDetails.getGame().name().toLowerCase() + DELIMITER + gameCardDetails.getIdCard();
        } else {
            return "";
        }
        
    }
}