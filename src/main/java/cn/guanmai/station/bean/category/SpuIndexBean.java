package cn.guanmai.station.bean.category;

import java.util.List;

/* 
* @author liming 
* @date Apr 8, 2019 6:43:43 PM 
* @des 商品库
* @version 1.0 
*/
public class SpuIndexBean {
	private String category_name_1;
	private String category_name_2;
	private String dispatch_method;
	private String image;
	private int p_type;
	private String pinlei_name;
	private String spu_id;
	private String spu_name;
	private String std_unit_name;

	private List<SkuBean> skus;

	/**
	 * @return the category_name_1
	 */
	public String getCategory_name_1() {
		return category_name_1;
	}

	/**
	 * @param category_name_1
	 *            the category_name_1 to set
	 */
	public void setCategory_name_1(String category_name_1) {
		this.category_name_1 = category_name_1;
	}

	/**
	 * @return the category_name_2
	 */
	public String getCategory_name_2() {
		return category_name_2;
	}

	/**
	 * @param category_name_2
	 *            the category_name_2 to set
	 */
	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
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
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name
	 *            the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

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
	 * @return the spu_name
	 */
	public String getSpu_name() {
		return spu_name;
	}

	/**
	 * @param spu_name
	 *            the spu_name to set
	 */
	public void setSpu_name(String spu_name) {
		this.spu_name = spu_name;
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
	 * @return the skus
	 */
	public List<SkuBean> getSkus() {
		return skus;
	}

	/**
	 * @param skus
	 *            the skus to set
	 */
	public void setSkus(List<SkuBean> skus) {
		this.skus = skus;
	}

}
