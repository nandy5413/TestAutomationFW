package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	private static ActionDriver actionDriver;
	protected static WebDriver driver;

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite

	public void LoadConfig() throws IOException {
		// Load the configuration files
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
		logger.info("Config.properties file loaded");
		// Start the Extent Report
		ExtentManager.getReporter();
	}

	@BeforeMethod

	public void setup() throws IOException {

		System.out.println("Setting up WebDriver for " + this.getClass().getSimpleName());

		LaunchBrowser();
		configureBrowser();
		staticWait(2);

		// Initialize the ActionDriver only once
		if (actionDriver == null) {
			actionDriver = new ActionDriver(driver);
			System.out.println("ActionDriver instance is created");
		}

		logger.info("WebDriver initialized & Browser maximized");
		logger.trace("Trace message");
		logger.error("error message");
		logger.debug("This is debug messages");
		logger.fatal("This is fatal messages");
		logger.warn("This is a warn message");

	}

	private void LaunchBrowser() throws IOException {

		// Initialize the web driver to launch the application
		String browser = prop.getProperty("browser");
		if (browser.equalsIgnoreCase("chrome")) {

			driver = new ChromeDriver();
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created");

		} else if (browser.equalsIgnoreCase("firefox")) {

			driver = new FirefoxDriver();
			ExtentManager.registerDriver(getDriver());
			logger.info("FirefoxDriver Instance is created");
		} else {
			throw new IllegalArgumentException("Browser not Supported");

		}

	}

	// Configure the browser settings such as implicite wait, maximize window, open
	// URL
	private void configureBrowser() {
		// Implicite Wait
		int implicitWait = Integer.parseInt(prop.getProperty("impliciteWait"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// maximize the browser

		driver.manage().window().maximize();

		// open the URL

		try {
			driver.get(prop.getProperty("url"));
		} catch (Exception e) {
			System.out.println("Failed to navigate to the URL" + e.getMessage());
		}

	}

	@AfterMethod

	public void tearDown() {
		if (driver != null) {
			try {
				driver.quit();
			} catch (Exception e) {
				System.out.println("Unable to load the driver" + e.getMessage());
			}

		}
		logger.info("WebDriver instance is closed");
		driver = null;
		actionDriver = null;
		ExtentManager.endTest();
	}

	// Prop getter Method
	public static Properties getProp() {
		return prop;
	}

	// Driver getter Method public WebDriver getDriver() { return driver; }

	// Driver setter method 
	public void setDriver(WebDriver driver) { this.driver= driver; }

	// Static Wait for pause

	// Getter Method for WebDriver
	public static WebDriver getDriver() {
		if (driver == null) {

			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver;
	}

//  Getter Method for ActionDriver
	public ActionDriver getActionDriver() {
		if (actionDriver == null) {

			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return actionDriver;
	}

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
