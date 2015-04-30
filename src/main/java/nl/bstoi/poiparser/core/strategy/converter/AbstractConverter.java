package nl.bstoi.poiparser.core.strategy.converter;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Created by hylke on 23/04/15.
 */
public abstract class AbstractConverter<T> implements Converter<T> {

    private final CellType[] supportedCellTypes;


    protected AbstractConverter(final CellType[] supportedCellTypes) {
        this.supportedCellTypes = supportedCellTypes;
    }

    protected boolean isCellTypeSupported(final Cell cell) {
        if (null != cell) {
            final CellType cellType = getCellType(cell);
            return ArrayUtils.contains(supportedCellTypes, cellType);
        } else {
            throw new NullPointerException("Cell cannot be null");
        }
    }

    protected CellType getCellType(final Cell cell) {
        return CellType.getCellTypeBasedOnNumeric(cell.getCellType());
    }
}
