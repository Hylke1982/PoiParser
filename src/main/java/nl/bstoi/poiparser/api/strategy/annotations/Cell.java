package nl.bstoi.poiparser.api.strategy.annotations;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.strategy.converter.StringConverter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Cell {

    /**
     * Column number
     *
     * @return column number
     */
    int columnNumber();

    /**
     * If field is required
     *
     * @return required
     */
    boolean required() default false;

    /**
     * If field is ignored while reading
     *
     * @return Ignore while reading
     */
    boolean readIgnore() default false;

    /**
     * If field is ignored while writing
     *
     * @return Ignore while writing
     */
    boolean writeIgnore() default false;

    /**
     * Validation rule based on regular expression can only be used with String cell types
     *
     * @return match rule based on regex
     */
    String regex() default "";
}
