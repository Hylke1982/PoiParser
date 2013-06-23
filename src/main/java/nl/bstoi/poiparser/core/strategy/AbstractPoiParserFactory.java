package nl.bstoi.poiparser.core.strategy;

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

    protected Sheet getSheetFromInputStream(InputStream inputStream, String sheetName) throws IOException {

        // Read work book
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        // Get correct sheet.
        Sheet sheet = workbook.getSheet(sheetName);
        return sheet;
    }
}
