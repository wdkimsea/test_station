package cn.guanmai.bshop.bean.order;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @author: liming
 * @Date: 2020年6月16日 下午8:52:19
 * @description:
 * @version: 1.0
 */

public class BsCartBean {
	private Info info;

	private Map<String, BigDecimal> list;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Map<String, BigDecimal> getList() {
		return list;
	}

	public void setList(Map<String, BigDecimal> list) {
		this.list = list;
	}

	public class Info {
		private int count;
		private boolean is_price_timing;
		private int reward_sku_count;
		private BigDecimal sum_money;
		private BigDecimal total_cost_points;

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public boolean isIs_price_timing() {
			return is_price_timing;
		}

		public void setIs_price_timing(boolean is_price_timing) {
			this.is_price_timing = is_price_timing;
		}

		public int getReward_sku_count() {
			return reward_sku_count;
		}

		public void setReward_sku_count(int reward_sku_count) {
			this.reward_sku_count = reward_sku_count;
		}

		public BigDecimal getSum_money() {
			return sum_money;
		}

		public void setSum_money(BigDecimal sum_money) {
			this.sum_money = sum_money;
		}

		public BigDecimal getTotal_cost_points() {
			return total_cost_points;
		}

		public void setTotal_cost_points(BigDecimal total_cost_points) {
			this.total_cost_points = total_cost_points;
		}
	}

}
