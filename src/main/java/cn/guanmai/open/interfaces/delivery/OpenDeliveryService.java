package cn.guanmai.open.interfaces.delivery;

import java.util.List;

import cn.guanmai.open.bean.delivery.OpenCarModelBean;
import cn.guanmai.open.bean.delivery.OpenCarrierBean;
import cn.guanmai.open.bean.delivery.OpenDeliveryTaskBean;
import cn.guanmai.open.bean.delivery.OpenDriverBean;
import cn.guanmai.open.bean.delivery.OpenRouteBean;
import cn.guanmai.open.bean.delivery.OpenRouteDetailBean;
import cn.guanmai.open.bean.delivery.param.DeliveryTaskFilterParam;
import cn.guanmai.open.bean.delivery.param.DrivcerCreateParam;
import cn.guanmai.open.bean.delivery.param.DrivcerUpdateParam;

/* 
* @author liming 
* @date Jun 5, 2019 2:15:34 PM 
* @des 配送相关业务接口
* @version 1.0 
*/
public interface OpenDeliveryService {
	/**
	 * 搜索过滤配送任务
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<OpenDeliveryTaskBean> searchDeliveryTasks(DeliveryTaskFilterParam filterParam) throws Exception;

	/**
	 * 订单分配司机
	 * 
	 * @param order_ids
	 * @param driver_id
	 * @return
	 * @throws Exception
	 */
	public boolean assignDriver(List<String> order_ids, String driver_id) throws Exception;

	/**
	 * 订单取消分配司机
	 * 
	 * @param order_ids
	 * @return
	 * @throws Exception
	 */
	public boolean cancelDriver(List<String> order_ids) throws Exception;

	/**
	 * 创建司机
	 * 
	 * @return
	 * @throws Exception
	 */
	public String createDriver(DrivcerCreateParam createParam) throws Exception;

	/**
	 * 修改司机
	 * 
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateDriver(DrivcerUpdateParam updateParam) throws Exception;

	/**
	 * 查询过滤司机
	 * 
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenDriverBean> searchDriver(String search_text, String offset, String limit) throws Exception;

	/**
	 * 查询车型
	 * 
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenCarModelBean> searchCarModel(String search_text, String offset, String limit) throws Exception;

	/**
	 * 查询承运商列表
	 * 
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenCarrierBean> searchCarrier(String search_text, String offset, String limit) throws Exception;

	/**
	 * 新建线路
	 * 
	 * @param route_name
	 * @param customer_ids
	 * @return
	 * @throws Exception
	 */
	public boolean createRoute(String route_name, List<String> customer_ids) throws Exception;

	/**
	 * 修改线路
	 * 
	 * @param route_id
	 * @param route_name
	 * @param customer_ids
	 * @return
	 * @throws Exception
	 */
	public boolean updateRoute(String route_id, String route_name, List<String> customer_ids) throws Exception;

	/**
	 * 删除线路
	 * 
	 * @param route_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRoute(String route_id) throws Exception;

	/**
	 * 查询路线列表
	 * 
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenRouteBean> searchRoute(String search_text, String offset, String limit) throws Exception;

	/**
	 * 获取配送线路详情
	 * 
	 * @param route_id
	 * @return
	 * @throws Exception
	 */
	public OpenRouteDetailBean getRouteDetail(String route_id) throws Exception;

}
