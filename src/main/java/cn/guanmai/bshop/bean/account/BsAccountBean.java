package cn.guanmai.bshop.bean.account;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Apr 19, 2019 6:00:32 PM 
* @des 账号信息
* @version 1.0 
*/
public class BsAccountBean {
	@JSONField(name="id")
	private String kid;
	private String username;
	private String station_id;
	private String salemenu_id;

	private BigDecimal balance;

	private List<Address> addresses;

	/**
	 * @return the kid
	 */
	public String getKid() {
		return kid;
	}

	/**
	 * @param kid the kid to set
	 */
	public void setKid(String kid) {
		this.kid = kid;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the station_id
	 */
	public String getStation_id() {
		return station_id;
	}

	/**
	 * @param station_id the station_id to set
	 */
	public void setStation_id(String station_id) {
		this.station_id = station_id;
	}

	/**
	 * @return the salemenu_id
	 */
	public String getSalemenu_id() {
		return salemenu_id;
	}

	/**
	 * @param salemenu_id the salemenu_id to set
	 */
	public void setSalemenu_id(String salemenu_id) {
		this.salemenu_id = salemenu_id;
	}

	/**
	 * @return the addresses
	 */
	public List<Address> getAddresses() {
		return addresses;
	}

	/**
	 * @param addresses the addresses to set
	 */
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance.divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP, 2);
	}

	public class Address {
		@JSONField(name="id")
		private String sid;
		private String resname;
		private List<CmsInfo> cms_info;

		/**
		 * @return the sid
		 */
		public String getSid() {
			return sid;
		}

		/**
		 * @param sid the sid to set
		 */
		public void setSid(String sid) {
			this.sid = sid;
		}

		/**
		 * @return the resname
		 */
		public String getResname() {
			return resname;
		}

		/**
		 * @param resname the resname to set
		 */
		public void setResname(String resname) {
			this.resname = resname;
		}

		/**
		 * @return the cms_info
		 */
		public List<CmsInfo> getCms_info() {
			return cms_info;
		}

		/**
		 * @param cms_info the cms_info to set
		 */
		public void setCms_info(List<CmsInfo> cms_info) {
			this.cms_info = cms_info;
		}

		public class CmsInfo {
			private String key;
			private String title;

			/**
			 * @return the key
			 */
			public String getKey() {
				return key;
			}

			/**
			 * @param key the key to set
			 */
			public void setKey(String key) {
				this.key = key;
			}

			/**
			 * @return the title
			 */
			public String getTitle() {
				return title;
			}

			/**
			 * @param title the title to set
			 */
			public void setTitle(String title) {
				this.title = title;
			}

		}
	}

}
