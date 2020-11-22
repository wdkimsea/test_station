package cn.guanmai.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigureUtil {
	private static Logger logger = LoggerFactory.getLogger(ConfigureUtil.class);

	private static String fileName = "item.properties";

	public static String getValueByKey(String key) {
		Properties pps = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			pps.load(in);
			if (pps.containsKey(key)) {
				String value = pps.getProperty(key);
				return value;
			}
			return null;
		} catch (IOException e) {
			logger.error("读取配置文件信息出错 : " + e.getMessage());
			throw new RuntimeException("读取配置文件信息出错 : " + e.getMessage());
		}
	}

	public static void updateProperties(String key, String value) {
		Properties pps = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			pps.load(in);
			pps.setProperty(key, value);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
			pps.store(out, "");
			out.close();
		} catch (IOException e) {
			logger.error("更新配置文件信息出错 : " + e.getMessage());
			throw new RuntimeException("更新配置文件信息出错 : " + e.getMessage());
		}
	}
}
