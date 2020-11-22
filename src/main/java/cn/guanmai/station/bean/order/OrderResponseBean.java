package cn.guanmai.station.bean.order;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Nov 12, 2018 7:13:47 PM 
* @des 下单返回信息类
* @version 1.0 
*/
public class OrderResponseBean {
	private String msg;
	private int code;
	@JSONField(name = "data")
	private Data data;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the data
	 */
	public Data getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private JSONArray error_sku_ids;
		private JSONArray new_order_ids;
		@JSONField(name = "not_enough_inventories")
		private List<NotEnoughInventories> not_enough_inventories;
		private JSONArray success_sku_ids;
		private JSONArray update_order_ids;

		/**
		 * @return the error_sku_ids
		 */
		public JSONArray getError_sku_ids() {
			return error_sku_ids;
		}

		/**
		 * @param error_sku_ids the error_sku_ids to set
		 */
		public void setError_sku_ids(JSONArray error_sku_ids) {
			this.error_sku_ids = error_sku_ids;
		}

		/**
		 * @return the new_order_ids
		 */
		public JSONArray getNew_order_ids() {
			return new_order_ids;
		}

		/**
		 * @param new_order_ids the new_order_ids to set
		 */
		public void setNew_order_ids(JSONArray new_order_ids) {
			this.new_order_ids = new_order_ids;
		}

		public List<NotEnoughInventories> getNot_enough_inventories() {
			return not_enough_inventories;
		}

		public void setNot_enough_inventories(List<NotEnoughInventories> not_enough_inventories) {
			this.not_enough_inventories = not_enough_inventories;
		}

		/**
		 * @return the success_sku_ids
		 */
		public JSONArray getSuccess_sku_ids() {
			return success_sku_ids;
		}

		/**
		 * @param success_sku_ids the success_sku_ids to set
		 */
		public void setSuccess_sku_ids(JSONArray success_sku_ids) {
			this.success_sku_ids = success_sku_ids;
		}

		/**
		 * @return the update_order_ids
		 */
		public JSONArray getUpdate_order_ids() {
			return update_order_ids;
		}

		/**
		 * @param update_order_ids the update_order_ids to set
		 */
		public void setUpdate_order_ids(JSONArray update_order_ids) {
			this.update_order_ids = update_order_ids;
		}

		public class NotEnoughInventories {
			private BigDecimal count;
			private String sku_id;
			private String name;

			/**
			 * @return the count
			 */
			public BigDecimal getCount() {
				return count;
			}

			/**
			 * @param count the count to set
			 */
			public void setCount(BigDecimal count) {
				this.count = count;
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

			public NotEnoughInventories() {
				super();
			}
		}

		public Data() {
			super();
		}
	}

	public OrderResponseBean() {
		super();
	}

}
