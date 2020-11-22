package cn.guanmai.bshop.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午4:49:00
 * @description:
 * @version: 1.0
 */

public class BsMarketingURL {
	public static final String bshopUrl = ConfigureUtil.getValueByKey("bshopUrl");

	// 获取可见优惠券列表接口
	public static final String VISIBLE_COUPON_URL = bshopUrl + "/coupon/visible_coupon";

	// 获取可用优惠券列表接口
	public static final String AVAIL_COUPON_URL = bshopUrl + "/coupon/avail_coupon/list";

	// 优惠券领取接口
	public static final String COLLECT_COUPON_URL = bshopUrl + "/coupon/collect";
}
