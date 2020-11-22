package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Feb 26, 2019 4:34:35 PM 
* @des 出库单详情类
* @version 1.0 
*/
public class OutStockDetailBean {
	private String id;
	private String create_time;
	private String creator;
	private boolean is_bind_order;
	private String money;
	private String out_stock_customer_id;
	private String out_stock_target;
	private String out_stock_time;
	private int status;
	private String update_time;

	private List<Detail> details;

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

	/**
	 * @return the creator
	 */
	public String getCreator() {
		return creator;
	}

	/**
	 * @param creator the creator to set
	 */
	public void setCreator(String creator) {
		this.creator = creator;
	}

	/**
	 * @return the is_bind_order
	 */
	public boolean isIs_bind_order() {
		return is_bind_order;
	}

	/**
	 * @param is_bind_order the is_bind_order to set
	 */
	public void setIs_bind_order(boolean is_bind_order) {
		this.is_bind_order = is_bind_order;
	}

	/**
	 * @return the money
	 */
	public String getMoney() {
		return money;
	}

	/**
	 * @param money the money to set
	 */
	public void setMoney(String money) {
		this.money = money;
	}

	public String getOut_stock_customer_id() {
		return out_stock_customer_id;
	}

	public void setOut_stock_customer_id(String out_stock_customer_id) {
		this.out_stock_customer_id = out_stock_customer_id;
	}

	/**
	 * @return the out_stock_target
	 */
	public String getOut_stock_target() {
		return out_stock_target;
	}

	/**
	 * @param out_stock_target the out_stock_target to set
	 */
	public void setOut_stock_target(String out_stock_target) {
		this.out_stock_target = out_stock_target;
	}

	/**
	 * @return the out_stock_time
	 */
	public String getOut_stock_time() {
		return out_stock_time;
	}

	/**
	 * @param out_stock_time the out_stock_time to set
	 */
	public void setOut_stock_time(String out_stock_time) {
		this.out_stock_time = out_stock_time;
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
	 * @return the update_time
	 */
	public String getUpdate_time() {
		return update_time;
	}

	/**
	 * @param update_time the update_time to set
	 */
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	/**
	 * @return the details
	 */
	public List<Detail> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public class Detail {
		private String category;
		// private boolean clean_food;
		private String id;
		private String name;
		private String creator;
		private BigDecimal quantity;
		private BigDecimal sale_ratio;
		private BigDecimal real_std_count; // 基本单位出库数
		private String sale_unit_name;
		private String spu_id;
		private String std_unit_name;
		private String std_unit_name_forsale;
		private BigDecimal sale_price;
		private BigDecimal money;

		private List<BatchDetail> batch_details;

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
				return price.divide(new BigDecimal("100"));
			}

			/**
			 * 提交入库单,非必填参数
			 * 
			 * @param price the price to set
			 */
			public void setPrice(BigDecimal price) {
				this.price = price;
			}
		}

		/**
		 * @return the category
		 */
		public String getCategory() {
			return category;
		}

		/**
		 * @param category the category to set
		 */
		public void setCategory(String category) {
			this.category = category;
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
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the creator
		 */
		public String getCreator() {
			return creator;
		}

		/**
		 * @param creator the creator to set
		 */
		public void setCreator(String creator) {
			this.creator = creator;
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

		public BigDecimal getReal_std_count() {
			return real_std_count.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		public void setReal_std_count(BigDecimal real_std_count) {
			this.real_std_count = real_std_count;
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
		 * @return the sale_price
		 */
		public BigDecimal getSale_price() {
			return sale_price.divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
		}

		/**
		 * @param sale_price the sale_price to set
		 */
		public void setSale_price(BigDecimal sale_price) {
			this.sale_price = sale_price;
		}

		public String getStd_unit_name_forsale() {
			return std_unit_name_forsale;
		}

		public void setStd_unit_name_forsale(String std_unit_name_forsale) {
			this.std_unit_name_forsale = std_unit_name_forsale;
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
		 * @return the batch_details
		 */
		public List<BatchDetail> getBatch_details() {
			return batch_details;
		}

		/**
		 * 先进先出模式才设置的参数
		 * 
		 * @param batch_details the batch_details to set
		 */
		public void setBatch_details(List<BatchDetail> batch_details) {
			this.batch_details = batch_details;
		}

	}

}
