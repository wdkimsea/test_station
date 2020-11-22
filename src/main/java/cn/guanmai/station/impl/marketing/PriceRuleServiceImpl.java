package cn.guanmai.station.impl.marketing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.marketing.PriceRuleBean;
import cn.guanmai.station.bean.marketing.PriceRuleSkuBean;
import cn.guanmai.station.bean.marketing.param.PriceRuleCreateParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleEditParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleFilterParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleSkuFilterParam;
import cn.guanmai.station.interfaces.marketing.PriceRuleService;
import cn.guanmai.station.url.MarketingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 19, 2019 5:46:59 PM 
* @des 限时锁价相关业务实现类
* @version 1.0 
*/
public class PriceRuleServiceImpl implements PriceRuleService {
	private BaseRequest baseRequest;

	public PriceRuleServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<PriceRuleBean> searchPriceRule(PriceRuleFilterParam filterParam) throws Exception {
		String url = MarketingURL.search_price_rule_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		List<PriceRuleBean> priceRuleList = null;
		if (retObj.getInteger("code") == 0) {
			priceRuleList = JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("list").toString(),
					PriceRuleBean.class);
		}
		return priceRuleList;
	}

	@Override
	public PriceRuleBean getPriceRuleDetailInfo(String price_rule_id) throws Exception {
		String url = MarketingURL.edit_price_rule_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("price_rule_id", price_rule_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		PriceRuleBean priceRule = null;
		if (retObj.getInteger("code") == 0) {
			priceRule = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PriceRuleBean.class);
		}
		return priceRule;
	}

	@Override
	public List<String> searchCustomer(String s, String salemenu_id) throws Exception {
		String url = MarketingURL.search_customer_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("s", s);
		paramMap.put("salemenu_id", salemenu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<String> customers = null;
		if (retObj.getInteger("code") == 0) {
			customers = new ArrayList<String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			if (dataArray != null) {
				for (Object obj : dataArray) {
					customers.add(JSONObject.parseObject(obj.toString()).getString("address_id"));
				}
			}
		}
		return customers;
	}

	@Override
	public List<String> searchSku(String salemenu_id, String search_text) throws Exception {
		String url = MarketingURL.search_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("active", "0");
		paramMap.put("salemenu_id", salemenu_id);
		paramMap.put("search_text", search_text);
		paramMap.put("limit", "20");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<String> skus = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray dataArray = retObj.getJSONArray("data");
			skus = new ArrayList<String>();
			for (Object obj : dataArray) {
				skus.add(JSONObject.parseObject(obj.toString()).getString("id"));
			}
		}
		return skus;
	}

	@Override
	public String createPriceRule(PriceRuleCreateParam createParam) throws Exception {
		String url = MarketingURL.create_price_rule_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, createParam);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;

	}

	@Override
	public JSONObject editPriceRule(PriceRuleEditParam editParam) throws Exception {
		String url = MarketingURL.edit_price_rule_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, editParam);

		return retObj;
	}

	@Override
	public List<PriceRuleSkuBean> searchPriceRuleSku(PriceRuleSkuFilterParam filterParam) throws Exception {
		String url = MarketingURL.search_price_rule_sku_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		List<PriceRuleSkuBean> priceRuleSkuList = null;
		if (retObj.getInteger("code") == 0) {
			priceRuleSkuList = JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("list").toString(),
					PriceRuleSkuBean.class);
		}
		return priceRuleSkuList;
	}

}
