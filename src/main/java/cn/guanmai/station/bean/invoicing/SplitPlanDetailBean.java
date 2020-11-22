package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午5:18:24
 * @description:
 * @version: 1.0
 */

public class SplitPlanDetailBean {
	private String id;
	private String name;
	private String remark;
	private String source_spu_id;
	private String source_spu_name;
	private String std_unit_name;
	private int version;

	private List<GainSpu> gain_spus;

	public class GainSpu {
		private int is_deleted;
		private BigDecimal split_ratio;
		private String spu_id;
		private String spu_name;
		private String std_unit_name;

		public int getIs_deleted() {
			return is_deleted;
		}

		public void setIs_deleted(int is_deleted) {
			this.is_deleted = is_deleted;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSource_spu_id() {
		return source_spu_id;
	}

	public void setSource_spu_id(String source_spu_id) {
		this.source_spu_id = source_spu_id;
	}

	public String getSource_spu_name() {
		return source_spu_name;
	}

	public void setSource_spu_name(String source_spu_name) {
		this.source_spu_name = source_spu_name;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public List<GainSpu> getGain_spus() {
		return gain_spus;
	}

	public void setGain_spus(List<GainSpu> gain_spus) {
		this.gain_spus = gain_spus;
	}
}
