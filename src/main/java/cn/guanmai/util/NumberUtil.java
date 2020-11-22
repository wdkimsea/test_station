package cn.guanmai.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/* 
* @author liming 
* @date Oct 31, 2018 10:57:05 AM 
* @todo TODO
* @version 1.0 
*/
public class NumberUtil {
	/**
	 * 
	 * @param min 最小值
	 * @param max 最大值
	 * @param scl 小数最大位数
	 * @return
	 */
	public static BigDecimal getRandomNumber(int min, int max, int scl) {
		double random = Math.random() * (max - min) + min;
		BigDecimal num = new BigDecimal(String.valueOf(random));
		BigDecimal setScale = num.setScale(scl, BigDecimal.ROUND_HALF_UP);
		return setScale;
	}

	/**
	 * 四舍五入指定数值,保留两位小数
	 * 
	 * @param num
	 * @return BigDecimal
	 */
	public static BigDecimal roundTheNumber(Object num, int newScale) {
		BigDecimal bd = new BigDecimal(String.valueOf(num));
		return bd.setScale(newScale, RoundingMode.HALF_UP);
	}

	/**
	 * 指定列表中随机获得一个值
	 * 
	 * @param list
	 * @return
	 */
	public static <T> T roundNumberInList(List<T> list) {
		Random random = new Random();
		int index = random.nextInt(list.size());
		return list.get(index);
	}

	/**
	 * 指定列表随机获取制定大小的值
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> List<T> roundNumberInList(List<T> list, int count) {
		Random random = new Random();
		if (list.size() <= count) {
			return list;
		} else {
			Set<Integer> keySet = new HashSet<Integer>();
			List<T> resultList = new ArrayList<T>();
			Integer index = null;
			for (int i = 0; i < count; i++) {
				index = random.nextInt(list.size());
				if (keySet.contains(index)) {
					i--;
					continue;
				}
				resultList.add(list.get(index));
				keySet.add(index);
			}
			return resultList;
		}
	}

	/**
	 * 比较两个数值是否在一定得误差内
	 * 
	 * @param num1
	 * @param num2
	 * @param round
	 * @return
	 */
	public static boolean roundCompare(Object num1, Object num2, Object round) {
		BigDecimal bd_num1 = new BigDecimal(String.valueOf(num1));
		BigDecimal bd_num2 = new BigDecimal(String.valueOf(num2));
		BigDecimal bd_round = new BigDecimal(String.valueOf(round));
		return bd_num1.subtract(bd_num2).abs().compareTo(bd_round.abs()) <= 0;
	}
}
