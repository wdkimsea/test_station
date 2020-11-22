package cn.guanmai.station.interfaces.delivery;

import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.delivery.CarModelBean;
import cn.guanmai.station.bean.delivery.CarrierBean;
import cn.guanmai.station.bean.delivery.DeliveryCategoryConfigBean;
import cn.guanmai.station.bean.delivery.DistributeOrderBean;
import cn.guanmai.station.bean.delivery.DistributeOrderDetailBean;
import cn.guanmai.station.bean.delivery.DriverBean;
import cn.guanmai.station.bean.delivery.DriverDistributeTaskBean;
import cn.guanmai.station.bean.delivery.DriverResultBean;
import cn.guanmai.station.bean.delivery.LedgerBean;
import cn.guanmai.station.bean.delivery.param.DeliverySettingParam;
import cn.guanmai.station.bean.delivery.param.DistributeOrderFilterParam;
import cn.guanmai.station.bean.delivery.param.DriverCreateParam;
import cn.guanmai.station.bean.delivery.param.DriverDistributeTaskFilterParam;
import cn.guanmai.station.bean.delivery.param.DriverUpdateParam;
import cn.guanmai.station.bean.delivery.param.LegerAddSkuParam;
import cn.guanmai.station.bean.delivery.param.LegerDeleteSkuParam;
import cn.guanmai.station.bean.delivery.param.LegerUpdateSkuParam;

/* 
* @author liming 
* @date Jan 4, 2019 10:21:56 AM 
* @des 配送相关业务
* @version 1.0 
*/
public interface DistributeService {

	/**
	 * 查询车型列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CarModelBean> searchCarModel(String q) throws Exception;

	/**
	 * 添加车型
	 * 
	 * @param carModel
	 * @return
	 * @throws Exception
	 */
	public BigDecimal addCarModel(CarModelBean carModel) throws Exception;

	/**
	 * 修改车型
	 * 
	 * @param carModel
	 * @return
	 * @throws Exception
	 */
	public boolean updateCarModel(CarModelBean carModel) throws Exception;

	/**
	 * 删除车型
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCarModel(BigDecimal id) throws Exception;

	/**
	 * 查询承运商列表
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	public List<CarrierBean> sarchCarrier(String q) throws Exception;

	/**
	 * 添加承运商
	 * 
	 * @param company_name
	 * @return
	 * @throws Exception
	 */
	public BigDecimal addCarrier(String company_name) throws Exception;

	/**
	 * 修改承运商
	 * 
	 * @param id
	 * @param company_name
	 * @return
	 * @throws Exception
	 */
	public boolean updateCarrier(BigDecimal id, String company_name) throws Exception;

	/**
	 * 删除承运商
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCarrier(BigDecimal id) throws Exception;

	/**
	 * 查询司机列表
	 * 
	 * @param q
	 * @param offset
	 * @param limit
	 * @return
	 * @throws Exception
	 */
	public List<DriverBean> searchDriver(String q, int offset, int limit) throws Exception;

	/**
	 * 添加司机
	 * 
	 * @param driverCreateParam
	 * @return
	 * @throws Exception
	 */
	public BigDecimal addDriver(DriverCreateParam driverCreateParam) throws Exception;

	/**
	 * 修改司机
	 * 
	 * @param driverUpdateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateDriver(DriverUpdateParam driverUpdateParam) throws Exception;

	/**
	 * 删除司机
	 * 
	 * @param driver_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteDriver(BigDecimal driver_id) throws Exception;

	/**
	 * 初始化配送相关数据-承运商-车型-司机
	 * 
	 * @return
	 * @throws Exception
	 */
	public DriverBean initDriverData(String station_id) throws Exception;

	/**
	 * 智能规划配送单
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean autoAssigndistributeTask(JSONArray order_ids) throws Exception;

	/**
	 * 搜索配送订单任务列表
	 * 
	 * @param distributeOrderFilterParam
	 * @return
	 * @throws Exception
	 */
	public List<DistributeOrderBean> getDistributeOrders(DistributeOrderFilterParam distributeOrderFilterParam)
			throws Exception;

	/**
	 * 配送-司机任务列表查询
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<DriverDistributeTaskBean> getDriverDistributeTasks(DriverDistributeTaskFilterParam paramBean)
			throws Exception;

	/**
	 * 订单分配任务司机
	 * 
	 * @param order_id
	 * @param receive_address_id
	 * @param receive_begin_time
	 * @param driver_id
	 * @param operation_type
	 * @return
	 * @throws Exception
	 */
	public boolean editAssignDistributeTask(String order_id, String receive_address_id, String receive_begin_time,
			BigDecimal driver_id, int operation_type) throws Exception;

	/**
	 * 获取司机列表接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<DriverResultBean> getDriverList() throws Exception;

	/**
	 * 获取配送单模板ID列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getDistributeConfigIds() throws Exception;

	/**
	 * 获取配送单模板详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean getDistributeConfigDetail(String id) throws Exception;

	/**
	 * 获取配送单配置详细信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getDistributeConfigDetail() throws Exception;

	/**
	 * 获取配送单打印自定义分类信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public DeliveryCategoryConfigBean getDeliveryCategoryConfig() throws Exception;

	/**
	 * 更新配送单打印自定义分类信息
	 * 
	 * @param id
	 * @param category_config
	 * @return
	 * @throws Exception
	 */
	public boolean updateDeliveryCategoryConfig(String id, List<List<String>> category_config) throws Exception;

	/**
	 * 获取新老配送单选中状态
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean getDistributeConfigSelected() throws Exception;

	/**
	 * 获取配送单详细列表接口
	 * 
	 * @param order_ids
	 * @return
	 * @throws Exception
	 */
	public List<DistributeOrderDetailBean> getDistributeOrderDetailArray(List<String> order_ids) throws Exception;

	/**
	 * 获取司机配送单信息
	 * 
	 * @param driver_id
	 * @param order_ids
	 * @return
	 * @throws Exception
	 */
	public boolean printDriverTasks(BigDecimal driver_id, JSONArray order_ids) throws Exception;

	/**
	 * 编辑配送单
	 * 
	 * @param order_id
	 * @param freight
	 * @param skus
	 * @param exception_skus
	 * @return
	 * @throws Exception
	 */
	public boolean submitDistributionOrder(String order_id, BigDecimal freight, JSONArray skus,
			JSONArray exception_skus) throws Exception;

	/**
	 * 新增打印日志
	 * 
	 * @param order_ids
	 * @return
	 * @throws Exception
	 */
	public boolean createPrintLog(List<String> order_ids) throws Exception;

	/**
	 * 获取订单同步配送单 同步设置
	 * 
	 * @param order_id
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public LedgerBean getLedger(String order_id, int type, boolean first) throws Exception;

	/**
	 * 编辑配送单的前置设置
	 * 
	 * @param deliverySettingParam
	 * @return
	 * @throws Exception
	 */
	public boolean submitDeliverySetting(DeliverySettingParam deliverySettingParam) throws Exception;

	/**
	 * 更新套账单-修改商品
	 * 
	 * @param delivery_id
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean updateLedgerSku(String delivery_id, List<LegerUpdateSkuParam> updateParam) throws Exception;

	/**
	 * 更新套账单-删除商品
	 * 
	 * @param delivery_id
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	public boolean deleteLedgerSku(String delivery_id, List<LegerDeleteSkuParam> updateParam) throws Exception;

	/**
	 * 更新套账单-新增商品
	 * 
	 * @param delivery_id
	 * @param addParam
	 * @return
	 * @throws Exception
	 */
	public boolean addLedgerSku(String delivery_id, List<LegerAddSkuParam> addParam) throws Exception;

}
