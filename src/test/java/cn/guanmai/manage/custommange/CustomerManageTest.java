package cn.guanmai.manage.custommange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.manage.bean.async.MgAsyncTaskBean;
import cn.guanmai.manage.bean.custommange.param.MgCustomerAddParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerEditParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerImportAddParam;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.bean.custommange.result.CustomerEditModel;
import cn.guanmai.manage.bean.custommange.result.CustomerEmployeeInfoBean;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.impl.async.MgAsyncServiceImpl;
import cn.guanmai.manage.impl.base.MgDownloadServiceImpl;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.async.MgAsyncService;
import cn.guanmai.manage.interfaces.base.MgDownLoadService;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Jan 15, 2019 10:13:34 AM 
* @des 商户相关操作测试
* @version 1.0 
*/
public class CustomerManageTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(CustomerManageTest.class);
	private Map<String, String> ma_headers;
	private MgCustomerService mgCustomerService;
	private CustomerBaseInfoBean customerBaseInfo;
	private MgAsyncService mgAsyncService;
	private MgDownLoadService mgDownLoadService;

	@BeforeClass
	public void initData() {
		ma_headers = getManageCookie();
		mgCustomerService = new MgCustomerServiceImpl(ma_headers);
		mgAsyncService = new MgAsyncServiceImpl(ma_headers);
		mgDownLoadService = new MgDownloadServiceImpl(ma_headers);
		try {
			customerBaseInfo = mgCustomerService.getCustomerBaseInfo();
			Assert.assertNotEquals(customerBaseInfo, null, "获取商户基础信息失败");
		} catch (Exception e) {
			logger.error("获取商户基础信息出现错误: ", e);
			Assert.fail("获取商户基础信息出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase01() {
		ReporterCSS.title("测试点: 拉取商户列表信息");
		MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
		try {
			List<MgCustomerBean> list = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(list, null, "获取商户列表信息失败");
		} catch (Exception e) {
			logger.error("获取商户列表信息出现错误: ", e);
			Assert.fail("获取商户列表信息出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase02() {
		ReporterCSS.title("测试点: 商户列表翻页");
		MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
		try {
			filterParam.setPage(1);
			filterParam.setNum(10);

			List<MgCustomerBean> mgCustomerList1 = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(mgCustomerList1, null, "获取商户列表信息失败");

			if (mgCustomerList1.size() == 10) {
				filterParam.setPage(2);
				List<MgCustomerBean> mgCustomerList2 = mgCustomerService.searchCustomer(filterParam);
				Assert.assertNotEquals(mgCustomerList2, null, "获取商户列表信息失败");

				List<String> address_ids1 = mgCustomerList1.stream().map(s -> s.getSID()).collect(Collectors.toList());
				List<String> address_ids2 = mgCustomerList2.stream().map(s -> s.getSID()).collect(Collectors.toList());

				address_ids2.retainAll(address_ids1);
				Assert.assertEquals(address_ids2.size(), 0, "商户列表翻页,出现了重复的商户数据" + address_ids2);
			}
		} catch (Exception e) {
			logger.error("获取商户列表信息出现错误: ", e);
			Assert.fail("获取商户列表信息出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase03() {
		ReporterCSS.title("测试点: 获取Group员工基础信息");
		try {
			CustomerEmployeeInfoBean customerEmployeeInfo = mgCustomerService.getCustomerEmployeeInfo();
			Assert.assertNotEquals(customerEmployeeInfo, null, "Group员工基础信息获取失败");
		} catch (Exception e) {
			logger.error("Group员工基础信息获取遇到错误: ", e);
			Assert.fail("Group员工基础信息获取遇到错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase04() {
		ReporterCSS.title("测试点: 获取商户标签");
		try {
			Map<BigDecimal, String> labelMap = mgCustomerService.getCustomerLabel();
			Assert.assertNotEquals(labelMap, null, "获取商户标签列表失败");
		} catch (Exception e) {
			logger.error("获取商户标签遇到错误: ", e);
			Assert.fail("获取商户标签遇到错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase05() {
		ReporterCSS.title("测试点: 查看商户详细信息");
		MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
		try {
			List<MgCustomerBean> list = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(list, null, "获取商户列表信息失败");

			if (list.size() > 0) {
				MgCustomerBean mgCustomer = NumberUtil.roundNumberInList(list);
				String sid = mgCustomer.getSID();
				MgCustomerDetailBean customerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
				Assert.assertNotEquals(customerDetail, null, "获取SID为 " + sid + " 的商户详细信息失败");
			} else {
				Assert.fail("请注意,此Group下无商户,请关注商户详细信息接口是否正常");
			}
		} catch (Exception e) {
			logger.error("获取商户列表信息出现错误: ", e);
			Assert.fail("获取商户列表信息出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase06() {
		ReporterCSS.title("测试点: 批量修改商户,模板导出(导出个别商户)");
		try {
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			if (customerList.size() > 0) {
				MgCustomerBean mgCustomer = NumberUtil.roundNumberInList(customerList);
				String sid = mgCustomer.getSID();

				List<String> sids = Arrays.asList(sid);
				BigDecimal task_id = mgCustomerService.exportEditCustomerTemplate(sids);
				Assert.assertNotEquals(task_id, null, "批量修改商户,模板导出(导出个别商户)异步任务创建失败");

				boolean result = mgAsyncService.getAsyncTaskResult(task_id, "成功");
				Assert.assertEquals(result, true, "批量修改商户模板导出异步任务执行失败");

				MgAsyncTaskBean mgAsyncTask = mgAsyncService.getAsyncTask(task_id);
				Assert.assertNotEquals(mgAsyncTask, null, "获取批量修改商户模板导出异步任务详情失败");

				String fileUrl = mgAsyncTask.getResult().getLink();

				String filePath = mgDownLoadService.downloadFile(fileUrl);
				Assert.assertNotEquals(filePath, null, "下载批量修改商户模板失败");

				List<CustomerEditModel> customerEditModels = EasyExcelFactory.read(filePath)
						.head(CustomerEditModel.class).sheet(0).headRowNumber(11).doReadSync();

				Assert.assertEquals(customerEditModels != null && customerEditModels.size() == 1, true,
						"Excel导出的数据条目数与传递的参数不匹配");

				CustomerEditModel customerEditModel = customerEditModels.get(0);

				MgCustomerDetailBean customerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
				Assert.assertNotEquals(customerDetail, null, "获取SID为 " + sid + " 的商户详细信息失败");

				String msg = null;
				if (!customerDetail.getSID().equals(customerEditModel.getSid())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[SID]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getSID(), customerEditModel.getSid());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getUsername().equals(customerEditModel.getUsername())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[账户名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getUsername(), customerEditModel.getUsername());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getPayment_name().equals(customerEditModel.getPayer_name())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[结款人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getPayment_name(), customerEditModel.getPayer_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getPayment_telephone().equals(customerEditModel.getPayer_phone())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[结款电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getPayment_telephone(), customerEditModel.getPayer_phone());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerDetail.getSettle_way() != customerEditModel.getSettle_way()) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[结款方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getSettle_way(), customerEditModel.getSettle_way());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerDetail.getPay_method() != customerEditModel.getPay_method()) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[账期方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getPay_method(), customerEditModel.getPay_method());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerDetail.getSettle_date_type() != customerEditModel.getSettle_date_type()) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[日期维度]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getSettle_date_type(), customerEditModel.getSettle_date_type());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerDetail.getFinance_status() != customerEditModel.getFreezeState()) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[冻结状态]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getFinance_status(), customerEditModel.getFreezeState());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerDetail.getIs_whitelist() != customerEditModel.getWhite()) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[财务白名单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getIs_whitelist(), customerEditModel.getWhite());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (customerEditModel.getCompany_name() == null)
					customerEditModel.setCompany_name("");
				if (!customerDetail.getCname().equals(customerEditModel.getCompany_name())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[公司名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getCname(), customerEditModel.getCompany_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getResname().equals(customerEditModel.getShop_name())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[店铺名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getResname(), customerEditModel.getShop_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getReceiveperson().equals(customerEditModel.getReceiver_name())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[收货人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getReceiveperson(), customerEditModel.getReceiver_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!customerDetail.getReceivephone().equals(customerEditModel.getReceiver_phone())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[收货电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getReceivephone(), customerEditModel.getReceiver_phone());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				StringBuffer expect_area_code = new StringBuffer();
				expect_area_code.append(customerDetail.getCity());
				expect_area_code.append("-");
				expect_area_code.append(customerDetail.getArea_l1());
				expect_area_code.append("-");
				expect_area_code.append(customerDetail.getArea_l2());

				if (!expect_area_code.toString().equals(customerEditModel.getArea_code())) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[地理位置]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							customerDetail.getMap_address(), customerEditModel.getArea_code());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String expected_sales_employee_name = customerDetail.getSales_employee_name().equals("--")
						|| customerDetail.getSales_employee_name().equals("(待定)") ? ""
								: customerDetail.getSales_employee_name();

				String actual_sales_employee_name = customerEditModel.getSale_employee_name() == null ? ""
						: customerEditModel.getSale_employee_name();

				if (!expected_sales_employee_name.equals(actual_sales_employee_name)) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[销售经理]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							expected_sales_employee_name, actual_sales_employee_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String expected_create_employee_name = customerDetail.getCreate_employee_name().equals("--")
						|| customerDetail.getCreate_employee_name().equals("(待定)") ? ""
								: customerDetail.getCreate_employee_name();

				String actual_create_employee_name = customerEditModel.getCreate_employee_name() == null ? ""
						: customerEditModel.getCreate_employee_name();

				if (!expected_create_employee_name.equals(actual_create_employee_name)) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[开户经理]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							expected_create_employee_name, actual_create_employee_name);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				List<String> salemeun_ids = customerDetail.getSalemenus().stream().map(c -> c.getSalemenu_id())
						.collect(Collectors.toList());
				String[] actual_salemenus_ids = customerEditModel.getSalemeuns().split(";");
				if (salemeun_ids.size() != actual_salemenus_ids.length) {
					msg = String.format("批量修改商户,模板导出商户%s的信息,[绑定报价单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
							salemeun_ids, actual_salemenus_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "批量修改商户,模板导出的商户信息与页面显示的不一致");
			} else {
				Assert.fail("请注意,此Group下无商户,请关注批量修改商户,模板导出接口是否正常");
			}
		} catch (Exception e) {
			logger.error("批量修改商户,模板导出出现错误: ", e);
			Assert.fail("批量修改商户,模板导出出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase07() {
		ReporterCSS.title("测试点: 批量修改商户,模板导出(导出所有商户)");
		try {
			MgCustomerFilterParam customerFilterParam = new MgCustomerFilterParam();
			BigDecimal task_id = mgCustomerService.exportEditCustomerTemplate(customerFilterParam);
			Assert.assertNotEquals(task_id, null, "批量修改商户,模板导出(导出所有商户)异步任务创建失败");

			boolean result = mgAsyncService.getAsyncTaskResult(task_id, "成功");
			Assert.assertEquals(result, true, "批量修改商户模板导出异步任务执行失败");

			MgAsyncTaskBean mgAsyncTask = mgAsyncService.getAsyncTask(task_id);
			Assert.assertNotEquals(mgAsyncTask, null, "获取批量修改商户模板导出异步任务详情失败");

			String fileUrl = mgAsyncTask.getResult().getLink();

			String filePath = mgDownLoadService.downloadFile(fileUrl);
			Assert.assertNotEquals(filePath, null, "下载批量修改商户模板失败");
		} catch (Exception e) {
			logger.error("批量修改商户,模板导出出现错误: ", e);
			Assert.fail("批量修改商户,模板导出出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase08() {
		ReporterCSS.title("测试点: Excel导入批量修改商户信息");
		try {
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			if (customerList.size() > 0) {
				List<String> sids = customerList.stream().map(c -> c.getSID()).collect(Collectors.toList());

				BigDecimal task_id = mgCustomerService.exportEditCustomerTemplate(sids);
				Assert.assertNotEquals(task_id, null, "批量修改商户,模板导出失败");

				boolean result = mgAsyncService.getAsyncTaskResult(task_id, "导出成功");
				Assert.assertEquals(result, true, "批量修改商户模板导出异步任务执行失败");

				MgAsyncTaskBean mgAsyncTask = mgAsyncService.getAsyncTask(task_id);
				Assert.assertNotEquals(mgAsyncTask, null, "获取批量修改商户模板导出异步任务详情失败");

				String fileUrl = mgAsyncTask.getResult().getLink();

				String filePath = mgDownLoadService.downloadFile(fileUrl);
				Assert.assertNotEquals(filePath, null, "下载批量修改商户模板失败");

				task_id = mgCustomerService.importEditCustomer(filePath);
				Assert.assertNotEquals(task_id, true, "批量修改商户,模板导入异步任务创建失败");

				result = mgAsyncService.getAsyncTaskResult(task_id, "失败0");
				Assert.assertEquals(result, true, "批量修改商户,模板导入异步任务执行失败");
			} else {
				Assert.fail("请注意,此Group下无商户,请关注批量修改商户,模板导出接口是否正常");
			}
		} catch (Exception e) {
			logger.error("批量修改商户,模板导出出现错误: ", e);
			Assert.fail("批量修改商户,模板导出出现错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase09() {
		ReporterCSS.title("测试点: 批量导入新建商户信息");
		String sid = null;
		try {
			MgCustomerImportAddParam customerImportAddParam = new MgCustomerImportAddParam();
			String username = "TEMP" + TimeUtil.getLongTime();
			customerImportAddParam.setUsername(username);
			customerImportAddParam.setPayer_name(username);
			String phone = "110" + StringUtil.getRandomNumber(8);
			customerImportAddParam.setPayer_telephone(phone);
			customerImportAddParam.setSettle_way(1);
			customerImportAddParam.setAccount_period_way(1);
			customerImportAddParam.setPay_method(1);
			customerImportAddParam.setSettle_date_type(1);
			customerImportAddParam.setFinance_status(0);
			customerImportAddParam.setWhitelist(1);
			customerImportAddParam.setCompany_name(username);
			customerImportAddParam.setRestaurant_name(username);
			customerImportAddParam.setReceiver_name(username);
			customerImportAddParam.setReceiver_telephone(phone);
			customerImportAddParam.setFee_type("CNY");

			String employee_id = customerBaseInfo.getSale_employee_ids().size() > 0
					? customerBaseInfo.getSale_employee_ids().get(0)
					: "-1";
			customerImportAddParam.setSale_employee_id(employee_id);

			List<CustomerBaseInfoBean.Salemenu> salemenus = customerBaseInfo.getSalemenus();
			CustomerBaseInfoBean.Salemenu salemenu = NumberUtil.roundNumberInList(salemenus);
			List<String> city_ids = salemenu.getDistribute_city_ids();
			String city_id = NumberUtil.roundNumberInList(city_ids);

			customerImportAddParam.setSalemenu_ids(salemenu.getSalemenu_ids());

			List<CustomerBaseInfoBean.District> districts = customerBaseInfo.getDistrict();
			CustomerBaseInfoBean.District district = districts.stream().filter(d -> d.getCity_code().equals(city_id))
					.findAny().orElse(null);
			String city_code = district.getCity_code();
			String city_name = district.getCity_name();

			CustomerBaseInfoBean.District.Area area = district.getAreas().get(0);
			String area_code = area.getArea_code();
			String area_name = area.getArea_name();

			CustomerBaseInfoBean.District.Area.Street street = area.getStreets().get(0);
			String street_id = street.getStreet_code();
			String street_name = street.getStreet_name();

			customerImportAddParam.setDistrict_code(city_code);
			customerImportAddParam.setArea_level1(area_code);
			customerImportAddParam.setArea_level2(street_id);

			StringBuffer restaurant_address = new StringBuffer();
			restaurant_address.append(city_name);
			restaurant_address.append(area_name);
			restaurant_address.append(street_name);
			customerImportAddParam.setRestaurant_address(restaurant_address.toString());

			boolean result = mgCustomerService.importAddCustomer(Arrays.asList(customerImportAddParam));
			Assert.assertEquals(result, true, "批量导入新建商户失败");

			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			filterParam.setSearch_text(username);

			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customerList, null, "商户过滤搜索失败");

			MgCustomerBean customer = customerList.stream().filter(c -> c.getUsername().equals(username)).findAny()
					.orElse(null);
			Assert.assertNotEquals(customer, null, "批量导入新建的商户没有查询到");

			sid = customer.getSID();

			MgCustomerDetailBean customerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
			Assert.assertNotEquals(customerDetail, null, "获取商户 " + sid + " 详细信息失败");

			String msg = null;

			if (!customerDetail.getUsername().equals(customerImportAddParam.getUsername())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[账户名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getUsername(), customerDetail.getUsername());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getPayment_name().equals(customerImportAddParam.getPayer_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getPayer_name(), customerDetail.getPayment_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getPayment_telephone().equals(customerImportAddParam.getPayer_telephone())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getPayer_telephone(), customerDetail.getPayment_telephone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getSettle_way() != customerImportAddParam.getSettle_way()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getSettle_way(), customerDetail.getSettle_way());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getPay_method() != customerImportAddParam.getPay_method()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[账期方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getPay_method(), customerDetail.getPay_method());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getSettle_date_type() != customerImportAddParam.getSettle_date_type()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[日期维度]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getSettle_date_type(), customerDetail.getSettle_date_type());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getFinance_status() != customerImportAddParam.getFinance_status()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[冻结状态]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getFinance_status(), customerDetail.getFinance_status());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getIs_whitelist() != customerImportAddParam.getWhitelist()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[财务白名单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getWhitelist(), customerDetail.getIs_whitelist());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getCname().equals(customerImportAddParam.getCompany_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[公司名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getCompany_name(), customerDetail.getCname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getResname().equals(customerImportAddParam.getRestaurant_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[店铺名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getRestaurant_name(), customerDetail.getResname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getReceiveperson().equals(customerImportAddParam.getReceiver_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[收货人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getReceiver_name(), customerDetail.getReceiveperson());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getReceivephone().equals(customerImportAddParam.getReceiver_telephone())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[收货电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getReceiver_telephone(), customerDetail.getReceivephone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			StringBuffer actual_area_code = new StringBuffer();
			actual_area_code.append(customerDetail.getDistrict_code());
			actual_area_code.append("-");
			actual_area_code.append(customerDetail.getArea_l1_id());
			actual_area_code.append("-");
			actual_area_code.append(customerDetail.getArea_l2_id());

			StringBuffer expected_area_code = new StringBuffer();
			expected_area_code.append(customerImportAddParam.getDistrict_code());
			expected_area_code.append("-");
			expected_area_code.append(customerImportAddParam.getArea_level1());
			expected_area_code.append("-");
			expected_area_code.append(customerImportAddParam.getArea_level2());

			if (!actual_area_code.toString().equals(expected_area_code.toString())) {
				msg = String.format("批量导入新建商户,新建的商户%s的信息,[地理位置]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						expected_area_code, actual_area_code);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> salemeun_ids = customerDetail.getSalemenus().stream().map(c -> c.getSalemenu_id())
					.collect(Collectors.toList());

			if (!salemeun_ids.containsAll(customerImportAddParam.getSalemenu_ids())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[绑定报价单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerImportAddParam.getSalemenu_ids(), salemeun_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "批量导入新建的商户信息与预期不一致");
		} catch (Exception e) {
			logger.error("批量导入新建商户信息出现错误: ", e);
			Assert.fail("批量导入新建商户信息出现错误: ", e);
		} finally {
			try {
				if (sid != null) {
					boolean result = mgCustomerService.deleteCustomter(sid);
					Assert.assertEquals(result, true, "删除商户  " + sid + " 失败");
				}
			} catch (Exception e) {
				logger.error("删除商户出现错误: ", e);
				Assert.fail("删除商户出现错误: ", e);
			}
		}
	}

	@Test
	public void customerManageTestCase10() {
		ReporterCSS.title("测试点: 新建商户信息");
		String address_id = null;
		try {
			String username = "TEMP" + TimeUtil.getLongTime();
			String company_name = "自动化测试优先公司";
			String telephone = "110" + StringUtil.getRandomNumber(8);

			MgCustomerAddParam customerAddParam = new MgCustomerAddParam();
			customerAddParam.setUsername(username);
			customerAddParam.setCompany_name(company_name);
			customerAddParam.setPassword("liuge1");
			customerAddParam.setEditPassword(false);
			customerAddParam.setTelephone(telephone);
			customerAddParam.setCustomer_type(0);
			customerAddParam.setSettle_way(1);
			customerAddParam.setPayer_name(username);
			customerAddParam.setPayer_telephone(telephone);
			customerAddParam.setFinance_status(0);
			customerAddParam.setWhitelist(1);
			customerAddParam.setCheck_out(1);
			customerAddParam.setPay_method(1);
			customerAddParam.setSettle_date_type(1);
			customerAddParam.setIs_credit(0);
			customerAddParam.setRestaurant_name(username);
			customerAddParam.setReceiver_name(username);
			customerAddParam.setReceiver_telephone(telephone);

			List<CustomerBaseInfoBean.Salemenu> salemenus = customerBaseInfo.getSalemenus();
			CustomerBaseInfoBean.Salemenu salemenu = NumberUtil.roundNumberInList(salemenus);
			List<String> city_ids = salemenu.getDistribute_city_ids();
			String city_id = NumberUtil.roundNumberInList(city_ids);

			customerAddParam.setSalemenu_ids(JSONArray.parseArray(salemenu.getSalemenu_ids().toString()));

			List<CustomerBaseInfoBean.District> districts = customerBaseInfo.getDistrict();
			CustomerBaseInfoBean.District district = districts.stream().filter(d -> d.getCity_code().equals(city_id))
					.findAny().orElse(null);
			String city_code = district.getCity_code();
			String city_name = district.getCity_name();

			CustomerBaseInfoBean.District.Area area = district.getAreas().get(0);
			String area_code = area.getArea_code();
			String area_name = area.getArea_name();

			CustomerBaseInfoBean.District.Area.Street street = area.getStreets().get(0);
			String street_id = street.getStreet_code();
			String street_name = street.getStreet_name();

			customerAddParam.setDistrict_code(city_code);
			customerAddParam.setArea_level1(area_code);
			customerAddParam.setArea_level2(street_id);

			StringBuffer restaurant_address = new StringBuffer();
			restaurant_address.append(city_name);
			restaurant_address.append(area_name);
			restaurant_address.append(street_name);
			customerAddParam.setRestaurant_address(restaurant_address.toString());

			address_id = mgCustomerService.createCustomer(customerAddParam);
			Assert.assertNotEquals(address_id, null, "新建商户失败");

			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			filterParam.setSearch_text(username);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customerList, null, "商户过滤搜索失败");

			MgCustomerBean customer = customerList.stream().filter(c -> c.getUsername().equals(username)).findAny()
					.orElse(null);
			Assert.assertNotEquals(customer, null, "批量导入新建的商户没有查询到");

			address_id = customer.getSID();

			MgCustomerDetailBean customerDetail = mgCustomerService.getCustomerDetailInfoBySID(address_id);
			Assert.assertNotEquals(customerDetail, null, "获取商户 " + address_id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (!customerDetail.getUsername().equals(customerAddParam.getUsername())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[账户名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getUsername(), customerDetail.getUsername());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getPayment_name().equals(customerAddParam.getPayer_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getPayer_name(), customerDetail.getPayment_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getPayment_telephone().equals(customerAddParam.getPayer_telephone())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getPayer_telephone(), customerDetail.getPayment_telephone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getSettle_way() != customerAddParam.getSettle_way()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[结款方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getSettle_way(), customerDetail.getSettle_way());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getPay_method() != customerAddParam.getPay_method()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[账期方式]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getPay_method(), customerDetail.getPay_method());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getSettle_date_type() != customerAddParam.getSettle_date_type()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[日期维度]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getSettle_date_type(), customerDetail.getSettle_date_type());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getFinance_status() != customerAddParam.getFinance_status()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[冻结状态]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getFinance_status(), customerDetail.getFinance_status());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerDetail.getIs_whitelist() != customerAddParam.getWhitelist()) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[财务白名单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getWhitelist(), customerDetail.getIs_whitelist());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getCname().equals(customerAddParam.getCompany_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[公司名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getCompany_name(), customerDetail.getCname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getResname().equals(customerAddParam.getRestaurant_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[店铺名]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getRestaurant_name(), customerDetail.getResname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getReceiveperson().equals(customerAddParam.getReceiver_name())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[收货人]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getReceiver_name(), customerDetail.getReceiveperson());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!customerDetail.getReceivephone().equals(customerAddParam.getReceiver_telephone())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[收货电话]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getReceiver_telephone(), customerDetail.getReceivephone());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			StringBuffer actual_area_code = new StringBuffer();
			actual_area_code.append(customerDetail.getDistrict_code());
			actual_area_code.append("-");
			actual_area_code.append(customerDetail.getArea_l1_id());
			actual_area_code.append("-");
			actual_area_code.append(customerDetail.getArea_l2_id());

			StringBuffer expected_area_code = new StringBuffer();
			expected_area_code.append(customerAddParam.getDistrict_code());
			expected_area_code.append("-");
			expected_area_code.append(customerAddParam.getArea_level1());
			expected_area_code.append("-");
			expected_area_code.append(customerAddParam.getArea_level2());

			if (!actual_area_code.toString().equals(expected_area_code.toString())) {
				msg = String.format("批量导入新建商户,新建的商户%s的信息,[地理位置]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						expected_area_code, actual_area_code);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<String> salemeun_ids = customerDetail.getSalemenus().stream().map(c -> c.getSalemenu_id())
					.collect(Collectors.toList());

			if (!salemeun_ids.containsAll(customerAddParam.getSalemenu_ids())) {
				msg = String.format("批量新建商户,批量新建的商户商户%s的信息,[绑定报价单]与页面显示的不一致,预期:%s,实际:%s", customerDetail.getSID(),
						customerAddParam.getSalemenu_ids(), salemeun_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "批量导入新建的商户信息与预期不一致");

		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		} finally {
			if (address_id != null) {
				try {
					boolean result = mgCustomerService.deleteCustomter(address_id);
					Assert.assertEquals(result, true, "删除新建的商户" + address_id + "失败");
				} catch (Exception e) {
					logger.error("删除新建商户遇到错误: ", e);
					Assert.fail("删除新建商户遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void customerManageTestCase11() {
		ReporterCSS.title("测试点: 商户导出");
		try {
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			BigDecimal task_id = mgCustomerService.exportCustomer(filterParam);
			Assert.assertNotEquals(task_id, true, "商户导出异步任务创建失败");

			boolean result = mgAsyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(result, true, "商户导出异步任务执行失败");
		} catch (Exception e) {
			logger.error("商户导出遇到错误: ", e);
			Assert.fail("商户导出遇到错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase12() {
		ReporterCSS.title("测试点: 商户修改");
		try {
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			List<MgCustomerBean> customers = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customers, null, "商户搜索失败");

			if (customers.size() > 0) {
				MgCustomerBean customer = NumberUtil.roundNumberInList(customers);
				String sid = customer.getSID();

				MgCustomerEditParam customerEditParam = new MgCustomerEditParam();
				customerEditParam.setId(sid);
				customerEditParam.setWhite(1);

				boolean result = mgCustomerService.editCustomer(customerEditParam);
				Assert.assertEquals(result, true, "修改商户信息失败");
			}
		} catch (Exception e) {
			logger.error("商户修改遇到错误: ", e);
			Assert.fail("商户修改遇到错误: ", e);
		}
	}

	@Test
	public void customerManageTestCase13() {
		ReporterCSS.title("测试点: 商户修改绑定的报价单");
		try {
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			List<MgCustomerBean> customers = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customers, null, "商户搜索失败");

			if (customers.size() > 0) {
				MgCustomerBean customer = NumberUtil.roundNumberInList(customers);
				String sid = customer.getSID();

				MgCustomerDetailBean customerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
				Assert.assertNotEquals(customerDetail, null, "获取商户 " + sid + " 详细信息失败");
				String city_code = customerDetail.getDistrict_code();
				List<String> salemenu_ids = customerDetail.getSalemenus().stream().map(s -> s.getSalemenu_id())
						.collect(Collectors.toList());

				if (customerDetail.getSalemenus().size() >= 2) {
					MgCustomerDetailBean.Salemenu salemenu = customerDetail.getSalemenus().get(0);
					String record_id = salemenu.getId();
					String salemenu_id = salemenu.getSalemenu_id();
					String station_id = salemenu.getStation_id();
					boolean result = mgCustomerService.deleteSalemenu(sid, record_id);
					Assert.assertEquals(result, true, "商户" + sid + "移除报价单" + salemenu.getSalemenu_name() + " 失败");

					result = mgCustomerService.addSalemenu(station_id, sid, salemenu_id);
					Assert.assertEquals(result, true, "商户" + sid + "添加报价单" + salemenu.getSalemenu_name() + " 失败");
				} else {
					List<CustomerBaseInfoBean.Salemenu> salemenus = customerBaseInfo.getSalemenus();
					String station_id = null;
					String salemenu_id = null;
					for (CustomerBaseInfoBean.Salemenu salemenu : salemenus) {
						if (salemenu.getDistribute_city_ids().contains(city_code)) {
							if (salemenu.getSalemenu_ids().size() > 0) {
								salemenu_id = salemenu.getSalemenu_ids().get(0);
								if (!salemenu_ids.contains(salemenu_id)) {
									station_id = salemenu.getStation_id();
									break;
								}

							}
						}
					}

					if (station_id != null) {
						boolean result = mgCustomerService.addSalemenu(station_id, sid, salemenu_id);
						Assert.assertEquals(result, true, "商户" + sid + "添加报价单" + salemenu_id + " 失败");

						customerDetail = mgCustomerService.getCustomerDetailInfoBySID(sid);
						Assert.assertNotEquals(customerDetail, null, "获取商户 " + sid + " 详细信息失败");

						String temp_salemenu_id = salemenu_id;
						MgCustomerDetailBean.Salemenu Salemenu = customerDetail.getSalemenus().stream()
								.filter(s -> s.getSalemenu_id().equals(temp_salemenu_id)).findAny().orElse(null);

						String record_id = Salemenu.getId();

						result = mgCustomerService.deleteSalemenu(sid, record_id);
						Assert.assertEquals(result, true, "商户" + sid + "移除报价单" + salemenu_id + " 失败");
					}
				}
			}
		} catch (Exception e) {
			logger.error("商户修改遇到错误: ", e);
			Assert.fail("商户修改遇到错误: ", e);
		}
	}

	public void customerManageTestCase99() {
		ReporterCSS.title("测试点: 清理站点商户");
		try {
			List<MgCustomerBean> customers = new ArrayList<MgCustomerBean>();
			List<MgCustomerBean> tempCustomers = null;
			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			int page = 1;
			int num = 10;
			filterParam.setNum(num);
			while (true) {
				filterParam.setPage(page);
				tempCustomers = mgCustomerService.searchCustomer(filterParam);
				Assert.assertNotEquals(customers, null, "商户搜索失败");
				customers.addAll(tempCustomers);
				if (tempCustomers.size() < num) {
					break;
				}
				page += 1;
			}
			for (MgCustomerBean customer : customers) {
				if (!customer.getUsername().startsWith("lmzz")) {
//					boolean result = mgCustomerService.deleteCustomter(customer.getSID());
//					Assert.assertEquals(result, true, "删除商户" + customer.getName() + "失败");
				}
			}
		} catch (Exception e) {
			logger.error("删除商户遇到错误: ", e);
			Assert.fail("删除商户遇到错误: ", e);
		}

	}

}
