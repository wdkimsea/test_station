package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年7月29日 下午4:29:21
 * @des 接口 /stock/out_stock_summary_by_spu/list 对应的结果
 * @version 1.0
 */
public class OutStockSummarySpuDetailBean {
	private BigDecimal amount;
	private BigDecimal avg_price;
	private String category2_name;

	@JSONField(name="restaurant_id")
	private String address_id;

	@JSONField(name="restaurant_name")
	private String address_name;

	private String spu_id;
	private String spu_name;
	private String std_unit_name;
	private BigDecimal value;

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
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

	/**
	 * @return the category2_name
	 */
	public String getCategory2_name() {
		return category2_name;
	}

	/**
	 * @param category2_name the category2_name to set
	 */
	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
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
	 * @return the spu_name
	 */
	public String getSpu_name() {
		return spu_name;
	}

	/**
	 * @param spu_name the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
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

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

}
