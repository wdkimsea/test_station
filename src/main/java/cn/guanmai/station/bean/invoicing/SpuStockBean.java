package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 28, 2019 10:24:31 AM 
* @des 库存判断
* @version 1.0 
*/
public class SpuStockBean {
	private String category_id_1;
	private String category_id_2;
	private String category_name_1;
	private String category_name_2;
	private String spu_id;
	private String name;
	private String std_unit_name;
	private BigDecimal remain;
	private BigDecimal avg_price;
	private BigDecimal stock_value;
	private BigDecimal threshold;
	private BigDecimal upper_threshold;

	private Integer retention_warning_day;

	// 冻结库存
	private BigDecimal frozen;

	private Material material;

	public class Material {
		// 库存
		private BigDecimal amount;

		// 库存均价(单位:分)
		private BigDecimal avg_price;

		/**
		 * @return the amount
		 */
		public BigDecimal getAmount() {
			return amount;
		}

		/**
		 * @param amount the amount to set
		 */
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		/**
		 * @return the avg_price
		 */
		public BigDecimal getAvg_price() {
			return avg_price;
		}

		/**
		 * @param avg_price the avg_price to set
		 */
		public void setAvg_price(BigDecimal avg_price) {
			this.avg_price = avg_price;
		}

	}

	/**
	 * @return the category_id_1
	 */
	public String getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the category_name_1
	 */
	public String getCategory_name_1() {
		return category_name_1;
	}

	/**
	 * @param category_name_1 the category_name_1 to set
	 */
	public void setCategory_name_1(String category_name_1) {
		this.category_name_1 = category_name_1;
	}

	/**
	 * @return the category_name_2
	 */
	public String getCategory_name_2() {
		return category_name_2;
	}

	/**
	 * @param category_name_2 the category_name_2 to set
	 */
	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
	}

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the frozen
	 */
	public BigDecimal getFrozen() {
		return frozen;
	}

	/**
	 * @param frozen the frozen to set
	 */
	public void setFrozen(BigDecimal frozen) {
		this.frozen = frozen;
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * @return the remain
	 */
	public BigDecimal getRemain() {
		return remain.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param remain the remain to set
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	/**
	 * @return the avg_price
	 */
	public BigDecimal getAvg_price() {
		return avg_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param avg_price the avg_price to set
	 */
	public void setAvg_price(BigDecimal avg_price) {
		this.avg_price = avg_price;
	}

	/**
	 * @return the stock_value
	 */
	public BigDecimal getStock_value() {
		return stock_value;
	}

	/**
	 * @param stock_value the stock_value to set
	 */
	public void setStock_value(BigDecimal stock_value) {
		this.stock_value = stock_value;
	}

	public BigDecimal getThreshold() {
		return threshold;
	}

	public void setThreshold(BigDecimal threshold) {
		this.threshold = threshold;
	}

	public BigDecimal getUpper_threshold() {
		return upper_threshold;
	}

	public void setUpper_threshold(BigDecimal upper_threshold) {
		this.upper_threshold = upper_threshold;
	}

	public Integer getRetention_warning_day() {
		return retention_warning_day;
	}

	public void setRetention_warning_day(Integer retention_warning_day) {
		this.retention_warning_day = retention_warning_day;
	}

}
