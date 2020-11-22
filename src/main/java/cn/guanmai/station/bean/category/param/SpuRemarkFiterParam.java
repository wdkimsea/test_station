package cn.guanmai.station.bean.category.param;

/**
 * @author: liming
 * @Date: 2020年7月15日 下午5:06:52
 * @description: 接口 /station/spu_remark/spu_search/ 对应的参数
 * @version: 1.0
 */

public class SpuRemarkFiterParam {
	private String address_id;
	private String spu_type;
	private String spu_search_text;
	private int offset;
	private int limit;

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getSpu_type() {
		return spu_type;
	}

	/**
	 * 有三个值,all、set、unset
	 * 
	 * @param spu_type
	 */
	public void setSpu_type(String spu_type) {
		this.spu_type = spu_type;
	}

	public String getSpu_search_text() {
		return spu_search_text;
	}

	public void setSpu_search_text(String spu_search_text) {
		this.spu_search_text = spu_search_text;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
