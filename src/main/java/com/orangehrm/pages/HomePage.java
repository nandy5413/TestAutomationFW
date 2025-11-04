package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;

public class HomePage {

	private ActionDriver actionDriver;

	/*
	 * // Define locators using By Class private By adminTab =
	 * By.xpath("//span[text()='Admin']"); private By userName =
	 * By.xpath("//p[text()='tran hung']"); private By brandBanner =
	 * By.xpath("//a[@class='oxd-brand']"); private By dropDown =
	 * By.xpath("//div[@class='oxd-topbar-header-userarea']"); private By logout =
	 * By.xpath("//a[@class='oxd-userdropdown-link' and text()='Logout']");
	 */

	// Define locators using By class
	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIDButton = By.className("oxd-userdropdown-name");
	private By logoutButton = By.xpath("//a[text()='Logout']");
	private By oranageHRMlogo = By.xpath("//div[@class='oxd-brand-banner']//img");
	// initialize the ActionDriver

	/*
	 * public HomePage(WebDriver driver) { this.actionDriver = new
	 * ActionDriver(driver);
	 * 
	 * }
	 */
	public HomePage(WebDriver driver) {
		this.actionDriver = new ActionDriver(driver);
	}
	// Method to check if any Admin Tab is displayed

	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTab);
	}
	// Method to check if Page Logo is displayed

	public boolean isLogoVisible() {
		return actionDriver.isDisplayed(oranageHRMlogo);
	}

	// Method to perform logout operation
	public void logout() {
		actionDriver.click(userIDButton);
		actionDriver.click(logoutButton);
	};

}
