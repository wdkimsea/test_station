package cn.guanmai.station.bean.delivery;

/**
 * Created by yangjinhai on 2019/8/26.
 */
public class PickUpStationBean {
    String name;
    String principal;
    String phone;
    String district_code;
    String area_l1;
    String area_l2;
    String address;
    String business_status;
    String id;
    String geographic_label;


    public PickUpStationBean() {
    }

    /**
     * 新建城市的站点
     * @param name
     * @param principal
     * @param phone
     * @param district_code
     * @param address
     * @param business_status
     */
    public PickUpStationBean(String name, String principal, String phone, String district_code,
        String address, String business_status) {
        this.name = name;
        this.principal = principal;
        this.phone = phone;
        this.district_code = district_code;
        this.address = address;
        this.business_status = business_status;
    }

    public PickUpStationBean(String name, String principal, String phone, String district_code,
        String area_l1, String business_status, String address) {
        this.name = name;
        this.principal = principal;
        this.phone = phone;
        this.district_code = district_code;
        this.area_l1 = area_l1;
        this.business_status = business_status;
        this.address = address;
    }

    public PickUpStationBean(String name, String principal, String phone, String district_code,
        String area_l1, String area_l2, String business_status, String address) {
        this.name = name;
        this.principal = principal;
        this.phone = phone;
        this.district_code = district_code;
        this.area_l1 = area_l1;
        this.area_l2 = area_l2;
        this.business_status = business_status;
        this.address = address;
    }


    public String getDistrict_code() {
        return district_code;
    }

    public void setDistrict_code(String district_code) {
        this.district_code = district_code;
    }


    public String getArea_l1() {
        return area_l1;
    }

    public void setArea_l1(String area_l1) {
        this.area_l1 = area_l1;
    }

    public String getArea_l2() {
        return area_l2;
    }

    public void setArea_l2(String area_l2) {
        this.area_l2 = area_l2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business_status) {
        this.business_status = business_status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeographic_label() {
        return geographic_label;
    }

    public void setGeographic_label(String geographic_label) {
        this.geographic_label = geographic_label;
    }
}
