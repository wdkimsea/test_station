package cn.guanmai.util;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.FastHttpClient;
import cn.guanmai.okhttp.Response;

/**
 * @author: liming
 * @Date: 2020年6月4日 下午5:03:29
 * @description:
 * @version: 1.0
 */

public class OpenApiUtil {
	private static Logger logger = LoggerFactory.getLogger(OpenApiUtil.class);
	private static String openUrl = ConfigureUtil.getValueByKey("openUrl");

	public static String getAccess_token() throws Exception {
		String pattern = "http(s)?://open\\.(.*)guanmai\\.cn/v1/api";
		if (!Pattern.matches(pattern, openUrl)) {
			throw new Exception("openUrl 路径配置的不正确,请重新确认");
		}
		String urlStr = openUrl + "/auth/access_token/get/1.0";
		String appid = ConfigureUtil.getValueByKey("openAppid");
		String secret = ConfigureUtil.getValueByKey("openSecret");

		if (appid == null || appid.equals("")) {
			throw new Exception("登录配置项 openAppid 对应的值为空");
		}

		if (secret == null || secret.equals("")) {
			throw new Exception("登录配置项 openSecret 对应的值为空");
		}

		Response response = FastHttpClient.post().url(urlStr).addParams("appid", appid).addParams("secret", secret)
				.build().execute();

		String retStr = response.string();
		logger.info("登录返回信息:" + retStr);
		JSONObject retObj = JSON.parseObject(retStr);
		String access_token = null;
		if (retObj.getInteger("code") == 0 && retObj.getJSONObject("data").containsKey("access_token")) {
			access_token = retObj.getJSONObject("data").getString("access_token");
			logger.info("获取access_token成功");
		} else {
			logger.info("获取access_token失败:" + retObj.getString("msg"));
		}
		return access_token;
	}

}
