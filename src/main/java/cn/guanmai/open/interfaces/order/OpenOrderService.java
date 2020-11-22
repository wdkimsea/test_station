package cn.guanmai.open.interfaces.order;

import java.util.List;
import java.util.Map;

import cn.guanmai.open.bean.order.OpenOrderBean;
import cn.guanmai.open.bean.order.OpenOrderDetailBean;
import cn.guanmai.open.bean.order.param.OrderAbnormalCreateParam;
import cn.guanmai.open.bean.order.param.OrderAbnormalDeleteParam;
import cn.guanmai.open.bean.order.param.OrderAbnormalUpdateParam;
import cn.guanmai.open.bean.order.param.OrderCreateParam;
import cn.guanmai.open.bean.order.param.OrderProductParam;
import cn.guanmai.open.bean.order.param.OrderRefundCreateParam;
import cn.guanmai.open.bean.order.param.OrderRefundDeleteParam;
import cn.guanmai.open.bean.order.param.OrderRefundUpdateParam;
import cn.guanmai.open.bean.order.param.OrderSearchParam;
import cn.guanmai.open.bean.order.param.OrderUpdateParam;

/* 
* @author liming 
* @date Jun 4, 2019 2:42:28 PM 
* @des 订单相关服务接口
* @version 1.0 
*/
public interface OpenOrderService {
	/**
	 * 创建订单
	 * 
	 * @param createParam
	 * @return
	 * @throws Exception
	 */
	public String createOrder(OrderCreateParam createParam) throws Exception;

	/**
	 * 删除订单
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrder(String order_id) throws Exception;

	/**
	 * 修改订单
	 * 
	 * @param updateParam
	 * @return
	 */
	public boolean updateOrder(OrderUpdateParam updateParam) throws Exception;

	/**
	 * 获取订单详细信息
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public OpenOrderDetailBean getOrderDetail(String order_id) throws Exception;

	/**
	 * 查询订单列表
	 * 
	 * @param searchParam
	 * @return
	 */
	public List<OpenOrderBean> searchOrder(OrderSearchParam searchParam) throws Exception;

	/**
	 * 订单添加商品
	 * 
	 * @param order_id
	 * @param products
	 * @return
	 * @throws Exception
	 */
	public boolean addOrderSkus(String order_id, List<OrderProductParam> products) throws Exception;

	/**
	 * 删除订单商品
	 * 
	 * @param order_id
	 * @param sku_ids
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrderSkus(String order_id, List<String> sku_ids) throws Exception;

	/**
	 * 修改订单商品
	 * 
	 * @param order_id
	 * @param products
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderSkus(String order_id, List<OrderProductParam> products) throws Exception;

	/**
	 * 新建订单异常
	 * 
	 * @param order_id
	 * @param abnormals
	 * @return
	 * @throws Exception
	 */
	public boolean createOrderAbnormal(String order_id, List<OrderAbnormalCreateParam> abnormals) throws Exception;

	/**
	 * 新建订单退货
	 * 
	 * @param order_id
	 * @param refunds
	 * @return
	 * @throws Exception
	 */
	public boolean createOrderRefund(String order_id, List<OrderRefundCreateParam> refunds) throws Exception;

	/**
	 * 修改订单异常
	 * 
	 * @param order_id
	 * @param abnormals
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderAbnormal(String order_id, List<OrderAbnormalUpdateParam> abnormals) throws Exception;

	/**
	 * 修改订单退货
	 * 
	 * @param order_id
	 * @param refunds
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderRefund(String order_id, List<OrderRefundUpdateParam> refunds) throws Exception;

	/**
	 * 删除订单异常
	 * 
	 * @param order_id
	 * @param abnormals
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrderAbnormal(String order_id, List<OrderAbnormalDeleteParam> abnormals) throws Exception;

	/**
	 * 删除订单退货
	 * 
	 * @param order_id
	 * @param refunds
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrderRefund(String order_id, List<OrderRefundDeleteParam> refunds) throws Exception;

	/**
	 * 获取异常原因集合
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getExceptionReasons() throws Exception;

}
