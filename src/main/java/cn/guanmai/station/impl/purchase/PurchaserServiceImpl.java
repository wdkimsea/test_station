package cn.guanmai.station.impl.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.url.PurchaseURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Nov 27, 2018 11:04:54 AM 
* @des 采购员相关业务实现类
* @version 1.0 
*/
public class PurchaserServiceImpl implements PurchaserService {
	private BaseRequest baseRequest;

	public PurchaserServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<PurchaserBean> searchPurchaser(String search_text) throws Exception {
		String urlStr = PurchaseURL.search_purchaser_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			return JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PurchaserBean.class);
		} else {
			return null;
		}
	}

	@Override
	public PurchaserBean getPurchaserDetail(String id) throws Exception {
		String urlStr = PurchaseURL.get_purchaser_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaserBean.class)
				: null;
	}

	@Override
	public PurchaserResponseBean createPurchaser(PurchaserParam purchaserParam) throws Exception {
		String urlStr = PurchaseURL.create_purchaser_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, purchaserParam);

		return JsonUtil.strToClassObject(retObj.toString(), PurchaserResponseBean.class);
	}

	@Override
	public boolean deletePurchaser(String id) throws Exception {
		String urlStr = PurchaseURL.delete_purchaser_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public PurchaserResponseBean updatePurchaser(PurchaserParam purchaserParam) throws Exception {
		String urlStr = PurchaseURL.update_purchaser_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, purchaserParam);

		return JsonUtil.strToClassObject(retObj.toString(), PurchaserResponseBean.class);
	}

	@Override
	public boolean purchaserLoginStation(String phone, String pwd) throws Exception {
		String urlStr = PurchaseURL.purchaser_login_st_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", phone);
		paramMap.put("password", pwd);
		paramMap.put("this_is_the_login_form", "1");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SettleSupplier> getNoBindSettleSupplierArray() throws Exception {
		String urlStr = PurchaseURL.no_bind_settle_suppliers_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		return JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettleSupplier.class);
	}

}
