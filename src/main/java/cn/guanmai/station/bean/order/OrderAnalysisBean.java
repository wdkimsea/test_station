package cn.guanmai.station.bean.order;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Mar 14, 2019 7:50:01 PM 
* @des 订单分析结果
* @version 1.0 
*/
public class OrderAnalysisBean {
	@JSONField(name = "orders")
	private List<OrderDetailAnalysis> orderDetailAnalysisList;

	@JSONField(name = "products")
	private List<OrderSkuAnalysis> orderSkuAnalysisList;

	private int async;

	private String task_url;

	public class OrderDetailAnalysis {

		private String id;

		// 商品总价
		private BigDecimal sales;

		// 接收方式
		private String receive_way;

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
		 * @return the sales
		 */
		public BigDecimal getSales() {
			return sales;
		}

		/**
		 * @param sales the sales to set
		 */
		public void setSales(BigDecimal sales) {
			this.sales = sales;
		}

		public String getReceive_way() {
			return receive_way;
		}

		public void setReceive_way(String receive_way) {
			this.receive_way = receive_way;
		}
	}

	public class OrderSkuAnalysis {
		private String order_id;
		private String spu_id;
		private String sku_id;
		private BigDecimal quantity;
		private BigDecimal real_quantity;
		private BigDecimal real_std_count;
		private BigDecimal quantity_base_unit;
		private BigDecimal std_sale_price;

		/**
		 * @return the order_id
		 */
		public String getOrder_id() {
			return order_id;
		}

		/**
		 * @param order_id the order_id to set
		 */
		public void setOrder_id(String order_id) {
			this.order_id = order_id;
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
		 * @return the real_quantity
		 */
		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		/**
		 * @param real_quantity the real_quantity to set
		 */
		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		/**
		 * @return the real_std_count
		 */
		public BigDecimal getReal_std_count() {
			return real_std_count;
		}

		/**
		 * @param real_std_count the real_std_count to set
		 */
		public void setReal_std_count(BigDecimal real_std_count) {
			this.real_std_count = real_std_count;
		}

		/**
		 * @return the quantity_base_unit
		 */
		public BigDecimal getQuantity_base_unit() {
			return quantity_base_unit;
		}

		/**
		 * @param quantity_base_unit the quantity_base_unit to set
		 */
		public void setQuantity_base_unit(BigDecimal quantity_base_unit) {
			this.quantity_base_unit = quantity_base_unit;
		}

		/**
		 * @return the std_sale_price
		 */
		public BigDecimal getStd_sale_price() {
			return std_sale_price;
		}

		/**
		 * @param std_sale_price the std_sale_price to set
		 */
		public void setStd_sale_price(BigDecimal std_sale_price) {
			this.std_sale_price = std_sale_price;
		}
	}

	/**
	 * @return the orderDetailAnalysisList
	 */
	public List<OrderDetailAnalysis> getOrderDetailAnalysisList() {
		return orderDetailAnalysisList;
	}

	/**
	 * @param orderDetailAnalysisList the orderDetailAnalysisList to set
	 */
	public void setOrderDetailAnalysisList(List<OrderDetailAnalysis> orderDetailAnalysisList) {
		this.orderDetailAnalysisList = orderDetailAnalysisList;
	}

	/**
	 * @return the orderSkuAnalysisList
	 */
	public List<OrderSkuAnalysis> getOrderSkuAnalysisList() {
		return orderSkuAnalysisList;
	}

	/**
	 * @param orderSkuAnalysisList the orderSkuAnalysisList to set
	 */
	public void setOrderSkuAnalysisList(List<OrderSkuAnalysis> orderSkuAnalysisList) {
		this.orderSkuAnalysisList = orderSkuAnalysisList;
	}

	/**
	 * @return the async
	 */
	public int getAsync() {
		return async;
	}

	/**
	 * @param async the async to set
	 */
	public void setAsync(int async) {
		this.async = async;
	}

	/**
	 * @return the task_url
	 */
	public String getTask_url() {
		return task_url;
	}

	/**
	 * @param task_url the task_url to set
	 */
	public void setTask_url(String task_url) {
		this.task_url = task_url;
	}

}
