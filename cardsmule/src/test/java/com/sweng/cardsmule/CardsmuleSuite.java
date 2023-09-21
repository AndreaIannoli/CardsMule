package com.sweng.cardsmule;

import com.sweng.cardsmule.client.CardsmuleTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;

public class CardsmuleSuite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for Cardsmule");
		suite.addTestSuite(CardsmuleTest.class);
		return suite;
	}
}
