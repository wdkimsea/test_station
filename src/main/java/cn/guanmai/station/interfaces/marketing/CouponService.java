package cn.guanmai.station.interfaces.marketing;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.marketing.CouponAddressBean;
import cn.guanmai.station.bean.marketing.CouponBean;
import cn.guanmai.station.bean.marketing.CouponDetailBean;
import cn.guanmai.station.bean.marketing.CouponPageBean;
import cn.guanmai.station.bean.marketing.CouponUsageBean;
import cn.guanmai.station.bean.marketing.param.CouponFilterParam;
import cn.guanmai.station.bean.marketing.param.CouponParam;
import cn.guanmai.station.bean.marketing.param.CouponUsageFilterParam;

/**
 * @author: liming
 * @Date: 2020年6月1日 下午5:11:25
 * @description:
 * @version: 1.0
 */

public interface CouponService {
	/**
	 * 搜索过滤优惠券
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CouponBean> searchCoupon(CouponFilterParam couponFilterParam) throws Exception;

	/**
	 * 优惠券使用情况搜索过滤
	 * 
	 * @param couponUsageFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<CouponUsageBean> searchCouponUsage(CouponUsageFilterParam couponUsageFilterParam) throws Exception;

	/**
	 * 获取优惠券详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public CouponDetailBean getCouponDetail(String id) throws Exception;

	/**
	 * 更新优惠券状态信息
	 * 
	 * @param id
	 * @param is_active
	 * @return
	 * @throws Exception
	 */
	public boolean editCouponStatus(String id, int is_active) throws Exception;

	/**
	 * 新建优惠券
	 * 
	 * @param couponParam
	 * @return
	 * @throws Exception
	 */
	public String createCoupon(CouponParam couponParam) throws Exception;

	/**
	 * 导出优惠券使用记录
	 * 
	 * @param couponUsageFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportCouponUsage(CouponUsageFilterParam couponUsageFilterParam) throws Exception;

	/**
	 * 优惠券搜索商户
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CouponAddressBean> searchCouponAddress() throws Exception;

	/**
	 * 带有分页信息的搜索过滤
	 * 
	 * @param couponFilterParam
	 * @return
	 * @throws Exception
	 */
	public CouponPageBean searchCouponPage(CouponFilterParam couponFilterParam) throws Exception;
}
