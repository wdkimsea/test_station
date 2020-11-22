package cn.guanmai.station.interfaces.boxing;

import cn.guanmai.station.bean.boxing.BoxBean;
import cn.guanmai.station.bean.boxing.BoxingManagePrintDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderDetailBean;
import cn.guanmai.station.bean.boxing.BoxingOrderListBean;
import cn.guanmai.station.bean.boxing.param.BoxParam;
import cn.guanmai.station.bean.boxing.param.BoxingManageOrderParam;
import cn.guanmai.station.bean.boxing.param.BoxingManagePrintParam;
import cn.guanmai.station.bean.boxing.param.BoxingOrderParam;

import java.util.List;

/**
 * Created by abc on 2020/2/20.
 *
 * @desc 装箱app接口业务
 */
public interface BoxingAppService {

    //装箱app列表
    public BoxingOrderListBean getBoxingOrderList(BoxingOrderParam param) throws Exception;

    //装箱app订单详情接口
    public BoxingOrderDetailBean getBoxingOrderDetail(BoxingOrderParam param) throws Exception;

    //修改订单集包状态
    public boolean updateBoxingOrderStatus(BoxingOrderParam param) throws Exception;

    //创建箱子
    public String createBox() throws Exception;

    //商品装入箱子
    public boolean packageInBox(List<String> package_ids, String box_id) throws Exception;

    //取消装箱
    public boolean packageOutBox(BoxParam boxParam) throws Exception;

    //查询订单箱子接口
    public List<BoxBean> getBoxDetails(String order_id) throws Exception;

    //商品更换箱子
    public boolean packageChangeBox(BoxParam boxParam) throws Exception;

    //订单列表扫码接口
    public String searchBoxBarCode(String barcode,String time_config_id,String date) throws Exception;

    //订单详情商品码扫码接口
    public String searchBoxPackageId(String package_id) throws Exception;

    //装箱管理获取订单列表
    public List<BoxingOrderDetailBean> getBoxingManageOrderList(BoxingManageOrderParam param) throws Exception;

    //装箱管理导出
    public String exportBoxingManageOrderList(BoxingManageOrderParam param) throws Exception;

    //装箱管理打印接口
    public List<BoxingManagePrintDetailBean> printBoxingManageOrder(BoxingManagePrintParam printParam) throws Exception;


}
