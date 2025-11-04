package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

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
		ExtentManager.startTest("Invalid Login Test");
		ExtentManager.logSteps("Trying to Login entering invalid username & password");
		loginpage.login("admin", "admin");
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedErrorMessage),
				"Test Failed: Expected error message not displayed → " + expectedErrorMessage);
		ExtentManager.logSteps("Validation Successful");
		
	}

	@Test
	public void verifyValidLoginTest() {
		ExtentManager.startTest("Valid Login Test");
		ExtentManager.logSteps("Navigating to Login Page entering valid username & password");
		loginpage.login("admin", "admin123");
		ExtentManager.logSteps("Verifying if the Admin Tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin Tab is visible");
		ExtentManager.logSteps("Validation Successful");
		homepage.logout();
		ExtentManager.logSteps("Logged out successfully");
		staticWait(10);
	}
}
