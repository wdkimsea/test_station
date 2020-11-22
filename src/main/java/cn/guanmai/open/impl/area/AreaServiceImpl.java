package cn.guanmai.open.impl.area;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.area.AreaBean;
import cn.guanmai.open.interfaces.area.AreaService;
import cn.guanmai.open.url.AreaURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 6, 2019 10:44:21 AM 
* @todo TODO
* @version 1.0 
*/
public class AreaServiceImpl implements AreaService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public AreaServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<AreaBean> getAreaList() throws Exception {
		String url = AreaURL.area_list_url;

		retObj = openRequest.baseRequest(url, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), AreaBean.class)
				: null;
	}

}
