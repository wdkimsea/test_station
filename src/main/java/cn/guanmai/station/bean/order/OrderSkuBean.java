package cn.guanmai.station.bean.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.BooleanTypeAdapter;

/* 
* @author liming 
* @date Nov 12, 2018 11:43:19 AM 
* @des 下单的时候查询商品SKU
* @version 1.0 
*/
public class OrderSkuBean {
	private String id;

	private String name;

	private String salemenu_id;

	private String spu_id;

	private BigDecimal sale_price;

	private BigDecimal sale_num_least;

	private String std_unit_name_forsale;

	private Object is_price_timing;

	@JSONField(serializeUsing = BooleanTypeAdapter.class)
	private boolean is_combine_goods;

	private List<OrderSkuBean> skus;

	private Map<String, BigDecimal> skus_ratio;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalemenu_id() {
		return salemenu_id;
	}

	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	private String spu_remark;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the sale_num_least
	 */
	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * @param sale_num_least the sale_num_least to set
	 */
	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	public String getStd_unit_name_forsale() {
		return std_unit_name_forsale;
	}

	public void setStd_unit_name_forsale(String std_unit_name_forsale) {
		this.std_unit_name_forsale = std_unit_name_forsale;
	}

	/**
	 * @return the is_price_timing
	 */
	public int isIs_price_timing() {
		return (boolean) is_price_timing == true ? 1 : 0;
	}

	/**
	 * @param is_price_timing the is_price_timing to set
	 */
	public void setIs_price_timing(int is_price_timing) {
		this.is_price_timing = is_price_timing == 0 ? false : true;
	}

	/**
	 * @return the spu_remark
	 */
	public String getSpu_remark() {
		return spu_remark;
	}

	/**
	 * @param spu_remark the spu_remark to set
	 */
	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
	}

	public boolean isIs_combine_goods() {
		return is_combine_goods;
	}

	public void setIs_combine_goods(boolean is_combine_goods) {
		this.is_combine_goods = is_combine_goods;
	}

	public List<OrderSkuBean> getSkus() {
		return skus;
	}

	public void setSkus(List<OrderSkuBean> skus) {
		this.skus = skus;
	}

	public Map<String, BigDecimal> getSkus_ratio() {
		return skus_ratio;
	}

	public void setSkus_ratio(Map<String, BigDecimal> skus_ratio) {
		this.skus_ratio = skus_ratio;
	}

}
