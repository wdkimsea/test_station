package cn.guanmai.station.interfaces.marketing;

import java.util.List;

import cn.guanmai.station.bean.marketing.SmartPricingSkuBean;
import cn.guanmai.station.bean.marketing.param.SmartFormulaPricingParam;
import cn.guanmai.station.bean.marketing.param.SmartPricingSkuFilterParam;
import cn.guanmai.station.bean.marketing.param.SmartPricingUpdateParam;

/* 
* @author liming 
* @date May 29, 2019 5:40:06 PM 
* @des 商品定价相关接口
* @version 1.0 
*/
public interface PricingService {
	/**
	 * 智能定价选取销售SKU展示的定价列表
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<SmartPricingSkuBean> getSmartPricingSkuList(SmartPricingSkuFilterParam filterParam) throws Exception;

	/**
	 * 更新智能定价信息
	 * 
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public String updateSmartPricing(SmartPricingUpdateParam updateParam) throws Exception;

	/**
	 * 修改定价公式
	 * @param smartFormulaPricingParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateSmartFormulaPricing(SmartFormulaPricingParam smartFormulaPricingParam) throws Exception;
}
