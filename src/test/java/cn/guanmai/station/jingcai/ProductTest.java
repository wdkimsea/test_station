package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.jingcai.IngredientBean;
import cn.guanmai.station.bean.jingcai.LabelBean;
import cn.guanmai.station.bean.jingcai.ProductBean;
import cn.guanmai.station.bean.jingcai.TechnicBean;
import cn.guanmai.station.bean.jingcai.TechnicFlowBean;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowCreateParam;
import cn.guanmai.station.bean.jingcai.param.TechnicFlowUpdateParam;
import cn.guanmai.station.impl.jingcai.LabelServiceImpl;
import cn.guanmai.station.impl.jingcai.ProductServiceImpl;
import cn.guanmai.station.impl.jingcai.TechnicServiceImpl;
import cn.guanmai.station.interfaces.jingcai.LabelService;
import cn.guanmai.station.interfaces.jingcai.ProductService;
import cn.guanmai.station.interfaces.jingcai.TechnicService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/**
 * @author: liming
 * @Date: 2020年4月28日 下午3:59:12
 * @description:
 * @version: 1.0
 */

public class ProductTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ProductTest.class);
	private TechnicService technicService;
	private ProductService productService;
	private LabelService labelService;
	private InitDataBean initData;
	private SpuBean spu;
	private PurchaseSpecBean purchaseSpec;
	private ProductBean product;
	private String salemenu_id;
	private String supplier_id;
	private String sku_id;
	private BigDecimal label_id;

	@BeforeClass
	public void initData() {
		Map<String, String> cookie = getStationCookie();
		technicService = new TechnicServiceImpl(cookie);
		productService = new ProductServiceImpl(cookie);
		labelService = new LabelServiceImpl(cookie);
		initData = getInitData();
		purchaseSpec = initData.getPurchaseSpec();
		spu = initData.getSpu();
		salemenu_id = initData.getSalemenu().getId();
		supplier_id = initData.getSupplier().getId();
		try {
			String label_name = "净菜";
			LabelBean label = labelService.getLabelByName(label_name);
			if (label == null) {
				label_id = labelService.createLabel(label_name);
				Assert.assertNotEquals(label_id, null, "新建商品加工标签失败");
			}
			technicService.initTechnic();
		} catch (Exception e) {
			logger.error("初始化工艺信息失败: ", e);
			Assert.fail("初始化工艺信息失败: ", e);
		}

	}

	@BeforeMethod
	public void beforeMethod() {
		sku_id = null;
		product = new ProductBean();
		product.setName(spu.getName() + "(净)");
		product.setSpu_id(spu.getSpu_id());
		product.setSalemenu_id(salemenu_id);
		product.setDesc("这个是描述");
		BigDecimal price = NumberUtil.getRandomNumber(4, 10, 2);
		product.setStd_sale_price_forsale(price);
		product.setSale_price(price);
		product.setSale_unit_name(spu.getStd_unit_name());
		product.setStd_unit_name_forsale(spu.getStd_unit_name());
		product.setSale_ratio(new BigDecimal("1"));
		product.setSale_num_least(new BigDecimal("1"));
		product.setAttrition_rate(new BigDecimal("0"));
		product.setState(1);
		product.setIs_weigh(0);
		product.setIs_price_timing(0);
		product.setSlitting(1);
		product.setPartframe(1);
		product.setStock_type(1);
		product.setStocks(null);
		product.setRounding(0);
		product.setOuter_id("");
		product.setSupplier_id(supplier_id);
		product.setPurchase_spec_id(purchaseSpec.getId());
		product.setClean_food(1);
		product.setRemark_type(2);

		ProductBean.CleanFoodInfo cleanFoodInfo = product.new CleanFoodInfo();
		cleanFoodInfo.setLicense("IOS123123");
		cleanFoodInfo.setStorage_condition("冷藏");
		cleanFoodInfo.setProduct_performance_standards("卫*12233");
		cleanFoodInfo.setNutrition("蛋白质、水");
		cleanFoodInfo.setMaterial_description("新鲜");
		cleanFoodInfo.setProcess_label_id(label_id);
		cleanFoodInfo.setOrigin_place("广东深圳");
		cleanFoodInfo.setCombine_technic_status("0");
		cleanFoodInfo.setCut_specification("分切");
		cleanFoodInfo.setRecommended_method("煮熟后食用");
		cleanFoodInfo.setShelf_life(90);

		product.setClean_food_info(cleanFoodInfo);

		List<ProductBean.Ingredient> ingredients = new ArrayList<>();
		ProductBean.Ingredient ingredient = product.new Ingredient();
		ingredient.setAttrition_rate(new BigDecimal("0"));
		ingredient.setSupplier_id(supplier_id);
		ingredient.setStd_unit_name(spu.getStd_unit_name());
		ingredient.setSale_unit_name(purchaseSpec.getPurchase_unit_name());
		ingredient.setId(purchaseSpec.getId());
		ingredient.setRatio(purchaseSpec.getRatio());
		ingredient.setName(purchaseSpec.getName());
		ingredient.setTechnic_flow_len(0);
		ingredient.setRemark_type(1);
		ingredient.setSale_proportion(new BigDecimal("1"));
		ingredient.setProportion(new BigDecimal("1"));
		ingredient.setCategory_id_2(purchaseSpec.getCategory_2());
		ingredient.setVersion(1);

		ingredients.add(ingredient);

		product.setIngredients(ingredients);

	}

	@Test
	public void productTestCase01() {
		try {
			ReporterCSS.title("测试点: 新建净菜销售商品");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean productResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			boolean result = compareResult(product, productResult);
			Assert.assertEquals(result, true, "新建净菜填写的信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建净菜商品出现错误: ", e);
			Assert.fail("新建净菜商品出现错误: ", e);
		}
	}

	@Test
	public void productTestCase02() {
		try {
			ReporterCSS.title("测试点: 新建净菜销售SKU,物料信息使用多种商品");
			List<IngredientBean> ingredients = productService.searchIngredient("菜");
			Assert.assertNotEquals(ingredients, null, "新建净菜,搜索物料信息失败");

			List<ProductBean.Ingredient> ingredientParams = product.getIngredients();
			boolean result = true;
			for (IngredientBean ingredient : ingredients) {
				ProductBean.Ingredient ingredientParam = product.new Ingredient();
				if (ingredient.getRemark_type() == 1) {
					ingredientParam.setSupplier_id(supplier_id);
				}
				ingredientParam.setAttrition_rate(new BigDecimal("0"));
				ingredientParam.setStd_unit_name(ingredient.getStd_unit_name());
				ingredientParam.setSale_unit_name(ingredient.getSale_unit_name());
				ingredientParam.setId(ingredient.getId());
				ingredientParam.setRatio(ingredient.getRatio());
				ingredientParam.setName(ingredient.getName());
				ingredientParam.setTechnic_flow_len(0);
				ingredientParam.setRemark_type(ingredient.getRemark_type());
				ingredientParam.setSale_proportion(new BigDecimal("1").multiply(ingredient.getRatio()));
				ingredientParam.setProportion(new BigDecimal("1"));
				ingredientParam.setCategory_id_2(ingredient.getCategory_id_2());
				ingredientParam.setVersion(1);
				ingredientParams.add(ingredientParam);
				if (ingredientParams.size() >= 6) {
					break;
				}
			}
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean productResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			result = compareResult(product, productResult);
			Assert.assertEquals(result, true, "新建净菜填写的信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建净菜商品出现错误: ", e);
			Assert.fail("新建净菜商品出现错误: ", e);
		}
	}

	@Test
	public void productTestCase03() {
		try {
			ReporterCSS.title("测试点: 修改净菜销售商品");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean productResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			ProductBean.CleanFoodInfo cleanFoodInfo = productResult.getClean_food_info();
			cleanFoodInfo.setLicense("IOS9012343");
			cleanFoodInfo.setStorage_condition("冷冻");
			cleanFoodInfo.setProduct_performance_standards("卫*901101");
			cleanFoodInfo.setNutrition("淀粉");
			cleanFoodInfo.setMaterial_description("保鲜");
			cleanFoodInfo.setProcess_label_id(new BigDecimal("0"));
			cleanFoodInfo.setOrigin_place("广东深圳宝安");
			cleanFoodInfo.setCombine_technic_status("0");
			cleanFoodInfo.setCut_specification("1/9");
			cleanFoodInfo.setRecommended_method("直接食用");
			cleanFoodInfo.setShelf_life(90);

			boolean result = productService.updateProduct(productResult);
			Assert.assertEquals(result, true, "修改净菜商品失败");

			ProductBean productUpdateResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productUpdateResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			result = compareResult(productResult, productUpdateResult);
			Assert.assertEquals(result, true, "修改的净菜信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("修改净菜商品出现错误: ", e);
			Assert.fail("修改净菜商品出现错误: ", e);
		}
	}

	@Test
	public void productTestCase04() {
		try {
			ReporterCSS.title("测试点: 修改净菜销售SKU,修改物料信息");

			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建销售SKU失败");

			ProductBean productResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<IngredientBean> ingredients = productService.searchIngredient("菜");
			Assert.assertNotEquals(ingredients, null, "新建净菜,搜索物料信息失败");

			List<ProductBean.Ingredient> ingredientParams = productResult.getIngredients();
			boolean result = true;
			for (IngredientBean ingredient : ingredients) {
				ProductBean.Ingredient ingredientParam = product.new Ingredient();
				if (ingredient.getRemark_type() == 1) {
					ingredientParam.setSupplier_id(supplier_id);
				}
				ingredientParam.setAttrition_rate(new BigDecimal("0"));
				ingredientParam.setStd_unit_name(ingredient.getStd_unit_name());
				ingredientParam.setSale_unit_name(ingredient.getSale_unit_name());
				ingredientParam.setId(ingredient.getId());
				ingredientParam.setRatio(ingredient.getRatio());
				ingredientParam.setName(ingredient.getName());
				ingredientParam.setTechnic_flow_len(0);
				ingredientParam.setRemark_type(ingredient.getRemark_type());
				ingredientParam.setSale_proportion(new BigDecimal("1").multiply(ingredient.getRatio()));
				ingredientParam.setProportion(new BigDecimal("1"));
				ingredientParam.setCategory_id_2(ingredient.getCategory_id_2());
				ingredientParam.setVersion(1);
				ingredientParams.add(ingredientParam);
				if (ingredientParams.size() >= 6) {
					break;
				}
			}
			result = productService.updateProduct(productResult);
			Assert.assertEquals(result, true, "修改净菜信息失败");

			ProductBean productUpdateResult = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(productUpdateResult, null, "新建的净菜商品 " + sku_id + "没有查询到");

			result = compareResult(productResult, productUpdateResult);
			Assert.assertEquals(result, true, "修改净菜填写的信息与查询到的不一致");
		} catch (Exception e) {
			logger.error("新建净菜商品出现错误: ", e);
			Assert.fail("新建净菜商品出现错误: ", e);
		}
	}

	@Test
	public void productTestCase05() {
		try {
			ReporterCSS.title("测试点: 修改净菜销售商品,为物料信息添加工艺设置");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			boolean result = true;
			String msg = null;

			// 每个物料信息把所有的工艺信息都加上
			for (ProductBean.Ingredient ingredient : ingredients) {
				for (TechnicBean technic : technics) {
					technicFlowCreateParam = new TechnicFlowCreateParam();
					technicFlowCreateParam.setSku_id(product.getId());
					technicFlowCreateParam.setIngredient_id(ingredient.getId());
					technicFlowCreateParam.setDesc(technic.getDesc());
					technicFlowCreateParam.setTechnic_id(technic.getId());
					technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
					technicFlowCreateParam.setType(1);

					// 工艺自定义列表参数
					List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

					// 工艺的自定义列表
					List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

					// 工艺中所有的自定义字段都给安排上
					for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
						TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
						technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

						// 自定义字段有参数的话就随机选一个参数进行设置
						List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
						if (params.size() > 0) {
							TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
							technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
						}
						technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
					}
					technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

					String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
					Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");

					List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
					Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

					TechnicFlowBean technicFlow = technicFlows.stream().filter(t -> t.getId().equals(technic_flow_id))
							.findAny().orElse(null);

					if (technicFlow == null) {
						msg = String.format("净菜商品:%s,物料信息:%s,添加的工艺:%s没有查询到", sku_id, ingredient.getName(),
								technic_flow_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (!technicFlow.getTechnic_id().equals(technicFlowCreateParam.getTechnic_id())) {
						msg = String.format("净菜商品:%s,物料信息:%s,添加的工艺:%s,工艺ID与预期的不一致,预期:%s,实际:%s", sku_id,
								ingredient.getName(), technicFlowCreateParam.getTechnic_id(),
								technicFlow.getTechnic_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					List<TechnicFlowBean.CustomColParam> customColParamResults = technicFlow.getCustom_col_params();
					if (technicFlowCreateParamCustomCols.size() != customColParamResults.size()) {
						msg = String.format("净菜商品:%s,物料信息:%s,工艺:%s,自定义字段总数与预期不一致,预期:%s,实际:%s", sku_id,
								ingredient.getName(), technicFlow.getName(), technicFlowCreateParamCustomCols.size(),
								customColParamResults.size());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					} else {
						for (TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol : technicFlowCreateParamCustomCols) {
							BigDecimal col_id = technicFlowCreateParamCustomCol.getCol_id();
							BigDecimal col_param_id = technicFlowCreateParamCustomCol.getCol_param_id();

							TechnicFlowBean.CustomColParam customColParamResult = customColParamResults.stream()
									.filter(s -> s.getCol_id().compareTo(col_id) == 0
											&& s.getParam_id().compareTo(col_param_id) == 0)
									.findAny().orElse(null);
							if (customColParamResult == null) {
								msg = String.format("净菜商品:%s,物料信息:%s,工艺:%s,没有找到对应的步骤:%s", sku_id,
										technicFlowCreateParam.getIngredient_id(), technicFlow.getName(),
										technicFlowCreateParamCustomCol.getCol_id());
								ReporterCSS.warn(msg);
								logger.warn(msg);
								result = false;
								continue;
							}
						}
					}
				}
			}
			Assert.assertEquals(result, true, "净菜商品物料信息添加工艺后,结果与预期不一致");
		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	@Test
	public void productTestCase06() {
		try {
			ReporterCSS.title("测试点: 净菜销售商品,物料的工艺设置,删除自定义字段");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			boolean result = true;
			String msg = null;

			// 每个物料信息把所有的工艺信息都加上
			for (ProductBean.Ingredient ingredient : ingredients) {
				for (TechnicBean technic : technics) {
					technicFlowCreateParam = new TechnicFlowCreateParam();
					technicFlowCreateParam.setSku_id(product.getId());
					technicFlowCreateParam.setIngredient_id(ingredient.getId());
					technicFlowCreateParam.setDesc(technic.getDesc());
					technicFlowCreateParam.setTechnic_id(technic.getId());
					technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
					technicFlowCreateParam.setType(1);

					// 工艺自定义列表参数
					List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

					// 工艺的自定义列表
					List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

					// 工艺中所有的自定义字段都给安排上
					for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
						TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
						technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

						// 自定义有参数的话就随机选一个参数进行设置
						List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
						if (params.size() > 0) {
							TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
							technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
						}
						technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
					}
					technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

					String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
					Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");

					TechnicFlowUpdateParam technicFlowUpdateParam = new TechnicFlowUpdateParam();
					technicFlowUpdateParam.setId(technic_flow_id);
					technicFlowUpdateParam.setSku_id(product.getId());
					technicFlowUpdateParam.setIngredient_id(ingredient.getId());
					technicFlowUpdateParam.setDesc(technic.getDesc());
					technicFlowUpdateParam.setTechnic_id(technic.getId());
					technicFlowUpdateParam.setRemark_type(ingredient.getRemark_type());
					technicFlowUpdateParam.setType(1);
					technicFlowUpdateParam.setActive(true);
					technicFlowUpdateParam.setName(technic.getName());
					technicFlowUpdateParam.setCustom_cols(new ArrayList<>());
					result = technicService.updateTechnicFlow(technicFlowUpdateParam);
					Assert.assertEquals(result, true, "修改净菜商品物料的工艺信息失败");

					List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
					Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

					TechnicFlowBean technicFlow = technicFlows.stream().filter(t -> t.getId().equals(technic_flow_id))
							.findAny().orElse(null);

					if (technicFlow == null) {
						msg = String.format("净菜商品:%s,物料信息:%s,添加的工艺:%s没有查询到", sku_id, ingredient.getName(),
								technic_flow_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					if (!technicFlow.getTechnic_id().equals(technicFlowUpdateParam.getTechnic_id())) {
						msg = String.format("净菜商品:%s,物料信息:%s,添加的工艺:%s,工艺ID与预期的不一致,预期:%s,实际:%s", sku_id,
								ingredient.getName(), technicFlowCreateParam.getTechnic_id(),
								technicFlow.getTechnic_id());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
						continue;
					}

					List<TechnicFlowBean.CustomColParam> customColParamResults = technicFlow.getCustom_col_params();
					if (customColParamResults.size() > 0) {
						msg = String.format("净菜商品:%s,物料信息:%s,添加的工艺:%s,工艺对应的自定义字段数应该为0,实际为:%s", sku_id,
								ingredient.getName(), technic.getName(), customColParamResults.size());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "净菜商品物料信息添加工艺后,结果与预期不一致");
		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	@Test
	public void productTestCase07() {
		try {
			ReporterCSS.title("测试点: 净菜销售商品,物料的工艺设置,修改自定义字段");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			boolean result = true;
			String msg = null;

			ProductBean.Ingredient ingredient = ingredients.get(0);
			TechnicBean technic = technics.stream().filter(t -> t.getName().equals("清洗")).findAny().orElse(null);

			technicFlowCreateParam = new TechnicFlowCreateParam();
			technicFlowCreateParam.setSku_id(product.getId());
			technicFlowCreateParam.setIngredient_id(ingredient.getId());
			technicFlowCreateParam.setDesc(technic.getDesc());
			technicFlowCreateParam.setTechnic_id(technic.getId());
			technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
			technicFlowCreateParam.setType(1);

			// 工艺自定义列表参数
			List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

			// 工艺的自定义列表
			List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

			// 工艺中所有的自定义字段都给安排上
			for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
				TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
				technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

				// 自定义有参数的话就随机选一个参数进行设置
				List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
				if (params.size() > 0) {
					TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
					technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
				}
				technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
			}
			technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

			String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
			Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");

			List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
			Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

			TechnicFlowBean technicFlow = technicFlows.stream().filter(t -> t.getId().equals(technic_flow_id)).findAny()
					.orElse(null);

			Assert.assertNotEquals(technicFlow, null,
					"净菜商品:" + sku_id + ",物料信息:" + ingredient.getName() + ",添加的工艺:" + technic.getName() + "没有查询到");

			List<TechnicFlowBean.CustomColParam> customColParamResults = technicFlow.getCustom_col_params();

			// 开始封装修改工艺参数
			TechnicFlowUpdateParam technicFlowUpdateParam = new TechnicFlowUpdateParam();
			List<TechnicFlowUpdateParam.CustomCol> customCols = new ArrayList<>();
			TechnicFlowUpdateParam.CustomCol customCol = null;

			// 切换工艺的自定义字段
			for (TechnicFlowBean.CustomColParam customColParamResult : customColParamResults) {
				BigDecimal col_id = customColParamResult.getCol_id();
				TechnicBean.CustomColParam customColParam = technicCustomColParams.stream()
						.filter(t -> t.getCol_id().equals(col_id)).findAny().orElse(null);

				List<TechnicBean.CustomColParam.Param> params = customColParam.getParams();
				BigDecimal param_id = customColParamResult.getParam_id();

				TechnicBean.CustomColParam.Param param = params.stream().filter(p -> !p.getParam_id().equals(param_id))
						.findFirst().orElse(null);

				customCol = technicFlowUpdateParam.new CustomCol();
				customCol.setCol_id(col_id);
				customCol.setCol_param_id(param.getParam_id());
				customCols.add(customCol);
			}

			technicFlowUpdateParam.setId(technic_flow_id);
			technicFlowUpdateParam.setSku_id(product.getId());
			technicFlowUpdateParam.setIngredient_id(ingredient.getId());
			technicFlowUpdateParam.setDesc(technic.getDesc());
			technicFlowUpdateParam.setTechnic_id(technic.getId());
			technicFlowUpdateParam.setRemark_type(ingredient.getRemark_type());
			technicFlowUpdateParam.setType(1);
			technicFlowUpdateParam.setActive(true);
			technicFlowUpdateParam.setName(technic.getName());
			technicFlowUpdateParam.setCustom_cols(customCols);
			result = technicService.updateTechnicFlow(technicFlowUpdateParam);
			Assert.assertEquals(result, true, "修改净菜商品物料的工艺信息失败");

			technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
			Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

			technicFlow = technicFlows.stream().filter(t -> t.getId().equals(technic_flow_id)).findAny().orElse(null);

			customColParamResults = technicFlow.getCustom_col_params();

			for (TechnicFlowUpdateParam.CustomCol customColUpdate : customCols) {
				BigDecimal col_id = customColUpdate.getCol_id();
				TechnicFlowBean.CustomColParam customColParam = customColParamResults.stream()
						.filter(c -> c.getCol_id().compareTo(col_id) == 0).findAny().orElse(null);
				if (customColParam == null) {
					msg = String.format("净菜商品:%s,物料信息:%s,工艺:%s,没有了自定义字段:%s", sku_id, ingredient.getName(),
							technic.getName(), col_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (customColParam.getParam_id().compareTo(customColUpdate.getCol_param_id()) != 0) {
					msg = String.format("净菜商品:%s,物料信息:%s,工艺:%s,自定义字段:%s,对应的值与预期不符,预期:%s,实际:%s", sku_id,
							ingredient.getName(), technic.getName(), col_id, customColUpdate.getCol_param_id(),
							customColParam.getParam_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "修改净菜物料信息的工艺信息,结果与预期不符");
		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	@Test
	public void productTestCase08() {
		try {
			ReporterCSS.title("测试点: 净菜销售商品,物料的工艺设置,添加后删除");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			ProductBean.Ingredient ingredient = ingredients.get(0);
			TechnicBean technic = technics.stream().filter(t -> t.getName().equals("清洗")).findAny().orElse(null);

			technicFlowCreateParam = new TechnicFlowCreateParam();
			technicFlowCreateParam.setSku_id(product.getId());
			technicFlowCreateParam.setIngredient_id(ingredient.getId());
			technicFlowCreateParam.setDesc(technic.getDesc());
			technicFlowCreateParam.setTechnic_id(technic.getId());
			technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
			technicFlowCreateParam.setType(1);

			// 工艺自定义列表参数
			List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

			// 工艺的自定义列表
			List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

			// 工艺中所有的自定义字段都给安排上
			for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
				TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
				technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

				// 自定义有参数的话就随机选一个参数进行设置
				List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
				if (params.size() > 0) {
					TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
					technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
				}
				technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
			}
			technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

			String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
			Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");

			boolean result = technicService.deleteTechnicFlow(technic_flow_id);
			Assert.assertEquals(result, true, "删除净菜物料的工艺信息失败");

			List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
			Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

			TechnicFlowBean technicFlow = technicFlows.stream().filter(t -> t.getId().equals(technic_flow_id)).findAny()
					.orElse(null);

			Assert.assertEquals(technicFlow, null,
					"净菜商品:" + sku_id + ",物料信息:" + ingredient.getName() + ",添加的工艺:" + technic.getName() + "没有真正删除成功");

		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	@Test
	public void productTestCase09() {
		try {
			ReporterCSS.title("测试点: 净菜商品,物料信息,工艺切换顺序");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			// 每个物料信息把所有的工艺信息都加上
			for (ProductBean.Ingredient ingredient : ingredients) {
				for (TechnicBean technic : technics) {
					technicFlowCreateParam = new TechnicFlowCreateParam();
					technicFlowCreateParam.setSku_id(product.getId());
					technicFlowCreateParam.setIngredient_id(ingredient.getId());
					technicFlowCreateParam.setDesc(technic.getDesc());
					technicFlowCreateParam.setTechnic_id(technic.getId());
					technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
					technicFlowCreateParam.setType(1);

					// 工艺自定义列表参数
					List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

					// 工艺的自定义列表
					List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

					// 工艺中所有的自定义字段都给安排上
					for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
						TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
						technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

						// 自定义字段有参数的话就随机选一个参数进行设置
						List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
						if (params.size() > 0) {
							TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
							technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
						}
						technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
					}
					technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

					String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
					Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");
				}

				List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
				Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

				Assert.assertEquals(technicFlows.size() >= 2, true, "物料信息的工艺数小于2,无法进行顺序更换操作");

				List<String> before_technic_flow_ids = technicFlows.stream().map(t -> t.getId())
						.collect(Collectors.toList());

				String id = before_technic_flow_ids.get(0);
				boolean result = technicService.changeTechnicFlow(id, "0");
				Assert.assertEquals(result, true, "物料信息切换工艺顺序失败");

				String temp_id = before_technic_flow_ids.get(1);
				before_technic_flow_ids.set(1, id);
				before_technic_flow_ids.set(0, temp_id);

				technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
				Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

				List<String> after_technic_flow_ids = technicFlows.stream().map(t -> t.getId())
						.collect(Collectors.toList());

				String msg = null;
				if (!before_technic_flow_ids.equals(after_technic_flow_ids)) {
					msg = String.format("净菜:%s,物料:%s,工艺信息切换顺序后,工艺排序与预期不符,预期:%s,实际:%s", sku_id, ingredient.getName(),
							before_technic_flow_ids, after_technic_flow_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "物料信息工艺切换顺序后,排序与预期不同");
			}
		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	@Test
	public void productTestCase10() {
		try {
			ReporterCSS.title("测试点: 净菜商品,物料信息,工艺切换顺序");
			sku_id = productService.createProduct(product);
			Assert.assertNotEquals(sku_id, null, "新建净菜商品失败");

			ProductBean product = productService.getProduct(spu.getSpu_id(), sku_id);
			Assert.assertNotEquals(product, null, "新建的净菜商品 " + sku_id + "没有查询到");

			List<TechnicBean> technics = technicService.getAllTechnics();
			Assert.assertNotEquals(technics, null, "获取所有的工艺信息失败");

			Assert.assertEquals(technics.size() >= 2, true, "站点无工艺信息,与预期不符");

			TechnicFlowCreateParam technicFlowCreateParam = null;
			List<ProductBean.Ingredient> ingredients = product.getIngredients();

			// 每个物料信息把所有的工艺信息都加上
			for (ProductBean.Ingredient ingredient : ingredients) {
				for (TechnicBean technic : technics) {
					technicFlowCreateParam = new TechnicFlowCreateParam();
					technicFlowCreateParam.setSku_id(product.getId());
					technicFlowCreateParam.setIngredient_id(ingredient.getId());
					technicFlowCreateParam.setDesc(technic.getDesc());
					technicFlowCreateParam.setTechnic_id(technic.getId());
					technicFlowCreateParam.setRemark_type(ingredient.getRemark_type());
					technicFlowCreateParam.setType(1);

					// 工艺自定义列表参数
					List<TechnicFlowCreateParam.CustomCol> technicFlowCreateParamCustomCols = new ArrayList<>();

					// 工艺的自定义列表
					List<TechnicBean.CustomColParam> technicCustomColParams = technic.getCustom_col_params();

					// 工艺中所有的自定义字段都给安排上
					for (TechnicBean.CustomColParam technicCustomColParam : technicCustomColParams) {
						TechnicFlowCreateParam.CustomCol technicFlowCreateParamCustomCol = technicFlowCreateParam.new CustomCol();
						technicFlowCreateParamCustomCol.setCol_id(technicCustomColParam.getCol_id());

						// 自定义字段有参数的话就随机选一个参数进行设置
						List<TechnicBean.CustomColParam.Param> params = technicCustomColParam.getParams();
						if (params.size() > 0) {
							TechnicBean.CustomColParam.Param param = NumberUtil.roundNumberInList(params);
							technicFlowCreateParamCustomCol.setCol_param_id(param.getParam_id());
						}
						technicFlowCreateParamCustomCols.add(technicFlowCreateParamCustomCol);
					}
					technicFlowCreateParam.setCustom_cols(technicFlowCreateParamCustomCols);

					String technic_flow_id = technicService.createTechnicFlow(technicFlowCreateParam);
					Assert.assertNotEquals(technic_flow_id, null, "为净菜销售商品的物料信息添加工艺信息失败");
				}

				List<TechnicFlowBean> technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
				Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

				Assert.assertEquals(technicFlows.size() >= 2, true, "物料信息的工艺数小于2,无法进行顺序更换操作");

				List<String> before_technic_flow_ids = technicFlows.stream().map(t -> t.getId())
						.collect(Collectors.toList());

				String id = before_technic_flow_ids.get(1);
				String next_id = before_technic_flow_ids.get(0);

				boolean result = technicService.changeTechnicFlow(id, next_id);
				Assert.assertEquals(result, true, "物料信息切换工艺顺序失败");

				before_technic_flow_ids.set(1, next_id);
				before_technic_flow_ids.set(0, id);

				technicFlows = technicService.getTechnicFlows(sku_id, ingredient.getId());
				Assert.assertNotEquals(technicFlows, null, "获取净菜商品的物料信息对应的工艺详细信息失败");

				List<String> after_technic_flow_ids = technicFlows.stream().map(t -> t.getId())
						.collect(Collectors.toList());

				String msg = null;
				if (!before_technic_flow_ids.equals(after_technic_flow_ids)) {
					msg = String.format("净菜:%s,物料:%s,工艺信息切换顺序后,工艺排序与预期不符,预期:%s,实际:%s", sku_id, ingredient.getName(),
							before_technic_flow_ids, after_technic_flow_ids);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
				Assert.assertEquals(result, true, "物料信息工艺切换顺序后,排序与预期不同");
			}
		} catch (Exception e) {
			logger.error("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
			Assert.fail("修改净菜销售商品,为物料信息添加工艺设置出现错误: ", e);
		}
	}

	public boolean compareResult(ProductBean productParam, ProductBean productResult) {
		String msg = null;
		boolean result = true;
		if (!productParam.getName().equals(productResult.getName())) {
			msg = String.format("新建的净菜销售SKU,规则名称与预期不一致,预期:%s,实际:%s", productParam.getName(), productResult.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getSalemenu_id() != null) {
			if (!productParam.getSalemenu_id().equals(productResult.getSalemenu_id())) {
				msg = String.format("新建的净菜销售SKU,规则名称与预期不一致,预期:%s,实际:%s", productParam.getName(),
						productResult.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}

		if (!productParam.getDesc().equals(productResult.getDesc())) {
			msg = String.format("新建的净菜销售SKU,描述信息与预期不一致,预期:%s,实际:%s", productParam.getDesc(), productResult.getDesc());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getSale_price().compareTo(productResult.getSale_price()) != 0) {
			msg = String.format("新建的净菜销售SKU,销售价格与预期不一致,预期:%s,实际:%s", productParam.getSale_price(),
					productResult.getSale_price());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!productParam.getSale_unit_name().equals(productResult.getSale_unit_name())) {
			msg = String.format("新建的净菜销售SKU,销售规则与预期不一致,预期:%s,实际:%s", productParam.getSale_unit_name(),
					productResult.getSale_unit_name());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!productParam.getStd_unit_name_forsale().equals(productResult.getStd_unit_name_forsale())) {
			msg = String.format("新建的净菜销售SKU,销售规格与预期不一致,预期:%s,实际:%s", productParam.getStd_unit_name_forsale(),
					productResult.getStd_unit_name_forsale());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getSale_ratio().compareTo(productResult.getSale_ratio()) != 0) {
			msg = String.format("新建的净菜销售SKU,销售规格比例与预期不一致,预期:%s,实际:%s", productParam.getSale_ratio(),
					productResult.getSale_ratio());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getSale_num_least().compareTo(productResult.getSale_num_least()) != 0) {
			msg = String.format("新建的净菜销售SKU,最小下单数与预期不一致,预期:%s,实际:%s", productParam.getSale_num_least(),
					productResult.getSale_num_least());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getState() != productResult.getState()) {
			msg = String.format("新建的净菜销售SKU,状态值与预期不一致,预期:%s,实际:%s", productParam.getState(), productResult.getState());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getIs_weigh() != productResult.getIs_weigh()) {
			msg = String.format("新建的净菜销售SKU,是否称重值与预期不一致,预期:%s,实际:%s", productParam.getIs_weigh(),
					productResult.getIs_weigh());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getIs_price_timing() != productResult.getIs_price_timing()) {
			msg = String.format("新建的净菜销售SKU,是否时价值与预期不一致,预期:%s,实际:%s", productParam.getIs_price_timing(),
					productResult.getIs_price_timing());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getStock_type() != productResult.getStock_type()) {
			msg = String.format("新建的净菜销售SKU,最小下单数与预期不一致,预期:%s,实际:%s", productParam.getStock_type(),
					productResult.getStock_type());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!productParam.getOuter_id().equals(productResult.getOuter_id())) {
			msg = String.format("新建的净菜销售SKU,自定义ID与预期不一致,预期:%s,实际:%s", productParam.getOuter_id(),
					productResult.getOuter_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (productParam.getClean_food() != productResult.getClean_food()) {
			msg = String.format("新建的净菜销售SKU,是否是净菜值与预期不一致,预期:%s,实际:%s", productParam.getClean_food(),
					productResult.getClean_food());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		ProductBean.CleanFoodInfo cleanFoodInfoParam = productParam.getClean_food_info();
		ProductBean.CleanFoodInfo cleanFoodInfoResult = productResult.getClean_food_info();

		if (!cleanFoodInfoParam.getLicense().equals(cleanFoodInfoResult.getLicense())) {
			msg = String.format("新建的净菜销售SKU,许可证与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getLicense(),
					cleanFoodInfoResult.getLicense());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getStorage_condition().equals(cleanFoodInfoResult.getStorage_condition())) {
			msg = String.format("新建的净菜销售SKU,贮存条件与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getStorage_condition(),
					cleanFoodInfoResult.getStorage_condition());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getProduct_performance_standards()
				.equals(cleanFoodInfoResult.getProduct_performance_standards())) {
			msg = String.format("新建的净菜销售SKU,产品执行标准与预期不一致,预期:%s,实际:%s",
					cleanFoodInfoParam.getProduct_performance_standards(),
					cleanFoodInfoResult.getProduct_performance_standards());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getProduct_performance_standards()
				.equals(cleanFoodInfoResult.getProduct_performance_standards())) {
			msg = String.format("新建的净菜销售SKU,产品执行标准与预期不一致,预期:%s,实际:%s",
					cleanFoodInfoParam.getProduct_performance_standards(),
					cleanFoodInfoResult.getProduct_performance_standards());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getNutrition().equals(cleanFoodInfoResult.getNutrition())) {
			msg = String.format("新建的净菜销售SKU,营养成分表与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getNutrition(),
					cleanFoodInfoResult.getNutrition());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getMaterial_description().equals(cleanFoodInfoResult.getMaterial_description())) {
			msg = String.format("新建的净菜销售SKU,材料说明与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getMaterial_description(),
					cleanFoodInfoResult.getMaterial_description());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getOrigin_place().equals(cleanFoodInfoResult.getOrigin_place())) {
			msg = String.format("新建的净菜销售SKU,产地与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getOrigin_place(),
					cleanFoodInfoResult.getOrigin_place());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getCut_specification().equals(cleanFoodInfoResult.getCut_specification())) {
			msg = String.format("新建的净菜销售SKU,切配规格与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getCut_specification(),
					cleanFoodInfoResult.getCut_specification());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getRecommended_method().equals(cleanFoodInfoResult.getRecommended_method())) {
			msg = String.format("新建的净菜销售SKU,建议使用方法与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getRecommended_method(),
					cleanFoodInfoResult.getRecommended_method());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!cleanFoodInfoParam.getProcess_label_id().equals(cleanFoodInfoResult.getProcess_label_id())) {
			msg = String.format("新建的净菜销售SKU,商品加工标签与预期不一致,预期:%s,实际:%s", cleanFoodInfoParam.getProcess_label_id(),
					cleanFoodInfoResult.getProcess_label_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<ProductBean.Ingredient> ingredientParams = productParam.getIngredients();
		List<ProductBean.Ingredient> ingredientResults = productResult.getIngredients();

		if (ingredientParams.size() != ingredientResults.size()) {
			msg = String.format("新建的净菜销售SKU,物料信息成分种类数量不一致,预期:%s,实际:%s", ingredientParams.size(),
					ingredientResults.size());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		} else {
			for (int i = 0; i < ingredientParams.size(); i++) {
				ProductBean.Ingredient ingredientParam = ingredientParams.get(i);
				ProductBean.Ingredient ingredientResult = ingredientResults.get(i);
				if (!ingredientParam.getId().equals(ingredientResult.getId())) {
					msg = String.format("新建的净菜销售SKU,物料信息成分第%s条与预期不一致,预期:%s,实际:%s", i + 1, ingredientParam.getId(),
							ingredientResult.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!ingredientParam.getId().equals(ingredientResult.getId())) {
					msg = String.format("新建的净菜销售SKU,物料信息成分第%s条与预期不一致,预期:%s,实际:%s", i + 1, ingredientParam.getId(),
							ingredientResult.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (ingredientParam.getSale_proportion().compareTo(ingredientResult.getSale_proportion()) != 0) {
					msg = String.format("新建的净菜销售SKU,物料信息成分第%s条单位数量(包装单位)与预期不一致,预期:%s,实际:%s", i + 1,
							ingredientParam.getSale_proportion(), ingredientResult.getSale_proportion());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (ingredientParam.getProportion().compareTo(ingredientResult.getProportion()) != 0) {
					msg = String.format("新建的净菜销售SKU,物料信息成分第%s条单位数量(基本单位)与预期不一致,预期:%s,实际:%s", i + 1,
							ingredientParam.getProportion(), ingredientResult.getProportion());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}

		return result;
	}

	@AfterMethod
	public void afterMethod() {
		if (sku_id != null) {
			try {
				boolean result = productService.deleteProduct(sku_id);
				Assert.assertEquals(result, true, "删除净菜商品 " + sku_id + " 失败");
			} catch (Exception e) {
				logger.error("删除净菜商品出现错误: ", e);
				Assert.fail("删除净菜商品出现错误: ", e);
			}
		}
	}
}
