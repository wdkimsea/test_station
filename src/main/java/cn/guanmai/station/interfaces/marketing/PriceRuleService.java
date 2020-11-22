package cn.guanmai.station.interfaces.marketing;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.marketing.PriceRuleBean;
import cn.guanmai.station.bean.marketing.PriceRuleSkuBean;
import cn.guanmai.station.bean.marketing.param.PriceRuleCreateParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleEditParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleFilterParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleSkuFilterParam;

/* 
* @author liming 
* @date Feb 19, 2019 5:41:09 PM 
* @des 限时锁价相关接口
* @version 1.0 
*/
public interface PriceRuleService {
	/**
	 * 搜索过滤限时锁价
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PriceRuleBean> searchPriceRule(PriceRuleFilterParam filterParam) throws Exception;

	/**
	 * 获取指定限时锁价的详细信息
	 * 
	 * @param price_rule_id
	 * @return
	 */
	public PriceRuleBean getPriceRuleDetailInfo(String price_rule_id) throws Exception;

	/**
	 * 新建锁价规则,搜索商户
	 * 
	 * @param s
	 * @param salemenu_id
	 * @return
	 * @throws Exception
	 */
	public List<String> searchCustomer(String s, String salemenu_id) throws Exception;

	/**
	 * 新建锁价规则,搜索商品
	 * 
	 * @param salemenu_id
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<String> searchSku(String salemenu_id, String search_text) throws Exception;

	/**
	 * 新建锁价规则
	 * 
	 * @param createParam
	 * @return
	 * @throws Exception
	 */
	public String createPriceRule(PriceRuleCreateParam createParam) throws Exception;

	/**
	 * 修改锁价规则
	 * 
	 * @param editParam
	 * @return
	 * @throws Exception
	 */
	public JSONObject editPriceRule(PriceRuleEditParam editParam) throws Exception;

	/**
	 * 锁价规则,按商户商品查看
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PriceRuleSkuBean> searchPriceRuleSku(PriceRuleSkuFilterParam filterParam) throws Exception;

}
