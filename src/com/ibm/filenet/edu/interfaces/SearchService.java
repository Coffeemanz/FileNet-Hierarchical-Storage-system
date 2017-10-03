package com.ibm.filenet.edu.interfaces;

import java.util.ArrayList;

import com.filenet.api.core.Document;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.property.Property;

public interface SearchService {
	
	Document getDocument(ObjectChangeEvent event);
	
	String getDocName(Document doc);
	
	ArrayList<String> retrieveChoiceList(ObjectChangeEvent event, String choiceListName);
	
	ArrayList<Property> checkAttributes(ArrayList<String> attributes, Document doc);
	
	String parsePropertyNames(ArrayList<Property> properties);
	
	String parseWhereCondition(ArrayList<Property> properties);
	
	ArrayList<String> search(ObjectChangeEvent event, Document doc, String choiceListName);
	
	void createXLSFile(ArrayList<Property> properties, ArrayList<String> documentsList, ObjectChangeEvent event);

}
