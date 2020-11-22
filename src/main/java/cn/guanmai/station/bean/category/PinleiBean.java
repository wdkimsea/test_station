package cn.guanmai.station.bean.category;

/* 
* @author liming 
* @date Oct 31, 2018 11:21:58 AM 
* @des 品类Bean文件
* @version 1.0 
*/
public class PinleiBean {
	private String id;
	private String name;
	private String upstream_id;

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

	public PinleiBean(String id, String name, String upstream_id) {
		this.id = id;
		this.name = name;
		this.upstream_id = upstream_id;
	}
	
	public PinleiBean() {
		super();
	}

	/**
	 * 用于创建品类的构造方法
	 * 
	 * @param name
	 * @param upstream_id
	 */
	public PinleiBean(String upstream_id, String name) {
		this.name = name;
		this.upstream_id = upstream_id;
	}

}
