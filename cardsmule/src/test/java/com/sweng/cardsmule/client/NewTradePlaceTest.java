package com.sweng.cardsmule.client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.sweng.cardsmule.client.place.NewTradePlace;

public class NewTradePlaceTest {
	  private String card;
	    private String userEmail;

	    NewTradePlace place;

	    @BeforeEach
	    public void initialize() {
	        card = "cardId";
	        userEmail = "test@test.it";
	        place = new NewTradePlace(card, userEmail);
	    }

	    @Test
	    public void testGetReceiverUserEmail() {
	        Assertions.assertEquals(userEmail, place.getReceiverUserEmail());
	    }

	    @Test
	    public void testGetSelectedCard() {
	        Assertions.assertEquals(card, place.getSelectedCardId());
	    }
}
