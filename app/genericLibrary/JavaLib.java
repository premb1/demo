package com.eBMS.app.genericLibrary;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class JavaLib 
{	
	public static String[][] readExcelData(String fileName, String sheetName) throws IOException	
	//public static void main (String[] args) throws IOException	
	{
		String[][] excelData = null;		
		Workbook workBook = null;
		//String fileName=System.getProperty("user.dir")+"\\src\\com\\eBMS\\app\\testInput\\TestData.xlsx";
		//String sheetName="Client_BSSA";
		FileInputStream file = new FileInputStream(fileName);
		if (fileName.endsWith("xlsx")) 
		{
	        workBook = new XSSFWorkbook(file);
	    } 
		else if (fileName.endsWith("xls")) 
	    {
	        workBook = new HSSFWorkbook(file);
	    } 
	    else 
	    {
	        System.out.println("The specified file is not Excel file");
	    }
		
		Sheet sheet = workBook.getSheet(sheetName);
		Row row=sheet.getRow(0);
		int rowCount=sheet.getLastRowNum();
		int columnCount=row.getLastCellNum();
		
		excelData=new String[rowCount+1][columnCount];
		
		for(int i=0;i<=rowCount;i++)
		{
			row=sheet.getRow(i);
			while(sheet.getRow(i)==null) // To find and handle if the row is null
			{
				for(int j=0;j<columnCount;j++)
				{
					excelData[i][j]="";
					//System.out.println("-");
				}
				i++;
			}
			row=sheet.getRow(i);
			for(int j=0;j<columnCount;j++)
			{
				if(row.getCell(j)==null)
				{
					excelData[i][j]="";
					//System.out.println("-");
				}
				else
				{
					switch(row.getCell(j).getCellType())
					{
						case 4:
							//excelData[i][j]=row.getCell(j).getBooleanCellValue();
							//System.out.println(excelData[i][j]);
							break;
						case 0:
							//excelData[i][j]=row.getCell(j).getNumericCellValue();
							//System.out.println(excelData[i][j]);
							break;
						case 1:
							excelData[i][j]=row.getCell(j).getStringCellValue();
							//System.out.println(excelData[i][j]);
							break;
					}					
				}
			}
		}
		workBook.close();
        return excelData;
	}
	
	public static String getTodayDate()
	{
		String a = null;
		return a;
		
	}
        
}

