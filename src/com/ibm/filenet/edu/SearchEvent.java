package com.ibm.filenet.edu;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;
import com.filenet.api.core.Document;
import com.filenet.api.property.Property;

public class SearchEvent implements EventActionHandler{
	
	private static final String CLASS_NAME = SearchEvent.class.getName();
	private static Logger logger = Logger.getLogger( CLASS_NAME );

	@Override
	public void onEvent(ObjectChangeEvent event, Id subId)
			throws EngineRuntimeException {
		
		SearchServiceImpl service = new SearchServiceImpl();
		
		Document doc = service.getDocument(event);
			
		try {
			logger.info("------------------------------------------------------------");
			logger.info("Loger search event");
			logger.info("Checking code module");
			logger.info("Event: " + event.getClassName() );
			logger.info(doc.get_Name());
			logger.info("try to load searchimpl class");
		} catch (Exception e) {
			logger.throwing(CLASS_NAME, "onEvent", e);
		}
		
		ArrayList<String> choiceList = service.retrieveChoiceList(event, "Attributes");
		ArrayList<Property> properties = service.checkAttributes(choiceList, doc);
		ArrayList<String> listOfDocuments = service.search(event, doc, "Attributes");
		service.createXLSFile(properties, listOfDocuments, event);

		

	}

}
