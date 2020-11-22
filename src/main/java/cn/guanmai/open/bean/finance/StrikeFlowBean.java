package cn.guanmai.open.bean.finance;

/* 
* @author liming 
* @date Jun 6, 2019 11:07:59 AM 
* @des 接口 /finance/strike_flow/list 对应的结果
* @version 1.0 
*/
public class StrikeFlowBean {
	private String district_code;
//	private String update_people;
	private String deal_code;
	private String operator;
	private String telephone;

	/**
	 * @return the district_code
	 */
	public String getDistrict_code() {
		return district_code;
	}

	/**
	 * @param district_code
	 *            the district_code to set
	 */
	public void setDistrict_code(String district_code) {
		this.district_code = district_code;
	}

//	/**
//	 * @return the update_people
//	 */
//	public String getUpdate_people() {
//		return update_people;
//	}
//
//	/**
//	 * @param update_people
//	 *            the update_people to set
//	 */
//	public void setUpdate_people(String update_people) {
//		this.update_people = update_people;
//	}

	/**
	 * @return the deal_code
	 */
	public String getDeal_code() {
		return deal_code;
	}

	/**
	 * @param deal_code
	 *            the deal_code to set
	 */
	public void setDeal_code(String deal_code) {
		this.deal_code = deal_code;
	}

	/**
	 * @return the strike_people
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the strike_people to set
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
