package nl.bstoi.poiparser.core;

import nl.bstoi.poiparser.core.exception.PoiParserException;

public class WritePoiParserException extends PoiParserException {

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = -4478013655637215976L;

	public WritePoiParserException(int rowNumber, int columnNumber,String message) {
		super(rowNumber, columnNumber, message);
	}
	
	public WritePoiParserException(int rowNumber, int columnNumber,String message,Throwable throwable) {
		super(rowNumber, columnNumber, message,throwable);
	}
	
	
}
