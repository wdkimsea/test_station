package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 7, 2018 11:47:50 AM 
* @todo 商品采购规格测试
* @version 1.0 
*/
public class PurchaseSpecUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseSpecCreateTest.class);
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String spu_id;
	private String purchase_spec_id;
	private String tmp_purchase_spec_id;
	private PurchaseSpecBean purchaseSpec;

	private static CategoryService categoryService;

	@BeforeClass
	public void beforeTest() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
		try {
			String name = "蔬菜";
			Category1Bean category = categoryService.getCategory1ByName(name);
			if (category == null) {
				category = new Category1Bean(name, 1);
				category1_id = categoryService.createCategory1(category);
				Assert.assertNotEquals(category1_id, null, "新建一级分类失败");
			} else {
				category1_id = category.getId();

			}
			String category2_name = "叶菜";
			Category2Bean category2 = categoryService.getCategory2ByName(category1_id, "叶菜");
			if (category2 == null) {
				category2 = new Category2Bean(category1_id, category2_name);
				category2_id = categoryService.createCategory2(category2);
				Assert.assertNotEquals(category2_id, null, "新建二级分类失败");
			} else {
				category2_id = category2.getId();
			}

			String pinlei_name = "茼蒿";
			PinleiBean pinlei = categoryService.getPinleiByName(category2_id, pinlei_name);
			if (pinlei == null) {
				pinlei = new PinleiBean(category2_id, pinlei_name);
				pinlei_id = categoryService.createPinlei(pinlei);
				Assert.assertNotEquals(pinlei_id, null, "新建品类分类失败");
			} else {
				pinlei_id = pinlei.getId();
			}

			String spu_name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(spu_name, pinlei_id, "验证采购规格", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败");

			String purchaseSpec_name = StringUtil.getRandomString(6);
			purchaseSpec = new PurchaseSpecBean(purchaseSpec_name, StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), "斤", new BigDecimal(1), category1_id, category2_id,
					pinlei_id, spu_id);
			purchase_spec_id = categoryService.createPurchaseSpec(purchaseSpec);
			Assert.assertNotEquals(purchase_spec_id, null, "新建采购规格失败");
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			purchaseSpec = categoryService.getPurchaseSpecById(purchase_spec_id);
			Assert.assertNotEquals(purchaseSpec, null, "获取商品采购规格失败");
		} catch (Exception e) {
			logger.error("获取采购规格遇到错误: ", e);
			Assert.fail("获取采购规格遇到错误: ", e);
		}

	}

	/**
	 * 修改商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase01() {
		try {
			Reporter.log("修改商品采购规格名称");
			String new_purchaseSpec_name = StringUtil.getRandomString(6);
			purchaseSpec.setName(new_purchaseSpec_name);
			boolean result = categoryService.updatePurchaseSpec(purchaseSpec);
			Assert.assertEquals(result, true, "修改商品采购规格名称,断言成功");

			PurchaseSpecBean tmp_purchaseSpec = categoryService.getPurchaseSpecById(purchase_spec_id);
			result = tmp_purchaseSpec.getName().equals(new_purchaseSpec_name);
			Assert.assertEquals(result, true, "修改后再次验证是否修改成功,验证失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}

	}

	/**
	 * 修改商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase02() {
		try {
			Reporter.log("修改商品采购规格的采购规格");
			purchaseSpec.setRatio(new BigDecimal(5));
			purchaseSpec.setUnit_name("包");
			boolean result = categoryService.updatePurchaseSpec(purchaseSpec);
			Assert.assertEquals(result, true, "修改商品采购规格的采购规格,断言成功");

			PurchaseSpecBean tmp_purchaseSpec = categoryService.getPurchaseSpecById(purchase_spec_id);
			result = tmp_purchaseSpec.getRatio().compareTo(purchaseSpec.getRatio()) == 0
					&& tmp_purchaseSpec.getPurchase_unit().equals(purchaseSpec.getUnit_name());
			Assert.assertEquals(result, true, "修改后再次验证是否修改成功,验证失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}

	}

	/**
	 * 修改商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase03() {
		try {
			Reporter.log("修改商品采购规格的采购规格为已经存在的规格");
			String tmp_name = StringUtil.getRandomString(6);
			BigDecimal tmp_ratio = new BigDecimal(10);
			String tmp_unit_name = "N";
			PurchaseSpecBean tmp_purchaseSpec = new PurchaseSpecBean(tmp_name, StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), tmp_unit_name, tmp_ratio, category1_id, category2_id,
					pinlei_id, spu_id);
			tmp_purchase_spec_id = categoryService.createPurchaseSpec(tmp_purchaseSpec);
			Assert.assertNotEquals(tmp_purchase_spec_id, null, "新建采购规格失败");

			purchaseSpec.setRatio(tmp_ratio);
			purchaseSpec.setUnit_name(tmp_unit_name);
			boolean result = categoryService.updatePurchaseSpec(purchaseSpec);
			Assert.assertEquals(result, false, "修改商品采购规格的采购规格为已经存在的规格,断言失败");
		} catch (Exception e) {
			logger.error("新建采购规格遇到错误: ", e);
			Assert.fail("新建采购规格遇到错误: ", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (tmp_purchase_spec_id != null) {
				boolean result = categoryService.deletePurchaseSpec(tmp_purchase_spec_id);
				Assert.assertEquals(result, true, "删除商品采购规格失败");
			}
		} catch (Exception e) {
			logger.error("删除采购规格遇到错误: ", e);
			Assert.fail("删除采购规格遇到错误: ", e);
		}

	}

	@AfterClass
	public void afterClass() {
		try {
			boolean result = categoryService.deletePurchaseSpec(purchase_spec_id);
			Assert.assertEquals(result, true, "删除商品采购规格失败");

			result = categoryService.deleteSpu(spu_id);
			Assert.assertEquals(result, true, "删除商品SPU失败");
		} catch (Exception e) {
			logger.error("后置处理数据遇到错误: ", e);
			Assert.fail("后置处理数据遇到错误: ", e);
		}

	}

}
