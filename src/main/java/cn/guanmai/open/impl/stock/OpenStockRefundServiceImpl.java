package cn.guanmai.open.impl.stock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetBean;
import cn.guanmai.open.bean.stock.OpenStockRefundSheetDetailBean;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockRefundSheetFiterParam;
import cn.guanmai.open.interfaces.stock.OpenStockRefundService;
import cn.guanmai.open.url.OpenStockURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年10月23日
 * @time 下午3:35:38
 * @des TODO
 */

public class OpenStockRefundServiceImpl implements OpenStockRefundService {
	private OpenRequest openRequest;

	public OpenStockRefundServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenStockRefundSheetBean> queryStockRefundSheet(OpenStockRefundSheetFiterParam filterParam)
			throws Exception {
		String url = OpenStockURL.query_stock_refund_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenStockRefundSheetBean.class)
				: null;
	}

	@Override
	public OpenStockRefundSheetDetailBean getStockRefundSheetDetail(String sheet_id) throws Exception {
		String url = OpenStockURL.get_stock_refund_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_refund_sheet_id", sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(),
				OpenStockRefundSheetDetailBean.class) : null;
	}

	@Override
	public String createStockRefundSheet(OpenStockRefundSheetCommonParam stockRefundSheetCreateParam) throws Exception {
		String url = OpenStockURL.create_stock_refund_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, stockRefundSheetCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("supplier_refund_sheet_id")
				: null;
	}

	@Override
	public boolean updateStockRefundSheet(OpenStockRefundSheetCommonParam openStockRefundSheetUpdateParam)
			throws Exception {
		String url = OpenStockURL.update_stock_refund_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openStockRefundSheetUpdateParam);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean submitStockRefundSheet(String refund_sheet_id) throws Exception {
		String url = OpenStockURL.submit_stock_refund_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_refund_sheet_id", refund_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean revertStockRefundSheet(String refund_sheet_id) throws Exception {
		String url = OpenStockURL.revert_stock_refund_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_refund_sheet_id", refund_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean rejectStockRefundSheet(String refund_sheet_id) throws Exception {
		String url = OpenStockURL.reject_stock_refund_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("supplier_refund_sheet_id", refund_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
