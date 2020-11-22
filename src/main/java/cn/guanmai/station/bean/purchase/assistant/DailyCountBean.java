package cn.guanmai.station.bean.purchase.assistant;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date May 23, 2019 8:18:42 PM 
* @des 采购助手APP采购金额趋势统计
* @version 1.0 
*/
public class DailyCountBean {
	private BigDecimal all_purchase_price;
	private BigDecimal all_purchase_sheet_num;

	@JSONField(name = "daily_price")
	private List<DailyPrice> daily_prices;

	public class DailyPrice {
		private String date;
		private BigDecimal purchase_price;

		/**
		 * @return the date
		 */
		public String getDate() {
			return date;
		}

		/**
		 * @param date the date to set
		 */
		public void setDate(String date) {
			this.date = date;
		}

		/**
		 * @return the purchase_price
		 */
		public BigDecimal getPurchase_price() {
			return purchase_price;
		}

		/**
		 * @param purchase_price the purchase_price to set
		 */
		public void setPurchase_price(BigDecimal purchase_price) {
			this.purchase_price = purchase_price;
		}
	}

	/**
	 * @return the all_purchase_price
	 */
	public BigDecimal getAll_purchase_price() {
		return all_purchase_price;
	}

	/**
	 * @param all_purchase_price the all_purchase_price to set
	 */
	public void setAll_purchase_price(BigDecimal all_purchase_price) {
		this.all_purchase_price = all_purchase_price;
	}

	/**
	 * @return the all_purchase_sheet_num
	 */
	public BigDecimal getAll_purchase_sheet_num() {
		return all_purchase_sheet_num;
	}

	/**
	 * @param all_purchase_sheet_num the all_purchase_sheet_num to set
	 */
	public void setAll_purchase_sheet_num(BigDecimal all_purchase_sheet_num) {
		this.all_purchase_sheet_num = all_purchase_sheet_num;
	}

	/**
	 * @return the daily_prices
	 */
	public List<DailyPrice> getDaily_prices() {
		return daily_prices;
	}

	/**
	 * @param daily_prices the daily_prices to set
	 */
	public void setDaily_prices(List<DailyPrice> daily_prices) {
		this.daily_prices = daily_prices;
	}

}
