package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/22.
 */
public class CustomerStockBean {
    String address_id;
    String address_name;
    BigDecimal stock_value;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public BigDecimal getStock_value() {
        return stock_value;
    }

    public void setStock_value(BigDecimal stock_value) {
        this.stock_value = stock_value;
    }
}
