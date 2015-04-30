package nl.bstoi.poiparser.core.strategy.annotation.structures;

import nl.bstoi.poiparser.api.strategy.annotations.Embedded;

/**
 * Created by hylke on 30/04/15.
 */
public class EmbeddedRecursionTestRow {

    @Embedded
    private EmbeddedRecursionTestRow embeddedRecursionTestRow; // Not allowed

    public EmbeddedRecursionTestRow getEmbeddedRecursionTestRow() {
        return embeddedRecursionTestRow;
    }
}
