package cn.guanmai.open.bean.product;

public class OpenCategory1Bean {
    private String category1_id;
    private String category1_name;

    public String getId() {
        return category1_id;
    }

    public void setId(String id) {
        this.category1_id = id;
    }

    public String getName() {
        return category1_name;
    }

    public void setName(String name) {
        this.category1_name = name;
    }

    public OpenCategory1Bean(String id, String name) {
        this.category1_id = id;
        this.category1_name = name;
    }
}
