package cn.guanmai.open.impl.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockOutDetailBean;
import cn.guanmai.open.bean.stock.OpenStockOutSheetBean;
import cn.guanmai.open.bean.stock.param.OpenStockOutCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockOutSheetFilterParam;
import cn.guanmai.open.interfaces.stock.OpenStockOutService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年10月23日
 * @time 下午2:27:01
 * @des TODO
 */

public class OpenStockOutServiceImpl implements OpenStockOutService {
	private OpenRequest openRequest;

	public OpenStockOutServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenStockOutSheetBean> queryStockOutSheet(OpenStockOutSheetFilterParam stockOutSheetFilterParam)
			throws Exception {
		String url = OpenStockURL.query_stock_out_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, stockOutSheetFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenStockOutSheetBean.class)
				: null;
	}

	@Override
	public OpenStockOutDetailBean getStockOutDetail(String out_stock_sheet_id) throws Exception {
		String url = OpenStockURL.get_stock_out_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_sheet_id", out_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenStockOutDetailBean.class)
				: null;
	}

	@Override
	public String createStockOutSheet(OpenStockOutCommonParam stockOutCreateParam) throws Exception {
		String url = OpenStockURL.create_stock_out_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockOutCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("out_stock_sheet_id") : null;
	}

	@Override
	public boolean updateStockOutSheet(OpenStockOutCommonParam stockOutUpdateParam) throws Exception {
		String url = OpenStockURL.update_stock_out_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockOutUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean submitStockOutSheet(String out_stock_sheet_id) throws Exception {
		String url = OpenStockURL.submit_stock_out_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_sheet_id", out_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean revertStockOutSheet(String out_stock_sheet_id) throws Exception {
		String url = OpenStockURL.revert_stock_out_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("out_stock_sheet_id", out_stock_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
