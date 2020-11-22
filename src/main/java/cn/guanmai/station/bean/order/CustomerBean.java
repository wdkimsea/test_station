package cn.guanmai.station.bean.order;

/* 
* @author liming 
* @date Nov 12, 2018 10:02:34 AM 
* @des 下单商户类
* @version 1.0 
*/
public class CustomerBean {
	private String address;
	private String address_id;
	private String address_sign_id;
	private String create_time;
	private int finance_status;
	private String id;
	private int keycustomer;
	private String lat;
	private String lng;
	private String map_address;
	private String receiver_name;
	private String receiver_phone;
	private String resname;
	private String service_station_id;
	private String username;

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the address_id
	 */
	public String getAddress_id() {
		return address_id;
	}

	/**
	 * @param address_id
	 *            the address_id to set
	 */
	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	/**
	 * @return the address_sign_id
	 */
	public String getAddress_sign_id() {
		return address_sign_id;
	}

	/**
	 * @param address_sign_id
	 *            the address_sign_id to set
	 */
	public void setAddress_sign_id(String address_sign_id) {
		this.address_sign_id = address_sign_id;
	}

	/**
	 * @return the create_time
	 */
	public String getCreate_time() {
		return create_time;
	}

	/**
	 * @param create_time
	 *            the create_time to set
	 */
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	/**
	 * @return the finance_status
	 */
	public int getFinance_status() {
		return finance_status;
	}

	/**
	 * @param finance_status
	 *            the finance_status to set
	 */
	public void setFinance_status(int finance_status) {
		this.finance_status = finance_status;
	}

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
	 * @return the keycustomer
	 */
	public int getKeycustomer() {
		return keycustomer;
	}

	/**
	 * @param keycustomer
	 *            the keycustomer to set
	 */
	public void setKeycustomer(int keycustomer) {
		this.keycustomer = keycustomer;
	}

	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * @return the map_address
	 */
	public String getMap_address() {
		return map_address;
	}

	/**
	 * @param map_address
	 *            the map_address to set
	 */
	public void setMap_address(String map_address) {
		this.map_address = map_address;
	}

	/**
	 * @return the receiver_name
	 */
	public String getReceiver_name() {
		return receiver_name;
	}

	/**
	 * @param receiver_name
	 *            the receiver_name to set
	 */
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}

	/**
	 * @return the receiver_phone
	 */
	public String getReceiver_phone() {
		return receiver_phone;
	}

	/**
	 * @param receiver_phone
	 *            the receiver_phone to set
	 */
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}

	/**
	 * @return the resname
	 */
	public String getResname() {
		return resname;
	}

	/**
	 * @param resname
	 *            the resname to set
	 */
	public void setResname(String resname) {
		this.resname = resname;
	}

	/**
	 * @return the service_station_id
	 */
	public String getService_station_id() {
		return service_station_id;
	}

	/**
	 * @param service_station_id
	 *            the service_station_id to set
	 */
	public void setService_station_id(String service_station_id) {
		this.service_station_id = service_station_id;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public CustomerBean(String address, String address_id, String address_sign_id, String create_time,
			int finance_status, String id, int keycustomer, String lat, String lng, String map_address,
			String receiver_name, String receiver_phone, String resname, String service_station_id, String username) {
		super();
		this.address = address;
		this.address_id = address_id;
		this.address_sign_id = address_sign_id;
		this.create_time = create_time;
		this.finance_status = finance_status;
		this.id = id;
		this.keycustomer = keycustomer;
		this.lat = lat;
		this.lng = lng;
		this.map_address = map_address;
		this.receiver_name = receiver_name;
		this.receiver_phone = receiver_phone;
		this.resname = resname;
		this.service_station_id = service_station_id;
		this.username = username;
	}

}
