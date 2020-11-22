package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年7月31日 上午10:58:08
 * @des 接口 /weight/package/sku/list 对应的结果,商品预分拣
 * @version 1.0
 */
public class PreSortingSkuBean {
	private String sku_id;
	private List<String> sku_ids;
	private String sku_name;
	private String std_unit_name;
	private String unit_name;
	private BigDecimal sale_ratio;
	private String sku_info;

	/**
	 * @return the sku_id
	 */
	public String getSku_id() {
		return sku_id;
	}

	/**
	 * @param sku_id
	 *            the sku_id to set
	 */
	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
	}

	/**
	 * @return the sku_ids
	 */
	public List<String> getSku_ids() {
		return sku_ids;
	}

	/**
	 * @param sku_ids
	 *            the sku_ids to set
	 */
	public void setSku_ids(List<String> sku_ids) {
		this.sku_ids = sku_ids;
	}

	/**
	 * @return the sku_name
	 */
	public String getSku_name() {
		return sku_name;
	}

	/**
	 * @param sku_name
	 *            the sku_name to set
	 */
	public void setSku_name(String sku_name) {
		this.sku_name = sku_name;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the unit_name
	 */
	public String getUnit_name() {
		return unit_name;
	}

	/**
	 * @param unit_name
	 *            the unit_name to set
	 */
	public void setUnit_name(String unit_name) {
		this.unit_name = unit_name;
	}

	/**
	 * @return the sale_ratio
	 */
	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	/**
	 * @param sale_ratio
	 *            the sale_ratio to set
	 */
	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	/**
	 * @return the sku_info
	 */
	public String getSku_info() {
		return sku_info;
	}

	/**
	 * @param sku_info
	 *            the sku_info to set
	 */
	public void setSku_info(String sku_info) {
		this.sku_info = sku_info;
	}

}
