package cn.guanmai.open.impl.customer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.customer.OpenCustomerAreaBean;
import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.customer.param.OpenCustomerCreateParam;
import cn.guanmai.open.bean.customer.param.OpenCustomerUpdateParam;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.url.CustomerURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jun 6, 2019 9:56:51 AM 
* @des 商户相关业务接口实现类
* @version 1.0 
*/
public class OpenCustomerServiceImpl implements OpenCustomerService {
	private JSONObject retObj;
	private OpenRequest openRequest;

	public OpenCustomerServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public String createCustomer(OpenCustomerCreateParam createParam) throws Exception {
		String url = CustomerURL.customer_create_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, createParam);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("customer_id") : null;
	}

	@Override
	public boolean updateCustomer(OpenCustomerUpdateParam updateParam) throws Exception {
		String url = CustomerURL.customer_update_url;

		retObj = openRequest.baseRequest(url, RequestType.POST, updateParam);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OpenCustomerBean> searchCustomer(String customer_id, String customer_name, int offset, int limit)
			throws Exception {
		String url = CustomerURL.customer_search_url;

		Map<String, String> paramMap = new HashMap<>();
		if (customer_name != null) {
			paramMap.put("customer_name", customer_name);
		}

		if (customer_id != null) {
			paramMap.put("customer_id", customer_id);
		}

		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("limit", String.valueOf(limit));

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCustomerBean.class)
				: null;
	}

	@Override
	public boolean checkCustomerOrderStatus(String customer_id) throws Exception {
		String url = CustomerURL.customer_order_status_check_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("customer_id", customer_id);

		retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		int code = retObj.getInteger("code");
		if (code == 0) {
			return true;
		} else if (code == 1007) {
			throw new Exception(retObj.getString("msg"));
		} else {
			return false;
		}
	}

	@Override
	public List<OpenCustomerAreaBean> getAreaList() throws Exception {
		String url = CustomerURL.customer_area_list_url;

		retObj = openRequest.baseRequest(url, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenCustomerAreaBean.class)
				: null;
	}

}
