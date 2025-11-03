package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;

public class LoginPage {

	private ActionDriver actionDriver;

	// Define locators using By Class

	private By userNameField = By.xpath("//input[@name='username']");
	private By passwordField = By.xpath("//input[@name='password']");
	private By loginButton = By.xpath("//button[@type='submit']");
	private By errorMessage = By.xpath("//p[text()='Invalid credentials']");

	// initialize the ActionDriver

	/*
	 * public LoginPage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver);
	 * 
	 * }
	 */
	public LoginPage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}


	// Method to perform login

	public void login(String username, String password) {
		actionDriver.enterText(userNameField, username);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}

	// Method to check if any error message displayed

	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}

	// Method to check the error message displayed

	public String getErrorMessageDisplayed() {
		return actionDriver.getText(errorMessage);
	}

	// Verify if the error message is correct

	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}

}
