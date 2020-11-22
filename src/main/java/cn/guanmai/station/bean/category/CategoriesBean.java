package cn.guanmai.station.bean.category;

/* 
* @author liming 
* @date May 20, 2019 2:45:19 PM 
* @des 站点一二级分类信息接口
* @version 1.0 
*/
public class CategoriesBean {
	// 分类所属group_id
	private Integer group_id;

	// 一级分类特有的图标ID值
	private Integer icon;

	private String id;

	private String title;

	private Integer level;

	// 非一级分类特有的属性值,上级分类ID
	private String upstream_id;

	/**
	 * @return the group_id
	 */
	public Integer getGroup_id() {
		return group_id;
	}

	/**
	 * @param group_id
	 *            the group_id to set
	 */
	public void setGroup_id(Integer group_id) {
		this.group_id = group_id;
	}

	/**
	 * @return the icon
	 */
	public Integer getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            the icon to set
	 */
	public void setIcon(Integer icon) {
		this.icon = icon;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param level
	 *            the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}

	/**
	 * @return the upstream_id
	 */
	public String getUpstream_id() {
		return upstream_id;
	}

	/**
	 * @param upstream_id
	 *            the upstream_id to set
	 */
	public void setUpstream_id(String upstream_id) {
		this.upstream_id = upstream_id;
	}

}
