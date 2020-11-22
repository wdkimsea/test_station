package cn.guanmai.util;

import java.util.Date;

import org.testng.Reporter;

/**
 * @author liming
 * @date 2019年8月2日 上午11:45:30
 * @des 报告日志添加样式
 * @version 1.0
 */
public class ReporterCSS {
	private static long date;

	public static void title(String s) {
		date = new Date().getTime();
		Reporter.log(date + "===<span style=\"color:#1565C0;font-weight:bold;\">" + s + "</span>");
	}

	public static void step(String s) {
		date = new Date().getTime();
		Reporter.log(date + "===<span style=\"color:#01AF00;font-weight:bold;\">" + s + "</span>");
	}

	public static void warn(String s) {
		date = new Date().getTime();
		Reporter.log(date + "===<span style=\"color:red\">" + s + "</span>");
	}

	public static void log(String s) {
		date = new Date().getTime();
		Reporter.log(date + "===" + s);
	}

}
