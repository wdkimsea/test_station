package cn.guanmai.station.impl.invoicing;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.*;
import cn.guanmai.station.bean.invoicing.param.TransferLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferStockBatchFilterParam;
import cn.guanmai.station.interfaces.invoicing.StockTransferService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjinhai on 2019/8/7.
 */
public class StockTransferServiceImpl implements StockTransferService {
	private BaseRequest baseRequest;

	public StockTransferServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	/**
	 * 获取货位信息接口
	 */
	@Override
	public List<StockTransferShelfBean> getStockInTransferShelf() throws Exception {

		String urlStr = InvoicingURL.get_shelf_tree;
		Map<String, String> paramMap = new HashMap<String, String>();

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), StockTransferShelfBean.class)
				: null;
	}

	@Override
	public List<TransferStockBatchBean> searchStockBatchNumber(TransferStockBatchFilterParam filterParam)
			throws Exception {
		String url = InvoicingURL.search_stock_batch_number_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TransferStockBatchBean.class)
				: null;
	}

	/**
	 * 获取spu信息
	 */
	@Override
	public List<TransferSpuBean> searchTransferSpu(String spuName) throws Exception {
		String urlStr = InvoicingURL.simple_search_spu_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("q", spuName);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TransferSpuBean.class)
				: null;
	}

	/**
	 * 创建仓内移库单
	 */
	@Override
	public String createStockTransfer(TransferSheetCreateParam stockTransferSheetBean) throws Exception {
		String urlStr = InvoicingURL.create_stock_inner_transfer;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, stockTransferSheetBean);

		return retObj.getInteger("code") == 0 && !retObj.getJSONObject("data").containsKey("batch_errors")
				? retObj.getJSONObject("data").get("sheet_no").toString()
				: null;

	}

	/**
	 * 根据sheet_no(移库单号)查看移库单详情
	 */
	@Override
	public TransferSheetDetailBean getTransferSheetDetail(String sheet_no) throws Exception {
		String url = InvoicingURL.get_stock_inner_transfer_detail;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("sheet_no", sheet_no);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), TransferSheetDetailBean.class)
				: null;
	}

	@Override
	public List<TransferSheetBean> searchTransferSheet(TransferSheetFilterParam transferSheetFilterParam)
			throws Exception {
		String url = InvoicingURL.search_stock_inner_transfer_sheet_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, transferSheetFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TransferSheetBean.class)
				: null;
	}

	@Override
	public boolean updateTransferShee(TransferSheetCreateParam transferSheetUpdateParam) throws Exception {
		String url = InvoicingURL.update_stock_inner_transfer_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, transferSheetUpdateParam);

		return retObj.getInteger("code") == 0 && !retObj.getJSONObject("data").containsKey("batch_errors");
	}

	@Override
	public BigDecimal exportTransferSheets(TransferSheetFilterParam transferSheetFilterParam) throws Exception {
		String url = InvoicingURL.export_stock_inner_transfer_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, transferSheetFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

	@Override
	public List<TransferLogBean> searchTransferLog(TransferLogFilterParam transferLogFilterParam) throws Exception {
		String url = InvoicingURL.get_stock_inner_transfer_log_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, transferLogFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), TransferLogBean.class)
				: null;
	}

	@Override
	public BigDecimal exportTransferLogs(TransferLogFilterParam transferLogFilterParam) throws Exception {
		String url = InvoicingURL.export_stock_inner_transfer_log_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, transferLogFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}
}
