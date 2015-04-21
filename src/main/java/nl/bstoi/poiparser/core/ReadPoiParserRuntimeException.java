package nl.bstoi.poiparser.core;

public class ReadPoiParserRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 5623047832515069238L;
	private final static String DEFAULT_MESSAGE = "Error while reading excel file";
	
	public ReadPoiParserRuntimeException(final String message) {
		super(message);
	}
	
	public ReadPoiParserRuntimeException() {
		super(DEFAULT_MESSAGE);
	}

}
