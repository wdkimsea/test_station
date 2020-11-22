package cn.guanmai.station.impl.invoicing;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.InStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetImportParam;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;
import org.testng.Reporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* 
* @author liming 
* @date Feb 18, 2019 11:29:29 AM 
* @des 成品入库相关业务实现类
* @version 1.0 
*/
public class InStockServiceImpl implements InStockService {
	private BaseRequest baseRequest;

	public InStockServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createInStockSheet(String settle_supplier_id, String supplier_name) throws Exception {
		String urlStr = InvoicingURL.create_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("settle_supplier_id", settle_supplier_id);
		paramMap.put("supplier_name", supplier_name);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public String createInStockSheet(InStockCreateParam inStockCreateParam) throws Exception {
		String url = InvoicingURL.new_create_in_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, inStockCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public InStockDetailInfoBean getInStockSheetDetail(String sheet_id) throws Exception {
		String urlStr = InvoicingURL.get_in_stock_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), InStockDetailInfoBean.class)
				: null;
	}

	@Override
	public BigDecimal getSupplieraveragePrice(String spec_id, String settle_supplier_id) throws Exception {
		String urlStr = InvoicingURL.supplier_avg_price_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spec_id", spec_id);
		paramMap.put("settle_supplier_id", settle_supplier_id);
		paramMap.put("query_type", "3");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);
		BigDecimal supplier_avg_price = null;
		if (retObj.getInteger("code") == 0) {
			String price_str = retObj.getJSONObject("data").getString("supplier_avg_price");
			if (price_str.equals("")) {
				supplier_avg_price = BigDecimal.ZERO;
			} else {
				supplier_avg_price = new BigDecimal(price_str);
			}
		}
		return supplier_avg_price;
	}

	@Override
	public boolean modifyInStockSheet(InStockDetailInfoBean stockIn) throws Exception {
		String url = InvoicingURL.modify_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("settle_supplier_id", stockIn.getSettle_supplier_id());
		paramMap.put("supplier_customer_id", stockIn.getSupplier_customer_id());
		paramMap.put("supplier_name", stockIn.getSupplier_name());
		paramMap.put("station_id", stockIn.getStation_id());
		paramMap.put("id", stockIn.getId());
		paramMap.put("submit_time", stockIn.getSubmit_time());
		paramMap.put("date_time", stockIn.getDate_time());
		paramMap.put("creator", stockIn.getCreator());
		paramMap.put("remark", stockIn.getRemark());
		paramMap.put("status", String.valueOf(stockIn.getStatus()));
		paramMap.put("type", String.valueOf(stockIn.getType()));
		paramMap.put("is_submit", String.valueOf(stockIn.getIs_submit()));
		paramMap.put("sku_money", String.valueOf(stockIn.getSku_money()));
		paramMap.put("delta_money", String.valueOf(stockIn.getDelta_money()));

		paramMap.put("share", JsonUtil.objectToStr(stockIn.getShares()));
		paramMap.put("discount", JsonUtil.objectToStr(stockIn.getDiscounts()));
		paramMap.put("details", JsonUtil.objectToStr(stockIn.getDetails()));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean reviewInStockSheet(String sheet_id) throws Exception {
		String url = InvoicingURL.review_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);
		paramMap.put("is_pass", "2");
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelInStockSheet(String sheet_id) throws Exception {
		String url = InvoicingURL.cancel_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createInStockSheetPrintLog(List<String> sheet_ids) throws Exception {
		String url = InvoicingURL.print_in_stock_sheet_log_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_type", "2");
		paramMap.put("ids", JSONArray.toJSONString(sheet_ids));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<InStockDetailInfoBean> printInStockSheetDetail(List<String> sheet_ids) throws Exception {
		String url = InvoicingURL.print_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("print_type", "2");
		paramMap.put("ids", JSONArray.parseArray(JSON.toJSONString(sheet_ids)).toString());
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), InStockDetailInfoBean.class)
				: null;
	}

	@Override
	public boolean exportInStockSheetDetail(String sheet_id) throws Exception {
		String url = InvoicingURL.get_in_stock_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);
		paramMap.put("export", "1");
		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");
		return file_name != null;
	}

	@Override
	public List<InStockSheetBean> searchInStockSheet(InStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_in_stock_sheet_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		List<InStockSheetBean> stockInSheetList = null;
		if (retObj.getInteger("code") == 0) {
			if (retObj.get("data") instanceof JSONObject) {
				stockInSheetList = JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("in_stock_list").toString(), InStockSheetBean.class);
			} else {
				stockInSheetList = new ArrayList<InStockSheetBean>();
			}
		}
		return stockInSheetList;
	}

	@Override
	public boolean exportInStockSheet(InStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_in_stock_sheet_url;
		if (filterParam.getExport() == null || filterParam.getExport() != 1) {
			Reporter.log("参数不正确,export参数应该为1");
			return false;
		}
		String file_name = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");
		return file_name != null;
	}

	@Override
	public boolean downloadInStockSheetTemplate() throws Exception {
		String url = InvoicingURL.download_in_stock_sheet_template_url;

		Map<String, String> paramMap = new HashMap<String, String>();

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean importInStockSheet(List<InStockSheetImportParam> param) throws Exception {
		String url = InvoicingURL.import_in_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("in_stock_list", JsonUtil.objectToStr(param));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
