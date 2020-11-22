package cn.guanmai.station.bean.delivery;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/* 
* @author liming 
* @date Jan 4, 2019 10:26:40 AM 
* @des 车型
* @version 1.0 
*/
public class CarModelBean {
	private BigDecimal id;
	@JSONField(name="name")
	private String car_model_name;
	private int max_load;

	/**
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * @return the car_model_name
	 */
	public String getCar_model_name() {
		return car_model_name;
	}

	/**
	 * @param car_model_name
	 *            the car_model_name to set
	 */
	public void setCar_model_name(String car_model_name) {
		this.car_model_name = car_model_name;
	}

	/**
	 * @return the max_load
	 */
	public int getMax_load() {
		return max_load;
	}

	/**
	 * @param max_load
	 *            the max_load to set
	 */
	public void setMax_load(int max_load) {
		this.max_load = max_load;
	}

	/**
	 * 添加车型使用到的构造方法
	 * 
	 * @param car_model_name
	 * @param max_load
	 */
	public CarModelBean(String car_model_name, int max_load) {
		super();
		this.car_model_name = car_model_name;
		this.max_load = max_load;
	}

}
