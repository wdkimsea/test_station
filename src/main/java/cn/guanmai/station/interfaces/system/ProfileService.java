package cn.guanmai.station.interfaces.system;

import cn.guanmai.station.bean.system.param.MerchandiseProfileParam;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.bean.system.param.SortingProfileParam;

/**
 * @author liming
 * @date 2020年1月7日
 * @time 下午4:45:11
 * @des TODO
 */

public interface ProfileService {

	/**
	 * 订单自定义相关配置
	 * 
	 * @param orderProfileParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderProfile(OrderProfileParam orderProfileParam) throws Exception;

	/**
	 * 系统设置-商品设置接口
	 * 
	 * @param merchandiseProfileParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateMerchandiseProfile(MerchandiseProfileParam merchandiseProfileParam) throws Exception;

	/**
	 * 系统设置-分拣设置
	 * 
	 * @param sortingProfileParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSortingProfile(SortingProfileParam sortingProfileParam) throws Exception;

}
