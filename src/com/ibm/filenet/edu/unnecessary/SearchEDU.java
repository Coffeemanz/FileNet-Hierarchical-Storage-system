package com.ibm.filenet.edu.unnecessary;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.filenet.api.collection.ChoiceList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Factory.ReferentialContainmentRelationship;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.ChoiceImpl;

public class SearchEDU {

	public void searchDocumentEDU(ObjectStore os)
	{

		
		SearchScope search = new SearchScope(os);
		SearchSQL searchSQL = new SearchSQL();
		searchSQL.setSelectList("Type, DocumentTitle, Header");
		searchSQL.setFromClauseInitialValue("BasicDocument", "d", true);
		searchSQL.setWhereClause("d.Header = 'ser_test'");
		
		DocumentSet documents = (DocumentSet) search.fetchObjects(searchSQL, Integer.valueOf("50"), null, Boolean.valueOf(true));
		
		Document doc;
		Iterator docsIt = documents.iterator();
		
	
		while (docsIt.hasNext())
		{
			doc = (Document) docsIt.next();
			System.out.println("document: " + doc.getProperties().getStringValue("DocumentTitle"));
		}
	
	}
	
	
	public static void main(String[] args) {
		
		GetConnectionEDU edu = new GetConnectionEDU();
		Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
		Domain domain = edu.getDomainEDU(conn);
		ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		
		 String s = "SELECT id, DisplayName from ChoiceList where DisplayName = 'Access Level'";
		    SearchSQL searchSQL = new SearchSQL(s);

		    SearchScope searchScope = new SearchScope(object_store);
		    RepositoryRowSet rowSet = searchScope.fetchRows(searchSQL, null, null, new Boolean(true));
		    Iterator iter = rowSet.iterator();
		    Id docId = null;
		    RepositoryRow row = null ;

		    while (iter.hasNext()) {
		        row = (RepositoryRow) iter.next();
		        docId = row.getProperties().get("Id").getIdValue();
		        String DisplayName = row.getProperties().getStringValue("DisplayName");

		        System.out.println(" Id=" + docId.toString());
		        System.out.println(" DisplayName=" + DisplayName);

		    }

		    com.filenet.api.admin.ChoiceList list = Factory.ChoiceList.fetchInstance(object_store,docId, null);
		    com.filenet.api.collection.ChoiceList choicelist = null;
		    choicelist = list.get_ChoiceValues();
		    Iterator i = choicelist.iterator();
		    while (i.hasNext() ) {
		        com.filenet.apiimpl.core.ChoiceImpl choice = (ChoiceImpl) i.next();
		        System.out.println(choice.get_DisplayName());
		    }
		
		
		
//		ArrayList<String> choice = new ArrayList<String>();
//		choice.add("Type");
//		choice.add("DocumentTitle");
//		
////		SearchEDU searchEDU = new SearchEDU();
////		searchEDU.searchDocumentEDU(object_store);
//		
//		Document doc = Factory.Document.fetchInstance(object_store, "/TESTING/Incoming documents/MarketingPlan5(1).doc", null);
//		com.filenet.api.property.Properties pr = doc.getProperties();
//		
//		Iterator it = pr.iterator();
//		while (it.hasNext())
//		{
//			Property prop = (Property) it.next();
//			if (choice.contains(prop.getPropertyName()) && (prop.getObjectValue() != null))
//			{
//				System.out.println("----------------");
//				System.out.println(prop.getPropertyName()+ ": " + prop.getObjectValue());
//				System.out.println("----------------");
//				
//			}
////			System.out.println(prop.getPropertyName()+ ": " + prop.getObjectValue());
//
//		}
		
		

	}

}
