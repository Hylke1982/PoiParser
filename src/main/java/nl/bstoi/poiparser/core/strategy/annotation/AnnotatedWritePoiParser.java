package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.strategy.AbstractWritePoiParser;
import nl.bstoi.poiparser.core.strategy.CellDescriptor;
import nl.bstoi.poiparser.core.strategy.WritePoiParser;
import nl.bstoi.poiparser.core.strategy.util.TypedList;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

/**
 * Hylke Stapersma (codecentric nl)
 * hylke.stapersma@codecentric.nl
 */
public class AnnotatedWritePoiParser extends AbstractWritePoiParser implements WritePoiParser {

    private final OutputStream outputStream;

    public AnnotatedWritePoiParser(final OutputStream outputStream, final Workbook workbook) {
        super(workbook);
        this.outputStream = outputStream;
    }

    public void write(final Map<String, TypedList<?>> sheetDataset) throws PoiParserException {
        if (null == sheetDataset) throw new IllegalArgumentException("Sheet dataset name cannot be null");
        try {
            for (final String sheetName : sheetDataset.keySet()) {
                final TypedList<?> sheetData = sheetDataset.get(sheetName);
                writeSheet(sheetName, sheetData, getCellDescriptorsForGenericList(sheetData));
            }
            getWorkbook().write(outputStream);
        } catch (final IOException e) {
            throw new PoiParserException("Error while writing output stream", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private Set<CellDescriptor> getCellDescriptorsForGenericList(final TypedList<?> list) {
        final AnnotatedClassDescriber annotatedClassDescriber = AnnotatedClassDescriber.getInstance();
        final Set<CellDescriptor> cellDescriptors = annotatedClassDescriber.getCellDescriptorsForClass(list.getType());
        return cellDescriptors;
    }
}
