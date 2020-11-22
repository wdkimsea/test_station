package cn.guanmai.station.category;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.BatchSkuDetail;
import cn.guanmai.station.bean.category.MerchandiseTreeBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SkuSuppliersBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.param.BatchSkuCreateParam;
import cn.guanmai.station.bean.invoicing.SupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 9, 2018 4:14:23 PM 
* @des 新建销售SKU测试
* @version 1.0 
*/
public class SkuCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SkuCreateTest.class);
	private InitDataBean initData;
	private SpuBean spu;
	private String sku_id;
	private SkuBean sku;
	private PurchaseSpecBean purchaseSpec;
	private SupplierDetailBean supplier;
	private SalemenuBean salemenu;

	private CategoryService categoryService;
	private SalemenuService salemenuService;
	private SupplierService supplierService;

	@BeforeClass
	public void beforeTest() {
		try {
			Map<String, String> headers = getStationCookie();
			initData = getInitData();
			categoryService = new CategoryServiceImpl(headers);
			salemenuService = new SalemenuServiceImpl(headers);
			supplierService = new SupplierServiceImpl(headers);

			spu = initData.getSpu();
			purchaseSpec = initData.getPurchaseSpec();
			supplier = initData.getSupplier();
			salemenu = initData.getSalemenu();

		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		sku_id = null;

		// 填写SKU相关属性值,创建SKU必填
		sku = new SkuBean();
		sku.setSpu_id(spu.getId());
		sku.setOuter_id("");
		BigDecimal std_sale_price = new BigDecimal("4");
		sku.setStd_sale_price(std_sale_price);
		sku.setPartframe(1);
		sku.setStd_unit_name(spu.getStd_unit_name());
		sku.setSlitting(1);
		sku.setSale_num_least(new BigDecimal("1"));
		sku.setStocks("-99999");
		sku.setSale_ratio(new BigDecimal("1"));
		sku.setSale_unit_name(spu.getStd_unit_name());
		sku.setDesc("beforeMethod");
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
	}

	/**
	 * 新建SKU测试
	 * 
	 */
	@Test
	public void skuCreateTestCase01() {
		ReporterCSS.title("测试点: 新建SKU,只输入必填项");
		try {
			sku_id = categoryService.createSaleSku(sku);
			Assert.assertEquals(sku_id != null, true, "新建销售SKU失败");

			SkuBean tmp_sku = categoryService.getSaleSkuById(spu.getId(), sku_id);
			Assert.assertEquals(tmp_sku != null, true, "获取新建的销售SKU详细信息失败");

			boolean result = true;
			Reporter.log("验证创建SKU输入信息与创建出来的SKU信息是否一致");
			String msg = null;
			if (!tmp_sku.getName().equals(sku.getName())) {
				msg = String.format("SKU名称与预期不一致,预期: %s,实际: %s", sku.getName(), tmp_sku.getName());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (!tmp_sku.getSalemenu_id().equals(sku.getSalemenu_id())) {
				msg = String.format("SKU所处报价单与预期不一致,预期: %s,实际: %s", sku.getSalemenu_id(), tmp_sku.getSalemenu_id());
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getIs_price_timing() != 0) {
				msg = String.format("SKU是否是时价商品与预期不一致,预期: 非时价商品,实际: 时价商品");
				ReporterCSS.warn(msg);
				logger.info(msg);
				result = false;
			}
			if (tmp_sku.getIs_weigh() != 1) {
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

			Assert.assertEquals(result, true, "验证输入信息与新建的SKU " + sku_id + "基本信息对比,存在与预想不一致的字段值");

		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}

	}

	/**
	 * 新建SKU,非必填属性也赋值
	 * 
	 */
	@Test
	public void skuCreateTestCase02() {
		ReporterCSS.title("测试点: 新建SKU,非必填属性也赋值");
		try {
			sku.setOuter_id("L" + StringUtil.getRandomString(5).toUpperCase());
			sku.setDesc(StringUtil.getRandomString(6));
			sku_id = categoryService.createSaleSku(sku);
			Assert.assertEquals(sku_id != null, true, "新建销售SKU失败");

			SkuBean tmp_sku = categoryService.getSaleSkuById(spu.getId(), sku_id);
			boolean result = tmp_sku.getDesc().equals(sku.getDesc()) && tmp_sku.getOuter_id().equals(sku.getOuter_id());
			Assert.assertEquals(result, true, "创建销售SKU后,再验证其基本信息,验证失败");
		} catch (Exception e) {
			logger.error("新建销售SKU遇到错误: ", e);
			Assert.fail("新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void skuCreateTestCase03() {
		ReporterCSS.title("测试点: 批量新建销售SKU");
		try {
			List<SalemenuBean> salemenus = salemenuService.getSalemenuList(4, 1);
			Assert.assertNotEquals(salemenus, null, "批量新建销售SKU拉取站点所有激活的销售报价单失败");

			List<MerchandiseTreeBean> merchandiseTrees = categoryService.getMerchandiseTree();
			Assert.assertNotEquals(merchandiseTrees, null, "获取站点商品分类树失败");

			List<String> spu_ids = Arrays.asList(spu.getId());
			List<BatchSkuDetail> batchSkuDetails = categoryService.getBatchSpuDetails(spu_ids);
			Assert.assertNotEquals(batchSkuDetails, null, "批量新建SKU选取SPU后展示详细信息失败");

			BatchSkuDetail batchSkuDetail = batchSkuDetails.get(0);
			String salemenu_id = salemenu.getId();
			BatchSkuCreateParam createParam = new BatchSkuCreateParam();
			createParam.setSpu_id(spu.getId());
			createParam.setSku_name(batchSkuDetail.getSku_name());
			createParam.setSale_price(new BigDecimal("2"));
			createParam.setRatio(batchSkuDetail.getRatio());
			createParam.setSale_unit_name(batchSkuDetail.getSale_unit_name());
			createParam.setSale_num_least(batchSkuDetail.getSale_num_least());
			createParam.setIs_weigh(1);
			createParam.setState(1);
			createParam.setIs_price_timing(0);
			createParam.setStock_type(1);
			createParam.setStock(new BigDecimal("-99999"));
			createParam.setSupplier_id(batchSkuDetail.getSupplier_id());
			createParam.setPur_spec_id(batchSkuDetail.getPur_spec_id());

			List<BatchSkuCreateParam> params = Arrays.asList(createParam);

			boolean result = categoryService.batchCreateSaleSku(salemenu_id, params);
			Assert.assertEquals(result, true, "批量新建销售SKU失败");

		} catch (Exception e) {
			logger.error("批量新建销售SKU遇到错误: ", e);
			Assert.fail("批量新建销售SKU遇到错误: ", e);
		}
	}

	@Test
	public void skuCreateTestCase04() {
		ReporterCSS.title("测试点: 新建、修改SKU拉取对应的供应商列表");
		try {
			List<String> supplier_ids = categoryService.getSkuSupplierList(spu.getSpu_id());
			Assert.assertNotEquals(supplier_ids, null, "新建、修改SKU拉取对应的供应商列表接口调用失败");
		} catch (Exception e) {
			logger.error("新建、修改SKU拉取对应的供应商列表遇到错误: ", e);
			Assert.fail("新建、修改SKU拉取对应的供应商列表遇到错误: ", e);
		}
	}

	@Test
	public void skuCreateTestCase05() {
		ReporterCSS.title("测试点: 新建、修改SKU拉取对应的供应商列表[新接口]");
		try {
			SkuSuppliersBean skuSuppliers = categoryService.getSkuSupplierListNew(spu.getSpu_id());
			Assert.assertNotEquals(skuSuppliers, null, "新建、修改SKU拉取对应的供应商列表接口[新接口]调用失败");

			List<SupplierBean> suppliers = supplierService.searchSupplier(null);
			Assert.assertNotEquals(suppliers, null, "获取供应商列表信息失败");

			String category2_id = initData.getCategory2().getId();

			List<String> recommend_suppliers = new ArrayList<String>();
			List<String> other_suppliers = new ArrayList<String>();
			for (SupplierBean supplier : suppliers) {
				if (supplier.getMerchandises().contains(category2_id)) {
					recommend_suppliers.add(supplier.getSupplier_id());
				} else {
					other_suppliers.add(supplier.getSupplier_id());
				}
			}

			List<String> actualRecommendSupplier = skuSuppliers.getRecommend_suppliers().stream()
					.filter(s -> s.getUpstream() == 0).map(s -> s.getId()).collect(Collectors.toList());

			List<String> actualOtherSupplier = skuSuppliers.getOther_suppliers().stream()
					.filter(s -> s.getUpstream() == 0).map(s -> s.getId()).collect(Collectors.toList());

			boolean result = true;
			String msg = null;
			if (actualRecommendSupplier.size() != recommend_suppliers.size()
					|| !actualRecommendSupplier.containsAll(recommend_suppliers)) {
				msg = String.format("商品%s推荐供应商列表与预期不一致,预期:%s,实际:%s", spu.getSpu_id(), recommend_suppliers,
						actualRecommendSupplier);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (actualOtherSupplier.size() != other_suppliers.size()
					|| !actualOtherSupplier.containsAll(other_suppliers)) {
				msg = String.format("商品%s其他供应商列表与预期不一致,预期:%s,实际:%s", spu.getSpu_id(), other_suppliers,
						actualOtherSupplier);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商品" + spu.getId() + "获取的供应商信息与预期不一致");
		} catch (Exception e) {
			logger.error("新建、修改SKU拉取对应的供应商列表遇到错误: ", e);
			Assert.fail("新建、修改SKU拉取对应的供应商列表遇到错误: ", e);
		}
	}

	@AfterMethod
	public void afterMethod() {
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
