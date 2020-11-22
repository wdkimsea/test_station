package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建仓内移库请求参数类 Created by yangjinhai on 2019/8/8.
 */
public class TransferSheetCreateParam {
	private int status;
	private String remark;
	private String sheet_no;
	private List<Detail> details;

	public List<Detail> getDetails() {
		return details;
	}

	public void setDetails(List<Detail> details) {
		this.details = details;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

	public class Detail {
		String spu_id;
		String out_batch_num;
		String remark;
		BigDecimal in_shelf_id;
		BigDecimal out_amount;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getOut_batch_num() {
			return out_batch_num;
		}

		public void setOut_batch_num(String out_batch_num) {
			this.out_batch_num = out_batch_num;
		}

		public BigDecimal getIn_shelf_id() {
			return in_shelf_id;
		}

		public void setIn_shelf_id(BigDecimal in_shelf_id) {
			this.in_shelf_id = in_shelf_id;
		}

		public BigDecimal getOut_amount() {
			return out_amount;
		}

		public void setOut_amount(BigDecimal out_amount) {
			this.out_amount = out_amount;
		}
	}

}
