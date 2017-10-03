package com.ibm.filenet.edu.Exceptions;

public class MoveToFolderException extends Exception {
	
	public MoveToFolderException() {
    }

    public MoveToFolderException(String message) {
        super(message);
    }

    public MoveToFolderException(String message, Throwable cause) {
        super(message, cause);
    }

    public MoveToFolderException(Throwable cause) {
        super(cause);
    }

}
