package cn.guanmai.station.impl.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.purchase.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetShareBean;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetShareParam;
import cn.guanmai.station.interfaces.purchase.PurchaseSheetService;
import cn.guanmai.station.url.PurchaseURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Nov 28, 2018 7:02:11 PM 
* @des 采购单据业务实现类
* @version 1.0 
*/
public class PurchaseSheetServiceImpl implements PurchaseSheetService {
	private BaseRequest baseRequest;

	public PurchaseSheetServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<PurchaseSheetBean> purchaseSheetArray(String start_time, String end_time, String sheet_no,
			String settle_supplier_id, String status) throws Exception {
		String urlStr = PurchaseURL.purchase_sheet_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("start_time", start_time);
		paramMap.put("end_time", end_time);
		paramMap.put("sheet_no", sheet_no);
		paramMap.put("settle_supplier_id", settle_supplier_id);
		paramMap.put("status", status);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		JSONArray dataArray = retObj.getJSONArray("data");

		return JsonUtil.strToClassList(dataArray.toString(), PurchaseSheetBean.class);
	}

	@Override
	public boolean deletePurchaseSheet(String sheet_no) throws Exception {
		String urlStr = PurchaseURL.delete_purchase_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", sheet_no);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public PurchaseSheetDetailBean getPurchaseSheetDetail(String sheet_no) throws Exception {
		String urlStr = PurchaseURL.purchase_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", sheet_no);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		PurchaseSheetDetailBean purchaseSheetDetail = JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(),
				PurchaseSheetDetailBean.class);
		return purchaseSheetDetail;
	}

	@Override
	public boolean submitPurchaseSheet(String sheet_no) throws Exception {
		String urlStr = PurchaseURL.submit_purchase_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", sheet_no);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createSharePurchaseSheet(String sheet_no) throws Exception {
		String urlStr = PurchaseURL.create_share_purchase_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", sheet_no);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("token") : null;
	}

	@Override
	public PurchaseSheetShareBean getSharePurchaseSheet(PurchaseSheetShareParam purchaseSheetShareParam)
			throws Exception {
		String urlStr = PurchaseURL.get_share_purchase_sheet_url;

		JSONObject retObj = baseRequest.baseRequestWithoutCookie(urlStr, RequestType.GET, purchaseSheetShareParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaseSheetShareBean.class)
				: null;
	}

}
