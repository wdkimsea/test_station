package cn.guanmai.station.bean.boxing.param;

/**
 * Created by abc on 2020/2/20.
 * 装箱app订单列表和订单详情类
 */
public class BoxingOrderParam {
    private String time_config_id;
    private String date;
    private String order_box_status;
    private String route_id;
    private String search;
    private String order_id;
    private String sku_box_status;

    public String getTime_config_id() {
        return time_config_id;
    }

    public void setTime_config_id(String time_config_id) {
        this.time_config_id = time_config_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrder_box_status() {
        return order_box_status;
    }

    public void setOrder_box_status(String order_box_status) {
        this.order_box_status = order_box_status;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getSku_box_status() {
        return sku_box_status;
    }

    public void setSku_box_status(String sku_box_status) {
        this.sku_box_status = sku_box_status;
    }


    public BoxingOrderParam(){

    }

    /**
     * 装箱app订单列表初始化
     */
    public BoxingOrderParam(String time_config_id,String order_box_status,String date,String search){
        this.time_config_id = time_config_id;
        this.order_box_status = order_box_status;
        this.date = date;
        this.search = search;
    }
}
