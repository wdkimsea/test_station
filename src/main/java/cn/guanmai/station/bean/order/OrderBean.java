package cn.guanmai.station.bean.order;

import cn.guanmai.station.bean.system.ServiceTimeBean;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Apr 1, 2019 8:33:17 PM 
* @des 订单列表展示的订单信息
* @version 1.0 
*/
public class OrderBean {
	private String id;
	private int status;
	private int pay_status;
	private int inspect_status;
	private int client;
	private String route_name;
	private String sort_id;
	private String remark;
	@JSONField(name = "date_time_str")
	private String create_time;
	private BigDecimal sale_money;
	private BigDecimal total_price;
	private BigDecimal freight;
	private BigDecimal abnormal_money;
	private Customer customer;

	private ServiceTimeBean time_config_info;

	private List<Detail> detail;

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
	 * @return the sale_money
	 */
	public BigDecimal getSale_money() {
		return sale_money;
	}

	/**
	 * @param sale_money the sale_money to set
	 */
	public void setSale_money(BigDecimal sale_money) {
		this.sale_money = sale_money;
	}

	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
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
	 * @return the pay_status
	 */
	public int getPay_status() {
		return pay_status;
	}

	/**
	 * @param pay_status the pay_status to set
	 */
	public void setPay_status(int pay_status) {
		this.pay_status = pay_status;
	}

	public int getInspect_status() {
		return inspect_status;
	}

	public void setInspect_status(int inspect_status) {
		this.inspect_status = inspect_status;
	}

	public int getClient() {
		return client;
	}

	public void setClient(int client) {
		this.client = client;
	}

	/**
	 * @return the route_name
	 */
	public String getRoute_name() {
		return route_name;
	}

	/**
	 * @param route_name the route_name to set
	 */
	public void setRoute_name(String route_name) {
		this.route_name = route_name;
	}

	/**
	 * @return the sort_id
	 */
	public String getSort_id() {
		return sort_id;
	}

	/**
	 * @param sort_id the sort_id to set
	 */
	public void setSort_id(String sort_id) {
		this.sort_id = sort_id;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time.replaceAll("T", " ");
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the total_price
	 */
	public BigDecimal getTotal_price() {
		return total_price;
	}

	/**
	 * @param total_price the total_price to set
	 */
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}

	/**
	 * 订单运费
	 * 
	 * @return the freight
	 */
	public BigDecimal getFreight() {
		return freight;
	}

	/**
	 * @param freight the freight to set
	 */
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	/**
	 * @return the abnormal_money
	 */
	public BigDecimal getAbnormal_money() {
		return abnormal_money;
	}

	/**
	 * @param abnormal_money the abnormal_money to set
	 */
	public void setAbnormal_money(BigDecimal abnormal_money) {
		this.abnormal_money = abnormal_money;
	}

	/**
	 * @return the detail
	 */
	public List<Detail> getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(List<Detail> detail) {
		this.detail = detail;
	}

	public class Customer {
		private String uid;
		private String address_id;
		private String receive_begin_time;
		private String receive_end_time;
		private Extender extender;
		private String address;
		private Integer receive_way;
		private String pick_up_st_name;

		/**
		 * @return the uid
		 */
		public String getUid() {
			return uid;
		}

		/**
		 * @param uid the uid to set
		 */
		public void setUid(String uid) {
			this.uid = uid;
		}

		/**
		 * @return the address_id
		 */
		public String getAddress_id() {
			return address_id;
		}

		/**
		 * @param address_id the address_id to set
		 */
		public void setAddress_id(String address_id) {
			this.address_id = address_id;
		}

		/**
		 * @return the receive_begin_time
		 */
		public String getReceive_begin_time() {
			return receive_begin_time;
		}

		/**
		 * @param receive_begin_time the receive_begin_time to set
		 */
		public void setReceive_begin_time(String receive_begin_time) {
			this.receive_begin_time = receive_begin_time;
		}

		/**
		 * @return the receive_end_time
		 */
		public String getReceive_end_time() {
			return receive_end_time;
		}

		/**
		 * @param receive_end_time the receive_end_time to set
		 */
		public void setReceive_end_time(String receive_end_time) {
			this.receive_end_time = receive_end_time;
		}

		public String getPick_up_st_name() {
			return pick_up_st_name;
		}

		public void setPick_up_st_name(String pick_up_st_name) {
			this.pick_up_st_name = pick_up_st_name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public Integer getReceive_way() {
			return receive_way;
		}

		public void setReceive_way(Integer receive_way) {
			this.receive_way = receive_way;
		}

		public class Extender {
			private String resname;

			/**
			 * @return the resname
			 */
			public String getResname() {
				return resname;
			}

			/**
			 * @param resname the resname to set
			 */
			public void setResname(String resname) {
				this.resname = resname;
			}

		}

		/**
		 * @return the extender
		 */
		public Extender getExtender() {
			return extender;
		}

		/**
		 * @param extender the extender to set
		 */
		public void setExtender(Extender extender) {
			this.extender = extender;
		}

	}

	/**
	 * @return the time_config_info
	 */
	public ServiceTimeBean getTime_config_info() {
		return time_config_info;
	}

	/**
	 * @param time_config_info the time_config_info to set
	 */
	public void setTime_config_info(ServiceTimeBean time_config_info) {
		this.time_config_info = time_config_info;
	}

	public class Detail {
		private String spu_id;
		@JSONField(name = "id")
		private String sku_id;

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
	}

}
