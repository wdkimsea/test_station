package cn.guanmai.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StringUtil {
	public static boolean isNotEmpty(String str) {
		return null != str && !"".equals(str);
	}

	public static boolean isEmpty(String str) {
		return null == str || "".equals(str);
	}

	/**
	 * 
	 * @param sourceStr  待替换字符串
	 * @param matchStr   匹配字符串
	 * @param replaceStr 目标替换字符串
	 * @return
	 */
	public static String replaceFirst(String sourceStr, String matchStr, String replaceStr) {
		int index = sourceStr.indexOf(matchStr);
		int matLength = matchStr.length();
		int sourLength = sourceStr.length();
		String beginStr = sourceStr.substring(0, index);
		String endStr = sourceStr.substring(index + matLength, sourLength);
		sourceStr = beginStr + replaceStr + endStr;
		return sourceStr;
	}

	/***
	 * 生成指定长度的字符串
	 * 
	 * @param length
	 * @return str
	 */
	public static String getRandomString(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		int number = 0;
		for (int i = 0; i < length; i++) {
			number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/***
	 * 生成指定长度的数字字符串
	 * 
	 * @param length
	 * @return str
	 */
	public static String getRandomNumber(int length) {
		String base = "0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		int number = 0;
		for (int i = 0; i < length; i++) {
			number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	/**
	 * 获取随机数的字符串
	 * 
	 * @param min
	 * @param max
	 * @param scl
	 * @return
	 */
	public static String getRandomNumberStr(int min, int max, int scl) {
		double random = Math.random() * (max - min) + min;
		BigDecimal num = new BigDecimal(Double.toString(random));
		BigDecimal setScale = num.setScale(scl, BigDecimal.ROUND_HALF_UP);
		return new DecimalFormat("0.##").format(setScale.doubleValue());
	}

	/**
	 * 将Map<String,String> 集合转化成字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String mapChangeToStr(Map<String, String> map) {
		StringBuffer buffer = new StringBuffer("(");
		for (String key : map.keySet()) {
			buffer.append(key + ":" + map.get(key) + ",");
		}
		buffer.deleteCharAt(buffer.length() - 1);
		return buffer.append(")").toString();
	}

	/**
	 * 长度超过 length 指定长度,截取字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String interceptString(String str, int length) {
		if (str != null) {
			return str.length() > length ? str.substring(0, length) + "......" : str;
		}
		return null;
	}

	/**
	 * 自定义进制转化
	 * 
	 * @param num
	 * @param radix
	 * @return
	 */
	public static String getCustomizedStationId(String station_id) {
		String customize_chars = "ABCDEFGHJKLMNPRSTUVWXY";

		int station_id_int = Integer.valueOf(station_id.substring(1));

		List<Integer> list = new ArrayList<Integer>();
		while (station_id_int > 0) {
			// 把除以基数的余数存到缓冲区中
			list.add(station_id_int % 22);
			station_id_int /= 22;
		}
		Collections.reverse(list);

		StringBuffer sb = new StringBuffer();
		for (Integer num : list) {
			sb.append(customize_chars.charAt(num));
		}
		return sb.toString();
	}
}
