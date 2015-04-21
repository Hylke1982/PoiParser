package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.exception.PoiParserException;

import java.io.InputStream;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:19
 */
public interface ReadPoiParserFactory<T> {

    ReadPoiParser<T> createReadPoiParser(final InputStream excelInputStream, final String sheetName) throws PoiParserException;

    public Set<CellDescriptor> getCellDescriptors();

}
