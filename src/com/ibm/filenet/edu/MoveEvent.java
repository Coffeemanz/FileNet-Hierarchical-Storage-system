package com.ibm.filenet.edu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentElement;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.DynamicReferentialContainmentRelationship;
import com.filenet.api.core.Folder;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.Event;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.security.AccessPermission;
import com.filenet.api.util.Id;
import com.ibm.filenet.edu.ServiceImpl;
import com.ibm.filenet.edu.Constans.Constans;
import com.ibm.filenet.edu.Exceptions.DocumentNotFoundException;
import com.ibm.filenet.edu.Exceptions.MoveToFolderException;
import com.ibm.filenet.edu.interfaces.BaseService;

public class MoveEvent implements EventActionHandler {
	
	private static final String CLASS_NAME = MoveEvent.class.getName();
	private static Logger logger = Logger.getLogger( CLASS_NAME );
	
	@Override
	public void onEvent(ObjectChangeEvent event, Id subId)
			throws EngineRuntimeException {
		
		
		ServiceImpl service = new ServiceImpl();
		Document doc = null;
		
		try
		{
			doc = service.getDocument(event);
		}
		catch (DocumentNotFoundException e)
		{
			System.out.println("Document not found");
		}
		
		ArrayList<String> levelAttribute = service.getLevelAttribue(doc);

		try 
		{
			if (levelAttribute.contains(Constans.CONFIDENTIAL) || levelAttribute.contains(Constans.FOR_EVERYONE))
			{
				service.moveToAccessLayer(levelAttribute, event, doc);
				return;
			}
		}
		catch (MoveToFolderException e)
		{
			System.out.println("Error occured while moving to the access layer");
		}
		
		
		for (String s: levelAttribute)
		{
			List<String> attribute = service.parseLevelAttribute(s);
			service.setAccess(attribute, doc);
		}
		
		try
		{
			service.moveToFolder(doc, event);
		}
		catch (MoveToFolderException e)
		{
			System.out.println("Error occured while moving to the folder");
		}
		
		return;		
	}
}	
	
	
	
	
	
	
	
	
	
	
	
	
