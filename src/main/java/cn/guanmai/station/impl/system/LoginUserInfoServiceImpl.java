package cn.guanmai.station.impl.system;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Apr 11, 2019 10:51:23 AM 
* @todo TODO
* @version 1.0 
*/
public class LoginUserInfoServiceImpl implements LoginUserInfoService {
	private BaseRequest baseRequest;

	public LoginUserInfoServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public LoginUserInfoBean getLoginUserInfo() throws Exception {
		String url = SystemURL.station_user_info_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), LoginUserInfoBean.class)
				: null;
	}

	@Override
	public LoginStationInfoBean getLoginStationInfo() throws Exception {
		String url = SystemURL.station_info_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), LoginStationInfoBean.class)
				: null;
	}

}
