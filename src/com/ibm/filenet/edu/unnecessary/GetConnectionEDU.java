package com.ibm.filenet.edu.unnecessary;

import java.util.*;
import javax.security.auth.Subject;

import com.filenet.api.collection.ObjectStoreSet;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.core.*;
import com.filenet.api.util.UserContext;

public class GetConnectionEDU {

	public Connection getConnection(String name, String password)
	{
		//String uri = "iiop://ccv01135:2809/FileNet/Engine";
		String uri = "http://ccv01135:9080/wsi/FNCEWS40MTOM"; 
		Connection conn = Factory.Connection.getConnection(uri);
try{

		
		//String stanza = "FileNetP8";
		Subject subject = UserContext.createSubject(conn, name, password, null);
		UserContext uc = UserContext.get();
		uc.pushSubject(subject);
		System.out.println("Got the connection");
		System.out.println("-------------------");
		
	}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return conn;
	}
	
	public Domain getDomainEDU (Connection conn)
	{
		String domainName = "P8Domain";
		Domain domain = Factory.Domain.fetchInstance(conn, domainName, null);
		System.out.println("Name ot the domain: " + domain.get_Name());
		System.out.println("-------------------");
		return domain;
	}
	
	@SuppressWarnings("unchecked")
	public void getObjectStoresEDU(Domain domain)
	{
		ObjectStoreSet osSet = domain.get_ObjectStores();
		ObjectStore store;
		Iterator iterator = osSet.iterator();
		Iterator<ObjectStore> osIter = iterator;
		System.out.println("All object stores: ");
		while (osIter.hasNext())
		{
			store = (ObjectStore)osIter.next();
			if ((store.getAccessAllowed().intValue() &
					AccessLevel.USE_OBJECT_STORE.getValue()) > 0)
				System.out.println(store.get_Name());
		}
		System.out.println("-------------------");
	}
	
	public ObjectStore getObjectStoreEDU (Domain domain, String objectStoreName)
	{
		ObjectStore store = null;
		
		try
		{
			store = Factory.ObjectStore.fetchInstance(domain, objectStoreName, null);
			System.out.println("Name of the ObjectStore: " + store.get_Name());
			System.out.println("-------------------");
			
		}
		catch (Exception e)
		{
			System.out.print("No such Object Store\n");
			e.printStackTrace();
		}
		return store;
	}
	
	
	
	
	public static void main(String[] args) {
		
		try
		{
			GetConnectionEDU edu = new GetConnectionEDU();
			Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
			Domain domain = edu.getDomainEDU(conn);
			edu.getObjectStoresEDU(domain);
			ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}

}
