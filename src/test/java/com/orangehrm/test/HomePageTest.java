package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {

	private LoginPage loginpage;
	private HomePage homepage;

	@BeforeMethod
	public void setupPages() {
		
		loginpage = new LoginPage(getDriver());
		homepage = new HomePage(getDriver());
		

	}
	
	@Test 
	public void verifyOrangeHRMLogo() {
		ExtentManager.startTest("Home page Logo Test");
		ExtentManager.logSteps("Trying to Login entering invalid username & password");
		loginpage.login("admin", "admin123");
		Assert.assertTrue(homepage.isLogoVisible(),"OrangeHRM logo is visible");
		ExtentManager.logSteps("Validation Successful: Logo Displayed");
	}
}
