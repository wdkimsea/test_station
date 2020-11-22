package cn.guanmai.util;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * @author: liming
 * @Date: 2020年6月23日 上午11:23:24
 * @description:
 * @version: 1.0
 */

public class LocalRetry implements IRetryAnalyzer {
	private int retryTimes = 0;
	private static final int maxRetryTimes = 1;

	@Override
	public boolean retry(ITestResult result) {
		if (retryTimes < maxRetryTimes) {
			retryTimes++;
			return true;
		}
		return false;
	}

}
