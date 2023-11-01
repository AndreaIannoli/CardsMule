package com.sweng.cardsmule.client;

import com.sweng.cardsmule.client.authentication.User;
import com.sweng.cardsmule.client.activities.LoginActivity;
import com.sweng.cardsmule.client.views.LoginView;
import com.sweng.cardsmule.shared.AuthenticationServiceAsync;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.CredentialsPayload;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.easymock.EasyMock.*;

public class AuthenticationTest {
    IMocksControl ctrl;
    LoginView mockView;
    AuthenticationServiceAsync mockRpcService;
    User mockSubject;
    PlaceController mockPlaceController;
    LoginActivity authActivity;

    private static Stream<Arguments> provideExceptions() {
        return Stream.of(
                Arguments.of(new AuthenticationException("User not found")),
                Arguments.of(new Exception("Internal server error"))
        );
    }

    @BeforeEach
    public void initialize() {
        ctrl = EasyMock.createStrictControl();
        mockView = ctrl.createMock(LoginView.class);
        mockRpcService = ctrl.createMock(AuthenticationServiceAsync.class);
        mockSubject = new User();
        mockPlaceController = ctrl.createMock(PlaceController.class);
        authActivity = new LoginActivity(mockView, mockSubject, mockPlaceController, mockRpcService);
    }

    @Test
    public void testAuthenticateForIncorrectEmails() {
        mockView.displayAlert(anyString());
        expectLastCall();
        ctrl.replay();
        authActivity.signIn("test", "pass");
        ctrl.verify();
    }



    @Test
    public void testAuthenticateForIncorrectPassword() {
        mockView.displayAlert(anyString());
        expectLastCall();
        ctrl.replay();
        authActivity.signIn("test", "pass");
        ctrl.verify();
    }

    @Test
    public void testAuthenticateSuccessForAuthModeParameter() {
        String token = "validToken";
        String email = "test@test.it";
        String username = "test";
        CredentialsPayload authPayload = new CredentialsPayload(token,username, email);
        mockRpcService.signIn(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<CredentialsPayload> callback = (AsyncCallback<CredentialsPayload>) args[args.length - 1];
            callback.onSuccess(authPayload);
            return null;
        });
        mockView.setAuthToken(token);
        expectLastCall();
        mockSubject.setCredentials(token,username,email);
        expectLastCall();
        mockPlaceController.goTo(isA(Place.class));
        expectLastCall();
        ctrl.replay();
        authActivity.signIn(username, "password");
        ctrl.verify();
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    public void testAuthenticateFailure(Exception e) {
        mockRpcService.signIn(anyString(), anyString(), isA(AsyncCallback.class));
        expectLastCall().andAnswer(() -> {
            Object[] args = getCurrentArguments();
            AsyncCallback<String> callback = (AsyncCallback<String>) args[args.length - 1];
            callback.onFailure(e);
            return null;
        });
        mockView.displayAlert(anyString());
        expectLastCall();
        ctrl.replay();
        authActivity.signIn("test", "password");
        ctrl.verify();
    }
}
