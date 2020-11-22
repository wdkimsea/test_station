package cn.guanmai.open.impl.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockInSheetBean;
import cn.guanmai.open.bean.stock.OpenStockInSheetDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockInCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockInSheetFilterParam;
import cn.guanmai.open.interfaces.stock.OpenStockInService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年10月23日
 * @time 上午11:31:59
 * @des TODO
 */

public class OpenStockInServiceImpl implements OpenStockInService {
	private OpenRequest openRequest;

	public OpenStockInServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenStockInSheetBean> queryStockInSheet(OpenStockInSheetFilterParam stockInSheetFilterParam)
			throws Exception {
		String url = OpenStockURL.query_stock_in_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, stockInSheetFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenStockInSheetBean.class)
				: null;
	}

	@Override
	public OpenStockInSheetDetailBean getStockInSheetDetail(String sheet_id) throws Exception {
		String url = OpenStockURL.get_stock_in_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("in_stock_sheet_id", sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenStockInSheetDetailBean.class)
				: null;
	}

	@Override
	public String createStockInSheet(OpenStockInCommonParam stockInCreateParam) throws Exception {
		String url = OpenStockURL.create_stock_in_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockInCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("in_stock_sheet_id") : null;
	}

	@Override
	public boolean updateStockInSheet(OpenStockInCommonParam stockInUpdateParam) throws Exception {
		String url = OpenStockURL.update_stock_in_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockInUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean addStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception {
		String url = OpenStockURL.add_stock_in_sheet_detail_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockInCommonParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception {
		String url = OpenStockURL.update_stock_in_sheet_detail_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockInCommonParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteStockInDetail(OpenStockInCommonParam stockInCommonParam) throws Exception {
		String url = OpenStockURL.delete_stock_in_sheet_detail_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockInCommonParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean submitStockInSheet(String in_stock_sheet_id) throws Exception {
		String url = OpenStockURL.submit_stock_in_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("in_stock_sheet_id", in_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean revertStockInsheet(String in_stock_sheet_id) throws Exception {
		String url = OpenStockURL.revert_stock_in_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("in_stock_sheet_id", in_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean rejectStockInSheet(String in_stock_sheet_id) throws Exception {
		String url = OpenStockURL.reject_stock_in_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("in_stock_sheet_id", in_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
