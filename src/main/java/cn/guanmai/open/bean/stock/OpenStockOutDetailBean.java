package cn.guanmai.open.bean.stock;

import java.util.List;

/**
 * @author liming
 * @date 2019年10月22日
 * @time 下午7:09:31
 * @des TODO
 */

public class OpenStockOutDetailBean {
	private String out_stock_sheet_id;
	private String create_date;
	private String creator;
	private String customer_id;
	private String customer_name;
	private String status;
	private String out_stock_sheet_type;

	public List<Detail> details;

	public class Detail {
		private String sku_id;
		private String sku_name;
		private String spu_id;
		private String category;
		private String out_stock_count;
		private boolean out_of_stock;
		private String std_unit_name;
		private String sale_unit_name;
		private String sale_ratio;

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getSku_name() {
			return sku_name;
		}

		public void setSku_name(String sku_name) {
			this.sku_name = sku_name;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getOut_stock_count() {
			return out_stock_count;
		}

		public void setOut_stock_count(String out_stock_count) {
			this.out_stock_count = out_stock_count;
		}

		public boolean isOut_of_stock() {
			return out_of_stock;
		}

		public void setOut_of_stock(boolean out_of_stock) {
			this.out_of_stock = out_of_stock;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		public String getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(String sale_ratio) {
			this.sale_ratio = sale_ratio;
		}
	}

	public String getOut_stock_sheet_id() {
		return out_stock_sheet_id;
	}

	public void setOut_stock_sheet_id(String out_stock_sheet_id) {
		this.out_stock_sheet_id = out_stock_sheet_id;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}

	public String getCustomer_name() {
		return customer_name;
	}

	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOut_stock_sheet_type() {
		return out_stock_sheet_type;
	}

	public void setOut_stock_sheet_type(String out_stock_sheet_type) {
		this.out_stock_sheet_type = out_stock_sheet_type;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
