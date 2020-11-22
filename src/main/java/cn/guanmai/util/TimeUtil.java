package cn.guanmai.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/* 
* @author liming 
* @date Feb 25, 2019 3:25:46 PM 
* @des 时间格式化工具
* @version 1.0 
*/
public class TimeUtil {
	private static Calendar calendar;

	static {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
	}

	/**
	 * 获取指定格式化的当前时间
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getCurrentTime(String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(new Date());
	}

	/**
	 * 对指定时间进行格式化
	 * 
	 * @param pattern
	 * @param timeStr
	 * @return
	 */
	public static String getFormatTime(String pattern, Date date) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 对指定日期进行加减运算
	 * 
	 * @param pattern
	 * @param timeStr
	 * @param span
	 * @param unit
	 * @return
	 * @throws ParseException
	 */
	public static String calculateTime(String pattern, String timeStr, int span, int unit) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		calendar.setTime(format.parse(timeStr));
		calendar.add(unit, span);
		return format.format(calendar.getTime());
	}

	/**
	 * 获取当天是星期几
	 * 
	 * @return
	 */
	public static int getDateOfWeek() {
		LocalDate currentDate = LocalDate.now();
		return currentDate.getDayOfWeek().getValue();
	}

	/**
	 * 获取指定日期是周几
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static int getDateOfWeek(String dateStr) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = format.parse(dateStr);
		calendar.setTime(date);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (weekday == 0) {
			weekday = 7;
		}
		return weekday;
	}

	public static int compareDate(String pattern, String date1, String date2) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date tempDate1 = format.parse(date1);
		Date tempDate2 = format.parse(date2);
		return tempDate1.compareTo(tempDate2);

	}

	public static long getLongTime() {
		return new Date().getTime();
	}
}
