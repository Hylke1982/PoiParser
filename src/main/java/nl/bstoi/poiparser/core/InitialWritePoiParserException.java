package nl.bstoi.poiparser.core;

public class InitialWritePoiParserException extends WritePoiParserException {
	
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 260867511149564242L;
	private static final String message = "Column with number %1$d is defined more than once, use writeIgnore or change columnNumber";

	public InitialWritePoiParserException(final int columnNumber) {
		super(0, columnNumber, String.format(message, columnNumber));
	}
	

}
