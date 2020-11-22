package cn.guanmai.station.impl.invoicing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.SettleSheetDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementBean;
import cn.guanmai.station.bean.invoicing.SettlementCollectBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailBean;
import cn.guanmai.station.bean.invoicing.SettlementDetailPageBean;
import cn.guanmai.station.bean.invoicing.SettleSheetBean;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailSubmitParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetDetailFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettleSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.SettlementFilterParam;
import cn.guanmai.station.interfaces.invoicing.SupplierFinanceService;
import cn.guanmai.station.url.SupplierURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Mar 28, 2019 2:41:37 PM 
* @des 供应商结款相关业务实现类
* @version 1.0 
*/
public class SupplierFinanceServiceImpl implements SupplierFinanceService {
	private BaseRequest baseRequest;

	public SupplierFinanceServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<SettleSheetBean> searchSettleSheet(SettleSheetFilterParam settleSheetFilterParam) throws Exception {
		String url = SupplierURL.get_settle_sheet_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, settleSheetFilterParam);

		if (retObj.getInteger("code") == 0) {
			return JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettleSheetBean.class);
		} else {
			return null;
		}
	}

	@Override
	public String addSettleSheet(String settle_supplier_id, List<String> sheet_nos) throws Exception {
		String url = SupplierURL.add_settle_sheet_to_payment_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("op", "add");
		paramMap.put("settle_supplier_id", settle_supplier_id);

		paramMap.put("sheet_nos", JsonUtil.objectToStr(sheet_nos));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("sheet_no") : null;
	}

	@Override
	public boolean addExistedSettleSheet(String id, List<String> sheet_nos) throws Exception {
		String url = SupplierURL.add_settle_sheet_to_payment_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("op", "append");
		paramMap.put("id", id);

		paramMap.put("sheet_nos", JsonUtil.objectToStr(sheet_nos));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SettleSheetDetailBean> searchSettleSheetDetail(
			SettleSheetDetailFilterParam settleSheetDetailFilterParam) throws Exception {
		String url = SupplierURL.search_settle_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, settleSheetDetailFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettleSheetDetailBean.class)
				: null;
	}

	@Override
	public SettleSheetDetailBean getSettleSheetDetail(String id) throws Exception {
		String url = SupplierURL.get_settle_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SettleSheetDetailBean.class)
				: null;
	}

	@Override
	public boolean submitSettleSheetDetail(SettleSheetDetailSubmitParam submitParam) throws Exception {
		String url = SupplierURL.submit_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", submitParam.getId());
		paramMap.put("settle_supplier_id", submitParam.getSettle_supplier_id());
		paramMap.put("station_id", submitParam.getStation_id());
		paramMap.put("date_time", submitParam.getDate_time());

		StringJoiner sheet_nos = new StringJoiner(",");
		for (String sheet_no : submitParam.getSheet_nos()) {
			sheet_nos.add(sheet_no);
		}
		paramMap.put("sheet_nos", sheet_nos.toString());

		paramMap.put("status", String.valueOf(submitParam.getStatus()));
		paramMap.put("total_price", String.valueOf(submitParam.getTotal_price()));
		paramMap.put("delta_money", String.valueOf(submitParam.getDelta_money()));
		paramMap.put("settle_supplier_name", submitParam.getSettle_supplier_name());
		paramMap.put("remark", submitParam.getRemark());
		paramMap.put("discount", JsonUtil.objectToStr(submitParam.getDiscount()));
		paramMap.put("op", "submit");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean markPayment(String id, String running_number, BigDecimal real_pay) throws Exception {
		String url = SupplierURL.pay_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("running_number", running_number);
		paramMap.put("real_pay", String.valueOf(real_pay));
		paramMap.put("op", "pay");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSettleSheetDetail(String id) throws Exception {
		String url = SupplierURL.delete_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("op", "del");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean reviewSettleSheetDetail(String id) throws Exception {
		String url = SupplierURL.review_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("op", "reject");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public SettleSheetDetailBean printSettleSheetDetail(String id) throws Exception {
		String url = SupplierURL.print_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SettleSheetDetailBean.class)
				: null;
	}

	@Override
	public boolean exportSettleSheetDetail(String id) throws Exception {
		String url = SupplierURL.export_settle_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean exportSettleSheet(String start, String end) throws Exception {
		String url = SupplierURL.export_pre_payment_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "2");
		paramMap.put("start", start);
		paramMap.put("end", end);
		paramMap.put("receipt_type", "5");
		paramMap.put("settle_supplier_id", "");
		paramMap.put("export", "1");

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");
		return file_name != null;
	}

	@Override
	public boolean exportPaymentList(String start, String end) throws Exception {
		String url = SupplierURL.export_payment_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("type", "2");
		paramMap.put("start", start);
		paramMap.put("end", end);
		paramMap.put("receipt_type", "5");
		paramMap.put("settle_supplier_id", "");
		paramMap.put("export", "1");

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");
		return file_name != null;
	}

	/**
	 * 应付金额统计
	 */
	@Override
	public SettlementCollectBean getSettlementCollect(String begin, String end, String settle_supplier_id)
			throws Exception {
		String url = SupplierURL.settlement_report_collect_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("begin", begin);
		paramMap.put("end", end);
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SettlementCollectBean.class)
				: null;
	}

	@Override
	public List<SettlementBean> searchSettlement(SettlementFilterParam filterParam) throws Exception {
		String url = SupplierURL.settlement_report_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettlementBean.class)
				: null;
	}

	@Override
	public boolean exportSettlement(SettlementFilterParam filterParam) throws Exception {
		String url = SupplierURL.settlement_report_export_url;

		String file_path = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");

		return file_path != null;
	}

	@Override
	public List<SettlementDetailBean> searchSettlementDetail(SettlementFilterParam settlementFilterParam)
			throws Exception {
		String url = SupplierURL.settlement_report_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, settlementFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SettlementDetailBean.class)
				: null;
	}

	@Override
	public SettlementDetailPageBean searchSettlementDetailPage(SettlementFilterParam settlementFilterParam)
			throws Exception {
		String url = SupplierURL.settlement_report_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, settlementFilterParam);
		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.toString(), SettlementDetailPageBean.class)
				: null;
	}

	@Override
	public BigDecimal exportSettlementDetail(SettlementFilterParam settlementFilterParam) throws Exception {
		String url = SupplierURL.export_settlement_report_detail_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, settlementFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}
}
