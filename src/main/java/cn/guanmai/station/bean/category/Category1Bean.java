package cn.guanmai.station.bean.category;

/* 
* @author liming 
* @date Oct 31, 2018 11:20:01 AM 
* @des 一级分类Bean文件
* @version 1.0 
*/
public class Category1Bean {
	private String id;
	private String name;
	private int icon;
	private int rank;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public Category1Bean() {
		super();
	}

	/**
	 * 用于修改一级分类的构造方法
	 * 
	 * @param id
	 * @param name
	 * @param icon
	 * @param rank
	 */
	public Category1Bean(String id, String name, int icon, int rank) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.rank = rank;
	}

	/**
	 * 用于创建一级分类的构造方法
	 * 
	 * @param name
	 * @param icon
	 */
	public Category1Bean(String name, int icon) {
		this.name = name;
		this.icon = icon;
	}

}
