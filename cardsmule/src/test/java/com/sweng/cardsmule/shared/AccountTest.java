package com.sweng.cardsmule.shared;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import com.sweng.cardsmule.shared.models.Account;

public class AccountTest implements AccountTestCostants{
	private Account account;

    @BeforeEach
    public void initialize() {
    	account= new Account(email, username, BCrypt.hashpw(password, BCrypt.gensalt()) );
    }

    @Test
    public void testGetEmail() {
        Assertions.assertEquals(email, account.getEmail());
    }

    @Test
    public void testGetPassword() {
        Assertions.assertTrue(BCrypt.checkpw(password, account.getPassword()));
    }
    @Test
    public void testGetUsername() {
        Assertions.assertEquals(username, account.getUsername());
    }
}
