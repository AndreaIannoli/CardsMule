package com.sweng.cardsmule.client.handlers;

import com.sweng.cardsmule.shared.models.CardsmuleGame;

public interface HandleCard {
	void onCardDetailsOpen(int idCard, CardsmuleGame game);
}
