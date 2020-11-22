package cn.guanmai.bshop.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年9月26日
 * @time 下午4:25:55
 * @des 商城进销存接口 /stock/spu/in_stock/create 对应的参数
 */

public class BshopSpuInStockParam {
	private String address_id;
	private String start_time;
	private String end_time;
	private String search;
	private int query_type = 1;
	private int order_status = -1;
	private Integer all;
	private List<BatchInStock> batch_list;

	public class BatchInStock {
		private String order_id;
		private String sku_id;
		private BigDecimal std_quantity;
		private BigDecimal std_unit_price;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public BigDecimal getStd_quantity() {
			return std_quantity;
		}

		public void setStd_quantity(BigDecimal std_quantity) {
			this.std_quantity = std_quantity;
		}

		public BigDecimal getStd_unit_price() {
			return std_unit_price;
		}

		public void setStd_unit_price(BigDecimal std_unit_price) {
			this.std_unit_price = std_unit_price;
		}
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getQuery_type() {
		return query_type;
	}

	public void setQuery_type(int query_type) {
		this.query_type = query_type;
	}

	public int getOrder_status() {
		return order_status;
	}

	public void setOrder_status(int order_status) {
		this.order_status = order_status;
	}

	public Integer getAll() {
		return all;
	}

	public void setAll(Integer all) {
		this.all = all;
	}

	public List<BatchInStock> getBatch_list() {
		return batch_list;
	}

	public void setBatch_list(List<BatchInStock> batch_list) {
		this.batch_list = batch_list;
	}

}
