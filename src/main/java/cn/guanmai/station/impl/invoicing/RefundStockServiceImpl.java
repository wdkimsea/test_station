package cn.guanmai.station.impl.invoicing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.PurchaseSkuSettleSupplier;
import cn.guanmai.station.bean.invoicing.RefundStockResultBean;
import cn.guanmai.station.bean.invoicing.param.RefundStockFilterParam;
import cn.guanmai.station.bean.invoicing.param.RefundStockParam;
import cn.guanmai.station.interfaces.invoicing.RefundStockService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Jan 7, 2019 11:19:19 AM 
* @des 商户退货相关接口实现类
* @version 1.0 
*/
public class RefundStockServiceImpl implements RefundStockService {
	private BaseRequest baseRequest;

	public RefundStockServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<RefundStockResultBean> searchRefundStock(RefundStockFilterParam refundStockFilterParam)
			throws Exception {
		String urlStr = InvoicingURL.search_refund_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, refundStockFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("refund_list").toString(),
						RefundStockResultBean.class)
				: null;
	}

	@Override
	public boolean exportRefundStockData(RefundStockFilterParam refundStockFilterParam) throws Exception {
		String urlStr = InvoicingURL.export_refund_url;

		String file_path = baseRequest.baseExport(urlStr, RequestType.GET, refundStockFilterParam, "temp.xlsx");

		return file_path != null;
	}

	@Override
	public List<PurchaseSkuSettleSupplier> getPurchaseSkuSettleSupplierList(String purchase_sku_id) throws Exception {
		String urlStr = InvoicingURL.get_purchase_sku_settle_supplier_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("purchase_sku_id", purchase_sku_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONArray("data").getJSONObject(0).getJSONArray("settle_suppliers").toString(),
				PurchaseSkuSettleSupplier.class) : null;
	}

	@Override
	public boolean editRefundStock(List<RefundStockParam> refundStockList) throws Exception {
		String urlStr = InvoicingURL.edit_refund_url;

		String paramStr = JsonUtil.objectToStr(refundStockList);

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("refunds", paramStr);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public JSONObject refundBaseInfo() throws Exception {
		String url = InvoicingURL.refund_base_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);
		return retObj;
	}

}
