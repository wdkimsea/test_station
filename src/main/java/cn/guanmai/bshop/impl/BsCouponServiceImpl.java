package cn.guanmai.bshop.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.bshop.bean.marketing.BsCouponAvailBean;
import cn.guanmai.bshop.bean.marketing.BsCouponVisibleBean;
import cn.guanmai.bshop.service.BsCouponService;
import cn.guanmai.bshop.url.BsMarketingURL;
import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午4:45:26
 * @description:
 * @version: 1.0
 */

public class BsCouponServiceImpl implements BsCouponService {
	private BaseRequest baseRequest;

	public BsCouponServiceImpl(Map<String, String> bs_headers) {
		baseRequest = new BaseRequestImpl(bs_headers);
	}

	@Override
	public List<BsCouponVisibleBean> getVisibleCoupons() throws Exception {
		String url = BsMarketingURL.VISIBLE_COUPON_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsCouponVisibleBean.class)
				: null;
	}

	@Override
	public String collectCoupon(String id) throws Exception {
		String url = BsMarketingURL.COLLECT_COUPON_URL;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public List<BsCouponAvailBean> getAvailCoupons() throws Exception {
		String url = BsMarketingURL.AVAIL_COUPON_URL;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), BsCouponAvailBean.class)
				: null;
	}

}
