package cn.guanmai.station.impl.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.order.param.OrderPurchaseTaskParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.purchase.PrioritySupplierBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSummaryBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSupplierBean;
import cn.guanmai.station.bean.purchase.PurchaseSpecSuppliersBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskCanChangeSupplierBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskHistoryBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskPrintBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskShareBean;
import cn.guanmai.station.bean.purchase.SupplyLimitBean;
import cn.guanmai.station.bean.purchase.param.PrioritySupplierFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskHistoryFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskPrintParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskShareGetParam;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.purchase.param.SupplyLimitFilterParam;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.url.PurchaseURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Nov 16, 2018 10:18:40 AM 
* @todo TODO
* @version 1.0 
*/
public class PurchaseTaskServiceImpl implements PurchaseTaskService {
	private BaseRequest baseRequest;

	public PurchaseTaskServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public PurchaseTaskBean searchPurchaseTask(PurchaseTaskFilterParam param) throws Exception {
		String urlStr = PurchaseURL.purchase_task_search_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, param);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.toJSONString(), PurchaseTaskBean.class)
				: null;

	}

	@Override
	public List<PurchaseTaskData> newSearchPurchaseTask(PurchaseTaskFilterParam param) throws Exception {
		String urlStr = PurchaseURL.purchase_task_search_new_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, param);

		return retObj.getInteger("code") == 0
				? (retObj.getString("data") != null && !retObj.getString("data").equals("null")
						? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PurchaseTaskData.class)
						: new ArrayList<PurchaseTaskData>())
				: null;
	}

	@Override
	public List<PurchaseTaskHistoryBean> getPurchaseTaskHistorys(PurchaseTaskHistoryFilterParam filterParam)
			throws Exception {
		String urlStr = PurchaseURL.purchase_task_history_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PurchaseTaskHistoryBean.class)
				: null;
	}

	@Override
	public PurcahseTaskSummaryBean getPurcahseTaskSummary(PurchaseTaskFilterParam param) throws Exception {
		String url = PurchaseURL.purchase_task_summary_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		return retObj.getInteger("code") == 0 ? (retObj.get("data") instanceof JSONObject
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toJSONString(), PurcahseTaskSummaryBean.class)
				: new PurcahseTaskSummaryBean()) : null;

	}

	@Override
	public List<SupplyLimitBean> searchSupplyLimit(SupplyLimitFilterParam param) throws Exception {
		String url = PurchaseURL.purchase_supply_limit_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), SupplyLimitBean.class)
				: null;
	}

	@Override
	public boolean exportPurchaseTask(PurchaseTaskFilterParam param) throws Exception {
		String url = PurchaseURL.export_purcahse_task_url;

		String file_name = baseRequest.baseExport(url, RequestType.GET, param, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public BigDecimal exportPurchaseTaskV2(PurchaseTaskFilterParam param) throws Exception {
		String url = PurchaseURL.export_purcahse_task_v2_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public boolean releasePurchaseTask(ReleasePurchaseTaskParam param) throws Exception {
		String urlStr = PurchaseURL.release_purchase_task_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, param);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public JSONArray createPruchaseSheet(PurchaseSheetCreateParam param) throws Exception {
		String urlStr = PurchaseURL.create_purchase_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, param);

		if (retObj.getInteger("code") == 0) {
			JSONArray sheetIdArray = retObj.getJSONArray("data");
			Reporter.log("生成的采购单据列表: " + sheetIdArray);
			return sheetIdArray;
		} else {
			return null;
		}

	}

	@Override
	public List<PurcahseTaskSupplierBean> getPurcahseTaskSuppliers(String spec_id) throws Exception {
		String url = PurchaseURL.purcahse_task_suppliers_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("spec_id", spec_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("supply").toJSONString(),
						PurcahseTaskSupplierBean.class)
				: null;
	}

	@Override
	public Map<String, String> getPurchaseTaskSettleSuppliers() throws Exception {
		String url = PurchaseURL.purcahse_task_settle_suppliers_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);
		int code = retObj.getInteger("code");
		Map<String, String> settleSupplierMap = null;
		if (code == 0) {
			settleSupplierMap = new HashMap<String, String>();
			JSONArray dataArray = retObj.getJSONArray("data");
			for (Object obj : dataArray) {
				JSONObject dataObj = JSONObject.parseObject(obj.toString());
				settleSupplierMap.put(dataObj.getString("id"), dataObj.getString("name"));
			}
		}
		return settleSupplierMap;
	}

	@Override
	public boolean createPurchaseTasks(List<PurchaseTaskCreateParam> tasks) throws Exception {
		String urlStr = PurchaseURL.create_purchase_task_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tasks", JsonUtil.objectToStr(tasks));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PurchaseSpecSuppliersBean> searchPurchaseSpecSuppliers(String spu_id) throws Exception {
		String urlStr = PurchaseURL.purchaseSpec_suppliers_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PurchaseSpecSuppliersBean.class)
				: null;
	}

	@Override
	public List<String> optionalSupplierSurchasers(String settle_supplier_id) throws Exception {
		String url = PurchaseURL.optional_suppliers_purchasers_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<String> purcahser_ids = null;
		if (retObj.getInteger("code") == 0) {
			purcahser_ids = new ArrayList<String>();
			for (Object obj : retObj.getJSONObject("data").getJSONArray("optional_purchasers")) {
				purcahser_ids.add(JSONObject.parseObject(obj.toString()).getString("purchaser_id"));
			}
		}
		return purcahser_ids;
	}

	@Override
	public List<PurchaseTaskPrintBean> purchaseTaskPrint(PurchaseTaskPrintParam purchaseTaskPrintParam)
			throws Exception {
		String urlStr = PurchaseURL.purchase_print_task_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, purchaseTaskPrintParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PurchaseTaskPrintBean.class)
				: null;
	}

	@Override
	public boolean purchaseTaskChangePurchaser(List<String> task_ids, String purchaser_id) throws Exception {
		String urlStr = PurchaseURL.purcahse_task_change_parchaser_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ids", JSONArray.toJSONString(task_ids));
		paramMap.put("purchaser_id", purchaser_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean purchaseTaskChangeSupplier(List<String> task_ids, String settle_supplier_id) throws Exception {
		String urlStr = PurchaseURL.purcahse_task_change_parchaser_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ids", JSONArray.toJSONString(task_ids));
		paramMap.put("settle_supplier_id", settle_supplier_id);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PurchaseTaskCanChangeSupplierBean> searchPurchaseTaskCanChangeSuppliers(int q_type, String begin_time,
			String end_time, String spec_id) throws Exception {
		String url = PurchaseURL.purchase_task_can_change_settle_suppliers_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q_type", String.valueOf(q_type));
		paramMap.put("begin_time", begin_time);
		paramMap.put("end_time", end_time);
		paramMap.put("spec_id", spec_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(),
				PurchaseTaskCanChangeSupplierBean.class) : null;
	}

	@Override
	public boolean purchaseTaskChangeSupplyLimit(String settle_supplier_id, String spec_id, BigDecimal limit)
			throws Exception {
		String urlStr = PurchaseURL.purchase_task_change_supply_limit_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_id", settle_supplier_id);
		paramMap.put("spec_id", spec_id);
		paramMap.put("limit", String.valueOf(limit));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PrioritySupplierBean> queryPrioritySupplier(PrioritySupplierFilterParam filterParam) throws Exception {
		String urlStr = PurchaseURL.priority_supplier_list_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PrioritySupplierBean.class)
				: null;
	}

	@Override
	public String createSharePurchaseTask(PurchaseTaskShareCreateParam purchaseTaskShareParam) throws Exception {
		String urlStr = PurchaseURL.create_share_purcahse_task_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, purchaseTaskShareParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("token") : null;
	}

	@Override
	public List<PurchaseTaskShareBean> getSharePurchaseTask(PurchaseTaskShareGetParam purchaseTaskShareGetParam)
			throws Exception {
		String urlStr = PurchaseURL.get_share_purcahse_task_url;

		JSONObject retObj = baseRequest.baseRequestWithoutCookie(urlStr, RequestType.GET, purchaseTaskShareGetParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toJSONString(), PurchaseTaskShareBean.class)
				: null;
	}

	@Override
	public BigDecimal createPurchaseTaskByOrder(OrderSkuFilterParam orderSkuFilterParam) throws Exception {
		String url = PurchaseURL.create_purchase_task_by_order_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderSkuFilterParam);
		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

	@Override
	public BigDecimal createPurchaseTaskByOrder(List<OrderPurchaseTaskParam> orderPurchaseTaskParams) throws Exception {
		String url = PurchaseURL.create_purchase_task_by_order_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("tasks", JsonUtil.objectToStr(orderPurchaseTaskParams));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("task_id"))
				: null;
	}

}
