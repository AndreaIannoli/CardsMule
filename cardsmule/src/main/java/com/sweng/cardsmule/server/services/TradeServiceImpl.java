package com.sweng.cardsmule.server.services;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.server.mapDB.DBImplements;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.shared.models.Offer;
import com.sweng.cardsmule.shared.OfferPayload;
import com.sweng.cardsmule.shared.TradeCardsService;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import org.mapdb.Serializer;
import com.sweng.cardsmule.shared.models.Account;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedList;

//import com.google.common.base.Function;
import com.google.common.reflect.TypeToken;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.GeneralException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.shared.throwables.OfferNotFoundException;

public class TradeServiceImpl extends RemoteServiceServlet implements TradeCardsService, MapDBConst {
	private static final long serialVersionUID = 5868088467963819042L;
	private final MapDB db;
    private final Gson gson = new Gson();
    
    private final Type type = new TypeToken<Map<String, Collection>>() {
    }.getType();

    public TradeServiceImpl() {
        db = new DBImplements();
    }

    public TradeServiceImpl(MapDB mockDB) {
        db = mockDB;
    }
    //metodo per verificare che la mail esista tra gli utenti
    //DA SISTEMARE NEL CASO
    private boolean checkEmailExistence(String email) {
        Map<String, Account > userMap = db.getPersistentMap(
                getServletContext(), MAP_USER , Serializer.STRING, new GsonSerializer<>(gson));
        return userMap.get(email) != null;
    }
    
   
	@Override
	public boolean addOffer(String token, String receiverUserEmail, List<OwnedCard> senderOwnedCards,
			List<OwnedCard> receiverOwnedCards) throws GeneralException {
		String email = AuthenticationServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
		System.out.println(receiverUserEmail + "sono l'email del receiver" + checkEmailExistence(receiverUserEmail));
        if (receiverUserEmail == null || receiverUserEmail.isEmpty())
            throw new InputException("Invalid receiver email");
        if (email.equals(receiverUserEmail))
            throw new InputException("You cannot propose an exchange with yourself.");
        if (senderOwnedCards == null || senderOwnedCards.isEmpty())
            throw new InputException("Invalid sender cards");
        if (receiverOwnedCards == null || receiverOwnedCards.isEmpty())
            throw new InputException("Invalid receiver cards");

        Offer newOffer = new Offer(email, receiverUserEmail, senderOwnedCards, receiverOwnedCards);
        //putifAbsent inserisce la proposta nella mappa se assente essa
        return db.writeOperation(getServletContext(), MAP_PROPOSAL, Serializer.INTEGER, new GsonSerializer<>(gson),
                (Map<Integer, Offer> offerMap) -> offerMap.putIfAbsent(newOffer.getId(), newOffer) == null);
		
	}
	private List<OwnedCardFetched> joinOwnedCardsWithOwnedCardFetched(List<OwnedCard> oCards) {
        List<OwnedCardFetched> cardsWithName = new LinkedList<>();
        for (OwnedCard oCard : oCards) {
            cardsWithName.add(new OwnedCardFetched(oCard, CardServiceImpl.getNameCard(
                    oCard.getReferenceCardId(),
                    db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(oCard.getGameType()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    )
            )));
        }
        return cardsWithName;
    }

	@Override
	public OfferPayload getOffer(String token, int proposalId) throws GeneralException {
		String email = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN , Serializer.STRING, new GsonSerializer<>(gson)));
        if (proposalId < 0)
            throw new InputException("Invalid proposal Id");
        Map<Integer, Offer> offerMap = db.getPersistentMap(getServletContext(), MAP_PROPOSAL , Serializer.INTEGER, new GsonSerializer<>(gson));
        Offer offer = offerMap.get(proposalId);
        if (offer == null)
            throw new OfferNotFoundException("Not existing proposal");
        if (!email.equals(offer.getSenderUserEmail()) && !email.equals(offer.getReceiverUserEmail()))
            throw new AuthenticationException("You can only view proposals linked to your account as sender or receiver");

        return new OfferPayload(offer.getSenderUserEmail(), offer.getReceiverUserEmail(),
        		joinOwnedCardsWithOwnedCardFetched(offer.getSenderOwnedCards()),
        		joinOwnedCardsWithOwnedCardFetched(offer.getReceiverOwnedCards())
        );
	}
	//metodo che utilizzo per eliminare la proposta accettata dall'elenco delle mie proposte in  cui almeno una delle carte fisiche del mittente o del ricevente è presente nella lista di carte
	/*private void deleteReferredOffer(Map<Integer, Offer> offerMap, List<OwnedCard> receiver_cards, List<OwnedCard> sender_cards) {
	    List<OwnedCard> allCards = Stream.concat(receiver_cards.stream(), sender_cards.stream()).collect(Collectors.toList());
	    //allCards.stream().anyMatch per verificare se almeno una carta fisica in allCards è presente sia nella lista proposal.getSenderPhysicalCards() o nella lista proposal.getReceiverPhysicalCards.
	    offerMap.values().removeIf(offer ->
	        allCards.stream()
	            .anyMatch(card -> 
	                offer.getSenderOwnedCards().contains(card) ||
	                offer.getReceiverOwnedCards().contains(card)
	            )
	    );
	}*/
	private void deleteReferredOffer(Map<Integer, Offer> offerMap, List<OwnedCard> receiver_cards, List<OwnedCard> sender_cards) {
        List<OwnedCard> allCards = Stream.concat(receiver_cards.stream(), sender_cards.stream()).collect(Collectors.toList());
        Map<String, OwnedCard> lookUp = allCards.stream().collect(Collectors.toMap(OwnedCard::getId, Function.identity()));

        offerMap.values().removeIf(offer ->
	        offer.getSenderOwnedCards().stream().anyMatch(oCard -> lookUp.containsKey(oCard.getId())) ||
	        offer.getReceiverOwnedCards().stream().anyMatch(oCard -> lookUp.containsKey(oCard.getId()))
        );
    }


	@Override
	public boolean acceptOffer(String token, int offerId)
			throws AuthenticationException, OfferNotFoundException {
		String email = AuthenticationServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
		System.out.println(email + "di colui che ha ricevuto l'offerta");
		//mappa delle offerte
		Map<Integer, Offer> offerMap = db.getPersistentMap(getServletContext(), MAP_PROPOSAL, Serializer.INTEGER, new GsonSerializer<>(gson));
        //offerta specifica della mappa
		Offer offer = offerMap.get(offerId);
		System.out.println("offer" + offer);
        if (offer == null)
            throw new OfferNotFoundException("Not existing proposal");
        if (!email.equals(offer.getReceiverUserEmail()))
            throw new AuthenticationException("You can only accept proposals made to you.");

        return db.writeOperation(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type),
                (Map<String, Map<String, Collection>> collectionMap) -> {
                	try {
                		System.out.println(offer.getReceiverUserEmail()+ " " + offer.getReceiverOwnedCards());
                		collectionMap.put(offer.getReceiverUserEmail(), CollectionServiceImpl.updateUserCollection(collectionMap.get(offer.getReceiverUserEmail()), offer.getSenderOwnedCards(), offer.getReceiverOwnedCards()));
                    	System.out.println(" ssss");
                    	collectionMap.put(offer.getSenderUserEmail(), CollectionServiceImpl.updateUserCollection(collectionMap.get(offer.getSenderUserEmail()), offer.getReceiverOwnedCards(), offer.getSenderOwnedCards()));
                        System.out.println("put figlio di puttana");
                        return true;
                	}catch (Exception e) {
						System.out.println(e.toString());
						System.out.println(e.getMessage());
						return false;
					}
                	
                	//deleteReferredOffer(offerMap, offer.getSenderOwnedCards(), offer.getReceiverOwnedCards());
                    
                }) != null;
		
	}
	//offerte lista ricevute
	@Override
	public List<Offer> getOfferListReceived(String token) throws GeneralException {
		 String email = AuthenticationServiceImpl.checkTokenValidity(token,
	                db.getPersistentMap(getServletContext(), MAP_LOGIN , Serializer.STRING, new GsonSerializer<>(gson)));
	        Map<Integer, Offer> offerMap = db.getPersistentMap(getServletContext(),MAP_PROPOSAL , Serializer.INTEGER, new GsonSerializer<>(gson));
	        List<Offer> offerList = new ArrayList<>();
	        //ciclo sulle offerte se l'email dell'utente autenticato corrisponde all'email del destinatario della offerta  l'offerta viene aggiunta alla lista
	        for (Offer item : offerMap.values())
	            if (email.equals(item.getReceiverUserEmail()))
	                offerList.add(item);
	        return offerList;
	}
	//ottenere offerte inviate
	@Override
	public List<Offer> getOfferListSent(String token) throws GeneralException {
		String email = AuthenticationServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(),MAP_LOGIN , Serializer.STRING, new GsonSerializer<>(gson)));
        Map<Integer, Offer> offerMap = db.getPersistentMap(getServletContext(), MAP_PROPOSAL, Serializer.INTEGER, new GsonSerializer<>(gson));
        List<Offer> offerList = new ArrayList<>();
        for (Offer item : offerMap.values())
            if (email.equals(item.getSenderUserEmail()))
                offerList.add(item);
        return offerList;
		
	}
	///metodo per rifiutare o ritirare l'offerta o la proposta
	@Override
	public boolean refuseOrWithdrawOffer(String token, int offerId) throws GeneralException {
		 String email = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN , Serializer.STRING, new GsonSerializer<>(gson)));
	        if (offerId < 0)
	            throw new InputException("Invalid proposal Id");
	        Map<Integer, Offer> offerMap = db.getPersistentMap(getServletContext(),MAP_PROPOSAL , Serializer.INTEGER, new GsonSerializer<>(gson));
	        Offer offer = offerMap.get(offerId);
	        if (offer == null)
	            throw new OfferNotFoundException("Not existing proposal");
	        if (!email.equals(offer.getSenderUserEmail()) && !email.equals(offer.getReceiverUserEmail()))
	            throw new AuthenticationException("You can only interact with proposals of which you are the sender or the receiver");
	        return db.writeOperation(getServletContext(), () -> offerMap.remove(offerId, offer));
	}
    
    
}
