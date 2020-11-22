package cn.guanmai.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.bshop.url.BsURL;
import cn.guanmai.okhttp.FastHttpClient;
import cn.guanmai.okhttp.Response;

/**
 * @author: liming
 * @Date: 2020年5月20日 上午10:08:08
 * @description: 系统各个模块登录管理工具类
 * @version: 1.0
 */

public class LoginUtil {
	private static Logger logger = LoggerFactory.getLogger(LoginUtil.class);
	private static String manage_url = ConfigureUtil.getValueByKey("manageUrl");
	private static String station_url = ConfigureUtil.getValueByKey("stationUrl");
	private static String bshop_url = ConfigureUtil.getValueByKey("bshopUrl");

	static {
		if (station_url == null || station_url.trim().equals("")) {
			throw new RuntimeException("配置文件中没有配置station链接");
		} else {
			String regex = "http(s)?://station.(.)*guanmai.cn";
			if (!Pattern.matches(regex, station_url)) {
				throw new RuntimeException("配置文件中的Station路径:" + station_url + " 不符合规范");
			}
		}
	}

	private static Map<String, String> login(String login_url, String user_name, String password) throws Exception {
		Response response = FastHttpClient.post().url(login_url).addParams("username", user_name)
				.addParams("password", password).build().execute();
		String retStr = response.string();
		logger.info(retStr);
		JSONObject retObj = JSON.parseObject(retStr);

		Map<String, String> headers = null;
		if (retObj.getInteger("code") == 0) {
			headers = new HashMap<String, String>();
			String headersStr = response.headers().toString();
			String session_id = headersStr.split("sessionid=")[1].split(";")[0];
			String group_id = headersStr.split("group_id=")[1].split(";")[0];
			String cookie_str = String.format("sessionid=%s;group_id=%s", session_id, group_id);
			logger.info(cookie_str);
			headers = new HashMap<String, String>();
			headers.put("cookie", cookie_str);
			logger.info("登录成功");
		} else {
			logger.error("登录失败");
		}
		return headers;
	}

	/**
	 * 读取配置文件里的账户信息登录station
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginStation() throws Exception {
		String url = station_url + "/station/login";

		String user_name = ConfigureUtil.getValueByKey("stationName");
		String password = ConfigureUtil.getValueByKey("stationPwd");

		Map<String, String> headers = login(url, user_name, password);
		return headers;

	}

	/**
	 * 输入账户密码登录station
	 * 
	 * @param user_name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginStation(String user_name, String password) throws Exception {
		String url = station_url + "/station/login";
		Map<String, String> headers = login(url, user_name, password);
		return headers;
	}

	/**
	 * 读取配置文件里的账户信息登录manage
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginManage() throws Exception {
		String url = manage_url + "/login";

		String user_name = ConfigureUtil.getValueByKey("manageName");
		String password = ConfigureUtil.getValueByKey("managePwd");

		Map<String, String> headers = login(url, user_name, password);
		return headers;
	}

	/**
	 * 输入账户密码登录manage
	 * 
	 * @param user_name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginManage(String user_name, String password) throws Exception {
		String url = manage_url + "/login";
		Map<String, String> headers = login(url, user_name, password);
		return headers;
	}

	/**
	 * 读取配置文件里的账户信息登录bshop
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginBshop() throws Exception {
		logger.info("登录商城");
		String url = bshop_url + "/login";
		String user_name = ConfigureUtil.getValueByKey("bshopName");
		String password = ConfigureUtil.getValueByKey("bshopPwd");
		String cms_key = ConfigureUtil.getValueByKey("cms_key");

		Response response = FastHttpClient.post().url(url).addParams("username", user_name)
				.addParams("password", password).build().execute();
		JSONObject retJson = JSON.parseObject(response.string());
		Map<String, String> headers = null;
		if (retJson.getInteger("code") == 0) {
			String cookie = response.headers().toString().split("sessionid=")[1].split(";")[0];
			url = BsURL.user_account_url;
			cookie = String.format("sessionid=%s;cms_key=%s;", cookie, cms_key);
			response = FastHttpClient.get().url(url).addHeader("cookie", cookie).build().execute();
			retJson = JSON.parseObject(response.string());
			if (retJson.getInteger("code") == 0) {
				String group_id = response.headers().toString().split("group_id=")[1].split(";")[0];
				cookie = String.format("%s;group_id=%s;", cookie, group_id);
				headers = new HashMap<String, String>();
				headers.put("cookie", cookie);
			}
		} else {
			logger.error("登录失败: " + retJson);
		}
		return headers;

	}

	/**
	 * 输入账户密码登录bshop
	 * 
	 * @param user_name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginBshop(String user_name, String password) throws Exception {
		logger.info("登录商城");
		String url = bshop_url + "/login";
		String cms_key = ConfigureUtil.getValueByKey("cms_key");

		Response response = FastHttpClient.post().url(url).addParams("username", user_name)
				.addParams("password", password).build().execute();
		JSONObject retJson = JSON.parseObject(response.string());
		Map<String, String> headers = null;
		if (retJson.getInteger("code") == 0) {
			String cookie = response.headers().toString().split("sessionid=")[1].split(";")[0];
			url = BsURL.user_account_url;
			cookie = String.format("sessionid=%s;cms_key=%s;", cookie, cms_key);
			response = FastHttpClient.get().url(url).addHeader("cookie", cookie).build().execute();
			retJson = JSON.parseObject(response.string());
			if (retJson.getInteger("code") == 0) {
				String group_id = response.headers().toString().split("group_id=")[1].split(";")[0];
				cookie = String.format("%s;group_id=%s;", cookie, group_id);
				headers = new HashMap<String, String>();
				headers.put("cookie", cookie);
			}
		} else {
			logger.error("登录失败: " + retJson);
		}
		return headers;

	}

	/**
	 * 使用账号密码登入司机APP
	 * 
	 * @param phone
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginDriverApp(String phone, String pwd) throws Exception {
		String urlStr = station_url + "/driver/login";
		Map<String, String> headers = null;
		Response response = FastHttpClient.post().url(urlStr).addParams("phone", phone).addParams("psw", pwd).build()
				.execute();
		JSONObject retJson = JSON.parseObject(response.string());
		if (retJson.getInteger("code") == 0) {
			String headerStr = response.headers().toString();
			String session_id = headerStr.split("sessionid=")[1].split(";")[0];
			String group_id = headerStr.split("group_id=")[1].split(";")[0];
			StringBuffer driver_cookie = new StringBuffer();
			driver_cookie.append("sessionid=");
			driver_cookie.append(session_id);
			driver_cookie.append(";");
			driver_cookie.append("group_id=");
			driver_cookie.append(group_id);
			headers = new HashMap<>();
			headers.put("cookie", driver_cookie.toString());
			headers.put("id", retJson.getJSONObject("data").getString("id"));
			headers.put("token", retJson.getJSONObject("data").getString("token"));
		}
		return headers;
	}

	/**
	 * 采购助手登录
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> loginPurchaseAssistant(String user_name, String password) throws Exception {
		String url = station_url + "/purchase_assistant/login";
		Map<String, String> headers = login(url, user_name, password);
		return headers;
	}

}
