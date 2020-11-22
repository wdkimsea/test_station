package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 9, 2018 6:50:50 PM 
* @des 销售规格更新
* @version 1.0 
*/
public class SkuUpdateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SkuUpdateTest.class);
	private InitDataBean initData;
	private SpuBean spu;
	private String sku_id;
	private SkuBean sku;
	private PurchaseSpecBean purchaseSpec;
	private SupplierDetailBean supplier;
	private SalemenuBean salemenu;
	private CategoryService categoryService;

	@BeforeClass
	public void beforeTest() {
		try {
			Map<String, String> headers = getStationCookie();
			initData = getInitData();
			categoryService = new CategoryServiceImpl(headers);

			spu = initData.getSpu();
			purchaseSpec = initData.getPurchaseSpec();
			supplier = initData.getSupplier();
			salemenu = initData.getSalemenu();

			// 填写SKU相关属性值,创建SKU必填
			sku = new SkuBean();
			sku.setSpu_id(spu.getId());
			sku.setOuter_id("");
			BigDecimal std_sale_price = new BigDecimal("400");
			sku.setStd_sale_price(std_sale_price);
			sku.setPartframe(1);
			sku.setStd_unit_name(spu.getStd_unit_name());
			sku.setSlitting(1);
			sku.setSale_num_least(new BigDecimal("1"));
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("1"));
			sku.setSale_unit_name(spu.getStd_unit_name());
			sku.setDesc("SkuCreateTestCase01");
			sku.setSupplier_id(supplier.getId());
			sku.setIs_price_timing(0);
			sku.setIs_weigh(1);
			sku.setPurchase_spec_id(purchaseSpec.getId());
			sku.setAttrition_rate(BigDecimal.ZERO);
			sku.setStock_type(1);
			sku.setName(spu.getName() + "|" + spu.getStd_unit_name());
			sku.setSalemenu_id(salemenu.getId());
			sku.setState(1);
			sku.setSale_price(std_sale_price.multiply(sku.getSale_ratio()));
			sku.setRemark_type(7);

			sku_id = categoryService.createSaleSku(sku);
			Assert.assertEquals(sku_id != null, true, "新建销售SKU失败");

			sku.setId(sku_id);
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	/**
	 * 修改SKU,修改必填值
	 * 
	 */
	@Test
	public void skuUpdateTestCase01() {
		ReporterCSS.title("测试点: 修改SKU,修改必填项");
		try {
			sku.setName(StringUtil.getRandomString(6));
			sku.setStd_sale_price(new BigDecimal("6"));
			sku.setSale_num_least(new BigDecimal("2"));
			sku.setSalemenu_id(null);
			sku.setStocks("-99999");
			sku.setSale_ratio(new BigDecimal("5"));
			sku.setSale_unit_name("包");
			sku.setIs_price_timing(1);
			sku.setIs_weigh(0);
			sku.setAttrition_rate(new BigDecimal("1"));
			sku.setStock_type(3);
			sku.setState(0);
			sku.setSale_price(sku.getStd_sale_price().multiply(sku.getSale_ratio()));

			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改SKU,修改必填项,断言成功");

			SkuBean tmp_sku = categoryService.getSaleSkuById(spu.getId(), sku_id);
			Assert.assertEquals(tmp_sku != null, true, "获取修改的销售SKU详细信息失败");

			Reporter.log("验证修改SKU输入信息与修改后的SKU信息是否一致");
			String msg = null;
			if (!tmp_sku.getName().equals(sku.getName())) {
				msg = String.format("SKU名称与预期不一致,预期: %s,实际: %s", sku.getName(), tmp_sku.getName());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (!tmp_sku.getSalemenu_id().equals(salemenu.getId())) {
				msg = String.format("SKU所处报价单与预期不一致,预期: %s,实际: %s", salemenu.getId(), tmp_sku.getSalemenu_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getIs_price_timing() != sku.getIs_price_timing()) {
				msg = String.format("SKU是否是时价商品与预期不一致,预期: 非时价商品,实际: 时价商品");
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getIs_weigh() != sku.getIs_weigh()) {
				msg = String.format("SKU是否是称重商品与预期不一致,预期: 称重,实际:不称重");
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getState() != sku.getState()) {
				msg = String.format("SKU状态值与预期不一致,预期: %s,实际: %s", sku.getState(), tmp_sku.getState());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getSale_price().compareTo(sku.getSale_price()) != 0) {
				msg = String.format("SKU销售价格与预期不一致,预期: %s,实际: %s", sku.getSale_price(), tmp_sku.getSale_price());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getSale_num_least().compareTo(sku.getSale_num_least()) != 0) {
				msg = String.format("SKU最小下单数与预期不一致,预期: %s,实际: %s", sku.getSale_num_least(),
						tmp_sku.getSale_num_least());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getSale_ratio().compareTo(sku.getSale_ratio()) != 0) {
				msg = String.format("SKU销售单位与基本单位转换率与预期不一致,预期: %s,实际: %s", sku.getSale_ratio(),
						tmp_sku.getSale_ratio());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (!tmp_sku.getSupplier_id().equals(sku.getSupplier_id())) {
				msg = String.format("SKU绑定的供应商ID与预期不一致,预期: %s,实际: %s", sku.getSupplier_id(), tmp_sku.getSupplier_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (!tmp_sku.getPurchase_spec_id().equals(sku.getPurchase_spec_id())) {
				msg = String.format("SKU绑定的采购规格ID与预期不一致,预期: %s,实际: %s", sku.getPurchase_spec_id(),
						tmp_sku.getPurchase_spec_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getAttrition_rate().compareTo(sku.getAttrition_rate()) != 0) {
				msg = String.format("SKU损耗率与预期不一致,预期: %s,实际: %s", sku.getAttrition_rate(), tmp_sku.getAttrition_rate());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getStock_type() != sku.getStock_type()) {
				msg = String.format("SKU库存模式值与预期不一致,预期: %s,实际: %s", sku.getStock_type(), tmp_sku.getStock_type());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "验证输入信息与修改后的SKU " + sku_id + "基本信息对比,存在与预想不一致的字段值");

		} catch (Exception e) {
			logger.error("修改销售SKU遇到错误: ", e);
			Assert.fail("修改销售SKU遇到错误: ", e);
		}

	}

	/**
	 * 修改SKU,非必填属性也赋值
	 * 
	 */
	@Test
	public void skuUpdateTestCase02() {
		ReporterCSS.title("测试点:新建SKU,非必填属性也赋值");
		try {
			sku.setOuter_id("L" + StringUtil.getRandomString(5).toUpperCase());
			sku.setDesc(StringUtil.getRandomString(6).toUpperCase());

			boolean result = categoryService.updateSaleSku(sku);
			Assert.assertEquals(result, true, "修改SKU,修改必填项,断言成功");

			SkuBean tmp_sku = categoryService.getSaleSkuById(spu.getId(), sku_id);
			Assert.assertEquals(tmp_sku != null, true, "获取修改的销售SKU详细信息失败");

			result = tmp_sku.getDesc().equals(sku.getDesc()) && tmp_sku.getOuter_id().equals(sku.getOuter_id());
			Assert.assertEquals(result, true, "创建销售SKU后,再验证其基本信息,验证失败");
		} catch (Exception e) {
			logger.error("修改销售SKU遇到错误: ", e);
			Assert.fail("修改销售SKU遇到错误: ", e);
		}
	}

	@AfterClass
	public void afterTest() {
		if (sku_id != null) {
			try {
				boolean result = categoryService.deleteSaleSku(sku_id);
				Assert.assertEquals(result, true, "删除销售SKU失败");
			} catch (Exception e) {
				logger.error("删除销售SKU遇到错误: ", e);
				Assert.fail("删除销售SKU遇到错误: ", e);
			}

		}
	}

}
