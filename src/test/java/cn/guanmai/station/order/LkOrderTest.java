package cn.guanmai.station.order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.manage.bean.account.StUserBean;
import cn.guanmai.manage.bean.account.StationInfoBean;
import cn.guanmai.manage.bean.account.param.StUserFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerAddParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.impl.account.MgAccountServiceImpl;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.account.MgAccountService;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.param.BatchDeleteSkuParam;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderSkuFilterResultBean;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderEditParam;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.order.param.OrderSkuFilterParam;
import cn.guanmai.station.bean.order.param.OrderEditParam.OrderData;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年7月10日 下午3:35:50
 * @description: LK 单相关测试用例
 * @version: 1.0
 */

public class LkOrderTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(LkOrderTest.class);

	private OrderService orderService1;
	private OrderService orderService2;

	private String address_id;
	private String kid;
	private String time_config_id2;

	// 代售商品ID
	private String sku1_id1;
	private String sku1_id2;
	private String sku1_id3;
	private String sku1_id4;

	private String sku2_id1;
	private String sku2_id2;
	private String sku2_id3;
	private String sku2_id4;

	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
	private String tommrowStr;
	private String spu_id;
	private String spu_name;

	private String customized_station1_id;

	private String station2_id;
	private String station2_name;

	@BeforeClass
	public void initData() {
		// 总仓的登录Cookie
		Map<String, String> st1_headers = getStationCookie();
		try {
			InitDataBean initData = getInitData();

			tommrowStr = TimeUtil.calculateTime("yyyy-MM-dd 00:00", todayStr, 1, Calendar.DATE);

			LoginUserInfoService loginUserInfoService1 = new LoginUserInfoServiceImpl(st1_headers);
			LoginUserInfoBean loginUserInfo1 = loginUserInfoService1.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo1, null, "获取登录账号信息失败");

			// 总仓的Station ID
			String station1_id = loginUserInfo1.getStation_id();

			Map<String, String> mg_headers = LoginUtil.loginManage();
			Assert.assertNotEquals(mg_headers, null, "manage登录失败");

			MgAccountService mgAccountService = new MgAccountServiceImpl(mg_headers);

			List<StationInfoBean> stationInfos = mgAccountService.getAllStations();
			Assert.assertNotEquals(stationInfos, null, "获取站点列表信息失败");

			List<String> station_ids = stationInfos.stream().map(s -> s.getId()).collect(Collectors.toList());

			Assert.assertEquals(station_ids.contains(station1_id), true, "MA账号和ST账号不属于同一Group");

			Assert.assertEquals(station_ids.size() >= 2, true, "此Group下不足两个站点,无法进行LK单创建");

			// 分仓的Station ID
			for (StationInfoBean stationInfo : stationInfos) {
				if (!station1_id.equals(stationInfo.getId())) {
					if (station2_id == null) {
						station2_id = stationInfo.getId();
						station2_name = stationInfo.getName();
					} else {
						if (Integer.valueOf(station2_id.substring(1)) > Integer
								.valueOf(stationInfo.getId().substring(1))) {
							station2_id = stationInfo.getId();
							station2_name = stationInfo.getName();
						}
					}
				}
			}

			customized_station1_id = StringUtil.getCustomizedStationId(station1_id);

			StUserFilterParam stUserFilterParam = new StUserFilterParam();
			stUserFilterParam.setStation_id(station2_id);
			stUserFilterParam.setType_id(999);

			List<StUserBean> stUsers = mgAccountService.searchStationUser(stUserFilterParam);
			Assert.assertNotEquals(stUsers, null, "管理品台用户管理搜索失败");

			Assert.assertEquals(stUsers.size() >= 1, true, "分仓没有管理员账号！");

			StUserBean stUser2 = stUsers.get(0);
			String station2_user_name = stUser2.getUsername();
			BigDecimal station2_user_id = stUser2.getId();

			boolean result = mgAccountService.changeStationUserPassword(station2_user_id, "liuge1");
			Assert.assertEquals(result, true, "修改ST用户密码失败");

			// 分仓的Cookie
			Map<String, String> st2_headers = LoginUtil.loginStation(station2_user_name, "liuge1");
			Assert.assertNotEquals(st2_headers, null, "业务平台分仓登录失败");

			// 新建代售报价单
			SalemenuService salemenuService1 = new SalemenuServiceImpl(st1_headers);
			SalemenuService salemenuService2 = new SalemenuServiceImpl(st2_headers);

			CategoryService categoryService1 = new CategoryServiceImpl(st1_headers);
			CategoryService categoryService2 = new CategoryServiceImpl(st2_headers);

			AsyncService asyncService1 = new AsyncServiceImpl(st1_headers);
			AsyncService asyncService2 = new AsyncServiceImpl(st2_headers);

			orderService1 = new OrderServiceImpl(st1_headers);
			orderService2 = new OrderServiceImpl(st2_headers);

			String salemenu_name1 = "[自动化] 总仓代售报价单";
			String salemenu_name2 = "[自动化] 分仓代售报价单";

			SalemenuFilterParam salemenuFilterParam = new SalemenuFilterParam();
			salemenuFilterParam.setQ(salemenu_name1);
			salemenuFilterParam.setWith_sku_num(1);

			List<SalemenuBean> salemenuList1 = salemenuService1.searchSalemenu(salemenuFilterParam);
			Assert.assertNotEquals(salemenuList1, null, "报价单搜索过滤失败");
			String salemenu_id1 = null;
			String salemenu_id2 = null;

			SpuBean spu = initData.getSpu();
			spu_id = spu.getId();
			spu_name = spu.getName();
			String supplier_id = initData.getSupplier().getId();
			String spec_purchase_id = initData.getPurchaseSpec().getId();

			// 没有代售报价单就新建
			if (salemenuList1.size() == 0) {
				// 获取运营时间
				ServiceTimeService serviceTimeService1 = new ServiceTimeServiceImpl(st1_headers);

				List<ServiceTimeBean> serviceTimes1 = serviceTimeService1.allTimeConfig();
				Assert.assertNotEquals(serviceTimes1, null, "获取运营时间列表失败");

				String time_config_id1 = serviceTimes1.get(0).getId();

				SalemenuBean salemenu1 = new SalemenuBean();
				salemenu1.setName(salemenu_name1);
				salemenu1.setIs_active(1);
				salemenu1.setSupplier_name("主仓代售");
				salemenu1.setTime_config_id(time_config_id1);
				salemenu1.setAbout("自动化用来测试LK的,请不要修改报价单以及报价单里面的数据");
				JSONArray targets = new JSONArray();
				targets.add(station2_id);
				salemenu1.setTargets(targets);

				salemenu_id1 = salemenuService1.createSalemenu(salemenu1);
				Assert.assertNotEquals(salemenu_id1, null, "新建报价单失败");
			} else {
				SalemenuBean salemenu1 = salemenuList1.get(0);
				salemenu_id1 = salemenu1.getId();
				JSONArray targets = new JSONArray();
				targets.add(station2_id);
				salemenu1.setTargets(targets);
				salemenu1.setIs_active(1);
				result = salemenuService1.updateSalemenu(salemenu1);
				Assert.assertEquals(result, true, "修改报价单失败");

				if (salemenu1.getSku_num() > 0) {
					// 防止数据以及被修改,先删除后创建
					JSONArray salemenu_ids = new JSONArray();
					salemenu_ids.add(salemenu_id1);

					BatchDeleteSkuParam batchDeleteSkuParam = new BatchDeleteSkuParam(new JSONArray(), new JSONArray(),
							new JSONArray(), salemenu_ids, null, null, 1);
					result = categoryService1.batchDeleteSaleSku(batchDeleteSkuParam);
					Assert.assertEquals(result, true, "批量删除销售规格,异步任务创建成功");

					Thread.sleep(1000);

					List<AsyncTaskResultBean> asyncTasks = asyncService1.getAsyncTaskResultList();
					Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

					AsyncTaskResultBean asyncTask = asyncTasks.stream().filter(at -> at.getType() == 12).findFirst()
							.orElse(null);

					BigDecimal task_id = asyncTask.getTask_id();

					result = asyncService1.getAsyncTaskResult(task_id, "失败0");
					Assert.assertEquals(result, true, "批量删除销售规格的异步任务执行失败");
				}
			}

			SkuBean sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			BigDecimal std_sale_price = new BigDecimal("1");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("分仓代售总仓的商品");
			sku.setSupplier_id(supplier_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setPurchase_spec_id(spec_purchase_id);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|分投");
			sku.setSalemenu_id(salemenu_id1);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku1_id1 = categoryService1.createSaleSku(sku);
			Assert.assertNotEquals(sku1_id1, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("2");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("分仓代售总仓的商品");
			sku.setSupplier_id(supplier_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setPurchase_spec_id(spec_purchase_id);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|投");
			sku.setSalemenu_id(salemenu_id1);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku1_id2 = categoryService1.createSaleSku(sku);
			Assert.assertNotEquals(sku1_id1, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("3");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("分仓代售总仓的商品");
			sku.setSupplier_id(supplier_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setPurchase_spec_id(spec_purchase_id);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|分");
			sku.setSalemenu_id(salemenu_id1);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku1_id3 = categoryService1.createSaleSku(sku);
			Assert.assertNotEquals(sku1_id3, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("4");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("总仓的代售商品");
			sku.setSupplier_id(supplier_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setPurchase_spec_id(spec_purchase_id);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|无");
			sku.setSalemenu_id(salemenu_id1);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku1_id4 = categoryService1.createSaleSku(sku);
			Assert.assertNotEquals(sku1_id4, null, "新建SKU失败");

			salemenuFilterParam = new SalemenuFilterParam();
			salemenuFilterParam.setQ(salemenu_name2);
			salemenuFilterParam.setWith_sku_num(1);

			List<SalemenuBean> salemenuList2 = salemenuService2.searchSalemenu(salemenuFilterParam);
			Assert.assertNotEquals(salemenuList2, null, "报价单搜索过滤失败");

			// 分仓的代售报价单
			// 获取运营时间
			ServiceTimeService serviceTimeService2 = new ServiceTimeServiceImpl(st2_headers);

			List<ServiceTimeBean> serviceTimes2 = serviceTimeService2.allTimeConfig();
			Assert.assertNotEquals(serviceTimes2, null, "获取运营时间列表失败");

			time_config_id2 = serviceTimes2.get(0).getId();
			if (salemenuList2.size() == 0) {
				SalemenuBean salemenu2 = new SalemenuBean();
				salemenu2.setName(salemenu_name2);
				salemenu2.setIs_active(1);
				salemenu2.setSupplier_name("分仓代售");
				salemenu2.setTime_config_id(time_config_id2);
				salemenu2.setAbout("自动化用来测试LK的,请不要修改报价单以及报价单里面的数据");
				JSONArray targets = new JSONArray();
				salemenu2.setTargets(targets);

				salemenu_id2 = salemenuService2.createSalemenu(salemenu2);
				Assert.assertNotEquals(salemenu_id2, null, "新建报价单失败");
			} else {
				SalemenuBean salemenu2 = salemenuList2.get(0);
				salemenu_id2 = salemenu2.getId();
				JSONArray targets = new JSONArray();
				salemenu2.setTargets(targets);
				salemenu2.setIs_active(1);
				salemenu2.setTime_config_id(time_config_id2);
				result = salemenuService2.updateSalemenu(salemenu2);
				Assert.assertEquals(result, true, "修改报价单失败");

				if (salemenu2.getSku_num() > 0) {
					// 防止数据以及被修改,先删除后创建
					JSONArray salemenu_ids = new JSONArray();
					salemenu_ids.add(salemenu_id2);

					BatchDeleteSkuParam batchDeleteSkuParam = new BatchDeleteSkuParam(new JSONArray(), new JSONArray(),
							new JSONArray(), salemenu_ids, null, null, 1);
					result = categoryService2.batchDeleteSaleSku(batchDeleteSkuParam);
					Assert.assertEquals(result, true, "批量删除销售规格,异步任务创建成功");

					Thread.sleep(1000);

					List<AsyncTaskResultBean> asyncTasks = asyncService2.getAsyncTaskResultList();
					Assert.assertNotNull(asyncTasks, "获取异步任务列表失败");

					AsyncTaskResultBean asyncTask = asyncTasks.stream().filter(at -> at.getType() == 12).findFirst()
							.orElse(null);

					BigDecimal task_id = asyncTask.getTask_id();

					result = asyncService2.getAsyncTaskResult(task_id, "失败0");
					Assert.assertEquals(result, true, "批量删除销售规格的异步任务执行失败");
				}
			}

			// 分仓新建代售商品
			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("1");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("总仓的代售商品");
			sku.setSupplier_id(station1_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setSlitting(1);
			sku.setPartframe(1);
			sku.setPurchase_spec_id(sku1_id1);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|分投");
			sku.setSalemenu_id(salemenu_id2);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku2_id1 = categoryService2.createSaleSku(sku);
			Assert.assertNotEquals(sku2_id1, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("2");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("总仓的代售商品");
			sku.setSupplier_id(station1_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setSlitting(0);
			sku.setPartframe(1);
			sku.setPurchase_spec_id(sku1_id2);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|投");
			sku.setSalemenu_id(salemenu_id2);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku2_id2 = categoryService2.createSaleSku(sku);
			Assert.assertNotEquals(sku2_id2, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("3");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("总仓的代售商品");
			sku.setSupplier_id(station1_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setSlitting(1);
			sku.setPartframe(0);
			sku.setPurchase_spec_id(sku1_id3);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|分");
			sku.setSalemenu_id(salemenu_id2);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku2_id3 = categoryService2.createSaleSku(sku);
			Assert.assertNotEquals(sku2_id3, null, "新建SKU失败");

			sku = new SkuBean();
			sku.setSpu_id(spu_id);
			sku.setOuter_id("");
			std_sale_price = new BigDecimal("4");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("分仓代售总仓的商品");
			sku.setSupplier_id(station1_id);
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setSlitting(0);
			sku.setPartframe(0);
			sku.setPurchase_spec_id(sku1_id4);
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu_name + "|无");
			sku.setSalemenu_id(salemenu_id2);
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);
			sku2_id4 = categoryService2.createSaleSku(sku);
			Assert.assertNotEquals(sku2_id4, null, "新建SKU失败");

			// 代售商户,没有就新建
			MgCustomerService customerService = new MgCustomerServiceImpl(mg_headers);

			MgCustomerFilterParam customerFilterParam = new MgCustomerFilterParam();
			customerFilterParam.setSearch_text("DS");

			List<MgCustomerBean> customers = customerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customers, null, "搜索过滤商户信息失败");

			MgCustomerBean mgCustomer = null;
			if (customers.size() > 0) {
				String regex = "DS[0-9]{13}";
				for (MgCustomerBean customer : customers) {
					result = Pattern.matches(regex, customer.getUsername());
					if (result) {
						mgCustomer = customer;
						break;
					}
				}
			}

			CustomerBaseInfoBean customerBaseInfo = customerService.getCustomerBaseInfo();
			Assert.assertNotEquals(customerBaseInfo, null, "获取商户信息失败");

			List<String> salemenu_ids = new ArrayList<String>();
			salemenu_ids.add(salemenu_id2);

			String bs_user_name = null;
			if (mgCustomer == null) {
				bs_user_name = "DS" + TimeUtil.getLongTime();
				String company_name = "自动化代售专用公司";
				String telephone = "101" + StringUtil.getRandomNumber(8);

				String name = "自动化代售专用商户";

				MgCustomerAddParam customerAddParam = new MgCustomerAddParam();
				customerAddParam.setUsername(bs_user_name);
				customerAddParam.setCompany_name(company_name);
				customerAddParam.setPassword("1qaz2wsx");
				customerAddParam.setEditPassword(false);
				customerAddParam.setTelephone(telephone);
				customerAddParam.setCustomer_type(0);
				customerAddParam.setSettle_way(1);
				customerAddParam.setPayer_name(name);
				customerAddParam.setPayer_telephone(telephone);
				customerAddParam.setFinance_status(0);
				customerAddParam.setWhitelist(1);
				customerAddParam.setCheck_out(1);
				customerAddParam.setPay_method(1);
				customerAddParam.setSettle_date_type(1);
				customerAddParam.setIs_credit(0);
				customerAddParam.setRestaurant_name(name);
				customerAddParam.setReceiver_name(name);
				customerAddParam.setReceiver_telephone(telephone);

				String district_code = customerBaseInfo.getStation_district_code_map().get(station2_id);

				List<CustomerBaseInfoBean.District> districts = customerBaseInfo.getDistrict();
				OK: for (CustomerBaseInfoBean.District district : districts) {
					if (district.getCity_code().equals(district_code)) {
						for (CustomerBaseInfoBean.District.Area area : district.getAreas()) {
							for (CustomerBaseInfoBean.District.Area.Street street : area.getStreets()) {
								StringBuffer restaurant_address = new StringBuffer();
								restaurant_address.append(district.getCity_name());
								restaurant_address.append(area.getArea_name());
								restaurant_address.append(street.getStreet_name());
								customerAddParam.setRestaurant_address(restaurant_address.toString());
								customerAddParam.setMap_address(restaurant_address.toString());

								customerAddParam.setDistrict_code(district.getCity_code());
								customerAddParam.setArea_level1(area.getArea_code());
								customerAddParam.setArea_level2(street.getStreet_code());
								break OK;
							}
						}
					}

				}

				customerAddParam.setSalemenu_ids(JSONArray.parseArray(salemenu_ids.toString()));

				address_id = customerService.createCustomer(customerAddParam);
				Assert.assertNotEquals(address_id, null, "新建商户失败");

				MgCustomerDetailBean mgCustomerDetail = customerService.getCustomerDetailInfoBySID(address_id);
				Assert.assertNotEquals(mgCustomerDetail, null, "获取商户详细信息失败");

				kid = mgCustomerDetail.getKID();
			} else {
				bs_user_name = mgCustomer.getUsername();
				address_id = mgCustomer.getSID();
				kid = mgCustomer.getKID();
			}
			address_id = address_id.replaceAll("S[0]*", "");
			kid = kid.replaceAll("K[0]*", "");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void lkOrderTestCase01() {
		ReporterCSS.title("测试点: 新建PL单,验证是否生成LK单");
		try {
			// 下单之前的代售商品下单数
			Map<String, OrderSkuFilterResultBean> beforeOrderSkuMap = orderSkuMap();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService2.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");

			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = orderReceiveTimes.stream()
					.filter(t -> t.getTime_config_id().equals(time_config_id2)).findAny().orElse(null);
			Assert.assertNotEquals(orderReceiveTime, null, "商户绑定的运营时间与预期不符,请检测商户绑定的报价单是否被修改");

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id2 + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { spu_name };
			OrderCreateParam orderCreateParam = orderService2.searchOrderSkus(address_id, time_config_id2, search_texts,
					4);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空,与预期不符,请检测报价单里的商品");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(kid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id2);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService2.createOrder(orderCreateParam);
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			String order_id = orderResponse.getData().getNew_order_ids().getString(0);
			OrderDetailBean orderDetail = orderService2.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String create_time = orderDetail.getCreate_time();
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setStart_date_new(todayStr);
			orderFilterParam.setEnd_date_new(tommrowStr);
			orderFilterParam.setSearch_text(station2_name);
			orderFilterParam.setLimit(10);
			orderFilterParam.setOffset(0);

			// 查询LK单,没有分切能力的商品单独生成一个LK单,创建时间在PL单后
			List<OrderBean> orders = new ArrayList<OrderBean>();
			int number = 0;
			while (number++ < 10 && orders.size() == 0) {
				List<OrderBean> tempOrders = orderService1.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单失败");
				for (OrderBean tempOrder : tempOrders) {
					if (tempOrder.getId().startsWith("LK") || tempOrder.getId().startsWith(customized_station1_id + "c")
							|| tempOrder.getId().startsWith(customized_station1_id + "d")
									&& tempOrder.getCustomer().getAddress_id().equals(station2_id)) {
						String lk_create_time = tempOrder.getCreate_time();
						// 因为Order服务部署在多台机器上,机器时间可能差了那么1-2秒,这里给LK单时间多加5秒
						lk_create_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", lk_create_time, 5,
								Calendar.SECOND);
						if (TimeUtil.compareDate("yyyy-MM-dd HH:mm:dd", lk_create_time, create_time) >= 0) {
							orders.add(tempOrder);
						}
					}
				}
				Thread.sleep(3000);
			}
			Assert.assertEquals(orders.size() > 0, true, "PL单在创建后30S,没有找到对应的LK单");

			// 下单之后的代售商品下单数
			Map<String, OrderSkuFilterResultBean> afterOrderSkuMap = orderSkuMap();

			String msg = null;
			boolean result = true;

			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			// 分切、投框能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id1)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id1);
				BigDecimal sku1_1_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id1)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_1_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id1)) {
					expected_sku1_1_std_unit_quantity = beforeOrderSkuMap.get(sku1_id1).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_1_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_1_std_unit_quantity.compareTo(expected_sku1_1_std_unit_quantity) != 0) {
					msg = String.format("PL单%s创建后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id1, expected_sku1_1_std_unit_quantity,
							sku1_1_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				// 这个单里只有具有分切能力的订单
				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().startsWith(spu_name + "|分")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有分切能力的商品,但是却出现了没有分切能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有[分切&投框]能力的商品%s的LK单", sku1_id1);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 投框能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id2)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id2);
				BigDecimal sku1_2_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id2)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_2_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id2)) {
					expected_sku1_2_std_unit_quantity = beforeOrderSkuMap.get(sku1_id2).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_2_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_2_std_unit_quantity.compareTo(expected_sku1_2_std_unit_quantity) != 0) {
					msg = String.format("PL单%s创建后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id2, expected_sku1_2_std_unit_quantity,
							sku1_2_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().equals(spu_name + "|投")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有投框能力的商品,但是却出现了没有投框能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有[投框]能力的商品%s的LK单", sku1_id2);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 分切能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id3)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id3);
				BigDecimal sku1_3_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id3)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_3_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id3)) {
					expected_sku1_3_std_unit_quantity = beforeOrderSkuMap.get(sku1_id3).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_3_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_3_std_unit_quantity.compareTo(expected_sku1_3_std_unit_quantity) != 0) {
					msg = String.format("PL单%s创建后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id3, expected_sku1_3_std_unit_quantity,
							sku1_3_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().startsWith(spu_name + "|分")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有分切能力的商品,但是却出现了没有分切能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			} else {
				msg = String.format("PL单创建后,没有找到有[分切]能力的商品%s的LK单", sku1_id3);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 无任何能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id4)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id4);
				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id4)).findAny()
						.orElse(null);
				if (orderSkuFilterResult.getStd_unit_quantity().compareTo(detail.getStd_real_quantity()) != 0) {
					msg = String.format("PL单%s创建后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id4, detail.getStd_real_quantity(),
							orderSkuFilterResult.getStd_unit_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = lkOrderDetail.getDetails().stream()
						.filter(s -> !s.getSku_id().equals(sku1_id4)).map(s -> s.getSku_id())
						.collect(Collectors.toList());
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只有%s,但是却出现了别的商品%s", lk_order_id, sku1_id4, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到没有任何能力的商品%s的LK单", sku1_id4);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "PL创建后,对应的LK单的数据与预期不符");
		} catch (Exception e) {
			logger.error("下单遇到错误: ", e);
			Assert.fail("下单遇到错误: ", e);
		}
	}

	@Test
	public void lkOrderTestCase02() {
		ReporterCSS.title("测试点: 修改PL单,删除商品,验证LK单是否同步修改");
		try {
			List<OrderReceiveTimeBean> orderReceiveTimes = orderService2.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = orderReceiveTimes.stream()
					.filter(t -> t.getTime_config_id().equals(time_config_id2)).findAny().orElse(null);
			Assert.assertNotEquals(orderReceiveTime, null, "商户绑定的运营时间与预期不符,请检测商户绑定的报价单是否被修改");

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id2 + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { spu_name };
			OrderCreateParam orderCreateParam = orderService2.searchOrderSkus(address_id, time_config_id2, search_texts,
					4);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空,与预期不符,请检测报价单里的商品");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(kid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id2);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService2.createOrder(orderCreateParam);
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			String order_id = orderResponse.getData().getNew_order_ids().getString(0);
			OrderDetailBean orderDetail = orderService2.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String create_time = orderDetail.getCreate_time();
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setStart_date_new(todayStr);
			orderFilterParam.setEnd_date_new(tommrowStr);
			orderFilterParam.setSearch_text(station2_name);
			orderFilterParam.setLimit(10);
			orderFilterParam.setOffset(0);

			// 查询LK单,没有分切能力的商品单独生成一个LK单,创建时间在PL单后
			List<OrderBean> orders = new ArrayList<OrderBean>();
			int number = 0;
			while (number++ < 10 && orders.size() == 0) {
				List<OrderBean> tempOrders = orderService1.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单失败");
				for (OrderBean tempOrder : tempOrders) {
					if (tempOrder.getId().startsWith("LK") || tempOrder.getId().startsWith(customized_station1_id + "c")
							|| tempOrder.getId().startsWith(customized_station1_id + "d")
									&& tempOrder.getCustomer().getAddress_id().equals(station2_id)) {
						String lk_create_time = tempOrder.getCreate_time();
						// 因为Order服务部署在多台机器上,机器时间可能差了那么1-2秒,这里给LK单时间多加5秒
						lk_create_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", lk_create_time, 5,
								Calendar.SECOND);
						if (TimeUtil.compareDate("yyyy-MM-dd HH:mm:dd", lk_create_time, create_time) >= 0) {
							orders.add(tempOrder);
						}
					}
				}
				Thread.sleep(3000);
			}
			Assert.assertEquals(orders.size() > 0, true, "PL单在创建后30S,没有找到对应的LK单");

			Map<String, OrderSkuFilterResultBean> beforeOrderSkuMap = orderSkuMap();

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			OrderCreateParam.OrderSku orderSku = orderSkus.stream().filter(s -> s.getSku_id().equals(sku2_id4))
					.findAny().orElse(null);
			orderSkus.remove(orderSku);

			OrderEditParam orderEditParam = new OrderEditParam();
			orderEditParam.setCombine_goods_map(new HashMap<>());
			orderEditParam.setOrder_id(order_id);
			orderEditParam.setDetails(orderSkus);

			// 设置收货时间
			OrderData orderData = orderEditParam.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			orderEditParam.setOrder_data(orderData);

			boolean result = orderService2.editOrder(orderEditParam);
			Assert.assertEquals(result, true, "订单修改失败");

			Map<String, OrderSkuFilterResultBean> afterOrderSkuMap = orderSkuMap();

			String msg = null;
			if (afterOrderSkuMap.containsKey(sku1_id1)) {
				BigDecimal expected_std_unit_quantity = beforeOrderSkuMap.get(sku1_id1).getStd_unit_quantity();
				BigDecimal actual_std_unit_quantity = afterOrderSkuMap.get(sku1_id1).getStd_unit_quantity();
				if (expected_std_unit_quantity.compareTo(actual_std_unit_quantity) != 0) {
					msg = String.format("修改PL单%s,没有对商品%s做更改,但是对应的LK单中的商品%s下单数却发生了变化,预期:%s,实际:%s", order_id, sku2_id1,
							sku1_id1, expected_std_unit_quantity, actual_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单%s中没有删除商品%s,但是对应的LK单中却没有了商品%s", order_id, sku2_id1, sku1_id1);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (afterOrderSkuMap.containsKey(sku1_id2)) {
				BigDecimal expected_std_unit_quantity = beforeOrderSkuMap.get(sku1_id2).getStd_unit_quantity();
				BigDecimal actual_std_unit_quantity = afterOrderSkuMap.get(sku1_id2).getStd_unit_quantity();
				if (expected_std_unit_quantity.compareTo(actual_std_unit_quantity) != 0) {
					msg = String.format("修改PL单%s,没有对商品%s做更改,但是对应的LK单中的商品%s下单数却发生了变化,预期:%s,实际:%s", order_id, sku2_id2,
							sku1_id2, expected_std_unit_quantity, actual_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单%s中没有删除商品%s,但是对应的LK单中却没有了商品%s", order_id, sku2_id2, sku1_id2);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (afterOrderSkuMap.containsKey(sku1_id3)) {
				BigDecimal expected_std_unit_quantity = beforeOrderSkuMap.get(sku1_id3).getStd_unit_quantity();
				BigDecimal actual_std_unit_quantity = afterOrderSkuMap.get(sku1_id3).getStd_unit_quantity();
				if (expected_std_unit_quantity.compareTo(actual_std_unit_quantity) != 0) {
					msg = String.format("修改PL单%s,没有对商品%s做更改,但是对应的LK单中的商品%s下单数却发生了变化,预期:%s,实际:%s", order_id, sku2_id3,
							sku1_id3, expected_std_unit_quantity, actual_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单%s中没有删除商品%s,但是对应的LK单中却没有了商品%s", order_id, sku2_id3, sku1_id3);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (afterOrderSkuMap.containsKey(sku1_id4)) {
				String before_order_id = beforeOrderSkuMap.get(sku1_id4).getOrder_id();
				String after_order_id = afterOrderSkuMap.get(sku1_id4).getOrder_id();
				if (before_order_id.equalsIgnoreCase(after_order_id)) {
					msg = String.format("PL单%s,商品%s已经删除,但是对应的LK单却没有删除", order_id, sku2_id4, before_order_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "修改订单PL单,对应的LK单更新与预期不同");
		} catch (Exception e) {
			logger.error("下单遇到错误: ", e);
			Assert.fail("下单遇到错误: ", e);
		}
	}

	@Test
	public void lkOrderTestCase03() {
		ReporterCSS.title("测试点: 修改PL单,新增商品,验证LK单是否同步修改");
		try {

			Map<String, OrderSkuFilterResultBean> beforeOrderSkuMap = orderSkuMap();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService2.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = orderReceiveTimes.stream()
					.filter(t -> t.getTime_config_id().equals(time_config_id2)).findAny().orElse(null);
			Assert.assertNotEquals(orderReceiveTime, null, "商户绑定的运营时间与预期不符,请检测商户绑定的报价单是否被修改");

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id2 + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { spu_name };
			OrderCreateParam orderCreateParam = orderService2.searchOrderSkus(address_id, time_config_id2, search_texts,
					2);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空,与预期不符,请检测报价单里的商品");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(kid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id2);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService2.createOrder(orderCreateParam);
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			String order_id = orderResponse.getData().getNew_order_ids().getString(0);
			OrderDetailBean orderDetail = orderService2.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			// 修改订单
			OrderCreateParam tempOrderCreateParam = orderService2.searchOrderSkus(address_id, time_config_id2,
					search_texts, 4);
			Assert.assertEquals(tempOrderCreateParam != null && tempOrderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空,与预期不符,请检测报价单里的商品");

			OrderEditParam orderEditParam = new OrderEditParam();
			orderEditParam.setCombine_goods_map(new HashMap<>());
			orderEditParam.setOrder_id(order_id);
			orderEditParam.setDetails(tempOrderCreateParam.getDetails());

			// 设置收货时间
			OrderData orderData = orderEditParam.new OrderData();
			orderData.setReceive_begin_time(orderCreateParam.getReceive_begin_time());
			orderData.setReceive_end_time(orderCreateParam.getReceive_end_time());
			orderData.setRemark(StringUtil.getRandomString(6));
			orderEditParam.setOrder_data(orderData);

			boolean result = orderService2.editOrder(orderEditParam);
			Assert.assertEquals(result, true, "订单修改失败");

			String create_time = orderDetail.getCreate_time();
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setStart_date_new(todayStr);
			orderFilterParam.setEnd_date_new(tommrowStr);
			orderFilterParam.setSearch_text(station2_name);
			orderFilterParam.setLimit(10);
			orderFilterParam.setOffset(0);

			// 查询LK单,没有分切能力的商品单独生成一个LK单,创建时间在PL单后
			List<OrderBean> orders = new ArrayList<OrderBean>();
			int number = 0;
			while (number++ < 10 && orders.size() == 0) {
				List<OrderBean> tempOrders = orderService1.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单失败");
				for (OrderBean tempOrder : tempOrders) {
					if (tempOrder.getId().startsWith("LK") || tempOrder.getId().startsWith(customized_station1_id + "c")
							|| tempOrder.getId().startsWith(customized_station1_id + "c")
									&& tempOrder.getCustomer().getAddress_id().equals(station2_id)) {
						String lk_create_time = tempOrder.getCreate_time();
						// 因为Order服务部署在多台机器上,机器时间可能差了那么1-2秒,这里给LK单时间多加5秒
						lk_create_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", lk_create_time, 5,
								Calendar.SECOND);
						if (TimeUtil.compareDate("yyyy-MM-dd HH:mm:dd", lk_create_time, create_time) >= 0) {
							orders.add(tempOrder);
						}
					}
				}
				Thread.sleep(3000);
			}
			Assert.assertEquals(orders.size() > 0, true, "PL单在创建后30S,没有找到对应的LK单");

			Map<String, OrderSkuFilterResultBean> afterOrderSkuMap = orderSkuMap();

			String msg = null;

			orderDetail = orderService2.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + "详细信息失败");

			List<OrderDetailBean.Detail> details = orderDetail.getDetails();

			// 分切、投框能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id1)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id1);
				BigDecimal sku1_1_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id1)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_1_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id1)) {
					expected_sku1_1_std_unit_quantity = beforeOrderSkuMap.get(sku1_id1).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_1_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_1_std_unit_quantity.compareTo(expected_sku1_1_std_unit_quantity) != 0) {
					msg = String.format("PL单%s修改后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id1, expected_sku1_1_std_unit_quantity,
							sku1_1_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().startsWith(spu_name + "|分")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有分切能力的商品,但是却出现了没有分切能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有商品%s的LK单", sku1_id1);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 投框能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id2)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id2);
				BigDecimal sku1_2_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id2)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_2_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id2)) {
					expected_sku1_2_std_unit_quantity = beforeOrderSkuMap.get(sku1_id2).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_2_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_2_std_unit_quantity.compareTo(expected_sku1_2_std_unit_quantity) != 0) {
					msg = String.format("PL单%s修改后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id2, expected_sku1_2_std_unit_quantity,
							sku1_2_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().equals(spu_name + "|投")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有投框能力的商品,但是却出现了没有投框能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有商品%s的LK单", sku1_id2);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 分切能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id3)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id3);
				BigDecimal sku1_3_std_unit_quantity = orderSkuFilterResult.getStd_unit_quantity();

				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id3)).findAny()
						.orElse(null);
				BigDecimal expected_sku1_3_std_unit_quantity = null;
				if (beforeOrderSkuMap.containsKey(sku1_id3)) {
					expected_sku1_3_std_unit_quantity = beforeOrderSkuMap.get(sku1_id3).getStd_unit_quantity()
							.add(detail.getStd_unit_quantity());
				} else {
					expected_sku1_3_std_unit_quantity = detail.getStd_unit_quantity();
				}

				if (sku1_3_std_unit_quantity.compareTo(expected_sku1_3_std_unit_quantity) != 0) {
					msg = String.format("PL单%s修改后,对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id3, expected_sku1_3_std_unit_quantity,
							sku1_3_std_unit_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = new ArrayList<String>();
				for (OrderDetailBean.Detail lkDetail : lkOrderDetail.getDetails()) {
					if (lkDetail.getSpu_id().equals(spu_id)) {
						if (!lkDetail.getSku_name().startsWith(spu_name + "|分")) {
							other_sku_ids.add(lkDetail.getSku_id());
						}
					}
				}
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只能存在有分切能力的商品,但是却出现了没有分切能力的商品%s", lk_order_id, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有商品%s的LK单", sku1_id3);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			// 无任何能力的商品
			if (afterOrderSkuMap.containsKey(sku1_id4)) {
				OrderSkuFilterResultBean orderSkuFilterResult = afterOrderSkuMap.get(sku1_id4);
				OrderDetailBean.Detail detail = details.stream().filter(s -> s.getSku_id().equals(sku2_id4)).findAny()
						.orElse(null);
				if (orderSkuFilterResult.getStd_unit_quantity().compareTo(detail.getStd_real_quantity()) != 0) {
					msg = String.format("PL单%s后,修改对应的LK单%s里的商品%s下单数(基本单位)与预期不符,预期:%s,实际:%s", order_id,
							orderSkuFilterResult.getOrder_id(), sku1_id4, detail.getStd_real_quantity(),
							orderSkuFilterResult.getStd_unit_quantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String lk_order_id = orderSkuFilterResult.getOrder_id();
				OrderDetailBean lkOrderDetail = orderService1.getOrderDetailById(lk_order_id);
				Assert.assertNotEquals(lkOrderDetail, null, "获取LK单" + lk_order_id + "详情失败");

				List<String> other_sku_ids = lkOrderDetail.getDetails().stream()
						.filter(s -> !s.getSku_id().equals(sku1_id4)).map(s -> s.getSku_id())
						.collect(Collectors.toList());
				if (other_sku_ids.size() > 0) {
					msg = String.format("LK单%s的商品预期只有%s,但是却出现了别的商品%s", lk_order_id, sku1_id4, other_sku_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				msg = String.format("PL单创建后,没有找到有商品%s的LK单", sku1_id4);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "PL创建后,对应的LK单的数据与预期不符");

		} catch (Exception e) {
			logger.error("下单遇到错误: ", e);
			Assert.fail("下单遇到错误: ", e);
		}
	}

	@Test
	public void lkOrderTestCase04() {
		ReporterCSS.title("测试点: 删除PL单,查看LK单是否同步删除数据");
		try {
			Map<String, OrderSkuFilterResultBean> beforeOrderSkuMap = orderSkuMap();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService2.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = orderReceiveTimes.stream()
					.filter(t -> t.getTime_config_id().equals(time_config_id2)).findAny().orElse(null);
			Assert.assertNotEquals(orderReceiveTime, null, "商户绑定的运营时间与预期不符,请检测商户绑定的报价单是否被修改");

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id2 + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { spu_name };
			OrderCreateParam orderCreateParam = orderService2.searchOrderSkus(address_id, time_config_id2, search_texts,
					4);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空,与预期不符,请检测报价单里的商品");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(kid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id2);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService2.createOrder(orderCreateParam);
			Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			String order_id = orderResponse.getData().getNew_order_ids().getString(0);
			OrderDetailBean orderDetail = orderService2.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

			String create_time = orderDetail.getCreate_time();
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setStart_date_new(todayStr);
			orderFilterParam.setEnd_date_new(tommrowStr);
			orderFilterParam.setSearch_text(station2_name);
			orderFilterParam.setLimit(10);
			orderFilterParam.setOffset(0);

			// 查询LK单,没有分切能力的商品单独生成一个LK单,创建时间在PL单后
			List<OrderBean> orders = new ArrayList<OrderBean>();
			int number = 0;
			while (number++ < 10 && orders.size() == 0) {
				List<OrderBean> tempOrders = orderService1.searchOrder(orderFilterParam);
				Assert.assertNotEquals(tempOrders, null, "搜索过滤订单失败");
				for (OrderBean tempOrder : tempOrders) {
					if (tempOrder.getId().startsWith("LK") || tempOrder.getId().startsWith(customized_station1_id + "c")
							|| tempOrder.getId().startsWith(customized_station1_id + "d")
									&& tempOrder.getCustomer().getAddress_id().equals(station2_id)) {
						String lk_create_time = tempOrder.getCreate_time();
						// 因为Order服务部署在多台机器上,机器时间可能差了那么1-2秒,这里给LK单时间多加5秒
						lk_create_time = TimeUtil.calculateTime("yyyy-MM-dd HH:mm:ss", lk_create_time, 5,
								Calendar.SECOND);
						if (TimeUtil.compareDate("yyyy-MM-dd HH:mm:dd", lk_create_time, create_time) >= 0) {
							orders.add(tempOrder);
						}
					}
				}
				Thread.sleep(3000);
			}
			Assert.assertEquals(orders.size() > 0, true, "PL单" + order_id + "在创建后30S,没有找到对应的LK单");

			boolean result = orderService2.deleteOrder(order_id);
			Assert.assertEquals(result, true, "删除订单失败");

			Thread.sleep(2000);

			String msg = null;
			Map<String, OrderSkuFilterResultBean> afterOrderSkuMap = orderSkuMap();
			if (beforeOrderSkuMap.containsKey(sku1_id1)) {
				if (afterOrderSkuMap.containsKey(sku1_id1)) {
					OrderSkuFilterResultBean beforeOrderSku = beforeOrderSkuMap.get(sku1_id1);
					OrderSkuFilterResultBean afterOrderSku = afterOrderSkuMap.get(sku1_id1);
					if (beforeOrderSku.getOrder_id().equals(afterOrderSku.getOrder_id())) {
						if (beforeOrderSku.getStd_unit_quantity()
								.compareTo(afterOrderSku.getStd_unit_quantity()) != 0) {
							msg = String.format("PL单删除后,LK单中存在的商品%下单数发生了变化,之前:%s,之后:%s", sku1_id1,
									beforeOrderSku.getStd_unit_quantity(), afterOrderSku.getStd_unit_quantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("PL单删除后,LK单中存在的商品%s所在LK单发生了变化,之前:%s,之后:%s", sku1_id1,
								beforeOrderSku.getOrder_id(), afterOrderSku.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("PL单%s删除后,把之前LK单中存在的商品%s也删除了", order_id, sku1_id1);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				if (afterOrderSkuMap.containsKey(sku1_id1)) {
					msg = String.format("PL单%s删除后,LK单中的商品%s没有对应删除", order_id, sku1_id1);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (beforeOrderSkuMap.containsKey(sku1_id2)) {
				if (afterOrderSkuMap.containsKey(sku1_id2)) {
					OrderSkuFilterResultBean beforeOrderSku = beforeOrderSkuMap.get(sku1_id2);
					OrderSkuFilterResultBean afterOrderSku = afterOrderSkuMap.get(sku1_id2);
					if (beforeOrderSku.getOrder_id().equals(afterOrderSku.getOrder_id())) {
						if (beforeOrderSku.getStd_unit_quantity()
								.compareTo(afterOrderSku.getStd_unit_quantity()) != 0) {
							msg = String.format("PL单删除后,LK单中存在的商品%下单数发生了变化,之前:%s,之后:%s", sku1_id2,
									beforeOrderSku.getStd_unit_quantity(), afterOrderSku.getStd_unit_quantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("PL单删除后,LK单中存在的商品%s所在LK单发生了变化,之前:%s,之后:%s", sku1_id2,
								beforeOrderSku.getOrder_id(), afterOrderSku.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("PL单%s删除后,把之前LK单中存在的商品%s也删除了", order_id, sku1_id2);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				if (afterOrderSkuMap.containsKey(sku1_id2)) {
					msg = String.format("PL单%s删除后,LK单中的商品%s没有对应删除", order_id, sku1_id2);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (beforeOrderSkuMap.containsKey(sku1_id3)) {
				if (afterOrderSkuMap.containsKey(sku1_id3)) {
					OrderSkuFilterResultBean beforeOrderSku = beforeOrderSkuMap.get(sku1_id3);
					OrderSkuFilterResultBean afterOrderSku = afterOrderSkuMap.get(sku1_id3);
					if (beforeOrderSku.getOrder_id().equals(afterOrderSku.getOrder_id())) {
						if (beforeOrderSku.getStd_unit_quantity()
								.compareTo(afterOrderSku.getStd_unit_quantity()) != 0) {
							msg = String.format("PL单删除后,LK单中存在的商品%下单数发生了变化,之前:%s,之后:%s", sku1_id3,
									beforeOrderSku.getStd_unit_quantity(), afterOrderSku.getStd_unit_quantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("PL单删除后,LK单中存在的商品%s所在LK单发生了变化,之前:%s,之后:%s", sku1_id3,
								beforeOrderSku.getOrder_id(), afterOrderSku.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("PL单%s删除后,把之前LK单中存在的商品%s也删除了", order_id, sku1_id3);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				if (afterOrderSkuMap.containsKey(sku1_id3)) {
					msg = String.format("PL单%s删除后,LK单中的商品%s没有对应删除", order_id, sku1_id3);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (beforeOrderSkuMap.containsKey(sku1_id4)) {
				if (afterOrderSkuMap.containsKey(sku1_id4)) {
					OrderSkuFilterResultBean beforeOrderSku = beforeOrderSkuMap.get(sku1_id4);
					OrderSkuFilterResultBean afterOrderSku = afterOrderSkuMap.get(sku1_id4);
					if (beforeOrderSku.getOrder_id().equals(afterOrderSku.getOrder_id())) {
						if (beforeOrderSku.getStd_unit_quantity()
								.compareTo(afterOrderSku.getStd_unit_quantity()) != 0) {
							msg = String.format("PL单删除后,LK单中存在的商品%下单数发生了变化,之前:%s,之后:%s", sku1_id4,
									beforeOrderSku.getStd_unit_quantity(), afterOrderSku.getStd_unit_quantity());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					} else {
						msg = String.format("PL单删除后,LK单中存在的商品%s所在LK单发生了变化,之前:%s,之后:%s", sku1_id4,
								beforeOrderSku.getOrder_id(), afterOrderSku.getOrder_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("PL单%s删除后,把之前LK单中存在的商品%s也删除了", order_id, sku1_id4);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			} else {
				if (afterOrderSkuMap.containsKey(sku1_id4)) {
					msg = String.format("PL单%s删除后,LK单中的商品%s没有对应删除", order_id, sku1_id4);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "PL单删除后,对应的LK单变化与预期不符");
		} catch (Exception e) {
			logger.error("下单遇到错误: ", e);
			Assert.fail("下单遇到错误: ", e);
		}
	}

	private Map<String, OrderSkuFilterResultBean> orderSkuMap() throws Exception {
		OrderSkuFilterParam orderSkuFilterParam = new OrderSkuFilterParam();
		orderSkuFilterParam.setStart_date_new(todayStr);
		orderSkuFilterParam.setEnd_date_new(tommrowStr);
		orderSkuFilterParam.setLimit(10);
		orderSkuFilterParam.setOffset(0);
		orderSkuFilterParam.setQuery_type(1);
		orderSkuFilterParam.setSearch_text(sku1_id1);

		Map<String, OrderSkuFilterResultBean> skuOrderMap = new HashMap<>();

		List<OrderSkuFilterResultBean> orderSkuFilterResults = orderService1.searchOrderSku(orderSkuFilterParam);
		Assert.assertNotEquals(orderSkuFilterResults, null, "下单商品列表搜索过滤失败");

		// 有[分割&投框]、[投框]、[分割]能力的商品会合并到一个订单里
		OrderSkuFilterResultBean orderSkuFilterResult = orderSkuFilterResults.stream()
				.filter(s -> s.getOrder_id().startsWith("LK")
						|| s.getOrder_id().startsWith(customized_station1_id + "c")
						|| s.getOrder_id().startsWith(customized_station1_id + "d") && s.getSku_id().equals(sku1_id1))
				.findAny().orElse(null);
		if (orderSkuFilterResult != null) {
			skuOrderMap.put(sku1_id1, orderSkuFilterResult);
		}

		orderSkuFilterParam.setSearch_text(sku1_id2);
		orderSkuFilterResults = orderService1.searchOrderSku(orderSkuFilterParam);
		Assert.assertNotEquals(orderSkuFilterResults, null, "下单商品列表搜索过滤失败");

		orderSkuFilterResult = orderSkuFilterResults.stream()
				.filter(s -> s.getOrder_id().startsWith("LK")
						|| s.getOrder_id().startsWith(customized_station1_id + "c")
						|| s.getOrder_id().startsWith(customized_station1_id + "c") && s.getSku_id().equals(sku1_id2))
				.findAny().orElse(null);
		if (orderSkuFilterResult != null) {
			skuOrderMap.put(sku1_id2, orderSkuFilterResult);
		}

		orderSkuFilterParam.setSearch_text(sku1_id3);
		orderSkuFilterResults = orderService1.searchOrderSku(orderSkuFilterParam);
		Assert.assertNotEquals(orderSkuFilterResults, null, "下单商品列表搜索过滤失败");

		orderSkuFilterResult = orderSkuFilterResults.stream()
				.filter(s -> s.getOrder_id().startsWith("LK")
						|| s.getOrder_id().startsWith(customized_station1_id + "c")
						|| s.getOrder_id().startsWith(customized_station1_id + "d") && s.getSku_id().equals(sku1_id3))
				.findAny().orElse(null);
		if (orderSkuFilterResult != null) {
			skuOrderMap.put(sku1_id3, orderSkuFilterResult);
		}

		orderSkuFilterParam.setSearch_text(sku1_id4);
		orderSkuFilterResults = orderService1.searchOrderSku(orderSkuFilterParam);
		Assert.assertNotEquals(orderSkuFilterResults, null, "下单商品列表搜索过滤失败");

		// 没有任何能力的商品会独自新生成一个订单,这里找出订单号最大的LK单
		int temp_sub_index = 11;
		String regex = "LK[0~9]+";
		for (OrderSkuFilterResultBean orderSku : orderSkuFilterResults) {
			if (orderSku.getOrder_id().matches(regex)) {
				temp_sub_index = 2;
				break;
			}
		}
		int sub_index = temp_sub_index;
		if (orderSkuFilterResults.size() > 0) {
			orderSkuFilterResult = orderSkuFilterResults.stream()
					.filter(s -> s.getOrder_id().startsWith("LK")
							|| s.getOrder_id().startsWith(customized_station1_id + "c")
							|| s.getOrder_id().startsWith(customized_station1_id + "d")
									&& s.getSku_id().equals(sku1_id4))
					.max((a, b) -> Integer.valueOf(a.getOrder_id().substring(sub_index)) > Integer
							.valueOf(b.getOrder_id().substring(sub_index)) ? 1 : -1)
					.get();
			skuOrderMap.put(sku1_id4, orderSkuFilterResult);
		}
		return skuOrderMap;
	}

}
