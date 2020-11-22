package cn.guanmai.open.impl.product;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.bean.product.param.OpenSalemenuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSalemenuUpdateParam;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.url.OpenProductURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class OpenSalemenuServiceImpl implements OpenSalemenuService {
	private OpenRequest openRequest;

	public OpenSalemenuServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenSalemenuBean> searchSalemenu(String customer_id, Integer is_active) throws Exception {
		String url = OpenProductURL.salemenu_list_url;

		Map<String, String> paramMap = new HashMap<>();
		if (customer_id != null)
			paramMap.put("customer_id", customer_id);
		if (is_active != null)
			paramMap.put("is_active", String.valueOf(is_active));

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		List<OpenSalemenuBean> salemenuList = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray salemenu_list = retObj.getJSONArray("data");
			salemenuList = JsonUtil.strToClassList(salemenu_list.toString(), OpenSalemenuBean.class);
		}
		return salemenuList;
	}

	@Override
	public String createSalemenu(OpenSalemenuCreateParam openSalemenuCreateParam) throws Exception {
		String url = OpenProductURL.salemenu_create_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openSalemenuCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("salemenu_id") : null;
	}

	@Override
	public boolean updateSalemenu(OpenSalemenuUpdateParam openSalemenuUpdateParam) throws Exception {
		String url = OpenProductURL.salemenu_update_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openSalemenuUpdateParam);

		return retObj.getInteger("code") == 0;

	}

}
