package cn.guanmai.open.bean.product.param;

/* 
* @author liming 
* @date Jun 3, 2019 4:12:57 PM 
* @des 新建SPU参数
* @version 1.0 
*/
public class OpenSpuCreateParam {
	private String spu_name;
	private String desc;
	private String std_unit_name;
	private String pinlei_id;
	private String dispatch_method;
	private String image;
	private String detail_images;

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
	 * @return the dispatch_method
	 */
	public String getDispatch_method() {
		return dispatch_method;
	}

	/**
	 * @param dispatch_method
	 *            the dispatch_method to set
	 */
	public void setDispatch_method(String dispatch_method) {
		this.dispatch_method = dispatch_method;
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

	/**
	 * @return the detail_images
	 */
	public String getDetail_images() {
		return detail_images;
	}

	/**
	 * @param detail_images
	 *            the detail_images to set
	 */
	public void setDetail_images(String detail_images) {
		this.detail_images = detail_images;
	}
}
