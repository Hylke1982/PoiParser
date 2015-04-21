package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.strategy.util.TypedList;

import java.util.List;
import java.util.Map;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public interface WritePoiParser {
    public void write(final Map<String, TypedList<?>> sheetDataset) throws PoiParserException;
}
