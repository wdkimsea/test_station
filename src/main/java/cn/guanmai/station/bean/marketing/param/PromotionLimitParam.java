package cn.guanmai.station.bean.marketing.param;

import java.util.List;

import cn.guanmai.station.bean.marketing.PromotionSkuBean;


/* 
* @author liming 
* @date Feb 21, 2019 3:18:10 PM 
* @des 创建限购营销活动的参数类
* @version 1.0 
*/
public class PromotionLimitParam extends PromotionBaseParam {
	private String id;
	private List<PromotionSkuBean> skus;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 修改营销活动必填的参数
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the skus
	 */
	public List<PromotionSkuBean> getSkus() {
		return skus;
	}

	/**
	 * @param skus
	 *            the skus to set
	 */
	public void setSkus(List<PromotionSkuBean> skus) {
		this.skus = skus;
	}

}
