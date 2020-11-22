package cn.guanmai.station.weight;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.account.UserBean;
import cn.guanmai.station.bean.account.param.UserAddParam;
import cn.guanmai.station.bean.account.param.UserFilterParam;
import cn.guanmai.station.bean.invoicing.SupplierAccountBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.order.OrderCycle;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.PrintTagTemplateBean;
import cn.guanmai.station.bean.weight.WeightCategoryTreeBean;
import cn.guanmai.station.bean.weight.WeightSkuBean;
import cn.guanmai.station.bean.weight.param.OutOfStockParam;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.weight.param.WeightDataFilterParam;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.system.AccountServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.TemplateServiceImpl;
import cn.guanmai.station.impl.weight.WeightServiceImpl;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.system.AccountService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.TemplateService;
import cn.guanmai.station.interfaces.weight.WeightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.station.tools.PurchaseTaskTool;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月29日 上午10:42:05
 * @description: 供应商分拣测试
 * @version: 1.0
 */

public class SupplierWeightTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierWeightTest.class);

	private OrderTool orderTool;
	private OrderService orderService;
	private PurchaseTaskService purchaseTaskService;
	private PurchaseTaskTool purchaseTaskTool;
	private AccountService accountService;
	private SupplierService supplierService;
	private LoginUserInfoService loginUserInfoService;

	private WeightService weightService;
	private TemplateService templateService;

	private InitDataBean initData;
	private String order_id;
	private OrderCycle orderCycle;
	private String time_config_id;
	private String cycle_start_date;
	private OrderDetailBean orderDetail;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderTool = new OrderTool(headers);
		orderService = new OrderServiceImpl(headers);
		accountService = new AccountServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaseTaskTool = new PurchaseTaskTool(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号信息失败");

			String station_id = loginUserInfo.getStation_id();

			UserFilterParam userFilterParam = new UserFilterParam();
			userFilterParam.setIs_valid(1);
			userFilterParam.setStation_id(station_id);
			userFilterParam.setType_id(1);

			List<UserBean> users = accountService.searchUser(userFilterParam);
			Assert.assertNotEquals(users, null, "账号管理,搜索过滤供应商账号失败");

			UserBean user = users.stream().filter(u -> u.getUsername().startsWith("GYS")).findFirst().orElse(null);
			String username = null;
			String pwd = "1qaz2wsx";
			if (user == null) {
				username = "GYS" + TimeUtil.getLongTime();

				UserAddParam userAddParam = new UserAddParam();
				userAddParam.setUsername(username);
				userAddParam.setPassword(pwd);
				userAddParam.setStation_id(station_id);
				userAddParam.setIs_admin(false);
				userAddParam.setIs_valid(1);
				userAddParam.setType_id(1);
				userAddParam.setRole_ids(new ArrayList<BigDecimal>());
				Integer user_id = accountService.addUser(userAddParam);
				Assert.assertNotEquals(user_id, null, "用户管理,新建用户失败");
			} else {
				username = user.getUsername();

				boolean result = accountService.updateUserPwd(user.getId(), pwd);
				Assert.assertEquals(result, true, "用户管理,修改商户密码失败");
			}

			List<SupplierAccountBean> supplierAccounts = supplierService.getSupplierAccounts();
			Assert.assertNotEquals(supplierAccounts, null, "获取供应商账号列表失败");

			SupplierAccountBean supplierAccount = null;
			for (SupplierAccountBean sa : supplierAccounts) {
				if (sa.getUsername().equals(username)) {
					supplierAccount = sa;
					break;
				}
			}
			Assert.assertNotEquals(supplierAccount, null, "供应商账号列表拉取的数据不全,没有账号 " + username);

			initData = getInitData();
			String supplier_id = initData.getSupplier().getId();
			SupplierDetailBean supplierDetail = supplierService.getSupplierById(supplier_id);
			Assert.assertNotEquals(supplierDetail, null, "获取供应商详情失败");
			supplierDetail.setUser_id(supplierAccount.getUser_id());
			boolean result = supplierService.updateSupplier(supplierDetail);
			Assert.assertEquals(result, true, "修改供应商信息失败");

			Map<String, String> supplier_headers = LoginUtil.loginStation(username, pwd);
			Assert.assertNotEquals(supplier_headers, null, "供应商登录失败");

			weightService = new WeightServiceImpl(supplier_headers);
			templateService = new TemplateServiceImpl(supplier_headers);

		} catch (Exception e) {
			logger.error("初始化供应商账号遇到错误: ", e);
			Assert.fail("初始化供应商账号遇到错误: ", e);
		}

		// 创建订单-订单状态改为分拣-生成采购任务-采购任务切换供应商-发布采购任务
		try {
			order_id = orderTool.oneStepCreateOrder(6);
			Assert.assertNotEquals(order_id, null, "新建订单失败");

			boolean result = orderService.updateOrderState(order_id, 5);
			Assert.assertEquals(result, true, "订单状态改为分拣中失败");

			List<PurchaseTaskData> purchaseTaskDataArray = purchaseTaskTool.getOrderPurcahseTask(order_id);
			Assert.assertEquals(purchaseTaskDataArray != null && purchaseTaskDataArray.size() > 0, true,
					"订单" + order_id + "的采购任务在60秒内没有生成");

			List<String> task_ids = new ArrayList<String>();
			for (PurchaseTaskData purchaseTaskData : purchaseTaskDataArray) {
				for (PurchaseTaskData.Task task : purchaseTaskData.getTasks()) {
					task_ids.add(task.getId());
				}
			}

			result = purchaseTaskService.purchaseTaskChangeSupplier(task_ids, initData.getSupplier().getId());
			Assert.assertEquals(result, true, "采购任务修改供应商失败");

			String today = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(today);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setQ(order_id);
			fiterParam.setTask_suggests(new JSONArray());

			result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			orderCycle = orderTool.getOrderOperationCycle(order_id);
			time_config_id = orderCycle.getTime_config_id();
			cycle_start_date = orderCycle.getCycle_start_time().substring(0, 10);

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + " 详细信息失败");
		} catch (Exception e) {
			logger.error("创建供应商分拣数据遇到错误: ", e);
			Assert.fail("创建供应商分拣数据遇到错误: ", e);
		}

	}

	@Test
	public void supplierWeightTestCase01() {
		ReporterCSS.title("测试点: 供应商分拣,查看拉取的订单分拣商品数据(SPU)是否正确");
		try {
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			List<String> weight_spus = new ArrayList<String>();
			List<String> non_weight_spus = new ArrayList<String>();
			for (OrderDetailBean.Detail detail : details) {
				if (detail.getIs_weigh() == 1) {
					weight_spus.add(detail.getSpu_id());
				} else {
					non_weight_spus.add(detail.getSpu_id());
				}
			}

			List<WeightCategoryTreeBean> weightCategoryTreeList = weightService.getWeightCategoryTree(time_config_id,
					cycle_start_date, true);
			Assert.assertNotEquals(weightCategoryTreeList, null, "供应商分拣,拉取计重商品列表失败");

			List<String> actual_weight_spus = new ArrayList<>();
			for (WeightCategoryTreeBean weightCategoryTree : weightCategoryTreeList) {
				for (WeightCategoryTreeBean.Category2 category2 : weightCategoryTree.getCategory2s()) {
					for (WeightCategoryTreeBean.Category2.Spu spu : category2.getSpus()) {
						actual_weight_spus.add(spu.getSpu_id());
					}
				}
			}

			String msg = null;
			boolean result = true;
			if (!actual_weight_spus.containsAll(weight_spus)) {
				weight_spus.removeAll(actual_weight_spus);
				msg = String.format("订单%s中的如下商品%s没有出现在分拣软件中的计重商品列表", order_id, weight_spus);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<WeightCategoryTreeBean> nonWeightCategoryTreeList = weightService.getWeightCategoryTree(time_config_id,
					cycle_start_date, false);
			Assert.assertNotEquals(nonWeightCategoryTreeList, null, "供应商分拣,拉取计重商品列表失败");

			List<String> actual_non_weight_spus = new ArrayList<>();
			for (WeightCategoryTreeBean weightCategoryTree : nonWeightCategoryTreeList) {
				for (WeightCategoryTreeBean.Category2 category2 : weightCategoryTree.getCategory2s()) {
					for (WeightCategoryTreeBean.Category2.Spu spu : category2.getSpus()) {
						actual_non_weight_spus.add(spu.getSpu_id());
					}
				}
			}

			if (!actual_non_weight_spus.containsAll(non_weight_spus)) {
				non_weight_spus.removeAll(actual_non_weight_spus);
				msg = String.format("订单%s中的如下商品%s没有出现在分拣软件中的非计重商品列表", order_id, non_weight_spus);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "供应商分拣,拉取的订单数据不全");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

	@Test
	public void supplierWeightTestCase02() {
		ReporterCSS.title("测试点: 供应商分拣,查看拉取的订单分拣商品数据(SKU)是否正确");
		try {
			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			List<String> weight_spus = new ArrayList<String>();
			List<String> non_weight_spus = new ArrayList<String>();
			for (OrderDetailBean.Detail detail : details) {
				if (detail.getIs_weigh() == 1) {
					weight_spus.add(detail.getSpu_id());
				} else {
					non_weight_spus.add(detail.getSpu_id());
				}
			}

			WeightDataFilterParam filterParam = new WeightDataFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(cycle_start_date);
			filterParam.setSpu_ids(weight_spus);
			filterParam.setIs_weight(1);

			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(weightSkuList, "供应商分拣,拉取计重商品列表数据失败");

			// 不需要称重商品
			filterParam.setSpu_ids(non_weight_spus);
			filterParam.setIs_weight(0);
			List<WeightSkuBean> nonWeightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(nonWeightSkuList, "供应商分拣,拉取不计重商品列表数据失败");

			WeightSkuBean weightSku = null;
			String msg = null;
			boolean result = true;
			for (OrderDetailBean.Detail detail : details) {
				if (detail.getIs_weigh() == 1) {
					weightSku = weightSkuList.stream()
							.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(detail.getSku_id()))
							.findAny().orElse(null);
					if (weightSku == null) {
						msg = String.format("订单%s的称重商品%s,没有出现在分拣软件中的计重商品列表里", order_id, detail.getSku_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}
				} else {
					weightSku = nonWeightSkuList.stream()
							.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(detail.getSku_id()))
							.findAny().orElse(null);
					if (weightSku == null) {
						msg = String.format("订单%s的非称重商品%s,没有出现在分拣软件中的非计重商品列表里", order_id, detail.getSku_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}
				}

				if (weightSku.getQuantity().compareTo(detail.getQuantity()) != 0) {
					msg = String.format("订单%s里商品%s,订单详情里显示的下单数和在分拣软件中显示的下单数不一致,预期:%s,实际:%s", order_id,
							detail.getSku_id(), detail.getQuantity(), weightSku.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "供应商分拣,拉取的订单分拣商品数据(SKU)不正确");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

	@Test
	public void supplierWeightTestCase03() {
		ReporterCSS.title("测试点: 供应商分拣,进行商品称重");
		try {
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			BigDecimal std_unit_quantity = detail.getStd_unit_quantity().add(new BigDecimal("0.5"));

			SetWeightParam setWeightParam = new SetWeightParam();
			List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
			SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"),
					std_unit_quantity, false, 0);
			weights.add(weight);

			setWeightParam.setWeights(weights);
			setWeightParam.setDate(cycle_start_date);

			boolean result = weightService.setWeight(setWeightParam);
			Assert.assertEquals(result, true, "供应商分拣失败");

			WeightDataFilterParam filterParam = new WeightDataFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(cycle_start_date);
			filterParam.setSpu_ids(Arrays.asList(detail.getSpu_id()));
			filterParam.setIs_weight(detail.getIs_weigh());

			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(weightSkuList, "供应商分拣,拉取称重商品列表数据失败");

			WeightSkuBean weightSku = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(weightSku, null, "订单" + order_id + "中的商品" + detail.getSku_id() + "在称重软件中没有找到");

			String msg = null;
			if (weightSku.getWeighting_quantity().compareTo(std_unit_quantity) != 0) {
				msg = String.format("订单%s里的商品%s,供应商称重后,分拣软件显示的称重数与预期不符,预期:%s,实际:%s", order_id, sku_id,
						std_unit_quantity, weightSku.getWeighting_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			OrderDetailBean.Detail afterWightDetail = orderDetail.getDetails().stream()
					.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (afterWightDetail.getStd_real_quantity().compareTo(std_unit_quantity) != 0) {
				msg = String.format("订单%s里的商品%s,供应商称重后,订单详情显示的称重数与预期不符,预期:%s,实际:%s", order_id, sku_id,
						std_unit_quantity, afterWightDetail.getStd_real_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "供应商分拣订单后,分拣后的数据与预期不符");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

	@Test
	public void supplierWeightTestCase04() {
		ReporterCSS.title("测试点: 供应商分拣,进行商品称重(加入称重数)");
		try {
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			String spu_id = detail.getSpu_id();

			WeightDataFilterParam filterParam = new WeightDataFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(cycle_start_date);
			filterParam.setSpu_ids(Arrays.asList(spu_id));
			filterParam.setIs_weight(detail.getIs_weigh());

			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(weightSkuList, "供应商分拣,拉取称重商品列表数据失败");

			WeightSkuBean weightSku = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(weightSku, null, "订单" + order_id + "中的商品" + detail.getSku_id() + "在称重软件中没有找到");

			BigDecimal final_std_unit_quantity = null;
			if (weightSku.isHas_weighted()) {
				BigDecimal std_unit_quantity = weightSku.getWeighting_quantity();
				final_std_unit_quantity = std_unit_quantity.add(new BigDecimal("0.2"));

				SetWeightParam setWeightParam = new SetWeightParam();
				List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
				SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, std_unit_quantity,
						final_std_unit_quantity, true, 1);
				weights.add(weight);

				setWeightParam.setWeights(weights);
				setWeightParam.setDate(cycle_start_date);

				boolean result = weightService.setWeight(setWeightParam);
				Assert.assertEquals(result, true, "供应商分拣-加入称重数失败");

			} else {
				BigDecimal std_unit_quantity = weightSku.getQuantity().multiply(weightSku.getSale_ratio())
						.add(new BigDecimal("0.5"));

				SetWeightParam setWeightParam = new SetWeightParam();
				List<SetWeightParam.Weight> weights = new ArrayList<SetWeightParam.Weight>();
				SetWeightParam.Weight weight = setWeightParam.new Weight(order_id, sku_id, new BigDecimal("0"),
						std_unit_quantity, false, 0);
				weights.add(weight);

				setWeightParam.setWeights(weights);
				setWeightParam.setDate(cycle_start_date);

				boolean result = weightService.setWeight(setWeightParam);
				Assert.assertEquals(result, true, "供应商分拣失败");

				final_std_unit_quantity = std_unit_quantity.add(new BigDecimal("0.2"));

				weights = new ArrayList<SetWeightParam.Weight>();
				weight = setWeightParam.new Weight(order_id, sku_id, std_unit_quantity, final_std_unit_quantity, true,
						1);
				weights.add(weight);
				setWeightParam.setWeights(weights);

				result = weightService.setWeight(setWeightParam);
				Assert.assertEquals(result, true, "供应商分拣-加入称重数失败");
			}

			weightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(weightSkuList, "供应商分拣,拉取称重商品列表数据失败");

			weightSku = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(weightSku, null, "订单" + order_id + "中的商品" + detail.getSku_id() + "在称重软件中没有找到");

			String msg = null;
			boolean result = true;
			if (weightSku.getWeighting_quantity().compareTo(final_std_unit_quantity) != 0) {
				msg = String.format("订单%s里的商品%s,供应商称重后,分拣软件显示的称重数与预期不符,预期:%s,实际:%s", order_id, sku_id,
						final_std_unit_quantity, weightSku.getWeighting_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详细信息失败");

			OrderDetailBean.Detail afterWightDetail = orderDetail.getDetails().stream()
					.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (afterWightDetail.getStd_real_quantity().compareTo(final_std_unit_quantity) != 0) {
				msg = String.format("订单%s里的商品%s,供应商称重后,订单详情显示的称重数与预期不符,预期:%s,实际:%s", order_id, sku_id,
						final_std_unit_quantity, afterWightDetail.getStd_real_quantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "供应商分拣订单后,分拣后的数据与预期不符");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

	@Test
	public void supplierWeightTestCase05() {
		ReporterCSS.title("测试点: 供应商分拣,设置商品缺货&取消商品缺货");
		try {
			OrderDetailBean.Detail detail = NumberUtil.roundNumberInList(orderDetail.getDetails());
			String sku_id = detail.getSku_id();
			String spu_id = detail.getSpu_id();

			WeightDataFilterParam filterParam = new WeightDataFilterParam();
			filterParam.setTime_config_id(time_config_id);
			filterParam.setDate(cycle_start_date);
			filterParam.setSpu_ids(Arrays.asList(spu_id));
			filterParam.setIs_weight(detail.getIs_weigh());

			List<WeightSkuBean> weightSkuList = weightService.getWeightSkus(filterParam);
			Assert.assertNotNull(weightSkuList, "供应商分拣,拉取称重商品列表数据失败");

			WeightSkuBean weightSku = weightSkuList.stream()
					.filter(w -> w.getOrder_id().equals(order_id) && w.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(weightSku, null, "订单" + order_id + "中的商品" + detail.getSku_id() + "在称重软件中没有找到");

			BigDecimal weighting_quantity = weightSku.getWeighting_quantity();

			List<OutOfStockParam> outOfStockParams = new ArrayList<OutOfStockParam>();
			OutOfStockParam outOfStockParam = new OutOfStockParam(order_id, sku_id, weighting_quantity,
					weightSku.getSort_way());
			outOfStockParams.add(outOfStockParam);
			boolean result = weightService.outOfStock(outOfStockParams);
			Assert.assertEquals(result, true, "供应商分拣,设置商品缺货失败");

			OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

			String msg = null;
			if (!detail.isOut_of_stock()) {
				msg = String.format("订单%s里的商品%s,供应商称重设置缺货后,订单详情里没有显示为缺货状态", order_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<OutOfStockParam> unOutOfStockParams = new ArrayList<OutOfStockParam>();
			OutOfStockParam unOutOfStockParam = new OutOfStockParam(order_id, sku_id, new BigDecimal("0"), 1);
			unOutOfStockParams.add(unOutOfStockParam);
			result = weightService.unOutOfStock(unOutOfStockParams);
			Assert.assertEquals(result, true, "供应商分拣,取消缺货状态失败");

			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			detail = orderDetail.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id)).findAny().orElse(null);

			if (detail.isOut_of_stock()) {
				msg = String.format("订单%s里的商品%s,供应商称重取消缺货后,订单详情里还是显示为缺货状态", order_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "供应商分拣,设置缺货&取消缺货状态后,相关信息与预期不符");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

	@Test
	public void supplierWeightTestCase06() {
		ReporterCSS.title("测试点: 供应商分拣,获取打印标签模板");
		try {
			List<PrintTagTemplateBean> printTagTemplates = templateService.getPrintTagTemplateList();
			Assert.assertNotEquals(printTagTemplates, null, "供应商分拣,获取打印标签模板失败");
		} catch (Exception e) {
			logger.error("供应商分拣遇到错误: ", e);
			Assert.fail("供应商分拣遇到错误: ", e);
		}
	}

}
