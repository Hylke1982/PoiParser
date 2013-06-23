package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.exception.PoiParserException;

import java.util.List;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:08
 */
public interface ReadPoiParser<T> {

    public List<T> read() throws PoiParserException;

    public List<T> read(int startRow) throws PoiParserException;

    public List<T> read(int startRow, int endRow) throws PoiParserException;

}
