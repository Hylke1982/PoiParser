package nl.bstoi.poiparser.core.exception;

import nl.bstoi.poiparser.core.exception.PoiParserException;

public class RequiredFieldPoiParserException extends PoiParserException {

    /**
     * SERIAL VERSION ID.
     */
    private static final long serialVersionUID = 1832731939379097960L;

    private static final String message = "Required field is empty at in sheet %1$s row %2$d at column %3$d";

    public RequiredFieldPoiParserException(final String sheetName, final int rowNumber, final int columnNumber) {
        super(rowNumber, columnNumber, String.format(message, sheetName, rowNumber, columnNumber));
    }


}
