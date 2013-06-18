package nl.bstoi.poiparser.core.strategy.annotation;

import nl.bstoi.poiparser.api.strategy.annotations.Cell;
import nl.bstoi.poiparser.api.strategy.annotations.Embedded;

/**
 * User: Hylke Stapersma
 * E-mail:[ hylke.stapersma@codecentric.nl]
 * Date: 18-06-13
 * Time: 20:02
 */
public class EmbeddedTestRow {

    @Cell(columnNumber = 0)
    private Short field1;

    @Cell(columnNumber = 1)
    private Integer field2;

    @Embedded
    private EmbeddableTestRow embeddableTestRow;

    public Short getField1() {
        return field1;
    }

    public void setField1(Short field1) {
        this.field1 = field1;
    }

    public Integer getField2() {
        return field2;
    }

    public void setField2(Integer field2) {
        this.field2 = field2;
    }

    public EmbeddableTestRow getEmbeddableTestRow() {
        return embeddableTestRow;
    }

    public void setEmbeddableTestRow(EmbeddableTestRow embeddableTestRow) {
        this.embeddableTestRow = embeddableTestRow;
    }
}
