package cn.guanmai.station.invoicing;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 7, 2018 4:42:02 PM 
* @des 修改供应商信息
* @version 1.0 
*/
public class SupplierUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SupplierUpdateTest.class);
	private SupplierDetailBean supplierDetail;
	private SupplierService supplierService;
	private CategoryService categoryService;
	private String supplier_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		supplierService = new SupplierServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<SupplierBean> suppliers = supplierService.searchSupplier(null);
			Assert.assertNotEquals(suppliers, null, "供应商列表获取失败");

			Assert.assertEquals(suppliers.size() > 0, true, "此站点无供应商,无法进行修改供应商操作");
			supplier_id = suppliers.get(0).getSupplier_id();
			supplierDetail = supplierService.getSupplierById(supplier_id);
			Assert.assertNotEquals(supplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}

	}

	@Test
	public void supplierUpdateTestCase01() {
		ReporterCSS.title("测试点: 修改供应商,不修改任何信息,直接保存");
		try {
			boolean result = supplierService.updateSupplier(supplierDetail);
			Assert.assertEquals(result, true, "修改供应商信息失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		}
	}

	@Test
	public void supplierUpdateTestCase02() {
		ReporterCSS.title("测试点: 修改供应商,修改供应商绑定的二级分类");
		JSONArray category2_ids = supplierDetail.getMerchandise();
		try {
			List<Category2Bean> category2List = categoryService.getCategory2List();
			Assert.assertNotEquals(category2List, null, "获取二级分类列表失败,无法进行修改供应商操作");

			Set<String> edit_category2_ids = new HashSet<String>();
			category2List.stream().forEach(c -> edit_category2_ids.add(c.getId()));

			supplierDetail.setMerchandise(JSONArray.parseArray(JSON.toJSONString(edit_category2_ids)));
			boolean result = supplierService.updateSupplier(supplierDetail);
			Assert.assertEquals(result, true, "修改供应商信息失败");

			SupplierDetailBean tempSupplierDetail = supplierService.getSupplierById(supplier_id);
			Assert.assertNotEquals(tempSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");
			Set<String> after_edit_category2_ids = new HashSet<String>();
			for (Object obj : tempSupplierDetail.getMerchandise()) {
				after_edit_category2_ids.add(String.valueOf(obj));
			}

			Assert.assertEquals(after_edit_category2_ids, edit_category2_ids,
					"修改供应商绑定的二级分类,修改后与预期结果不一致,预期:" + edit_category2_ids + ",实际: " + after_edit_category2_ids);
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		} finally {
			try {
				supplierDetail.setMerchandise(category2_ids);
				boolean result = supplierService.updateSupplier(supplierDetail);
				Assert.assertEquals(result, true, "修改供应商信息失败");
			} catch (Exception e) {
				logger.error("修改供应商遇到错误: ", e);
				Assert.fail("修改供应商遇到错误: ", e);
			}
		}
	}

	/**
	 * 修改供应商名称,修改必填字段
	 * 
	 */
	@Test
	public void supplierUpdateTestCase03() {
		try {
			ReporterCSS.title("修改供应商,修改必填字段");
			String new_name = StringUtil.getRandomString(6);
			String new_customer_id = StringUtil.getRandomString(6);
			int new_pay_method = 2;

			SupplierDetailBean eidtSupplierDetail = supplierService.getSupplierById(supplier_id);
			Assert.assertNotEquals(eidtSupplierDetail, null, "获取供应商 " + supplier_id + " 详细信息失败");

			eidtSupplierDetail.setName(new_name);
			eidtSupplierDetail.setCustomer_id(new_customer_id);
			eidtSupplierDetail.setPay_method(new_pay_method);

			boolean result = supplierService.updateSupplier(eidtSupplierDetail);
			Assert.assertEquals(result, true, "修改供应商,修改必填字段,断言成功");

			SupplierDetailBean tmp_supplier = supplierService.getSupplierById(supplier_id);
			result = tmp_supplier.getName().equals(eidtSupplierDetail.getName())
					&& tmp_supplier.getCustomer_id().equals(eidtSupplierDetail.getCustomer_id())
					&& tmp_supplier.getPay_method() == eidtSupplierDetail.getPay_method();
			Assert.assertEquals(result, true, "修改完供应商基本信息后,再次验证,验证失败");
		} catch (Exception e) {
			logger.error("修改供应商遇到错误: ", e);
			Assert.fail("修改供应商遇到错误: ", e);
		} finally {
			try {
				boolean result = supplierService.updateSupplier(supplierDetail);
				Assert.assertEquals(result, true, "修改供应商信息失败");
			} catch (Exception e) {
				logger.error("修改供应商遇到错误: ", e);
				Assert.fail("修改供应商遇到错误: ", e);
			}
		}
	}
}
