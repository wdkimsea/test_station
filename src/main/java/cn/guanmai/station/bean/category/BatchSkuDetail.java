package cn.guanmai.station.bean.category;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date May 28, 2019 6:00:47 PM 
* @des 接口 /product/batchsku/details 对应的接口
*      批量SPU详细信息
* @version 1.0 
*/
public class BatchSkuDetail {
	private String category_1_name;
	private String category_2_name;
	private int is_weigh;
	private String pinlei_name;
	private String pur_spec_id;
	private List<PurSpecs> pur_specs;
	private BigDecimal ratio;
	private BigDecimal sale_num_least;
	private BigDecimal sale_price;
	private String sale_unit_name;
	private String sku_name;
	private String spu_id;
	private String spu_name;
	private int state;
	private String std_unit_name;
	private BigDecimal stock;
	private int stock_type;
	private String supplier_id;
	private List<Supplier> suppliers;

	public class PurSpecs {
		private String name;
		private String pur_spec_id;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the pur_spec_id
		 */
		public String getPur_spec_id() {
			return pur_spec_id;
		}

		/**
		 * @param pur_spec_id
		 *            the pur_spec_id to set
		 */
		public void setPur_spec_id(String pur_spec_id) {
			this.pur_spec_id = pur_spec_id;
		}

	}

	public class Supplier {
		private String name;
		private String supplier_id;

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the supplier_id
		 */
		public String getSupplier_id() {
			return supplier_id;
		}

		/**
		 * @param supplier_id
		 *            the supplier_id to set
		 */
		public void setSupplier_id(String supplier_id) {
			this.supplier_id = supplier_id;
		}
	}

	/**
	 * @return the category_1_name
	 */
	public String getCategory_1_name() {
		return category_1_name;
	}

	/**
	 * @param category_1_name
	 *            the category_1_name to set
	 */
	public void setCategory_1_name(String category_1_name) {
		this.category_1_name = category_1_name;
	}

	/**
	 * @return the category_2_name
	 */
	public String getCategory_2_name() {
		return category_2_name;
	}

	/**
	 * @param category_2_name
	 *            the category_2_name to set
	 */
	public void setCategory_2_name(String category_2_name) {
		this.category_2_name = category_2_name;
	}

	/**
	 * @return the is_weigh
	 */
	public int getIs_weigh() {
		return is_weigh;
	}

	/**
	 * @param is_weigh
	 *            the is_weigh to set
	 */
	public void setIs_weigh(int is_weigh) {
		this.is_weigh = is_weigh;
	}

	/**
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name
	 *            the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	/**
	 * @return the pur_spec_id
	 */
	public String getPur_spec_id() {
		return pur_spec_id;
	}

	/**
	 * @param pur_spec_id
	 *            the pur_spec_id to set
	 */
	public void setPur_spec_id(String pur_spec_id) {
		this.pur_spec_id = pur_spec_id;
	}

	/**
	 * @return the pur_specs
	 */
	public List<PurSpecs> getPur_specs() {
		return pur_specs;
	}

	/**
	 * @param pur_specs
	 *            the pur_specs to set
	 */
	public void setPur_specs(List<PurSpecs> pur_specs) {
		this.pur_specs = pur_specs;
	}

	/**
	 * @return the ratio
	 */
	public BigDecimal getRatio() {
		return ratio;
	}

	/**
	 * @param ratio
	 *            the ratio to set
	 */
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	/**
	 * @return the sale_num_least
	 */
	public BigDecimal getSale_num_least() {
		return sale_num_least;
	}

	/**
	 * @param sale_num_least
	 *            the sale_num_least to set
	 */
	public void setSale_num_least(BigDecimal sale_num_least) {
		this.sale_num_least = sale_num_least;
	}

	/**
	 * @return the sale_price
	 */
	public BigDecimal getSale_price() {
		return sale_price;
	}

	/**
	 * @param sale_price
	 *            the sale_price to set
	 */
	public void setSale_price(BigDecimal sale_price) {
		this.sale_price = sale_price;
	}

	/**
	 * @return the sale_unit_name
	 */
	public String getSale_unit_name() {
		return sale_unit_name;
	}

	/**
	 * @param sale_unit_name
	 *            the sale_unit_name to set
	 */
	public void setSale_unit_name(String sale_unit_name) {
		this.sale_unit_name = sale_unit_name;
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
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
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
	 * @param spu_name
	 *            the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
	}

	/**
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(int state) {
		this.state = state;
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
	 * @return the stock
	 */
	public BigDecimal getStock() {
		return stock;
	}

	/**
	 * @param stock
	 *            the stock to set
	 */
	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	/**
	 * @return the stock_type
	 */
	public int getStock_type() {
		return stock_type;
	}

	/**
	 * @param stock_type
	 *            the stock_type to set
	 */
	public void setStock_type(int stock_type) {
		this.stock_type = stock_type;
	}

	/**
	 * @return the supplier_id
	 */
	public String getSupplier_id() {
		return supplier_id;
	}

	/**
	 * @param supplier_id
	 *            the supplier_id to set
	 */
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}

	/**
	 * @return the suppliers
	 */
	public List<Supplier> getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            the suppliers to set
	 */
	public void setSuppliers(List<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

}
