package cn.guanmai.manage.types;

/**
 * @program: test_station
 * @description: 结算周期
 * @author: weird
 * @create: 2019-01-11 15:01
 **/
public enum PayMethodType {
	Day(1), Week(2), Month(3), Other(4);
    private int value;

    private PayMethodType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
