package cn.guanmai.station.invoicing;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 7, 2018 4:42:02 PM 
* @todo TODO
* @version 1.0 
*/
public class SupplierCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierCreateTest.class);
	private JSONArray permissionArray;
	private String id;
	private String tmp_id;
	private List<Category2Bean> category2Array;
	private JSONArray merchandise;

	private SupplierService supplierService;
	private CategoryService categoryService;

	private LoginUserInfoService loginUserInfoService;

	@BeforeClass
	public void initData() {
		try {
			Map<String, String> headers = getStationCookie();
			supplierService = new SupplierServiceImpl(headers);
			categoryService = new CategoryServiceImpl(headers);
			loginUserInfoService = new LoginUserInfoServiceImpl(headers);

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			permissionArray = loginUserInfo.getUser_permission();
			boolean result = permissionArray != null && permissionArray.contains("delete_settle_supplier");
			Assert.assertEquals(result, true, "此站点没有删除供应商的权限,所以测试用例不执行");

			category2Array = categoryService.getCategory2List();
			merchandise = new JSONArray();
			for (Category2Bean category2 : category2Array) {
				merchandise.add(category2.getId());
			}
		} catch (Exception e) {
			logger.error("获取二级分类列表出现错误: ", e);
			Assert.fail("获取二级分类列表出现错误: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		id = null;
		tmp_id = null;
	}

	/**
	 * 创建供应商测试
	 * 
	 */
	@Test
	public void supplierCreateTestCase01() {
		try {
			ReporterCSS.title("新建供应商");
			String customer_id = StringUtil.getRandomString(6);
			String name = StringUtil.getRandomString(6);
			SupplierDetailBean supplier = new SupplierDetailBean(customer_id, name, merchandise, 1);
			id = supplierService.createSupplier(supplier);
			Assert.assertNotEquals(id, null, "新建供应商,断言成功");

			SupplierDetailBean tmp_supplier = supplierService.getSupplierById(id);
			boolean result = tmp_supplier.getName().equals(supplier.getName())
					&& tmp_supplier.getCustomer_id().equals(supplier.getCustomer_id())
					&& tmp_supplier.getMerchandise().removeAll(supplier.getMerchandise())
					&& tmp_supplier.getPay_method() == supplier.getPay_method();
			Assert.assertEquals(result, true, "新建供应商成功后,验证基本信息,验证失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}

	}

	/**
	 * 创建供应商,使用相同的customer_id
	 * 
	 */
	@Test
	public void supplierCreateTestCase02() {
		try {
			ReporterCSS.title("创建供应商,使用相同的customer_id");
			String customer_id = StringUtil.getRandomString(6);
			String name = StringUtil.getRandomString(6);
			SupplierDetailBean supplier = new SupplierDetailBean(customer_id, name, merchandise, 1);
			id = supplierService.createSupplier(supplier);
			Assert.assertNotEquals(id, null, "新建供应商,断言成功");

			tmp_id = supplierService.createSupplier(supplier);
			Assert.assertEquals(tmp_id, null, "创建供应商,自定义编号使用已经存在的自定义编号,断言失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}

	}

	/**
	 * 创建供应商,验证非必填字段
	 * 
	 */
	@Test
	public void supplierCreateTestCase03() {
		try {
			ReporterCSS.title("创建供应商,非必填字段全部赋值");
			String customer_id = StringUtil.getRandomString(6);
			String name = StringUtil.getRandomString(6);
			SupplierDetailBean supplier = new SupplierDetailBean(customer_id, name, merchandise, 1);
			// 下面是非必填字段
			supplier.setPhone("12536545898");
			supplier.setCompany_name("这个是公司名称");
			supplier.setCompany_address("这个是公司地址");
			supplier.setFinance_manager("财务联系人");
			supplier.setFinance_manager_phone("12365485758");
			supplier.setAccount_name("开户名");
			supplier.setBank("开户银行");
			supplier.setCard_no("银行卡号");
			supplier.setBusiness_licence("营业执照号");

			id = supplierService.createSupplier(supplier);
			Assert.assertNotEquals(id, null, "新建供应商,断言成功");

			SupplierDetailBean tmp_supplier = supplierService.getSupplierById(id);
			boolean result = tmp_supplier.getPhone().equals(supplier.getPhone())
					&& tmp_supplier.getCompany_name().equals(supplier.getCompany_name())
					&& tmp_supplier.getCompany_address().equals(supplier.getCompany_address())
					&& tmp_supplier.getFinance_manager().equals(supplier.getFinance_manager())
					&& tmp_supplier.getFinance_manager_phone().equals(supplier.getFinance_manager_phone())
					&& tmp_supplier.getAccount_name().equals(supplier.getAccount_name())
					&& tmp_supplier.getBank().equals(supplier.getBank())
					&& tmp_supplier.getCard_no().equals(supplier.getCard_no())
					&& tmp_supplier.getBusiness_licence().equals(supplier.getBusiness_licence());
			Assert.assertEquals(result, true, "供应商创建成功后,验证非必填字段,验证失败");
		} catch (Exception e) {
			logger.error("新建供应商遇到错误: ", e);
			Assert.fail("新建供应商遇到错误: ", e);
		}

	}

	@AfterMethod
	public void tearDown() {
		try {
			if (id != null) {
				boolean result = supplierService.deleteSupplier(id);
				Assert.assertEquals(result, true, "删除供应商,断言成功");
			}

			if (tmp_id != null) {
				boolean result = supplierService.deleteSupplier(tmp_id);
				Assert.assertEquals(result, true, "删除供应商,断言成功");
			}
		} catch (Exception e) {
			logger.error("删除供应商遇到错误: ", e);
			Assert.fail("删除供应商遇到错误: ", e);
		}

	}

}
