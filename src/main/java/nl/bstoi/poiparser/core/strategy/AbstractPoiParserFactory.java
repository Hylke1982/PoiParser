package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 22:09
 */
public abstract class AbstractPoiParserFactory {

    private final static Log log = LogFactory.getLog(AbstractPoiParserFactory.class);

    private PoiType poiType;
    protected boolean createHeaderRow = false;
    protected boolean ignoreFirstRow = false;
    protected Properties columnHeaderProperties;

    protected Sheet getSheetFromInputStream(final InputStream inputStream, final String sheetName) throws PoiParserException {

        // Read work book
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            return workbook.getSheet(sheetName);
        } catch (InvalidFormatException e) {
            throw new PoiParserException("Cannot read input stream", e);
        } catch (IllegalArgumentException e) {
            throw new PoiParserException("Cannot read input stream", e);
        } catch (IOException e) {
            throw new PoiParserException("Cannot read input stream", e);
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new PoiParserRuntimeException("Cannot close input stream", e);
                }
            }
        }
    }

    protected Workbook createNewWorkBook(final PoiType poiType) {
        if (null == poiType) return createNewWorkBook(PoiType.HSSF);
        switch (poiType) {
            case HSSF:
                return new HSSFWorkbook();
            case XSSF:
                return new XSSFWorkbook();
            case SXSSF:
                return new SXSSFWorkbook();
            default:
                return new HSSFWorkbook();
        }
    }

    public PoiType getPoiType() {
        return this.poiType;
    }


    public void setCreateHeaderRow(boolean createHeaderRow) {
        this.createHeaderRow = createHeaderRow;
    }

    public void setIgnoreFirstRow(final boolean ignoreFirstRow) {
        this.ignoreFirstRow = ignoreFirstRow;
    }

    public void setColumnHeaderProperties(final Properties columnHeaderProperties) {
        this.columnHeaderProperties = columnHeaderProperties;
    }

}
