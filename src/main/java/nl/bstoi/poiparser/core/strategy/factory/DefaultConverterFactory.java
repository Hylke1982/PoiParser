package nl.bstoi.poiparser.core.strategy.factory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nl.bstoi.poiparser.api.strategy.converter.Converter;
import nl.bstoi.poiparser.core.strategy.converter.*;

public class DefaultConverterFactory {
	
	private final Map<Class<?>,Class<? extends Converter<?>>> typeConvertorMap = new ConcurrentHashMap<Class<?>, Class<? extends Converter<?>>>();
	
	public DefaultConverterFactory() {
		// Register default converters
		registerConverter(String.class, StringConverter.class);
		registerConverter(Short.class, ShortConverter.class);
		registerConverter(Short.TYPE, ShortConverter.class);
		registerConverter(Integer.class, IntegerConvertor.class);
		registerConverter(Integer.TYPE, IntegerConvertor.class);
		registerConverter(Long.class, LongConverter.class);
		registerConverter(Long.TYPE, LongConverter.class);
		registerConverter(BigDecimal.class, BigDecimalConvertor.class);
		registerConverter(Date.class, DateConverter.class);
		registerConverter(Calendar.class, CalendarConverter.class);
		registerConverter(Boolean.class, BooleanConverter.class);
		registerConverter(Boolean.TYPE, BooleanConverter.class);
	}
	
	public Converter<?> getConverter(final Class<?> type) throws InstantiationException, IllegalAccessException{
		return typeConvertorMap.get(type).newInstance();
	}
	
	public void registerConverter(final Class<?> type,final Class<? extends Converter<?>> converterType){
		typeConvertorMap.put(type, converterType);
	}
	
	public Map<Class<?>, Class<? extends Converter<?>>> getTypeConvertorMap() {
		return typeConvertorMap;
	}

}
