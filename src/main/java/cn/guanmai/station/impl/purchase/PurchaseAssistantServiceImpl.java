package cn.guanmai.station.impl.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.assistant.DailyCountBean;
import cn.guanmai.station.bean.purchase.assistant.DailyWorkBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseAssistantTaskBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetCountBean;
import cn.guanmai.station.bean.purchase.assistant.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierCountBean;
import cn.guanmai.station.bean.purchase.assistant.SupplierSpecBean;
import cn.guanmai.station.bean.purchase.assistant.SupplySkuBean;
import cn.guanmai.station.bean.purchase.param.assistant.SheetCreateParam;
import cn.guanmai.station.bean.purchase.param.assistant.TaskFiterParam;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.interfaces.purchase.PurchaseAssistantService;
import cn.guanmai.station.url.PurchaseURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date May 23, 2019 10:56:42 AM 
* @des 采购助手功能实现类
* @version 1.0 
*/
public class PurchaseAssistantServiceImpl implements PurchaseAssistantService {
	private BaseRequest baseRequest;

	public PurchaseAssistantServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public PurchaserBean getPurchaserInfo() throws Exception {
		String url = PurchaseURL.get_purchase_assistant_info_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaserBean.class)
				: null;
	}

	@Override
	public List<String> getQuotedSettleSuppliers() throws Exception {
		String url = PurchaseURL.quoted_settle_suppliers_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("limit", "200");
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<String> settle_supplier_ids = null;
		if (retObj.getInteger("code") == 0) {
			settle_supplier_ids = new ArrayList<>();
			for (Object obj : retObj.getJSONArray("data")) {
				settle_supplier_ids.add(JSONObject.parseObject(obj.toString()).getString("supplier_id"));
			}
		}
		return settle_supplier_ids;
	}

	@Override
	public List<ServiceTimeBean> getPurchaseAssistantServiceTime() throws Exception {
		String url = PurchaseURL.purchase_assistant_service_time_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ServiceTimeBean.class)
				: null;
	}

	@Override
	public DailyWorkBean getPurchaseAssistantDailyWork() throws Exception {
		String url = PurchaseURL.daily_work_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DailyWorkBean.class)
				: null;
	}

	@Override
	public DailyCountBean getPurchaseAssistantDailyCount(int days) throws Exception {
		String url = PurchaseURL.daily_count_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("days", String.valueOf(days));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DailyCountBean.class)
				: null;
	}

	@Override
	public SupplierCountBean getPurchaseAssistantSupplierCount(int days) throws Exception {
		String url = PurchaseURL.supplier_count_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("days", String.valueOf(days));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SupplierCountBean.class)
				: null;
	}

	@Override
	public List<SupplierSpecBean> getSupplierSpecs(String supplier_id, int with_task, String search_text)
			throws Exception {
		String url = PurchaseURL.supplier_spec_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("supplier_id", supplier_id);
		paramMap.put("with_task", String.valueOf(with_task));
		paramMap.put("search_text", search_text);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SupplierSpecBean.class)
				: null;
	}

	@Override
	public boolean updateSpecQuotedPrice(String spec_id, BigDecimal price, String remark, String origin_place,
			String settle_supplier_id) throws Exception {
		String url = PurchaseURL.update_spec_quoted_price_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("spec_id", spec_id);
		paramMap.put("price", String.valueOf(price.multiply(new BigDecimal("100"))));
		paramMap.put("remark", remark);
		paramMap.put("origin_place", origin_place);
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PurchaseAssistantTaskBean> searchPurchaseAssistantTask(TaskFiterParam filterParam) throws Exception {
		String url = PurchaseURL.search_purchase_assistant_task_url;

		List<PurchaseAssistantTaskBean> purchaseAssistantTasks = new ArrayList<PurchaseAssistantTaskBean>();
		boolean more = true;
		while (more) {
			JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
			if (retObj.getInteger("code") == 0) {
				purchaseAssistantTasks.addAll(JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						PurchaseAssistantTaskBean.class));
			} else {
				more = false;
				purchaseAssistantTasks = null;
			}
			more = retObj.getJSONObject("pagination").getBoolean("more");
			if (more) {
				filterParam.setPage_obj(retObj.getJSONObject("pagination").getString("page_obj"));
			}
		}
		return purchaseAssistantTasks;
	}

	@Override
	public List<String> createPurchaseAssistantSheet(SheetCreateParam param) throws Exception {
		String url = PurchaseURL.create_purcahse_assistant_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), String.class)
				: null;
	}

	@Override
	public List<SupplySkuBean> getSettleSupplierSupplySkus(String settle_supplier_id) throws Exception {
		String url = PurchaseURL.settle_supplier_supply_skus_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SupplySkuBean.class)
				: null;
	}

	@Override
	public PurchaseSheetCountBean getPurchaseSheetCountInfo(String begin_time, String end_time, int status)
			throws Exception {
		String url = PurchaseURL.get_purchase_sheet_count_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("status", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaseSheetCountBean.class)
				: null;
	}

	@Override
	public List<PurchaseSheetBean> getPurchaseSheets(String begin_time, String end_time, int sort_type, int status)
			throws Exception {
		String url = PurchaseURL.get_purchase_sheets_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("status", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PurchaseSheetBean.class)
				: null;
	}

	@Override
	public PurchaseSheetDetailBean getPurchaseSheetDetail(String sheet_no) throws Exception {
		String url = PurchaseURL.get_purchase_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sheet_no", sheet_no);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PurchaseSheetDetailBean.class)
				: null;
	}

	@Override
	public boolean modifyPurchaseSheet(String sheet_no, PurchaseSheetDetailBean purchaseSheetDetail) throws Exception {
		String url = PurchaseURL.update_purchase_assistant_sheet_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("purchase_sheet_id", sheet_no);
		paramMap.put("settle_supplier_id", purchaseSheetDetail.getPurchase_sheet().getSettle_supplier_id());
		paramMap.put("details", JsonUtil.objectToStr(purchaseSheetDetail.getTasks()));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePurchaseSheet(String sheet_no) throws Exception {
		String url = PurchaseURL.delete_purchase_assistant_sheet_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sheet_no", sheet_no);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean submitPurchaseAssistantSheet(String sheet_no) throws Exception {
		String url = PurchaseURL.submit_purchase_assistant_sheet_url;
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sheet_no", sheet_no);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean finishTask(List<BigDecimal> release_ids) throws Exception {
		String url = PurchaseURL.finish_task_url;
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("release_ids", JSONArray.toJSONString(release_ids));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
