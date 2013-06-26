package nl.bstoi.poiparser.core.strategy;

import nl.bstoi.poiparser.core.exception.PoiParserException;
import nl.bstoi.poiparser.core.exception.PoiParserRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@gmail.com]
 * Date: 23-06-13
 * Time: 22:09
 */
public abstract class AbstractPoiParserFactory {

    private final static Log log = LogFactory.getLog(AbstractPoiParserFactory.class);

    protected Sheet getSheetFromInputStream(InputStream inputStream, String sheetName) throws PoiParserException {

        // Read work book
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            return workbook.getSheet(sheetName);
        } catch (InvalidFormatException e) {
            throw new PoiParserException("Cannot read input stream", e);
        } catch (IllegalArgumentException e){
            throw new PoiParserException("Cannot read input stream", e);
        } catch (IOException e) {
            throw new PoiParserException("Cannot read input stream", e);
        } finally {
            if (null != inputStream) try {
                inputStream.close();
            } catch (IOException e) {
                throw new PoiParserRuntimeException("Cannot close input stream",e);
            }
        }
    }
}
