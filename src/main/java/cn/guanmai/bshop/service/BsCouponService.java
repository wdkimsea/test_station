package cn.guanmai.bshop.service;

import java.util.List;

import cn.guanmai.bshop.bean.marketing.BsCouponAvailBean;
import cn.guanmai.bshop.bean.marketing.BsCouponVisibleBean;

/**
 * @author: liming
 * @Date: 2020年6月15日 下午3:32:13
 * @description: 优惠券相关接口
 * @version: 1.0
 */

public interface BsCouponService {
	/**
	 * 获取可见优惠券列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BsCouponVisibleBean> getVisibleCoupons() throws Exception;

	/**
	 * 获取可用优惠券列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BsCouponAvailBean> getAvailCoupons() throws Exception;

	/**
	 * 领取优惠券
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public String collectCoupon(String id) throws Exception;

}
