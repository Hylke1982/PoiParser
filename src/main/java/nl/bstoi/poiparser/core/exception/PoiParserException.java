package nl.bstoi.poiparser.core.exception;

public class PoiParserException extends Exception {

    /**
     * SERIAL VERSION ID
     */
    private static final long serialVersionUID = 1138883970917406361L;

    private int rowNumber;
    private int columnNumber;

    public PoiParserException(final int rowNumber,final int columnNumber,final String message) {
        this(rowNumber, columnNumber, message, null);
    }

    public PoiParserException(final String message) {
        super(message);
    }

    public PoiParserException(final String message,final Throwable throwable) {
        super(message, throwable);
    }

    public PoiParserException(final int rowNumber,final int columnNumber,final String message,final Throwable throwable) {
        super(message, throwable);
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
