package cn.guanmai.station.bean.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/* 
* @author liming 
* @date Jun 20, 2019 7:23:16 PM 
* @des 采购模板
* @version 1.0 
*/
public class PurchaseTemplateBean {
	private String id;
	private boolean is_default;
	private String create_time;

	private Content content;

	public static class Content {
		private String name;
		private JSONObject header;
		private JSONArray contents;
		private JSONObject footer;
		private JSONObject page;
		private JSONObject sign;

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
		 * @return the header
		 */
		public JSONObject getHeader() {
			return header;
		}

		/**
		 * @param header the header to set
		 */
		public void setHeader(JSONObject header) {
			this.header = header;
		}

		/**
		 * @return the contents
		 */
		public JSONArray getContents() {
			return contents;
		}

		/**
		 * @param contents the contents to set
		 */
		public void setContents(JSONArray contents) {
			this.contents = contents;
		}

		/**
		 * @return the footer
		 */
		public JSONObject getFooter() {
			return footer;
		}

		/**
		 * @param footer the footer to set
		 */
		public void setFooter(JSONObject footer) {
			this.footer = footer;
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

		/**
		 * @return the sign
		 */
		public JSONObject getSign() {
			return sign;
		}

		/**
		 * @param sign the sign to set
		 */
		public void setSign(JSONObject sign) {
			this.sign = sign;
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

	public PurchaseTemplateBean() {
		super();
	}

}
