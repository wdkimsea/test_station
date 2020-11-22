package cn.guanmai.manage.interfaces.custommange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.guanmai.manage.bean.custommange.param.MgCustomerAddParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerEditParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerImportAddParam;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerEmployeeInfoBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;

/* 
* @author liming 
* @date Jan 14, 2019 6:01:50 PM 
* @des 商户管理相关接口
* @version 1.0 
*/
public interface MgCustomerService {

	/**
	 * 用户相关基础信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public CustomerBaseInfoBean getCustomerBaseInfo() throws Exception;

	/**
	 * 获取商户列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<MgCustomerBean> searchCustomer(MgCustomerFilterParam customerFilterParam) throws Exception;

	/**
	 * 获取商户详细信息
	 * 
	 * @param sid
	 * @return
	 * @throws Exception
	 */
	public MgCustomerDetailBean getCustomerDetailInfoBySID(String sid) throws Exception;

	/**
	 * 商户批量修改,商户信息模板导出
	 * 
	 * @param sids
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportEditCustomerTemplate(List<String> sids) throws Exception;

	/**
	 * 商户批量修改,商户信息模板导出
	 * 
	 * @param customerFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportEditCustomerTemplate(MgCustomerFilterParam customerFilterParam) throws Exception;

	/**
	 * 商户批量修改,商户信息模板导入
	 * 
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public BigDecimal importEditCustomer(String file_path) throws Exception;

	/**
	 * 商户批量导入添加
	 * 
	 * @param customerImportAddParamList
	 * @return
	 * @throws Exception
	 */
	public boolean importAddCustomer(List<MgCustomerImportAddParam> customerImportAddParamList) throws Exception;

	/**
	 * 新建商户信息
	 * 
	 * @param customerAddParam
	 * @return
	 * @throws Exception
	 */
	public String createCustomer(MgCustomerAddParam customerAddParam) throws Exception;

	/**
	 * 删除商户信息
	 * 
	 * @param sid
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCustomter(String sid) throws Exception;

	/**
	 * 获取Group员工信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public CustomerEmployeeInfoBean getCustomerEmployeeInfo() throws Exception;

	/**
	 * 商户标签
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<BigDecimal, String> getCustomerLabel() throws Exception;

	/**
	 * 导出商户
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal exportCustomer(MgCustomerFilterParam filterParam) throws Exception;

	/**
	 * 修改商户信息
	 * 
	 * @param customerEditParam
	 * @return
	 * @throws Exception
	 */
	public boolean editCustomer(MgCustomerEditParam customerEditParam) throws Exception;

	/**
	 * 商户添加报价单绑定
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean addSalemenu(String station_id, String sid, String salemenu_id) throws Exception;

	/**
	 * 商户移除报价单绑定
	 * 
	 * @param sid
	 * @param record_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteSalemenu(String sid, String record_id) throws Exception;

}
