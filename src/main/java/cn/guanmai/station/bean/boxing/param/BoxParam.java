package cn.guanmai.station.bean.boxing.param;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by abc on 2020/2/24.
 */
public class BoxParam {

    private String box_id;//箱子ID

    //装箱
    private JSONArray package_ids;
    //更换箱子
    private String package_id;
    private String old_box_id;
    //取消装箱
    private JSONArray box_ids;

    public String getBox_id() {
        return box_id;
    }

    public void setBox_id(String box_id) {
        this.box_id = box_id;
    }

    public JSONArray getPackage_ids() {
        return package_ids;
    }

    public void setPackage_ids(JSONArray package_ids) {
        this.package_ids = package_ids;
    }

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getOld_box_id() {
        return old_box_id;
    }

    public void setOld_box_id(String old_box_id) {
        this.old_box_id = old_box_id;
    }

    public JSONArray getBox_ids() {
        return box_ids;
    }

    public void setBox_ids(JSONArray box_ids) {
        this.box_ids = box_ids;
    }
}
