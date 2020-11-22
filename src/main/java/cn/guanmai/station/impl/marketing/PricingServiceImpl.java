package cn.guanmai.station.impl.marketing;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.marketing.SmartPricingSkuBean;
import cn.guanmai.station.bean.marketing.param.SmartFormulaPricingParam;
import cn.guanmai.station.bean.marketing.param.SmartPricingSkuFilterParam;
import cn.guanmai.station.bean.marketing.param.SmartPricingUpdateParam;
import cn.guanmai.station.interfaces.marketing.PricingService;
import cn.guanmai.station.url.MarketingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date May 29, 2019 5:59:29 PM 
* @todo TODO
* @version 1.0 
*/
public class PricingServiceImpl implements PricingService {
	private BaseRequest baseRequest;

	public PricingServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<SmartPricingSkuBean> getSmartPricingSkuList(SmartPricingSkuFilterParam filterParam) throws Exception {
		String url = MarketingURL.get_smart_pricing_sku_list;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, filterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("sku_list").toString(), SmartPricingSkuBean.class) : null;
	}

	@Override
	public String updateSmartPricing(SmartPricingUpdateParam updateParam) throws Exception {
		String url = MarketingURL.update_smart_pricing_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, updateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

	@Override
	public boolean updateSmartFormulaPricing(SmartFormulaPricingParam smartFormulaPricingParam) throws Exception {
		String url = MarketingURL.update_smart_formula_pricint_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, smartFormulaPricingParam);

		return retObj.getInteger("code") == 0;
	}

}
