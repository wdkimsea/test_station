package cn.guanmai.station.bean.boxing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by abc on 2020/2/20.
 */
public class BoxingOrderListBean {
    private BigDecimal unbox_num;
    private BigDecimal total;
    private List<Orders> orders;

    public BigDecimal getUnbox_num() {
        return unbox_num;
    }

    public void setUnbox_num(BigDecimal unbox_num) {
        this.unbox_num = unbox_num;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<Orders> getOrders() {
        return orders;
    }

    public void setOrders(List<Orders> orders) {
        this.orders = orders;
    }

    public class Orders{
        private String address_id;
        private String address_name;
        private String order_box_num;
        private String order_box_status;
        private String order_id;
        private String route_name;
        private String total;
        private String finished;
        private String driver_name;

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

        public String getFinished() {
            return finished;
        }

        public void setFinished(String finished) {
            this.finished = finished;
        }

        public String getDriver_name() {
            return driver_name;
        }

        public void setDriver_name(String driver_name) {
            this.driver_name = driver_name;
        }
    }
}
