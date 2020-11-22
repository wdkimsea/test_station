package cn.guanmai.bshop.bean;

/**
 * 设置收货地址信息类
 * Created by yangjinhai on 2019/8/28.
 */
public class SetAddressBean {
    String address_id;
    String receive_way;
    String pick_up_st_id;

    public SetAddressBean() {
    }

    public SetAddressBean(String address_id, String receive_way, String pick_up_st_id) {
        this.address_id = address_id;
        this.receive_way = receive_way;
        this.pick_up_st_id = pick_up_st_id;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getReceive_way() {
        return receive_way;
    }

    public void setReceive_way(String receive_way) {
        this.receive_way = receive_way;
    }

    public String getPick_up_st_id() {
        return pick_up_st_id;
    }

    public void setPick_up_st_id(String pick_up_st_id) {
        this.pick_up_st_id = pick_up_st_id;
    }
}
