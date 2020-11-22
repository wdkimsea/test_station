package cn.guanmai.manage.custommange;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.result.CustomerBaseInfoBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.bean.custommange.result.MgCustomerDetailBean;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.manage.tools.LoginManage;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/**
 * @author: liming
 * @Date: 2020年7月23日 下午5:39:07
 * @description: 商户列表搜索过滤
 * @version: 1.0
 */

public class CustomerFilterTest extends LoginManage {
	private Logger logger = LoggerFactory.getLogger(CustomerManageTest.class);
	private Map<String, String> ma_headers;
	private MgCustomerService mgCustomerService;
	private CustomerBaseInfoBean customerBaseInfo;
	private MgCustomerDetailBean customerDetail;
	private MgCustomerFilterParam customerFilterParam;
	private String address_id;

	@BeforeClass
	public void initData() {
		ma_headers = getManageCookie();
		mgCustomerService = new MgCustomerServiceImpl(ma_headers);
		try {
			customerBaseInfo = mgCustomerService.getCustomerBaseInfo();
			Assert.assertNotEquals(customerBaseInfo, null, "获取商户基础信息失败");

			MgCustomerFilterParam filterParam = new MgCustomerFilterParam();
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(filterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");
			Assert.assertEquals(customerList.size() > 0, true, "此Group下无商户条目信息");

			MgCustomerBean customer = NumberUtil.roundNumberInList(customerList);
			address_id = customer.getSID();

			customerDetail = mgCustomerService.getCustomerDetailInfoBySID(address_id);
			Assert.assertNotEquals(customerDetail, null, "获取商户" + address_id + "详细信息失败");
		} catch (Exception e) {
			logger.error("获取商户基础信息出现错误: ", e);
			Assert.fail("获取商户基础信息出现错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		customerFilterParam = new MgCustomerFilterParam();
	}

	@Test
	public void customerFilterTestCase01() {
		ReporterCSS.title("测试点: 按地理标签[城市]过滤商户信息");
		try {
			String city_code = customerDetail.getDistrict_code();
			String city_name = customerDetail.getCity();

			customerFilterParam.setCity(city_code);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			List<String> other_city_names = customerList.stream().filter(c -> !c.getCity().equals(city_name))
					.map(c -> c.getCity()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (other_city_names.size() > 0) {
				msg = String.format("商户列表,按地理标签[%s]过滤,过滤出了其他城市%s的商户信息", city_name, other_city_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按地理标签[%s]过滤,没有过滤出目标商户%s", city_name, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按地理标签过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase02() {
		ReporterCSS.title("测试点: 按地理标签[城市-区]过滤商户信息");
		try {
			String city_code = customerDetail.getDistrict_code();
			String city_name = customerDetail.getCity();

			String area_l1 = customerDetail.getArea_l1();
			String area_l1_code = customerDetail.getArea_l1_id();

			customerFilterParam.setCity(city_code);
			customerFilterParam.setFirstMenu(area_l1_code);

			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			List<String> other_area_l1s = customerList.stream().filter(c -> !c.getArea_l1().equals(area_l1))
					.map(c -> c.getArea_l1()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (other_area_l1s.size() > 0) {
				msg = String.format("商户列表,按地理标签[%s-%s]过滤,过滤出了其他地区%s的商户信息", city_name, area_l1, other_area_l1s);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按地理标签[%s-%s]过滤,没有过滤出目标商户%s", city_name, area_l1, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按地理标签过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase03() {
		ReporterCSS.title("测试点: 按地理标签[城市-区-街道]过滤商户信息");
		try {
			String city_code = customerDetail.getDistrict_code();
			String city_name = customerDetail.getCity();

			String area_l1 = customerDetail.getArea_l1();
			String area_l1_code = customerDetail.getArea_l1_id();

			String area_l2 = customerDetail.getArea_l2();
			String area_l2_code = customerDetail.getArea_l2_id();

			customerFilterParam.setCity(city_code);
			customerFilterParam.setFirstMenu(area_l1_code);
			customerFilterParam.setSecondMenu(area_l2_code);

			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			List<String> other_area_l2s = customerList.stream().filter(c -> !c.getArea_l2().equals(area_l2))
					.map(c -> c.getArea_l2()).collect(Collectors.toList());

			String msg = null;
			boolean result = true;
			if (other_area_l2s.size() > 0) {
				msg = String.format("商户列表,按地理标签[%s-%s-%s]过滤,过滤出了其他街道%s的商户信息", city_name, area_l1, area_l2,
						other_area_l2s);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按地理标签[%s-%s-%s]过滤,没有过滤出目标商户%s", city_name, area_l1, area_l2, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按地理标签过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase04() {
		ReporterCSS.title("测试点: 按报价单过滤商户信息(选择某个报价单)");
		try {
			List<MgCustomerDetailBean.Salemenu> salemenuList = customerDetail.getSalemenus();
			MgCustomerDetailBean.Salemenu salemenu = NumberUtil.roundNumberInList(salemenuList);
			String station_id = salemenu.getStation_id();
			String salemenu_id = salemenu.getSalemenu_id();

			customerFilterParam.setStationMenu(station_id);
			customerFilterParam.setSaleMenu(salemenu_id);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按报价单[%s]过滤,没有过滤出目标商户%s", salemenu_id, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按报价单过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase05() {
		ReporterCSS.title("测试点: 按报价单过滤商户信息(选择某个站点)");
		try {
			List<MgCustomerDetailBean.Salemenu> salemenuList = customerDetail.getSalemenus();
			MgCustomerDetailBean.Salemenu salemenu = NumberUtil.roundNumberInList(salemenuList);
			String station_id = salemenu.getStation_id();
			String salemenu_id = salemenu.getSalemenu_id();

			customerFilterParam.setStationMenu(station_id);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按报价单[%s]过滤,没有过滤出目标商户%s", salemenu_id, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按报价单过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase06() {
		ReporterCSS.title("测试点: 按商户SID搜索过滤商户信息");
		try {
			customerFilterParam.setSearch_text(address_id);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;

			List<String> other_address_ids = customerList.stream().filter(c -> !c.getSID().equals(address_id))
					.map(c -> c.getSID()).collect(Collectors.toList());
			if (other_address_ids.size() > 0) {
				msg = String.format("商户列表,按商户SID[%s]过滤,过滤出了其他商户%s", address_id, other_address_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按商户SID过滤,没有过滤出目标商户%s", address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按商户SID过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase07() {
		ReporterCSS.title("测试点: 按商户KID搜索过滤商户信息");
		try {
			String kid = customerDetail.getKID();
			customerFilterParam.setSearch_text(kid);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> other_kids = customerList.stream().filter(c -> !c.getKID().equals(kid)).map(c -> c.getKID())
					.collect(Collectors.toList());
			if (other_kids.size() > 0) {
				msg = String.format("商户列表,按商户KID[%s]过滤,过滤出了其他商户%s", kid, other_kids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按商户KID过滤,没有过滤出目标商户%s", kid);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按商户KID过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase08() {
		ReporterCSS.title("测试点: 按账户名搜索过滤商户信息");
		try {
			String username = customerDetail.getUsername();
			customerFilterParam.setSearch_text(username);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> other_usernames = customerList.stream().filter(c -> !c.getUsername().contains(username))
					.map(c -> c.getUsername()).collect(Collectors.toList());
			if (other_usernames.size() > 0) {
				msg = String.format("商户列表,按账户名[%s]过滤,过滤出了其他商户%s", username, other_usernames);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按账户名过滤,没有过滤出目标商户%s", username);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按账户名过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase09() {
		ReporterCSS.title("测试点: 按店铺名搜索过滤商户信息");
		try {
			String resname = customerDetail.getResname();
			customerFilterParam.setSearch_text(resname);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> other_resnames = customerList.stream().filter(c -> !c.getResname().contains(resname))
					.map(c -> c.getResname()).collect(Collectors.toList());
			if (other_resnames.size() > 0) {
				msg = String.format("商户列表,按店铺名[%s]过滤,过滤出了其他商户%s", resname, other_resnames);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按店铺名过滤,没有过滤出目标商户%s", resname);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按店铺名过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase10() {
		ReporterCSS.title("测试点: 按审核状态过滤商户信息");
		try {
			customerFilterParam.setCheck_out(1);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> resnames = customerList.stream().filter(c -> c.getCheck_out() == 0).map(c -> c.getResname())
					.collect(Collectors.toList());
			if (resnames.size() > 0) {
				msg = String.format("商户列表,按审核状态[已审核]过滤,过滤出了其他商户%s", resnames);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按店铺名过滤,没有过滤出目标商户%s", customerDetail.getResname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按店铺名过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase11() {
		ReporterCSS.title("测试点: 按审核状态过滤商户信息");
		try {
			customerFilterParam.setCheck_out(1);
			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> resnames = customerList.stream().filter(c -> c.getCheck_out() == 0).map(c -> c.getResname())
					.collect(Collectors.toList());
			if (resnames.size() > 0) {
				msg = String.format("商户列表,按审核状态[已审核]过滤,过滤出了其他商户%s", resnames);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (customerList.size() == 0) {
				msg = String.format("商户列表,按店铺名过滤,没有过滤出目标商户%s", customerDetail.getResname());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商户列表,按店铺名过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase12() {
		ReporterCSS.title("测试点: 按结款方式搜索过滤商户");
		try {
			customerFilterParam.setSettle_way(1);

			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

			String msg = null;
			boolean result = true;
			List<String> resnames = customerList.stream().filter(c -> !c.getSettle_way().equals("先货后款"))
					.map(c -> c.getResname()).collect(Collectors.toList());
			if (resnames.size() > 0) {
				msg = String.format("商户列表,按结款方式[先款后货]搜索过滤商户,过滤出了其他结款方式的商户%s", resnames);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "商户列表,按结款方式[先款后货]搜索过滤商户,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

	@Test
	public void customerFilterTestCase13() {
		ReporterCSS.title("测试点: 按开户经理和销售经理过滤商户");
		try {
			List<String> create_employee_ids = customerBaseInfo.getCreate_employee_ids();
			List<String> sale_employee_ids = customerBaseInfo.getSale_employee_ids();
			String create_employee_id = NumberUtil.roundNumberInList(create_employee_ids);
			String sale_employee_id = NumberUtil.roundNumberInList(sale_employee_ids);

			customerFilterParam.setCrtManager(create_employee_id);
			customerFilterParam.setSaleManager(sale_employee_id);

			List<MgCustomerBean> customerList = mgCustomerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(customerList, null, "获取商户列表信息失败");

		} catch (Exception e) {
			logger.error("搜索过滤商户信息遇到错误: ", e);
			Assert.fail("搜索过滤商户信息遇到错误: ", e);
		}
	}

}
