package cn.guanmai.open.bean.delivery;

/* 
* @author liming 
* @date Jun 5, 2019 5:36:03 PM 
* @des 接口 /delivery/car_model/list 对应的参数
* @version 1.0 
*/
public class OpenCarModelBean {
	private String car_model_id;
	private String car_model_name;
	private int max_load;

	/**
	 * @return the car_model_id
	 */
	public String getCar_model_id() {
		return car_model_id;
	}

	/**
	 * @param car_model_id
	 *            the car_model_id to set
	 */
	public void setCar_model_id(String car_model_id) {
		this.car_model_id = car_model_id;
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

}
