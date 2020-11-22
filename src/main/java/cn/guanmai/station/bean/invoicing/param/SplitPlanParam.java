package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午3:48:44
 * @description: 分割方案-新建、修改封装参数
 * @version: 1.0
 */

public class SplitPlanParam {
	private String name;
	private String remark;
	private boolean is_deleted;
	private List<GainSpu> gain_spus;
	private String source_spu_id;

	// id 修改分割方案特有参数
	private String id;

	// 版本 修改分割方案特有参数
	private Integer version;

	public class GainSpu {
		private String spu_id;
		private BigDecimal split_ratio;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getSplit_ratio() {
			return split_ratio;
		}

		public void setSplit_ratio(BigDecimal split_ratio) {
			this.split_ratio = split_ratio;
		}
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

	public boolean isIs_deleted() {
		return is_deleted;
	}

	public void setIs_deleted(boolean is_deleted) {
		this.is_deleted = is_deleted;
	}

	public List<GainSpu> getGain_spus() {
		return gain_spus;
	}

	public void setGain_spus(List<GainSpu> gain_spus) {
		this.gain_spus = gain_spus;
	}

	public String getSource_spu_id() {
		return source_spu_id;
	}

	public void setSource_spu_id(String source_spu_id) {
		this.source_spu_id = source_spu_id;
	}

	/**
	 * id 修改分割方案特有参数
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * id 修改分割方案特有参数
	 * 
	 * @return
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 版本 修改分割方案特有参数
	 * 
	 * @return
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 版本 修改分割方案特有参数
	 * 
	 * @param version
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

}
