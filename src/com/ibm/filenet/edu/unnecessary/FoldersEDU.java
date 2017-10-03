package com.ibm.filenet.edu.unnecessary;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.ReferentialContainmentRelationshipSet;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Containable;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.Property;
import com.filenet.api.security.*;

public class FoldersEDU extends GetConnectionEDU {
	
	public Folder getFolderEDU(ObjectStore store, String folderName)
	{
		Folder folder = null;
		try {
			folder = Factory.Folder.fetchInstance(store, folderName, null);
			folderName = folder.get_FolderName();
			System.out.println(folderName + " folder is retrived");
			System.out.println("-------------------");
		} catch (Exception e) {
			System.out.println("Smth is wrong");
			e.printStackTrace();
		}
		return folder;
	}
	
	public Folder createFolderEDU(ObjectStore store, String folderName)
	{
		Folder myFolder = Factory.Folder.createInstance(store, null);
		myFolder.set_FolderName(folderName);
		Folder rootFolder = store.get_RootFolder();
		myFolder.set_Parent(rootFolder);
		myFolder.save(RefreshMode.REFRESH);
		System.out.println(myFolder.get_Name() + " is created");
		System.out.println("-------------------");
		return myFolder;
	}
	
	public void createSubFolderEDU(Folder parentFolder, String folderName)
	{
		Folder subFolder = parentFolder.createSubFolder(folderName);
		subFolder.save(RefreshMode.REFRESH);
		System.out.println(subFolder.get_Name() + " is created");
		System.out.println("-------------------");
	}
	
	public void deleteFoldersEDU (ObjectStore store, String folderToDelete)
	{
		Folder folder = Factory.Folder.fetchInstance(store, folderToDelete, null);
		String folderName = folder.get_FolderName();
		folder.delete();
		folder.save(RefreshMode.REFRESH);
		System.out.println(folderName + " is deleted");
		System.out.println("-------------------");
	}
	
	public void getSubFoldersEDU (ObjectStore store, String parentFolder)
	{
		try {
			Folder folder  = Factory.Folder.fetchInstance(store, parentFolder, null);
			FolderSet subFolders = folder.get_SubFolders();
			Iterator it = subFolders.iterator();
			System.out.println("List of sub folders under the " + parentFolder + " folder: ");
			while (it.hasNext())
			{
				Folder retrieveFolder = (Folder) it.next();
				String name = retrieveFolder.get_FolderName();
				if (retrieveFolder.getProperties().getBooleanValue("IsHiddenContainer"))
				{
					System.out.println("Folder " + name + " is hidden");
					System.out.println("-");
				}
				else
				{
					System.out.println(name);
					System.out.println("-");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("-------------------");
	}
	
	public void getDocsInTheFolder (ObjectStore store, String parentFolder)
	{
		try {
			Folder folder = Factory.Folder.fetchInstance(store, parentFolder, null);
			DocumentSet documents = folder.get_ContainedDocuments();
			Iterator it = documents.iterator();
			System.out.println("List of documents under the " + parentFolder + " folder");
			while (it.hasNext())
			{
				Document retrieveDoc = (Document) it.next();
				String name = retrieveDoc.get_Name();
				System.out.println(name);
			}
			System.out.println("-------------------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getFolderContaineesEDU (ObjectStore store, String parentFolder)
	{
		Folder folder = Factory.Folder.fetchInstance(store, parentFolder, null);
		ReferentialContainmentRelationshipSet refConRelSet = folder.get_Containees();
		
		Iterator it = refConRelSet.iterator();
		System.out.println("List of objects in " + parentFolder + " folder:");
		while (it.hasNext())
		{
			try {
				System.out.println("------");
				ReferentialContainmentRelationship retrieveObject =
					(ReferentialContainmentRelationship) it.next();
				IndependentObject containee = retrieveObject.get_Head();
				String className = containee.getClassName();
				System.out.println("class = " + className);
				String displayName = retrieveObject.get_Name();
				System.out.println("display name = " + displayName);
				System.out.println("------");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
			
	}
	
	public void getPermissionsEDU()
	{
		GetConnectionEDU edu = new GetConnectionEDU();
		Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
		Domain domain = edu.getDomainEDU(conn);
		ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		
		Folder folderSec = getFolderEDU(object_store, "/TESTING/For departaments");
		
//		Document doc = Factory.Document.fetchInstance(object_store, "/TESTING/For everyone/test", null);
		
		AccessPermissionList permissions = folderSec.get_Permissions();
		Iterator it = permissions.iterator();
		while (it.hasNext())
		{
			AccessPermission permission =  (AccessPermission) it.next();
			System.out.println("Grantee name= " + permission.get_GranteeName());
			System.out.println("Grantee type= "+ permission.get_GranteeType().toString());
			System.out.println("Permission source= " + permission.get_PermissionSource().toString());
			System.out.println("Access level= " + AccessLevel.getInstanceFromInt(permission.get_AccessMask().intValue()));
			System.out.println("Access type= " + permission.get_AccessType().toString());
			System.out.println("Inheritable depth= " + permission.get_InheritableDepth());
			System.out.println("------------------------------");
		}
	}
	
	public void setFolderSecurity()
	{
		GetConnectionEDU edu = new GetConnectionEDU();
		Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
		Domain domain = edu.getDomainEDU(conn);
		ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		
//		Folder folderSec = getFolderEDU(object_store, "/TESTING/test security");
		
		Document doc = Factory.Document.fetchInstance(object_store, "/TESTING/test folder/MarketingPlan4.doc", null);
		
		AccessPermission permission = Factory.AccessPermission.createInstance();
		permission.set_GranteeName("Departament 1");
		permission.set_AccessType(AccessType.ALLOW);
		permission.set_InheritableDepth(new Integer(-1));
		permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT));
		
		AccessPermissionList permissions = doc.get_Permissions();
		permissions.add(permission);
		doc.set_Permissions(permissions);
		doc.save(RefreshMode.REFRESH);
		System.out.println("Doc security is set");
		
		permission.set_GranteeName("Departament 2");
		permission.set_AccessType(AccessType.DENY);
		permission.set_InheritableDepth(new Integer(-1));
		permission.set_AccessMask(new Integer(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT));
		
		AccessPermissionList permissions_1 = doc.get_Permissions();
		permissions_1.add(permission);
		doc.set_Permissions(permissions_1);
		doc.save(RefreshMode.REFRESH);
		System.out.println("Doc security is set");
		
		
	}
	
	
	public void moveFileToFolder()
	{
		GetConnectionEDU edu = new GetConnectionEDU();
		Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
		Domain domain = edu.getDomainEDU(conn);
		ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		
//		Folder folderToRetrieveFrom = getFolderEDU(object_store, "/TESTING/1");
		
		Document doc = Factory.Document.fetchInstance(object_store, "/TESTING/Incoming Documents/123", null);
		String docName = doc.get_Name();
		StringList level = doc.getProperties().getStringListValue("Level");
//		String type = doc.getProperties().getStringValue("Doctype");
//		System.out.println(docName + " is retrieved" + " " + level + " " + type);
		
		Iterator it = level.iterator();
		ArrayList<String> levelString = new ArrayList<String>();
		
		while (it.hasNext())
		{
			levelString.add((String) it.next());
		}
		
		for (String s: levelString)
		{
			System.out.println(s);
		}
		

		
		
//		com.filenet.api.property.Properties pr = doc.getProperties();
//		Property property;
//		Iterator it = pr.iterator();
//		while (it.hasNext())
//		{
//			property = (Property) it.next();
//			System.out.println(property);
//		}
		
//		Folder folderToMoveTo = Factory.Folder.fetchInstance(object_store, "/TESTING/2" , null);
//		
//		ReferentialContainmentRelationship rr = folderToMoveTo.file(doc, AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
//		rr.save(RefreshMode.REFRESH);
		
//		ClassDefinition ts= Factory.ClassDefinition.fetchInstance(object_store,"BasicDocument ", null); 
//		
//		Document newDoc = Factory.Document.createInstance(object_store, ts.get_SymbolicName()); 
//		
//		newDoc.getProperties().putValue("Header", "TESTING"); 
//		//newDoc.getProperties().putValue("ContainmentName", "Test22");
//		
//		ContentElementList s = Factory.ContentElement.createList(); 
//		
//		ContentTransfer cs = Factory.ContentTransfer.createInstance(object_store); 
//		
//		cs.set_ContentType("MIME::image/tiff"); 
//		
//		FileInputStream is = null;
//		try {
//			is = new FileInputStream("C:/readme.txt");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		
//		cs.setCaptureSource(is); 
//		
//		s.add(cs); 
//		
//		newDoc.set_ContentElements(s); 
//		
//		newDoc.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION); 
//		
//		
//		newDoc.save(RefreshMode.REFRESH); 
//		
//		String folderPath = "/TESTING/2"; 
//		Folder newFolder = Factory.Folder.fetchInstance(object_store, folderPath, null); 
		
//		 Files the document into the folder with the autoUniqueName parameter enabled, and the 
		
//		 defineSecurityParentage parameter set to inherit permissions from the folder. 
		
		
		
//		System.out.println(newDoc.get_Name());
//		ReferentialContainmentRelationship rr = newFolder.file(newDoc, AutoUniqueName.AUTO_UNIQUE, "test", DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
//		rr.save(RefreshMode.REFRESH);
	} 
	

	
	public static void main (String[] args)
	{
		try
		{
			

//			GetConnectionEDU con_edu = new GetConnectionEDU();
//			Connection conn =con_edu.getConnection("P8admin", "IBMFileNetP8");
//			Domain domain = con_edu.getDomainEDU(conn);
//			con_edu.getObjectStoresEDU(domain);
//			ObjectStore object_store = con_edu.getObjectStoreEDU(domain, "MyObjectStore");

			FoldersEDU fol_edu = new FoldersEDU();
//			fol_edu.getFolderEDU(object_store, "/Stest");
//			Folder parentFolder = fol_edu.createFolderEDU(object_store, "TESTING");
//			fol_edu.createSubFolderEDU(parentFolder, "SUB_TESTING");
			
//			fol_edu.deleteFoldersEDU(object_store, "/TESTING");
			
//			fol_edu.getSubFoldersEDU(object_store, "/TESTING");
	
//			fol_edu.getDocsInTheFolder(object_store, "/TESTING");
	
//			fol_edu.getFolderContaineesEDU(object_store, "/TESTING");
			
//			fol_edu.moveFileToFolder();
			
//			fol_edu.getPermissionsEDU();
			
			fol_edu.setFolderSecurity();

			}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
