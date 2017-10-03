package com.ibm.filenet.edu.unnecessary;

import java.io.*;
import java.util.*;

import com.filenet.api.core.Document;
import com.filenet.api.engine.EventActionHandler;
import com.filenet.api.events.ObjectChangeEvent;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.util.Id;

public class EventActionEDU implements EventActionHandler {

	public void writeToLogFile (String message) throws IOException
	{
		File outputFile = new File("EventLog.txt");
		FileWriter out = new FileWriter(outputFile, true);
		out.write(message);
		out.close();
	}
	
	@Override
	public void onEvent(ObjectChangeEvent event, Id subId)
			throws EngineRuntimeException {
				try
				{
					Document doc = (Document) event.get_SourceObject();
					if (event.getClassName().equalsIgnoreCase("CreationEvent"))
					{
						writeToLogFile("In log event");
						writeToLogFile(event.get_Creator()+ "\r\n");
						writeToLogFile(event.get_Name() + "\r\n");
						writeToLogFile(event.get_Owner() + "\r\n");
						writeToLogFile(event.toString() + "\r\n");
						writeToLogFile(event.get_EventStatus() + "\r\n");
						
					}
				}
				
				catch (IOException e)
				{
					throw new EngineRuntimeException (ExceptionCode.E_FAILED, e);
				}
		
	}
}
