package com.sweng.cardsmule.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AuthenticationServiceAsync {
    void me(String token, AsyncCallback<String> callback);

    void signUp(String email, String username, String password, AsyncCallback<CredentialsPayload> callback);

    void signIn(String username, String password, AsyncCallback<CredentialsPayload> callback);

    void logout(String token, AsyncCallback<Boolean> callback);
}
