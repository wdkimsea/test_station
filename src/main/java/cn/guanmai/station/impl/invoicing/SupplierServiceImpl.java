package cn.guanmai.station.impl.invoicing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.SupplierAccountBean;
import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.url.SupplierURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Nov 7, 2018 4:18:08 PM 
* @todo TODO
* @version 1.0 
*/
public class SupplierServiceImpl implements SupplierService {
	private BaseRequest baseRequest;

	public SupplierServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<SupplierBean> searchSupplier(String search_text) throws Exception {
		String url = SupplierURL.search_supplier_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		if (search_text != null && !search_text.equals("")) {
			paramMap.put("search_text", search_text);
		}

		int offset = 0;
		int limit = 20;
		paramMap.put("limit", String.valueOf(limit));
		JSONObject retObj = null;
		List<SupplierBean> suppliers = new ArrayList<>();
		while (true) {
			paramMap.put("offset", String.valueOf(offset));
			retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
			if (retObj.getInteger("code") == 0) {
				List<SupplierBean> tempArray = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						SupplierBean.class);
				suppliers.addAll(tempArray);
				if (tempArray.size() < limit) {
					break;
				}
				offset += limit;
			} else {
				suppliers = null;
				break;
			}
		}
		return suppliers;
	}

	@Override
	public String createSupplier(SupplierDetailBean supplier) throws Exception {
		String urlStr = SupplierURL.create_supplier_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, supplier);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public SupplierDetailBean getSupplierById(String id) throws Exception {
		SupplierDetailBean supplier = null;
		String urlStr = SupplierURL.get_supplier_info_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			supplier = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SupplierDetailBean.class);
		}
		return supplier;
	}

	@Override
	public boolean updateSupplier(SupplierDetailBean supplier) throws Exception {
		String urlStr = SupplierURL.update_supplier_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, supplier);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSupplier(String id) throws Exception {
		String urlStr = SupplierURL.delete_supplier_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SupplierDetailBean> getSupplierByCustomerId(String customer_id) throws Exception {
		String urlStr = SupplierURL.search_supplier_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");
		paramMap.put("search_text", customer_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);
		List<SupplierDetailBean> supplierBeanList = null;
		if (retObj.getInteger("code") == 0) {
			supplierBeanList = JsonUtil.strToClassList(retObj.getString("data"), SupplierDetailBean.class);

		}
		return supplierBeanList;
	}

	@Override
	public List<SupplierDetailBean> getSettleSupplierList() throws Exception {
		String urlStr = SupplierURL.get_supplier_list_url;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);
		List<SupplierDetailBean> supplierList = null;
		if (retObj.getInteger("code") == 0) {
			supplierList = JsonUtil.strToClassList(
					retObj.getJSONArray("data").getJSONObject(0).getJSONArray("settle_suppliers").toString(),
					SupplierDetailBean.class);
		}
		return supplierList;
	}

	@Override
	public List<SupplierAccountBean> getSupplierAccounts() throws Exception {
		String url = SupplierURL.supplier_account_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SupplierAccountBean.class)
				: null;
	}

}
