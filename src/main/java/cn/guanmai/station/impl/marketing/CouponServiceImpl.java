package cn.guanmai.station.impl.marketing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
import cn.guanmai.station.bean.marketing.CouponAddressBean;
import cn.guanmai.station.bean.marketing.CouponBean;
import cn.guanmai.station.bean.marketing.CouponDetailBean;
import cn.guanmai.station.bean.marketing.CouponPageBean;
import cn.guanmai.station.bean.marketing.CouponUsageBean;
import cn.guanmai.station.bean.marketing.param.CouponFilterParam;
import cn.guanmai.station.bean.marketing.param.CouponParam;
import cn.guanmai.station.bean.marketing.param.CouponUsageFilterParam;
import cn.guanmai.station.interfaces.marketing.CouponService;
import cn.guanmai.station.url.MarketingURL;
import cn.guanmai.util.JsonUtil;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午7:02:50
 * @description:
 * @version: 1.0
 */

public class CouponServiceImpl implements CouponService {
	private BaseRequest baseRequest;

	public CouponServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public List<CouponBean> searchCoupon(CouponFilterParam couponFilterParam) throws Exception {
		String url = MarketingURL.coupon_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, couponFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CouponBean.class)
				: null;
	}

	@Override
	public List<CouponUsageBean> searchCouponUsage(CouponUsageFilterParam couponUsageFilterParam) throws Exception {
		String url = MarketingURL.coupon_usage_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, couponUsageFilterParam);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CouponUsageBean.class)
				: null;
	}

	@Override
	public CouponDetailBean getCouponDetail(String id) throws Exception {
		String url = MarketingURL.coupon_get_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), CouponDetailBean.class)
				: null;
	}

	@Override
	public boolean editCouponStatus(String id, int is_active) throws Exception {
		String url = MarketingURL.coupon_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("id", String.valueOf(id));
		paramMap.put("is_active", String.valueOf(is_active));

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public String createCoupon(CouponParam couponParam) throws Exception {
		String url = MarketingURL.coupon_create_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, couponParam);

		return retObj.getInteger("code") == 0 ? retObj.getString("data") : null;
	}

	@Override
	public BigDecimal exportCouponUsage(CouponUsageFilterParam couponUsageFilterParam) throws Exception {
		String url = MarketingURL.coupon_usage_export_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, couponUsageFilterParam);

		return retObj.getInteger("code") == 0
				? new BigDecimal(retObj.getJSONObject("data").getString("task_url").substring(18))
				: null;
	}

	@Override
	public List<CouponAddressBean> searchCouponAddress() throws Exception {
		String url = MarketingURL.coupon_address_list_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("search_type", "1");

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("list").toString(), CouponAddressBean.class) : null;
	}

	@Override
	public CouponPageBean searchCouponPage(CouponFilterParam couponFilterParam) throws Exception {
		String url = MarketingURL.coupon_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, couponFilterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassObject(retObj.toString(), CouponPageBean.class)
				: null;
	}

}
