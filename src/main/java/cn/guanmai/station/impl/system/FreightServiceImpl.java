package cn.guanmai.station.impl.system;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.FreightAddressBean;
import cn.guanmai.station.bean.system.FreightBean;
import cn.guanmai.station.bean.system.FreightDetailBean;
import cn.guanmai.station.bean.system.FreightSaleMenuBean;
import cn.guanmai.station.interfaces.system.FreightService;
import cn.guanmai.station.url.SystemURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午4:48:16
 * @description:
 * @version: 1.0
 */

public class FreightServiceImpl implements FreightService {
	private BaseRequest baseRequest;

	public FreightServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<FreightBean> getFreights() throws Exception {
		String url = SystemURL.freight_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);
		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), FreightBean.class)
				: null;
	}

	@Override
	public boolean createFreight(FreightDetailBean freightDetail, List<BigDecimal> address_ids) throws Exception {
		String url = SystemURL.freight_create_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delivery_freight", JsonUtil.objectToStr(freightDetail.getDelivery_freight()));
		paramMap.put("pick_up_freight", JsonUtil.objectToStr(freightDetail.getPick_up_freight()));
		paramMap.put("address_ids", JsonUtil.objectToStr(address_ids));
		paramMap.put("name", freightDetail.getName());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public FreightDetailBean getFreightDetail(String id) throws Exception {
		String url = SystemURL.freight_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), FreightDetailBean.class)
				: null;
	}

	@Override
	public boolean updateFreight(FreightDetailBean freightDetail, List<BigDecimal> address_ids) throws Exception {
		String url = SystemURL.freight_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", freightDetail.getId());
		paramMap.put("delivery_freight", JsonUtil.objectToStr(freightDetail.getDelivery_freight()));
		paramMap.put("pick_up_freight", JsonUtil.objectToStr(freightDetail.getPick_up_freight()));
		paramMap.put("address_ids", JSONArray.toJSONString(address_ids));
		paramMap.put("name", freightDetail.getName());

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteFreight(String id) throws Exception {
		String url = SystemURL.freight_delete_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("freight_id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean setDefaultFreight(String id) throws Exception {
		String url = SystemURL.freight_default_set_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("freight_id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<FreightSaleMenuBean> getFreighSaleMenus() throws Exception {
		String url = SystemURL.freight_sale_menu_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), FreightSaleMenuBean.class)
				: null;
	}

	@Override
	public boolean updateFreighSaleMenu(String freight_id, List<String> salemenu_ids) throws Exception {
		String url = SystemURL.freight_sale_menu_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("freight_id", freight_id);
		paramMap.put("salemenu_ids", JsonUtil.objectToStr(salemenu_ids));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<FreightAddressBean> getFreightAddressList() throws Exception {
		String url = SystemURL.freight_address_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), FreightAddressBean.class)
				: null;
	}

}
