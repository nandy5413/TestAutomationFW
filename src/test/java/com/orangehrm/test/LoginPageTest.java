package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;

public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {

		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());

	}

	@Test
	public void verifyInvalidLoginTest() {
		loginpage.login("admin", "admin");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedErrorMessage),
				"Test Failed: Expected error message not displayed → " + expectedErrorMessage);
		homepage.logout();
		staticWait(5);
	}

	@Test
	public void verifyValidLoginTest() {
		loginpage.login("admin", "admin123");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin Tab is visible");
		homepage.logout();
		staticWait(10);
	}
}
