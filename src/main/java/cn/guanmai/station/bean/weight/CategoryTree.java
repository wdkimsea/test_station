package cn.guanmai.station.bean.weight;

import java.util.List;

/* 
* @author liming 
* @date Apr 15, 2019 5:36:48 PM 
* @des 老版本称重软件 {/station/weigh/what_can_i_do} 对应的结果
* @version 1.0 
*/
public class CategoryTree {
	private String id;
	private String name;
	private List<Category2> list;

	/**
	 * 一级分类ID
	 * 
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
	 * 一级分类名称
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 二级分类列表
	 * 
	 * @return the list
	 */
	public List<Category2> getList() {
		return list;
	}

	/**
	 * @param list
	 *            the list to set
	 */
	public void setList(List<Category2> list) {
		this.list = list;
	}

	public class Category2 {
		private String id;
		private String name;
		private String text;

		/**
		 * 二级分类ID
		 * 
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
		 * 二级分类名称
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(String text) {
			this.text = text;
		}

	}

}
