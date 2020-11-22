package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Mar 29, 2019 10:16:26 AM 
* @des 提交结款单据
* @version 1.0 
*/
public class SettleSheetDetailSubmitParam {
	private String id;
	private String settle_supplier_id;
	private String station_id;
	private String date_time;
	private List<String> sheet_nos;
	private int status;
	private BigDecimal total_price;
	private BigDecimal delta_money;
	private String settle_supplier_name;
	private String remark;

	private List<Discount> discount;

	public class Discount {
		private int action;
		private String remark;
		private int reason;
		private BigDecimal money;
		private String create_time;

		/**
		 * @return the action
		 */
		public int getAction() {
			return action;
		}

		/**
		 * @param action the action to set
		 */
		public void setAction(int action) {
			this.action = action;
		}

		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}

		/**
		 * @param remark the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}

		/**
		 * @return the reason
		 */
		public int getReason() {
			return reason;
		}

		/**
		 * @param reason the reason to set
		 */
		public void setReason(int reason) {
			this.reason = reason;
		}

		/**
		 * @return the money
		 */
		public BigDecimal getMoney() {
			return money;
		}

		/**
		 * @param money the money to set
		 */
		public void setMoney(BigDecimal money) {
			this.money = money;
		}

		/**
		 * @return the create_time
		 */
		public String getCreate_time() {
			return create_time;
		}

		/**
		 * @param create_time the create_time to set
		 */
		public void setCreate_time(String create_time) {
			this.create_time = create_time;
		}
	}

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
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
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

	public List<String> getSheet_nos() {
		return sheet_nos;
	}

	public void setSheet_nos(List<String> sheet_nos) {
		this.sheet_nos = sheet_nos;
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

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
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
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the discount
	 */
	public List<Discount> getDiscount() {
		return discount;
	}

	/**
	 * @param discount the discount to set
	 */
	public void setDiscount(List<Discount> discount) {
		this.discount = discount;
	}

}
