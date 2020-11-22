package cn.guanmai.station.bean.category;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.annotation.JSONField;

import cn.guanmai.util.IntTypeAdapter;

/* 
* @author liming 
* @date Oct 31, 2018 11:22:22 AM 
* @des SPU Bean文件
* @version 1.0 
*/
public class SpuBean {
	private String id;
	private String spu_id;
	private String name;
	private String category_id_1;
	private String category_name_1;
	private String category_id_2;
	private String category_name_2;
	private String pinlei_id;
	private String pinlei_name;
	private String desc;
	private JSONArray images;
	private String std_unit_name;
	private String cms_key;
	private JSONArray alias;
	private JSONArray image_list;

	// 0 代表通用;1代表本站
	private int p_type;

	// 1 代表八卦;2代表统配
	private int dispatch_method;

	// 0 代表不显示;1代表显示
	@JSONField(serializeUsing = IntTypeAdapter.class)
	private int need_pesticide_detect;

	/**
	 * @return the id
	 */
	public String getId() {
		return id == null ? spu_id : id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the spu_id
	 */
	public String getSpu_id() {
		return spu_id == null ? id : spu_id;
	}

	/**
	 * @param spu_id the spu_id to set
	 */
	public void setSpu_id(String spu_id) {
		this.spu_id = spu_id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the category_id_1
	 */
	public String getCategory_id_1() {
		return category_id_1;
	}

	/**
	 * @param category_id_1 the category_id_1 to set
	 */
	public void setCategory_id_1(String category_id_1) {
		this.category_id_1 = category_id_1;
	}

	/**
	 * @return the category_name_1
	 */
	public String getCategory_name_1() {
		return category_name_1;
	}

	/**
	 * @param category_name_1 the category_name_1 to set
	 */
	public void setCategory_name_1(String category_name_1) {
		this.category_name_1 = category_name_1;
	}

	/**
	 * @return the category_id_2
	 */
	public String getCategory_id_2() {
		return category_id_2;
	}

	/**
	 * @param category_id_2 the category_id_2 to set
	 */
	public void setCategory_id_2(String category_id_2) {
		this.category_id_2 = category_id_2;
	}

	/**
	 * @return the category_name_2
	 */
	public String getCategory_name_2() {
		return category_name_2;
	}

	/**
	 * @param category_name_2 the category_name_2 to set
	 */
	public void setCategory_name_2(String category_name_2) {
		this.category_name_2 = category_name_2;
	}

	/**
	 * @return the pinlei_id
	 */
	public String getPinlei_id() {
		return pinlei_id;
	}

	/**
	 * @param pinlei_id the pinlei_id to set
	 */
	public void setPinlei_id(String pinlei_id) {
		this.pinlei_id = pinlei_id;
	}

	/**
	 * @return the pinlei_name
	 */
	public String getPinlei_name() {
		return pinlei_name;
	}

	/**
	 * @param pinlei_name the pinlei_name to set
	 */
	public void setPinlei_name(String pinlei_name) {
		this.pinlei_name = pinlei_name;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the images
	 */
	public JSONArray getImages() {
		return images;
	}

	/**
	 * @param images the images to set
	 */
	public void setImages(JSONArray images) {
		this.images = images;
	}

	/**
	 * @return the std_unit_name
	 */
	public String getStd_unit_name() {
		return std_unit_name;
	}

	/**
	 * @param std_unit_name the std_unit_name to set
	 */
	public void setStd_unit_name(String std_unit_name) {
		this.std_unit_name = std_unit_name;
	}

	/**
	 * @return the cms_key
	 */
	public String getCms_key() {
		return cms_key;
	}

	/**
	 * @param cms_key the cms_key to set
	 */
	public void setCms_key(String cms_key) {
		this.cms_key = cms_key;
	}

	/**
	 * @return the alias
	 */
	public JSONArray getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(JSONArray alias) {
		this.alias = alias;
	}

	public JSONArray getImage_list() {
		return image_list;
	}

	public void setImage_list(JSONArray image_list) {
		this.image_list = image_list;
	}

	/**
	 * @return the p_type
	 */
	public int getP_type() {
		return p_type;
	}

	/**
	 * @param p_type the p_type to set
	 */
	public void setP_type(int p_type) {
		this.p_type = p_type;
	}

	/**
	 * @return the dispatch_method
	 */
	public int getDispatch_method() {
		return dispatch_method;
	}

	/**
	 * @param dispatch_method the dispatch_method to set
	 */
	public void setDispatch_method(int dispatch_method) {
		this.dispatch_method = dispatch_method;
	}

	/**
	 * @return the need_pesticide_detect
	 */
	public int getNeed_pesticide_detect() {
		return need_pesticide_detect;
	}

	/**
	 * @param need_pesticide_detect the need_pesticide_detect to set
	 */
	public void setNeed_pesticide_detect(int need_pesticide_detect) {
		this.need_pesticide_detect = need_pesticide_detect;
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param category_id_1
	 * @param category_name_1
	 * @param category_id_2
	 * @param category_name_2
	 * @param pinlei_id
	 * @param pinlei_name
	 * @param desc
	 * @param images
	 * @param std_unit_name
	 * @param cms_key
	 * @param alias
	 * @param detail_images
	 * @param p_type
	 * @param dispatch_method
	 * @param need_pesticide_detect
	 */
	public SpuBean(String id, String name, String category_id_1, String category_name_1, String category_id_2,
			String category_name_2, String pinlei_id, String pinlei_name, String desc, JSONArray images,
			String std_unit_name, String cms_key, JSONArray alias, JSONArray image_list, int p_type,
			int dispatch_method, int need_pesticide_detect) {
		super();
		this.id = id;
		this.name = name;
		this.category_id_1 = category_id_1;
		this.category_name_1 = category_name_1;
		this.category_id_2 = category_id_2;
		this.category_name_2 = category_name_2;
		this.pinlei_id = pinlei_id;
		this.pinlei_name = pinlei_name;
		this.desc = desc;
		this.images = images;
		this.std_unit_name = std_unit_name;
		this.cms_key = cms_key;
		this.alias = alias;
		this.image_list = image_list;
		this.p_type = p_type;
		this.dispatch_method = dispatch_method;
		this.need_pesticide_detect = need_pesticide_detect;
	}

	public SpuBean() {
		super();
	}

	/**
	 * 用于创建SPU
	 * 
	 * @param name
	 * @param pinlei_id
	 * @param desc
	 * @param image
	 * @param p_type
	 * @param std_unit_name
	 * @param alias
	 * @param dispatch_method
	 * @param need_pesticide_detect
	 */
	public SpuBean(String name, String pinlei_id, String desc, JSONArray images, int p_type, String std_unit_name,
			JSONArray alias, int dispatch_method, int need_pesticide_detect) {
		this.name = name;
		this.pinlei_id = pinlei_id;
		this.desc = desc;
		this.images = images;
		this.std_unit_name = std_unit_name;
		this.alias = alias;
		this.p_type = p_type;
		this.dispatch_method = dispatch_method;
		this.need_pesticide_detect = need_pesticide_detect;
	}

}
