package cn.guanmai.manage.types;

/**
 * @program: test_station
 * @description: 结算方式
 * @author: weird
 * @create: 2019-01-11 15:12
 **/
public enum SettleWay {
    Default(1), PayFirst(2);
    private int value;

    private SettleWay(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
