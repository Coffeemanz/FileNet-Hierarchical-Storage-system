package com.ibm.filenet.edu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;



import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.ChoiceImpl;
import com.filenet.apiimpl.engine.ReservationlessCheckin;
import com.filenet.apiimpl.engine.ReservationlessCheckout;
import com.ibm.filenet.edu.interfaces.SearchService;

public class SearchServiceImpl implements SearchService {
	
	private static final String CLASS_NAME = SearchServiceImpl.class.getName();
	private static Logger logger = Logger.getLogger( CLASS_NAME );

	@Override
	public Document getDocument(ObjectChangeEvent event) {
		
		ObjectStore os = event.getObjectStore();
		Id id = event.get_SourceObjectId();
		Document doc = Factory.Document.fetchInstance(os, id, null);

		return doc;
	}

	@Override
	public String getDocName(Document doc) {
		
		String docName = "";
			
		if (doc.get_Name().trim().length() != 0)
		{
			docName = doc.get_Name();
		}
		else
		{
			docName = "New File";
		}
			
		return docName;
	}
	
	@Override
	public ArrayList<String> retrieveChoiceList(ObjectChangeEvent event, String choiceListName) {
		
		String s = "SELECT id, DisplayName from ChoiceList where DisplayName = " + "'" + choiceListName + "'";
		SearchSQL searchSQL = new SearchSQL(s);
		ArrayList<String> choiceList = new ArrayList<String>();
		
		SearchScope searchScope = new SearchScope(event.getObjectStore());
		RepositoryRowSet rowSet = searchScope.fetchRows(searchSQL, null, null, new Boolean(true));
		Iterator iter = rowSet.iterator();
		Id docId = null;
		RepositoryRow row = null ;
		

		while (iter.hasNext()) {
		    row = (RepositoryRow) iter.next();
		    docId = row.getProperties().get("Id").getIdValue();
		  }

		com.filenet.api.admin.ChoiceList list = Factory.ChoiceList.fetchInstance(event.getObjectStore(),docId, null);
		com.filenet.api.collection.ChoiceList choicelist = null;
		choicelist = list.get_ChoiceValues();
		Iterator i = choicelist.iterator();
		while (i.hasNext() ) {
		    com.filenet.apiimpl.core.ChoiceImpl choice = (ChoiceImpl) i.next();
		    choiceList.add(choice.get_DisplayName());
		  }
		
		return choiceList;
	}

	@Override
	public ArrayList<Property> checkAttributes(ArrayList<String> attributes,
			Document doc) {

		com.filenet.api.property.Properties pr = doc.getProperties();
		ArrayList<Property> properties = new ArrayList<Property>();
			
		Iterator it = pr.iterator();
		while (it.hasNext())
		{
			Property prop = (Property) it.next();
			if (attributes.contains(prop.getPropertyName()) && (prop.getState().toString().equals("VALUE")))
			{
				properties.add(prop);
			}

		}
		return properties;
	}

	@Override
	public String parsePropertyNames(ArrayList<Property> properties) {
		
		ArrayList<String> propNames = new ArrayList<String>();
		for (Property p: properties)
		{
			propNames.add(p.getPropertyName());
		}	
		propNames.add("DocumentTitle");
		String listOfNames = propNames.toString().substring(1, propNames.toString().length() - 1);
		
		return listOfNames;
	}

	@Override
	public String parseWhereCondition(ArrayList<Property> properties) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'07mmss'Z'");
		String whereCondition = "";
		
		if (properties.size() == 1)
		{

			if (properties.get(0).getPropertyName().equals("Att_CreateDate"))
			{
				whereCondition = "d." + properties.get(0).getPropertyName() + " >= "  + dateFormat.format(properties.get(0).getObjectValue());
				logger.info(whereCondition);
			}
			else
			{
				whereCondition = "d." + properties.get(0).getPropertyName() + " = " + "'" + properties.get(0).getObjectValue() + "'";
			}
			
		}
		else if (properties.size() > 1)
		{
			if (properties.get(0).getPropertyName().equals("Att_CreateDate"))
			{
				whereCondition = "d." + properties.get(0).getPropertyName() + " >= "  + dateFormat.format(properties.get(0).getObjectValue());
				logger.info(whereCondition);
			}
			else
			{
				whereCondition = "d." + properties.get(0).getPropertyName() + " = " + "'" + properties.get(0).getObjectValue() + "'";
			}
			for (int x = 1; x <= properties.size() - 1; x++)
			{
				if (properties.get(x).getPropertyName().equals("Att_CreateDate"))
				{
					whereCondition = whereCondition + " AND d." + properties.get(0).getPropertyName() + " >= "  + dateFormat.format(properties.get(0).getObjectValue());
					logger.info(whereCondition);
				}
				else
				{
					whereCondition = whereCondition + " AND d." + properties.get(x).getPropertyName() + " = " + "'" + properties.get(x).getObjectValue() + "'";
				}		
			}
		}
		return whereCondition;
	}

	@Override
	public ArrayList<String> search(ObjectChangeEvent event, Document doc, String choiceListName) {
		

		SearchScope search = new SearchScope(event.getObjectStore());
		SearchSQL searchSQL1 = new SearchSQL();
		
		ArrayList<String> attributes = retrieveChoiceList(event, choiceListName);
		ArrayList<Property> properties = checkAttributes(attributes, doc);
		
		logger.info(parseWhereCondition(properties));
		
		searchSQL1.setSelectList(parsePropertyNames(properties));
		searchSQL1.setFromClauseInitialValue("BasicDocument", "d", true);
		searchSQL1.setWhereClause(parseWhereCondition(properties));

		
		DocumentSet documents = (DocumentSet) search.fetchObjects(searchSQL1, Integer.valueOf("50"), null, Boolean.valueOf(true));
		
		Document doc1;
		Iterator docsIt = documents.iterator();
		
		ArrayList<String> documentsList = new ArrayList<String>();
		
		while (docsIt.hasNext())
		{
			doc1 = (Document) docsIt.next();
			documentsList.add(doc1.getProperties().getStringValue("DocumentTitle"));
		}
		
		return documentsList;
	}


	@Override
	public void createXLSFile(ArrayList<Property> properties,
			ArrayList<String> documentsList, ObjectChangeEvent event){

		Date date = new Date();
		
		HSSFWorkbook wb = new HSSFWorkbook();
	    FileOutputStream fileOut = null;
	    
	    String filePath = "/home/student1/docs/search_" + date +  ".xls";
	    String documentName = "search_" + date +  ".xls";
	    
		try {
			fileOut = new FileOutputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Sheet sheet = wb.createSheet("new sheet");

		if (properties.isEmpty())
		{
			Row rowAttribute = sheet.createRow(0);
		    Cell cell= rowAttribute.createCell(0);
		    cell.setCellValue("No attributes were chosen to search for.");
		    return;
		}
		else if (documentsList.isEmpty())
		{
			Row rowAttribute = sheet.createRow(0);
		    Cell cell= rowAttribute.createCell(0);
		    cell.setCellValue("No documents were found.");
		    return;
		}
		
		
		Row rowAttribute = sheet.createRow(0);

	    Cell cellAttribue = rowAttribute.createCell(0);
	    Cell cellValue = rowAttribute.createCell(1);
	    cellAttribue.setCellValue("Attributes:");
	    cellValue.setCellValue("Value:");
	    
	    Cell cellDocument = rowAttribute.createCell(3);
	    cellDocument.setCellValue("Documents:");
		
		
	    for (int y = 0; y <= properties.size() - 1; y++)
	    {
	    	Row row1 = sheet.createRow(y + 1);
	    	Cell cell = row1.createCell(0);
	    	Cell cell1 = row1.createCell(1);
	    	cell.setCellValue(properties.get(y).getPropertyName());
	    	cell1.setCellValue(properties.get(y).getObjectValue().toString());
	    }
	    
	    for (int y = 0; y <= documentsList.size() - 1; y++)
	    {
	    	System.out.println(documentsList.get(y));
	    	Row row1 = sheet.createRow(properties.size() + y + 1);
	    	Cell cell = row1.createCell(3);
	    	cell.setCellValue(documentsList.get(y));
	    }
  
	    try {
			wb.write(fileOut);
			wb.close();
			fileOut.close();
		} catch (IOException e) {
			System.out.println("CAN'T CREATE FILE");
		}
	    
		
//---------------------- ADDING CONTENT TO THE DOC ------------------------------------------
		
		Document doc = getDocument(event);
		Document reservation = (Document) doc.get_Reservation();
		
		logger.info("Reservation document: " + reservation.toString());

		File internalFile = new File(filePath);

		try {
		    ContentTransfer ctObject = Factory.ContentTransfer.createInstance();
		    FileInputStream fileIS = new FileInputStream(internalFile.getAbsolutePath());
		    @SuppressWarnings("deprecation")
			ContentElementList contentList = Factory.ContentTransfer.createList();
		    ctObject.setCaptureSource(fileIS);
		    ctObject.set_ContentType("application/vnd.ms-excel");
		    contentList.add(ctObject);
		    reservation.getProperties().putValue("DocumentTitle", documentName);
		    reservation.set_ContentElements(contentList);
		    reservation.save(RefreshMode.REFRESH);
		    }
		catch (Exception e)
		{
		    System.out.println(e.getMessage() );
		}

//---------------------- ADDING CONTENT TO THE DOC ------------------------------------------	
		return;
	}
}
