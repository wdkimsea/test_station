package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * @author: liming
 * @Date: 2020年7月7日 上午10:36:01
 * @description: /stock/split/loss/list 分割列表数据
 * @version: 1.0
 */

public class SplitLossBean {
	private String source_spu_id;
	private String source_spu_name;
	private BigDecimal split_loss;
	private String split_time;
	private String std_unit_name;

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

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
