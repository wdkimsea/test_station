package cn.guanmai.open.bean.product;

public class OpenPinleiBean {
    private String pinlei_id;
    private String pinlei_name;
    private String category1_id;
    private String category2_id;

    public String getId() {
        return pinlei_id;
    }

    public void setId(String id) {
        this.pinlei_id = id;
    }

    public String getName() {
        return pinlei_name;
    }

    public void setName(String name) {
        this.pinlei_name = name;
    }

    public String getCategory1_id() {
        return category1_id;
    }

    public void setCategory1_id(String category1_id) {
        this.category1_id = category1_id;
    }

    public String getCategory2_id() {
        return category2_id;
    }

    public void setCategory2_id(String category2_id) {
        this.category2_id = category2_id;
    }

    public OpenPinleiBean(String id, String name, String category1_id, String category2_id) {
        this.pinlei_id = id;
        this.pinlei_name = name;
        this.category1_id = category1_id;
        this.category2_id = category2_id;
    }
}
