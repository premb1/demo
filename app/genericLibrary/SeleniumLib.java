package com.eBMS.app.genericLibrary;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;



import org.apache.commons.io.FileUtils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.eBMS.app.init.Init;
import com.eBMS.app.testEngine.TestCaseEngine;


public class SeleniumLib extends Init
{
	static String locatorNameSel;
	static String locatorSel;
	static String elementTypeSel;
	static String inputDataSel;
	static WebElement webElement;
	static String parentWindow;
	static int downTime;
	static int errScreenCount=0;
	public static String screenshotFilePath="";
	
	private static WebElement getElement()
	{
		try
		{
			if(locatorSel.equalsIgnoreCase("ID"))
			{
				webElement = driver.findElement(By.id(locatorNameSel));
				return webElement;
			}
			else if(locatorSel.equalsIgnoreCase("NAME"))
			{
				webElement = driver.findElement(By.name(locatorNameSel));
				return webElement;
			}
			else if(locatorSel.equalsIgnoreCase("LINKTEXT"))
			{
				webElement = driver.findElement(By.linkText(locatorNameSel));
				return webElement;
			}
			else if(locatorSel.equalsIgnoreCase("XPATH"))
			{
				webElement = driver.findElement(By.xpath(locatorNameSel));
				return webElement;
			}
			else if(locatorSel.equalsIgnoreCase("CSS"))
			{
				webElement = driver.findElement(By.cssSelector(locatorNameSel));
				return webElement;
			}
			else if(locatorSel.equalsIgnoreCase("classname"))
			{
				webElement = driver.findElement(By.className(locatorNameSel));
				return webElement;
			}
			
			
			
			
		}
		catch(Exception e)
		{
			SeleniumLib.takeScreenshot();			
		}
		return null;
		
	}
	
	private static void getLaunchPage()
	{
		
		driver.get(Init.projectURL);
		
	}
	
	private static void getAJAXValue() throws InterruptedException 
	{
		WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='tblResult']/tbody/tr[2]")));		
		WebElement ajaxResultTable = driver.findElement(By.id("tblResult"));
		List<WebElement> ajaxRows = ajaxResultTable.findElements(By.tagName("tr"));
		//System.out.println(ajaxRows.size());
		for(int i=2;i<ajaxRows.size()-1;i++)
		{
			List<WebElement> ajaxRowData =ajaxRows.get(i).findElements(By.tagName("td"));
			//System.out.println(ajaxRowData.size());
			for(int j=0;j<ajaxRowData.size();j++)
			{
				if(ajaxRowData.get(j).getText().equalsIgnoreCase(inputDataSel))
				{
					ajaxRowData.get(j).click();
					break;
				}
			}		
		//	SeleniumLib.getElement().sendKeys(Keys.TAB);    Solomon 12-Oct-2016
		}
		
	}
	
	private static void enterValue() throws InterruptedException 
	{	
		Thread.sleep(3000);
		SeleniumLib.getElement().clear();
		SeleniumLib.getElement().sendKeys(inputDataSel);
		if(elementTypeSel.equalsIgnoreCase("AJAX"))
		{			
			SeleniumLib.getAJAXValue();			
		}
	}
	
	private static void keyPress()
	{
		if(elementTypeSel.equalsIgnoreCase("TAB"))
		{
			webElement.sendKeys(Keys.TAB);
		}		
	}
	
	private static void switchToFrameByName()
	{
		try 
		{
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WebElement frames = SeleniumLib.getElement();
		driver.switchTo().frame(frames);
	}
	
	private static void switchToFrameByName(String name)
	{
		try 
		{
			Thread.sleep(2000);
		} 
		catch (InterruptedException e) 
		{			
			e.printStackTrace();
		}
		WebElement frames = driver.findElement(By.name(name));
		driver.switchTo().frame(frames);		
	}
	
	private static void switchToLastWindow()
	{
		parentWindow=driver.getWindowHandle();
		Set<String> winHandles=driver.getWindowHandles();
		for(String win:winHandles)
		{
			driver.switchTo().window(win);
		}
	}
	
	private static void switchToParentWindow()
	{
		driver.switchTo().window(parentWindow);
	}
	
	private static void comboBoxSelection()
	{
		Select comboBox = new Select(SeleniumLib.getElement());
		comboBox.selectByVisibleText(inputDataSel);
	}
	
	private static void radioButtonSelection()
	{
		List<WebElement> radioButton=driver.findElements(By.name(locatorNameSel));
		for(WebElement radio : radioButton)
		{
			if(inputDataSel.equalsIgnoreCase(radio.getAttribute("value")))
			{
				radio.click();
			}			
		}
	}
	
	private static void selectSpiffyCalendar() throws InterruptedException
	{
		SeleniumLib.clickElement();
		String inputDate = inputDataSel;
		String day = null;
		String[] date = inputDate.split("-", 3);		
		Select comboBoxMonth = new Select(driver.findElement(By.name("cboMonth")));
		comboBoxMonth.selectByVisibleText(date[1]);		
		Select comboBoxYear = new Select(driver.findElement(By.name("cboYear")));
		comboBoxYear.selectByVisibleText(date[2]);		
		if(date[0].startsWith("0"))
			day=date[0].substring(1);
		else
			day=date[0];
		
		WebDriverWait wait = new WebDriverWait(driver, 60);
		WebElement spiffyDatesWebTable;
		if(driver.findElements(By.xpath("//*[@id='spiffycalendar']/table/form")).size()>0)
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='spiffycalendar']/table/form")));
			spiffyDatesWebTable = driver.findElement(By.xpath("//*[@id='spiffycalendar']/table/form"));
		}
		else
		{
			wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='spiffycalendar']/form/table")));
			spiffyDatesWebTable = driver.findElement(By.xpath("//*[@id='spiffycalendar']/form/table"));
		}
		List<WebElement> spiffyDatesRows = spiffyDatesWebTable.findElements(By.tagName("tr"));
		//System.out.println(spiffyDatesRows.size());
		for(int i=1;i<spiffyDatesRows.size();i++)
		{						
			List<WebElement> spiffyDatesRowData =spiffyDatesRows.get(i).findElements(By.tagName("td"));
			//System.out.println(spiffyDatesRowData.size());
			for(int j=0;j<spiffyDatesRowData.size();j++)
			{
				//System.out.println(spiffyDatesRowData.get(j).getText());
				if(spiffyDatesRowData.get(j).getText().equalsIgnoreCase(day))
				{
					spiffyDatesRowData.get(j).click();
					break;
				}
			}					
		}
	}
	
	private static void selectHTMLCalendar() throws InterruptedException
	{
		SeleniumLib.clickElement();
		//driver.findElement(By.xpath("//input[@"+locatorSel+"='"+locatorNameSel+"']/following-sibling::input")).click();
		//driver.findElement(By.cssSelector("body > table > tbody > tr > td > form > table:nth-child(2) > tbody > tr:nth-child(3) > td:nth-child(3) > input.getValue")).click();
		Thread.sleep(3000);
		SeleniumLib.switchToLastWindow();
		//System.out.println(driver.getTitle());
		String inputDate = inputDataSel;
		String day = null;
		String[] date = inputDate.split("-", 3);
		if(date[0].startsWith("0"))
			day=date[0].substring(1);
		else
			day=date[0];
		
		Select comboBoxMonth = new Select(driver.findElement(By.name("Month")));
		List<WebElement> optionsList = comboBoxMonth.getOptions();
		for(WebElement option:optionsList)
		{
			if(option.getText().startsWith(date[1]))
			{
				comboBoxMonth.selectByVisibleText(option.getText());
				break;
			}
		}
			
		Select comboBoxYear = new Select(driver.findElement(By.name("Year")));
		comboBoxYear.selectByVisibleText(date[2]);		
		
		List<WebElement> calendarDates = driver.findElements(By.tagName("a"));
		for(WebElement calendarDate:calendarDates)
		{
			System.out.println(calendarDate.getText());
			if(calendarDate.getText().equals(day))
			{
				calendarDate.click();
				break;
			}
		}
		
		SeleniumLib.switchToParentWindow();
		SeleniumLib.switchToFrameByName("RightFrame");		
	}
	
	public static void takeScreenshot()
	{
		
		errScreenCount++;
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try 
		{
			Date today = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
			String folderName = dateFormat.format(today);	
			String time = timeFormat.format(today);
			//System.out.println(folderName);
			//System.out.println(time);
			screenshotFilePath = System.getProperty("user.dir")+"\\screenshots\\"+folderName+"\\"+TestCaseEngine.testCaseName +"_"+ errScreenCount +"_"+ time +".jpg";
			//System.out.println("SeleniumLib variabele: "+screenshotFilePath);
			FileUtils.copyFile(scrFile, new File(screenshotFilePath));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
	}
	
	
	private static void sleep() throws InterruptedException
	{
		Thread.sleep(downTime);
		AssertionLib.resultValue="Pass";
	}
	
	private static void setAppDownTime(String str)
	{
		downTime=Integer.parseInt(str.substring(6, str.length()-1))*1000;		
	}
	
	private static void handleAlertAccept() throws InterruptedException
	{
		Thread.sleep(5000);
		Alert alert=driver.switchTo().alert();
		alert.accept();
		
		//alert=driver.switchTo().alert();
		//alert.accept();
		
	}
	
	private static void selectCheckBox()
	{
		if ( !SeleniumLib.getElement().isSelected() )
		{
			SeleniumLib.getElement().click();
		}
	}
	
	private static void clickElement() throws InterruptedException
	{
		try
		{
			SeleniumLib.getElement().click();
			Thread.sleep(1000);
		}
		catch(UnhandledAlertException f)
		{
			System.out.println("solo");
			SeleniumLib.handleAlertAccept();
		}
		catch(ElementNotVisibleException f)
		{
			Actions builder = new Actions(driver);
			builder.moveToElement(SeleniumLib.getElement()).click(SeleniumLib.getElement()).perform();
		}
		
		
		//catch(Exception e)
		//{
			//AssertionLib.verify("hi.. This is unable to click element exception");
		//}
		
	}
	
	private static void closeCurrentWindow()
	{
		driver.close();
	}
	
	private static void solo()
	{
		driver.get("http://192.168.1.66/bssa/agencyindexpage.asp");
	}
	public static void globalSearch() throws InterruptedException
	{
		Thread.sleep(5000);
		driver.findElement(By.className("form-control")).click();
		Thread.sleep(1000);
		driver.findElement(By.id("GlobalSearch")).sendKeys(inputDataSel);
		Thread.sleep(3000);
		
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"GlobalResult\"]/a/span")));
		
		WebElement searchDiv = driver.findElement(By.xpath("//*[@id=\"GlobalResult\"]/a/span"));
		List<WebElement> rows = searchDiv.findElements(By.tagName("tr"));
		for(WebElement row:rows)
		{
			System.out.println(inputDataSel);
			System.out.println(row.findElement(By.partialLinkText(inputDataSel)).getText());
			if(inputDataSel.equalsIgnoreCase(row.findElement(By.partialLinkText(inputDataSel)).getText().trim()))
			{
				row.click();
			}			
		}
	}
	public static void executeTestCase(String action, String locator, String locatorName, String elementType, String inputData, String validation, String expected) throws InterruptedException
	{
		locatorSel=locator;
		locatorNameSel=locatorName;
		elementTypeSel=elementType;
		inputDataSel=inputData;
		
		if(action.contains("Sleep"))
		{
			SeleniumLib.setAppDownTime(action);	
			action="Sleep";
		}
		
		if(action.contains("put") || action.contains("get"))
		{
			
		}

		switch (action)
		{
			case "LaunchPage":
				SeleniumLib.getLaunchPage();
				break;
			case "Enter":
				SeleniumLib.enterValue();
				break;
			case "Click":
				SeleniumLib.clickElement();
				break;
			case "switchToFrameByName":
				SeleniumLib.switchToFrameByName();
				break;
			case "alertAccept":
				SeleniumLib.handleAlertAccept();
				break;
			case "Sleep":
				SeleniumLib.sleep();
				break;
			case "Validate":
				AssertionLib.validate(expected);
				break;
			case "SelectCombo":
				SeleniumLib.comboBoxSelection();
				break;
			case "SelectRadio":
				SeleniumLib.radioButtonSelection();
				break;
			case "SelectCheckBox":
				SeleniumLib.selectCheckBox();
				break;
			case "switchToLastWindow":
				SeleniumLib.switchToLastWindow();
				break;
			case "KeyPress":
				SeleniumLib.keyPress();
				break;
			case "switchToParentWindow":
				SeleniumLib.switchToParentWindow();
				break;
			case "SelectSpiffyCalendar":
				SeleniumLib.selectSpiffyCalendar();
				break;
			case "SelectHTMLCalendar":
				SeleniumLib.selectHTMLCalendar();
				break;
			case "closeCurrentWindow":
				SeleniumLib.closeCurrentWindow();
				break;
			case "solo":
				SeleniumLib.solo();
				break;
			case "put":
				break;
			case "GlobalSearch":
				SeleniumLib.globalSearch();
				break;
		}		
	}
}
