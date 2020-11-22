package cn.guanmai.station.impl.system;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.AreaBean;
import cn.guanmai.station.interfaces.system.AreaService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Apr 3, 2019 4:35:49 PM 
* @todo TODO
* @version 1.0 
*/
public class AreaServiceImpl implements AreaService {
	private BaseRequest baseRequest;

	public AreaServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<AreaBean> getAreaDict() throws Exception {
		String urlStr = SystemURL.area_dict_url;

		JSONObject retJson = baseRequest.baseRequest(urlStr, RequestType.GET);

		return retJson.getInteger("code") == 0
				? JsonUtil.strToClassList(retJson.getJSONArray("data").toString(), AreaBean.class)
				: null;
	}

}
