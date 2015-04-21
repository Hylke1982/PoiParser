package nl.bstoi.poiparser.core;

import nl.bstoi.poiparser.core.exception.PoiParserException;

public class RegexReadPoiParserException extends PoiParserException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5426437436350768514L;
	
	private static final String message = "Field doesn't validate against regext is empty at in sheet %1$s row %2$d at column %3$d";
	
	public RegexReadPoiParserException(final String sheetName,final int rowNumber, final int columnNumber) {
		super(rowNumber, columnNumber, String.format(message, sheetName,rowNumber,columnNumber));
	}
	
	

}
