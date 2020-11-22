package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * Created by abc on 2020/2/21.
 * @desc 装箱助手app
 */
public class BoxingURL {
    private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

    //装箱订单接口列表
    public static final String get_box_order_list = stationUrl + "/box/order/list";

    //装箱订单详情接口
    public static final String get_box_order_details = stationUrl + "/box/order/detail";

    //修改订单集包状态
    public static final String update_box_order_status = stationUrl + "/box/order/box_status/update";

    //创建箱子
    public static final String create_box = stationUrl + "/box/create";

    //商品装箱
    public static final String box_package_in_box = stationUrl + "/box/package/in_box";

    //取消装箱
    public static final String box_package_out_box = stationUrl + "/box/package/out_box";

    // 订单当前关联的所有箱子
    public static final String box_order_box_list = stationUrl + "/box/order/box/list";

    //更改箱子
    public static final String box_package_change_box = stationUrl + "/box/package/box/change";

    //订单扫码接口
    public static final String box_barcode_search = stationUrl + "/box/barcode/search";

    //订单详情扫码接口
    public static final String box_package_id_search = stationUrl + "/box/package_id/search";

    //装箱管理获取订单列表
    public static final String box_manage_order_list = stationUrl + "/box/box_manage/order/list";

    //装箱管理打印接口
    public static final String box_manage_print = stationUrl + "/box/print";
}
