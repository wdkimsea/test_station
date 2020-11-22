package cn.guanmai.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* 
* @author liming 
* @date Oct 31, 2018 10:58:50 AM 
* @todo 文件操作
* @version 1.0 
*/
public class FilesUtil {
	private static Logger logger = LoggerFactory.getLogger(FilesUtil.class);

	/**
	 * 
	 * 将byte数组转化成文件
	 * 
	 * @param bye
	 * @return
	 */
	public static boolean saveTempFile(byte[] bye, String fileName) {
		boolean result = true;
		String saveFileDir = System.getProperty("user.dir") + "/temp/";
		File file = new File(saveFileDir);
		OutputStream output = null;
		BufferedOutputStream bufferedOutput = null;
		try {
			if (!file.isDirectory()) {
				file.mkdir();
			}
			String filePath = saveFileDir + fileName;
			file = new File(filePath);
			output = new FileOutputStream(file);
			bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.write(bye);
		} catch (FileNotFoundException e) {
			result = false;
			logger.error("文件保存地址未找到: " + e);
		} catch (IOException e) {
			result = false;
			logger.error("生成文件失败: " + e);
		} finally {
			try {
				if (bufferedOutput != null) {
					bufferedOutput.close();
				}
			} catch (IOException e) {
				result = false;
				logger.error("关闭输出流遇到错误: " + e);
			}
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				result = false;
				logger.error("关闭输出流遇到错误: " + e);
			}
		}
		return result;
	}

}
