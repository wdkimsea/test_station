package cn.guanmai.manage.bean.custommange.result;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Jan 15, 2019 3:06:43 PM 
* @des 商户对账单详细信息类 /custommanage/bill/detail
* @version 1.0 
*/
public class CustomerBillDetailBean {
	private String KID;
	private String SID;
	private String addr_detail;
	private BigDecimal amount;
	private String company_name;
	private String consignee_name;
	private String consignee_phone;
	private String district_code;
	private String district_name;
	private BigDecimal lack;
	private List<Order> orders;
	private BigDecimal paid_amount;
	private Integer pay_method;
	private String payment_name;
	private String payment_telephone;
	private BigDecimal refund_amount;
	private String resname;
	private BigDecimal sale_employee_id;
	private String sale_employee_name;
	private String username;

	/**
	 * @return the kID
	 */
	public String getKID() {
		return KID;
	}

	/**
	 * @param kID
	 *            the kID to set
	 */
	public void setKID(String kID) {
		KID = kID;
	}

	/**
	 * @return the sID
	 */
	public String getSID() {
		return SID;
	}

	/**
	 * @param sID
	 *            the sID to set
	 */
	public void setSID(String sID) {
		SID = sID;
	}

	/**
	 * @return the addr_detail
	 */
	public String getAddr_detail() {
		return addr_detail;
	}

	/**
	 * @param addr_detail
	 *            the addr_detail to set
	 */
	public void setAddr_detail(String addr_detail) {
		this.addr_detail = addr_detail;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * @return the company_name
	 */
	public String getCompany_name() {
		return company_name;
	}

	/**
	 * @param company_name
	 *            the company_name to set
	 */
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	/**
	 * @return the consignee_name
	 */
	public String getConsignee_name() {
		return consignee_name;
	}

	/**
	 * @param consignee_name
	 *            the consignee_name to set
	 */
	public void setConsignee_name(String consignee_name) {
		this.consignee_name = consignee_name;
	}

	/**
	 * @return the consignee_phone
	 */
	public String getConsignee_phone() {
		return consignee_phone;
	}

	/**
	 * @param consignee_phone
	 *            the consignee_phone to set
	 */
	public void setConsignee_phone(String consignee_phone) {
		this.consignee_phone = consignee_phone;
	}

	/**
	 * @return the district_code
	 */
	public String getDistrict_code() {
		return district_code;
	}

	/**
	 * @param district_code
	 *            the district_code to set
	 */
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

	/**
	 * @return the district_name
	 */
	public String getDistrict_name() {
		return district_name;
	}

	/**
	 * @param district_name
	 *            the district_name to set
	 */
	public void setDistrict_name(String district_name) {
		this.district_name = district_name;
	}

	/**
	 * @return the lack
	 */
	public BigDecimal getLack() {
		return lack;
	}

	/**
	 * @param lack
	 *            the lack to set
	 */
	public void setLack(BigDecimal lack) {
		this.lack = lack;
	}

	/**
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * @param orders
	 *            the orders to set
	 */
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	/**
	 * @return the paid_amount
	 */
	public BigDecimal getPaid_amount() {
		return paid_amount;
	}

	/**
	 * @param paid_amount
	 *            the paid_amount to set
	 */
	public void setPaid_amount(BigDecimal paid_amount) {
		this.paid_amount = paid_amount;
	}

	/**
	 * @return the pay_method
	 */
	public Integer getPay_method() {
		return pay_method;
	}

	/**
	 * @param pay_method
	 *            the pay_method to set
	 */
	public void setPay_method(Integer pay_method) {
		this.pay_method = pay_method;
	}

	/**
	 * @return the payment_name
	 */
	public String getPayment_name() {
		return payment_name;
	}

	/**
	 * @param payment_name
	 *            the payment_name to set
	 */
	public void setPayment_name(String payment_name) {
		this.payment_name = payment_name;
	}

	/**
	 * @return the payment_telephone
	 */
	public String getPayment_telephone() {
		return payment_telephone;
	}

	/**
	 * @param payment_telephone
	 *            the payment_telephone to set
	 */
	public void setPayment_telephone(String payment_telephone) {
		this.payment_telephone = payment_telephone;
	}

	/**
	 * @return the refund_amount
	 */
	public BigDecimal getRefund_amount() {
		return refund_amount;
	}

	/**
	 * @param refund_amount
	 *            the refund_amount to set
	 */
	public void setRefund_amount(BigDecimal refund_amount) {
		this.refund_amount = refund_amount;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname
	 *            the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the sale_employee_id
	 */
	public BigDecimal getSale_employee_id() {
		return sale_employee_id;
	}

	/**
	 * @param sale_employee_id
	 *            the sale_employee_id to set
	 */
	public void setSale_employee_id(BigDecimal sale_employee_id) {
		this.sale_employee_id = sale_employee_id;
	}

	/**
	 * @return the sale_employee_name
	 */
	public String getSale_employee_name() {
		return sale_employee_name;
	}

	/**
	 * @param sale_employee_name
	 *            the sale_employee_name to set
	 */
	public void setSale_employee_name(String sale_employee_name) {
		this.sale_employee_name = sale_employee_name;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	class Order {
		private BigDecimal amount;
		private String date_time;
		private List<Detail> details;
		private String id;
		private BigDecimal paid_amount;
		private String receive_time;
		private BigDecimal refund_amount;
		private String remark;
		private String station_name;
		private String status;

		/**
		 * @return the amount
		 */
		public BigDecimal getAmount() {
			return amount;
		}

		/**
		 * @param amount
		 *            the amount to set
		 */
		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		/**
		 * @return the date_time
		 */
		public String getDate_time() {
			return date_time;
		}

		/**
		 * @param date_time
		 *            the date_time to set
		 */
		public void setDate_time(String date_time) {
			this.date_time = date_time;
		}

		/**
		 * @return the details
		 */
		public List<Detail> getDetails() {
			return details;
		}

		/**
		 * @param details
		 *            the details to set
		 */
		public void setDetails(List<Detail> details) {
			this.details = details;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id
		 *            the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the paid_amount
		 */
		public BigDecimal getPaid_amount() {
			return paid_amount;
		}

		/**
		 * @param paid_amount
		 *            the paid_amount to set
		 */
		public void setPaid_amount(BigDecimal paid_amount) {
			this.paid_amount = paid_amount;
		}

		/**
		 * @return the receive_time
		 */
		public String getReceive_time() {
			return receive_time;
		}

		/**
		 * @param receive_time
		 *            the receive_time to set
		 */
		public void setReceive_time(String receive_time) {
			this.receive_time = receive_time;
		}

		/**
		 * @return the refund_amount
		 */
		public BigDecimal getRefund_amount() {
			return refund_amount;
		}

		/**
		 * @param refund_amount
		 *            the refund_amount to set
		 */
		public void setRefund_amount(BigDecimal refund_amount) {
			this.refund_amount = refund_amount;
		}

		/**
		 * @return the remark
		 */
		public String getRemark() {
			return remark;
		}

		/**
		 * @param remark
		 *            the remark to set
		 */
		public void setRemark(String remark) {
			this.remark = remark;
		}

		/**
		 * @return the station_name
		 */
		public String getStation_name() {
			return station_name;
		}

		/**
		 * @param station_name
		 *            the station_name to set
		 */
		public void setStation_name(String station_name) {
			this.station_name = station_name;
		}

		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		/**
		 * @param status
		 *            the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		class Detail {
			private boolean clean_food;
			private String name;
			private BigDecimal real_quantity;
			private String sale_unit;
			private String sale_unit_name;
			private BigDecimal std_real_quantity;
			private BigDecimal tax;
			private BigDecimal tax_rate;
			private BigDecimal total_item_pay;
			private String unit_name;
			private BigDecimal unit_price;

			/**
			 * @return the clean_food
			 */
			public boolean isClean_food() {
				return clean_food;
			}

			/**
			 * @param clean_food
			 *            the clean_food to set
			 */
			public void setClean_food(boolean clean_food) {
				this.clean_food = clean_food;
			}

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
			 * @return the real_quantity
			 */
			public BigDecimal getReal_quantity() {
				return real_quantity;
			}

			/**
			 * @param real_quantity
			 *            the real_quantity to set
			 */
			public void setReal_quantity(BigDecimal real_quantity) {
				this.real_quantity = real_quantity;
			}

			/**
			 * @return the sale_unit
			 */
			public String getSale_unit() {
				return sale_unit;
			}

			/**
			 * @param sale_unit
			 *            the sale_unit to set
			 */
			public void setSale_unit(String sale_unit) {
				this.sale_unit = sale_unit;
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
			 * @return the std_real_quantity
			 */
			public BigDecimal getStd_real_quantity() {
				return std_real_quantity;
			}

			/**
			 * @param std_real_quantity
			 *            the std_real_quantity to set
			 */
			public void setStd_real_quantity(BigDecimal std_real_quantity) {
				this.std_real_quantity = std_real_quantity;
			}

			/**
			 * @return the tax
			 */
			public BigDecimal getTax() {
				return tax;
			}

			/**
			 * @param tax
			 *            the tax to set
			 */
			public void setTax(BigDecimal tax) {
				this.tax = tax;
			}

			/**
			 * @return the tax_rate
			 */
			public BigDecimal getTax_rate() {
				return tax_rate;
			}

			/**
			 * @param tax_rate
			 *            the tax_rate to set
			 */
			public void setTax_rate(BigDecimal tax_rate) {
				this.tax_rate = tax_rate;
			}

			/**
			 * @return the total_item_pay
			 */
			public BigDecimal getTotal_item_pay() {
				return total_item_pay;
			}

			/**
			 * @param total_item_pay
			 *            the total_item_pay to set
			 */
			public void setTotal_item_pay(BigDecimal total_item_pay) {
				this.total_item_pay = total_item_pay;
			}

			/**
			 * @return the unit_name
			 */
			public String getUnit_name() {
				return unit_name;
			}

			/**
			 * @param unit_name
			 *            the unit_name to set
			 */
			public void setUnit_name(String unit_name) {
				this.unit_name = unit_name;
			}

			/**
			 * @return the unit_price
			 */
			public BigDecimal getUnit_price() {
				return unit_price;
			}

			/**
			 * @param unit_price
			 *            the unit_price to set
			 */
			public void setUnit_price(BigDecimal unit_price) {
				this.unit_price = unit_price;
			}

		}

	}

}
