package cn.guanmai.station.bean.boxing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by abc on 2020/2/22.
 */
public class BoxingOrderDetailBean {

    private String address_id;
    private String address_name;
    private String order_box_num;
    private String order_box_status;
    private String order_id;
    private String route_name;
    private String total;
    private String service_time_period;
    private String driver_name;
    private List<Details> details;

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

    public String getOrder_box_num() {
        return order_box_num;
    }

    public void setOrder_box_num(String order_box_num) {
        this.order_box_num = order_box_num;
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

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getService_time_period() {
        return service_time_period;
    }

    public void setService_time_period(String service_time_period) {
        this.service_time_period = service_time_period;
    }

    public String getDriver_name() {
        return driver_name;
    }

    public void setDriver_name(String driver_name) {
        this.driver_name = driver_name;
    }

    public List<Details> getDetails() {
        return details;
    }

    public void setDetails(List<Details> details) {
        this.details = details;
    }

    public class Details{
        private BigDecimal outstock_quantity;
        private BigDecimal quantity;
        private String package_id;
        private String sku_box_status;
        private String sku_id;
        private List<Box_list> box_list;
        private String sku_name;

        public BigDecimal getOutstock_quantity() {
            return outstock_quantity;
        }

        public void setOutstock_quantity(BigDecimal outstock_quantity) {
            this.outstock_quantity = outstock_quantity;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public String getPackage_id() {
            return package_id;
        }

        public void setPackage_id(String package_id) {
            this.package_id = package_id;
        }

        public String getSku_box_status() {
            return sku_box_status;
        }

        public void setSku_box_status(String sku_box_status) {
            this.sku_box_status = sku_box_status;
        }

        public String getSku_id() {
            return sku_id;
        }

        public void setSku_id(String sku_id) {
            this.sku_id = sku_id;
        }

        public List<Box_list> getBox_list() {
            return box_list;
        }

        public void setBox_list(List<Box_list> box_list) {
            this.box_list = box_list;
        }

        public String getSku_name() {
            return sku_name;
        }

        public void setSku_name(String sku_name) {
            this.sku_name = sku_name;
        }

        public class Box_list{
            private String box_code;
            private String box_id;
            private String box_no;

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


        }
    }

}
