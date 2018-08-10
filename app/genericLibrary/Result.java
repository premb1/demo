package com.eBMS.app.genericLibrary;

public class Result 
{
	private String stepDescription;
	private String resultPassFail;
	private String screenshotFilePath;
	
	public Result(String stepDescription, String resultPassFail, String screenshotFilePath)
	{
		this.stepDescription = stepDescription;
		this.resultPassFail = resultPassFail;
		this.screenshotFilePath = screenshotFilePath;
		
	}
	
	public String getStepDescription()
	{
		return stepDescription;				
	}
	
	public String getresultPassFail()
	{
		return resultPassFail;				
	}
	
	public String getScreenshotFilePath()
	{	
		return screenshotFilePath;				
	}

	
	
}
