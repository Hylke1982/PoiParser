package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by hylke on 23/04/15.
 */
public class CellTypeTest {


    @Test
    public void testGetValues() {
        assertEquals(5, CellType.values().length);
        assertEquals(CellType.NUMERIC, CellType.values()[0]);
        assertEquals(CellType.STRING, CellType.values()[1]);
        assertEquals(CellType.FORMULA, CellType.values()[2]);
        assertEquals(CellType.BLANK, CellType.values()[3]);
        assertEquals(CellType.BOOLEAN, CellType.values()[4]);
    }

    @Test
    public void testGetNumericCellType() throws Exception {
        assertEquals(org.apache.poi.ss.usermodel.CellType.NUMERIC, CellType.NUMERIC.getPoiCellType());
        assertEquals(org.apache.poi.ss.usermodel.CellType.STRING, CellType.STRING.getPoiCellType());
        assertEquals(org.apache.poi.ss.usermodel.CellType.FORMULA, CellType.FORMULA.getPoiCellType());
        assertEquals(org.apache.poi.ss.usermodel.CellType.BLANK, CellType.BLANK.getPoiCellType());
        assertEquals(org.apache.poi.ss.usermodel.CellType.BOOLEAN, CellType.BOOLEAN.getPoiCellType());
    }

    @Test
    public void testGetCellTypeBasedOnNumeric() throws Exception {
        assertEquals(CellType.NUMERIC, CellType.getCellTypePoiCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC));
        assertEquals(CellType.STRING, CellType.getCellTypePoiCellType(org.apache.poi.ss.usermodel.CellType.STRING));
        assertEquals(CellType.FORMULA, CellType.getCellTypePoiCellType(org.apache.poi.ss.usermodel.CellType.FORMULA));
        assertEquals(CellType.BLANK, CellType.getCellTypePoiCellType(org.apache.poi.ss.usermodel.CellType.BLANK));
        assertEquals(CellType.BOOLEAN, CellType.getCellTypePoiCellType(org.apache.poi.ss.usermodel.CellType.BOOLEAN));
    }
}