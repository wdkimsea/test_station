package cn.guanmai.open.bean.product;

public class OpenCategory2Bean {
	private String category2_id;
	private String category2_name;
	private String category1_id;

	public String getId() {
		return category2_id;
	}

	public void setId(String id) {
		this.category2_id = id;
	}

	public String getName() {
		return category2_name;
	}

	public void setName(String name) {
		this.category2_name = name;
	}

	/**
	 * @return the category1_id
	 */
	public String getCategory1_id() {
		return category1_id;
	}

	/**
	 * @param category1_id
	 *            the category1_id to set
	 */
	public void setCategory1_id(String category1_id) {
		this.category1_id = category1_id;
	}

}
