package cn.guanmai.station.bean.boxing;

import java.util.List;

/**
 * Created by abc on 2020/3/1.
 */
public class BoxingManagePrintDetailBean {
    private String address_name;
    private String box_code;
    private String box_id;
    private String box_no;
    private String order_box_status;
    private String order_id;

    private List<Details> details;

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public String getBox_no() {
        return box_no;
    }

    public void setBox_no(String box_no) {
        this.box_no = box_no;
    }

    public String getOrder_box_status() {
        return order_box_status;
    }

    public void setOrder_box_status(String order_box_status) {
        this.order_box_status = order_box_status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public class Details{
        private String sku_id;
        private String sku_name;

        public String getSku_id() {
            return sku_id;
        }

        public void setSku_id(String sku_id) {
            this.sku_id = sku_id;
        }

        public String getSku_name() {
            return sku_name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }
    }
}

