package com.ibm.filenet.edu.unnecessary;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;


public class TestFolderCreateEvent implements EventActionHandler{
	
	private static final String CLASS_NAME = TestFolderCreateEvent.class.getName();
	private static Logger logger = Logger.getLogger( CLASS_NAME );

	@Override
	public void onEvent(ObjectChangeEvent arg0, Id arg1)
			throws EngineRuntimeException {
		
		logger.info("1-----------------------------------------------------------1");
		logger.info("testing");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date = new Date();
		
		HSSFWorkbook wb = new HSSFWorkbook();
	    
//	    File fl = new File("/home/student1/workbook.xls");
//	    
//		fl.setReadable(true, false);
//		fl.setExecutable(true, false);
//		fl.setWritable(true, false);	
//		fl.getParentFile().mkdirs(); 
		
//		try
//		{
//		fl.createNewFile();
//		}
//		catch (IOException e)
//		{
//		System.out.println("CAN' WRITE TO FILE");
//		}
		
		
		logger.info("TRYING TO GET EXCEL PROPS:");
		
		
		FileOutputStream fo = null;
		try {
			 fo = new FileOutputStream("/test_workbook_" + date + ".xls");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		logger.info(fo.toString());
		
		Sheet sheet = wb.createSheet("new sheet");
		Row rowAttribute = sheet.createRow(0);
	    // Create a cell and put a value in it.
	    Cell cellAttribue = rowAttribute.createCell(0);
	    Cell cellValue = rowAttribute.createCell(1);
	    cellAttribue.setCellValue("Attributes:");
	    cellValue.setCellValue("Value:");
	    
	    // Create a cell and put a value in it.
	    Cell cellDocument = rowAttribute.createCell(3);
	    cellDocument.setCellValue("Documents:");
	    
	    
	    logger.info(sheet.getSheetName());
	    logger.info(cellAttribue.getStringCellValue());
	    logger.info(cellDocument.getStringCellValue());
	    
	    logger.info("2-----------------------------------------------------------2");
	    
	    try {
			wb.write(fo);
			wb.close();
			fo.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
//	   -------------------Here was the writing to the file--------------------
	    
	    
//	    try {
//			wb.write(fl);
////			fileOut.close();
//		} catch (IOException e) {
//			System.out.println("CAN'T CREATE FILE");
//		}
		
		
		


//			File f = new File("/home/student1/TEST_WORKBOOK.xls");
//			File f = new File("/KEKEKEKEKEKEK.xls");
//			f.setReadable(true, false);
//			f.setExecutable(true, false);
//			f.setWritable(true, false);	
//			f.getParentFile().mkdirs(); 
//
//		try
//		{
//		f.createNewFile();
//		}
//		catch (IOException e)
//		{
//		System.out.println("CAN' WRITE TO FILE");
//		}
			
	}
}
