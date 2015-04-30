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
        assertEquals(Cell.CELL_TYPE_NUMERIC, CellType.NUMERIC.getNumericCellType());
        assertEquals(Cell.CELL_TYPE_STRING, CellType.STRING.getNumericCellType());
        assertEquals(Cell.CELL_TYPE_FORMULA, CellType.FORMULA.getNumericCellType());
        assertEquals(Cell.CELL_TYPE_BLANK, CellType.BLANK.getNumericCellType());
        assertEquals(Cell.CELL_TYPE_BOOLEAN, CellType.BOOLEAN.getNumericCellType());
    }

    @Test
    public void testGetCellTypeBasedOnNumericNonExisting() throws Exception {
        try {
            CellType.getCellTypeBasedOnNumeric(34584234);
            fail("Should not reach this point");
        } catch (final IllegalStateException e) {
            assertEquals("CellType with numeric value 34584234 cannot be found", e.getMessage());
        }
    }

    @Test
    public void testGetCellTypeBasedOnNumeric() throws Exception {
        assertEquals(CellType.NUMERIC, CellType.getCellTypeBasedOnNumeric(Cell.CELL_TYPE_NUMERIC));
        assertEquals(CellType.STRING, CellType.getCellTypeBasedOnNumeric(Cell.CELL_TYPE_STRING));
        assertEquals(CellType.FORMULA, CellType.getCellTypeBasedOnNumeric(Cell.CELL_TYPE_FORMULA));
        assertEquals(CellType.BLANK, CellType.getCellTypeBasedOnNumeric(Cell.CELL_TYPE_BLANK));
        assertEquals(CellType.BOOLEAN, CellType.getCellTypeBasedOnNumeric(Cell.CELL_TYPE_BOOLEAN));
    }
}