package cn.guanmai.station.impl.delivery;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.delivery.PickUpStationBean;
import cn.guanmai.station.bean.delivery.param.PickUpStationFilterParam;
import cn.guanmai.station.interfaces.delivery.PickUpStationService;
import cn.guanmai.station.url.DistributeURL;
import cn.guanmai.util.JsonUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 自提点相关业务 Created by yangjinhai on 2019/8/26.
 */
public class PickUpStationServiceImpl implements PickUpStationService {

	private BaseRequest baseRequest;

	public PickUpStationServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean createPickUpStation(PickUpStationBean pickUpStation) throws Exception {
		String urlStr = DistributeURL.create_pick_up_station;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, pickUpStation);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<PickUpStationBean> queryPickUpStations(String business_status, String searchText) throws Exception {
		String urlStr = DistributeURL.query_pick_up_station_list;

		Map<String, String> params = new HashMap<>();
		params.put("limit", "10");
		params.put("offset", "0");
		params.put("peek", "60");
		params.put("business_status", business_status);
		params.put("search_text", searchText);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PickUpStationBean.class)
				: null;
	}

	/**
	 * 根据地理位置以及自提点状态查询自提点
	 *
	 * @param pickUpStationBean 传入查询的参数
	 * @param searchText        搜索内容
	 * @return 查询结果
	 * @throws Exception
	 */
	@Override
	public List<PickUpStationBean> queryPickUpStations(PickUpStationFilterParam pickUpStationFilterParam)
			throws Exception {
		String urlStr = DistributeURL.query_pick_up_station_list;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, pickUpStationFilterParam);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), PickUpStationBean.class)
				: null;
	}

	/**
	 * 根据ID查询创建的自提点
	 *
	 * @param id
	 */
	@Override
	public PickUpStationBean getPickUpStationDetailInfo(String id) throws Exception {
		String urlStr = DistributeURL.query_pick_up_station_get;
		Map<String, String> params = new HashMap<>();
		params.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, params);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), PickUpStationBean.class)
				: null;
	}

	@Override
	public boolean updatePickUpStation(PickUpStationBean pickUpStationBean) throws Exception {
		String urlStr = DistributeURL.update_pick_up_station;
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, pickUpStationBean);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deletePickUpStation(String id) throws Exception {
		String urlStr = DistributeURL.delete_pick_up_station;
		Map<String, String> params = new HashMap<>();
		params.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, params);

		return retObj.getInteger("code") == 0;
	}
}
