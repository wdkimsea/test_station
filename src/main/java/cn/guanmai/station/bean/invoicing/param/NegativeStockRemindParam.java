package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liming
 * @date 2019年7月25日 下午2:26:09
 * @des 接口 /stock/out_stock_sheet/negative_stock_remind_single 对应的参数
 * @version 1.0
 */
public class NegativeStockRemindParam {
	private String id;

	private List<Detail> details;

	private Integer is_submit;

	public class Detail {
		private String sku_id;
		private String spu_id;
		private BigDecimal quantity;

		public List<FIFO> FIFO;

		public class FIFO {
			private String batch_number;
			private BigDecimal out_stock_base;

			public String getBatch_number() {
				return batch_number;
			}

			public void setBatch_number(String batch_number) {
				this.batch_number = batch_number;
			}

			public BigDecimal getOut_stock_base() {
				return out_stock_base;
			}

			public void setOut_stock_base(BigDecimal out_stock_base) {
				this.out_stock_base = out_stock_base;
			}
		}

		public String getSku_id() {
			return sku_id;
		}

		public void setSku_id(String sku_id) {
			this.sku_id = sku_id;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public List<FIFO> getFIFO() {
			return FIFO;
		}

		public void setFIFO(List<FIFO> fIFO) {
			FIFO = fIFO;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public Integer getIs_submit() {
		return is_submit;
	}

	public void setIs_submit(Integer is_submit) {
		this.is_submit = is_submit;
	}

}
