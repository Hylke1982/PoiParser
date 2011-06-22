package nl.bstoi.poiparser.core;

public class PoiParserException extends Exception{
	
	/**
	 * SERIAL VERSION ID
	 */
	private static final long serialVersionUID = 1138883970917406361L;
	
	private int rowNumber;
	private int columnNumber;
	
	public PoiParserException(int rowNumber, int columnNumber, String message) {
		this(rowNumber,columnNumber,message,null);
	}
	
	public PoiParserException(int rowNumber, int columnNumber, String message,Throwable throwable) {
		super(message,throwable);
		this.columnNumber = columnNumber;
		this.rowNumber = rowNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}
	

}
