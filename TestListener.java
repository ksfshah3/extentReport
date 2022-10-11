package com.framework.report;

import java.io.IOException;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.framework.selenium.core.BaseTest;
import com.framework.selenium.core.LTtunnelsetup;
import com.framework.utilities.Configuration;


public class TestListener extends BaseTest implements ITestListener{
	
	private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }
    
    /**
     * Before starting all tests, below method runs
     */
    @Override
    public void onStart(ITestContext iTestContext) {
    	String runMode = null;
    	try {
			runMode = Configuration.getConfigData("RunMode");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	if("LT".equalsIgnoreCase(runMode))
    	//	LTtunnelsetup.onExecutionStart();
        System.out.println("Starting test suite " + iTestContext.getName());
        iTestContext.setAttribute("WebDriver", this.getWebDriver());
    }
 
 
    /**
     * After ending all tests, below method runs.
     */
    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("Finishing test suite " + iTestContext.getName());     
        ReportTestManager.endTest();
        ReportManager.getInstance().flush();       
    }
  
    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("Starting test " +  getTestMethodName(iTestResult) + " start");
        //Start operation for extent reports.
        
        ReportTestManager.startTest(iTestResult.getMethod().getMethodName(),
        		iTestResult.getInstance().getClass().getSimpleName());        
        ReportTestManager.addPriority(iTestResult.getMethod().getPriority());
    }
 
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("Test " +  getTestMethodName(iTestResult) + " has passed");
        //Extentreports log operation for passed tests.
        ReportTestManager.getTest().log(Status.PASS, "Test passed");
    }
 
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("Test " +  getTestMethodName(iTestResult) + " has failed");
 
        //Get driver from BaseTest and assign to local webdriver variable.
        Object testClass = iTestResult.getInstance();
        WebDriver webDriver = ((BaseTest) testClass).getWebDriver();
 
        //Take base64Screenshot screenshot.
        String base64Screenshot = "data:image/png;base64,"+((TakesScreenshot)webDriver).
                getScreenshotAs(OutputType.BASE64);
        
        //ReportTestManager.getTest().log(Status.FAIL, iTestResult.getThrowable());
        ReportTestManager.getTest().fail(iTestResult.getThrowable());
       try {
			ReportTestManager.getTest().fail("details", MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot).build());
		} catch (IOException e) {
			
			e.printStackTrace();
		}        
    }
 
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("Test "+  getTestMethodName(iTestResult) + " has skipped");
        
        //Extentreports log operation for skipped tests.
        ReportTestManager.getTest().log(Status.SKIP, "Test "+  getTestMethodName(iTestResult) + " has skipped");
    }
 
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
       // System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
    }	
}
