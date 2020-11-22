package cn.guanmai.station.bean.invoicing;

import java.math.BigDecimal;

/**
 * Created by yangjinhai on 2019/8/12.
 */
public class StockTransferLogBean {
    private String spu_id;
    private String category_2_name;
    private BigDecimal unit_price;
    private String operator;
    private BigDecimal price;
    private String pinlei_name;
    private String in_batch_num;
    private BigDecimal out_amount;
    private String remark;
    private String out_batch_num;
    private String submit_time;
    private String sheet_no;
    private String std_unit_name;
    private String spu_name;
    private String supplier_name;
    private String category_1_name;

    public String getSpu_id() {
        return spu_id;
    }

    public void setSpu_id(String spu_id) {
        this.spu_id = spu_id;
    }

    public String getCategory_2_name() {
        return category_2_name;
    }

    public void setCategory_2_name(String category_2_name) {
        this.category_2_name = category_2_name;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPinlei_name() {
        return pinlei_name;
    }

    public void setPinlei_name(String pinlei_name) {
        this.pinlei_name = pinlei_name;
    }

    public String getIn_batch_num() {
        return in_batch_num;
    }

    public void setIn_batch_num(String in_batch_num) {
        this.in_batch_num = in_batch_num;
    }

    public BigDecimal getOut_amount() {
        return out_amount;
    }

    public void setOut_amount(BigDecimal out_amount) {
        this.out_amount = out_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOut_batch_num() {
        return out_batch_num;
    }

    public void setOut_batch_num(String out_batch_num) {
        this.out_batch_num = out_batch_num;
    }

    public String getSubmit_time() {
        return submit_time;
    }

    public void setSubmit_time(String submit_time) {
        this.submit_time = submit_time;
    }

    public String getSheet_no() {
        return sheet_no;
    }

    public void setSheet_no(String sheet_no) {
        this.sheet_no = sheet_no;
    }

    public String getStd_unit_name() {
        return std_unit_name;
    }

    public void setStd_unit_name(String std_unit_name) {
        this.std_unit_name = std_unit_name;
    }

    public String getSpu_name() {
        return spu_name;
    }

    public void setSpu_name(String spu_name) {
        this.spu_name = spu_name;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getCategory_1_name() {
        return category_1_name;
    }

    public void setCategory_1_name(String category_1_name) {
        this.category_1_name = category_1_name;
    }
}
