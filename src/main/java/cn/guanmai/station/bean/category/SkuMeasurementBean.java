package cn.guanmai.station.bean.category;

import java.math.BigDecimal;

/**
 * @author liming
 * @date 2019年12月23日
 * @time 下午7:53:30
 * @des TODO
 */

public class SkuMeasurementBean {
	private BigDecimal std_ratio;
	private String std_unit_name_forsale;

	public BigDecimal getStd_ratio() {
		return std_ratio;
	}

	public void setStd_ratio(BigDecimal std_ratio) {
		this.std_ratio = std_ratio;
	}

	public String getStd_unit_name_forsale() {
		return std_unit_name_forsale;
	}

	public void setStd_unit_name_forsale(String std_unit_name_forsale) {
		this.std_unit_name_forsale = std_unit_name_forsale;
	}

}
