package cn.guanmai.open.impl.purchase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseSheetDetailBean;
import cn.guanmai.open.bean.purchase.OpenPurchaseTaskBean;
import cn.guanmai.open.bean.purchase.OpenTimeConfigBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetCommonParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseSheetFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskCreateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskFilterParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaseTaskUpdateParam;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.url.OpenPurchaseURL;
import cn.guanmai.request.impl.OpenRequestImpl;
import cn.guanmai.request.intefaces.OpenRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author liming
 * @date 2019年11月11日
 * @time 下午7:52:24
 * @des TODO
 */

public class OpenPurcahseServiceImpl implements OpenPurcahseService {
	private OpenRequest openRequest;

	public OpenPurcahseServiceImpl(String access_token) {
		openRequest = new OpenRequestImpl(access_token);
	}

	@Override
	public List<OpenPurcahserBean> queryPurchaser(OpenPurchaserFilterParam filterParam) throws Exception {
		String url = OpenPurchaseURL.query_purcahser_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenPurcahserBean.class)
				: null;
	}

	@Override
	public List<OpenPurchaseSheetBean> queryPurchaseSheet(OpenPurchaseSheetFilterParam filterParam) throws Exception {
		String url = OpenPurchaseURL.query_purchase_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, filterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenPurchaseSheetBean.class)
				: null;
	}

	@Override
	public OpenPurchaseSheetDetailBean getPurchaseSheetDetail(String purchase_sheet_id) throws Exception {
		String url = OpenPurchaseURL.get_purchase_sheet_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("purchase_sheet_id", purchase_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, paramMap);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), OpenPurchaseSheetDetailBean.class)
				: null;
	}

	@Override
	public String createPurchaseSheet(OpenPurchaseSheetCommonParam openPurchaseSheetCreateParam) throws Exception {
		String url = OpenPurchaseURL.create_purchase_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openPurchaseSheetCreateParam);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("purchase_sheet_id") : null;
	}

	@Override
	public boolean updatePurchaseSheet(OpenPurchaseSheetCommonParam openPurchaseSheetUpdateParam) throws Exception {
		String url = OpenPurchaseURL.update_purchase_sheet_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openPurchaseSheetUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean submitPurchaseSheet(String purchase_sheet_id) throws Exception {
		String url = OpenPurchaseURL.submit_purchase_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("purchase_sheet_id", purchase_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePurcahseSheet(String purchase_sheet_id) throws Exception {
		String url = OpenPurchaseURL.delete_purchase_sheet_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("purchase_sheet_id", purchase_sheet_id);

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<OpenTimeConfigBean> getTimeConfigs() throws Exception {
		String url = OpenPurchaseURL.get_time_config_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenTimeConfigBean.class)
				: null;
	}

	@Override
	public List<OpenPurchaseTaskBean> queryPurcahseTask(OpenPurchaseTaskFilterParam openPurchaseTaskFilterParam)
			throws Exception {
		String url = OpenPurchaseURL.query_purchase_task_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.GET, openPurchaseTaskFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), OpenPurchaseTaskBean.class)
				: null;
	}

	@Override
	public boolean createPurchaseTask(OpenPurchaseTaskCreateParam openPurchaseTaskCreateParam) throws Exception {
		String url = OpenPurchaseURL.create_purchase_task_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openPurchaseTaskCreateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updatePurcahseTask(OpenPurchaseTaskUpdateParam openPurchaseTaskUpdateParam) throws Exception {
		String url = OpenPurchaseURL.update_purcahse_task_url;

		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, openPurchaseTaskUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean publishPurchaseTask(String task_id) throws Exception {
		String url = OpenPurchaseURL.publish_purchase_task_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("task_id", task_id);
		JSONObject retObj = openRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

}
