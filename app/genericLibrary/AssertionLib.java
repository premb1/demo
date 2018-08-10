package com.eBMS.app.genericLibrary;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import com.eBMS.app.init.Init;


public class AssertionLib extends Init
{
	public static String resultValue = "";
	
	public static void validate(String expectedValue)
	{
		String actualValue;
		StringBuffer verificationErrors = new StringBuffer();
		//System.out.println(driver.getPageSource());
		if(driver.getPageSource().contains(expectedValue))
		{
			actualValue=expectedValue;
			resultValue = "Pass";
			SeleniumLib.takeScreenshot();
		}
		else
		{
			actualValue="~ValueNotFound~";
			resultValue = "Fail";
			SeleniumLib.takeScreenshot();
			//driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td/table/tbody/tr/td/a[1]/span")).click();
		}		
		System.out.println("Actual : " + actualValue);
		System.out.println("Expected : " + expectedValue);		
		try 
		{			
			Assert.assertEquals(actualValue, expectedValue);						
		} 
		catch (Error e) 
		{			
			verificationErrors.append(e.toString());
			System.out.println(verificationErrors);
		}		
	}
	
	public static void verify(String expectedValue)
	{
		String actualValue;
		if(driver.getPageSource().contains(expectedValue))
		{
			actualValue=expectedValue;
			resultValue = "Pass";
			SeleniumLib.takeScreenshot();
		}
		else
		{
			actualValue="~ValueNotFound~";
			resultValue = "Fail";
			SeleniumLib.takeScreenshot();			
		}		
		System.out.println("Actual : " + actualValue);
		System.out.println("Expected : " + expectedValue);		
	}
}
