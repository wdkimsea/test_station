package cn.guanmai.bshop.bean.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;


/* 
* @author liming 
* @date Apr 22, 2019 9:56:06 AM 
* @des 销售商品类
* @version 1.0 
*/
public class BsProductBean {
	private String combine_good_id;
	private String id;
	private String name;

	private boolean is_combine_goods;
	private boolean is_price_timing;

	private String sale_unit_name;

	private List<Sku> skus;
	private Map<String, BigDecimal> skus_ratio;

	public class Sku {
		private String category_id_1;
		private String category_id_2;
		private String spu_id;
		@JSONField(name="id")
		private String sku_id;
		private boolean is_price_timing;
		@JSONField(name="name")
		private String sku_name;
		private BigDecimal sale_num_least;
		private BigDecimal sale_price;
		private BigDecimal sale_ratio;
		private String sale_unit_name;
		private String salemenu_id;
		private BigDecimal std_sale_price;
		private String std_unit_name;
		private BigDecimal stocks;

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

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		/**
		 * @return the sku_id
		 */
		public String getSku_id() {
			return sku_id;
		}

		/**
		 * @param sku_id the sku_id to set
		 */
		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		/**
		 * @return the is_price_timing
		 */
		public boolean isIs_price_timing() {
			return is_price_timing;
		}

		/**
		 * @param is_price_timing the is_price_timing to set
		 */
		public void setIs_price_timing(boolean is_price_timing) {
			this.is_price_timing = is_price_timing;
		}

		/**
		 * @return the sku_name
		 */
		public String getSku_name() {
			return sku_name;
		}

		/**
		 * @param sku_name the sku_name to set
		 */
		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
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

		/**
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		/**
		 * @return the sale_ratio
		 */
		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		/**
		 * @param sale_ratio the sale_ratio to set
		 */
		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		/**
		 * @return the sale_unit_name
		 */
		public String getSale_unit_name() {
			return sale_unit_name;
		}

		/**
		 * @param sale_unit_name the sale_unit_name to set
		 */
		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		/**
		 * @return the salemenu_id
		 */
		public String getSalemenu_id() {
			return salemenu_id;
		}

		/**
		 * @param salemenu_id the salemenu_id to set
		 */
		public void setSalemenu_id(String salemenu_id) {
			this.salemenu_id = salemenu_id;
		}

		/**
		 * @return the std_sale_price
		 */
		public BigDecimal getStd_sale_price() {
			return std_sale_price.divide(new BigDecimal("100"));
		}

		/**
		 * @param std_sale_price the std_sale_price to set
		 */
		public void setStd_sale_price(BigDecimal std_sale_price) {
			this.std_sale_price = std_sale_price;
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
		 * @return the stocks
		 */
		public BigDecimal getStocks() {
			return stocks;
		}

		/**
		 * @param stocks the stocks to set
		 */
		public void setStocks(BigDecimal stocks) {
			this.stocks = stocks;
		}
	}

	public String getCombine_good_id() {
		return combine_good_id;
	}

	public void setCombine_good_id(String combine_good_id) {
		this.combine_good_id = combine_good_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIs_combine_goods() {
		return is_combine_goods;
	}

	public void setIs_combine_goods(boolean is_combine_goods) {
		this.is_combine_goods = is_combine_goods;
	}

	public boolean isIs_price_timing() {
		return is_price_timing;
	}

	public void setIs_price_timing(boolean is_price_timing) {
		this.is_price_timing = is_price_timing;
	}

	public String getSale_unit_name() {
		return sale_unit_name;
	}

	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
	}

	public List<Sku> getSkus() {
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public Map<String, BigDecimal> getSkus_ratio() {
		return skus_ratio;
	}

	public void setSkus_ratio(Map<String, BigDecimal> skus_ratio) {
		this.skus_ratio = skus_ratio;
	}

}
