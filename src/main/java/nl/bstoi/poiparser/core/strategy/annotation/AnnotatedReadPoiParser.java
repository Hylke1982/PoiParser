package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.strategy.AbstractReadPoiParser;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import nl.bstoi.poiparser.core.strategy.ReadPoiParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 18:11
 */
public class AnnotatedReadPoiParser<T> extends AbstractReadPoiParser implements ReadPoiParser<T> {

    private final static Log log = LogFactory.getLog(AnnotatedReadPoiParser.class);

    public AnnotatedReadPoiParser(Set<CellDescriptor> cellDescriptors, Sheet sheet, Class<T> clazz) {
        super(cellDescriptors, sheet, clazz);
    }

    public List<T> read() throws PoiParserException {
        return readSheet();
    }

    public List<T> read(int startRow) throws PoiParserException {
        return readSheet(startRow,getSheet().getLastRowNum());
    }

    public List<T> read(int startRow, int endRow) throws PoiParserException {
        return readSheet(startRow, endRow);
    }


}
