package cn.guanmai.station.impl.invoicing;

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
import cn.guanmai.station.bean.invoicing.BatchOutStockBean;
import cn.guanmai.station.bean.invoicing.OutStockDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSheetBean;
import cn.guanmai.station.bean.invoicing.OutStockRemindBean;
import cn.guanmai.station.bean.invoicing.StockSaleSkuBean;
import cn.guanmai.station.bean.invoicing.param.NegativeStockRemindParam;
import cn.guanmai.station.bean.invoicing.param.OutStockModifyParam;
import cn.guanmai.station.bean.invoicing.param.OutStockPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.OutStockSheetFilterParam;
import cn.guanmai.station.interfaces.invoicing.OutStockService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 26, 2019 10:07:12 AM 
* @des 成品入库单相关业务实现类
* @version 1.0 
*/
public class OutStockServiceImpl implements OutStockService {
	private BaseRequest baseRequest;

	public OutStockServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<OutStockSheetBean> searchOutStockSheet(OutStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_out_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("out_stock_list").toString(), OutStockSheetBean.class) : null;
	}

	@Override
	public OutStockDetailBean getOutStockDetailInfo(String sheet_id) throws Exception {
		String url = InvoicingURL.out_stock_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OutStockDetailBean.class)
				: null;
	}

	@Override
	public boolean asyncOrderToOutStockSheet(String order_id) throws Exception {
		String url = InvoicingURL.search_out_stock_sheet_url;

		String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("type", "2");
		paramMap.put("status", "0");
		paramMap.put("search_text", order_id);
		paramMap.put("start", today);
		paramMap.put("end", today);
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");

		boolean result = false;
		int i = 10;
		while (i-- > 0) {
			JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
			if (retObj.getInteger("code") == 0) {
				List<OutStockSheetBean> stockOutSheetList = JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("out_stock_list").toString(),
						OutStockSheetBean.class);
				OutStockSheetBean stockOutSheet = stockOutSheetList.stream().filter(s -> s.getId().equals(order_id))
						.findAny().orElse(null);
				if (stockOutSheet != null) {
					result = true;
					break;
				} else {
					Thread.sleep(2000);
				}
			} else {
				break;
			}
		}
		return result;
	}

	@Override
	public boolean modifyOutStockSheet(OutStockModifyParam outStockModifyParam) throws Exception {
		String url = InvoicingURL.modify_out_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, outStockModifyParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelOutStockSheet(String sheet_id) throws Exception {
		String url = InvoicingURL.cancel_out_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OutStockRemindBean> singleOutStockRemind(NegativeStockRemindParam negativeStockRemindParam)
			throws Exception {
		String url = InvoicingURL.negative_stock_remind_single_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, negativeStockRemindParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("spu_remain").toString(), OutStockRemindBean.class) : null;
	}

	@Override
	public boolean exportOutStockSheet(OutStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_out_stock_sheet_url;

		boolean result = false;
		if (filterParam.getExport() == null || filterParam.getExport() != 1) {
			Reporter.log("导出成品出库单,缺少export参数");
			return result;
		}

		String file_name = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public List<OutStockRemindBean> batchOutStockRemind(OutStockSheetFilterParam outStockSheetFilterParam)
			throws Exception {
		String url = InvoicingURL.negative_stock_remind_batch_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, outStockSheetFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OutStockRemindBean.class)
				: null;
	}

	@Override
	public List<OutStockRemindBean> batchOutStockRemind(List<String> out_stock_sheet_ids) throws Exception {
		String url = InvoicingURL.negative_stock_remind_batch_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_list", JSONArray.toJSONString(out_stock_sheet_ids).toString());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OutStockRemindBean.class)
				: null;
	}

	@Override
	public List<OutStockRemindBean> newBatchOutStockRemind(OutStockSheetFilterParam outStockSheetFilterParam)
			throws Exception {
		String url = InvoicingURL.negative_stock_remind_batch_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, outStockSheetFilterParam);

		List<OutStockRemindBean> outStockReminds = null;
		if (retObj.getInteger("code") == 0) {
			if (retObj.get("data") instanceof JSONObject) {
				outStockReminds = JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("negative_out_list").toString(),
						OutStockRemindBean.class);
			} else {
				outStockReminds = new ArrayList<OutStockRemindBean>();
			}
		}
		return outStockReminds;
	}

	@Override
	public List<OutStockRemindBean> newBatchOutStockRemind(List<String> out_stock_sheet_ids) throws Exception {
		String url = InvoicingURL.negative_stock_remind_batch_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_list", JSONArray.toJSONString(out_stock_sheet_ids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<OutStockRemindBean> outStockReminds = null;
		if (retObj.getInteger("code") == 0) {
			if (retObj.get("data") instanceof JSONObject) {
				outStockReminds = JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("negative_out_list").toString(),
						OutStockRemindBean.class);
			} else {
				outStockReminds = new ArrayList<OutStockRemindBean>();
			}
		}
		return outStockReminds;
	}

	@Override
	public String batchOutStock(OutStockSheetFilterParam outStockSheetFilterParam) throws Exception {
		String url = InvoicingURL.batch_out_stock_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, outStockSheetFilterParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url") : null;
	}

	@Override
	public boolean createOutStockSheet(String out_stock_target, String id) throws Exception {
		String url = InvoicingURL.create_out_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_target", out_stock_target);
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createOutStockSheet(OutStockSheetCreateParam stockOutSheetCreateParam) throws Exception {
		String url = InvoicingURL.create_out_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, stockOutSheetCreateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateOutStockPrice(List<OutStockPriceUpdateParam> outStockPriceUpdateParams) throws Exception {
		String url = InvoicingURL.update_out_stock_price_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("goods_infos", JsonUtil.objectToStr(outStockPriceUpdateParams));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<StockSaleSkuBean> searchStockSaleSku(String name, String out_stock_customer_id) throws Exception {
		String url = InvoicingURL.search_stock_sale_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);
		if (out_stock_customer_id != null && !out_stock_customer_id.trim().equals("")) {
			paramMap.put("out_stock_customer_id", out_stock_customer_id);
		}

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<StockSaleSkuBean> stockSaleSkuList = null;
		if (retObj.getInteger("code") == 0) {
			stockSaleSkuList = new ArrayList<StockSaleSkuBean>();
			JSONObject dataObj = retObj.getJSONObject("data");
			for (Object obj : dataObj.keySet()) {
				String key = String.valueOf(obj);
				JSONObject spuObj = dataObj.getJSONObject(key);
				stockSaleSkuList.addAll(
						JsonUtil.strToClassList(spuObj.getJSONArray("skus").toString(), StockSaleSkuBean.class));
			}
		}
		return stockSaleSkuList;
	}

	@Override
	public List<OutStockDetailBean> printStockOutSheet(List<String> ids) throws Exception {
		String url = InvoicingURL.search_out_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ids", JSONArray.toJSONString(ids));
		paramMap.put("is_for_print", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("out_stock_list").toString(),
						OutStockDetailBean.class)
				: null;
	}

	/*******************************************************************************************/
	/********************************
	 * 以下是先进先出的接口业务实现
	 ********************************/
	/*******************************************************************************************/

	@Override
	public List<BatchOutStockBean> searchOutStockBatch(String sku_id, String q) throws Exception {
		String url = InvoicingURL.get_batch_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sku_id", sku_id);
		paramMap.put("q", q);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<BatchOutStockBean> stockOutBatchList = null;
		if (retObj.getInteger("code") == 0) {
			stockOutBatchList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					BatchOutStockBean.class);
		}
		return stockOutBatchList;
	}

	/**
	 * 先进先出手工出库
	 * 
	 */
	@Override
	public boolean modifyStockOutSheetInFIFO(OutStockDetailBean stockOutDetailBean) throws Exception {
		String url = InvoicingURL.modify_out_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", stockOutDetailBean.getId());
		paramMap.put("details", JsonUtil.objectToStr(stockOutDetailBean.getDetails()));
		paramMap.put("update_time", stockOutDetailBean.getUpdate_time());
		paramMap.put("create_time", stockOutDetailBean.getCreate_time());
		paramMap.put("out_stock_target", stockOutDetailBean.getOut_stock_target());
		paramMap.put("money", "-");
		paramMap.put("is_bind_order", String.valueOf(stockOutDetailBean.isIs_bind_order()));
		paramMap.put("status", String.valueOf(stockOutDetailBean.getStatus()));
		paramMap.put("creator", stockOutDetailBean.getCreator());
		paramMap.put("out_stock_time", stockOutDetailBean.getOut_stock_time());
		paramMap.put("is_submit", "2");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
