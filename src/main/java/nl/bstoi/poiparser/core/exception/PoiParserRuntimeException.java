package nl.bstoi.poiparser.core.exception;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:21
 */
public class PoiParserRuntimeException extends RuntimeException {

    public PoiParserRuntimeException(final String message) {
        super(message);
    }

    public PoiParserRuntimeException(final String message,final Throwable cause) {
        super(message, cause);
    }

    public PoiParserRuntimeException(final Throwable cause) {
        super(cause);
    }
}
