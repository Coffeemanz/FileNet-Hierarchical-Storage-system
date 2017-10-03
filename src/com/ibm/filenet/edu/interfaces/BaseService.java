package com.ibm.filenet.edu.interfaces;

import java.util.ArrayList;
import java.util.List;

import javax.management.monitor.MonitorSettingException;

import com.filenet.api.collection.StringList;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.events.ObjectChangeEvent;
import com.ibm.filenet.edu.Exceptions.DocumentNotFoundException;
import com.ibm.filenet.edu.Exceptions.MoveToFolderException;

public interface BaseService {
	
	Document getDocument(ObjectChangeEvent event) throws DocumentNotFoundException;
	
	String getDocName(Document doc);
	
	ArrayList<String> getLevelAttribue(Document doc);
	
	String getRawTypeAttribute(Document doc);
	
	List<String> parseLevelAttribute(String attribute);
	
	void moveToAccessLayer(ArrayList levelAttribute, ObjectChangeEvent event, Document Doc) throws MoveToFolderException;
	
	void setAccess(List<String> levelAttribure, Document doc);
	
	public Folder checkFolderForType(String parentFolderPath, String type, ObjectChangeEvent event);
	
	void moveToFolder(Document doc, ObjectChangeEvent event) throws MoveToFolderException;

}
