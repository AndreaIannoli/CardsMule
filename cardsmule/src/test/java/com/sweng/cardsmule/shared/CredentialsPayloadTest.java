package com.sweng.cardsmule.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class CredentialsPayloadTest {
	 CredentialsPayload credentialsPayload;

	    @BeforeEach
	    public void initialize() {
	    	credentialsPayload = new CredentialsPayload("validToken", "test?4", "test@test.it");
	    }

	    @Test
	    public void testGetToken() {
	        Assertions.assertEquals("validToken", credentialsPayload.getToken());
	    }
	    @Test
	    public void testGetUsername() {
	        Assertions.assertEquals("test?4", credentialsPayload.getUsername());
	    }

	    @Test
	    public void testGetEmail() {
	        Assertions.assertEquals("test@test.it", credentialsPayload.getEmail());
	    }
}
