package cn.guanmai.manage.interfaces.ordermanange;

import java.util.List;

import cn.guanmai.manage.bean.ordermanage.param.DailyOrderParamBean;
import cn.guanmai.manage.bean.ordermanage.param.OrderExceptionParamBean;
import cn.guanmai.manage.bean.ordermanage.result.DailyOrderBean;
import cn.guanmai.manage.bean.ordermanage.result.OrderDetailInfoBean;

/* 
* @author liming 
* @date Jan 18, 2019 5:23:43 PM 
* @des 订单管理相关接口
* @version 1.0 
*/
public interface OrderManangeService {
	/**
	 * 每日订单查询接口
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<DailyOrderBean> searchDailyOrder(DailyOrderParamBean paramBean) throws Exception;

	/**
	 * 获取地区信息接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> districtCodeList() throws Exception;

	/**
	 * 导出每日订单
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean exportDailyOrder(DailyOrderParamBean paramBean) throws Exception;

	/**
	 * 用户订单异常页面搜索订单
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean searchOrder(String order_id) throws Exception;

	/**
	 * 用户订单异常页面获取订单详情
	 * 
	 * @param order_id
	 * @return
	 */
	public OrderDetailInfoBean getOrderDetailInfo(String order_id) throws Exception;

	/**
	 * 获取token 用于添加订单售后
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public String getToken(String order_id) throws Exception;

	/**
	 * 添加售后信息
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean addOrderException(OrderExceptionParamBean paramBean) throws Exception;

}
