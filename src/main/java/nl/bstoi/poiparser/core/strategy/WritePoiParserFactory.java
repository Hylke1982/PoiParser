package nl.bstoi.poiparser.core.strategy;

import java.io.OutputStream;
import java.util.Set;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public interface WritePoiParserFactory {

    WritePoiParser createWritePoiParser(OutputStream excelInputStream);
}
