package cn.guanmai.station.impl.invoicing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.StockSummaryBean;
import cn.guanmai.station.bean.invoicing.StockSummaryCategoryDetailBean;
import cn.guanmai.station.bean.invoicing.InStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.OutStockSummarySpuDetailBean;
import cn.guanmai.station.bean.invoicing.param.StockSummaryFilterParam;
import cn.guanmai.station.interfaces.invoicing.StockSummaryService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年7月29日 下午4:21:58
 * @des
 * @version 1.0
 */
public class StockSummaryServiceImpl implements StockSummaryService {
	private BaseRequest baseRequest;

	public StockSummaryServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<StockSummaryCategoryDetailBean> inStockSummaryDetailByCategory(
			StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String urlStr = InvoicingURL.in_stock_summary_by_category_list_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, stockSummaryFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockSummaryCategoryDetailBean.class)
				: null;
	}

	@Override
	public List<InStockSummarySpuDetailBean> inStockSummaryDetailBySpu(StockSummaryFilterParam stockSummaryFilterParam)
			throws Exception {
		String urlStr = InvoicingURL.in_stock_summary_by_spu_list_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, stockSummaryFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), InStockSummarySpuDetailBean.class)
				: null;
	}

	@Override
	public List<StockSummaryCategoryDetailBean> outStockSummaryDetailByCategory(
			StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String urlStr = InvoicingURL.out_stock_summary_by_category_lsit_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, stockSummaryFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockSummaryCategoryDetailBean.class)
				: null;
	}

	@Override
	public List<OutStockSummarySpuDetailBean> outStockSummaryDetailBySpu(
			StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String urlStr = InvoicingURL.out_stock_summary_by_spu_list_url;
		JSONObject retObj = null;
		boolean more = true;
		List<OutStockSummarySpuDetailBean> stockSummarySpuList = new ArrayList<OutStockSummarySpuDetailBean>();
		while (more) {
			retObj = baseRequest.baseRequest(urlStr, RequestType.GET, stockSummaryFilterParam);
			if (retObj.getInteger("code") == 0) {
				stockSummarySpuList.addAll(JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						OutStockSummarySpuDetailBean.class));
				if (retObj.containsKey("pagination")) {
					JSONObject pagination = retObj.getJSONObject("pagination");
					more = pagination.getBooleanValue("more");
					stockSummaryFilterParam.setPage_obj(pagination.getString("page_obj"));
				} else {
					more = false;
				}
			} else {
				more = false;
				throw new Exception("按SPU维度查看出库汇总报错");
			}
		}
		return stockSummarySpuList;
	}

	@Override
	public StockSummaryBean inStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.in_stock_summary_by_spu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockSummaryBean.class)
				: null;
	}

	@Override
	public StockSummaryBean inStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.in_stock_summary_by_category_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockSummaryBean.class)
				: null;
	}

	@Override
	public StockSummaryBean outStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam)
			throws Exception {
		String url = InvoicingURL.out_stock_summary_by_category_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockSummaryBean.class)
				: null;
	}

	@Override
	public StockSummaryBean outStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.out_stock_summary_by_spu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), StockSummaryBean.class)
				: null;
	}

	@Override
	public String exportInStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.export_in_stock_summary_by_spu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

	@Override
	public String exportInStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.export_in_stock_summary_by_category_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

	@Override
	public String exportOutStockSummaryBySpu(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.export_out_stock_summary_by_spu_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

	@Override
	public String exportOutStockSummaryByCategory(StockSummaryFilterParam stockSummaryFilterParam) throws Exception {
		String url = InvoicingURL.export_out_stock_summary_by_category_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockSummaryFilterParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("task_url").split("=")[1] : null;
	}

}
