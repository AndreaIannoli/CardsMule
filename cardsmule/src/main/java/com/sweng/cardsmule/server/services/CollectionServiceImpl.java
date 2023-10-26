package com.sweng.cardsmule.server.services;

import static com.sweng.cardsmule.server.services.AuthenticationServiceImpl.validateEmail;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapdb.Serializer;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sweng.cardsmule.server.mapDB.DBImplements;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.shared.CollectionClassificationConstants;
import com.sweng.cardsmule.shared.CollectionService;
import com.sweng.cardsmule.shared.CollectionVariationPayload;
import com.sweng.cardsmule.shared.models.CardsmuleGame;
import com.sweng.cardsmule.shared.models.Collection;
import com.sweng.cardsmule.shared.models.Grade;
import com.sweng.cardsmule.shared.models.OwnedCard;
import com.sweng.cardsmule.shared.models.UserCard;
import com.sweng.cardsmule.shared.models.WishedCard;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.throwables.GeneralException;
import com.sweng.cardsmule.shared.throwables.InputException;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.shared.models.OwnedCardFetched;

public class CollectionServiceImpl extends RemoteServiceServlet implements CollectionService, MapDBConst, CollectionClassificationConstants {
	private static final long serialVersionUID = 7035000122385784676L;
    private final MapDB db;
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<Map<String, Collection>>(){}.getType();

    public CollectionServiceImpl() {
        db = new DBImplements();
    }

    public CollectionServiceImpl(MapDB mockDB) {
        db = mockDB;
    }

	@Override
	public boolean addCollection(String token, String collectionName) throws AuthenticationException {
        String email = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        return db.writeOperation(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type),
        		(Map<String, Map<String, Collection>> collectionMap) -> addCollection(email, collectionName, true, collectionMap));
	}
	
    private static boolean addCollection(String email, String collectionName, boolean isDeck, Map<String, Map<String, Collection>> collectionMap) {
        
    	try {
        	Map<String, Collection> userCollections = collectionMap.computeIfAbsent(email, k -> new LinkedHashMap<>());
        	   System.out.println("email: " + email + "  collectionName: " + collectionName + "  collectionMap " + collectionMap);
               if (userCollections.get(collectionName) != null) {
                   return false;
               }
               userCollections.put(collectionName, new Collection(collectionName, isDeck));
               collectionMap.put(email, userCollections);
               
               System.out.println(userCollections + " collection");
               
    	}catch(Exception error) {
    		System.out.println(error.toString());
    		System.out.println(error.getMessage());
    	}
    	
        
        for(String collectionEmail: collectionMap.keySet()) {
        	System.out.println("deck id " + collectionEmail);
        	for(String collectionN : collectionMap.get(collectionEmail).keySet()) {
        		System.out.println(collectionN);
        		System.out.println(collectionMap.get(collectionEmail).get(collectionN));
        	}
        }
        return true;
    }

	@Override
	public boolean removeDeck(String token, String deckName) throws AuthenticationException {
        String email = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        return db.writeOperation(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type), (Map<String, Map<String, Collection>> collectionMap) -> {
            Map<String, Collection> userCollections = collectionMap.get(email);
            if (userCollections == null)
                return false;
            Collection collection = userCollections.get(deckName);
            if (collection == null || !collection.isDeck())
                return false;
            userCollections.remove(deckName, collection);
            collectionMap.put(email, userCollections);
            return true;
        });
	}
	
	public static boolean createDefaultCollection(String email, Map<String, Map<String, Collection>> CollectionMap) {
		System.out.println("dentro a createDefaultCollection");
        return addCollection(email, COLLECTION_OWNED, false, CollectionMap) && addCollection(email, COLLECTION_WISHED, false, CollectionMap);
    }
	
    private void checkCollectionName(String collectionName) throws InputException {
        if (collectionName == null || collectionName.isEmpty())
            throw new InputException("Deck name is mandatory!");
    }
	
    private boolean checkDescriptionLength(String description) {
    	return (description.length() < 200) ? true : false;
    }

    private static void checkNotFound(String collectionName, Collection foundCollection) throws GeneralException {
        if (foundCollection == null) {
            throw new GeneralException("Collection not found!");
        }
    }


    
	@Override
	public boolean addOwnedCardToCollection(String token, CardsmuleGame game, String collectionName, int cardId,
			Grade grade, String description) throws GeneralException {
        String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        CardServiceImpl.checkGameValidity(game);
        checkCollectionName(collectionName);
        CardServiceImpl.checkCardIdValidity(cardId);
        if (grade == null) {
            throw new InputException("Status is mandatory");
        }
        if (description == null || !checkDescriptionLength(description)) {
            throw new InputException("Description is mandatory and must be less than 200 characters long");
        }
        return db.writeOperation(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type), (Map<String, Map<String, Collection>> collectionMap) -> {
            Map<String, Collection> decks = collectionMap.get(userEmail);
            System.out.println(userEmail + "email user");
            if (decks == null) {
            	System.out.println("errori");
                return false;
            }
            Collection foundCollection = decks.get(collectionName);
            if (foundCollection == null) {
            	System.out.println("errore 2");
                return false;
            }
            if (!foundCollection.addOwnedCard(new OwnedCard(cardId, grade, game, userEmail, description))) {
            	System.out.println("errore 3");
                return false;
            }
            collectionMap.put(userEmail, decks);
            return true;
        });
	}
	
    private List<OwnedCardFetched> fetchOwnedCardNames(Set<OwnedCard> ownedCards) {
        List<OwnedCardFetched> ownedCardFetched = new LinkedList<>();
        for (OwnedCard ownedCard : ownedCards) {
        	ownedCardFetched.add(new OwnedCardFetched(
                    ownedCard,
                    CardServiceImpl.getNameCard(ownedCard.getReferenceCardId(), db.getCachedMap(
                            getServletContext(),
                            CardServiceImpl.getCardMap(ownedCard.getCardGame()),
                            Serializer.INTEGER,
                            new GsonSerializer<>(gson)
                    ))
            ));
        }
        return ownedCardFetched;
    }
    
	public String getReferenceCardName(UserCard userCard) {
		MapDB db = new DBImplements();
		return CardServiceImpl.getNameCard(userCard.getReferenceCardId(), db.getCachedMap(
                getServletContext(),
                CardServiceImpl.getCardMap(userCard.getCardGame()),
                Serializer.INTEGER,
                new GsonSerializer<>(gson)
        ));
	}

	@Override
	public List<CollectionVariationPayload> removeOwnedCardFromCollection(String token, String collectionName,
			OwnedCard ownedCard) throws GeneralException {
		System.out.println("ENTERED IN CARD REMOVE METHOD");

        String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        checkCollectionName(collectionName);
        if (ownedCard == null)
            throw new InputException("Invalid physical card");
        
        //TODO: Controllare che la carta sia in eventuali proposte di scambio e rimuoverla o lanciare errore
        
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Collection> userCollections = collectionMap.get(userEmail);
        List<CollectionVariationPayload> modifiedCollections = new LinkedList<>();
        Collection foundCollection = userCollections.get(collectionName);
        checkNotFound(collectionName, foundCollection);
        System.out.println("CHECK NOT FOUND PASSED");
        
        if (collectionName.equals("Owned")) {
        	System.out.println("USER COLLECTIONS: " + userCollections.values());
            for (Collection collection : userCollections.values()) {
            	try {
        		System.out.println("COLLECTION NAME: " + collection.getName());
        		System.out.println("OwnedCard instance" + ownedCard.getClass().getName() + " ");
        		OwnedCard oc = ((OwnedCard) ownedCard);
        		System.out.println(((OwnedCard) collection.getOwnedCards().toArray()[0]).getId());
        		System.out.println("OC ID" + oc.getId());
        		
        		boolean removeRes = collection.removeOwnedCard(oc);
        		System.out.println("REMOVED RESULT: " + removeRes);
                if (removeRes) {
                	System.out.println("CARD REMOVED" + collection.getName());
                	modifiedCollections.add(new CollectionVariationPayload(collection.getName(), fetchOwnedCardNames(collection.getOwnedCards())));
                }
            	} catch(Exception e) {
            		System.out.println(e.getMessage());
            	}
            }
        } else {
        	System.out.println("ENTERED IN CUSTOM DECK DELETE INSTEAD");
        	foundCollection.removeOwnedCard(ownedCard);
        	modifiedCollections.add(new CollectionVariationPayload(foundCollection.getName(), fetchOwnedCardNames(foundCollection.getOwnedCards())));
        }
        try {
        	db.writeOperation(getServletContext(), () -> collectionMap.put(userEmail, userCollections));
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }
        return modifiedCollections;
	}

	@Override
	public List<CollectionVariationPayload> editOwnedCard(String token, String collectionName, OwnedCard ownedCard)
			throws GeneralException {
        String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        checkCollectionName(collectionName);
        if (!collectionName.equals(COLLECTION_OWNED) && !collectionName.equals(COLLECTION_WISHED))
            throw new InputException("You can only edit cards in dafults deck!");
        if (ownedCard == null)
            throw new InputException("Invalid card");
        //TODO: Controllare che la carta sia in eventuali proposte di scambio e rimuoverla o lanciare errore
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson));
        System.out.println("Email Querried" + userEmail);
        System.out.println("Emails existing" + collectionMap.keySet());
        Map<String, Collection> userCollections = collectionMap.get(userEmail);
        List<CollectionVariationPayload> modifiedCollections = new LinkedList<>();
        userCollections.values().forEach(collection -> {
            if (collection.removeOwnedCard(ownedCard)) {
            	collection.addOwnedCard(ownedCard);
            	modifiedCollections.add(new CollectionVariationPayload(collection.getName(), fetchOwnedCardNames(collection.getOwnedCards())));
            }
        });
        db.writeOperation(getServletContext(), () -> collectionMap.put(userEmail, userCollections));
        return modifiedCollections;
	}

	@Override
	public List<String> getUserCollectionNames(String token) throws AuthenticationException {
        String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Collection> collections = collectionMap.get(userEmail);
        return collections == null ? Collections.emptyList() : new ArrayList<>(collections.keySet());
	}

	@Override
	public List<OwnedCardFetched> getDeck(String token, String deckName) throws AuthenticationException {
		String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
		Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Collection> userCollections = collectionMap.get(userEmail);
        Collection userDeck = userCollections.get(deckName);
        return fetchOwnedCardNames(userDeck.getOwnedCards());
	}

	@Override
	public List<OwnedCardFetched> getUserCollection(String email) throws AuthenticationException {
		if (email == null || !validateEmail(email)) {
            throw new AuthenticationException("Email is not valid");
        }
		Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Collection> userCollections = collectionMap.get(email);
        Collection userCollection = userCollections.get(COLLECTION_OWNED);
        return fetchOwnedCardNames(userCollection.getOwnedCards());
		
	}

	@Override
	public List<OwnedCard> getOwnedCardsByCardId(int cardId) throws InputException {
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Set<String> userEmails = collectionMap.keySet();
        List<OwnedCard> ownedCards = new ArrayList<>();
        for (String userEmail : userEmails) {
            Set<OwnedCard> ownedUserCards = collectionMap.get(userEmail).get(COLLECTION_OWNED).getOwnedCards();
            for (OwnedCard ownedUserCard : ownedUserCards) {
                if (ownedUserCard.getReferenceCardId() == cardId) {
                	ownedCards.add(ownedUserCard);
                }
            }
        }
        return ownedCards;
	}	

	@Override
	public List<WishedCard> getWishedCardsByCardId(int cardId) throws InputException {
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        System.out.println(type + "questo è il type");
        Set<String> userEmails = collectionMap.keySet();
        List<WishedCard> wishedCards = new ArrayList<>();
        for (String userEmail : userEmails) {
            Set<OwnedCard> ownedUserCards = collectionMap.get(userEmail).get(COLLECTION_WISHED).getOwnedCards();
            for (OwnedCard ownedUserCard : ownedUserCards) {
                if (ownedUserCard.getReferenceCardId() == cardId) {
                	System.out.println("L'errore è in questo pezzo di codice ? " + ownedUserCard.getReferenceCardId() + " " + ownedUserCard.getGrade() + " " + ownedUserCard.getCardGame()  + " " + ownedUserCard.getUserEmail() );
                	wishedCards.add(new WishedCard(ownedUserCard.getReferenceCardId(), ownedUserCard.getGrade(), ownedUserCard.getCardGame(), ownedUserCard.getUserEmail()));
                	System.out.println("questo punto lo supera " );
                }
            }
        }
        return wishedCards;
	}
	
	@Override
	public boolean isOwnerOfACard(String token, int cardId) throws AuthenticationException {
		String userEmail = AuthenticationServiceImpl.checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
		Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson));
		Map<String, Collection> userCollections = collectionMap.get(userEmail);
		Collection ownedCollection = userCollections.get("Owned");
		for(OwnedCard oc : ownedCollection.getOwnedCards()) {
			if(oc.getReferenceCardId() == cardId) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public List<OwnedCardFetched> addOwnedCardsToDeck(String token, String deckName, List<OwnedCard> ownedCards)
			throws GeneralException {
        String userEmail = AuthenticationServiceImpl.checkTokenValidity(token,
                db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
        checkCollectionName(deckName);
        if (deckName.equals(COLLECTION_OWNED) || deckName.equals(COLLECTION_WISHED))
            throw new InputException("This method is not valid for default decks");
        if (ownedCards.isEmpty())
            throw new InputException("The list of cards is empty");
        Map<String, Map<String, Collection>> collectionMap = db.getPersistentMap(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type));
        Map<String, Collection> userCollections = collectionMap.get(userEmail);
        
        
        Collection userDeck = userCollections.get(deckName);
        checkNotFound(deckName, userDeck);
        ownedCards.forEach(userDeck::addOwnedCard);
        db.writeOperation(getServletContext(), () -> collectionMap.put(userEmail, userCollections));
        return fetchOwnedCardNames(userDeck.getOwnedCards());
	}
	
	public static Map<String, Collection> updateUserCollection(String userEmail, Map<String, Collection> userCollection,
            List<OwnedCard> received_trade_cards, List<OwnedCard> trade_away_cards) {
		System.out.println("userCollection " + userCollection + " received_trade_cards " +received_trade_cards + " trade_away_cards "+ trade_away_cards);
        for (Collection collection : userCollection.values()) {
            String collectionName = collection.getName();
            System.out.println("collection name" + collectionName );
            if (collectionName.equals(COLLECTION_OWNED)) {
                for (OwnedCard oCard : received_trade_cards) {
                	System.out.println("Trying to add " + oCard + " in " + userCollection.get(COLLECTION_OWNED).getOwnedCards());
                	System.out.println("OwnedCard id" + oCard.getId());
                	System.out.println("ALREADY CONTAINED: " + userCollection.get(COLLECTION_OWNED).getOwnedCards().contains(oCard));
                	System.out.println("OwnedCard reference id" + oCard.getReferenceCardId());
                	System.out.println("Collection OwnedCards reference ids");
                	for(OwnedCard oc: userCollection.get(COLLECTION_OWNED).getOwnedCards()) {
                		System.out.println("REF " + oc.getReferenceCardId() + " ID " + oc.getId());
                	}
                    if (!collection.addOwnedCard(oCard)) {
                    	System.out.println("mi rompo qui dio cane");
                        throw new RuntimeException("DB ROLLBACK!");
                    } else {
                    	oCard.setUserEmail(userEmail);
                    }
                    System.out.println("andato a buon fine");
                }
                for (OwnedCard ownedCard : trade_away_cards) {
                	System.out.println("pcard" + ownedCard.getUserEmail() + " "+ ownedCard.getId() + " " + ownedCard.getDescription());

                    if (collection.removeOwnedCard(ownedCard) == false) {
                    	System.out.println("mi rompo qui dio porco");
                        throw new RuntimeException("DB ROLLBACK!");
                    }
                }
            } else if (collectionName.equals(COLLECTION_WISHED)) {
                Map<Integer, List<OwnedCard>> lookUp = collection.getOwnedCards().stream().collect(
                        Collectors.groupingBy(OwnedCard::getReferenceCardId, Collectors.toCollection(LinkedList::new)));

                for (OwnedCard received_ocard : received_trade_cards) {
                    int cardId = received_ocard.getReferenceCardId();
                    if (!lookUp.containsKey(cardId)) {
                        continue;
                    }
                    for (OwnedCard wished_ocard : lookUp.get(cardId)) {
                        if (wished_ocard.getGrade().getValue() <= received_ocard.getGrade().getValue()) {
                            collection.removeOwnedCard(wished_ocard);
                        }
                    }
                }
            } else {
                trade_away_cards.forEach(collection::removeOwnedCard);
            }
        }
        return userCollection;
    }

}
