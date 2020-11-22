package cn.guanmai.station.bean.boxing.param;

/**
 * Created by abc on 2020/2/26.
 * 装箱管理页面请求参数
 */
public class BoxingManageOrderParam {
    private String time_config_id; //运营id
    private String begin; //开始时间 格式 yyyy-mm-dd hh:mm
    private String end;
    private String route_id; //线路id,不传全部线路
    private String carrier_id;//承运商id,不传不通过这个搜索
    private String driver_id;
    private String order_box_status;
    private String search;
    private int export;//导出,0 不导出,1 导出
    private String limit;
    private int offset;

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

    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
