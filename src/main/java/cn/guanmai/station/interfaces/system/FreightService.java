package cn.guanmai.station.interfaces.system;

import java.math.BigDecimal;
import java.util.List;

import cn.guanmai.station.bean.system.FreightAddressBean;
import cn.guanmai.station.bean.system.FreightBean;
import cn.guanmai.station.bean.system.FreightDetailBean;
import cn.guanmai.station.bean.system.FreightSaleMenuBean;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午4:43:14
 * @description:
 * @version: 1.0
 */

public interface FreightService {
	/**
	 * 获取运费模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FreightBean> getFreights() throws Exception;

	/**
	 * 新建运费模板
	 * 
	 * @param freightDetail
	 * @param address_ids
	 * @return
	 * @throws Exception
	 */
	public boolean createFreight(FreightDetailBean freightDetail, List<BigDecimal> address_ids) throws Exception;

	/**
	 * 获取运费模板详细
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public FreightDetailBean getFreightDetail(String id) throws Exception;

	/**
	 * 修改运费模板
	 * 
	 * @param freightDetail
	 * @param address_ids
	 * @return
	 * @throws Exception
	 */
	public boolean updateFreight(FreightDetailBean freightDetail, List<BigDecimal> address_ids) throws Exception;

	/**
	 * 删除运费模板
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteFreight(String id) throws Exception;

	/**
	 * 设置默认运费模板
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean setDefaultFreight(String id) throws Exception;

	/**
	 * 获取默认生效报价单
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FreightSaleMenuBean> getFreighSaleMenus() throws Exception;

	/**
	 * 设置默认生效报价单
	 * 
	 * @param freight_id
	 * @param salemenu_ids
	 * @return
	 * @throws Exception
	 */
	public boolean updateFreighSaleMenu(String freight_id, List<String> salemenu_ids) throws Exception;

	/**
	 * 默认模板客户列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<FreightAddressBean> getFreightAddressList() throws Exception;
}
