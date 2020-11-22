package cn.guanmai.station.interfaces.order;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.order.AddressLabelBean;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderAnalysisBean;
import cn.guanmai.station.bean.order.OrderBatchResultBean;
import cn.guanmai.station.bean.order.OrderBatchUploadResultBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderChangeSkuResultBean;
import cn.guanmai.station.bean.order.OrderDeleteSkuResultBean;
import cn.guanmai.station.bean.order.OrderSkuCopyBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderImportResultBean;
import cn.guanmai.station.bean.order.OrderPriceSyncToSkuResultBean;
import cn.guanmai.station.bean.order.OrderProcessBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.async.AsyncTaskBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.OrderSkuRecognizeBean;
import cn.guanmai.station.bean.order.RecentOrderBean;
import cn.guanmai.station.bean.order.SaleSkuPriceUpdateResultBean;
import cn.guanmai.station.bean.order.param.OrderBatchCreateParam;
import cn.guanmai.station.bean.order.param.OrderChangeSkuParam;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderDeleteSkuParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderExceptionParam;
import cn.guanmai.station.bean.order.param.OrderRefundParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuParam;
import cn.guanmai.station.bean.order.param.OrderSkuPriceAutoUpdateParam;
import cn.guanmai.station.bean.order.param.OrderStatusPreconfigParam;
import cn.guanmai.station.bean.order.param.WeightRemarkFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;

/* 
* @author liming 
* @date Nov 12, 2018 10:25:47 AM 
* @des 订单相关接口类
* @version 1.0 
*/
public interface OrderService {

	/**
	 * 搜索查询订单 返回订单details详情信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderBean> searchOrder(OrderFilterParam orderFilterParam) throws Exception;

	/**
	 * 获取绑定站点的所有商户
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomerBean> getCustomers() throws Exception;

	/**
	 * 获取站点所有能下单的商户,自定义总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CustomerBean> getOrderCustomerArray(int count) throws Exception;

	/**
	 * 获取运营时间的收货时间
	 * 
	 * @param serviceTime
	 * @return
	 * @throws ParseException
	 */
	public OrderReceiveTimeBean getOrderReceiveTime(ServiceTimeBean serviceTime) throws ParseException;

	/**
	 * 获取下单商户的服务时间集合
	 * 
	 * @param address_id
	 * @return
	 * @throws Exception
	 */
	public List<OrderReceiveTimeBean> getCustomerServiceTimeArray(String address_id) throws Exception;

	/**
	 * 获取下单商品集合
	 * 
	 * @param address_id
	 * @param time_config_id
	 * @param search_texts
	 * @param max_count
	 * @return
	 * @throws Exception
	 */
	public List<OrderSkuParam> orderSkus(String address_id, String time_config_id, String[] search_texts, int max_count)
			throws Exception;

	/**
	 * 获取下单商品集合(兼容组合商品)
	 * 
	 * @param address_id
	 * @param time_config_id
	 * @param search_texts
	 * @param max_count
	 * @return
	 * @throws Exception
	 */
	public OrderCreateParam searchOrderSkus(String address_id, String time_config_id, String[] search_texts,
			int max_count) throws Exception;

	/**
	 * 下单智能识别商品
	 * 
	 * @param address_id
	 * @param time_config_id
	 * @param recognition_text
	 * @return
	 * @throws Exception
	 */
	public OrderSkuRecognizeBean recognizeSaleSku(String address_id, String time_config_id, String recognition_text)
			throws Exception;

	/**
	 * 创建订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public OrderResponseBean createOrder(OrderCreateParam order) throws Exception;

	/**
	 * 补录订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public OrderResponseBean createOldOrder(OrderCreateParam order) throws Exception;

	/**
	 * 修改订单
	 * 
	 * @param editOrder
	 * @return
	 * @throws Exception
	 */
	public boolean editOrder(OrderEditParam editOrder) throws Exception;

	/**
	 * 删除订单
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteOrder(String order_id) throws Exception;

	/**
	 * 按预设数修改订单状态
	 * 
	 * @param orderStatusPreconfigParam
	 * @return
	 * @throws Exception
	 */
	public boolean preconfigUpdateOrderStatus(OrderStatusPreconfigParam orderStatusPreconfigParam) throws Exception;

	/**
	 * 获取订单详细信息
	 * 
	 * @param order_id
	 * @return
	 */
	public OrderDetailBean getOrderDetailById(String order_id) throws Exception;

	/**
	 * 从订单列表获取订单
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public OrderBean getOrderBeanById(String order_id) throws Exception;

	/**
	 * Station订单按商品查看,修改的出库数（基本单位）
	 * 
	 * @param order_id
	 * @param sku_id
	 * @param std_real_quantity
	 * @return
	 */
	public boolean orderRealQuantityUpdate(String order_id, String sku_id, BigDecimal std_real_quantity)
			throws Exception;

	/**
	 * 修改订单状态 state: 5 分拣 10 配送
	 * 
	 * @param order_ids
	 * @param state
	 * @param batch_remark
	 * @return
	 */
	public boolean updateOrderState(List<String> order_ids, int status, String batch_remark) throws Exception;

	/**
	 * 修改订单状态 state: 5 分拣 10 配送
	 * 
	 * @param order_id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public boolean updateOrderState(String order_id, int status) throws Exception;

	/**
	 * 批量设置缺货
	 * 
	 * @param batchOutOfStockBeans
	 * @return
	 * @throws Exception
	 */
	public boolean updateBatchOutOfStock(List<OrderAndSkuBean> batchOutOfStockArray) throws Exception;

	/**
	 * 批量同步最新单价方式1
	 * 
	 * @param orderSkuPriceAutoArray
	 * @return
	 * @throws Exception
	 */
	public AsyncTaskBean updateOrderSkuPriceAuto(List<OrderAndSkuBean> orderSkuPriceAutoArray, int price_unit_type)
			throws Exception;

	/**
	 * 批量同步最新单价方式2
	 * 
	 * @param orderSkuPriceAutoUpdateParam
	 * @return
	 * @throws Exception
	 */
	public AsyncTaskBean updateOrderSkuPriceAuto(OrderSkuPriceAutoUpdateParam orderSkuPriceAutoUpdateParam)
			throws Exception;

	/**
	 * 批量同步最新单价-查看结果
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public List<SaleSkuPriceUpdateResultBean> updateSkuPriceResult(BigDecimal task_id) throws Exception;

	/**
	 * 手工修改订单中的商品单价 <br/>
	 * 这里的参数结果过于复杂,就没有对参数进行封装,直接用JSONObject进行的组装
	 * 
	 * @param price_data
	 * @return
	 * @throws Exception
	 */
	public BigDecimal updateOrderSkuPrice(JSONObject price_data) throws Exception;

	/**
	 * 订单列表,按商品查看搜索
	 * 
	 * @param orderSkuFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<OrderSkuFilterResultBean> searchOrderSku(OrderSkuFilterParam orderSkuFilterParam) throws Exception;

	/**
	 * 获取分拣备注,按下单、收货日期方式获取 1=按下单日期、3=按收货日期
	 * 
	 * @param type
	 * @param start_date 格式 2019-04-08
	 * @param end_date   格式 2019-04-08
	 * @return
	 * @throws Exception
	 */
	public List<String> getWeightRemarks(WeightRemarkFilterParam weightRemarkFilterParam) throws Exception;

	/**
	 * 获取分拣备注,按运营时间方式获取
	 * 
	 * @param time_config_id
	 * @param cycle_start_time 格式 2019-04-08 00:00
	 * @param cycle_end_time   格式 2019-04-08 00:00
	 * @return
	 * @throws Exception
	 */
	public List<String> getSortingRemarks(String time_config_id, String cycle_start_time, String cycle_end_time)
			throws Exception;

	/**
	 * 订单添加售后
	 * 
	 * @param order_id
	 * @param orderExceptionArray
	 * @param orderRundArray
	 * @return
	 * @throws Exception
	 */
	public boolean addOrderException(String order_id, List<OrderExceptionParam> orderExceptionArray,
			List<OrderRefundParam> orderRundArray) throws Exception;

	/**
	 * 订单详细分析接口
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public OrderAnalysisBean orderDetailSalesAnalysis(OrderFilterParam paramBean) throws Exception;

	/**
	 * 指定客户指定运营时间下的近10条订单
	 * 
	 * @param address_id
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public List<RecentOrderBean> getRecentOrders(String address_id, String time_config_id) throws Exception;

	/**
	 * 复制订单商品数据
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public List<OrderSkuCopyBean> copyOrder(String order_id) throws Exception;

	/**
	 * 订单批量导入模板导出
	 * 
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public String exportOrderTemplate(String time_config_id, String file_name) throws Exception;

	/**
	 * 模板批量导入订单
	 * 
	 * @param time_config_id
	 * @param file_path
	 * @param template_id
	 * @return
	 * @throws Exception
	 */
	public OrderBatchUploadResultBean orderBatchUpload(String time_config_id, String file_path, String template_id)
			throws Exception;

	/**
	 * 下载模板
	 * 
	 * @param sid
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public String downloadOrderTemplate(String sid, String time_config_id) throws Exception;

	/**
	 * 导入订单
	 * 
	 * @param sid
	 * @param time_config_id
	 * @param file_path
	 * @return
	 * @throws Exception
	 */
	public List<OrderImportResultBean> importOrder(String sid, String time_config_id, String file_path)
			throws Exception;

	/**
	 * 批量创建订单
	 * 
	 * @param orderBatchCreateParam
	 * @return
	 * @throws Exception
	 */
	public String orderBatchSubmite(OrderBatchCreateParam orderBatchCreateParam) throws Exception;

	/**
	 * 获取批量导入订单的异步任务最后执行结果
	 * 
	 * @param user_task_id
	 * @return
	 * @throws Exception
	 */
	public OrderBatchResultBean getOrderBatchResult(String user_task_id) throws Exception;

	/**
	 * 订单价格同步销售SKU
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean orderPriceSyncToSku(String order_id) throws Exception;

	/**
	 * 批量订单商品同步销售SKU方式一
	 * 
	 * @return
	 * @throws Exception
	 */
	public BigDecimal batchOrderPriceSyncToSku(List<OrderAndSkuBean> skuArray) throws Exception;

	/**
	 * 批量订单商品同步销售SKU方式二
	 * 
	 * @param orderSkuFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal batchOrderPriceSyncToSku(OrderSkuFilterParam orderSkuFilterParam) throws Exception;

	/**
	 * 批量订单商品同步销售SKU失败结果详情
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public List<OrderPriceSyncToSkuResultBean> batchOrderPriceSyncToSkuResult(BigDecimal task_id) throws Exception;

	/**
	 * 获取所有的商户标签
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AddressLabelBean> getAddressLabels() throws Exception;

	/**
	 * 新建订单-选好商户和商品后再切换商户才发送的请求
	 * 
	 * @param time_config_id
	 * @param address_id
	 * @param sku_ids
	 * @return
	 * @throws Exception
	 */
	public List<String> checkOrderSku(String time_config_id, String address_id, List<String> sku_ids) throws Exception;

	/**
	 * 查询订单可替换商品
	 * 
	 * @param salemenu_id
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<String> searchOrderChangeSku(String salemenu_id, String search_text) throws Exception;

	/**
	 * 订单替换商品
	 * 
	 * @param orderChangeSkuParams
	 * @return
	 * @throws Exception
	 */
	public BigDecimal orderChangeSkus(List<OrderChangeSkuParam> orderChangeSkuParams) throws Exception;

	/**
	 * 订单删除商品
	 * 
	 * @param orderDeleteSkuParams
	 * @return
	 * @throws Exception
	 */
	public BigDecimal orderDeleteSkus(List<OrderDeleteSkuParam> orderDeleteSkuParams) throws Exception;

	/**
	 * 搜索过滤后批量删除订单商品
	 * 
	 * @param orderSkuFilterParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal orderDeleteSkus(OrderSkuFilterParam orderSkuFilterParam) throws Exception;

	/**
	 * 获取批量删除订单商品的结果
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public List<OrderDeleteSkuResultBean> getOrderDeleteSkuResults(BigDecimal task_id) throws Exception;

	/**
	 * 获取批量替换订单商品的结果
	 * 
	 * @param task_id
	 * @return
	 * @throws Exception
	 */
	public List<OrderChangeSkuResultBean> getOrderChangeSkuResults(BigDecimal task_id) throws Exception;

	/**
	 * 获取订单流
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderProcessBean> getOrderProcessList() throws Exception;

}
