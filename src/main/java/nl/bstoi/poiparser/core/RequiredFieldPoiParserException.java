package nl.bstoi.poiparser.core;

public class RequiredFieldPoiParserException extends PoiParserException{
	
	/**
	 * SERIAL VERSION ID.
	 */
	private static final long serialVersionUID = 1832731939379097960L;
	
	private static final String message = "Required field is empty at in sheet %1$s row %2$d at column %3$d";
	
	public RequiredFieldPoiParserException(String sheetName,int rowNumber, int columnNumber) {
		super(rowNumber, columnNumber, String.format(message, sheetName,rowNumber,columnNumber));
	}
	
	

}
