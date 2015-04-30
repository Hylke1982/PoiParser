package nl.bstoi.poiparser.core.strategy.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public class TypedArrayList<T> extends ArrayList<T> implements TypedList<T> {

    private final Class<T> clazz;

    public TypedArrayList(final Class<T> clazz, final Collection<T> c) {
        super(c);
        this.clazz = clazz;
    }

    public TypedArrayList(final Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    public TypedArrayList(final Class<T> clazz, int initialCapacity) {
        super(initialCapacity);
        this.clazz = clazz;
    }

    public Class<T> getType() {
        return this.clazz;
    }
}
