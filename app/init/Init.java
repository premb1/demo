package com.eBMS.app.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import com.eBMS.app.genericLibrary.JavaLib;
import com.eBMS.app.genericLibrary.Reporter;


public class Init
{
	public Init()
	{
		try 
		{
			Init.initProperty();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		Reporter.initialize();
	}	
	
	public static String project;
	public static String clientName;
	public static String projectExcelPath;
	public static String projectURL;
	public static Properties prop;
	public static WebDriver driver;
	@BeforeSuite
	public static void initProperty() throws IOException
	{
		String propFile = System.getProperty("user.dir") + "\\src\\com\\eBMS\\app\\config\\config.properties";
		File fileObject = new File(propFile);
		FileInputStream fileInput = new FileInputStream(fileObject);
		prop = new Properties();
		prop.load(fileInput);				
	}
	
	@BeforeSuite
	public static void initWebDriverMethod() throws InterruptedException
	{
		String browser = Init.prop.getProperty("browser");
		if(browser.equalsIgnoreCase("FF"))
		{
			driver=new FirefoxDriver();
		}
		else if(browser.equalsIgnoreCase("IE"))
		{
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\driver\\IEDriverServer.exe");
			
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			caps.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			driver = new InternetExplorerDriver(caps);
						
			//driver=new InternetExplorerDriver();
			
		}
		else if(browser.equalsIgnoreCase("CHROME"))
		{
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\driver\\chromedriver.exe");
			driver=new ChromeDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	@BeforeSuite
	public static void initReport()
	{
		
	}
	
	
	public static boolean isNewWindowPresent(Set <String> handles)
	{
		if (handles.size() >= 2)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	@BeforeSuite
	@Parameters("project")
	public void appExecutor(String project) throws IOException
	{	
		System.out.println(System.getProperty("user.dir"));
		System.out.println(Init.prop.getProperty("masterExcelFileName"));
		String masterExcelFileName = System.getProperty("user.dir") + Init.prop.getProperty("masterExcelFileName");
		String masterExecutor = Init.prop.getProperty("masterExecutor");
		String[][] masterExecutorSheet = JavaLib.readExcelData(masterExcelFileName, masterExecutor);
						
		for(int a=1;a<masterExecutorSheet.length;a++)
		{			
			if(project.equalsIgnoreCase(masterExecutorSheet[a][2]))
			{
				clientName = masterExecutorSheet[a][1];
				this.project = masterExecutorSheet[a][2];
				projectExcelPath = masterExecutorSheet[a][3];
				projectURL = masterExecutorSheet[a][4];
				break;
			}
		}
	}
	
	
	@BeforeTest
	public static void goHomePage()
	{
		if(driver.findElements(By.cssSelector("body > table > tbody > tr > td > form > table > tbody > tr > td > a > span")).size()>0)
		{
			driver.findElement(By.cssSelector("body > table > tbody > tr > td > form > table > tbody > tr > td > a > span")).click();
		}	
	}
	
	@AfterSuite
	public static void tearDown() throws InterruptedException
	{
		Reporter.writeResults();
		Thread.sleep(5000);
		driver.close();
		driver.quit();
	}
	
}
