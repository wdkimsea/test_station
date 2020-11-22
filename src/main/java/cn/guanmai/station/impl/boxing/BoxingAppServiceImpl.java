package cn.guanmai.station.impl.boxing;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.boxing.BoxBean;
import cn.guanmai.station.bean.boxing.BoxingManagePrintDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderListBean;
import cn.guanmai.station.bean.boxing.param.BoxParam;
import cn.guanmai.station.bean.boxing.param.BoxingManageOrderParam;
import cn.guanmai.station.bean.boxing.param.BoxingManagePrintParam;
import cn.guanmai.station.bean.boxing.param.BoxingOrderParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.boxing.BoxingAppService;
import cn.guanmai.station.url.BoxingURL;
import cn.guanmai.util.JsonUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by abc on 2020/2/20.
 *
 * @desc 装箱app业务实现类
 */
public class BoxingAppServiceImpl implements BoxingAppService {

	private BaseRequest baseRequest;
	private AsyncService asyncService;

	public BoxingAppServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
	}

	@Override
	public BoxingOrderListBean getBoxingOrderList(BoxingOrderParam param) throws Exception {
		String url = BoxingURL.get_box_order_list;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BoxingOrderListBean.class)
				: null;
	}

	@Override
	public BoxingOrderDetailBean getBoxingOrderDetail(BoxingOrderParam param) throws Exception {
		String url = BoxingURL.get_box_order_details;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BoxingOrderDetailBean.class)
				: null;

	}

	@Override
	public boolean updateBoxingOrderStatus(BoxingOrderParam param) throws Exception {
		String url = BoxingURL.update_box_order_status;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, param);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createBox() throws Exception {
		String url = BoxingURL.create_box;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST);

		return String.valueOf(retObj.getJSONObject("data").getInteger("box_id"));
	}

	@Override
	public boolean packageInBox(List<String> package_ids, String box_id) throws Exception {
		String url = BoxingURL.box_package_in_box;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("package_ids", JSON.parseArray(package_ids.toString()).toString());
		paramMap.put("box_id", box_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean packageOutBox(BoxParam boxParam) throws Exception {
		String url = BoxingURL.box_package_out_box;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, boxParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<BoxBean> getBoxDetails(String order_id) throws Exception {
		String url = BoxingURL.box_order_box_list;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("order_id", order_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BoxBean.class)
				: null;
	}

	@Override
	public boolean packageChangeBox(BoxParam boxParam) throws Exception {
		String url = BoxingURL.box_package_change_box;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, boxParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String searchBoxBarCode(String barcode, String time_config_id, String date) throws Exception {
		String url = BoxingURL.box_barcode_search;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("time_config_id", time_config_id);
		paramMap.put("date", date);
		paramMap.put("barcode", barcode);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("order_id") : null;
	}

	@Override
	public String searchBoxPackageId(String package_id) throws Exception {
		String url = BoxingURL.box_package_id_search;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("package_id", package_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("package_id") : null;
	}

	@Override
	public List<BoxingOrderDetailBean> getBoxingManageOrderList(BoxingManageOrderParam bmoParam) throws Exception {
		String url = BoxingURL.box_manage_order_list;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, bmoParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BoxingOrderDetailBean.class)
				: null;
	}

	@Override
	public String exportBoxingManageOrderList(BoxingManageOrderParam param) throws Exception {
		String url = BoxingURL.box_manage_order_list;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, param);
		String file_path = null;
		if (retObj.getInteger("code") == 0) {
			int async = retObj.getJSONObject("data").getInteger("async");
			if (async == 0) {
				file_path = retObj.getJSONObject("data").getString("filename");
			} else {
				BigDecimal task_id = new BigDecimal(retObj.getJSONObject("data").getString("task_url").split("=")[1]);
				System.out.println(task_id);
				AsyncTaskResultBean asyncTask = asyncService.getAsyncTaskResult(task_id);
				if (asyncTask != null) {
					if (asyncTask.getProgress() == 100 && asyncTask.getStatus() == 3) {
						String file_down_url = asyncTask.getResult().getLink();
						file_path = file_down_url;
					}

				}
			}
		}
		return file_path;
	}

	@Override
	public List<BoxingManagePrintDetailBean> printBoxingManageOrder(BoxingManagePrintParam printParam)
			throws Exception {
		String url = BoxingURL.box_manage_print;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, printParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BoxingManagePrintDetailBean.class)
				: null;
	}
}
