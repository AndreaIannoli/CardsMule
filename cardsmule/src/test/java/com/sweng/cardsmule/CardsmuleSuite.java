package com.sweng.cardsmule;

import com.sweng.cardsmule.client.CardsmuleTest;
import com.google.gwt.junit.tools.GWTTestSuite;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


public class CardsmuleSuite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Tests for Cardsmule");
		suite.addTestSuite(CardsmuleTest.class);
		return suite;
	}
}
