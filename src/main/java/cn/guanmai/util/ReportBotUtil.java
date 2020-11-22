package cn.guanmai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.FastHttpClient;
import cn.guanmai.okhttp.Response;

/**
 * @author: liming
 * @Date: 2020年4月23日 上午11:12:58
 * @description: 发送测试报告的机器人
 * @version: 1.0
 */

public class ReportBotUtil {
	private static Logger logger = LoggerFactory.getLogger(ReportBotUtil.class);

	// 发送地址
	private static String bot_url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=892ff8bf-442a-46ca-bbc6-0d3b673b3837";

	public static void sendReportResult(String reportUrl, String desc) {
		try {
			JSONObject retObj = TestResult.getTestResult();
			int pass = retObj.getInteger("pass");
			int fail = retObj.getInteger("fail");
			int skip = retObj.getInteger("skip");
			int total = pass + fail + skip;

			String msg = "{\r\n" + "    \"msgtype\": \"markdown\",\r\n" + "    \"markdown\": {\r\n"
					+ "        \"content\": \"测试报告地址:  [点击查看详情](" + reportUrl + ")\r\n" + "         >自定义的信息: " + desc
					+ "\r\n" + "         >总执行用例数:<font color=\\\"info\\\"> " + total + "</font>\r\n"
					+ "         >失败的用例数:<font color=\\\"warning\\\"> " + fail + "</font>\r\n"
					+ "         >跳过的用例数:<font color=\\\"warning\\\"> " + skip + "</font>\"\r\n" + "    }\r\n" + "}\r\n"
					+ "";

			Response response = FastHttpClient.post().url(bot_url).body(msg).build().execute();
			logger.info("发送测试报告结果: ", response.string());
		} catch (Exception e) {
			logger.error("发送测试报告失败: ", e);
		}
	}
}
