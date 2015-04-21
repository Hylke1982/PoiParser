package nl.bstoi.poiparser.core;

import nl.bstoi.poiparser.core.exception.PoiParserException;

public class WritePoiParserException extends PoiParserException {

    /**
     * Serial version ID
     */
    private static final long serialVersionUID = -4478013655637215976L;

    public WritePoiParserException(final int rowNumber, final int columnNumber, final String message) {
        super(rowNumber, columnNumber, message);
    }

    public WritePoiParserException(final int rowNumber, final int columnNumber, final String message, final Throwable throwable) {
        super(rowNumber, columnNumber, message, throwable);
    }


}
