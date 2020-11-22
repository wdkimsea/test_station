package cn.guanmai.bshop.bean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 22, 2019 11:15:41 AM 
* @des 更新购物车结果
* @version 1.0 
*/
public class CartUpdateResult {
	private Map<String, BigDecimal> list;
	private Info info;
	private List<InvalidSku> invalid_skus;

	public class Info {
		private BigDecimal count;
		private boolean is_price_timing;
		private BigDecimal sum_money;

		/**
		 * @return the count
		 */
		public BigDecimal getCount() {
			return count;
		}

		/**
		 * @param count
		 *            the count to set
		 */
		public void setCount(BigDecimal count) {
			this.count = count;
		}

		/**
		 * @return the is_price_timing
		 */
		public boolean isIs_price_timing() {
			return is_price_timing;
		}

		/**
		 * @param is_price_timing
		 *            the is_price_timing to set
		 */
		public void setIs_price_timing(boolean is_price_timing) {
			this.is_price_timing = is_price_timing;
		}

		/**
		 * @return the sum_money
		 */
		public BigDecimal getSum_money() {
			return sum_money;
		}

		/**
		 * @param sum_money
		 *            the sum_money to set
		 */
		public void setSum_money(BigDecimal sum_money) {
			this.sum_money = sum_money;
		}
	}

	public class InvalidSku {
		private int code;
		private String msg;
		private String sku_id;
		@JSONField(name="name")
		private String sku_name;
		private BigDecimal stocks;

		/**
		 * @return the code
		 */
		public int getCode() {
			return code;
		}

		/**
		 * @param code
		 *            the code to set
		 */
		public void setCode(int code) {
			this.code = code;
		}

		/**
		 * @return the msg
		 */
		public String getMsg() {
			return msg;
		}

		/**
		 * @param msg
		 *            the msg to set
		 */
		public void setMsg(String msg) {
			this.msg = msg;
		}

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
		 * @return the stocks
		 */
		public BigDecimal getStocks() {
			return stocks;
		}

		/**
		 * @param stocks
		 *            the stocks to set
		 */
		public void setStocks(BigDecimal stocks) {
			this.stocks = stocks;
		}
	}

	/**
	 * @return the list
	 */
	public Map<String, BigDecimal> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(Map<String, BigDecimal> list) {
		this.list = list;
	}

	/**
	 * @return the info
	 */
	public Info getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(Info info) {
		this.info = info;
	}

	/**
	 * @return the invalid_skus
	 */
	public List<InvalidSku> getInvalid_skus() {
		return invalid_skus;
	}

	/**
	 * @param invalid_skus
	 *            the invalid_skus to set
	 */
	public void setInvalid_skus(List<InvalidSku> invalid_skus) {
		this.invalid_skus = invalid_skus;
	}

}
