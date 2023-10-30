package com.sweng.cardsmule.server;

import com.sweng.cardsmule.client.authentication.User;

import com.sweng.cardsmule.server.mapDB.MapDB;
import com.sweng.cardsmule.server.TestDBCreation;
import com.sweng.cardsmule.server.services.AuthenticationServiceImpl;
import com.sweng.cardsmule.shared.throwables.AuthenticationException;
import com.sweng.cardsmule.shared.models.Account;
import com.sweng.cardsmule.shared.models.UserCard;
import com.sweng.cardsmule.shared.CredentialsPayload;
import org.easymock.IMocksControl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mapdb.Serializer;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;

import static org.easymock.EasyMock.*;

public class AuthenticationServiceTest {
    @Nested
    class WithMockDB {
        ServletConfig mockConfig;
        ServletContext mockCtx;
        private IMocksControl ctrl;
        private MapDB mockDB;
        private AuthenticationServiceImpl authService;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            mockDB = ctrl.createMock(MapDB.class);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            authService = new AuthenticationServiceImpl(mockDB);
            authService.init(mockConfig);
        }

        @Test
        public void testsignInForNullUsername1() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signUp(null,"test", "password"));
        }

        @Test
        public void testsignInForEmptyStringUsername1() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signUp("", "", "password"));
        }

        @Test
        public void testsignInForIncorrectUsernames1() {
            Assertions.assertAll(() -> {
                Assertions.assertThrows(AuthenticationException.class, () -> authService.signUp("test", "test", "password"));
            });
        }

        @Test
        public void testsignInForNullPassword1() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signUp("test", "test", ""));
        }

        @Test
        public void testsignInForLessThan8CharactersPassword1() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signUp("test","test", "passwor"));
        }

        @Test
        public void testsignInForNullUsername() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signIn(null, "password"));
        }

        @Test
        public void testsignInForEmptyStringUsername() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signIn("", "password"));
        }

        @Test
        public void testsignInForNullPassword() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signIn("test", ""));
        }

        @Test
        public void testsignInForLessThan8CharactersPassword() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.signIn("test", "passwor"));
        }

        @Test
        public void testsignInForNotPasswordMatch() {
            Map<String, Account> userMap = new HashMap<>();
            userMap.put("test", new Account("test", " " ,BCrypt.hashpw("wrong_password", BCrypt.gensalt())));
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            expect(mockDB.getPersistentMap(isA(ServletContext.class), anyString(), isA(Serializer.class), isA(Serializer.class)))
                    .andReturn(userMap);
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () ->
                    authService.signIn("test", "password"));
            ctrl.verify();
        }

        @Test
        public void testLogoutForNullToken() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.logout(null));
        }

        @Test
        public void testCheckTokenValidityForInvalidToken() {
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> authService.checkTokenValidity("invalidToken", new HashMap() {{
                put("validToken1", new Account("test@test1.it", "test", "passw"));
                put("validToken2", new Account("test@test1.it", "test", "passw"));
                put("validToken3", new Account("test@test1.it", "test", "passw"));
            }}));
            ctrl.verify();
        }

        @Test
        public void testCheckTokenValidityForNullToken() {
            Assertions.assertThrows(AuthenticationException.class, () -> authService.checkTokenValidity(null, new HashMap<>()));
        }


        


    @Nested
    class DBcreation {
        IMocksControl ctrl;
        AuthenticationServiceImpl authService;
        ServletConfig mockConfig;
        ServletContext mockCtx;

        @BeforeEach
        public void initialize() throws ServletException {
            ctrl = createStrictControl();
            TestDBCreation cDB = new TestDBCreation(new HashMap(), new HashMap<>());
            authService = new AuthenticationServiceImpl(cDB);
            mockConfig = ctrl.createMock(ServletConfig.class);
            mockCtx = ctrl.createMock(ServletContext.class);
            authService.init(mockConfig);
        }
        
        @Test
        public void testLogoutForInvalidToken() {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            ctrl.replay();
            Assertions.assertThrows(AuthenticationException.class, () -> authService.logout("invalidToken"));
            ctrl.verify();
        }

        @Test
        public void testLogoutForValidToken() throws AuthenticationException {
            expect(mockConfig.getServletContext()).andReturn(mockCtx);
            ctrl.replay();
            Assertions.assertTrue(authService.logout("validToken"));
            ctrl.verify();
        }
    }

    }
    }