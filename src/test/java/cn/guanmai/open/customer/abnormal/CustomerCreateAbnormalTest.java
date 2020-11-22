package cn.guanmai.open.customer.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.open.bean.customer.OpenCustomerAreaBean;
import cn.guanmai.open.bean.customer.param.OpenCustomerCreateParam;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.customer.CustomerTest;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 17, 2019 4:34:49 PM 
* @des 创建商户异常测试
* @version 1.0 
*/
public class CustomerCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CustomerTest.class);
	private MgCustomerService customerService;
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private List<String> salemenu_id_list;
	private String district_code;
	private String area_level1_code;
	private String area_level2_code;
	private String customer_name;
	private String customer_id;
	private OpenCustomerCreateParam customerCreateParam;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openCustomerService = new OpenCustomerServiceImpl(access_token);
		openSalemenuService = new OpenSalemenuServiceImpl(access_token);

		Map<String, String> ma_hearders = getMa_headers();
		customerService = new MgCustomerServiceImpl(ma_hearders);
		try {
			customer_name = StringUtil.getRandomString(6).toUpperCase();
			List<OpenCustomerAreaBean> openCustomerAreaList = openCustomerService.getAreaList();
			Assert.assertNotNull(openCustomerAreaList, "获取商户地理标签失败");

			Assert.assertEquals(openCustomerAreaList.size() >= 1, true, "地理标签列表为空");
			OpenCustomerAreaBean openCustomerArea = openCustomerAreaList.get(0);
			district_code = openCustomerArea.getCode();

			List<OpenCustomerAreaBean.AreaLevel1> area_level1_list = openCustomerArea.getArea_level1();
			Assert.assertEquals(area_level1_list != null && area_level1_list.size() >= 1, true, "地理标签列表为空");

			OpenCustomerAreaBean.AreaLevel1 area_level1 = area_level1_list.get(0);
			area_level1_code = area_level1.getCode();

			List<OpenCustomerAreaBean.AreaLevel1.AreaLevel2> area_level2_list = area_level1.getArea_level2();
			Assert.assertEquals(area_level2_list != null && area_level2_list.size() >= 1, true, "地理标签列表为空");

			OpenCustomerAreaBean.AreaLevel1.AreaLevel2 area_level2 = area_level2_list.get(0);
			area_level2_code = area_level2.getCode();

			List<OpenSalemenuBean> salemenu_list = openSalemenuService.searchSalemenu(null, 1);
			Assert.assertNotEquals(salemenu_list, null, "获取站点报价单列表失败");

			salemenu_id_list = salemenu_list.stream().map(s -> s.getId()).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("获取商户地理标签遇到错误: ", e);
			Assert.fail("获取商户地理标签遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		String telephone = "12" + StringUtil.getRandomNumber(9);
		String address = "长安街1号";
		customerCreateParam = new OpenCustomerCreateParam();
		customerCreateParam.setDistrict_code(district_code);
		customerCreateParam.setArea_level1(area_level1_code);
		customerCreateParam.setArea_level2(area_level2_code);
		customerCreateParam.setPassword("Test1234_");
		customerCreateParam.setPayer_name(customer_name);
		customerCreateParam.setPayer_telephone(telephone);
		customerCreateParam.setReceiver_name(customer_name);
		customerCreateParam.setReceiver_telephone(telephone);
		customerCreateParam.setCustomer_address(address);
		customerCreateParam.setCustomer_name(customer_name);
		customerCreateParam.setUsername(customer_name);
		customerCreateParam.setSalemenu_ids(salemenu_id_list);
	}

	@Test
	public void customerCreateAbnormalTestCase01() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,username输入为空,断言失败");
			customerCreateParam.setUsername("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,username输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase02() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,username输入为空格,断言失败");
			customerCreateParam.setUsername("  ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,username输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase03() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,username输入过长字符(大于32个字符),断言失败");
			customerCreateParam.setUsername(StringUtil.getRandomString(33));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "username输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase04() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,district_code为空,断言失败");
			customerCreateParam.setDistrict_code("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,district_code为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase05() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,district_code为没绑定的地区编码,断言失败");
			customerCreateParam.setDistrict_code("450100");
			customerCreateParam.setArea_level1("100200000000");
			customerCreateParam.setArea_level2("100200200000");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,district_code为没绑定的地区编码,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase06() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,area_level1输入为空,断言失败");
			customerCreateParam.setArea_level1("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,district_code为没绑定的地区编码,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase07() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,area_level1输入与district_code不匹配的值,断言失败");
			customerCreateParam.setArea_level1("100200000000");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,district_code为没绑定的地区编码,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase08() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,area_level2输入为空,断言失败");
			customerCreateParam.setArea_level2("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,area_level2输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase09() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,area_level2输入为与area_level1不匹配的值,断言失败");
			customerCreateParam.setArea_level2("100200200000");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,area_level2输入为与area_level1不匹配的值,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase10() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,password输入为空,断言失败");
			customerCreateParam.setPassword("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,password输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase11() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,password输入为空格,断言失败");
			customerCreateParam.setPassword("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,password输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase12() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,password输入过长字符(大于32个字符),断言失败");
			customerCreateParam.setPassword(StringUtil.getRandomString(33));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,password输入过长字符(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase13() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,payer_name输入为空,断言失败");
			customerCreateParam.setPayer_name("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase14() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,payer_name输入为空格,断言失败");
			customerCreateParam.setPayer_name("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase15() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,payer_name输入过长字符串(大于32个字符),断言失败");
			customerCreateParam.setPayer_name(StringUtil.getRandomString(33));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase16() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,payer_telephone输入为空,断言失败");
			customerCreateParam.setPayer_telephone("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_telephone输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase17() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,payer_telephone输入为空格,断言失败");
			customerCreateParam.setPayer_telephone("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_telephone输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

//	//@Test
//	public void customerCreateAbnormalTestCase18() {
//		try {
//			ReporterCSS.title("测试点: 新建商户异常测试,payer_telephone输入非数字,断言失败");
//			customerCreateParam.setPayer_name("12D" + StringUtil.getRandomString(8));
//			customer_id = openCustomerService.createCustomer(customerCreateParam);
//			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_telephone输入非数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

//	//@Test
//	public void customerCreateAbnormalTestCase19() {
//		try {
//			ReporterCSS.title("测试点: 新建商户异常测试,payer_telephone输入非11位数字,断言失败");
//			customerCreateParam.setPayer_name("12" + StringUtil.getRandomNumber(8));
//			customer_id = openCustomerService.createCustomer(customerCreateParam);
//			Assert.assertEquals(customer_id, null, "新建商户异常测试,payer_telephone输入非11位数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

	@Test
	public void customerCreateAbnormalTestCase20() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,receiver_name输入为空,断言失败");
			customerCreateParam.setReceiver_name("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase21() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,receiver_name输入为空格,断言失败");
			customerCreateParam.setReceiver_name("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase22() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,receiver_name输入过长字符串(大于32个字符),断言失败");
			customerCreateParam.setReceiver_name(StringUtil.getRandomString(33));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase23() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,receiver_telephone输入为空,断言失败");
			customerCreateParam.setReceiver_telephone("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_telephone输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase24() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,receiver_telephone输入为空格,断言失败");
			customerCreateParam.setReceiver_telephone("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_telephone输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

//	@Test
//	public void customerCreateAbnormalTestCase25() {
//		try {
//			ReporterCSS.title("测试点: 新建商户异常测试,receiver_telephone输入非数字,断言失败");
//			customerCreateParam.setReceiver_telephone("12D" + StringUtil.getRandomString(8));
//			customer_id = openCustomerService.createCustomer(customerCreateParam);
//			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_telephone输入非数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

//	@Test
//	public void customerCreateAbnormalTestCase26() {
//		try {
//			ReporterCSS.title("测试点: 新建商户异常测试,receiver_telephone输入非11位数字,断言失败");
//			customerCreateParam.setReceiver_telephone("12" + StringUtil.getRandomNumber(8));
//			customer_id = openCustomerService.createCustomer(customerCreateParam);
//			Assert.assertEquals(customer_id, null, "新建商户异常测试,receiver_telephone输入非11位数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

	@Test
	public void customerCreateAbnormalTestCase27() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,customer_address输入为空,断言失败");
			customerCreateParam.setCustomer_address("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,customer_address输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase28() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,customer_address输入为空格,断言失败");
			customerCreateParam.setCustomer_address("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,customer_address输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase29() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,customer_name输入为空,断言失败");
			customerCreateParam.setCustomer_name("");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,customer_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase30() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,customer_name输入为空格,断言失败");
			customerCreateParam.setCustomer_name("      ");
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,customer_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase31() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,customer_name输入过长字符串(大于32个字符),断言失败");
			customerCreateParam.setCustomer_name(StringUtil.getRandomString(33));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,customer_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase32() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,salemenu_ids不传值,断言失败");
			customerCreateParam.setSalemenu_ids(null);
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,salemenu_ids不传值,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase33() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,salemenu_ids输入空数组,断言失败");
			customerCreateParam.setSalemenu_ids(new ArrayList<String>());
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,salemenu_ids输入空数组,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase34() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,salemenu_ids输入别的站点的报价单ID,断言失败");
			customerCreateParam.setSalemenu_ids(Arrays.asList("S13776"));
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,salemenu_ids输入别的站点的报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase35() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,res_custom_code输入过长字符(大于10个字符)");
			customerCreateParam.setRes_custom_code(StringUtil.getRandomString(11).toUpperCase());
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,res_custome_code输入过长字符(大于10个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerCreateAbnormalTestCase36() {
		try {
			ReporterCSS.title("测试点: 新建商户异常测试,res_custom_code输入非法字符(小写字符)");
			customerCreateParam.setRes_custom_code(StringUtil.getRandomString(6).toLowerCase());
			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertEquals(customer_id, null, "新建商户异常测试,res_custom_code非法字符,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
		if (customer_id != null) {
			try {
				boolean result = customerService.deleteCustomter(customer_id);
				Assert.assertEquals(result, true, "删除商户 " + customer_id + "失败");
			} catch (Exception e) {
				logger.error("删除商户遇到错误: ", e);
				Assert.fail("删除商户遇到错误: ", e);
			}
		}
	}
}
