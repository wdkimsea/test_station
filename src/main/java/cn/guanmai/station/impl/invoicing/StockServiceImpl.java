package cn.guanmai.station.impl.invoicing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.StockSettleSupplierBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.StockCostReportFilterParam;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 27, 2019 11:38:54 AM 
* @todo TODO
* @version 1.0 
*/
public class StockServiceImpl implements StockService {
	private BaseRequest baseRequest;

	public StockServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<SupplySkuBean> getSupplySkuList(String name, String sheet_id) throws Exception {
		String urlStr = InvoicingURL.get_supply_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("id", sheet_id);
		paramMap.put("source", "supply_sku");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		List<SupplySkuBean> supplySkuList = null;
		if (retObj.getInteger("code") == 0) {
			supplySkuList = new ArrayList<SupplySkuBean>();
			JSONObject dataObj = retObj.getJSONObject("data");
			String spu_id = null;
			for (Object obj : dataObj.keySet()) {
				spu_id = String.valueOf(obj);
				supplySkuList.addAll(JsonUtil.strToClassList(
						dataObj.getJSONObject(spu_id).getJSONArray("skus").toString(), SupplySkuBean.class));
			}
		}
		return supplySkuList;
	}

	@Override
	public Map<String, List<SupplySkuBean>> newSearchSupplySku(String name, String settle_supplier_id)
			throws Exception {
		String urlStr = InvoicingURL.new_supply_sku_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("settle_supplier_id", settle_supplier_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		Map<String, List<SupplySkuBean>> supplySkuListMap = null;
		if (retObj.getInteger("code") == 0) {
			supplySkuListMap = new HashMap<String, List<SupplySkuBean>>();
			JSONObject dataObj = retObj.getJSONObject("data");
			JSONObject target_supplier = dataObj.getJSONObject("target_supplier");
			JSONObject other_supplier = dataObj.getJSONObject("other_supplier");
			String spu_id = null;
			List<SupplySkuBean> targetSupplySkus = new ArrayList<SupplySkuBean>();
			for (Object obj : target_supplier.keySet()) {
				spu_id = String.valueOf(obj);
				targetSupplySkus.addAll(JsonUtil.strToClassList(
						target_supplier.getJSONObject(spu_id).getJSONArray("skus").toString(), SupplySkuBean.class));
			}

			List<SupplySkuBean> otherSupplySkus = new ArrayList<SupplySkuBean>();
			for (Object obj : other_supplier.keySet()) {
				spu_id = String.valueOf(obj);
				targetSupplySkus.addAll(JsonUtil.strToClassList(
						other_supplier.getJSONObject(spu_id).getJSONArray("skus").toString(), SupplySkuBean.class));
			}
			supplySkuListMap.put("target", targetSupplySkus);
			supplySkuListMap.put("other", otherSupplySkus);
		}
		return supplySkuListMap;

	}

	@Override
	public boolean getStockCostReport(StockCostReportFilterParam filterParam) throws Exception {
		String urlStr = InvoicingURL.stock_cost_report_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<StockSettleSupplierBean> getStockSettleSuppliers() throws Exception {
		String url = InvoicingURL.stock_settle_supplier_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		List<StockSettleSupplierBean> stockSettleSuppliers = null;
		if (retObj.getInteger("code") == 0) {
			JSONArray dataArray = retObj.getJSONArray("data");
			if (dataArray.size() > 0) {
				stockSettleSuppliers = JsonUtil.strToClassList(
						dataArray.getJSONObject(0).getJSONArray("settle_suppliers").toString(),
						StockSettleSupplierBean.class);
			}
		}
		return stockSettleSuppliers;
	}

}
