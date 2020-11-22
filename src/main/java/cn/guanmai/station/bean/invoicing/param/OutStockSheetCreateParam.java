package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年7月17日 上午10:53:15
 * @description:
 * @version: 1.0
 */

public class OutStockSheetCreateParam {
	private String id;
	private String out_stock_customer_id;
	private String out_stock_target;
	private String out_stock_remark;
	private String out_stock_time;

	private int is_submit;

	private List<Detail> details;

	public class Detail {
		private String id;
		private String name;
		private String spu_id;
		private String std_unit_name;
		private String category;
		private String sale_unit_name;
		private String out_of_stock;

		private BigDecimal quantity;
		private BigDecimal real_std_count;
		private BigDecimal sale_ratio;

		private boolean clean_food;

		private List<BatchDetail> batch_details;

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

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getSale_unit_name() {
			return sale_unit_name;
		}

		public void setSale_unit_name(String sale_unit_name) {
			this.sale_unit_name = sale_unit_name;
		}

		public String getOut_of_stock() {
			return out_of_stock;
		}

		public void setOut_of_stock(String out_of_stock) {
			this.out_of_stock = out_of_stock;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getReal_std_count() {
			return real_std_count;
		}

		public void setReal_std_count(BigDecimal real_std_count) {
			this.real_std_count = real_std_count;
		}

		public BigDecimal getSale_ratio() {
			return sale_ratio;
		}

		public void setSale_ratio(BigDecimal sale_ratio) {
			this.sale_ratio = sale_ratio;
		}

		public boolean isClean_food() {
			return clean_food;
		}

		public void setClean_food(boolean clean_food) {
			this.clean_food = clean_food;
		}

		public List<BatchDetail> getBatch_details() {
			return batch_details;
		}

		public void setBatch_details(List<BatchDetail> batch_details) {
			this.batch_details = batch_details;
		}

		public class BatchDetail {
			private String batch_number;
			private BigDecimal out_stock_base;

			// 提交入库单,非必填参数
			private BigDecimal price;

			/**
			 * @return the batch_number
			 */
			public String getBatch_number() {
				return batch_number;
			}

			/**
			 * @param batch_number the batch_number to set
			 */
			public void setBatch_number(String batch_number) {
				this.batch_number = batch_number;
			}

			/**
			 * @return the out_stock_base
			 */
			public BigDecimal getOut_stock_base() {
				return out_stock_base;
			}

			/**
			 * @param out_stock_base the out_stock_base to set
			 */
			public void setOut_stock_base(BigDecimal out_stock_base) {
				this.out_stock_base = out_stock_base;
			}

			/**
			 * @return the price
			 */
			public BigDecimal getPrice() {
				return price;
			}

			/**
			 * 提交入库单,非必填参数
			 * 
			 * @param price the price to set
			 */
			public void setPrice(BigDecimal price) {
				this.price = price;
			}

			public BatchDetail() {
				super();
			}

		}

		public Detail() {
			super();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOut_stock_customer_id() {
		return out_stock_customer_id;
	}

	public void setOut_stock_customer_id(String out_stock_customer_id) {
		this.out_stock_customer_id = out_stock_customer_id;
	}

	public String getOut_stock_target() {
		return out_stock_target;
	}

	public void setOut_stock_target(String out_stock_target) {
		this.out_stock_target = out_stock_target;
	}

	public String getOut_stock_remark() {
		return out_stock_remark;
	}

	public void setOut_stock_remark(String out_stock_remark) {
		this.out_stock_remark = out_stock_remark;
	}

	public String getOut_stock_time() {
		return out_stock_time;
	}

	public void setOut_stock_time(String out_stock_time) {
		this.out_stock_time = out_stock_time;
	}

	public int getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(int is_submit) {
		this.is_submit = is_submit;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public OutStockSheetCreateParam() {
		super();
	}

}
