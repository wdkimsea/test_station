package cn.guanmai.station.impl.jingcai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.jingcai.WorkshopBean;
import cn.guanmai.station.bean.jingcai.param.WorkshopFilterParam;
import cn.guanmai.station.bean.jingcai.param.WorkshopParam;
import cn.guanmai.station.interfaces.jingcai.WorkshopService;
import cn.guanmai.station.url.JingcaiURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年4月27日 下午7:42:56
 * @description:
 * @version: 1.0
 */

public class WorkshopServiceImpl implements WorkshopService {
	private BaseRequest baseRequest;

	public WorkshopServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public String createWorkshop(WorkshopParam workshopCreateParam) throws Exception {
		String url = JingcaiURL.workshop_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, workshopCreateParam);
		return retObj.getInteger("code") == 0 ? retObj.getJSONObject("data").getString("workshop_id") : null;
	}

	@Override
	public boolean updateWorkshop(WorkshopParam workshopUpdateParam) throws Exception {
		String url = JingcaiURL.workshop_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, workshopUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteWorkshop(String workshop_id) throws Exception {
		String url = JingcaiURL.workshop_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workshop_id", workshop_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<WorkshopBean> searchWorkshop(WorkshopFilterParam workshopFilterParam) throws Exception {
		String url = JingcaiURL.workshop_list_url;

		boolean more = true;
		List<WorkshopBean> workshops = new ArrayList<WorkshopBean>();
		List<WorkshopBean> tempWorkshops = null;
		while (more) {
			JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, workshopFilterParam);
			if (retObj.getInteger("code") == 0) {
				tempWorkshops = JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), WorkshopBean.class);
				workshops.addAll(tempWorkshops);

				more = retObj.getJSONObject("pagination").getBoolean("more");
				if (more) {
					workshopFilterParam.setPage_obj(
							JSONArray.parseArray(retObj.getJSONObject("pagination").getString("page_obj")));
				}
			} else {
				workshops = null;
				more = false;
			}
		}
		return workshops;
	}

	@Override
	public WorkshopBean getWorkshop(String workshop_id) throws Exception {
		String url = JingcaiURL.workshop_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("workshop_id", workshop_id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), WorkshopBean.class)
				: null;
	}

}
