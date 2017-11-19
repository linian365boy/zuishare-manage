package top.zuishare.util;
/**
 * 日期转换
 */

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateConverter{
	
	private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	private static final String MONTH_PATTERN = "yyyy-MM";
	
	//type为Date，则转换为Date，否则转换为String
	public static Object convert(Class type, Object value) {
		Object result = null;
		if (type == Date.class) {
			try {
				//Convert String to Date
				result = doConvertToDate(value);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else if (type == String.class) {
			//Convert Date to String
			result = doConvertToString(value);
		}
		return result;
	}
	
	/**
	 * Convert String to Date
	 *
	 * @param value
	 * @return
	 * @throws ParseException 
	 */
	private static Date doConvertToDate(Object value) throws ParseException {
		Date result = null;

		if (value instanceof String) {
			result = DateUtils.parseDate((String) value, new String[] { DATE_PATTERN, DATETIME_PATTERN,
					DATETIME_PATTERN_NO_SECOND, MONTH_PATTERN });
			// all patterns failed, try a milliseconds constructor
			if (result == null && StringUtils.isNotEmpty((String) value)) {
				try {
					result = new Date(new Long((String) value).longValue());
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		} else if (value instanceof Object[]) {
			// let's try to convert the first element only
			Object[] array = (Object[]) value;

			if ((array != null) && (array.length >= 1)) {
				value = array[0];
				result = doConvertToDate(value);
			}

		} else if (Date.class.isAssignableFrom(value.getClass())) {
			result = (Date) value;
		}
		return result;
	}

	/**
	 * Convert Date to String
	 *
	 * @param value
	 * @return
	 */
	private static String doConvertToString(Object value) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIME_PATTERN);
		String result = null;
		if (value instanceof Date) {
			result = simpleDateFormat.format(value);
		}
		return result;
	}
	
}
