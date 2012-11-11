package com.conx.logistics.app.whse.rcv.rcv.pageflow.tests.vaadin.testbench.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.vaadin.testbench.TestBench;
import com.vaadin.testbench.TestBenchTestCase;

public class FindReceivePageTest extends TestBenchTestCase {
	private String baseUrl;
	private String urlSuffix;

	@Before
	public void setUp() throws Exception {
		setDriver(TestBench.createDriver(new FirefoxDriver()));
        this.baseUrl = "http://localhost:8080";
        this.urlSuffix = "/";
	}

	@After
	public void tearDown() throws Exception {
		getDriver().quit();
	}
	
	@Test
	public void testSelectReceive() throws Exception {
		openPage();
	}
	
	private void openPage() {
        getDriver().get(baseUrl + urlSuffix);
    }
}
