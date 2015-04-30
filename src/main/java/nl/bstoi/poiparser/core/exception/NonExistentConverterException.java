package nl.bstoi.poiparser.core.exception;

/**
 * Created by hylke on 22/04/15.
 */
public class NonExistentConverterException extends PoiParserException {

    public NonExistentConverterException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public NonExistentConverterException(String message) {
        super(message);
    }
}
