package com.framework.report;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.framework.utilities.Configuration;

public class ReportTestManager {

	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	static ExtentReports extent = ReportManager.getInstance();

	/**
	 * It will return test name
	 * 
	 * @return
	 */
	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	/**
	 * Write code for end test
	 */
	public static synchronized void endTest() {

	}

	/**
	 * Add test in extent report listener
	 * 
	 * @param testName  : Test name
	 * @param className : class name
	 * @return
	 * @throws Exception
	 */
	public static synchronized ExtentTest startTest(String testName, String className) {
		ExtentTest test = extent.createTest(testName, className);
		String authName = null;
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		if (System.getenv("AuthorName") == null || System.getenv("AuthorName").equals("")) {
			try {
				authName = Configuration.getConfigData("AuthorName");
			} catch (Exception e) {				
				e.printStackTrace();
			}
		} else {
			authName = System.getenv("AuthorName");
		}
		test.assignAuthor(authName);
		test.assignCategory(className);
		return test;
	}

	/**
	 * Log text in extent report
	 * 
	 * @param message
	 */
	public static synchronized void logText(String message) {
		getTest().log(Status.PASS, message);
	}

	public static void addPriority(int priority) {
		getTest().assignDevice("Priority&nbsp;:&nbsp;" + priority);
	}
}
