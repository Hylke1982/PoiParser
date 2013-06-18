package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;

import java.math.BigDecimal;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@codecentric.nl]
 * Date: 18-06-13
 * Time: 20:03
 */
public class EmbeddableTestRow {

    @Cell(columnNumber = 2)
    private Long field3;

    @Cell(columnNumber = 3)
    private BigDecimal field4;

    public Long getField3() {
        return field3;
    }

    public void setField3(Long field3) {
        this.field3 = field3;
    }

    public BigDecimal getField4() {
        return field4;
    }

    public void setField4(BigDecimal field4) {
        this.field4 = field4;
    }
}
