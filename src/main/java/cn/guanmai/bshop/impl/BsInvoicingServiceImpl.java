package cn.guanmai.bshop.impl;

import cn.guanmai.bshop.bean.invoicing.*;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuInStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuOutStockParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopSpuStockFilterParam;
import cn.guanmai.bshop.bean.invoicing.param.BshopWatiInStockFilterParam;
import cn.guanmai.bshop.service.BsInvoicingService;
import cn.guanmai.bshop.url.BsInvoigingURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.TimeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by abc on 2019/8/16. 进销存相关实现类
 */
public class BsInvoicingServiceImpl implements BsInvoicingService {

	private BaseRequest baseRequest;

	public BsInvoicingServiceImpl(Map<String, String> cookie) {
		baseRequest = new BaseRequestImpl(cookie);
	}

	/**
	 * 统计进销存总货值
	 */
	@Override
	public BshopStockCountBean getStockCount(String address_id, String searchText) throws Exception {
		String urlStr = BsInvoigingURL.stock_spu_count_url;

		Map<String, String> params = new HashMap<>();
		params.put("address_id", address_id.replaceFirst("S(0*)", ""));
		params.put("search", searchText);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BshopStockCountBean.class)
				: null;
	}

	@Override
	public List<BshopSpuStockBean> searchSpuStock(BshopSpuStockFilterParam filterParam) throws Exception {
		String urlStr = BsInvoigingURL.spu_stock_list;
		List<BshopSpuStockBean> spuStockList = new ArrayList<BshopSpuStockBean>();
		boolean more = true;
		while (more) {
			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);
			if (retObj.getInteger("code") == 0) {
				more = retObj.getJSONObject("pagination").getBoolean("more");
				if (more) {
					filterParam.setPage_obj(retObj.getJSONObject("pagination").get("page_obj"));
				}
				spuStockList.addAll(
						JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BshopSpuStockBean.class));
			} else {
				spuStockList = null;
				more = false;
			}
		}
		return spuStockList;
	}

	/**
	 * 获取进销存sku统计数据
	 *
	 * @param address_id   商户ID
	 * @param search       搜索内容
	 * @param query_type   查询种类 1:按下单时间 2:按收货时间
	 * @param order_status 订单状态 1:配送中 2:已签收
	 * @return
	 */
	@Override
	public boolean querySkuWaitInStock(String address_id, String search, String query_type, String order_status)
			throws Exception {
		String urlStr = BsInvoigingURL.stock_sku_count;

		Map<String, String> params = new HashMap<>();
		params.put("address_id", address_id);
		params.put("start_time", TimeUtil.getCurrentTime("yyyy-MM-dd"));
		params.put("end_time", TimeUtil.getCurrentTime("yyyy-MM-dd"));
		params.put("search", search);
		params.put("query_type", query_type);
		params.put("order_status", order_status);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<BshopWaitInStockSkuBean> searchWaitInStockSku(BshopWatiInStockFilterParam filterParam)
			throws Exception {
		String urlStr = BsInvoigingURL.stock_sku_list;
		List<BshopWaitInStockSkuBean> waitInStockSkuList = new ArrayList<BshopWaitInStockSkuBean>();
		boolean more = true;
		while (more) {
			JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, filterParam);
			if (retObj.getInteger("code") == 0) {
				more = retObj.getJSONObject("pagination").getBoolean("more");
				if (more) {
					filterParam.setPage_obj(retObj.getJSONObject("pagination").getJSONObject("page_obj"));
				}
				waitInStockSkuList.addAll(
						JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BshopWaitInStockSkuBean.class));
			} else {
				waitInStockSkuList = null;
				more = false;
			}
		}
		return waitInStockSkuList;
	}

	@Override
	public List<BshopSpuStockBean> getAddressSpuStockList(String address_id) throws Exception {
		String url = BsInvoigingURL.address_spu_stock_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("address_id", address_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BshopSpuStockBean.class)
				: null;
	}

	@Override
	public boolean createSpuInStock(BshopSpuInStockParam spuInStockParam) throws Exception {
		String url = BsInvoigingURL.spu_in_stock_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, spuInStockParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createSpuStockOutput(BshopSpuOutStockParam spuOutStockParam) throws Exception {
		String url = BsInvoigingURL.create_spu_stock_output_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, spuOutStockParam);

		return retObj.getInteger("code") == 0;
	}

}
