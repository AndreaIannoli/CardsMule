package com.sweng.cardsmule.server.services;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sweng.cardsmule.server.gsonserializer.GsonSerializer;
import com.sweng.cardsmule.server.mapDB.DBImplements;
import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.mapDB.MapDBConst;
import com.sweng.cardsmule.shared.AuthenticationService;
import com.sweng.cardsmule.shared.CredentialsPayload;
import com.sweng.cardsmule.shared.LoginSession;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.models.Collection;
import java.lang.reflect.Type;


public class AuthenticationServiceImpl extends RemoteServiceServlet implements AuthenticationService, MapDBConst {
    private static final long serialVersionUID = -469906791894125129L;
	private static final String SECRET = "sadhgsahdgsahghjgjddshsadhjsajkhdkjhsadsavbnxznvcnbxzvdgsad";
    private static final Pattern emailPattern = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private final MapDB db;
    private final Gson gson = new Gson();
    private final Type type = new TypeToken<Map<String, Collection>>() {
    }.getType();

    public AuthenticationServiceImpl() {
        db = new DBImplements();
    }

    public AuthenticationServiceImpl(MapDB mockDB) {
        db = mockDB;
    }

    public static boolean validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        
        return matcher.find();
    }

    private boolean validateCredentials(String username, String password) {
        return (username == null || username.isBlank()) ||
                (password == null || password.isBlank() || password.length() < 8);
    }
    
    private boolean validateCredentials(String email, String username, String password) {
        return (username == null || username.isBlank() ||
        		email == null || email.isBlank() || !validateEmail(email)) ||
                (password == null || password.isBlank() || password.length() < 8);
    }

    // create token
    private static String generateHash(String userEmail) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest((userEmail + SECRET).getBytes());
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String generateAndStoreLoginToken(Account account) {
        String token = generateHash(account.getEmail());
        
        db.writeOperation(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson),
                (Map<String, LoginSession> loginMap) -> {
                    loginMap.put(token, new LoginSession(account.getUsername(), account.getEmail(), System.currentTimeMillis()));
                    return null;
                });
        return token;
    }

    private static boolean checkTokenExpiration(long loginTime) {
        final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
        return loginTime > System.currentTimeMillis() - EXPIRATION_TIME;
    }

    public static String checkTokenValidity(String token, Map<String, LoginSession> loginMap) throws AuthenticationException {
        if (token == null) {
            throw new AuthenticationException("Invalid token");
        }
        LoginSession session = loginMap.get(token);
        if (session == null) {
            throw new AuthenticationException("Invalid token");
        } else if (!checkTokenExpiration(session.getLoginTime())) {
            throw new AuthenticationException("Expired token");
        }
        return session.getEmail();
    }

    @Override
    public String me(String token) throws AuthenticationException {
        return checkTokenValidity(token, db.getPersistentMap(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson)));
    }

    @Override
    public CredentialsPayload signUp(String email, String username, String password) throws AuthenticationException {
        if (validateCredentials(email, username, password))
            throw new AuthenticationException("Invalid credentials");
        Map<String, Account> accountMap = db.getPersistentMap(
                getServletContext(), MAP_USER, Serializer.STRING, new GsonSerializer<>(gson));
        
        Map<String, Account> accountEmailMap = db.getPersistentMap(
                getServletContext(), MAP_ACCOUNT, Serializer.STRING, new GsonSerializer<>(gson));

        Account account = new Account(email, username, BCrypt.hashpw(password, BCrypt.gensalt()));
        
        System.out.println("contiene email?: " + accountEmailMap.containsValue(email));
        System.out.println("MAp Account " + accountEmailMap.size());
        
        
        if (accountEmailMap.get(email) != null)
            throw new AuthenticationException("Email already exists");
        if (accountMap.get(username) != null)
            throw new AuthenticationException("Username already exists");
        
        db.writeOperation(getServletContext(), MAP_ACCOUNT, Serializer.STRING, new GsonSerializer<>(gson),
                (Map<String, Account> userMap) -> {
                	accountEmailMap.put(email, account);
                    return null;
                });
        
        db.writeOperation(getServletContext(), MAP_USER, Serializer.STRING, new GsonSerializer<>(gson),
                  (Map<String, Account> userMap) -> {
                      accountMap.put(username, account);
                      return null;
                  });
          db.writeOperation(getServletContext(), MAP_DECK, Serializer.STRING, new GsonSerializer<>(gson, type),
                  (Map<String, Map<String, Collection>> collectionMap) -> {
               
                      System.out.println(collectionMap + "che palle" + "  email: " + email);
                      CollectionServiceImpl.createDefaultCollection(email, collectionMap);
                      return null;
                  });
        System.out.println("MAP START");
        for(Entry entry : accountMap.entrySet()) {
        	System.out.println(entry.getValue());
        	System.out.println(entry.getKey());
        }
        System.out.println("MAP END");
        return new CredentialsPayload(generateAndStoreLoginToken(account), username, email);
    }

    @Override
    public CredentialsPayload signIn(String username, String password) throws AuthenticationException {
    	System.out.println(username + " " + password);
        if (validateCredentials(username, password)) {
        	System.out.println(username + " " + password);
            throw new AuthenticationException("Invalid credentials" + username + " " + password);
        }
        
        Map<String, Account> accountMap = db.getPersistentMap(
                getServletContext(), MAP_USER, Serializer.STRING, new GsonSerializer<>(gson));
        Account account = accountMap.get(username);
        String email = account.getEmail();
        System.out.println("MAP START");
        for(Entry entry : accountMap.entrySet()) {
        	System.out.println(entry.getValue());
        	System.out.println(entry.getKey());
        }
        System.out.println("MAP END");
        if (account == null) {
            throw new AuthenticationException("User not found" + accountMap.toString());
        }
        
        if (!BCrypt.checkpw(password, account.getPassword())) {
            throw new AuthenticationException("Password don't match");
        }
        
        return new CredentialsPayload(generateAndStoreLoginToken(account),username, email);
    }

    @Override
    public Boolean logout(String token) throws AuthenticationException {
        if (token == null)
            throw new AuthenticationException("Invalid token");
        if (db.writeOperation(getServletContext(), MAP_LOGIN, Serializer.STRING, new GsonSerializer<>(gson),
                (Map<String, LoginSession> loginMap) -> loginMap.remove(token) == null))
            throw new AuthenticationException("User not found");
        return true;
    }
}
