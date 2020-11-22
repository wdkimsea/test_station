package cn.guanmai.station.bean.invoicing.param;

/**
 * @author liming
 * @date 2019年12月30日
 * @time 下午4:11:15
 * @des 接口 /stock/check/batch 对应的参数
 */

public class BatchStockCheckParam {
	private String batch_number;
	private String remark;
	private double new_stock;

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getNew_stock() {
		return new_stock;
	}

	public void setNew_stock(double new_stock) {
		this.new_stock = new_stock;
	}

}
