package nl.bstoi.poiparser.core.exception;

import nl.bstoi.poiparser.core.exception.PoiParserException;

public class ReadPoiParserException extends PoiParserException {

    private static final String message = "Unable to read row %1$d at column %2$d";

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = 8190084782674535356L;

    public ReadPoiParserException(final String message) {
        super(0, 0, message);
    }

    public ReadPoiParserException(final String message, final Throwable throwable) {
        super(0, 0, message, throwable);
    }

    public ReadPoiParserException(final int rowNumber, final int columnNumber, final Throwable throwable) {
        super(rowNumber, columnNumber, String.format(message, rowNumber, columnNumber), throwable);
    }
}
