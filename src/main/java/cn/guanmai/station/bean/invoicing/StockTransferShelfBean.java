package cn.guanmai.station.bean.invoicing;

import java.util.List;

/**
 * 接收货位信息接口
 * Created by yangjinhai on 2019/8/7.
 */
public class StockTransferShelfBean {

    private String name;
    private int value;
    private String parent_id;
    private int id;
    private List<Children> children;

    public class Children
    {
        private String name;
        private int value;
        private int parent_id;
        private List<Children> children;
        private int id;
        public void setName(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
        }

        public void setValue(int value) {
            this.value = value;
        }
        public int getValue() {
            return value;
        }

        public void setParent_id(int parent_id) {
            this.parent_id = parent_id;
        }
        public int getParent_id() {
            return parent_id;
        }

        public void setChildren(List<Children> children) {
            this.children = children;
        }
        public List<Children> getChildren() {
            return children;
        }

        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setValue(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
    public String getParent_id() {
        return parent_id;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }
    public List<Children> getChildren() {
        return children;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
