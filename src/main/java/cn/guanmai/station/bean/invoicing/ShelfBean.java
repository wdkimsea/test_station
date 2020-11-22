package cn.guanmai.station.bean.invoicing;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * 
 * @author Administrator
 *
 */
public class ShelfBean {
	private int level;
	
	@JSONField(name="shelf")
	private List<Shelf> shelfs;

	public class Shelf {
		private String shelf_id;
		private String name;
		private String parent_id;

		public String getShelf_id() {
			return shelf_id;
		}

		public void setShelf_id(String shelf_id) {
			this.shelf_id = shelf_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getParent_id() {
			return parent_id;
		}

		public void setParent_id(String parent_id) {
			this.parent_id = parent_id;
		}

	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public List<Shelf> getShelfs() {
		return shelfs;
	}

	public void setShelfs(List<Shelf> shelfs) {
		this.shelfs = shelfs;
	}

}
