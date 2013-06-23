package nl.bstoi.poiparser.core.exception;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:21
 */
public class PoiParserRuntimeException extends RuntimeException {

    public PoiParserRuntimeException(String message) {
        super(message);
    }

    public PoiParserRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PoiParserRuntimeException(Throwable cause) {
        super(cause);
    }

    protected PoiParserRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
