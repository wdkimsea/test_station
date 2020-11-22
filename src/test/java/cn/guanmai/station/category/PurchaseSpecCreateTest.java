package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 7, 2018 11:47:50 AM 
* @todo 商品采购规格测试
* @version 1.0 
*/
public class PurchaseSpecCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseSpecUpdateTest.class);
	private String category1_id;
	private String category2_id;
	private String pinlei_id;
	private String spu_id;
	private String purchase_spec_id;
	private String tmp_purchase_spec_id;

	private CategoryService categoryService;

	@BeforeClass
	public void beforeTest() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
		try {
			InitDataBean initData = getInitData();
			category1_id = initData.getCategory1().getId();
			category2_id = initData.getCategory2().getId();
			pinlei_id = initData.getPinlei().getId();

			String spu_name = StringUtil.getRandomString(6);
			SpuBean spu = new SpuBean(spu_name, pinlei_id, "验证采购规格", new JSONArray(), 0, "斤", new JSONArray(), 2, 1);
			spu_id = categoryService.createSpu(spu);
			Assert.assertNotEquals(spu_id, null, "新建SPU失败, " + spu_name);
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		purchase_spec_id = null;
		tmp_purchase_spec_id = null;
	}

	/**
	 * 新建商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase01() {
		try {
			ReporterCSS.title("测试点: 新建商品采购规格");
			String purchaseSpec_name = StringUtil.getRandomString(6);
			PurchaseSpecBean purchaseSpec = new PurchaseSpecBean(purchaseSpec_name, StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), "斤", new BigDecimal(1), category1_id, category2_id,
					pinlei_id, spu_id);
			purchase_spec_id = categoryService.createPurchaseSpec(purchaseSpec);
			Assert.assertNotEquals(purchase_spec_id, null, "新建采购规格失败");
		} catch (Exception e) {
			logger.error("新建商品采购规格遇到错误: ", e);
			Assert.fail("新建商品采购规格遇到错误: ", e);
		}

	}

	/**
	 * 新建商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase02() {
		try {
			ReporterCSS.title("测试点: 为同一个SPU创建两个unit_name相同的采购规格");
			String purchaseSpec_name = StringUtil.getRandomString(6);
			PurchaseSpecBean purchaseSpec = new PurchaseSpecBean(purchaseSpec_name, StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), "斤", new BigDecimal(1), category1_id, category2_id,
					pinlei_id, spu_id);
			purchase_spec_id = categoryService.createPurchaseSpec(purchaseSpec);
			Assert.assertNotEquals(purchase_spec_id, null, "新建商品采购规格,断言成功");

			tmp_purchase_spec_id = categoryService.createPurchaseSpec(purchaseSpec);
			Assert.assertEquals(tmp_purchase_spec_id, null, "为同一个SPU创建两个unit_name相同的采购规格,断言第二个创建失败");
		} catch (Exception e) {
			logger.error("新建商品采购规格遇到错误: ", e);
			Assert.fail("新建商品采购规格遇到错误: ", e);
		}

	}

	/**
	 * 新建商品采购规格
	 * 
	 */
	@Test
	public void purchaseSpecCreateTestCase03() {
		try {
			ReporterCSS.title("测试点: 新建采购单位为非基本单位的采购规格");
			String purchaseSpec_name = StringUtil.getRandomString(6);
			PurchaseSpecBean purchaseSpec = new PurchaseSpecBean(purchaseSpec_name, StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), "包", new BigDecimal(5), category1_id, category2_id,
					pinlei_id, spu_id);
			purchase_spec_id = categoryService.createPurchaseSpec(purchaseSpec);
			Assert.assertNotEquals(purchase_spec_id, null, "新建采购单位为非基本单位的采购规格,断言成功");
		} catch (Exception e) {
			logger.error("新建商品采购规格遇到错误: ", e);
			Assert.fail("新建商品采购规格遇到错误: ", e);
		}

	}

	@AfterMethod
	public void afterMethod() {
		try {
			if (purchase_spec_id != null) {
				boolean result = categoryService.deletePurchaseSpec(purchase_spec_id);
				Assert.assertEquals(result, true, "删除商品采购规格" + purchase_spec_id + "失败");
			}

			if (tmp_purchase_spec_id != null) {
				boolean result = categoryService.deletePurchaseSpec(tmp_purchase_spec_id);
				Assert.assertEquals(result, true, "删除商品采购规格" + tmp_purchase_spec_id + "失败");
			}
		} catch (Exception e) {
			logger.error("删除商品采购规格遇到错误: ", e);
			Assert.fail("删除商品采购规格遇到错误: ", e);
		}

	}

	@AfterClass
	public void afterClass() {
		try {
			boolean result = categoryService.deleteSpu(spu_id);
			Assert.assertEquals(result, true, "删除商品SPU " + spu_id + "失败");
		} catch (Exception e) {
			logger.error("后置处理数据遇到错误: ", e);
			Assert.fail("后置处理数据遇到错误: ", e);
		}

	}

}
