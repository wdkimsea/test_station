package cn.guanmai.bshop.service;

import java.util.List;

import cn.guanmai.bshop.bean.order.BsCartBean;
import cn.guanmai.bshop.bean.order.BsOrderDetailBean;
import cn.guanmai.bshop.bean.order.BsOrderResultBean;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;

/**
 * @author: liming
 * @Date: 2020年6月16日 下午2:12:06
 * @description: 订单相关接口
 * @version: 1.0
 */

public interface BsOrderService {

	/**
	 * 搜索过滤商品
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<BsProductBean> searchProducts(String search_text) throws Exception;

	/**
	 * 更新购物车
	 * 
	 * @param orderProducts
	 * @return
	 * @throws Exception
	 */
	public boolean updateCart(List<BsProductBean> orderProducts) throws Exception;

	/**
	 * 设置付款方式
	 * 
	 * @param paymethod
	 * @return
	 * @throws Exception
	 */
	public boolean setPaymethod(PayMethod paymethod) throws Exception;

	/**
	 * 设置收货时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean setReceiveTime() throws Exception;

	/**
	 * 获取购物车
	 * 
	 * @return
	 * @throws Exception
	 */
	public BsCartBean getCart() throws Exception;

	/**
	 * 提交订单,是否合单
	 * 
	 * @param isCombineOrder
	 * @return
	 * @throws Exception
	 */
	public List<BsOrderResultBean> submitCart(boolean isCombineOrder) throws Exception;

	/**
	 * 提交订单,使用优惠券
	 * 
	 * @param avail_coupon_id
	 * @return
	 * @throws Exception
	 */
	public BsOrderResultBean submitCart(String avail_coupon_id) throws Exception;

	/**
	 * 获取订单详情
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public BsOrderDetailBean getOrderDetail(String order_id) throws Exception;

	/**
	 * 支付订单
	 * 
	 * @param order_ids
	 * @return
	 * @throws Exception
	 */
	public boolean payOrder(List<String> order_ids) throws Exception;

	/**
	 * 获取部分支付订单列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getPartPayOrders() throws Exception;
}
