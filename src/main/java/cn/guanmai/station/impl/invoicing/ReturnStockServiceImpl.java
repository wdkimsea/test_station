package cn.guanmai.station.impl.invoicing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.Reporter;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.ReturnStockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.param.ReturnStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetImportParam;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 27, 2019 10:58:25 AM 
* @des 成品退货业务相关实现类
* @version 1.0 
*/
public class ReturnStockServiceImpl implements ReturnStockService {
	private BaseRequest baseRequest;

	public ReturnStockServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createReturnStockSheet(String settle_supplier_id, String supplier_name) throws Exception {
		String url = InvoicingURL.create_return_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("settle_supplier_id", settle_supplier_id);
		paramMap.put("supplier_name", supplier_name);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public String newCreateReturnStockSheet(ReturnStockCreateParam returnStockCreateParam) throws Exception {
		String url = InvoicingURL.new_create_return_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, returnStockCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public ReturnStockDetailBean getRetrunStockDetail(String id) throws Exception {
		String url = InvoicingURL.return_stock_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), ReturnStockDetailBean.class)
				: null;
	}

	@Override
	public boolean modifyReturnStockSheet(ReturnStockDetailBean returnStockDetail) throws Exception {
		String url = InvoicingURL.edit_return_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("settle_supplier_id", returnStockDetail.getSettle_supplier_id());
		paramMap.put("supplier_name", returnStockDetail.getSupplier_name());
		paramMap.put("station_id", returnStockDetail.getStation_id());
		paramMap.put("id", returnStockDetail.getId());
		paramMap.put("submit_time", returnStockDetail.getSubmit_time());
		paramMap.put("date_time", returnStockDetail.getDate_time());
		paramMap.put("creator", returnStockDetail.getCreator());
		paramMap.put("status", String.valueOf(returnStockDetail.getStatus()));
		paramMap.put("type", String.valueOf(returnStockDetail.getType()));
		paramMap.put("return_sheet_remark",
				returnStockDetail.getReturn_sheet_remark() == null ? "" : returnStockDetail.getReturn_sheet_remark());

		paramMap.put("sku_money", String.valueOf(returnStockDetail.getSku_money()));
		paramMap.put("delta_money", String.valueOf(returnStockDetail.getDelta_money()));

		paramMap.put("discount", JsonUtil.objectToStr(returnStockDetail.getDiscounts()));
		paramMap.put("details", JsonUtil.objectToStr(returnStockDetail.getDetails()));

		paramMap.put("is_submit", "2");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelReturnStockSheet(String sheet_id) throws Exception {
		String url = InvoicingURL.cancel_return_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean exportReturnStockDetailInfo(String sheet_id) throws Exception {
		String url = InvoicingURL.return_stock_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", sheet_id);
		paramMap.put("export", "1");

		String file_name = baseRequest.baseExport(url, RequestType.GET, paramMap, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public List<ReturnStockSheetBean> searchReturnStockSheet(ReturnStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_return_stock_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		List<ReturnStockSheetBean> returnStockDetailSheetList = null;
		if (retObj.getInteger("code") == 0) {
			returnStockDetailSheetList = new ArrayList<ReturnStockSheetBean>();
			if (retObj.get("data") instanceof JSONObject) {
				returnStockDetailSheetList = JsonUtil.strToClassList(
						retObj.getJSONObject("data").getJSONArray("return_stock_list").toString(),
						ReturnStockSheetBean.class);
			}
		}
		return returnStockDetailSheetList;
	}

	@Override
	public boolean exportReturnStockSheet(ReturnStockSheetFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_return_stock_sheet_url;

		if (filterParam.getExport() == null) {
			Reporter.log("参数异常: 导出成品退货单缺少export参数");
			return false;
		}

		String file_name = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean downReturnStockTemplate() throws Exception {
		String url = InvoicingURL.down_return_stock_sheet_template_url;

		String file_name = baseRequest.baseExport(url, RequestType.GET, new HashMap<String, String>(), "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean importReturnStockSheet(List<ReturnStockSheetImportParam> paramList) throws Exception {
		String url = InvoicingURL.import_return_stock_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("return_stock_list", JsonUtil.objectToStr(paramList));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<ReturnStockBatchBean> searchReturnStockBatch(String purchase_spec_id, String supplier_id)
			throws Exception {
		String url = InvoicingURL.get_batch_return_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("sku_id", purchase_spec_id);
		paramMap.put("settle_supplier_id", supplier_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<ReturnStockBatchBean> returnStockDetailBatchList = null;
		if (retObj.getInteger("code") == 0) {
			returnStockDetailBatchList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					ReturnStockBatchBean.class);
		}
		return returnStockDetailBatchList;
	}

}
