package cn.guanmai.station.bean.order;

import java.math.BigDecimal;
import java.util.List;

public class OrderBatchUploadResultBean {
	private String task_id;
	private String file_name;

	private List<Detail> details;

	public class Detail {
		private String sid;
		private String resname;

		private List<Sku> skus;

		public class Sku {
			private String sku_id;
			private String sku_name;
			private BigDecimal quantity;
			private BigDecimal sale_price;
			private String spu_remark;

			public String getSku_id() {
				return sku_id;
			}

			public void setSku_id(String sku_id) {
				this.sku_id = sku_id;
			}

			public String getSku_name() {
				return sku_name;
			}

			public void setSku_name(String sku_name) {
				this.sku_name = sku_name;
			}

			public BigDecimal getQuantity() {
				return quantity;
			}

			public void setQuantity(BigDecimal quantity) {
				this.quantity = quantity;
			}

			public BigDecimal getSale_price() {
				return sale_price;
			}

			public void setSale_price(BigDecimal sale_price) {
				this.sale_price = sale_price;
			}

			public String getSpu_remark() {
				return spu_remark;
			}

			public void setSpu_remark(String spu_remark) {
				this.spu_remark = spu_remark;
			}
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getResname() {
			return resname;
		}

		public void setResname(String resname) {
			this.resname = resname;
		}

		public List<Sku> getSkus() {
			return skus;
		}

		public void setSkus(List<Sku> skus) {
			this.skus = skus;
		}

	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		this.task_id = task_id;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

}
