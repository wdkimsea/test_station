package cn.guanmai.station.bean.category;

/**
 * @author: liming
 * @Date: 2020年2月17日 下午8:03:43
 * @description:
 * @version: 1.0
 */

public class SpuSimpleBean {
	private String id;
	private String category_1_name;
	private String category_2_name;
	private String pinlei_name;
	private String std_unit_name;
	private int p_type;
	private String desc;
	private int rank;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory_1_name() {
		return category_1_name;
	}

	public void setCategory_1_name(String category_1_name) {
		this.category_1_name = category_1_name;
	}

	public String getCategory_2_name() {
		return category_2_name;
	}

	public void setCategory_2_name(String category_2_name) {
		this.category_2_name = category_2_name;
	}

	public String getPinlei_name() {
		return pinlei_name;
	}

	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	public String getStd_unit_name() {
		return std_unit_name;
	}

	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	public int getP_type() {
		return p_type;
	}

	public void setP_type(int p_type) {
		this.p_type = p_type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

}
