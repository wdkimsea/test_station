package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/* 
* @author liming 
* @date Jun 20, 2019 7:50:17 PM 
* @des 分拣标签模板
* @version 1.0 
*/
public class PrintTagTemplateBean {
	private String id;
	private String create_time;
	private boolean is_default;
	private Object address_ids;
	private Object spu_ids;

	private Content content;

	public static class Content {
		private JSONArray blocks;
		private String name;
		private JSONObject page;

		/**
		 * @return the blocks
		 */
		public JSONArray getBlocks() {
			return blocks;
		}

		/**
		 * @param blocks the blocks to set
		 */
		public void setBlocks(JSONArray blocks) {
			this.blocks = blocks;
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
		 * @return the page
		 */
		public JSONObject getPage() {
			return page;
		}

		/**
		 * @param page the page to set
		 */
		public void setPage(JSONObject page) {
			this.page = page;
		}

		public Content() {
			super();
		}

	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the is_default
	 */
	public boolean isIs_default() {
		return is_default;
	}

	/**
	 * @param is_default the is_default to set
	 */
	public void setIs_default(boolean is_default) {
		this.is_default = is_default;
	}

	public Object getAddress_ids() {
		return address_ids;
	}

	public void setAddress_ids(String address_ids) {
		this.address_ids = address_ids;
	}

	public Object getSpu_ids() {
		return spu_ids;
	}

	public void setSpu_ids(String spu_ids) {
		this.spu_ids = spu_ids;
	}

	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}

	public PrintTagTemplateBean() {
		super();
	}

}
