package com.eBMS.app.testEngine;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.testng.SkipException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.eBMS.app.init.Init;
import com.eBMS.app.genericLibrary.AssertionLib;
import com.eBMS.app.genericLibrary.JavaLib;
import com.eBMS.app.genericLibrary.Reporter;
import com.eBMS.app.genericLibrary.SeleniumLib;

public class TestCaseEngine extends Init 
{
	public static String testCaseName;
	@Test
	@Parameters({"testcase"})
	public void solo(String testcase) throws IOException, InterruptedException
	{
		testCaseName=testcase;
		String excelFileName = System.getProperty("user.dir") + Init.projectExcelPath;		
		String testCaseSheetName = "Client_" + Init.project;
		String locatorSheetName = Init.prop.getProperty("locatorSheetName");
		String inputSheetName = Init.prop.getProperty("inputSheetName");
		
		String[][] testCaseSheet = JavaLib.readExcelData(excelFileName, testCaseSheetName);		
		String[][] locatorSheet = JavaLib.readExcelData(excelFileName, locatorSheetName);
		String[][] inputSheet = JavaLib.readExcelData(excelFileName, inputSheetName);		
		
		String testScenarioName, testCaseName;
		String action = null;
		String locator = null;
		String locatorName = null;
		String inputData = null;
		String validation = null;
		String expected = "";
		String elementType = null;
		String stepDescription = null;
		Boolean testScenarioexe = false;
		Boolean testCaseexe = false;
		Boolean isExecute=false;
		
		
		for(int i=1;i<testCaseSheet.length;i++)
		{	
			//System.out.println(i);
			//System.out.println(testCaseSheet[i][4].toString());
			//System.out.println(testCaseSheet[i][5].toString());
			//System.out.println(testCaseSheet[i][7].toString());
			//System.out.println(testCaseSheet[i][8].toString());
			
			if(testcase.equalsIgnoreCase(testCaseSheet[i][4]))
			{
				isExecute=true;
			}
			
			if("".equalsIgnoreCase(testCaseSheet[i][6]) ) // || testCaseSheet[i][6].isEmpty()
			{
				isExecute=false;
				//break;
			}
			
			
			if("Y".equalsIgnoreCase(testCaseSheet[i][2]))
				testScenarioexe=true;
			
			if("Y".equalsIgnoreCase(testCaseSheet[i][5]))
				testCaseexe=true;
			
			if(testCaseSheet[i][6]=="")
			{
				testScenarioexe=false;
				testCaseexe=false;
				continue;
			}
			
			if(testcase.equalsIgnoreCase(testCaseSheet[i][4]) && testCaseSheet[i][5].equalsIgnoreCase("N"))
			{
				throw new SkipException("Skipping this excecution");
			}
			
			if(testCaseexe==true && isExecute==true)
			{
				stepDescription = testCaseSheet[i][6];
				action = testCaseSheet[i][7];				
				expected = testCaseSheet[i][10];
				
				for(int j=1;j<locatorSheet.length;j++)
				{
					//System.out.println(j);
					//System.out.println(locatorSheet[j][1].toString());
					//System.out.println(testCaseSheet[i][8].toString());
					
					if(testCaseSheet[i][8].equalsIgnoreCase(locatorSheet[j][1]))
					{
						elementType=locatorSheet[j][2];
						locator=locatorSheet[j][3];
						locatorName=locatorSheet[j][4];
						break;
					}
				}
				
				for(int k=1;k<inputSheet.length;k++)
				{
					//System.out.println(k);
					//System.out.println(inputSheet[k][1].toString());
					
					if(testCaseSheet[i][8].equalsIgnoreCase(inputSheet[k][1]))
					{
						inputData=inputSheet[k][2];	
						break;
					}
				}
				SeleniumLib.executeTestCase(action, locator, locatorName, elementType, inputData, validation, expected);
				
				if("Click".contains(action))
				{
					Thread.sleep(3000);
				}
				
				if(expected != null)
				{
					if(!"".contains(expected))
					{
						AssertionLib.verify(expected);
					}					
				}				
				Reporter.report(stepDescription,AssertionLib.resultValue, SeleniumLib.screenshotFilePath);
				
			}
			else
			{
				//break;
			}				
						
			AssertionLib.resultValue="";
			SeleniumLib.screenshotFilePath="";
			
		}		
	}
}
