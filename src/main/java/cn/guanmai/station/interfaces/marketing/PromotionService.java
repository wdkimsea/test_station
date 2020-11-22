package cn.guanmai.station.interfaces.marketing;

import java.util.List;

import cn.guanmai.station.bean.category.SkuPromotionBean;
import cn.guanmai.station.bean.marketing.PromotionBean;
import cn.guanmai.station.bean.marketing.PromotionDetailBean;
import cn.guanmai.station.bean.marketing.PromotionResultBean;
import cn.guanmai.station.bean.marketing.param.PromotionDefaultParam;
import cn.guanmai.station.bean.marketing.param.PromotionFilterParam;
import cn.guanmai.station.bean.marketing.param.PromotionLimitParam;

/* 
* @author liming 
* @date Feb 21, 2019 11:05:24 AM 
* @des 营销活动相关接口
* @version 1.0 
*/
public interface PromotionService {
	/**
	 * 搜索过滤营销活动
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PromotionBean> searchPromotion(PromotionFilterParam filterParam) throws Exception;

	/**
	 * 获取指定ID的营销活动详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PromotionDetailBean getPromotionDetailById(String id) throws Exception;

	/**
	 * 删除指定的营销活动
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePromotion(String id) throws Exception;

	/**
	 * 营销活动,拉取所有销售SKU接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SkuPromotionBean> promotionSkus() throws Exception;

	/**
	 * 创建默认的营销活动
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String createPromotionDefault(PromotionDefaultParam param) throws Exception;

	/**
	 * 创建限购的营销活动
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public PromotionResultBean createPromotionLimit(PromotionLimitParam param) throws Exception;

	/**
	 * 修改默认的营销活动
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean updatePromotionDefault(PromotionDefaultParam param) throws Exception;

	/**
	 * 修改默认的营销活动
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public PromotionResultBean updatePromotionLimit(PromotionLimitParam param) throws Exception;

}
