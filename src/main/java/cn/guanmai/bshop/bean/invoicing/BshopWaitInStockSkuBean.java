package cn.guanmai.bshop.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/19.
 */
public class BshopWaitInStockSkuBean {
    private String std_unit_name;
    private BigDecimal std_unit_price;
    private String spu_id;
    private String station_id;
    private String order_id;
    private String sku_name;
    private String group_id;
    private String address_id;
    private String sku_id;
    private String order_status;
    private String order_time;
    private BigDecimal std_quantity;

    public String getStd_unit_name() {
        return std_unit_name;
    }

    public void setStd_unit_name(String std_unit_name) {
        this.std_unit_name = std_unit_name;
    }

    public BigDecimal getStd_unit_price() {
        return std_unit_price;
    }

    public void setStd_unit_price(BigDecimal std_unit_price) {
        this.std_unit_price = std_unit_price;
    }

    public String getSpu_id() {
        return spu_id;
    }

    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSku_name() {
        return sku_name;
    }

    public void setSku_name(String sku_name) {
        this.sku_name = sku_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getSku_id() {
        return sku_id;
    }

    public void setSku_id(String sku_id) {
        this.sku_id = sku_id;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public BigDecimal getStd_quantity() {
        return std_quantity;
    }

    public void setStd_quantity(BigDecimal std_quantity) {
        this.std_quantity = std_quantity;
    }
}
