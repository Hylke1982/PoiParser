package nl.bstoi.poiparser.core.strategy.util;

import java.util.List;

/**
 * Hylke Stapersma
 * hylke.stapersma@gmail.com
 */
public interface TypedList<T> extends List<T> {

    public Class<T> getType();
}
