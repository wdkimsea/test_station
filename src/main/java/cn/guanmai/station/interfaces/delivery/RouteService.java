package cn.guanmai.station.interfaces.delivery;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.delivery.RouteBean;
import cn.guanmai.station.bean.delivery.RouteBindCustomerBean;
import cn.guanmai.station.bean.delivery.RouteTask;
import cn.guanmai.station.bean.delivery.param.RouteTaskFilterParam;

/* 
* @author liming 
* @date Apr 1, 2019 2:39:38 PM 
* @des 线路相关接口
* @version 1.0 
*/
public interface RouteService {
	/**
	 * 创建线路
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean createRoute(String name) throws Exception;

	/**
	 * 删除路线
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteRoute(BigDecimal id) throws Exception;

	/**
	 * 获取搜索的线路
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<RouteBean> getAllRoutes() throws Exception;

	/**
	 * 获取路线列表
	 * 
	 * @param search_text
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<RouteBean> getRouteList(String search_text, int offset, int limit) throws Exception;

	/**
	 * 获取指定路线详细信息
	 * 
	 * @param route_id
	 * @return
	 * @throws Exception
	 */
	public List<RouteBindCustomerBean> getRouteBindCustomer(BigDecimal route_id) throws Exception;

	/**
	 * 更新路线绑定的客户
	 * 
	 * @param address_route_id
	 * @param address_ids
	 * @return
	 * @throws Exception
	 */
	public boolean updateRouteBindCustomer(BigDecimal address_route_id, List<BigDecimal> address_ids) throws Exception;

	/**
	 * 导出路线
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public boolean exportRoute(String search_text) throws Exception;

	/**
	 * 线路任务列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<RouteTask> searchRouteTasks(RouteTaskFilterParam filterParam) throws Exception;

	/**
	 * 初始化线路信息,把没有分配线路的商户都绑定到 [人民路] 线路上
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal initRouteData() throws Exception;

}
