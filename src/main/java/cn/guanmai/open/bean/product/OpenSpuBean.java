package cn.guanmai.open.bean.product;

import java.util.List;

/* 
* @author liming 
* @date Jun 3, 2019 4:17:56 PM 
* @des  接口 /product/spu/list 对应的结果
* @version 1.0 
*/
public class OpenSpuBean {
	private String spu_id;
	private String spu_name;
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String std_unit_name;
	private String desc;
	private int p_type;
	private List<String> detail_images;
	private String image;

	/**
	 * @return the id
	 */
	public String getId() {
		return spu_id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.spu_id = id;
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

	/**
	 * @return the category2_id
	 */
	public String getCategory2_id() {
		return category2_id;
	}

	/**
	 * @param category2_id
	 *            the category2_id to set
	 */
	public void setCategory2_id(String category2_id) {
		this.category2_id = category2_id;
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

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name
	 *            the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
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
	 * @return the p_type
	 */
	public int getP_type() {
		return p_type;
	}

	/**
	 * @param p_type
	 *            the p_type to set
	 */
	public void setP_type(int p_type) {
		this.p_type = p_type;
	}

	/**
	 * @return the detail_images
	 */
	public List<String> getDetail_images() {
		return detail_images;
	}

	/**
	 * @param detail_images
	 *            the detail_images to set
	 */
	public void setDetail_images(List<String> detail_images) {
		this.detail_images = detail_images;
	}

	/**
	 * @return the image
	 */
	public String getImage() {
		return image;
	}

	/**
	 * @param image
	 *            the image to set
	 */
	public void setImage(String image) {
		this.image = image;
	}
}
