package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.ss.usermodel.Cell;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by hylke on 23/04/15.
 */
public class AbstractConverterTest {

    private AbstractConverter<String> abstractConverter;

    @Mock
    private Cell mockCell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        abstractConverter = new AbstractConverter<String>(new CellType[]{CellType.BOOLEAN, CellType.NUMERIC}) {
            @Override
            public String readCell(Cell cell, String regex) {
                return null;
            }

            @Override
            public String readCell(Cell cell) {
                return null;
            }

            @Override
            public void writeCell(Cell cell, String value) {

            }
        };

    }

    @Test
    public void testIsCellTypeSupported1() throws Exception {
        when(mockCell.getCellType()).thenReturn(org.apache.poi.ss.usermodel.CellType.BOOLEAN);
        assertTrue(abstractConverter.isCellTypeSupported(mockCell));
    }

    @Test
    public void testIsCellTypeSupported2() throws Exception {
        when(mockCell.getCellType()).thenReturn(org.apache.poi.ss.usermodel.CellType.NUMERIC);
        assertTrue(abstractConverter.isCellTypeSupported(mockCell));
    }

    @Test
    public void testIsCellTypeNotSupported() throws Exception {
        when(mockCell.getCellType()).thenReturn(org.apache.poi.ss.usermodel.CellType.STRING);
        assertFalse(abstractConverter.isCellTypeSupported(mockCell));
    }

    @Test
    public void testIsCellTypeSupportedWithNullCell() throws Exception {
        try {
            abstractConverter.isCellTypeSupported(null);
            fail("Should not reach this point.");
        } catch (final NullPointerException e) {
            assertEquals("Cell cannot be null", e.getMessage());
        }
    }


    @Test
    public void testGetCellType() throws Exception {
        when(mockCell.getCellType()).thenReturn(org.apache.poi.ss.usermodel.CellType.STRING);
        assertEquals(CellType.STRING, abstractConverter.getCellType(mockCell));
    }
}