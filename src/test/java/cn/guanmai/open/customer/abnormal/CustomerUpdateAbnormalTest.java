package cn.guanmai.open.customer.abnormal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.manage.impl.customermange.MgCustomerServiceImpl;
import cn.guanmai.manage.interfaces.custommange.MgCustomerService;
import cn.guanmai.open.bean.customer.OpenCustomerAreaBean;
import cn.guanmai.open.bean.customer.param.OpenCustomerCreateParam;
import cn.guanmai.open.bean.customer.param.OpenCustomerUpdateParam;
import cn.guanmai.open.bean.product.OpenSalemenuBean;
import cn.guanmai.open.customer.CustomerTest;
import cn.guanmai.open.impl.customer.OpenCustomerServiceImpl;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.interfaces.customer.OpenCustomerService;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 17, 2019 5:29:18 PM 
* @des 开放平台商户修改异常测试
* @version 1.0 
*/
public class CustomerUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(CustomerTest.class);
	private OpenCustomerService openCustomerService;
	private OpenSalemenuService openSalemenuService;
	private List<String> salemenu_id_list;
	private String district_code;
	private String area_level1_code;
	private String area_level2_code;
	private String customer_name;
	private String customer_id;
	private OpenCustomerCreateParam customerCreateParam;
	private OpenCustomerUpdateParam customerUpdateParam;
	private MgCustomerService customerService;

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

			customer_id = openCustomerService.createCustomer(customerCreateParam);
			Assert.assertNotEquals(customer_id, null, "新建商户失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		customerUpdateParam = new OpenCustomerUpdateParam();
		customerUpdateParam.setCustomer_id(customer_id);

	}

	@Test
	public void customerUpdateAbnormalTestCase01() {
		Reporter.log("测试点: 修改商户异常测试,customer_id传入为空值,断言失败");
		try {
			customerUpdateParam.setCustomer_id("");
			customerUpdateParam.setCustomer_address("JK");
			boolean result = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(result, false, "修改商户异常测试,customer_id传入为空值,断言失败");
		} catch (Exception e) {
			logger.error("获取商户地理标签遇到错误: ", e);
			Assert.fail("获取商户地理标签遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase02() {
		Reporter.log("测试点: 修改商户异常测试,customer_id传入为别的站点的商户ID,断言失败");
		try {
			customerUpdateParam.setCustomer_id("S062175");
			customerUpdateParam.setCustomer_address("JK");
			boolean result = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(result, false, "修改商户异常测试,customer_id传入为别的站点的商户ID,断言失败");
		} catch (Exception e) {
			logger.error("获取商户地理标签遇到错误: ", e);
			Assert.fail("获取商户地理标签遇到错误: ", e);
		}
	}

//	//////@Test
//	public void customerUpdateAbnormalTestCase03() {
//		Reporter.log("测试点: 修改商户异常测试,customer_id传入为别的站点的商户ID,断言失败");
//		try {
//			customerUpdateParam.setCustomer_address("JK");
//			boolean result = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(result, false, "修改商户异常测试,customer_id传入为别的站点的商户ID,断言失败");
//		} catch (Exception e) {
//			logger.error("获取商户地理标签遇到错误: ", e);
//			Assert.fail("获取商户地理标签遇到错误: ", e);
//		}
//	}

	@Test
	public void customerUpdateAbnormalTestCase04() {
		try {
			Reporter.log("测试点: 修改商户异常测试,district_code为空,断言失败");
			customerUpdateParam.setDistrict_code("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,district_code为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase05() {
		try {
			Reporter.log("测试点: 修改商户异常测试,district_code为没绑定的地区编码,断言失败");
			customerUpdateParam.setDistrict_code("450100");
			customerUpdateParam.setArea_level1("100200000000");
			customerUpdateParam.setArea_level2("100200200000");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,district_code为没绑定的地区编码,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

//	////@Test
//	public void customerUpdateAbnormalTestCase06() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,area_level1输入为空,断言失败");
//			customerCreateParam.setArea_level1("");
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,district_code为没绑定的地区编码,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}
//
//	////@Test
//	public void customerUpdateAbnormalTestCase07() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,area_level1输入与district_code不匹配的值,断言失败");
//			customerCreateParam.setArea_level1("100200000000");
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,district_code为没绑定的地区编码,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

//	@Test
//	public void customerUpdateAbnormalTestCase07() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,area_level1输入与district_code不匹配的值,断言失败");
//			customerCreateParam.setArea_level1("100200000000");
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,district_code为没绑定的地区编码,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

	@Test
	public void customerUpdateAbnormalTestCase08() {
		try {
			Reporter.log("测试点: 修改商户异常测试,area_level2输入为空,断言失败");
			customerUpdateParam.setArea_level2("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,area_level2输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase09() {
		try {
			Reporter.log("测试点: 修改商户异常测试,area_level2输入为与area_level1不匹配的值,断言失败");
			customerUpdateParam.setArea_level2("100200200000");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,area_level2输入为与area_level1不匹配的值,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase10() {
		try {
			Reporter.log("测试点: 修改商户异常测试,password输入为空,断言失败");
			customerUpdateParam.setPassword("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,password输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase11() {
		try {
			Reporter.log("测试点: 修改商户异常测试,password输入为空格,断言失败");
			customerUpdateParam.setPassword("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,password输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase12() {
		try {
			Reporter.log("测试点: 修改商户异常测试,password输入过长字符(大于32个字符),断言失败");
			customerUpdateParam.setPassword(StringUtil.getRandomString(33));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,password输入过长字符(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase13() {
		try {
			Reporter.log("测试点: 修改商户异常测试,payer_name输入为空,断言失败");
			customerUpdateParam.setPayer_name("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,payer_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase14() {
		try {
			Reporter.log("测试点: 修改商户异常测试,payer_name输入为空格,断言失败");
			customerUpdateParam.setPayer_name("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,payer_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase15() {
		try {
			Reporter.log("测试点: 修改商户异常测试,payer_name输入过长字符串(大于32个字符),断言失败");
			customerUpdateParam.setPayer_name(StringUtil.getRandomString(33));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,payer_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase16() {
		try {
			Reporter.log("测试点: 修改商户异常测试,payer_telephone输入为空,断言失败");
			customerUpdateParam.setPayer_telephone("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,payer_telephone输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase17() {
		try {
			Reporter.log("测试点: 修改商户异常测试,payer_telephone输入为空格,断言失败");
			customerUpdateParam.setPayer_telephone("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,payer_telephone输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

//	////@Test
//	public void customerUpdateAbnormalTestCase18() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,payer_telephone输入非数字,断言失败");
//			customerCreateParam.setPayer_name("12D" + StringUtil.getRandomString(8));
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,payer_telephone输入非数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}
//
//	////@Test
//	public void customerUpdateAbnormalTestCase19() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,payer_telephone输入非11位数字,断言失败");
//			customerCreateParam.setPayer_name("12" + StringUtil.getRandomNumber(8));
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,payer_telephone输入非11位数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}
//
//	@Test
//	public void customerUpdateAbnormalTestCase19() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,payer_telephone输入非11位数字,断言失败");
//			customerCreateParam.setPayer_name("12" + StringUtil.getRandomNumber(8));
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,payer_telephone输入非11位数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

	@Test
	public void customerUpdateAbnormalTestCase20() {
		try {
			Reporter.log("测试点: 修改商户异常测试,receiver_name输入为空,断言失败");
			customerUpdateParam.setReceiver_name("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,receiver_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase21() {
		try {
			Reporter.log("测试点: 修改商户异常测试,receiver_name输入为空格,断言失败");
			customerUpdateParam.setReceiver_name("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,receiver_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase22() {
		try {
			Reporter.log("测试点: 修改商户异常测试,receiver_name输入过长字符串(大于32个字符),断言失败");
			customerUpdateParam.setReceiver_name(StringUtil.getRandomString(33));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,receiver_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase23() {
		try {
			Reporter.log("测试点: 修改商户异常测试,receiver_telephone输入为空,断言失败");
			customerUpdateParam.setReceiver_telephone("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,receiver_telephone输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase24() {
		try {
			Reporter.log("测试点: 修改商户异常测试,receiver_telephone输入为空格,断言失败");
			customerUpdateParam.setReceiver_telephone("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,receiver_telephone输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

//	////@Test
//	public void customerUpdateAbnormalTestCase25() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,receiver_telephone输入非数字,断言失败");
//			customerUpdateParam.setReceiver_telephone("12D" + StringUtil.getRandomString(8));
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,receiver_telephone输入非数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}
//
//	////@Test
//	public void customerUpdateAbnormalTestCase26() {
//		try {
//			Reporter.log("测试点: 修改商户异常测试,receiver_telephone输入非11位数字,断言失败");
//			customerUpdateParam.setReceiver_telephone("12" + StringUtil.getRandomNumber(8));
//			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
//			Assert.assertEquals(reuslt, false,"修改商户异常测试,receiver_telephone输入非11位数字,断言失败");
//		} catch (Exception e) {
//			logger.error("新建商户遇到错误: ", e);
//			Assert.fail("新建商户遇到错误: ", e);
//		}
//	}

	@Test
	public void customerUpdateAbnormalTestCase27() {
		try {
			Reporter.log("测试点: 修改商户异常测试,customer_address输入为空,断言失败");
			customerUpdateParam.setCustomer_address("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,customer_address输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase28() {
		try {
			Reporter.log("测试点: 修改商户异常测试,customer_address输入为空格,断言失败");
			customerUpdateParam.setCustomer_address("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,customer_address输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase29() {
		try {
			Reporter.log("测试点: 修改商户异常测试,customer_name输入为空,断言失败");
			customerUpdateParam.setCustomer_name("");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,customer_name输入为空,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase30() {
		try {
			Reporter.log("测试点: 修改商户异常测试,customer_name输入为空格,断言失败");
			customerUpdateParam.setCustomer_name("      ");
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,customer_name输入为空格,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase31() {
		try {
			Reporter.log("测试点: 修改商户异常测试,customer_name输入过长字符串(大于32个字符),断言失败");
			customerUpdateParam.setCustomer_name(StringUtil.getRandomString(33));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,customer_name输入过长字符串(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase32() {
		try {
			Reporter.log("测试点: 修改商户异常测试,salemenu_ids不传值,断言失败");
			customerUpdateParam.setSalemenu_ids(null);
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,salemenu_ids不传值,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase33() {
		try {
			Reporter.log("测试点: 修改商户异常测试,salemenu_ids输入空数组,断言失败");
			customerUpdateParam.setSalemenu_ids(new ArrayList<String>());
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,salemenu_ids输入空数组,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase34() {
		try {
			Reporter.log("测试点: 修改商户异常测试,salemenu_ids输入别的站点的报价单ID,断言失败");
			customerUpdateParam.setSalemenu_ids(Arrays.asList("S13776"));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,salemenu_ids输入别的站点的报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase35() {
		try {
			Reporter.log("测试点: 修改商户异常测试,res_custome_code输入过长字符,断言失败");
			customerUpdateParam.setRes_custom_code(StringUtil.getRandomNumber(11));
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,res_custome_code输入过长字符,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@Test
	public void customerUpdateAbnormalTestCase36() {
		try {
			Reporter.log("测试点: 修改商户异常测试,res_custome_code输入非法字符,断言失败");
			customerUpdateParam.setRes_custom_code(StringUtil.getRandomString(6).toLowerCase());
			boolean reuslt = openCustomerService.updateCustomer(customerUpdateParam);
			Assert.assertEquals(reuslt, false, "修改商户异常测试,res_custome_code输入非法字符,断言失败");
		} catch (Exception e) {
			logger.error("新建商户遇到错误: ", e);
			Assert.fail("新建商户遇到错误: ", e);
		}
	}

	@AfterClass
	public void afterClass() {
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
