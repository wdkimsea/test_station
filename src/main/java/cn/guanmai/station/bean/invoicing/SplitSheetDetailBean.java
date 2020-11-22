package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午8:23:05
 * @description:
 * @version: 1.0
 */

public class SplitSheetDetailBean {
	private String create_time;
	private String id;
	private String operator;
	private String plan_id;
	private String plan_name;
	private int plan_version;
	private String sheet_no;
	private BigDecimal split_loss;
	private String split_time;
	private int status;
	private SourceSpu source_spu;
	private List<GainSpu> gain_spus;

	public class SourceSpu {
		private String spu_id;
		private String spu_name;
		private BigDecimal quantity;
		private String std_unit_name;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getSpu_name() {
			return spu_name;
		}

		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}

	}

	public class GainSpu {
		private BigDecimal in_stock_price;
		private BigDecimal real_quantity;
		private BigDecimal remain_quantity;
		private BigDecimal split_ratio;
		private String spu_id;
		private String spu_name;
		private String std_unit_name;

		public BigDecimal getIn_stock_price() {
			return in_stock_price;
		}

		public void setIn_stock_price(BigDecimal in_stock_price) {
			this.in_stock_price = in_stock_price;
		}

		public BigDecimal getReal_quantity() {
			return real_quantity;
		}

		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		public BigDecimal getRemain_quantity() {
			return remain_quantity;
		}

		public void setRemain_quantity(BigDecimal remain_quantity) {
			this.remain_quantity = remain_quantity;
		}

		public BigDecimal getSplit_ratio() {
			return split_ratio;
		}

		public void setSplit_ratio(BigDecimal split_ratio) {
			this.split_ratio = split_ratio;
		}

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public String getSpu_name() {
			return spu_name;
		}

		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

		public String getStd_unit_name() {
			return std_unit_name;
		}

		public void setStd_unit_name(String std_unit_name) {
			this.std_unit_name = std_unit_name;
		}
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public int getPlan_version() {
		return plan_version;
	}

	public void setPlan_version(int plan_version) {
		this.plan_version = plan_version;
	}

	public String getSheet_no() {
		return sheet_no;
	}

	public void setSheet_no(String sheet_no) {
		this.sheet_no = sheet_no;
	}

	public BigDecimal getSplit_loss() {
		return split_loss;
	}

	public void setSplit_loss(BigDecimal split_loss) {
		this.split_loss = split_loss;
	}

	public String getSplit_time() {
		return split_time;
	}

	public void setSplit_time(String split_time) {
		this.split_time = split_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public SourceSpu getSource_spu() {
		return source_spu;
	}

	public void setSource_spu(SourceSpu source_spu) {
		this.source_spu = source_spu;
	}

	public List<GainSpu> getGain_spus() {
		return gain_spus;
	}

	public void setGain_spus(List<GainSpu> gain_spus) {
		this.gain_spus = gain_spus;
	}

}
