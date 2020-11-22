package cn.guanmai.station.impl.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.TaxRuleBean;
import cn.guanmai.station.interfaces.category.TaxRuleService;
import cn.guanmai.station.url.CategoryURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 18, 2019 7:29:55 PM 
* @des 税率规则相关业务实现类
* @version 1.0 
*/
public class TaxRuleServiceImpl implements TaxRuleService {
	private BaseRequest baseRequest;

	public TaxRuleServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<TaxRuleBean> getTaxRuleList(Integer status, String search_text) throws Exception {
		String url = CategoryURL.get_tax_rule_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		if (status != null) {
			paramMap.put("status", String.valueOf(status));
		}
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		List<TaxRuleBean> taxRuleList = null;
		if (retObj.getInteger("code") == 0) {
			taxRuleList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TaxRuleBean.class);
		}
		return taxRuleList;
	}

	@Override
	public List<String> searchTaxCustomer(String search_text) throws Exception {
		String url = CategoryURL.search_tax_customer_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		List<String> customerIdList = null;
		if (retObj.getInteger("code") == 0) {
			customerIdList = new ArrayList<String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				JSONObject customer = JSON.parseObject(obj.toString());
				customerIdList.add(customer.getString("address_id"));
			}
		}
		return customerIdList;
	}

	@Override
	public Map<String, String> searchTaxSpu(String search_text) throws Exception {
		String url = CategoryURL.search_tax_spu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		Map<String, String> spuMap = null;
		if (retObj.getInteger("code") == 0) {
			spuMap = new HashMap<String, String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				JSONObject spu = JSON.parseObject(obj.toString());
				spuMap.put(spu.getString("id"), spu.getString("name"));
			}
		}
		return spuMap;
	}

	@Override
	public boolean createTaxRule(TaxRuleBean taxRule) throws Exception {
		String url = CategoryURL.create_tax_rule_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tax_name", taxRule.getTax_rule_name());
		paramMap.put("status", String.valueOf(taxRule.getStatus()));
		paramMap.put("address", JsonUtil.objectToStr(taxRule.getAddress()));
		paramMap.put("spu", JsonUtil.objectToStr(taxRule.getSpu()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public TaxRuleBean getTaxRuleDetailInfo(String tax_id) throws Exception {
		String url = CategoryURL.get_tax_rule_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tax_id", tax_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		TaxRuleBean taxRule = null;
		if (retObj.getInteger("code") == 0) {
			taxRule = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), TaxRuleBean.class);
		}
		return taxRule;
	}

	@Override
	public JSONObject editTaxRule(TaxRuleBean taxRule) throws Exception {
		String url = CategoryURL.edit_tax_rule_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tax_id", taxRule.getTax_rule_id());
		paramMap.put("tax_name", taxRule.getTax_rule_name());
		paramMap.put("status", String.valueOf(taxRule.getStatus()));
		paramMap.put("address", JsonUtil.objectToStr(taxRule.getAddress()));
		paramMap.put("spu", JsonUtil.objectToStr(taxRule.getSpu()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj;

	}

	@Override
	public boolean taxSpuList(String search_text, Integer status) throws Exception {
		String url = CategoryURL.tax_spu_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);
		if (status != null) {
			paramMap.put("status", String.valueOf(status));
		}

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public Map<BigDecimal, String> searchAddressByLabel(String label_id) throws Exception {
		String url = CategoryURL.tax_label_address_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("address_label_id", label_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		Map<BigDecimal, String> addressMap = null;
		if (retObj.getInteger("code") == 0) {
			addressMap = new HashMap<BigDecimal, String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			JSONObject dataObj = null;
			BigDecimal address_id = null;
			String address_name = null;
			for (Object obj : dataArray) {
				dataObj = JSON.parseObject(obj.toString());
				address_id = new BigDecimal(dataObj.getString("address_id"));
				address_name = dataObj.getString("address_name");
				addressMap.put(address_id, address_name);
			}
		}
		return addressMap;
	}

}
