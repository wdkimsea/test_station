package cn.guanmai.station.interfaces.invoicing;

import java.util.List;

import cn.guanmai.station.bean.invoicing.SupplierAccountBean;
import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;

/* 
* @author liming 
* @date Nov 7, 2018 4:07:10 PM 
* @des 供应商相关接口
* @version 1.0 
*/
public interface SupplierService {

	/**
	 * 供应商列表搜索过滤
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<SupplierBean> searchSupplier(String search_text) throws Exception;

	/**
	 * 创建供应商
	 * 
	 * @return
	 */
	public String createSupplier(SupplierDetailBean supplier) throws Exception;

	/**
	 * 通过ID获取供应商详细信息
	 * 
	 * @param id
	 * @return
	 */
	public SupplierDetailBean getSupplierById(String id) throws Exception;

	/**
	 * 通过customer_id获取供应商信息
	 * 
	 * @param customer_id
	 * @return
	 */
	public List<SupplierDetailBean> getSupplierByCustomerId(String customer_id) throws Exception;

	/**
	 * 更新供应商信息
	 * 
	 * @param supplier
	 * @return
	 */
	public boolean updateSupplier(SupplierDetailBean supplier) throws Exception;

	/**
	 * 删除供应商
	 * 
	 * @param id
	 * @return
	 */
	public boolean deleteSupplier(String id) throws Exception;

	/**
	 * 获取所有的供应商信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SupplierDetailBean> getSettleSupplierList() throws Exception;

	/**
	 * 获取供应商登录账号
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SupplierAccountBean> getSupplierAccounts() throws Exception;

}
