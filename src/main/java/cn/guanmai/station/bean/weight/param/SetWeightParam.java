package cn.guanmai.station.bean.weight.param;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Jan 8, 2019 3:47:05 PM 
* @des 新版称重软件-称重参数类
* @version 1.0 
*/
public class SetWeightParam {
	// ST分拣传的参数
	private Integer need_res;
	private String date;
	private int sale_unit_sort_independence;
	private List<Weight> weights;

	public class Weight {
		private String sku_id;
		private String order_id;
		// 上一次称重数
		private BigDecimal weight;
		// 此次称重数
		private BigDecimal set_weight;

		// 销售规格上一次称重
		private BigDecimal sale_unit_weight;

		// 销售规格此次称重
		private BigDecimal sale_unit_set_weight;

		private Object add;

		private Integer sort_way;

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
		 * @return the weight
		 */
		public BigDecimal getWeight() {
			return weight;
		}

		/**
		 * 上一次称重数
		 * 
		 * @param weight the weight to set
		 */
		public void setWeight(BigDecimal weight) {
			this.weight = weight;
		}

		/**
		 * @return the set_weight
		 */
		public BigDecimal getSet_weight() {
			return set_weight;
		}

		/**
		 * 此次称重数
		 * 
		 * @param set_weight the set_weight to set
		 */
		public void setSet_weight(BigDecimal set_weight) {
			this.set_weight = set_weight;
		}

		public BigDecimal getSale_unit_weight() {
			return sale_unit_weight;
		}

		public void setSale_unit_weight(BigDecimal sale_unit_weight) {
			this.sale_unit_weight = sale_unit_weight;
		}

		public BigDecimal getSale_unit_set_weight() {
			return sale_unit_set_weight;
		}

		public void setSale_unit_set_weight(BigDecimal sale_unit_set_weight) {
			this.sale_unit_set_weight = sale_unit_set_weight;
		}

		/**
		 * @return the add
		 */
		public boolean isAdd() {
			return Boolean.valueOf(String.valueOf(add));
		}

		/**
		 * 是否加入称重数
		 * 
		 * @param add the add to set
		 */
		public void setAdd(boolean add) {
			if (add == true) {
				this.add = 1;
			} else {
				this.add = false;
			}

		}

		/**
		 * @return the sort_way
		 */
		public Integer getSort_way() {
			return sort_way;
		}

		/**
		 * 分拣序号的生成方式
		 * 
		 * @param sort_way the sort_way to set
		 */
		public void setSort_way(Integer sort_way) {
			this.sort_way = sort_way;
		}

		public Weight(String order_id, String sku_id, BigDecimal weight, BigDecimal set_weight, boolean add,
				Integer sort_way) {
			super();
			this.order_id = order_id;
			this.sku_id = sku_id;
			this.weight = weight;
			this.set_weight = set_weight;
			this.add = add == true ? 1 : false;
			this.sort_way = sort_way;
		}

		public Weight() {
			super();
		}

	}

	public Integer getNeed_res() {
		return need_res;
	}

	public void setNeed_res(Integer need_res) {
		this.need_res = need_res;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getSale_unit_sort_independence() {
		return sale_unit_sort_independence;
	}

	public void setSale_unit_sort_independence(int sale_unit_sort_independence) {
		this.sale_unit_sort_independence = sale_unit_sort_independence;
	}

	public List<Weight> getWeights() {
		return weights;
	}

	public void setWeights(List<Weight> weights) {
		this.weights = weights;
	}
}
