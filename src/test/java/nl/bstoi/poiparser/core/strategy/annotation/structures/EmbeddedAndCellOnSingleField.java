package nl.bstoi.poiparser.core.strategy.annotation.structures;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;
import nl.bstoi.poiparser.api.strategy.annotations.Embedded;

/**
 * Created by hylke on 30/04/15.
 */
public class EmbeddedAndCellOnSingleField {

    @Embedded
    @Cell(columnNumber = 1)
    private String bla;

    public String getBla() {
        return bla;
    }
}
