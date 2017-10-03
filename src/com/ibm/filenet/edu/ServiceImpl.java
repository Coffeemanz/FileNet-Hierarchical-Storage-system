package com.ibm.filenet.edu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
import com.ibm.filenet.edu.Constans.Constans;
import com.ibm.filenet.edu.Exceptions.DocumentNotFoundException;
import com.ibm.filenet.edu.Exceptions.MoveToFolderException;
import com.ibm.filenet.edu.interfaces.BaseService;

public class ServiceImpl implements BaseService {
	
	public Document getDocument(ObjectChangeEvent event) throws DocumentNotFoundException {
	
		ObjectStore os = event.getObjectStore();
		Id id = event.get_SourceObjectId();
		Document doc = Factory.Document.fetchInstance(os, id, null);

		return doc;
	}
	
	
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
	
	
	public ArrayList<String> getLevelAttribue(Document doc) {
			
		StringList level = doc.getProperties().getStringListValue("Level");
		Iterator it = level.iterator();
		ArrayList<String> levelStrings = new ArrayList<String>();
			
		while(it.hasNext())
		{
			levelStrings.add((String) it.next());
		}
			
		return levelStrings;
	}
		
	public String getRawTypeAttribute(Document doc)
	{
		String type = doc.getProperties().getStringValue("Type");
			
		return type;
	}
		
	
	public List<String> parseLevelAttribute(String attribute)
	{
		String[] parsedAttr = attribute.split("_");
		List<String> attr = Arrays.asList(parsedAttr);
			
		return attr;
	}
		
	
	public void moveToAccessLayer(ArrayList levelAttribute, ObjectChangeEvent event, Document doc) throws MoveToFolderException{
			
		ObjectStore os = event.getObjectStore();
		String folderPath = "";
		Folder folderToMoveTo = null;
		String rawType = getRawTypeAttribute(doc);
		String docName = getDocName(doc);
			
			
		if (levelAttribute.contains(Constans.FOR_EVERYONE) && levelAttribute.contains(Constans.CONFIDENTIAL))
		{
				
			folderPath = Constans.CONFIDENTIAL_DIRECTORY;
			folderToMoveTo = checkFolderForType(folderPath, rawType, event);
					
			if (event.getClassName().equalsIgnoreCase("CreationEvent"))
			{
					ReferentialContainmentRelationship rr = folderToMoveTo.file(doc, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
					rr.save(RefreshMode.REFRESH);
			}
			return;
		}
			
		else if (levelAttribute.contains(Constans.FOR_EVERYONE)) 
		{
				
			folderPath = Constans.FOR_EVERYONE_DIRECTORY;
			folderToMoveTo = checkFolderForType(folderPath, rawType, event);
				
			if (event.getClassName().equalsIgnoreCase("CreationEvent"))
			{
					ReferentialContainmentRelationship rr = folderToMoveTo.file(doc, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
					rr.save(RefreshMode.REFRESH);
			}
			return;
		}
		else if (levelAttribute.contains(Constans.CONFIDENTIAL))
		{
				
			folderPath = Constans.CONFIDENTIAL_DIRECTORY;
			folderToMoveTo = checkFolderForType(folderPath, rawType, event);
				
			if (event.getClassName().equalsIgnoreCase("CreationEvent"))
			{
					ReferentialContainmentRelationship rr = folderToMoveTo.file(doc, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
					rr.save(RefreshMode.REFRESH);
			}
			return;
		}	
	}
		
	
	public void setAccess(List<String> levelAttribure, Document doc) {
			
		AccessPermission permission = Factory.AccessPermission.createInstance();
		permission.set_GranteeName(levelAttribure.get(0) + " " + levelAttribure.get(1));
			
		if (levelAttribure.get(2).equals(Constans.DENY))
		{
			permission.set_AccessType(AccessType.DENY);
			permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT));
		}
		else if (levelAttribure.get(2).equals(Constans.READ))
		{
			permission.set_AccessType(AccessType.ALLOW);
			permission.set_AccessMask(new Integer(AccessLevel.VIEW_AS_INT));
		}
		else if (levelAttribure.get(2).equals(Constans.FULL))
		{
			permission.set_AccessType(AccessType.ALLOW);
			permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT));
		}
		permission.set_InheritableDepth(new Integer(-1));
	
			
		AccessPermissionList permissions = doc.get_Permissions();
		permissions.add(permission);
		doc.set_Permissions(permissions);
		doc.save(RefreshMode.REFRESH);
		
	}
		
	public Folder checkFolderForType(String parentFolderPath, String type, ObjectChangeEvent event)
	{
		Folder parentFolder = Factory.Folder.fetchInstance(event.getObjectStore(), parentFolderPath, null);
		FolderSet subFolders = parentFolder.get_SubFolders();
		Iterator it = subFolders.iterator();
		ArrayList<String> subFolderNames = new ArrayList<String>();
			
		while(it.hasNext())
		{
			Folder retrieveFolder = (Folder) it.next();
			String name = retrieveFolder.get_FolderName();
			subFolderNames.add(name);
		}
			
		if (subFolderNames.contains(type))
		{
			Folder subFolder = Factory.Folder.fetchInstance(event.getObjectStore(), parentFolderPath + "/" + type, null);
			return subFolder;
		}
		else 
		{
			Folder subFolder = parentFolder.createSubFolder(type);
			subFolder.save(RefreshMode.REFRESH);
			return subFolder;
		}
	}
	
	public void moveToFolder(Document doc, ObjectChangeEvent event) throws MoveToFolderException{
			
		String rawType = getRawTypeAttribute(doc);
		String docName = getDocName(doc);
			
		String folderPath = Constans.FOR_DEPARTAMENTS_DIRECTORY;
		Folder folderToMoveTo = checkFolderForType(folderPath, rawType, event);
			
		ReferentialContainmentRelationship rr = folderToMoveTo.file(doc, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rr.save(RefreshMode.REFRESH);	
	}
}
	
