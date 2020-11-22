package cn.guanmai.open.impl.product;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.product.OpenServiceTimeBean;
import cn.guanmai.open.interfaces.product.OpenServiceTimeService;
import cn.guanmai.open.url.OpenProductURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年2月7日 上午11:28:32
 * @description:
 * @version: 1.0
 */

public class OpenServiceTimeServiceImpl implements OpenServiceTimeService {
	private OpenRequest openRequest;

	public OpenServiceTimeServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenServiceTimeBean> getServiceTimes() throws Exception {
		String url = OpenProductURL.service_time_list_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenServiceTimeBean.class)
				: null;
	}

}
