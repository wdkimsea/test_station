package cn.guanmai.station.impl.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.SmartMenuBean;
import cn.guanmai.station.bean.category.SmartMenuDetailBean;
import cn.guanmai.station.bean.category.param.SmartMenuParam;
import cn.guanmai.station.interfaces.category.SmartMenuService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年3月3日 上午10:49:31
 * @description:
 * @version: 1.0
 */

public class SmartMenuServiceImpl implements SmartMenuService {
	private BaseRequest baseRequest;

	public SmartMenuServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createSmartMenu(SmartMenuParam smartMenuParam) throws Exception {
		String url = CategoryURL.create_smart_menu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, smartMenuParam);
		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public boolean editSmartMenu(SmartMenuParam smartMenuParam) throws Exception {
		String url = CategoryURL.edit_samrt_menu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, smartMenuParam);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public SmartMenuDetailBean getSmartMenuDetail(String id) throws Exception {
		String url = CategoryURL.smart_menu_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SmartMenuDetailBean.class)
				: null;
	}

	@Override
	public List<SmartMenuBean> searchSmartMenu(String search_text, int limit, int offset) throws Exception {
		String url = CategoryURL.smart_menu_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		if (search_text != null) {
			paramMap.put("search_text", search_text);
		}
		paramMap.put("limit", String.valueOf(limit));
		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("peek", "60");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SmartMenuBean.class)
				: null;
	}

	@Override
	public boolean deleteSmartMenu(String id) throws Exception {
		String url = CategoryURL.delete_smart_meun_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public SmartMenuDetailBean printSmartMenu(String id) throws Exception {
		String url = CategoryURL.print_smart_menu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SmartMenuDetailBean.class)
				: null;
	}

}
