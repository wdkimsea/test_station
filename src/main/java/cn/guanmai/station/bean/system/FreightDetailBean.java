package cn.guanmai.station.bean.system;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午4:51:18
 * @description:
 * @version: 1.0
 */

public class FreightDetailBean {
	@JSONField(name="default")
	private boolean is_default;
	private String name;
	private String id;
	private DeliveryFreight delivery_freight;
	private PickUpFreight pick_up_freight;

	public class DeliveryFreight {
		private BigDecimal min_total_price;
		private List<Section> section;

		public class Section {
			private BigDecimal freight;
			private BigDecimal max;
			private BigDecimal min;

			public BigDecimal getFreight() {
				return freight;
			}

			public void setFreight(BigDecimal freight) {
				this.freight = freight;
			}

			public BigDecimal getMax() {
				return max;
			}

			public void setMax(BigDecimal max) {
				this.max = max;
			}

			public BigDecimal getMin() {
				return min;
			}

			public void setMin(BigDecimal min) {
				this.min = min;
			}
		}

		public BigDecimal getMin_total_price() {
			return min_total_price;
		}

		public void setMin_total_price(BigDecimal min_total_price) {
			this.min_total_price = min_total_price;
		}

		public List<Section> getSection() {
			return section;
		}

		public void setSection(List<Section> section) {
			this.section = section;
		}

	}

	public class PickUpFreight {
		private BigDecimal min_total_price;
		private List<Section> section;

		public class Section {
			private BigDecimal freight;
			private BigDecimal max;
			private BigDecimal min;

			public BigDecimal getFreight() {
				return freight;
			}

			public void setFreight(BigDecimal freight) {
				this.freight = freight;
			}

			public BigDecimal getMax() {
				return max;
			}

			public void setMax(BigDecimal max) {
				this.max = max;
			}

			public BigDecimal getMin() {
				return min;
			}

			public void setMin(BigDecimal min) {
				this.min = min;
			}
		}

		public BigDecimal getMin_total_price() {
			return min_total_price;
		}

		public void setMin_total_price(BigDecimal min_total_price) {
			this.min_total_price = min_total_price;
		}

		public List<Section> getSection() {
			return section;
		}

		public void setSection(List<Section> section) {
			this.section = section;
		}

	}

	public boolean isIs_default() {
		return is_default;
	}

	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DeliveryFreight getDelivery_freight() {
		return delivery_freight;
	}

	public void setDelivery_freight(DeliveryFreight delivery_freight) {
		this.delivery_freight = delivery_freight;
	}

	public PickUpFreight getPick_up_freight() {
		return pick_up_freight;
	}

	public void setPick_up_freight(PickUpFreight pick_up_freight) {
		this.pick_up_freight = pick_up_freight;
	}

}
