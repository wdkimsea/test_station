package cn.guanmai.station.impl.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.category.CategoriesBean;
import cn.guanmai.station.bean.invoicing.BatchLogBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.StockBatchCheckResultBean;
import cn.guanmai.station.bean.invoicing.StockChangeLogBean;
import cn.guanmai.station.bean.invoicing.param.SpuStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockAvgPriceUpdateParam;
import cn.guanmai.station.bean.invoicing.param.BatchStockCheckParam;
import cn.guanmai.station.bean.invoicing.param.StockChangeLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckTemplateParam;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/* 
* @author liming 
* @date Feb 28, 2019 10:35:41 AM 
* @des 商品盘点业务实现类
* @version 1.0 
*/
public class StockCheckServiceImpl implements StockCheckService {

	private BaseRequest baseRequest;

	public StockCheckServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<CategoriesBean> getCategories() throws Exception {
		String url = InvoicingURL.categories_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CategoriesBean.class)
				: null;
	}

	@Override
	public List<SpuStockBean> searchStockCheck(StockCheckFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_stock_check_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SpuStockBean.class)
				: null;
	}

	@Override
	public SpuStockBean getSpuStock(String spu_id) throws Exception {
		String url = InvoicingURL.search_stock_check_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("text", spu_id);
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		SpuStockBean spuStock = null;
		if (retObj.getInteger("code") == 0) {
			List<SpuStockBean> spuStockList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					SpuStockBean.class);
			spuStock = spuStockList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny().orElse(null);
		} else {
			throw new Exception("库存盘点页面搜索查询报错");
		}
		return spuStock;
	}

	@Override
	public boolean editSpuStock(String spu_id, BigDecimal new_stock, String remark) throws Exception {
		String url = InvoicingURL.edit_spu_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("new_stock", String.valueOf(new_stock));
		paramMap.put("remark", remark);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public BigDecimal downloadStockCheckTemplate() throws Exception {
		String url = InvoicingURL.download_stock_check_template_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		BigDecimal task_id = null;
		if (retObj.getInteger("code") == 0) {
			String task_url = retObj.getJSONObject("data").getString("task_url");
			task_id = new BigDecimal(task_url.split("=")[1]);
		}
		return task_id;
	}

	@Override
	public BigDecimal downloadStockCheckTemplateStep1(StockCheckTemplateParam stockCheckTemplateParam)
			throws Exception {
		String url = InvoicingURL.download_stock_check_template_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, stockCheckTemplateParam);
		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public String downloadStockCheckTemplateStep2(String link) throws Exception {
		String url = link;

		String filePath = baseRequest.baseExport(url, RequestType.GET, new HashMap<String, String>(), "temp.xlsx");
		return filePath;
	}

	@Override
	public List<StockBatchCheckResultBean> uploadStockCheckTemplate(String file_path) throws Exception {
		String url = InvoicingURL.upload_stock_check_template_url;

		JSONObject retObj = baseRequest.baseUploadRequest(url, new HashMap<String, String>(), "file", file_path);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockBatchCheckResultBean.class)
				: null;
	}

	@Override
	public boolean downloadStockCheckFile(StockCheckFilterParam filterParam) throws Exception {
		String url = InvoicingURL.search_stock_check_url;

		String file_name = baseRequest.baseExport(url, RequestType.GET, filterParam, "temp.xlsx");

		return file_name != null;
	}

	@Override
	public boolean checkBatchStock(List<BatchStockCheckParam> stockBatchCheckParams) throws Exception {
		String url = InvoicingURL.check_batch_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("stock_details", JsonUtil.objectToStr(stockBatchCheckParams));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String stockWarning() throws Exception {
		String url = InvoicingURL.stock_warning_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("content") : null;
	}

	@Override
	public boolean setSpuStockLowerThreshold(String spu_id, BigDecimal threshold) throws Exception {
		String url = InvoicingURL.edit_spu_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("threshold", String.valueOf(threshold));
		paramMap.put("threshold_using", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setSpuStockUpperThreshold(String spu_id, BigDecimal threshold) throws Exception {
		String url = InvoicingURL.edit_spu_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("upper_threshold", String.valueOf(threshold));
		paramMap.put("set_upper_threshold", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelSpuStockLowerThreshold(String spu_id) throws Exception {
		String url = InvoicingURL.edit_spu_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("threshold_using", "2");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean cancelSpuStockUpperThreshold(String spu_id) throws Exception {
		String url = InvoicingURL.edit_spu_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);
		paramMap.put("set_upper_threshold", "2");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<StockChangeLogBean> getSpuStockChangeLogList(StockChangeLogFilterParam filterParam) throws Exception {
		String url = InvoicingURL.spu_stock_change_log_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockChangeLogBean.class)
				: null;
	}

	@Override
	public List<StockChangeLogBean> getSpuStockChangeLogs(StockChangeLogFilterParam filterParam) throws Exception {
		String url = InvoicingURL.spu_stock_change_log_url;

		JSONObject retObj = null;

		List<StockChangeLogBean> spuStockChangeLogs = new ArrayList<StockChangeLogBean>();
		boolean more = true;
		while (more) {
			retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
			if (retObj.getInteger("code") == 0) {
				spuStockChangeLogs.addAll(
						JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockChangeLogBean.class));

				JSONObject pagination = retObj.getJSONObject("pagination");
				more = pagination.containsKey("more") ? pagination.getBoolean("more") : false;
				filterParam.setPage_obj(pagination.getJSONArray("page_obj"));
			} else {
				more = false;
				spuStockChangeLogs = null;
			}
		}
		return spuStockChangeLogs;

	}

	@Override
	public BigDecimal spuBatchStockCheck(List<SpuStockCheckParam> param) throws Exception {
		String url = InvoicingURL.batch_stock_check_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("stock_details", JsonUtil.objectToStr(param));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public boolean updateStockAveragePrice(List<StockAvgPriceUpdateParam> updateParam) throws Exception {
		String url = InvoicingURL.stock_avg_price_update_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("goods_infos", JsonUtil.objectToStr(updateParam));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<StockBatchBean> searchStockBatch(String text, int offset, int limit) throws Exception {
		String url = InvoicingURL.search_stock_batch_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("text", text);
		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("limit", String.valueOf(limit));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockBatchBean.class)
				: null;
	}

	@Override
	public StockBatchBean getStockBatch(String batch_number) throws Exception {
		String url = InvoicingURL.search_stock_batch_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("text", batch_number);
		paramMap.put("offset", "0");
		paramMap.put("limit", "10");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		StockBatchBean stockBatch = null;
		if (retObj.getInteger("code") == 0) {
			List<StockBatchBean> stockBatchList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
					StockBatchBean.class);
			stockBatch = stockBatchList.stream().filter(s -> s.getBatch_number().equals(batch_number)).findAny()
					.orElse(null);
		} else {
			throw new Exception("查询批次信息失败");
		}
		return stockBatch;
	}

	@Override
	public boolean editBatchStock(String batch_number, BigDecimal new_remain, String remark) throws Exception {
		String url = InvoicingURL.edit_batch_stock_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("batch_number", batch_number);
		paramMap.put("new_remain", String.valueOf(new_remain));
		paramMap.put("remark", remark);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<BatchLogBean> getBatchLog(String start_time, String end_time, String batch_number) throws Exception {
		String url = InvoicingURL.get_batch_log_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("start_time", start_time);
		paramMap.put("end_time", end_time);
		paramMap.put("batch_number", batch_number);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		List<BatchLogBean> batchLogs = null;
		if (retObj.getInteger("code") == 0) {
			batchLogs = JsonUtil.strToClassList(retObj.getJSONObject("data").getJSONArray("data_list").toString(),
					BatchLogBean.class);
		}
		return batchLogs;
	}

}
