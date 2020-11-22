package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author liming
 * @date 2019年7月29日 下午4:15:54
 * @des 接口 /stock/in_stock_summary_by_category/list 对应的结果
 * @version 1.0
 */
public class StockSummaryCategoryDetailBean {
	private String supplier_name; // 入库对应的结果
	private String supplier_num; // 入库对应的结果
	private BigDecimal total_value;

	@JSONField(name="restaurant_id")
	private String address_id; // 出库对应的结果
	
	@JSONField(name="restaurant_name")
	private String address_name; // 出库对应的结果

	private List<Category1Detail> category1_details;

	public class Category1Detail {
		private String category1_name;
		private BigDecimal value;

		/**
		 * @return the category1_name
		 */
		public String getCategory1_name() {
			return category1_name;
		}

		/**
		 * @param category1_name the category1_name to set
		 */
		public void setCategory1_name(String category1_name) {
			this.category1_name = category1_name;
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

	/**
	 * 入库对应的结果,供应商名称
	 * 
	 * @return the supplier_name
	 */
	public String getSupplier_name() {
		return supplier_name;
	}

	/**
	 * 入库对应的结果,供应商名称
	 * 
	 * @param supplier_name the supplier_name to set
	 */
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}

	/**
	 * 入库对应的结果,供应商自定义ID
	 * 
	 * @return the supplier_num
	 */
	public String getSupplier_num() {
		return supplier_num;
	}

	/**
	 * 入库对应的结果,供应商自定义ID
	 * 
	 * @param supplier_num the supplier_num to set
	 */
	public void setSupplier_num(String supplier_num) {
		this.supplier_num = supplier_num;
	}

	/**
	 * @return the total_value
	 */
	public BigDecimal getTotal_value() {
		return total_value;
	}

	/**
	 * @param total_value the total_value to set
	 */
	public void setTotal_value(BigDecimal total_value) {
		this.total_value = total_value;
	}

	/**
	 * @return the category1_details
	 */
	public List<Category1Detail> getCategory1_details() {
		return category1_details;
	}

	/**
	 * @param category1_details the category1_details to set
	 */
	public void setCategory1_details(List<Category1Detail> category1_details) {
		this.category1_details = category1_details;
	}

	/**
	 * 出库记录统计
	 * 
	 * @return
	 */
	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * 出库记录统计
	 * 
	 * @return
	 */
	public String getAddress_name() {
		return address_name;
	}

	public void setAddress_name(String address_name) {
		this.address_name = address_name;
	}

}
