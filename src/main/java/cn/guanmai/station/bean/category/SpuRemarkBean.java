package cn.guanmai.station.bean.category;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午5:12:13
 * @description: 接口 /station/spu_remark/spu_search/ 对应结果
 * @version: 1.0
 */

public class SpuRemarkBean {
	private String category_name_1;
	private String category_name_2;
	private String spu_id;
	private String spu_name;
	private String spu_remark;

	public String getCategory_name_1() {
		return category_name_1;
	}

	public void setCategory_name_1(String category_name_1) {
		this.category_name_1 = category_name_1;
	}

	public String getCategory_name_2() {
		return category_name_2;
	}

	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
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

	public String getSpu_remark() {
		return spu_remark;
	}

	public void setSpu_remark(String spu_remark) {
		this.spu_remark = spu_remark;
	}

}
