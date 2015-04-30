package nl.bstoi.poiparser.core.strategy.converter;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by hylke on 23/04/15.
 */
public enum CellType {
    NUMERIC(Cell.CELL_TYPE_NUMERIC),
    STRING(Cell.CELL_TYPE_STRING),
    FORMULA(Cell.CELL_TYPE_FORMULA),
    BLANK(Cell.CELL_TYPE_BLANK),
    BOOLEAN(Cell.CELL_TYPE_BOOLEAN);

    private final int numericCellType;

    CellType(final int numericCellType) {
        this.numericCellType = numericCellType;
    }

    public int getNumericCellType() {
        return numericCellType;
    }

    /**
     * Get cell type based on numeric value
     *
     * @param numericCellType
     * @return
     */
    public static CellType getCellTypeBasedOnNumeric(final int numericCellType) {
        for (final CellType cellType : CellType.values()) {
            if (cellType.getNumericCellType() == numericCellType) {
                return cellType;
            }
        }
        throw new IllegalStateException(String.format("CellType with numeric value %d cannot be found", numericCellType));
    }

}
