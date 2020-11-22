package cn.guanmai.station.interfaces.system;

import java.util.List;

import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.bean.system.ServiceTimeLimitBean;
import cn.guanmai.station.bean.system.param.ServiceTimeParam;

/* 
* @author liming 
* @date Nov 8, 2018 11:15:30 AM 
* @des 运营时间相关接口
* @version 1.0 
*/
public interface ServiceTimeService {
	/**
	 * 根据运营时间ID获取运营时间相关详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ServiceTimeBean getServiceTimeById(String id) throws Exception;

	/**
	 * 获取运营时间的收货时间列表
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ServiceTimeLimitBean getServiceTimeLimit(String id) throws Exception;

	/**
	 * 获取指定运营时间的运营周期
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public List<String> getServiceTimeDateDetails(String id) throws Exception;

	/**
	 * 获取此站点所有的运营时间
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ServiceTimeBean> serviceTimeList() throws Exception;

	/**
	 * 获取此站点的所有运营时间-方式2
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ServiceTimeBean> allTimeConfig() throws Exception;

	/**
	 * 创建运营时间
	 * 
	 * @param serviceTime
	 * @return
	 * @throws Exception
	 */
	public boolean createServiceTime(ServiceTimeBean serviceTime) throws Exception;

	/********************************************************************/
	/**************************** 运营时间重构相关接口 **************************/
	/********************************************************************/

	/**
	 * 重构后的 拉取运营时间列表(0 返回部分主要信息,1返回所有的信息)
	 * 
	 * @param details
	 * @return
	 * @throws Exception
	 */
	public List<ServiceTimeBean> serviceTimeList(int details) throws Exception;

	/**
	 * 获取
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ServiceTimeBean getServiceTime(String id) throws Exception;

	/**
	 * 新建运营时间
	 * 
	 * @param serviceTimeParam
	 * @return
	 * @throws Exception
	 */
	public boolean createServiceTime(ServiceTimeParam serviceTimeParam) throws Exception;

	/**
	 * 修改运营时间
	 * 
	 * @param serviceTimeParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateServiceTime(ServiceTimeParam serviceTimeParam) throws Exception;

	/**
	 * 删除运营时间
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteServiceTime(String id) throws Exception;

}
