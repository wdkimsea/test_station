package cn.guanmai.station.bean.weight;

import java.math.BigDecimal;

/* 
* @author liming 
* @date Apr 11, 2019 4:24:07 PM 
* @des 新版称重-称重框
* @version 1.0 
*/
public class WeightBasketBean {
	private String id;
	private String name;
	private BigDecimal basket_weight;

	/**
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
	 * @return the basket_weight
	 */
	public BigDecimal getBasket_weight() {
		return basket_weight;
	}

	/**
	 * @param basket_weight
	 *            the basket_weight to set
	 */
	public void setBasket_weight(BigDecimal basket_weight) {
		this.basket_weight = basket_weight;
	}

	public WeightBasketBean() {
		super();
	}

	/**
	 * 新建称重框使用的构造方法
	 * 
	 * @param name
	 * @param basket_weight
	 */
	public WeightBasketBean(String name, BigDecimal basket_weight) {
		super();
		this.name = name;
		this.basket_weight = basket_weight;
	}

}
