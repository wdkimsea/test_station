package cn.guanmai.bshop.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/21.
 */
public class StockDetailsBean {
    BigDecimal amount;
    String op_time;
    String op_type;
    BigDecimal std_quantity;
    String std_unit_price;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOp_time() {
        return op_time;
    }

    public void setOp_time(String op_time) {
        this.op_time = op_time;
    }

    public String getOp_type() {
        return op_type;
    }

    public void setOp_type(String op_type) {
        this.op_type = op_type;
    }

    public BigDecimal getStd_quantity() {
        return std_quantity;
    }

    public void setStd_quantity(BigDecimal std_quantity) {
        this.std_quantity = std_quantity;
    }

    public String getStd_unit_price() {
        return std_unit_price;
    }

    public void setStd_unit_price(String std_unit_price) {
        this.std_unit_price = std_unit_price;
    }
}
