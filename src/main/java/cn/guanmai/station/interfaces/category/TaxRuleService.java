package cn.guanmai.station.interfaces.category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.category.TaxRuleBean;

/* 
* @author liming 
* @date Feb 18, 2019 7:11:08 PM 
* @des 税率规则相关接口
* @version 1.0 
*/
public interface TaxRuleService {
	/**
	 * 获取税率规则列表
	 * 
	 * @param status
	 * @param search_text
	 * @return
	 */
	public List<TaxRuleBean> getTaxRuleList(Integer status, String search_text) throws Exception;

	/**
	 * 新建税率规则搜索用户
	 * 
	 * @param search_text
	 * @return
	 */
	public List<String> searchTaxCustomer(String search_text) throws Exception;

	/**
	 * 新建税率规则搜索商品
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> searchTaxSpu(String search_text) throws Exception;

	/**
	 * 新建税率规则
	 * 
	 * @param taxRule
	 * @return
	 * @throws Exception
	 */
	public boolean createTaxRule(TaxRuleBean taxRule) throws Exception;

	/**
	 * 获取税率规则详细信息
	 * 
	 * @param tax_id
	 * @return
	 * @throws Exception
	 */
	public TaxRuleBean getTaxRuleDetailInfo(String tax_id) throws Exception;

	/**
	 * 修改税率规则
	 * 
	 * @param taxRule
	 * @return
	 * @throws Exception
	 */
	public JSONObject editTaxRule(TaxRuleBean taxRule) throws Exception;

	/**
	 * 税率规则,按商户商品查看
	 * 
	 * @param search_text
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean taxSpuList(String search_text, Integer status) throws Exception;

	/**
	 * 税率中根据商户标签查询商户
	 * 
	 * @param label_id
	 * @return
	 * @throws Exception
	 */
	public Map<BigDecimal, String> searchAddressByLabel(String label_id) throws Exception;

}
