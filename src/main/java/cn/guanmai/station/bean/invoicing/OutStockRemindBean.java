package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Feb 26, 2019 5:58:13 PM 
* @des 出库库存提醒结果封装类
* @version 1.0 
*/
public class OutStockRemindBean {
	private String spu_id;
	private String spu_name;
	private String out_sheet_id;
	private BigDecimal remain;
	private BigDecimal quantity;

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

	public String getOut_sheet_id() {
		return out_sheet_id;
	}

	public void setOut_sheet_id(String out_sheet_id) {
		this.out_sheet_id = out_sheet_id;
	}

	/**
	 * @return the remain
	 */
	public BigDecimal getRemain() {
		return remain;
	}

	/**
	 * @param remain the remain to set
	 */
	public void setRemain(BigDecimal remain) {
		this.remain = remain;
	}

	/**
	 * @return the quantity
	 */
	public BigDecimal getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

}
