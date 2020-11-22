package cn.guanmai.bshop.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.bean.account.BsRegisterAreaBean;
import cn.guanmai.bshop.bean.account.param.BsAddressParam;
import cn.guanmai.bshop.bean.account.param.BsRegisterParam;
import cn.guanmai.bshop.service.BsAccountService;
import cn.guanmai.bshop.url.BsAccountURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午3:12:52
 * @description:
 * @version: 1.0
 */

public class BsAccountServiceImpl implements BsAccountService {
	private BaseRequest baseRequest;

	public BsAccountServiceImpl(Map<String, String> bs_headers) {
		baseRequest = new BaseRequestImpl(bs_headers);
	}

	@Override
	public BsAccountBean getAccountInfo() throws Exception {
		String url = BsAccountURL.USER_INFO_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), BsAccountBean.class)
				: null;
	}

	@Override
	public boolean setAddress(String address_id) throws Exception {
		String url = BsAccountURL.SET_ADDRESS_URL;
		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("address_id", address_id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean register(BsRegisterParam registerParam) throws Exception {
		String url = BsAccountURL.REGISTER_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, registerParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<BsRegisterAreaBean> getRegisterArea() throws Exception {
		String url = BsAccountURL.REGISTER_AREA_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsRegisterAreaBean.class)
				: null;
	}

	@Override
	public boolean addAddress(BsAddressParam addressAddParam) throws Exception {
		String url = BsAccountURL.ADD_ADDRESS_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, addressAddParam);

		return retObj.getInteger("code") == 0;
	}

}
