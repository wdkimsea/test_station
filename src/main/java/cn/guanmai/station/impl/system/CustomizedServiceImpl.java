package cn.guanmai.station.impl.system;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.CustomizedBean;
import cn.guanmai.station.interfaces.system.CustomizedService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年9月25日
 * @time 上午10:44:48
 * @des TODO
 */

public class CustomizedServiceImpl implements CustomizedService {
	private BaseRequest baseRequest;

	public CustomizedServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public CustomizedBean getCustomized() throws Exception {
		String url = SystemURL.customized_url;

		JSONObject retJson = baseRequest.baseRequest(url, RequestType.GET);

		return retJson.getInteger("code") == 0
				? JsonUtil.strToClassObject(retJson.getJSONObject("data").toString(), CustomizedBean.class)
				: null;
	}

	@Override
	public String getCmsKey() throws Exception {
		String url = SystemURL.sms_customized_info_url;

		JSONObject retJson = baseRequest.baseRequest(url, RequestType.GET);

		return retJson.getJSONObject("data").getString("cms_key");
	}

	@Override
	public boolean updateCustomized(CustomizedBean customized) throws Exception {
		String url = SystemURL.customized_update_url;

		if (customized.getOrder_edit_time_limit() == 0) {
			customized.setOrder_edit_time_limit(null);
		}
		JSONObject retJson = baseRequest.baseRequest(url, RequestType.POST, customized);

		return retJson.getInteger("code") == 0;
	}

}
