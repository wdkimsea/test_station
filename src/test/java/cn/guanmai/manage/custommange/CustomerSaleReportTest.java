package cn.guanmai.manage.custommange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.account.MgPermissionBean;
import cn.guanmai.manage.bean.account.MgRoleBean;
import cn.guanmai.manage.bean.account.MgUserBean;
import cn.guanmai.manage.bean.account.param.MgRoleCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserCreateParam;
import cn.guanmai.manage.bean.account.param.MgUserFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerEditParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgSalesReportFiterParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportDetailParam;
import cn.guanmai.manage.bean.custommange.param.MgStationSalesReportFilterParam;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportBean;
import cn.guanmai.manage.bean.custommange.result.MgSalesReportDetailBean;
import cn.guanmai.manage.bean.custommange.result.MgStationSalesReportDetailBean;
import cn.guanmai.manage.impl.account.MgAccountServiceImpl;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.impl.customermange.MgSalesReportServiceImpl;
import cn.guanmai.manage.interfaces.account.MgAccountService;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.interfaces.custommange.MgSalesReportService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 15, 2019 5:52:42 PM 
* @des 商户销售报表测试类
* @version 1.0 
*/
public class CustomerSaleReportTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(CustomerSaleReportTest.class);
	private MgSalesReportService mgSalesReportService;
	private MgAccountService mgAccountService;
	private MgCustomerService mgCustomerService;

	private OrderService orderService;
	private OrderTool orderTool;

	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String station_id;

	private MgSalesReportFiterParam salesReportFiterParam;
	private String xs_user_id;
	private List<Integer> role_ids;
	private List<Integer> sale_employee_ids;

	@BeforeClass
	public void initData() {
		Map<String, String> mg_headers = getManageCookie();
		mgSalesReportService = new MgSalesReportServiceImpl(mg_headers);
		mgAccountService = new MgAccountServiceImpl(mg_headers);
		mgCustomerService = new MgCustomerServiceImpl(mg_headers);
		try {
			Map<String, String> st_headers = LoginUtil.loginStation();
			Assert.assertNotEquals(st_headers, null, "登录station失败");

			orderService = new OrderServiceImpl(st_headers);
			orderTool = new OrderTool(st_headers);

			List<String> station_ids = mgAccountService.getAllStationIds();
			Assert.assertNotEquals(station_ids, null, "获取Group下的全部站点ID失败");

			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取ST登录账号信息失败");

			station_id = loginUserInfo.getStation_id();

			Assert.assertEquals(station_ids.contains(station_id), true, "配置文件配置的station账号和manage账号不属于同一个group");

			MgUserFilterParam mgUserFilterParam = new MgUserFilterParam();
			mgUserFilterParam.setSearch_text("XS");

			List<MgUserBean> mgUsers = mgAccountService.searchManagerUser(mgUserFilterParam);
			Assert.assertNotEquals(mgUsers, null, "信息品台账号管理,搜索过滤信息平台账号失败");

			String xs_role_id = null;
			// 查看是否有销售经理账号
			String regex = "XS[0-9]+";
			for (MgUserBean mgUser : mgUsers) {
				if (mgUser.getUsername().matches(regex)) {
					xs_user_id = mgUser.getId();
					xs_role_id = mgUser.getRoles().get(0).getId();
					break;
				}
			}

			// 没有就新建
			if (xs_user_id == null) {
				// 先查询有没有销售经理
				List<MgRoleBean> mgRoles = mgAccountService.searchManageRole("销售经理");
				Assert.assertNotEquals(mgRoles, null, "信息平台搜索角色失败");

				if (mgRoles.size() == 0) {
					MgRoleCreateParam mgRoleCreateParam = new MgRoleCreateParam();
					mgRoleCreateParam.setName("销售经理");
					mgRoleCreateParam.setDescription("销售经理,自动化脚本所建");
					mgRoleCreateParam.setType(1);
					List<MgPermissionBean> mgPermissions = mgAccountService.getManagePermissions();
					Assert.assertNotEquals(mgPermissions, null, "获取MA相关账户权限信息失败");

					MgPermissionBean mgPermission = mgPermissions.stream().filter(p -> p.getName().equals("商户"))
							.findAny().orElse(null);
					Assert.assertNotEquals(mgPermission, null, "登录账号没有查看商户相关权限");

					List<Integer> permission_ids = mgPermission.getPermissions().stream()
							.map(p -> Integer.valueOf(p.getId())).collect(Collectors.toList());
					mgRoleCreateParam.setPermission_ids(permission_ids);

					xs_role_id = mgAccountService.createManageRole(mgRoleCreateParam);
				} else {
					MgRoleBean mgRole = NumberUtil.roundNumberInList(mgRoles);
					xs_role_id = mgRole.getId();
				}

				String username = "XS" + TimeUtil.getLongTime();
				MgUserCreateParam mgUserCreateParam = new MgUserCreateParam();
				mgUserCreateParam.setUsername(username);
				mgUserCreateParam.setName(username);
				mgUserCreateParam.setIs_admin(0);
				mgUserCreateParam.setIs_valid(1);
				mgUserCreateParam.setPassword("liuge1");
				mgUserCreateParam.setRole_ids(Arrays.asList(Integer.valueOf(xs_role_id)));
				mgUserCreateParam.setVisible_station_ids(station_ids);
				xs_user_id = mgAccountService.createManageUser(mgUserCreateParam);
				Assert.assertNotEquals(xs_user_id, null, "新建信息平台账号信息失败");
			}
			sale_employee_ids = new ArrayList<Integer>();
			sale_employee_ids.add(Integer.valueOf(xs_user_id));

			role_ids = new ArrayList<Integer>();
			role_ids.add(Integer.valueOf(xs_role_id));

		} catch (Exception e) {
			logger.error("获取商户报表相关数据出现错误: ", e);
			Assert.fail("获取商户报表相关数据出现错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		salesReportFiterParam = new MgSalesReportFiterParam();
		salesReportFiterParam.setSearch_date_type(1);
		salesReportFiterParam.setBegin_date(today);
		salesReportFiterParam.setEnd_date(today);
		salesReportFiterParam.setSort_type("all_address_num");
		salesReportFiterParam.setSort_desc(0);
		salesReportFiterParam.setLimit(10);
		salesReportFiterParam.setOffset(0);

	}

	@Test
	public void customerSaleReportTestCase01() {
		ReporterCSS.title("测试点: 搜索过滤销售报表");
		try {
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase02() {
		ReporterCSS.title("测试点: 搜索过滤某个销售经理的销售报表");
		try {
			salesReportFiterParam.setSale_employee_ids(sale_employee_ids);
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase03() {
		ReporterCSS.title("测试点: 按收货时间搜索过滤销售报表");
		try {
			salesReportFiterParam.setSearch_date_type(2);
			salesReportFiterParam.setSale_employee_ids(sale_employee_ids);
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase04() {
		ReporterCSS.title("测试点: 按下单时间+角色搜索过滤销售报表");
		try {
			salesReportFiterParam.setRole_ids(role_ids);
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase05() {
		ReporterCSS.title("测试点: 按收货时间+角色搜索过滤销售报表");
		try {
			salesReportFiterParam.setSearch_date_type(1);
			salesReportFiterParam.setRole_ids(role_ids);
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase06() {
		ReporterCSS.title("测试点: 导出销售报表");
		try {
			boolean result = mgSalesReportService.exportSalesReport(salesReportFiterParam);
			Assert.assertEquals(result, true, "导出销售报表失败");
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void customerSaleReportTestCase07() {
		ReporterCSS.title("测试点: 验证销售报表统计的数据");
		try {
			List<MgSalesReportBean> mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
			Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");

			mgSalesReports = mgSalesReports.stream().filter(s -> s.getOrder_num() > 0).collect(Collectors.toList());

			String start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			String end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);

			String msg = null;
			boolean result = true;
			if (mgSalesReports.size() > 0) {
				MgSalesReportBean mgSalesReport = NumberUtil.roundNumberInList(mgSalesReports);
				String sale_employee_id = mgSalesReport.getSale_employee_id();

				MgCustomerFilterParam customerFilterParam = new MgCustomerFilterParam();
				customerFilterParam.setSaleManager(sale_employee_id);
				customerFilterParam.setNum(50);
				int page = 1;
				List<MgCustomerBean> customerList = new ArrayList<MgCustomerBean>();
				while (true) {
					customerFilterParam.setPage(page);
					List<MgCustomerBean> customers = mgCustomerService.searchCustomer(customerFilterParam);
					Assert.assertNotEquals(customers, null, "获取商户列表信息失败");
					customerList.addAll(customers);

					if (customers.size() < 50) {
						break;
					}
					page += 1;
				}

				OrderFilterParam orderFilterParam = new OrderFilterParam();
				orderFilterParam.setSearch_type(1);
				orderFilterParam.setQuery_type(1);
				orderFilterParam.setStart_date_new(start_date_new);
				orderFilterParam.setEnd_date_new(end_date_new);
				orderFilterParam.setLimit(50);

				List<OrderBean> orderList = new ArrayList<OrderBean>();
				int offset = 0;
				while (true) {
					orderFilterParam.setOffset(offset);
					List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
					Assert.assertEquals(orders != null, true, "订单列表过滤搜索订单失败");
					orderList.addAll(orders);

					if (orders.size() < 50) {
						break;
					}
					offset += 50;
				}

				List<String> address_ids = customerList.stream().map(c -> c.getSID().replaceAll("S[0]*", "")).distinct()
						.collect(Collectors.toList());

				orderList = orderList.stream().filter(o -> address_ids.contains(o.getCustomer().getAddress_id()))
						.collect(Collectors.toList());

				if (mgSalesReport.getOrder_num() != orderList.size()) {
					msg = String.format("销售报表统计的销售经理[%s]的订单数与预期不符,预期:%s,实际:%s", xs_user_id, orderList.size(),
							mgSalesReport.getOrder_num());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				// 销售额(不含税、运)
				BigDecimal sale_money = new BigDecimal("0");
				BigDecimal total_price = new BigDecimal("0");

				for (OrderBean order : orderList) {
					logger.info(order.getId() + "---" + order.getTotal_price());
					sale_money = sale_money.add(order.getSale_money());
					total_price = total_price.add(order.getTotal_price());
				}

				if (sale_money.compareTo(mgSalesReport.getTotal_pay_without_freight()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的销售额(不含运费)与预期不符,预期:%s,实际:%s", xs_user_id, sale_money,
							mgSalesReport.getTotal_pay_without_freight());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				// 笔单价
				BigDecimal money_per_order = total_price.divide(new BigDecimal(String.valueOf(orderList.size())), 2,
						BigDecimal.ROUND_HALF_UP);

				long order_address_num = orderList.stream().map(o -> o.getCustomer().getAddress_id()).distinct()
						.count();

				if (order_address_num != mgSalesReport.getOrder_address_num()) {
					msg = String.format("销售报表统计的销售经理[%s]的下单商户数与预期不符,预期:%s,实际:%s", xs_user_id, order_address_num,
							mgSalesReport.getOrder_address_num());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				if (address_ids.size() != mgSalesReport.getAll_address_num()) {
					msg = String.format("销售报表统计的销售经理[%s]的总商户数与预期不符,预期:%s,实际:%s", xs_user_id, address_ids,
							mgSalesReport.getAll_address_num());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				if (money_per_order.compareTo(mgSalesReport.getMoney_per_order()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的笔单价与预期不符,预期:%s,实际:%s", xs_user_id, money_per_order,
							mgSalesReport.getMoney_per_order());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				BigDecimal money_per_address = total_price.divide(new BigDecimal(String.valueOf(order_address_num)), 2,
						BigDecimal.ROUND_HALF_UP);

				if (money_per_address.compareTo(mgSalesReport.getMoney_per_address()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的客单价与预期不符,预期:%s,实际:%s", xs_user_id, money_per_address,
							mgSalesReport.getMoney_per_address());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "销售报表统计的数据与预期不符");
			} else {
				List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
				Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

				Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

				CustomerBean customer = NumberUtil.roundNumberInList(customerArray);

				String address_id = customer.getAddress_id();
				String sid = String.format("S%06d", Integer.valueOf(address_id));
				MgCustomerDetailBean mgCustomerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
				Assert.assertNotEquals(mgCustomerDetail, null, "获取商户的详细信息失败");
				if (mgCustomerDetail.getSales_employee_id() == null
						|| mgCustomerDetail.getSales_employee_id().equals("-1")) {
					MgCustomerEditParam mgCustomerEditParam = new MgCustomerEditParam();
					mgCustomerEditParam.setId(mgCustomerDetail.getSID());
					mgCustomerEditParam.setSaleEmployeeValue(xs_user_id);
					result = mgCustomerService.editCustomer(mgCustomerEditParam);
					Assert.assertEquals(result, true, "修改商户的销售经理信息失败");
				}

				String username = mgCustomerDetail.getUsername();
				String order_id = orderTool.oneStepCreateOrder(username, 6);
				OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
				Assert.assertNotEquals(orderDetail, null, "获取订单" + order_id + "详情失败");

				mgSalesReports = mgSalesReportService.searchSalesReport(salesReportFiterParam);
				Assert.assertNotEquals(mgSalesReports, null, "搜索过滤销售报表失败");

				Assert.assertEquals(mgSalesReports.size() == 1, true, "销售报表列表无数据,与预期不符,应该有一条数据");

				MgSalesReportBean mgSalesReport = mgSalesReports.get(0);
				BigDecimal total_pay_without_freight = orderDetail.getTotal_pay().subtract(orderDetail.getFreight());
				if (total_pay_without_freight.compareTo(mgSalesReport.getTotal_pay_without_freight()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的销售额(不含运费)与预期不符,预期:%s,实际:%s", xs_user_id,
							total_pay_without_freight, mgSalesReport.getTotal_pay_without_freight());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				// 笔单价
				BigDecimal money_per_order = orderDetail.getTotal_price();

				if (1 != mgSalesReport.getOrder_address_num()) {
					msg = String.format("销售报表统计的销售经理[%s]的下单商户数与预期不符,预期:%s,实际:%s", xs_user_id, 1,
							mgSalesReport.getOrder_address_num());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				if (money_per_order.compareTo(mgSalesReport.getMoney_per_order()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的笔单价与预期不符,预期:%s,实际:%s", xs_user_id, money_per_order,
							mgSalesReport.getMoney_per_order());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				BigDecimal money_per_address = money_per_order;

				if (money_per_address.compareTo(mgSalesReport.getMoney_per_address()) != 0) {
					msg = String.format("销售报表统计的销售经理[%s]的客单价与预期不符,预期:%s,实际:%s", xs_user_id, money_per_address,
							mgSalesReport.getMoney_per_address());
					ReporterCSS.title(msg);
					logger.warn(msg);
					result = false;
				}

				Assert.assertEquals(result, true, "销售报表统计的数据与预期不符");
			}
		} catch (Exception e) {
			logger.error("搜索过滤销售报表遇到错误: ", e);
			Assert.fail("搜索过滤销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase08() {
		ReporterCSS.title("测试点: 获取销售报表详情");
		try {
			MgSalesReportDetailParam mgSalesReportDetailParam = new MgSalesReportDetailParam();
			mgSalesReportDetailParam.setSearch_date_type(1);
			mgSalesReportDetailParam.setBegin_date(today);
			mgSalesReportDetailParam.setEnd_date(today);
			mgSalesReportDetailParam.setSale_employee_id(xs_user_id);

			MgSalesReportDetailBean mgSalesReportDetail = mgSalesReportService
					.getSalesReportDetail(mgSalesReportDetailParam);
			Assert.assertNotEquals(mgSalesReportDetail, null, "获取销售报表详情失败");
		} catch (Exception e) {
			logger.error("获取销售报表详情遇到错误: ", e);
			Assert.fail("获取销售报表详情遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase09() {
		ReporterCSS.title("测试点: 获取销售报表基础信息");
		try {
			boolean result = mgSalesReportService.getSalesReportBaseInfo();
			Assert.assertEquals(result, true, "获取销售报表基础信息失败");
		} catch (Exception e) {
			logger.error("获取销售报表基础信息遇到错误: ", e);
			Assert.fail("获取销售报表基础信息遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase10() {
		ReporterCSS.title("测试点: 获取站点销售报表");
		try {
			MgStationSalesReportFilterParam mgStationSalesReportFilterParam = new MgStationSalesReportFilterParam();
			mgStationSalesReportFilterParam.setBeginTime(today + " 00:00");
			mgStationSalesReportFilterParam.setEndTime(today + " 23:59");
			mgStationSalesReportFilterParam.setStation(station_id);
			mgStationSalesReportFilterParam.setType(1);

			boolean result = mgSalesReportService.searchStationSalesReport(mgStationSalesReportFilterParam);
			Assert.assertEquals(result, true, "获取站点销售报表失败");
		} catch (Exception e) {
			logger.error("获取站点销售报表遇到错误: ", e);
			Assert.fail("获取站点销售报表遇到错误: ", e);
		}
	}

	@Test
	public void customerSaleReportTestCase11() {
		ReporterCSS.title("测试点: 导出站点销售报表");
		try {
			MgStationSalesReportFilterParam mgStationSalesReportFilterParam = new MgStationSalesReportFilterParam();
			mgStationSalesReportFilterParam.setBeginTime(today + " 00:00");
			mgStationSalesReportFilterParam.setEndTime(today + " 23:59");
			mgStationSalesReportFilterParam.setStation(station_id);
			mgStationSalesReportFilterParam.setType(1);
			mgStationSalesReportFilterParam.setExport(1);

			boolean result = mgSalesReportService.exportStationSalesReport(mgStationSalesReportFilterParam);
			Assert.assertEquals(result, true, "导出站点销售报表失败");
		} catch (Exception e) {
			logger.error("导出站点销售报表遇到错误: ", e);
			Assert.fail("导出站点销售报表遇到错误: ", e);
		}
	}

	@Test(timeOut = 60000)
	public void customerSaleReportTestCase12() {
		ReporterCSS.title("测试点: 获取站点的销售报表");
		try {
			MgStationSalesReportDetailParam mgStationSalesReportDetailParam = new MgStationSalesReportDetailParam();
			mgStationSalesReportDetailParam.setBt(today + " 00:00");
			mgStationSalesReportDetailParam.setEt(today + " 23:59");
			mgStationSalesReportDetailParam.setId(station_id);
			mgStationSalesReportDetailParam.setKind(1);
			mgStationSalesReportDetailParam.setType(0);

			MgStationSalesReportDetailBean mgStationSalesReportDetail = mgSalesReportService
					.getMgStationSalesReportDetail(mgStationSalesReportDetailParam);
			Assert.assertNotEquals(mgStationSalesReportDetail, null, "获取站点的销售报表失败");

			String start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			String end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);

			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setSearch_type(1);
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setStart_date_new(start_date_new);
			orderFilterParam.setEnd_date_new(end_date_new);
			orderFilterParam.setLimit(50);

			List<OrderBean> orderList = new ArrayList<OrderBean>();
			int offset = 0;
			while (true) {
				orderFilterParam.setOffset(offset);
				List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
				Assert.assertEquals(orders != null, true, "订单列表过滤搜索订单失败");
				orderList.addAll(orders);

				if (orders.size() < 50) {
					break;
				}
				offset += 50;
			}

			BigDecimal total_price = new BigDecimal("0");
			Map<String, Integer> addressOrderCountMap = new HashMap<String, Integer>();
			Map<String, BigDecimal> addressOrderTotalPriceMap = new HashMap<String, BigDecimal>();
			String address_id = null;
			for (OrderBean order : orderList) {
				total_price = total_price.add(order.getTotal_price());
				address_id = order.getCustomer().getAddress_id();
				if (addressOrderCountMap.containsKey(address_id)) {
					Integer addressOrderCount = addressOrderCountMap.get(address_id);
					addressOrderCount += 1;
					addressOrderCountMap.put(address_id, addressOrderCount);

					BigDecimal addressOrderTotalPrice = addressOrderTotalPriceMap.get(address_id);
					addressOrderTotalPrice = addressOrderTotalPrice.add(order.getTotal_price());
					addressOrderTotalPriceMap.put(address_id, addressOrderTotalPrice);
				} else {
					addressOrderCountMap.put(address_id, 1);
					addressOrderTotalPriceMap.put(address_id, order.getTotal_price());
				}
			}

			String msg = null;
			boolean result = true;
			MgStationSalesReportDetailBean.SaleEmployeeInfo saleEmployeeInfo = mgStationSalesReportDetail
					.getSaleEmployeeInfo();
			System.out.println(saleEmployeeInfo.getPrice());

			if (!NumberUtil.roundCompare(saleEmployeeInfo.getPrice().toPlainString(), total_price,
					new BigDecimal("0.5"))) {
				msg = String.format("销售报表按站点统计,站点%s的下单总金额(含税)结果与预期不一致,预期:%s,实际:%s", station_id, total_price,
						saleEmployeeInfo.getPrice());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (saleEmployeeInfo.getOrder_num() != orderList.size()) {
				msg = String.format("销售报表按站点统计,站点%s的下单单数结果与预期不一致,预期:%s,实际:%s", station_id, orderList.size(),
						saleEmployeeInfo.getOrder_num());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (saleEmployeeInfo.getCustomer_num() != addressOrderCountMap.size()) {
				msg = String.format("销售报表按站点统计,站点%s的下单商户数结果与预期不一致,预期:%s,实际:%s", station_id, addressOrderCountMap.size(),
						saleEmployeeInfo.getCustomer_num());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Map<String, MgStationSalesReportDetailBean.CustomerInfo> customerInfoMap = mgStationSalesReportDetail
					.getCustomerInfo();

			MgStationSalesReportDetailBean.CustomerInfo customerInfo = null;
			for (String sid : customerInfoMap.keySet()) {
				customerInfo = customerInfoMap.get(sid);
				sid = sid.replaceAll("S0*", "");
				if (addressOrderCountMap.containsKey(sid)) {
					Integer addressOrderCount = addressOrderCountMap.get(sid);
					BigDecimal addressOrderTotalPrice = addressOrderTotalPriceMap.get(sid);
					if (addressOrderCount != customerInfo.getOrder_num()) {
						msg = String.format("销售报表按站点统计,站点%s的下单商户[%s]下单单数结果与预期不一致,预期:%s,实际:%s", station_id,
								customerInfo.getResname(), addressOrderCount, customerInfo.getOrder_num());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (!NumberUtil.roundCompare(addressOrderTotalPrice, customerInfo.getTotal_price(),
							new BigDecimal("0.5"))) {
						msg = String.format("销售报表按站点统计,站点%s的下单商户[%s]下单金额(含税)结果与预期不一致,预期:%s,实际:%s", station_id,
								customerInfo.getResname(), addressOrderTotalPrice, customerInfo.getTotal_price());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					msg = String.format("销售报表按站点统计,站点%s的下单商户[%s],实际没有下单", station_id, customerInfo.getResname());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "销售报表,按站点统计的详情信息与预期不一致");
		} catch (Exception e) {
			logger.error("获取站点的销售报表遇到错误: ", e);
			Assert.fail("获取站点的销售报表遇到错误: ", e);
		}
	}

}
