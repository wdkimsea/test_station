package cn.guanmai.station.interfaces.delivery;

import java.util.List;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.delivery.DeliveryConfirmInfoBean;
import cn.guanmai.station.bean.delivery.DeliveryOrderDetail;
import cn.guanmai.station.bean.delivery.DeliveryOrderException;
import cn.guanmai.station.bean.delivery.DeliveryTaskBean;
import cn.guanmai.station.bean.delivery.HomePageBean;
import cn.guanmai.station.bean.delivery.param.DeliveryOrderExceptionParam;
import cn.guanmai.station.bean.delivery.param.DriverTracePointParam;

/* 
* @author liming 
* @date May 7, 2019 7:08:13 PM 
* @des 司机APP相关业务
* @version 1.0 
*/
public interface DriverService {
	/**
	 * 获取配送APP首页显示数据
	 * 
	 * @return
	 * @throws Exception
	 */
	public HomePageBean getHomePageData() throws Exception;

	/**
	 * 获取配送数据, true 配送完成、false 待配送
	 * 
	 * @param delivered
	 * @return
	 * @throws Exception
	 */
	public List<DeliveryTaskBean> getDeliveryTask(boolean delivered) throws Exception;

	/**
	 * 司机APP对配送订单进行完成配送操作
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean finishDelivery(String order_id) throws Exception;

	/**
	 * 获取配送订单详细信息,扫码验货
	 * 
	 * @param order_id
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public DeliveryOrderDetail getDeliveryOrderDetail(String order_id, String search_text) throws Exception;

	/**
	 * 获取装车验货信息
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public DeliveryConfirmInfoBean getDeliveryConfirmInfo(String order_id, String delivery_id) throws Exception;

	/**
	 * 司机装车验货
	 * 
	 * @param order_id
	 * @param sku_ids
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean productConfirm(String order_id, JSONArray sku_ids, String delivery_id, int status) throws Exception;

	/**
	 * 订单完成配送
	 * 
	 * @param order_id
	 * @param delivery_id
	 * @return
	 * @throws Exception
	 */
	public boolean deliveryConfirm(String order_id, String delivery_id) throws Exception;

	/**
	 * 更新商户收货地址
	 * 
	 * @param sid
	 * @param lat
	 * @param lng
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public boolean udpdateCustomerAddress(String sid, String lat, String lng, String address) throws Exception;

	/**
	 * 订单售后选项
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean exceptionOptions(String order_id) throws Exception;

	/**
	 * 获取订单售后信息
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public DeliveryOrderException getDeliveryOrderException(String order_id) throws Exception;

	/**
	 * 司机APP添加售后前的检测
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean checkReturnDeliveryOrder(DeliveryOrderExceptionParam param) throws Exception;

	/**
	 * 司机APP添加订单售后信息
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public boolean updateDeliveryOrderException(DeliveryOrderExceptionParam param) throws Exception;

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean startDelivery() throws Exception;

	/**
	 * 获取司机状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public Integer getDriverStatus() throws Exception;

	/**
	 * 上传司机轨迹坐标
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean uploadDriverTracePoint(DriverTracePointParam driverTracePointParam) throws Exception;
}
