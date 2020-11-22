package cn.guanmai.station.impl.category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.SpuRemarkBean;
import cn.guanmai.station.bean.category.param.SpuRemarkFiterParam;
import cn.guanmai.station.interfaces.category.SpuRemarkService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 18, 2019 5:18:02 PM 
* @des 商品备注相关业务实现类
* @version 1.0 
*/
public class SpuRemarkServiceImpl implements SpuRemarkService {

	private BaseRequest baseRequest;

	public SpuRemarkServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public Map<String, String> searchCustomer(String keyword, int offset, int limit) throws Exception {
		String url = CategoryURL.spu_remark_customer_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("keyword", keyword);
		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("limit", String.valueOf(limit));
		Map<String, String> customerMap = null;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		if (retObj.getInteger("code") == 0) {
			customerMap = new HashMap<String, String>();
			JSONArray customerList = retObj.getJSONObject("data").getJSONArray("list");
			for (Object obj : customerList) {
				JSONObject customerObj = JSON.parseObject(obj.toString());
				customerMap.put(customerObj.getString("address_id"), customerObj.getString("resname"));
			}
		}
		return customerMap;
	}

	@Override
	public List<SpuRemarkBean> searchSpuRemark(SpuRemarkFiterParam spuRemarkFiterParam) throws Exception {
		String url = CategoryURL.spu_remark_spu_search_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, spuRemarkFiterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("list").toString(), SpuRemarkBean.class) : null;
	}

	@Override
	public boolean updateSpuRemark(String address_id, String spu_id, String remark) throws Exception {
		String url = CategoryURL.update_spu_remark_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("address_id", address_id);
		paramMap.put("spu_id", spu_id);
		paramMap.put("remark", remark);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public Map<String, String> getCustomerMap() throws Exception {
		String url = CategoryURL.spu_remark_customer_search_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("keyword", "");
		paramMap.put("offset", "0");
		paramMap.put("limit", "20");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		Map<String, String> customerMap = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray customerList = retObj.getJSONObject("data").getJSONArray("list");
			Random random = new Random();
			JSONObject costomerObj = customerList.getJSONObject(random.nextInt(customerList.size()));
			customerMap = new HashMap<String, String>();
			customerMap.put(costomerObj.getString("address_id"), costomerObj.getString("resname"));
		}

		return customerMap;
	}
}
