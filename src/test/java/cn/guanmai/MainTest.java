package cn.guanmai;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.TestNG;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.manage.bean.account.StUserBean;
import cn.guanmai.manage.bean.account.param.StUserFilterParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerAddParam;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.impl.account.MgAccountServiceImpl;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.impl.finance.MgFinanceServiceImpl;
import cn.guanmai.manage.interfaces.account.MgAccountService;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.interfaces.finance.MgFinanceService;
import cn.guanmai.station.bean.system.LoginStationInfoBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.system.CustomizedServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.interfaces.system.CustomizedService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.util.ConfigureUtil;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.ReportBotUtil;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author: liming
 * @Date: 2020年6月1日 上午11:43:38
 * @description:
 * @version: 1.0
 */

public class MainTest {
	private static Logger logger = LoggerFactory.getLogger(MainTest.class);

	public static void main(String[] args) {
		try {
			Map<String, String> ma_headers = LoginUtil.loginManage();
			Assert.assertNotEquals(ma_headers, null, "信息平台登录失败");

			String manage_url = ConfigureUtil.getValueByKey("manageUrl");

			MgCustomerService customerService = new MgCustomerServiceImpl(ma_headers);
			MgFinanceService financeService = new MgFinanceServiceImpl(ma_headers);

			MgCustomerFilterParam customerFilterParam = new MgCustomerFilterParam();
			customerFilterParam.setSearch_text("AT");

			List<MgCustomerBean> customers = customerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customers, null, "搜索过滤商户信息失败");

			MgCustomerBean mgCustomer = null;
			if (customers.size() > 0) {
				String regex = "AT[0-9]{13}";
				for (MgCustomerBean customer : customers) {
					boolean result = Pattern.matches(regex, customer.getUsername());
					if (result) {
						mgCustomer = customer;
						break;
					}
				}
			}

			CustomerBaseInfoBean customerBaseInfo = customerService.getCustomerBaseInfo();
			Assert.assertNotEquals(customerBaseInfo, null, "获取商户信息失败");

			// 找到Station ID 最小的站点,把此站点的报价单全部绑定到商户上
			List<CustomerBaseInfoBean.Salemenu> salemenuList = customerBaseInfo.getSalemenus();
			String station_id = null;
			CustomerBaseInfoBean.Salemenu salemenu = null;
			for (CustomerBaseInfoBean.Salemenu s : salemenuList) {
				if (station_id == null) {
					station_id = s.getStation_id();
					salemenu = s;
				} else {
					if (Integer.valueOf(station_id.substring(1)) > Integer.valueOf(s.getStation_id().substring(1))) {
						station_id = s.getStation_id();
						salemenu = s;
					}
				}
			}
			List<String> salemenu_ids = salemenu.getSalemenu_ids();

			String bs_user_name = null;
			String kid = null;
			if (mgCustomer == null) {
				bs_user_name = "AT" + TimeUtil.getLongTime();
				String company_name = "自动化专用公司";
				String telephone = "122" + StringUtil.getRandomNumber(8);

				String name = "自动化专用商户";

				MgCustomerAddParam customerAddParam = new MgCustomerAddParam();
				customerAddParam.setUsername(bs_user_name);
				customerAddParam.setCompany_name(company_name);
				customerAddParam.setPassword("1qaz2wsx");
				customerAddParam.setEditPassword(false);
				customerAddParam.setTelephone(telephone);
				customerAddParam.setCustomer_type(0);
				customerAddParam.setSettle_way(2);
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

				List<CustomerBaseInfoBean.District> districts = customerBaseInfo.getDistrict();
				OK: for (CustomerBaseInfoBean.District district : districts) {
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

				customerAddParam.setSalemenu_ids(JSONArray.parseArray(salemenu_ids.toString()));

				String sid = customerService.createCustomer(customerAddParam);
				Assert.assertNotEquals(sid, null, "新建商户失败");

				MgCustomerDetailBean mgCustomerDetail = customerService.getCustomerDetailInfoBySID(sid);
				Assert.assertNotEquals(mgCustomerDetail, null, "获取商户详细信息失败");
				kid = mgCustomerDetail.getKID();
			} else {
				bs_user_name = mgCustomer.getUsername();
				kid = mgCustomer.getKID();
			}

			// 为此商户进行充值
			kid = kid.replaceAll("K0*", "");

			boolean result = financeService.rechargeMoney(kid, new BigDecimal("20000"));
			Assert.assertEquals(result, true, "商户充值失败");

			// 更新配置文件
			MgAccountService mgAccountService = new MgAccountServiceImpl(ma_headers);

			// 搜索管理员账号
			StUserFilterParam stUserFilterParam = new StUserFilterParam();
			stUserFilterParam.setStation_id(station_id);
			stUserFilterParam.setType_id(999);
			List<StUserBean> stUsers = mgAccountService.searchStationUser(stUserFilterParam);
			Assert.assertNotEquals(stUsers, null, "信息平台搜索过滤业务平台账号失败");

			Assert.assertEquals(stUsers.size() > 0, true, "此Group下没有站点管理员账号");

			// 取第一个
			StUserBean stUser = stUsers.get(0);
			String st_user_name = stUser.getUsername();
			BigDecimal st_user_id = stUser.getId();

			result = mgAccountService.changeStationUserPassword(st_user_id, "liuge1");
			Assert.assertEquals(result, true, "修改业务平台管理员账号密码失败");

			String station_url = manage_url.replaceAll("manage", "station");
			ConfigureUtil.updateProperties("stationUrl", station_url);
			ConfigureUtil.updateProperties("stationName", st_user_name);
			ConfigureUtil.updateProperties("stationPwd", "liuge1");

			Map<String, String> st_headers = LoginUtil.loginStation(st_user_name, "liuge1");
			Assert.assertNotEquals(st_headers, null, "业务平台登录失败");

			ProfileService profileService = new ProfileServiceImpl(st_headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品自动进入采购任务失败");

			CustomizedService customizedService = new CustomizedServiceImpl(st_headers);
			String cms_key = customizedService.getCmsKey();
			Assert.assertNotEquals(cms_key, null, "获取cms_key失败");

			String bshop_url = manage_url.replaceAll("manage", "bshop");

			ConfigureUtil.updateProperties("bshopUrl", bshop_url);
			ConfigureUtil.updateProperties("bshopName", bs_user_name);
			ConfigureUtil.updateProperties("bshopPwd", "1qaz2wsx");
			ConfigureUtil.updateProperties("cms_key", cms_key);

			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(st_headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账号信息失败");

			int stock_method = loginUserInfo.getStock_method();
			logger.info("进销存类型为: " + (stock_method == 1 ? "加权平均" : "先进先出"));

			LoginStationInfoBean loginStationInfo = loginUserInfoService.getLoginStationInfo();
			Assert.assertNotEquals(loginStationInfo, null, "获取登录站点信息失败");
			boolean clean_food = loginStationInfo.isClean_food();
			logger.info("是否为净菜站点: " + clean_food);

			TestNG testng = new TestNG();

			String dirPath = System.getProperty("user.dir");
			String testngxml_st = dirPath + "/testngxml/station";
			File dirFile = new File(testngxml_st);
			String[] fileList = dirFile.list();

			List<String> suits = new ArrayList<String>();

			for (String fileName : fileList) {
				suits.add(testngxml_st + "/" + fileName);
			}

			// 先进先出和加权平均执行用例区分
			if (stock_method == 1) {
				suits = suits.stream().filter(s -> !s.contains("xjxc")).collect(Collectors.toList());
			} else {
				suits = suits.stream().filter(s -> !s.contains("jqpj")).collect(Collectors.toList());
			}

			String testngxml_bs = dirPath + "/testngxml/bshop";
			dirFile = new File(testngxml_bs);
			fileList = dirFile.list();
			for (String fileName : fileList) {
				suits.add(testngxml_bs + "/" + fileName);
			}

			suits.add(0, dirPath + "/testngxml/IReporterListener.xml");
			logger.info(suits.toString());
			testng.setTestSuites(suits);
			testng.run();

			String reportDir = "本地执行";
			String desc = "";
			if (args.length >= 1) {
				reportDir = args[0];
			}

			if (args.length >= 2) {
				desc = args[1];
			}

			ReportBotUtil.sendReportResult(reportDir, desc);
		} catch (Exception e) {
			logger.error("遇到错误: " + e);
			Assert.fail("遇到错误: ", e);
		}
	}
}
