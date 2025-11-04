package com.orangehrm.actiondriver;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.Properties;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	// Implicite Wait
	// driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

	public ActionDriver(WebDriver driver) {

		this.driver = driver;

		int expliciteWait = Integer.parseInt(BaseClass.getProp().getProperty("expliciteWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(expliciteWait));
		System.out.println("WebDriver instance is created.");
	}

	// Action1: Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logSteps("clicked an element: " + elementDescription);
			logger.info("clicked an element-->" + elementDescription);
		} catch (Exception e) {
			logger.error("Unable to click element: " + e.getMessage());
			ExtentManager.logfailureWithScreenshots(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
		}
	}

	// Action2: Method to enter text into an input field element
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			// applyBorder(by,"green");
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			logger.error("Unable to enter the text: " + e.getMessage());

		}

	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid NullPointerException
		if (driver == null) {
			return "Driver is not initialized.";
		}
		if (locator == null) {
			return "Locator is null.";
		}

		try {
			// Find the element using the locator
			WebElement element = driver.findElement(locator);

			// Get element attributes
			String name = element.getDomProperty("name");
			String id = element.getDomProperty("id");
			String text = element.getText();
			String className = element.getDomProperty("class");
			String placeholder = element.getDomProperty("placeholder");

			// Return a description based on available attributes
			if (isNotEmpty(name)) {
				return "Element with name: " + name;
			} else if (isNotEmpty(id)) {
				return "Element with ID: " + id;
			} else if (isNotEmpty(text)) {
				return "Element with text: " + truncate(text, 50);
			} else if (isNotEmpty(className)) {
				return "Element with class: " + className;
			} else if (isNotEmpty(placeholder)) {
				return "Element with placeholder: " + placeholder;
			} else {
				return "Element located using: " + locator.toString();
			}
		} catch (Exception e) {
			// Log exception for debugging
			e.printStackTrace(); // Replace with a logger in a real-world scenario
			return "Unable to describe element due to error: " + e.getMessage();
		}
	}

	// Utility method to truncate long strings
	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to check if a string is not null or empty
	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Action3: Method to get text into an input field element
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			logger.error("Unable to retrieve  the value: " + e.getMessage());
			return "";
		}
	}

	// Action4: Method to compare two text into an input field element-- return type
	// to boolean
	public boolean compareText(By by, String expectedText) {

		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();

			if (expectedText.equals(actualText)) {
				logger.info("Texts are matching" + actualText + " equals " + expectedText);
				ExtentManager.logStepsWithScreenshots(BaseClass.getDriver(), "Compare Text",
						"Text Verified Successfully! " + actualText + " equals " + expectedText);
				return true;

			} else {
				logger.info("Texts are NOT matching" + actualText + " equals " + expectedText);
				ExtentManager.logfailureWithScreenshots(BaseClass.getDriver(), "Text Comparison Failed!",
						"Text Comparison Failed! " + actualText + " not equals " + expectedText);
				return false;

			}
		} catch (Exception e) {
			logger.error("Unable to compare  the value: " + e.getMessage());

		}
		return false;
	}

	// Action5: Method to check if an element is displayed
	/*
	 * public boolean isDisplayed(By by) { try { waitForElementToBeVisible(by);
	 * boolean isDisplayed = driver.findElement(by).isDisplayed(); if (isDisplayed)
	 * { logger.info("The element is visible"); return isDisplayed; } else { return
	 * isDisplayed; } } catch (Exception e) {
	 * logger.error("Unable to check if an element is displayed" + e.getMessage());
	 * return false; }
	 * 
	 * 
	 * }
	 */

	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			logger.info("Element is displayed", getElementDescription(by));
			ExtentManager.logStepsWithScreenshots(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			logger.error("Element not displayed : " + e.getMessage());

			ExtentManager.logfailureWithScreenshots(BaseClass.getDriver(), "Element not displayed : ",
					getElementDescription(by));
			return false;
		}
	}
	// Action6: Wait for the element to be clickable

	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("element is not clickable" + e.getMessage());
		}
	}
	// Wait for the element to be visible

	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			logger.error("element is not visible" + e.getMessage());
		}
	}

	// Action7: Scroll to an Element
	public void scrollToElement(By by) {

		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			logger.error("Unable to locate the element" + e.getMessage());
		}
	}

	// Action8: Wait for the page to load
	private void waitForPageLoad(int timeOutInSec) {

		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyStrate").equals("complete"));
			logger.info("Page Loaded Successfully");
		} catch (Exception e) {
			logger.error("Page didn't load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}

	}

}
