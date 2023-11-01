package com.sweng.cardsmule.client;

import com.sweng.cardsmule.client.authentication.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class AuthenticationSubjectTest {
    User user;

    @BeforeEach
    public void initialize() {
        user = new User();
        user.setCredentials("validToken", "test", "test@test.it");
    }

    @Test
    public void testGetToken() {
        Assertions.assertEquals("validToken", user.getToken());
    }
    
    @Test
    public void testGetUsername() {
        Assertions.assertEquals("test", user.getUsername());
    }
    
    @Test
    public void testGetEmail() {
        Assertions.assertEquals("test@test.it", user.getEmail());
    }

    
    @Test
    public void testIsLoggedIn() {
        Assertions.assertTrue(user.isLoggedIn());
    }
}