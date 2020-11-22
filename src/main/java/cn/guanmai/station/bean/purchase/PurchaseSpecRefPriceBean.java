package cn.guanmai.station.bean.purchase;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年3月13日 上午10:12:31
 * @description:
 * @version: 1.0
 */

public class PurchaseSpecRefPriceBean {
	private BigDecimal latest_in_stock_price;
	private BigDecimal stock_avg_price;
	private BigDecimal max_stock_unit_price;
	private BigDecimal latest_purchase_price;
	private BigDecimal latest_quote_price;

	private LastInStockPrice last_in_stock_price;

	public class LastInStockPrice {
		private Newest newest;
		
		@JSONField(name="earlier")
		private List<Earlier> earliers;

		public Newest getNewest() {
			return newest;
		}

		public void setNewest(Newest newest) {
			this.newest = newest;
		}

		public List<Earlier> getEarliers() {
			return earliers;
		}

		public void setEarliers(List<Earlier> earliers) {
			this.earliers = earliers;
		}

	}

	private LastPurchasePrice last_purchase_price;

	public class LastPurchasePrice {
		private Newest newest;
		
		@JSONField(name="earlier")
		private List<Earlier> earliers;

		public Newest getNewest() {
			return newest;
		}

		public void setNewest(Newest newest) {
			this.newest = newest;
		}

		public List<Earlier> getEarliers() {
			return earliers;
		}

		public void setEarliers(List<Earlier> earliers) {
			this.earliers = earliers;
		}
	}

	private LastQuotePrice last_quote_price;

	public class LastQuotePrice {
		private Newest newest;
		@JSONField(name="earlier")
		private List<Earlier> earliers;

		public Newest getNewest() {
			return newest;
		}

		public void setNewest(Newest newest) {
			this.newest = newest;
		}

		public List<Earlier> getEarliers() {
			return earliers;
		}

		public void setEarliers(List<Earlier> earliers) {
			this.earliers = earliers;
		}

	}

	public BigDecimal getLatest_in_stock_price() {
		return latest_in_stock_price;
	}

	public void setLatest_in_stock_price(BigDecimal latest_in_stock_price) {
		this.latest_in_stock_price = latest_in_stock_price;
	}

	public BigDecimal getStock_avg_price() {
		return stock_avg_price;
	}

	public void setStock_avg_price(BigDecimal stock_avg_price) {
		this.stock_avg_price = stock_avg_price;
	}

	public BigDecimal getMax_stock_unit_price() {
		return max_stock_unit_price;
	}

	public void setMax_stock_unit_price(BigDecimal max_stock_unit_price) {
		this.max_stock_unit_price = max_stock_unit_price;
	}

	public BigDecimal getLatest_purchase_price() {
		return latest_purchase_price;
	}

	public void setLatest_purchase_price(BigDecimal latest_purchase_price) {
		this.latest_purchase_price = latest_purchase_price;
	}

	public BigDecimal getLatest_quote_price() {
		return latest_quote_price;
	}

	public void setLatest_quote_price(BigDecimal latest_quote_price) {
		this.latest_quote_price = latest_quote_price;
	}

	public LastInStockPrice getLast_in_stock_price() {
		return last_in_stock_price;
	}

	public void setLast_in_stock_price(LastInStockPrice last_in_stock_price) {
		this.last_in_stock_price = last_in_stock_price;
	}

	public LastPurchasePrice getLast_purchase_price() {
		return last_purchase_price;
	}

	public void setLast_purchase_price(LastPurchasePrice last_purchase_price) {
		this.last_purchase_price = last_purchase_price;
	}

	public LastQuotePrice getLast_quote_price() {
		return last_quote_price;
	}

	public void setLast_quote_price(LastQuotePrice last_quote_price) {
		this.last_quote_price = last_quote_price;
	}

}

class Newest {
	private BigDecimal price;
	private String purchase_supplier_name;
	private String purchase_supplier_id;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPurchase_supplier_name() {
		return purchase_supplier_name;
	}

	public void setPurchase_supplier_name(String purchase_supplier_name) {
		this.purchase_supplier_name = purchase_supplier_name;
	}

	public String getPurchase_supplier_id() {
		return purchase_supplier_id;
	}

	public void setPurchase_supplier_id(String purchase_supplier_id) {
		this.purchase_supplier_id = purchase_supplier_id;
	}
}

class Earlier {
	private BigDecimal price;
	private String purchase_supplier_name;
	private String purchase_supplier_id;

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPurchase_supplier_name() {
		return purchase_supplier_name;
	}

	public void setPurchase_supplier_name(String purchase_supplier_name) {
		this.purchase_supplier_name = purchase_supplier_name;
	}

	public String getPurchase_supplier_id() {
		return purchase_supplier_id;
	}

	public void setPurchase_supplier_id(String purchase_supplier_id) {
		this.purchase_supplier_id = purchase_supplier_id;
	}
}
