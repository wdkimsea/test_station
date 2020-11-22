package cn.guanmai.open.interfaces.customer;

import java.util.List;

import cn.guanmai.open.bean.customer.OpenCustomerAreaBean;
import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.customer.param.OpenCustomerCreateParam;
import cn.guanmai.open.bean.customer.param.OpenCustomerUpdateParam;

/* 
* @author liming 
* @date Jun 6, 2019 9:51:49 AM 
* @des 商户相关业务接口
* @version 1.0 
*/
public interface OpenCustomerService {
	/**
	 * 创建商户
	 * 
	 * @param createParam
	 * @return
	 * @throws Exception
	 */
	public String createCustomer(OpenCustomerCreateParam createParam) throws Exception;

	/**
	 * 修改商户
	 * 
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateCustomer(OpenCustomerUpdateParam updateParam) throws Exception;

	/**
	 * 查询商户信息
	 * 
	 * @param customer_id
	 * @param customer_name
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<OpenCustomerBean> searchCustomer(String customer_id, String customer_name, int offset, int limit) throws Exception;

	/**
	 * 查询商户是否可以正常下单
	 * 
	 * @param customer_id
	 * @return
	 * @throws Exception
	 */
	public boolean checkCustomerOrderStatus(String customer_id) throws Exception;

	public List<OpenCustomerAreaBean> getAreaList() throws Exception;

}
