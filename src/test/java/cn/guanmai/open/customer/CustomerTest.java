package cn.guanmai.open.customer;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.open.bean.customer.OpenCustomerAreaBean;
import cn.guanmai.open.bean.customer.OpenCustomerBean;
import cn.guanmai.open.bean.customer.param.OpenCustomerCreateParam;
import cn.guanmai.open.bean.customer.param.OpenCustomerUpdateParam;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 17, 2019 10:02:48 AM 
* @des 开放平台接口商户管理接口
* @version 1.0 
*/
public class CustomerTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CustomerTest.class);
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private MgCustomerService customerService;
	private List<String> salemenu_id_list;
	private String district_code;
	private String district_name;
	private String area_level1_code;
	private String area_level1_code_2;
	private String area_level1_name;
	private String area_level2_code;
	private String area_level2_code_2;
	private String area_level2_name;
	private String customer_name;
	private String customer_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCustomerService = new OpenCustomerServiceImpl(access_token);
		openSalemenuService = new OpenSalemenuServiceImpl(access_token);
		customerService = new MgCustomerServiceImpl(getMa_headers());
		try {
			customer_name = StringUtil.getRandomString(6).toUpperCase();
			List<OpenCustomerAreaBean> openCustomerAreaList = openCustomerService.getAreaList();
			Assert.assertNotNull(openCustomerAreaList, "获取商户地理标签失败");

			Assert.assertEquals(openCustomerAreaList.size() >= 1, true, "地理标签列表为空");
			OpenCustomerAreaBean openCustomerArea = openCustomerAreaList.get(0);
			district_code = openCustomerArea.getCode();
			district_name = openCustomerArea.getName();

			List<OpenCustomerAreaBean.AreaLevel1> area_level1_list = openCustomerArea.getArea_level1();
			Assert.assertEquals(area_level1_list != null && area_level1_list.size() >= 1, true, "地理标签列表为空");

			OpenCustomerAreaBean.AreaLevel1 area_level1 = area_level1_list.get(0);
			area_level1_code = area_level1.getCode();

			Assert.assertEquals(area_level1.getArea_level2().size() > 0, true, "二级地理标签列表为空");

			area_level2_code = area_level1.getArea_level2().get(0).getCode();
			area_level2_name = area_level1.getArea_level2().get(0).getName();

			area_level1_name = area_level1.getName();
			if (area_level1_list.size() >= 2) {
				OpenCustomerAreaBean.AreaLevel1 area_level1_2 = area_level1_list.get(1);
				area_level1_code_2 = area_level1_2.getCode();

				if (area_level1_2.getArea_level2().size() > 0) {
					area_level2_code_2 = area_level1_2.getArea_level2().get(0).getCode();
				}

			}

			List<OpenSalemenuBean> salemenu_list = openSalemenuService.searchSalemenu(null, 1);
			Assert.assertNotEquals(salemenu_list, null, "获取站点报价单列表失败");

			salemenu_id_list = salemenu_list.stream().map(s -> s.getId()).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("获取商户地理标签遇到错误: ", e);
			Assert.fail("获取商户地理标签遇到错误: ", e);
		}
	}

	@Test
	public void customerTestCase01() {
		ReporterCSS.title("测试点: 新建商户");
		try {
			String telephone = "12" + StringUtil.getRandomNumber(9);
			String address = "长安街1号";
			OpenCustomerCreateParam customerCreateParam = new OpenCustomerCreateParam();
			customerCreateParam.setArea_level1(area_level1_code);
			customerCreateParam.setArea_level2(area_level2_code);
			customerCreateParam.setDistrict_code(district_code);
			customerCreateParam.setPassword("Test1234_");
			customerCreateParam.setPayer_name(customer_name);
			customerCreateParam.setPayer_telephone(telephone);
			customerCreateParam.setReceiver_name(customer_name);
			customerCreateParam.setReceiver_telephone(telephone);
			customerCreateParam.setCustomer_address(address);
			customerCreateParam.setCustomer_name(customer_name);
			customerCreateParam.setUsername(customer_name);
			customerCreateParam.setSalemenu_ids(salemenu_id_list);
			customerCreateParam.setRes_custom_code("汉A0(（）)[-]");

			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertNotEquals(customer_id, null, "新建商户失败");

			List<OpenCustomerBean> openCustomerList = openCustomerService.searchCustomer(customer_id, null, 0, 20);
			Assert.assertNotEquals(customer_id, null, "拉取商户列表失败");

			OpenCustomerBean openCustomer = openCustomerList.stream()
					.filter(c -> c.getCustomer_id().equals(customer_id)).findAny().orElse(null);
			Assert.assertNotNull(openCustomer, "新建的商户在商户列表没有找到");

			String msg = null;
			boolean result = true;
			if (!openCustomer.getDistrict_code().equals(district_code)) {
				msg = String.format("创建商户绑定的district_code和预期的不一致,预期:%s,实际:%s", district_code,
						openCustomer.getDistrict_code());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getDistrict_name().equals(district_name)) {
				msg = String.format("创建商户绑定的district_name和预期的不一致,预期:%s,实际:%s", district_name,
						openCustomer.getDistrict_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getArea_level1().equals(area_level1_code)) {
				msg = String.format("创建商户绑定的area_level1_code和预期的不一致,预期:%s,实际:%s", area_level1_code,
						openCustomer.getArea_level1());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getArea_level1_name().equals(area_level1_name)) {
				msg = String.format("创建商户绑定的area_level1_name和预期的不一致,预期:%s,实际:%s", area_level1_name,
						openCustomer.getArea_level1_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getArea_level2().equals(area_level2_code)) {
				msg = String.format("创建商户绑定的area_level2_code和预期的不一致,预期:%s,实际:%s", area_level2_code,
						openCustomer.getArea_level2());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getArea_level2_name().equals(area_level2_name)) {
				msg = String.format("创建商户绑定的area_level2_name和预期的不一致,预期:%s,实际:%s", area_level2_name,
						openCustomer.getArea_level2_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getCustomer_name().equals(customer_name)) {
				msg = String.format("创建商户绑定的customer_name和预期的不一致,预期:%s,实际:%s", customer_name,
						openCustomer.getCustomer_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getCustomer_address().equals(address)) {
				msg = String.format("创建商户绑定的customer_address和预期的不一致,预期:%s,实际:%s", address,
						openCustomer.getCustomer_address());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getPayer_name().equals(customer_name)) {
				msg = String.format("创建商户绑定的payment_name和预期的不一致,预期:%s,实际:%s", customer_name,
						openCustomer.getPayer_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getPayer_telephone().equals(telephone)) {
				msg = String.format("创建商户绑定的payment_telephone和预期的不一致,预期:%s,实际:%s", telephone,
						openCustomer.getPayer_telephone());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getReceiver_name().equals(customer_name)) {
				msg = String.format("创建商户绑定的receiver_name和预期的不一致,预期:%s,实际:%s", customer_name,
						openCustomer.getReceiver_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getReceiver_telephone().equals(telephone)) {
				msg = String.format("创建商户绑定的receiver_phone和预期的不一致,预期:%s,实际:%s", telephone,
						openCustomer.getReceiver_telephone());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getRes_custom_code().equals(customerCreateParam.getRes_custom_code())) {
				msg = String.format("创建商户绑定的res_custom_code和预期的不一致,预期:%s,实际:%s",
						customerCreateParam.getRes_custom_code(), openCustomer.getRes_custom_code());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "创建商户所填信息和创建完成查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = { "customerTestCase01" })
	public void customerTestCase02() {
		ReporterCSS.title("测试点: 修改商户信息");
		try {
			String new_name = StringUtil.getRandomString(6).toUpperCase();
			String payer_name = StringUtil.getRandomString(6).toUpperCase();
			String receiver_name = StringUtil.getRandomString(6).toUpperCase();
			String payer_telephone = "12" + StringUtil.getRandomNumber(9);
			String receiver_telephone = "12" + StringUtil.getRandomNumber(9);
			String address = "幸福大道";
			String res_custom_code = "自Z9[-](（）)";
			OpenCustomerUpdateParam customerUpdateParam = new OpenCustomerUpdateParam();
			customerUpdateParam.setCustomer_id(customer_id);
			if (area_level2_code_2 != null) {
				customerUpdateParam.setArea_level1(area_level1_code_2);
				customerUpdateParam.setArea_level2(area_level2_code_2);
			}

			customerUpdateParam.setCustomer_name(new_name);
			customerUpdateParam.setCustomer_address(address);
			customerUpdateParam.setPayer_name(payer_name);
			customerUpdateParam.setReceiver_name(receiver_name);

			customerUpdateParam.setPayer_telephone(payer_telephone);
			customerUpdateParam.setReceiver_telephone(receiver_telephone);
			customerUpdateParam.setRes_custom_code(res_custom_code);

			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, true, "修改商户信息失败");

			List<OpenCustomerBean> openCustomerList = openCustomerService.searchCustomer(customer_id, null, 0, 20);
			Assert.assertNotEquals(customer_id, null, "拉取商户列表失败");

			OpenCustomerBean openCustomer = openCustomerList.stream()
					.filter(c -> c.getCustomer_id().equals(customer_id)).findAny().orElse(null);
			Assert.assertNotNull(openCustomer, "新建的商户在商户列表没有找到");

			String msg = null;
			boolean result = true;

			if (area_level2_code_2 != null) {
				if (!openCustomer.getArea_level1().equals(area_level1_code_2)) {
					msg = String.format("修改商户绑定的area_level1_code和预期的不一致,预期:%s,实际:%s", area_level1_code_2,
							openCustomer.getArea_level1());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}

				if (!openCustomer.getArea_level2().equals(area_level2_code_2)) {
					msg = String.format("修改商户绑定的area_level2_code和预期的不一致,预期:%s,实际:%s", area_level2_code_2,
							openCustomer.getArea_level2());
					ReporterCSS.warn(msg);
					logger.info(msg);
					result = false;
				}
			}

			if (!openCustomer.getCustomer_name().equals(new_name)) {
				msg = String.format("修改商户绑定的customer_name和预期的不一致,预期:%s,实际:%s", new_name,
						openCustomer.getCustomer_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getCustomer_address().equals(address)) {
				msg = String.format("修改商户绑定的customer_address和预期的不一致,预期:%s,实际:%s", address,
						openCustomer.getCustomer_address());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getPayer_name().equals(payer_name)) {
				msg = String.format("修改商户绑定的payment_name和预期的不一致,预期:%s,实际:%s", customer_name,
						openCustomer.getPayer_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getPayer_telephone().equals(payer_telephone)) {
				msg = String.format("修改商户绑定的payment_telephone和预期的不一致,预期:%s,实际:%s", payer_telephone,
						openCustomer.getPayer_telephone());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getReceiver_name().equals(receiver_name)) {
				msg = String.format("修改商户绑定的receiver_name和预期的不一致,预期:%s,实际:%s", receiver_name,
						openCustomer.getReceiver_name());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getReceiver_telephone().equals(receiver_telephone)) {
				msg = String.format("修改商户绑定的receiver_telephone和预期的不一致,预期:%s,实际:%s", receiver_telephone,
						openCustomer.getReceiver_telephone());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			if (!openCustomer.getRes_custom_code().equals(res_custom_code)) {
				msg = String.format("修改商户绑定的res_custom_code和预期的不一致,预期:%s,实际:%s", res_custom_code,
						openCustomer.getRes_custom_code());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改商户所填信息和创建完成查询到的信息不一致");
		} catch (Exception e) {
			logger.error("修改商户遇到错误: ", e);
			Assert.fail("修改商户遇到错误: ", e);
		}
	}

	@AfterTest
	public void afterTest() {
		try {
			if (customer_id != null) {
				boolean result = customerService.deleteCustomter(customer_id);
				Assert.assertEquals(result, true, "后置处理,删除商户" + customer_id + "失败");
			}
		} catch (Exception e) {
			logger.error("删除商户遇到错误: ", e);
			Assert.fail("删除商户遇到错误: ", e);
		}
	}
}
