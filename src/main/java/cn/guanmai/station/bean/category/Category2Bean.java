package cn.guanmai.station.bean.category;

/* 
* @author liming 
* @date Oct 31, 2018 11:20:58 AM 
* @des 二级分类Bean文件
* @version 1.0 
*/
public class Category2Bean {
	private String id;
	private String name;
	private String upstream_id;
	private Integer rank;

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

	public String getUpstream_id() {
		return upstream_id;
	}

	public void setUpstream_id(String upstream_id) {
		this.upstream_id = upstream_id;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public Category2Bean() {
		super();
	}

	public Category2Bean(String id, String name, String upstream_id, Integer rank) {
		this.id = id;
		this.name = name;
		this.upstream_id = upstream_id;
		this.rank = rank;
	}

	/**
	 * 用于创建二级分类的构造方法
	 * 
	 * @param name
	 * @param upstream_id
	 */
	public Category2Bean(String upstream_id, String name) {
		this.name = name;
		this.upstream_id = upstream_id;
	}

}
