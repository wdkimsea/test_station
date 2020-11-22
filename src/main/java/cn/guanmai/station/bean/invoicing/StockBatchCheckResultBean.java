package cn.guanmai.station.bean.invoicing;

/**
 * @author liming
 * @date 2019年12月30日
 * @time 下午3:50:09
 * @des 接口 /stock/check/upload (先进先出站点对应的结果)
 */

public class StockBatchCheckResultBean {
	private double avg_price;
	private String batch_number;
	private String category_id_1;
	private String category_id_2;
	private String name;
	private double new_stock;
	private double old_stock;
	private String remark;
	private String spu_id;
	private String std_unit_name;

	public double getAvg_price() {
		return avg_price;
	}

	public void setAvg_price(double avg_price) {
		this.avg_price = avg_price;
	}

	public String getBatch_number() {
		return batch_number;
	}

	public void setBatch_number(String batch_number) {
		this.batch_number = batch_number;
	}

	public String getCategory_id_1() {
		return category_id_1;
	}

	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	public String getCategory_id_2() {
		return category_id_2;
	}

	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNew_stock() {
		return new_stock;
	}

	public void setNew_stock(double new_stock) {
		this.new_stock = new_stock;
	}

	public double getOld_stock() {
		return old_stock;
	}

	public void setOld_stock(double old_stock) {
		this.old_stock = old_stock;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpu_id() {
		return spu_id;
	}

	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

}
