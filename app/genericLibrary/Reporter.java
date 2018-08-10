package com.eBMS.app.genericLibrary;


import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.eBMS.app.init.Init;

public class Reporter 
{
	private static List<Result> details;
	private static String projectNamePlaceHolder = "<!-- INSERT_PROJECT_NAME -->";
	private static String clientNamePlaceHolder = "<!-- INSERT_CLIENT_NAME -->";
	private static String resultPlaceHolder = "<!-- INSERT_RESULTS -->";
	private static String resultTemplateFile = System.getProperty("user.dir")+ Init.prop.getProperty("resultTemplateFile");
	private static String reportPath = System.getProperty("user.dir")+ Init.prop.getProperty("reportPath");
	
	public static void initialize()
	{
		details = new ArrayList<Result>();		
	}
	
	public static void report(String stepDescription, String resultPassFail, String screenshotFilePath)
	{
		Result result = new Result(stepDescription, resultPassFail, screenshotFilePath);
		details.add(result);
	}
	
	public static void writeResults()
	{
		try {
			String reportIn = new String(Files.readAllBytes(Paths.get(resultTemplateFile)));
			reportIn = reportIn.replaceFirst(projectNamePlaceHolder, Init.project);
			reportIn = reportIn.replaceFirst(clientNamePlaceHolder, Init.clientName);
			for (int i = 0; i < details.size();i++)
			{
				String screenShot ="";
				if(!"".contains(details.get(i).getScreenshotFilePath()))
				{
					screenShot="<a href=\"" + details.get(i).getScreenshotFilePath().replace("\\", "\\\\") + "\" target = \"_blank\">Click here</a>";
				}
				reportIn = reportIn.replaceFirst(resultPlaceHolder,"<tr><td>" + Integer.toString(i+1) + "</td><td>" + details.get(i).getStepDescription() + "</td><td>" + details.get(i).getresultPassFail() + "</td> <td></td> <td> " + screenShot + " </td></tr>" + resultPlaceHolder);
			}			
			String currentDate = new SimpleDateFormat("dd-MM-yyyy_HH-mm").format(new Date());
			String reportFileName = reportPath + Init.project + "report_" + currentDate + ".html";
			Files.write(Paths.get(reportFileName),reportIn.getBytes(),StandardOpenOption.CREATE);
			
		} catch (Exception e) {
			System.out.println("Error when writing report file:\n" + e.toString());
		}
	}
	
	

}
