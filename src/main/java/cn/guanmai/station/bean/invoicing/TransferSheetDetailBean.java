package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/**
 * 仓内移库详情信息 Created by yangjinhai on 2019/8/9.
 */
public class TransferSheetDetailBean {
	private int status;
	private String creator;
	private String create_time;
	private String sheet_no;
	private String submit_time;
	private String remark;
	private List<Detail> details;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

	public String getSubmit_time() {
		return submit_time;
	}

	public void setSubmit_time(String submit_time) {
		this.submit_time = submit_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public class Detail {
		private String spu_name;
		private String category_2_name;
		private String pinlei_name;
		private String remark;
		private String out_batch_num;
		private BigDecimal remain;
		private String category_1_name;
		private String spu_id;
		private String unit_name;
		private BigDecimal out_amount;
		private String in_batch_num;
		private List<InShelf> in_shelf;// 移入货位
		private List<OutShelf> out_shelf;// 移出货位

		public String getSpu_name() {
			return spu_name;
		}

		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

		public String getCategory_2_name() {
			return category_2_name;
		}

		public void setCategory_2_name(String category_2_name) {
			this.category_2_name = category_2_name;
		}

		public String getOut_batch_num() {
			return out_batch_num;
		}

		public void setOut_batch_num(String out_batch_num) {
			this.out_batch_num = out_batch_num;
		}

		public BigDecimal getRemain() {
			return remain;
		}

		public void setRemain(BigDecimal remain) {
			this.remain = remain;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getUnit_name() {
			return unit_name;
		}

		public void setUnit_name(String unit_name) {
			this.unit_name = unit_name;
		}

		public BigDecimal getOut_amount() {
			return out_amount;
		}

		public void setOut_amount(BigDecimal out_amount) {
			this.out_amount = out_amount;
		}

		public List<InShelf> getIn_shelf() {
			return in_shelf;
		}

		public void setIn_shelf(List<InShelf> in_shelf) {
			this.in_shelf = in_shelf;
		}

		public List<OutShelf> getOut_shelf() {
			return out_shelf;
		}

		public void setOut_shelf(List<OutShelf> out_shelf) {
			this.out_shelf = out_shelf;
		}

		public String getIn_batch_num() {
			return in_batch_num;
		}

		public void setIn_batch_num(String in_batch_num) {
			this.in_batch_num = in_batch_num;
		}

		public String getCategory_1_name() {
			return category_1_name;
		}

		public void setCategory_1_name(String category_1_name) {
			this.category_1_name = category_1_name;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getPinlei_name() {
			return pinlei_name;
		}

		public void setPinlei_name(String pinlei_name) {
			this.pinlei_name = pinlei_name;
		}

		public class InShelf {
			private String id;
			private String name;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}

		public class OutShelf {
			private BigDecimal id;
			private String name;

			public BigDecimal getId() {
				return id;
			}

			public void setId(BigDecimal id) {
				this.id = id;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}
		}
	}
}
