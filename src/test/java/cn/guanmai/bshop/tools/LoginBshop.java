package cn.guanmai.bshop.tools;

import cn.guanmai.bshop.bean.account.BsAccountBean;
import cn.guanmai.bshop.impl.BshopServiceImpl;
import cn.guanmai.bshop.service.BshopService;
import cn.guanmai.manage.bean.custommange.param.MgCustomerFilterParam;
import cn.guanmai.manage.bean.custommange.result.MgCustomerBean;
import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.util.ConfigureUtil;
import cn.guanmai.util.LoginUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/* 
* @author liming 
* @date Apr 19, 2019 5:37:55 PM 
* @des BSHOP登录
* @version 1.0 
*/
public class LoginBshop {
	private static Logger logger = LoggerFactory.getLogger(LoginBshop.class);
	private Map<String, String> bs_headers;
	private String address_id;

	@BeforeTest
	public void beforeClass() {
		try {
			String regex = "http(s)?://bshop.(.)*guanmai.cn(/v587)?";
			String bshop_url = ConfigureUtil.getValueByKey("bshopUrl");
			Assert.assertEquals(Pattern.matches(regex, bshop_url), true, "配置文件中的BSHOP路径:" + bshop_url + " 不符合规范");

			regex = "http(s)?://station.(.)*guanmai.cn";
			String station_url = ConfigureUtil.getValueByKey("stationUrl");
			Assert.assertEquals(Pattern.matches(regex, station_url), true, "配置文件中的Station路径:" + station_url + " 不符合规范");

			regex = "http(s)?://manage.(.)*guanmai.cn";
			String manage_url = ConfigureUtil.getValueByKey("manageUrl");
			Assert.assertEquals(Pattern.matches(regex, manage_url), true, "配置文件中的Station路径:" + manage_url + " 不符合规范");

			String st_env = station_url.split("station.")[1].split(".cn")[0];
			String bs_env = bshop_url.split("bshop.")[1].split(".cn")[0];
			String ma_env = manage_url.split("manage.")[1].split(".cn")[0];
			Assert.assertEquals(st_env, bs_env, "配置文件配置的Staion和BSHOP不是同一环境");
			Assert.assertEquals(st_env, ma_env, "配置文件配置的Staion和Manage不是同一环境");

			bs_headers = LoginUtil.loginBshop();
			Assert.assertNotEquals(bs_headers, null, "BSHOP登录失败");

			BshopService bshopService = new BshopServiceImpl(bs_headers);
			BsAccountBean account = bshopService.getAccountInfo();
			Assert.assertNotEquals(account, null, "BSHOP获取登录账户信息失败");

			Map<String, String> st_headers = LoginUtil.loginStation();
			Assert.assertNotEquals(st_headers, null, "Station登录失败");

			Map<String, String> ma_headers = LoginUtil.loginManage();
			Assert.assertNotEquals(ma_headers, null, "Manage登录失败");

			OrderService orderService = new OrderServiceImpl(st_headers);
			List<CustomerBean> customerArray = orderService.getCustomers();
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			CustomerBean customer = null;
			List<BsAccountBean.Address> addresses = account.getAddresses();
			for (BsAccountBean.Address address : addresses) {
				String sid = address.getSid();
				customer = customerArray.stream().filter(c -> c.getAddress_id().contains(sid)).findAny().orElse(null);
				if (customer != null) {
					break;
				}
			}
			Assert.assertNotEquals(customer, null, "配置的BSHOP账号没有绑定配置的Station站点的报价单,请检测配置");

			address_id = customer.getAddress_id();

			String sid = String.format("S%6s", customer.getAddress_id()).replace("\\s", "0");

			MgCustomerService customerService = new MgCustomerServiceImpl(ma_headers);
			MgCustomerFilterParam customerFilterParam = new MgCustomerFilterParam();
			customerFilterParam.setSearch_text(customer.getUsername());
			List<MgCustomerBean> mgCustomers = customerService.searchCustomer(customerFilterParam);
			Assert.assertNotEquals(mgCustomers, null, "信息平台搜索过滤商户信息失败");

			MgCustomerBean mgCustomer = mgCustomers.stream().filter(c -> c.getSID().equals(sid)).findAny().orElse(null);
			Assert.assertNotEquals(mgCustomer, null, "配置的BSHOP账号没有在信息平台中");

			boolean result = bshopService.setAddress(address_id);
			Assert.assertEquals(result, true, "BSHOP设置店铺操作失败");
		} catch (Exception e) {
			logger.error("登录BSHOP遇到错误", e);
			Assert.fail("登录BSHOP遇到错误: ", e);
		}
	}

	public Map<String, String> getBshopCookie() {
		return bs_headers;
	}

	public String getAddressId() {
		return address_id;
	}

}
