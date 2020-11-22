package cn.guanmai.station.bean.invoicing.param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: liming
 * @Date: 2020年6月28日 下午8:03:44
 * @description:
 * @version: 1.0
 */

public class SplitSheetParam {
	private String id; // 修改分割单据带的参数
	private String plan_id;
	private int plan_version;
	private String source_spu_id;
	private BigDecimal source_quantity;
	private String split_time;
	private List<GainSpu> gain_spus;

	public class GainSpu {
		private String spu_id;
		private BigDecimal real_quantity;
		private BigDecimal in_stock_price;

		public String getSpu_id() {
			return spu_id;
		}

		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		public BigDecimal getReal_quantity() {
			return real_quantity.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		public void setReal_quantity(BigDecimal real_quantity) {
			this.real_quantity = real_quantity;
		}

		public BigDecimal getIn_stock_price() {
			return in_stock_price.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		public void setIn_stock_price(BigDecimal in_stock_price) {
			this.in_stock_price = in_stock_price;
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public int getPlan_version() {
		return plan_version;
	}

	public void setPlan_version(int plan_version) {
		this.plan_version = plan_version;
	}

	public String getSource_spu_id() {
		return source_spu_id;
	}

	public void setSource_spu_id(String source_spu_id) {
		this.source_spu_id = source_spu_id;
	}

	public String getSplit_time() {
		return split_time;
	}

	public void setSplit_time(String split_time) {
		this.split_time = split_time;
	}

	public BigDecimal getSource_quantity() {
		return source_quantity;
	}

	public void setSource_quantity(BigDecimal source_quantity) {
		this.source_quantity = source_quantity;
	}

	public List<GainSpu> getGain_spus() {
		return gain_spus;
	}

	public void setGain_spus(List<GainSpu> gain_spus) {
		this.gain_spus = gain_spus;
	}

	public SplitSheetParam() {
		super();
	}

	/**
	 * 修改分割单据对应的参数
	 * 
	 * @param id
	 * @param source_quantity
	 * @param gain_spus
	 */
	public SplitSheetParam(String id, BigDecimal source_quantity, List<GainSpu> gain_spus) {
		super();
		this.id = id;
		this.source_quantity = source_quantity;
		this.gain_spus = gain_spus;
	}

}
