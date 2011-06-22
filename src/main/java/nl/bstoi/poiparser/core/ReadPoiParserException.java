package nl.bstoi.poiparser.core;

public class ReadPoiParserException extends PoiParserException{
	
	private static final String message = "Unable to read row %1$d at column %2$d";

	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 8190084782674535356L;

	public ReadPoiParserException(String message) {
		super(0, 0, message);
	}
	
	public ReadPoiParserException(String message,Throwable throwable) {
		super(0, 0, message, throwable);
	}
	
	public ReadPoiParserException(int rowNumber, int columnNumber,Throwable throwable) {
		super(rowNumber, columnNumber, message, throwable);
	}
}
