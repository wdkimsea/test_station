package cn.guanmai.station.bean.jingcai;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年5月12日 下午7:40:24
 * @description:
 * @version: 1.0
 */

public class ProcessTaskBean {
	private String category1_name;
	private String category2_name;
	private String pinlei_name;
	private String name;
	private String spu_id;
	private String sku_id;
	private String std_unit_name;
	private String sale_unit_name;
	private String process_label;
	private BigDecimal sale_ratio;
	private BigDecimal plan_amount;
	private BigDecimal stock;

	private int status;

	private List<Task> tasks;

	public class Task {
		private BigDecimal id;
		private String route_name;
		private String driver_name;
		private String address_label;
		private String order_id;
		private Integer order_status;
		private String resname;
		private BigDecimal order_amount;

		public BigDecimal getId() {
			return id;
		}

		public void setId(BigDecimal id) {
			this.id = id;
		}

		public String getRoute_name() {
			return route_name;
		}

		public void setRoute_name(String route_name) {
			this.route_name = route_name;
		}

		public String getDriver_name() {
			return driver_name;
		}

		public void setDriver_name(String driver_name) {
			this.driver_name = driver_name;
		}

		public String getAddress_label() {
			return address_label;
		}

		public void setAddress_label(String address_label) {
			this.address_label = address_label;
		}

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public Integer getOrder_status() {
			return order_status;
		}

		public void setOrder_status(Integer order_status) {
			this.order_status = order_status;
		}

		public String getResname() {
			return resname;
		}

		public void setResname(String resname) {
			this.resname = resname;
		}

		public BigDecimal getOrder_amount() {
			return order_amount;
		}

		public void setOrder_amount(BigDecimal order_amount) {
			this.order_amount = order_amount;
		}
	}

	public String getCategory1_name() {
		return category1_name;
	}

	public void setCategory1_name(String category1_name) {
		this.category1_name = category1_name;
	}

	public String getCategory2_name() {
		return category2_name;
	}

	public void setCategory2_name(String category2_name) {
		this.category2_name = category2_name;
	}

	public String getPinlei_name() {
		return pinlei_name;
	}

	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
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

	public String getSku_id() {
		return sku_id;
	}

	public void setSku_id(String sku_id) {
		this.sku_id = sku_id;
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

	public String getProcess_label() {
		return process_label;
	}

	public void setProcess_label(String process_label) {
		this.process_label = process_label;
	}

	public BigDecimal getSale_ratio() {
		return sale_ratio;
	}

	public void setSale_ratio(BigDecimal sale_ratio) {
		this.sale_ratio = sale_ratio;
	}

	public BigDecimal getPlan_amount() {
		return plan_amount;
	}

	public void setPlan_amount(BigDecimal plan_amount) {
		this.plan_amount = plan_amount;
	}

	public BigDecimal getStock() {
		return stock;
	}

	public void setStock(BigDecimal stock) {
		this.stock = stock;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

}
