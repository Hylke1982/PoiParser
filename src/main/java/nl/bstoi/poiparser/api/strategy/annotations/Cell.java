package nl.bstoi.poiparser.api.strategy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface Cell {
	
	/**
	 * Column number
	 * @return
	 */
	int columnNumber();
	
	/**
	 * If field is required
	 * @return
	 */
	boolean required() default false;
	
	/**
	 * If field is ignored while reading
	 * @return
	 */
	boolean readIgnore() default false;
	
	/**
	 * If field is ignored while writing
	 * @return
	 */
	boolean writeIgnore() default false;
	
	/**
	 * Validation rule based on regular expression
	 * @return
	 */
	String regex() default "";

}
