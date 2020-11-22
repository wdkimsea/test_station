package cn.guanmai.station.impl.invoicing;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.ShelfBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuStockBean;
import cn.guanmai.station.bean.invoicing.ShelfStockBatchBean;
import cn.guanmai.station.bean.invoicing.param.ShelfSpuFilterParam;
import cn.guanmai.station.bean.invoicing.param.ShelfStockBatchFilterParam;
import cn.guanmai.station.interfaces.invoicing.ShelfService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by yangjinhai on 2019/7/30.
 */
public class ShelfServiceImpl implements ShelfService {
	private BaseRequest baseRequest;

	public ShelfServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	/**
	 * @param name 添加货位层架
	 */
	@Override
	public String addShelf(String name) throws Exception {
		String urlStr = InvoicingURL.add_shelf_location_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").get("id").toString() : null;
	}

	@Override
	public String addShelf(String parent_id, String name) throws Exception {
		String urlStr = InvoicingURL.add_shelf_location_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", name);
		paramMap.put("parent_id", parent_id);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").get("id").toString() : null;
	}

	/**
	 * 删除货位层级
	 */
	@Override
	public boolean deleteShelf(String id) throws Exception {
		String url = InvoicingURL.del_shelf_location_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<ShelfBean> getShelf() throws Exception {
		String urlStr = InvoicingURL.get_shelf_url;
		Map<String, String> paramMap = new HashMap<String, String>();

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		if (retObj.getInteger("code") == 0) {
			return JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ShelfBean.class);
		}

		return null;
	}

	/**
	 * 修改货位名称
	 * 
	 * @param id
	 */
	@Override
	public boolean modifyShelf(String newName, String id) throws Exception {
		String url = InvoicingURL.edit_shelf_location_url;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("new_name", newName);
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public JSONObject getShelfSpuStockSummaryByShelf(String shelf_id) throws Exception {
		String url = InvoicingURL.shelf_spu_summary_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("shelf_id", shelf_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data") : null;
	}

	@Override
	public JSONObject getShelfSpuStockSummaryBySpu(String spu_id) throws Exception {
		String url = InvoicingURL.shelf_spu_summary_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data") : null;
	}

	@Override
	public List<ShelfSpuBean> queryShelfSpu(ShelfSpuFilterParam shelfSpuFilterParam) throws Exception {
		String url = InvoicingURL.shelf_spu_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, shelfSpuFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ShelfSpuBean.class)
				: null;
	}

	@Override
	public List<ShelfSpuBean> queryShelfNegativeSpu(ShelfSpuFilterParam shelfSpuFilterParam) throws Exception {
		String url = InvoicingURL.shelf_spu_negative_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, shelfSpuFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ShelfSpuBean.class)
				: null;
	}

	@Override
	public List<ShelfStockBatchBean> queryShelfStockBatch(ShelfStockBatchFilterParam shelfStockBatchFilterParam)
			throws Exception {
		String url = InvoicingURL.shelf_stock_batch_list_url;
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, shelfStockBatchFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), ShelfStockBatchBean.class)
				: null;
	}

	@Override
	public List<ShelfSpuStockBean> getShelfSpuStockInfo(String spu_id) throws Exception {
		String url = InvoicingURL.shelf_spu_stock_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("spu_id", spu_id);

		int offset = 0;
		int limit = 40;
		paramMap.put("limit", String.valueOf(limit));

		JSONObject retObj = null;
		List<ShelfSpuStockBean> shelfSpuStockList = new ArrayList<ShelfSpuStockBean>();
		List<ShelfSpuStockBean> tempShelfSpuStockList = null;
		while (true) {
			paramMap.put("offset", String.valueOf(offset));
			retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
			if (retObj.getInteger("code") == 0) {
				tempShelfSpuStockList = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(),
						ShelfSpuStockBean.class);
				shelfSpuStockList.addAll(tempShelfSpuStockList);
				if (tempShelfSpuStockList.size() < limit) {
					break;
				}
				offset += limit;
			} else {
				shelfSpuStockList = null;
				break;
			}
		}
		return shelfSpuStockList;
	}

}
