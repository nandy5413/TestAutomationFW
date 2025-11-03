package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	// Initialize the Extent Report

	public static ExtentReports getReporter() {
		if (extent == null) {
			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.STANDARD);

			extent = new ExtentReports();
			// Adding System information

			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	// start the Test
	public static ExtentTest statTest(String testName) {
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
	}

	// End the Test
	public static void endTest() {
		getReporter().flush();
	}

	// Get Current Thread's test
	public static ExtentTest getTest() {
		return test.get();
	}

	// Method o get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return ("No Test is currently active for this thread");
		}

	}

	// Log the steps
	public static void logSteps(String logMessage) {
		getTest().info(logMessage);

	}
	// Log a step validation with screenshots

	public static void logStepsWithScreenshots(WebDriver driver, String logMessage, String Screenshotmessage) {
		getTest().pass(logMessage);
		// log screenshots
		attachScreenshot(driver, Screenshotmessage);
	}

	// Log a failure
	public static void logfailureWithScreenshots(WebDriver driver, String logMessage, String Screenshotmessage) {
		getTest().pass(logMessage);
		// log screenshots
		attachScreenshot(driver, Screenshotmessage);
	}

	// Log skip
	public static void logSkip(String logMessage) {
		getTest().skip(logMessage);

	}
//Take a screenshot with date & time in the file

	public static String takeScreenshot(WebDriver driver, String screenshotName) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		// Format date & Time for filename
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
		// Saving the screenshot to a file
		String DestPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/screenshots"
				+ screenshotName + "_" + timeStamp + ".png";
		File finalPath = new File(DestPath);
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Convert screenshot to Base64 for embedding in the report
		String base64Format = convertToBase64(src);
		return base64Format;

	}
	// method to convert the screenshot to the base64 format

	public static String convertToBase64(File screenShotFile) {
		String base64Format = "";
		// read the file content into a byte array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenShotFile);
			// convert byte array to the Base64 String
			base64Format = Base64.getEncoder().encodeToString(fileContent);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return base64Format;

	}

	// Attach screenshot to the report using Base64
	public static void attachScreenshot(WebDriver driver, String message) {
		try {
			String screenShotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenShotBase64).build());
		} catch (Exception e) {
			getTest().fail("Failed to attach screenshot" + message);
			e.printStackTrace();
		}
	}

	// Register WebDriver for the current thread

	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}
}
