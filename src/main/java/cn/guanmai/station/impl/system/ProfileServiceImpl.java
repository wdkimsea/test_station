package cn.guanmai.station.impl.system;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.system.param.MerchandiseProfileParam;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.bean.system.param.SortingProfileParam;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.url.SystemURL;

/**
 * @author liming
 * @date 2020年1月7日
 * @time 下午4:48:45
 * @des TODO
 */

public class ProfileServiceImpl implements ProfileService {
	private BaseRequest baseRequest;

	public ProfileServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public boolean updateOrderProfile(OrderProfileParam orderProfileParam) throws Exception {
		String url = SystemURL.order_profile_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, orderProfileParam);

		return retObj.getInteger("code")== 0;
	}

	@Override
	public boolean updateMerchandiseProfile(MerchandiseProfileParam merchandiseProfileParam) throws Exception {
		String url = SystemURL.merchandise_profile_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, merchandiseProfileParam);

		return retObj.getInteger("code")== 0;
	}

	@Override
	public boolean updateSortingProfile(SortingProfileParam sortingProfileParam) throws Exception {
		String url = SystemURL.sorting_profile_update_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, sortingProfileParam);

		return retObj.getInteger("code")== 0;
	}

}
