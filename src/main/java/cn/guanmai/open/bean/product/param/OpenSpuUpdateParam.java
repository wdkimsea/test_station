package cn.guanmai.open.bean.product.param;

/* 
* @author liming 
* @date Jun 3, 2019 4:29:06 PM 
* @des 接口 /product/spu/update 对应的参数
* @version 1.0 
*/
public class OpenSpuUpdateParam {
	private String spu_id;
	private String spu_name;
	private String desc;
	private String pinlei_id;

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id;
	}

	/**
	 * @param spu_id
	 *            the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return spu_name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.spu_name = name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the pinlei_id
	 */
	public String getPinlei_id() {
		return pinlei_id;
	}

	/**
	 * @param pinlei_id
	 *            the pinlei_id to set
	 */
	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

}
