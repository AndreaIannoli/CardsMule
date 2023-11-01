package com.sweng.cardsmule.shared;

import com.google.gwt.user.client.rpc.RemoteService;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;

@RemoteServiceRelativePath("users")
public interface AuthenticationService extends RemoteService {
    String me(String token) throws AuthenticationException;

    CredentialsPayload signIn(String username, String password) throws AuthenticationException;
    
    CredentialsPayload signUp(String email, String username, String password) throws AuthenticationException;

    Boolean logout(String token) throws AuthenticationException;
}
