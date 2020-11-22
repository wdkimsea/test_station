package cn.guanmai.open.bean.auth;

public class OpenStationInfoBean {
    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    private String station_id;
    private String station_name;

    public OpenStationInfoBean(String station_id, String station_name){
        super();
        this.station_id = station_id;
        this.station_name = station_name;
    }

}
