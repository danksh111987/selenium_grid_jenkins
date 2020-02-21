package com.orangehrm.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;

public class ImplIinvokedListner implements IInvokedMethodListener {

	@Override
	public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub
		
		if(method.isTestMethod()){
		//System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Chrome78ver\\chromedriver.exe");
			//Browser version changed
			
		//System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Chrome80ver\\chromedriver.exe");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setBrowserName("chrome");
		capabilities.setVersion("80");
		
		try {
			WebDriver dr = new RemoteWebDriver(new URL("http://192.168.1.6:8090/wd/hub/"), capabilities);
			WebDriverFactory.setWebDriver(dr);
			//for extend reports
			ExtentReportTestFactory.createNewTest(method);
			
			// implicit wait setting
			WebDriverFactory.getWebDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			WebDriverFactory.getWebDriver().manage().window().maximize();

			WebDriverFactory.getWebDriver().get("http://127.0.0.1/orangehrm-3.3.1/symfony/web/index.php/auth/login");
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		}
		
	}

	@Override
	public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
		// TODO Auto-generated method stub

		if(method.isTestMethod())
		{
			if(!testResult.isSuccess())
			{
				//screenshot for failed steps
				ExtentReportTestFactory.getTest().fail(testResult.getThrowable());
				SeleniumUtils.takeScreenshot(System.getProperty("user.dir")+"\\"+method.getTestMethod().getMethodName()+".png");
				try {
					ExtentReportTestFactory.getTest().addScreenCaptureFromPath(System.getProperty("user.dir")+"\\"+method.getTestMethod().getMethodName()+".png");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		// close dr after execution
		WebDriverFactory.getWebDriver().quit();
		}

	}

}
