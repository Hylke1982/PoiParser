package nl.bstoi.poiparser.core.strategy.converter;

/**
 * Created by hylke on 23/04/15.
 */
public enum CellType {
    NUMERIC(org.apache.poi.ss.usermodel.CellType.NUMERIC),
    STRING(org.apache.poi.ss.usermodel.CellType.STRING),
    FORMULA(org.apache.poi.ss.usermodel.CellType.FORMULA),
    BLANK(org.apache.poi.ss.usermodel.CellType.BLANK),
    BOOLEAN(org.apache.poi.ss.usermodel.CellType.BOOLEAN);

    private final org.apache.poi.ss.usermodel.CellType poiCellType;

    CellType(final org.apache.poi.ss.usermodel.CellType numericCellType) {
        this.poiCellType = numericCellType;
    }

    public org.apache.poi.ss.usermodel.CellType getPoiCellType() {
        return poiCellType;
    }

    /**
     * Get cell type based on numeric value
     *
     * @param numericCellType
     * @return
     */
    public static CellType getCellTypePoiCellType(final org.apache.poi.ss.usermodel.CellType numericCellType) {
        for (final CellType cellType : CellType.values()) {
            if (cellType.getPoiCellType() == numericCellType) {
                return cellType;
            }
        }
        throw new IllegalStateException(String.format("CellType with value %s cannot be found", numericCellType));
    }

}
