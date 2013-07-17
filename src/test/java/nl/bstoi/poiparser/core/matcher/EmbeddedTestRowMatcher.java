package nl.bstoi.poiparser.core.matcher;

import nl.bstoi.poiparser.core.TestHelper;
import nl.bstoi.poiparser.core.strategy.annotation.structures.EmbeddedTestRow;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.math.BigDecimal;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class EmbeddedTestRowMatcher extends BaseMatcher<EmbeddedTestRow> {

    private final Short field1;
    private final Integer field2;
    private final Long field3;
    private final BigDecimal field4;

    public EmbeddedTestRowMatcher(Short field1, Integer field2, Long field3, BigDecimal field4) {
        this.field1 = field1;
        this.field2 = field2;
        this.field3 = field3;
        this.field4 = field4;
    }

    public boolean matches(Object o) {
        if (o instanceof EmbeddedTestRow) {
            EmbeddedTestRow embeddedTestRow = (EmbeddedTestRow) o;
            boolean matches = TestHelper.objectEqual(embeddedTestRow.getField1(), field1);
            matches &= TestHelper.objectEqual(embeddedTestRow.getField2(), field2);
            if (null != field3 || null != field3) {
                if (null == embeddedTestRow.getEmbeddableTestRow()) return false;
                matches &= TestHelper.objectEqual(embeddedTestRow.getEmbeddableTestRow().getField3(), field3);
                matches &= TestHelper.objectEqual(embeddedTestRow.getEmbeddableTestRow().getField4(), field4);
            }
            return matches;
        }
        return false;
    }

    public void describeTo(Description description) {
    }
}
