package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {

		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());

	}

	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public  void inValidLoginTest(String username, String password){
		ExtentManager.startTest("Invalid Login Test");
		ExtentManager.logSteps("Trying to Login entering invalid username & password");
		loginpage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.verifyErrorMessage(expectedErrorMessage),
				"Test Failed: Expected error message not displayed → " + expectedErrorMessage);
		ExtentManager.logSteps("Validation Successful");
		
	}

	@Test (dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password)  {
		ExtentManager.startTest("Valid Login Test");
		ExtentManager.logSteps("Navigating to Login Page entering valid username & password");
		loginpage.login(username, password);
		ExtentManager.logSteps("Verifying if the Admin Tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(), "Admin Tab is visible");
		ExtentManager.logSteps("Validation Successful");
		homepage.logout();
		ExtentManager.logSteps("Logged out successfully");
		staticWait(10);
	}
}
