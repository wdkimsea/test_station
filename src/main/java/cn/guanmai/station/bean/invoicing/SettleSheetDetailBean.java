package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Mar 28, 2019 5:23:07 PM 
* @des 结款单据 /stock/settle_sheet 对应的结果
* @version 1.0 
*/
public class SettleSheetDetailBean {
	private String id;
	private String date_time;
	private BigDecimal delta_money;
	private String settle_supplier_id;
	private String settle_supplier_name;
	private int status;
	private Integer supplier_status;
	private List<String> sheet_nos;
	private BigDecimal total_price;

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

	/**
	 * @return the date_time
	 */
	public String getDate_time() {
		return date_time;
	}

	/**
	 * @param date_time the date_time to set
	 */
	public void setDate_time(String date_time) {
		this.date_time = date_time;
	}

	/**
	 * @return the delta_money
	 */
	public BigDecimal getDelta_money() {
		return delta_money;
	}

	/**
	 * @param delta_money the delta_money to set
	 */
	public void setDelta_money(BigDecimal delta_money) {
		this.delta_money = delta_money;
	}

	/**
	 * @return the settle_supplier_id
	 */
	public String getSettle_supplier_id() {
		return settle_supplier_id;
	}

	/**
	 * @param settle_supplier_id the settle_supplier_id to set
	 */
	public void setSettle_supplier_id(String settle_supplier_id) {
		this.settle_supplier_id = settle_supplier_id;
	}

	/**
	 * @return the settle_supplier_name
	 */
	public String getSettle_supplier_name() {
		return settle_supplier_name;
	}

	/**
	 * @param settle_supplier_name the settle_supplier_name to set
	 */
	public void setSettle_supplier_name(String settle_supplier_name) {
		this.settle_supplier_name = settle_supplier_name;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public List<String> getSheet_nos() {
		return sheet_nos;
	}

	public Integer getSupplier_status() {
		return supplier_status;
	}

	public void setSupplier_status(Integer supplier_status) {
		this.supplier_status = supplier_status;
	}

	public void setSheet_nos(List<String> sheet_nos) {
		this.sheet_nos = sheet_nos;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price.divide(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

}
