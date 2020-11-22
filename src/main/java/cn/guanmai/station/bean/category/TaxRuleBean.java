package cn.guanmai.station.bean.category;

import java.math.BigDecimal;
import java.util.List;

/* 
* @author liming 
* @date Feb 18, 2019 7:19:18 PM 
* @des 税率规则封装类
* @version 1.0 
*/
public class TaxRuleBean {
	private String tax_rule_id;
	private String tax_rule_name;
	private int spu_count;
	private int address_count;
	private int status;

	public List<Address> address;

	public class Address {
		private BigDecimal address_id;
		private String address_name;
		private String tax_rate_id;

		/**
		 * @return the address_id
		 */
		public BigDecimal getAddress_id() {
			return address_id;
		}

		/**
		 * @param address_id the address_id to set
		 */
		public void setAddress_id(BigDecimal address_id) {
			this.address_id = address_id;
		}

		/**
		 * @return the address_name
		 */
		public String getAddress_name() {
			return address_name;
		}

		/**
		 * @param address_name the address_name to set
		 */
		public void setAddress_name(String address_name) {
			this.address_name = address_name;
		}

		public String getTax_rate_id() {
			return tax_rate_id;
		}

		public void setTax_rate_id(String tax_rate_id) {
			this.tax_rate_id = tax_rate_id;
		}

	}

	public List<Spu> spu;

	public class Spu {
		private String spu_id;
		private String spu_name;
		private BigDecimal tax_rate;
		private Integer tax_rate_id;

		/**
		 * @return the spu_id
		 */
		public String getSpu_id() {
			return spu_id;
		}

		/**
		 * @param spu_id the spu_id to set
		 */
		public void setSpu_id(String spu_id) {
			this.spu_id = spu_id;
		}

		/**
		 * @return the spu_name
		 */
		public String getSpu_name() {
			return spu_name;
		}

		/**
		 * @param spu_name the spu_name to set
		 */
		public void setSpu_name(String spu_name) {
			this.spu_name = spu_name;
		}

		/**
		 * @return the tax_rate
		 */
		public BigDecimal getTax_rate() {
			return tax_rate;
		}

		/**
		 * @param tax_rate the tax_rate to set
		 */
		public void setTax_rate(BigDecimal tax_rate) {
			this.tax_rate = tax_rate;
		}

		/**
		 * @return the tax_rate_id
		 */
		public Integer getTax_rate_id() {
			return tax_rate_id;
		}

		/**
		 * @param tax_rate_id the tax_rate_id to set
		 */
		public void setTax_rate_id(Integer tax_rate_id) {
			this.tax_rate_id = tax_rate_id;
		}

	}

	/**
	 * @return the tax_rule_id
	 */
	public String getTax_rule_id() {
		return tax_rule_id;
	}

	/**
	 * @param tax_rule_id the tax_rule_id to set
	 */
	public void setTax_rule_id(String tax_rule_id) {
		this.tax_rule_id = tax_rule_id;
	}

	/**
	 * @return the tax_rule_name
	 */
	public String getTax_rule_name() {
		return tax_rule_name;
	}

	/**
	 * @param tax_rule_name the tax_rule_name to set
	 */
	public void setTax_rule_name(String tax_rule_name) {
		this.tax_rule_name = tax_rule_name;
	}

	/**
	 * @return the spu_count
	 */
	public int getSpu_count() {
		return spu_count;
	}

	/**
	 * @param spu_count the spu_count to set
	 */
	public void setSpu_count(int spu_count) {
		this.spu_count = spu_count;
	}

	/**
	 * @return the address_count
	 */
	public int getAddress_count() {
		return address_count;
	}

	/**
	 * @param address_count the address_count to set
	 */
	public void setAddress_count(int address_count) {
		this.address_count = address_count;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the address
	 */
	public List<Address> getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(List<Address> address) {
		this.address = address;
	}

	/**
	 * @return the spu
	 */
	public List<Spu> getSpu() {
		return spu;
	}

	/**
	 * @param spu the spu to set
	 */
	public void setSpu(List<Spu> spu) {
		this.spu = spu;
	}

	public TaxRuleBean() {
	}

	/**
	 * 用来创建税率规则的构造方法
	 * 
	 * @param tax_rule_name
	 * @param status
	 * @param address
	 * @param spu
	 */
	public TaxRuleBean(String tax_rule_name, int status, List<Address> address, List<Spu> spu) {
		super();
		this.tax_rule_name = tax_rule_name;
		this.status = status;
		this.address = address;
		this.spu = spu;
	}

	/**
	 * 修改税率规则的构造方法
	 * 
	 * @param tax_rule_id
	 * @param tax_rule_name
	 * @param status
	 * @param address
	 * @param spu
	 */
	public TaxRuleBean(String tax_rule_id, String tax_rule_name, int status, List<Address> address, List<Spu> spu) {
		super();
		this.tax_rule_id = tax_rule_id;
		this.tax_rule_name = tax_rule_name;
		this.status = status;
		this.address = address;
		this.spu = spu;
	}

}
