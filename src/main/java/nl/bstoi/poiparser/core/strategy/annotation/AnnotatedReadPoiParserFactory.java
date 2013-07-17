package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.strategy.AbstractPoiParserFactory;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import nl.bstoi.poiparser.core.strategy.ReadPoiParserFactory;
import nl.bstoi.poiparser.core.strategy.ReadPoiParser;
import org.apache.commons.lang.StringUtils;

import java.io.InputStream;
import java.util.Set;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 13:17
 */
public class AnnotatedReadPoiParserFactory<T> extends AbstractPoiParserFactory implements ReadPoiParserFactory<T> {

    private Set<CellDescriptor> overrideCellDescriptors;


    private final Class<T> clazz;


    public AnnotatedReadPoiParserFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public ReadPoiParser<T> createReadPoiParser(InputStream excelInputStream, String sheetName) throws PoiParserException {
        if (null == excelInputStream) throw new IllegalArgumentException("Excel input stream cannot be null");
        if (StringUtils.isEmpty(sheetName)) throw new IllegalArgumentException("Sheet name cannot be empty");
        return new AnnotatedReadPoiParser<T>(getCellDescriptors(), getSheetFromInputStream(excelInputStream, sheetName), clazz);
    }

    /**
     * Get active cell descriptors
     *
     * @return
     */
    public Set<CellDescriptor> getCellDescriptors() {
        if (null == this.overrideCellDescriptors) {
            AnnotatedClassDescriber annotatedClassDescriber = AnnotatedClassDescriber.getInstance();
            return annotatedClassDescriber.getCellDescriptorsForClass(clazz);
        }
        return this.overrideCellDescriptors;
    }

    public void setOverrideCellDescriptors(Set<CellDescriptor> overrideCellDescriptors) {
        this.overrideCellDescriptors = overrideCellDescriptors;
    }
}
