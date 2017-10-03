package com.ibm.filenet.edu.unnecessary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.DocumentSet;
import com.filenet.api.collection.FolderSet;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.ibm.filenet.edu.ServiceImpl;
import com.ibm.filenet.edu.Constans.Constans;
import com.filenet.api.core.Folder;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.filenet.apiimpl.core.ChoiceImpl;

public class Test {
	
	
	public static void main(String[] args) {
		
		
		//Connect and retrieve a document
		
		GetConnectionEDU edu = new GetConnectionEDU();
		Connection conn = edu.getConnection("P8admin", "IBMFileNetP8");
		Domain domain = edu.getDomainEDU(conn);
		ObjectStore object_store = edu.getObjectStoreEDU(domain, "MyObjectStore");
		
		Document doc = Factory.Document.fetchInstance(object_store, "/TESTING/Searches/search_Mon Sep 04 014350 PDT 2017.xls", null);
		
		

	}

}
