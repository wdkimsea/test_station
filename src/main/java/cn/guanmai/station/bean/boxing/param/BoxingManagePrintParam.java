package cn.guanmai.station.bean.boxing.param;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by abc on 2020/3/1.
 */
public class BoxingManagePrintParam {
    private JSONArray order_ids;
    private String force_print;
    private JSONArray box_ids;
    private String time_config_id;
    private String begin;
    private String end;
    private String route_id;
    private String carrier_id;
    private String driver_id;
    private String order_box_status;
    private String search;

    public JSONArray getOrder_ids() {
        return order_ids;
    }

    public void setOrder_ids(JSONArray order_ids) {
        this.order_ids = order_ids;
    }

    public String getForce_print() {
        return force_print;
    }

    public void setForce_print(String force_print) {
        this.force_print = force_print;
    }

    public JSONArray getBox_ids() {
        return box_ids;
    }

    public void setBox_ids(JSONArray box_ids) {
        this.box_ids = box_ids;
    }

    public String getTime_config_id() {
        return time_config_id;
    }

    public void setTime_config_id(String time_config_id) {
        this.time_config_id = time_config_id;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRoute_id() {
        return route_id;
    }

    public void setRoute_id(String route_id) {
        this.route_id = route_id;
    }

    public String getCarrier_id() {
        return carrier_id;
    }

    public void setCarrier_id(String carrier_id) {
        this.carrier_id = carrier_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public String getOrder_box_status() {
        return order_box_status;
    }

    public void setOrder_box_status(String order_box_status) {
        this.order_box_status = order_box_status;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
