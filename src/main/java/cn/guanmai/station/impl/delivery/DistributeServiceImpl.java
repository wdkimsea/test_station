package cn.guanmai.station.impl.delivery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.testng.Reporter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.okhttp.RequestType;
import cn.guanmai.request.impl.BaseRequestImpl;
import cn.guanmai.request.intefaces.BaseRequest;
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
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.url.DistributeURL;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jan 4, 2019 10:34:36 AM 
* @des 配送业务相关实现类
* @version 1.0 
*/
public class DistributeServiceImpl implements DistributeService {

	private BaseRequest baseRequest;

	public DistributeServiceImpl(Map<String, String> headers) {
		baseRequest = new BaseRequestImpl(headers);
	}

	@Override
	public BigDecimal addCarModel(CarModelBean carModel) throws Exception {

		String urlStr = DistributeURL.add_car_model_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, carModel);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public boolean updateCarModel(CarModelBean carModel) throws Exception {
		String urlStr = DistributeURL.update_car_model_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("car_model_id", String.valueOf(carModel.getId()));
		if (carModel.getCar_model_name() != null) {
			paramMap.put("car_model_name", carModel.getCar_model_name());
		}
		if (carModel.getMax_load() != 0) {
			paramMap.put("max_load", String.valueOf(carModel.getMax_load()));
		}

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteCarModel(BigDecimal id) throws Exception {
		String urlStr = DistributeURL.delete_car_model_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public BigDecimal addCarrier(String company_name) throws Exception {
		String urlStr = DistributeURL.add_carrier_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("company_name", company_name);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public boolean updateCarrier(BigDecimal id, String company_name) throws Exception {
		String urlStr = DistributeURL.update_carrier_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("carrier_id", String.valueOf(id));
		paramMap.put("company_name", company_name);

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteCarrier(BigDecimal id) throws Exception {
		String urlStr = DistributeURL.delete_carrier_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(id));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public BigDecimal addDriver(DriverCreateParam driverCreateParam) throws Exception {
		String urlStr = DistributeURL.add_driver_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, driverCreateParam);

		return retObj.getInteger("code") == 0 ? new BigDecimal(retObj.getJSONObject("data").getString("id")) : null;
	}

	@Override
	public boolean updateDriver(DriverUpdateParam driverUpdateParam) throws Exception {
		String urlStr = DistributeURL.update_driver_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, driverUpdateParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteDriver(BigDecimal driver_id) throws Exception {
		String urlStr = DistributeURL.delete_driver_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", String.valueOf(driver_id));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<CarModelBean> searchCarModel(String q) throws Exception {
		String urlStr = DistributeURL.search_car_model_url;

		Map<String, String> paramMap = new HashMap<>();
		if (q != null) {
			paramMap.put("q", q);
		}

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CarModelBean.class)
				: null;
	}

	@Override
	public List<CarrierBean> sarchCarrier(String q) throws Exception {
		String urlStr = DistributeURL.search_carrier_url;

		Map<String, String> paramMap = new HashMap<>();
		if (q != null) {
			paramMap.put("q", q);
		}

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), CarrierBean.class)
				: null;
	}

	@Override
	public List<DriverBean> searchDriver(String q, int offset, int limit) throws Exception {
		String urlStr = DistributeURL.search_driver_url;

		Map<String, String> paramMap = new HashMap<>();
		if (q != null) {
			paramMap.put("q", q);
		}
		paramMap.put("offset", String.valueOf(offset));
		paramMap.put("limit", String.valueOf(limit));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		String dataStr = retObj.getJSONArray("data").toString();
		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(dataStr, DriverBean.class) : null;
	}

	@Override
	public DriverBean initDriverData(String station_id) throws Exception {
		// 如果没有承运商就新建
		List<CarrierBean> carrierArray = sarchCarrier(null);
		BigDecimal carrier_id = null;
		if (carrierArray == null) {
			throw new Exception("搜索查询承运商失败");
		} else if (carrierArray.size() == 0) {
			carrier_id = addCarrier("极速快运");
			if (carrier_id == null) {
				throw new Exception("调度中心新建承运商失败");
			}
		} else {
			carrier_id = carrierArray.get(0).getId();
		}

		// 如果没有车型就新建
		List<CarModelBean> carModelArray = searchCarModel(null);
		BigDecimal car_model_id = null;
		int max_load = 10;
		if (carModelArray == null) {
			throw new Exception("搜索查询车型失败");
		} else if (carModelArray.size() == 0) {
			CarModelBean carModel = new CarModelBean("五菱宏光", max_load);
			car_model_id = addCarModel(carModel);
			if (car_model_id == null) {
				throw new Exception("调度中心新建车型失败");
			}
		} else {
			car_model_id = carModelArray.get(0).getId();
			max_load = carModelArray.get(0).getMax_load();
		}

		List<DriverBean> driverArray = searchDriver(null, 0, 10);
		DriverBean driver = null;
		if (driverArray == null) {
			throw new Exception("搜索查询司机失败");
		}
		driverArray = driverArray.stream().filter(d -> d.getStation_id().equals(station_id) && d.getState() == 1)
				.collect(Collectors.toList());

		if (driverArray.size() == 0) {
			String account = "AT" + StringUtil.getRandomNumber(6);
			String name = StringUtil.getRandomString(6).toUpperCase();
			DriverCreateParam driverCreateParam = new DriverCreateParam();
			driverCreateParam.setAccount(account);
			driverCreateParam.setAllow_login(1);
			driverCreateParam.setCar_model_id(car_model_id);
			driverCreateParam.setCarrier_id(carrier_id);
			driverCreateParam.setMax_load(max_load);
			driverCreateParam.setName(name);
			driverCreateParam.setPhone("12" + StringUtil.getRandomNumber(9));
			driverCreateParam.setPassword("Test1234_");
			driverCreateParam.setPassword_check("Test1234_");
			driverCreateParam.setShare(1);
			driverCreateParam.setState(1);

			BigDecimal driver_id = addDriver(driverCreateParam);
			if (driver_id == null) {
				throw new Exception("调度中心新建司机失败");
			}

			driverArray = searchDriver(name, 0, 10);
			if (driverArray == null) {
				throw new Exception("搜索查询司机失败");
			}

			driverArray = driverArray.stream().filter(d -> d.getStation_id().equals(station_id) && d.getState() == 1)
					.collect(Collectors.toList());
		}
		driver = driverArray.get(0);
		return driver;
	}

	@Override
	public boolean autoAssigndistributeTask(JSONArray order_ids) throws Exception {
		Reporter.log("配送-智能规划配送单");
		String urlStr = DistributeURL.auto_assign_distribute_task_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_ids", order_ids.toString());

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public List<DistributeOrderBean> getDistributeOrders(DistributeOrderFilterParam distributeOrderFilterParam)
			throws Exception {
		Reporter.log("配送-搜索配送订单任务列表");
		String urlStr = DistributeURL.search_distribute_orders_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, distributeOrderFilterParam);

		return retObj.getInteger("code") == 0 ? JsonUtil.strToClassList(
				retObj.getJSONObject("data").getJSONArray("order").toString(), DistributeOrderBean.class) : null;
	}

	@Override
	public List<DriverDistributeTaskBean> getDriverDistributeTasks(DriverDistributeTaskFilterParam paramBean)
			throws Exception {
		Reporter.log("配送-司机任务列表搜索");
		String urlStr = DistributeURL.driver_distribute_task_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramBean);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), DriverDistributeTaskBean.class)
				: null;
	}

	@Override
	public boolean editAssignDistributeTask(String order_id, String receive_address_id, String receive_begin_time,
			BigDecimal driver_id, int operation_type) throws Exception {
		Reporter.log("配送-订单分配任务司机");
		String urlStr = DistributeURL.edit_assign_distribute_task_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("receive_address_id", receive_address_id);
		paramMap.put("receive_begin_time", receive_begin_time);
		paramMap.put("driver_id", String.valueOf(driver_id));
		paramMap.put("operation_type", String.valueOf(operation_type));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<DriverResultBean> getDriverList() throws Exception {
		String urlStr = DistributeURL.get_driver_list_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").get(0).toString(), DriverResultBean.class)
				: null;
	}

	@Override
	public List<String> getDistributeConfigIds() throws Exception {
		String url = DistributeURL.distribute_config_list_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		List<String> ids = null;
		if (retObj.getInteger("code") == 0) {
			ids = new ArrayList<>();
			JSONArray distributeConfigArray = retObj.getJSONArray("data");
			for (Object obj : distributeConfigArray) {
				JSONObject distributeConfigObj = JSONObject.parseObject(obj.toString());
				ids.add(distributeConfigObj.getString("id"));
			}
		}
		return ids;
	}

	@Override
	public boolean getDistributeConfigDetail(String id) throws Exception {
		String url = DistributeURL.distribute_config_detail_url;

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("id", id);

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean getDistributeConfigDetail() throws Exception {
		String url = DistributeURL.get_distribute_config_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public DeliveryCategoryConfigBean getDeliveryCategoryConfig() throws Exception {
		String url = DistributeURL.delivery_category_config_get_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), DeliveryCategoryConfigBean.class)
				: null;
	}

	@Override
	public boolean updateDeliveryCategoryConfig(String id, List<List<String>> category_config) throws Exception {
		String url = DistributeURL.delivery_category_config_update_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("category_config", JSONArray.toJSONString(category_config));
		paramMap.put("id", id);
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean getDistributeConfigSelected() throws Exception {
		String url = DistributeURL.get_distribute_config_selected_url;

		JSONObject retObj = baseRequest.baseRequest(url, RequestType.GET);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public List<DistributeOrderDetailBean> getDistributeOrderDetailArray(List<String> order_ids) throws Exception {
		String urlStr = DistributeURL.get_distribute_order_detail_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ids", JSON.toJSONString(order_ids));

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassList(retObj.getJSONArray("data").toString(), DistributeOrderDetailBean.class)
				: null;
	}

	@Override
	public boolean printDriverTasks(BigDecimal driver_id, JSONArray order_ids) throws Exception {
		String urlStr = DistributeURL.print_driver_tasks_url;

		JSONObject paramObj = new JSONObject();
		paramObj.put(String.valueOf(driver_id), order_ids);
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("print_drivers", paramObj.toString());

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0;

	}

	@Override
	public boolean submitDistributionOrder(String order_id, BigDecimal freight, JSONArray skus,
			JSONArray exception_skus) throws Exception {
		Reporter.log("编辑配送单");
		String urlStr = DistributeURL.submit_distribution_order_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("freight", String.valueOf(freight));
		paramMap.put("skus", skus.toString());
		paramMap.put("exception_skus", exception_skus.toString());

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean createPrintLog(List<String> order_ids) throws Exception {
		String urlStr = DistributeURL.create_print_log_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		JSONArray ids = new JSONArray();
		for (String id : order_ids) {
			ids.add(id);
		}
		paramMap.put("ids", JSON.toJSONString(ids));
		paramMap.put("sheet_type", "1");

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public LedgerBean getLedger(String order_id, int type, boolean first) throws Exception {
		String urlStr = DistributeURL.get_delivery_setting_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("order_id", order_id);
		paramMap.put("type", String.valueOf(type));
		if (first) {
			paramMap.put("first", "1");
		}

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.GET, paramMap);

		return retObj.getInteger("code") == 0
				? JsonUtil.strToClassObject(retObj.getJSONObject("data").toString(), LedgerBean.class)
				: null;
	}

	@Override
	public boolean submitDeliverySetting(DeliverySettingParam deliverySettingParam) throws Exception {
		String urlStr = DistributeURL.submit_delivery_setting_url;

		JSONObject retObj = baseRequest.baseRequest(urlStr, RequestType.POST, deliverySettingParam);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean updateLedgerSku(String delivery_id, List<LegerUpdateSkuParam> updateParam) throws Exception {
		String url = DistributeURL.update_leger_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delivery_id", delivery_id);
		paramMap.put("details", JsonUtil.objectToStr(updateParam));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean deleteLedgerSku(String delivery_id, List<LegerDeleteSkuParam> updateParam) throws Exception {
		String url = DistributeURL.update_leger_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delivery_id", delivery_id);
		paramMap.put("details", JsonUtil.objectToStr(updateParam));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);

		return retObj.getInteger("code") == 0;
	}

	@Override
	public boolean addLedgerSku(String delivery_id, List<LegerAddSkuParam> addParam) throws Exception {
		String url = DistributeURL.update_leger_url;

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("delivery_id", delivery_id);
		paramMap.put("details", JsonUtil.objectToStr(addParam));
		JSONObject retObj = baseRequest.baseRequest(url, RequestType.POST, paramMap);
		return retObj.getInteger("code") == 0;
	}

}
