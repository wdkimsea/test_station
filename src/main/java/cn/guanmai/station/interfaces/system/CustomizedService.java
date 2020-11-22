package cn.guanmai.station.interfaces.system;

import cn.guanmai.station.bean.system.CustomizedBean;

/**
 * @author liming
 * @date 2019年9月25日
 * @time 上午10:23:53
 * @des 店铺运营相关设置
 */

public interface CustomizedService {
	/**
	 * 获取商户定制相关信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public CustomizedBean getCustomized() throws Exception;

	/**
	 * 获取bshop的cms_key
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getCmsKey() throws Exception;

	/**
	 * 更新商户定制
	 * 
	 * @param bshopCustomized
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomized(CustomizedBean customized) throws Exception;
}
