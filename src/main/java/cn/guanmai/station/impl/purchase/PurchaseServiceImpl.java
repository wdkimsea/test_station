package cn.guanmai.station.impl.purchase;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.purchase.PurchaseSpecRefPriceBean;
import cn.guanmai.station.interfaces.purchase.PurchaseService;
import cn.guanmai.station.url.PurchaseURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年3月13日 上午10:29:53
 * @description:
 * @version: 1.0
 */

public class PurchaseServiceImpl implements PurchaseService {
	private BaseRequest baseRequest;

	public PurchaseServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public PurchaseSpecRefPriceBean getPurchaseSpecRefPrice(String spu_id, String purchase_spec_id,
			String settle_supplier_id) throws Exception {
		String url = PurchaseURL.purchase_spec_ref_price_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("spec_id", purchase_spec_id);
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaseSpecRefPriceBean.class)
				: null;
	}

}
