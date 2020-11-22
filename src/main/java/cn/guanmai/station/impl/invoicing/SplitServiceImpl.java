package cn.guanmai.station.impl.invoicing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.invoicing.SplitLossBean;
import cn.guanmai.station.bean.invoicing.SplitLossCountBean;
import cn.guanmai.station.bean.invoicing.SplitPlanBean;
import cn.guanmai.station.bean.invoicing.SplitPlanDetailBean;
import cn.guanmai.station.bean.invoicing.SplitSheetBean;
import cn.guanmai.station.bean.invoicing.SplitSheetDetailBean;
import cn.guanmai.station.bean.invoicing.param.SplitLossFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanFilterParam;
import cn.guanmai.station.bean.invoicing.param.SplitPlanParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetParam;
import cn.guanmai.station.bean.invoicing.param.SplitSheetFiterParam;
import cn.guanmai.station.interfaces.invoicing.SplitService;
import cn.guanmai.station.url.InvoicingURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午3:54:54
 * @description:
 * @version: 1.0
 */

public class SplitServiceImpl implements SplitService {
	private BaseRequest baseRequest;

	public SplitServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<SplitPlanBean> searchSplitPlan(SplitPlanFilterParam splitPlanFilterParam) throws Exception {
		String url = InvoicingURL.split_plan_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, splitPlanFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SplitPlanBean.class)
				: null;
	}

	@Override
	public String createSplitPlan(SplitPlanParam splitPlanParam) throws Exception {
		String url = InvoicingURL.split_plan_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, splitPlanParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public SplitPlanDetailBean getSplitPlanDetail(String splitPlanId) throws Exception {
		String url = InvoicingURL.split_plan_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", splitPlanId);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SplitPlanDetailBean.class)
				: null;
	}

	@Override
	public boolean updateSplitPlan(SplitPlanParam splitPlanParam) throws Exception {
		String url = InvoicingURL.split_plan_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, splitPlanParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteSplitPlan(String id, int version) throws Exception {
		String url = InvoicingURL.split_plan_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		paramMap.put("version", String.valueOf(version));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<SplitSheetBean> searchSplitSheet(SplitSheetFiterParam splitSheetFiterParam) throws Exception {
		String url = InvoicingURL.split_sheet_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, splitSheetFiterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SplitSheetBean.class)
				: null;
	}

	@Override
	public String createSplitSheet(SplitSheetParam splitSheetCreateParam) throws Exception {
		String url = InvoicingURL.split_sheet_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, splitSheetCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("id") : null;
	}

	@Override
	public boolean updateSplitSheet(SplitSheetParam splitSheetUpdateParam) throws Exception {
		String url = InvoicingURL.split_sheet_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, splitSheetUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateSplitSheetStatus(String id, int status) throws Exception {
		String url = InvoicingURL.split_sheet_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		paramMap.put("status", String.valueOf(status));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;

	}

	@Override
	public SplitSheetDetailBean getSplitSheetDetail(String id) throws Exception {
		String url = InvoicingURL.split_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SplitSheetDetailBean.class)
				: null;
	}

	@Override
	public boolean deleteSplitSheet(String id) throws Exception {
		String url = InvoicingURL.split_sheet_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public SplitLossCountBean getSplitLossCount(SplitLossFilterParam splitLossFilterParam) throws Exception {
		String url = InvoicingURL.split_loss_count_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, splitLossFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), SplitLossCountBean.class)
				: null;
	}

	@Override
	public List<SplitLossBean> searchSplitLoss(SplitLossFilterParam splitLossFilterParam) throws Exception {
		String url = InvoicingURL.split_loss_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, splitLossFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), SplitLossBean.class)
				: null;
	}

	@Override
	public BigDecimal exportSplitLoss(SplitLossFilterParam splitLossFilterParam) throws Exception {
		String url = InvoicingURL.split_loss_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, splitLossFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1])
				: null;
	}

}
