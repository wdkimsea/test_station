package cn.guanmai.station.category;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.async.AsyncTaskResultBean;
import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.SpuIndexBean;
import cn.guanmai.station.bean.category.param.IndexSkuCreateModel;
import cn.guanmai.station.bean.category.param.IndexSkuUpdateModel;
import cn.guanmai.station.bean.category.param.SpuIndexFilterParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年6月23日 下午4:59:36
 * @description: 商品库下的销售规格
 * @version: 1.0
 */

public class IndexSkuTest extends LoginStation {
	private static Logger logger = LoggerFactory.getLogger(IndexSkuTest.class);

	private CategoryService categoryService;
	private SalemenuService salemenuService;
	private AsyncService asyncService;
	private InitDataBean initData;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		categoryService = new CategoryServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
		initData = getInitData();

	}

	@Test
	public void indexSkuTestCase01() {
		ReporterCSS.title("测试点: 商品库导入修改销售SKU");
		SkuBean sku_before = null;
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			BigDecimal task_id = categoryService.asyncExportIndexSkus(spuIndexFilterParam);
			Assert.assertNotEquals(task_id, null, "商户库导出销售SKU异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "成功");
			Assert.assertEquals(result, true, "商户库导出销售SKU异步任务执行失败");

			AsyncTaskResultBean asyncTaskResult = asyncService.getAsyncTaskResult(task_id);
			Assert.assertNotEquals(asyncTaskResult, null, "商户库导出销售SKU异步任务详细信息失败");

			String file_url = asyncTaskResult.getResult().getLink();

			String file_path = categoryService.downLoadIndexSkuFile(file_url);
			Assert.assertNotEquals(file_path, null, "商品库导出的销售SKU文件下载失败");

			File file = new File(file_path);

			List<IndexSkuUpdateModel> indexSkuUpdateModels = EasyExcelFactory.read(file).sheet(0)
					.head(IndexSkuUpdateModel.class).doReadSync();

			for (IndexSkuUpdateModel indexSkuUpdateModel : indexSkuUpdateModels) {
				if (indexSkuUpdateModel.getSupplier_customer_id().equals("-")
						|| indexSkuUpdateModel.getSupplier_customer_id().startsWith("No")) {
					indexSkuUpdateModel.setSupplier_customer_id(initData.getSupplier().getSupplier_id());
				}
			}

			// 取一个销售SKU进行信息修改
			IndexSkuUpdateModel indexSkuUpdateModel = NumberUtil.roundNumberInList(indexSkuUpdateModels);

			String spu_id = indexSkuUpdateModel.getSpu_id();
			String sku_id = indexSkuUpdateModel.getSku_id();
			sku_before = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku_before, null, "获取销售SKU" + sku_id + "详细信息失败");

			String new_sku_name = indexSkuUpdateModel.getSku_name() + "AT";
			indexSkuUpdateModel.setSku_name(new_sku_name);
			String new_desc = StringUtil.getRandomNumber(12);
			indexSkuUpdateModel.setDesc(new_desc);
			indexSkuUpdateModel.setIs_price_timing(1);
			indexSkuUpdateModel.setState(0);
			double new_unit_price = 2.4;
			indexSkuUpdateModel.setUnit_price(new_unit_price);

			double new_attrition_rate = 12;
			indexSkuUpdateModel.setAttrition_rate(new_attrition_rate);

			double new_sale_num_least = 2;
			indexSkuUpdateModel.setSale_num_least(new_sale_num_least);
			indexSkuUpdateModel.setIs_weigh(0);

			String outer_id = "AT" + StringUtil.getRandomNumber(5);
			indexSkuUpdateModel.setOuter_id(outer_id);

			ExcelWriter excelWriter = EasyExcel.write(file).build();
			WriteSheet writeSheet1 = EasyExcel.writerSheet(0, "SKU").head(IndexSkuUpdateModel.class).build();
			excelWriter.write(indexSkuUpdateModels, writeSheet1);
			excelWriter.finish();

			task_id = categoryService.importUpdateIndexSku(file_path);
			Assert.assertNotEquals(task_id, null, "批量导入修改商品库销售SKU,异步任务创建失败");

			result = asyncService.getAsyncTaskResult(task_id, "失败信息0条");
			Assert.assertEquals(result, true, "批量修改销售SKU,异步任务执行失败");

			String msg = null;

			SkuBean sku_after = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku_after, null, "获取销售SKU" + sku_id + "详细信息失败");
			if (sku_after.getIs_price_timing() != 1) {
				msg = String.format("批量导入修改销售SKU %s,是否时价状态没有更新,预期:1,实际:%s", sku_id, sku_after.getIs_price_timing());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku_after.getState() != 0) {
				msg = String.format("批量导入修改销售SKU %s,销售状态值没有更新,预期:0,实际:%s", sku_id, sku_after.getState());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			if (sku_after.getStd_sale_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP)
					.compareTo(new BigDecimal(String.valueOf(new_unit_price))) != 0) {
				msg = String.format("批量导入修改销售SKU %s,销售单价没有更新,预期:%s,实际:%s", sku_id, new_unit_price,
						sku_after.getStd_sale_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!sku_after.getName().equals(new_sku_name)) {
				msg = String.format("批量导入修改销售SKU %s,规格名称没有更新,预期:%s,实际:%s", sku_id, new_sku_name, sku_after.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!sku_after.getDesc().equals(new_desc)) {
				msg = String.format("批量导入修改销售SKU %s,描述信息没有更新,预期:%s,实际:%s", sku_id, new_desc, sku_after.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku_before.getStd_unit_name().equals("斤")) {
				if (sku_after.getAttrition_rate().compareTo(new BigDecimal(String.valueOf(new_attrition_rate))) != 0) {
					msg = String.format("批量导入修改销售SKU %s,损耗比例没有更新,预期:%s,实际:%s", sku_id, new_attrition_rate,
							sku_after.getAttrition_rate());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (sku_after.getSale_num_least().compareTo(new BigDecimal(String.valueOf(new_sale_num_least))) != 0) {
				msg = String.format("批量导入修改销售SKU %s,最小小单数没有更新,预期:%s,实际:%s", sku_id, new_sale_num_least,
						sku_after.getSale_num_least());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku_after.getIs_weigh() != 0) {
				msg = String.format("批量导入修改销售SKU %s,是否称重没有更新,预期:0,实际:%s", sku_id, sku_after.getIs_weigh());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!sku_after.getOuter_id().equals(outer_id)) {
				msg = String.format("批量导入修改销售SKU %s,自定义编码没有更新,预期:%s,实际:%s", sku_id, outer_id, sku_after.getOuter_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "批量修改,修改的结果与预期不符");
		} catch (Exception e) {
			logger.error("商品库导入修改销售SKU遇到错误: ", e);
			Assert.fail("商品库导入修改销售SKU遇到错误: ", e);
		} finally {
			try {
				if (sku_before != null) {
					ReporterCSS.step("后置处理: 恢复修改的销售SKU信息");
					boolean result = categoryService.updateSaleSku(sku_before);
					Assert.assertEquals(result, true, "销售SKU修改失败");
				}
			} catch (Exception e) {
				logger.error("修改销售SKU遇到错误: ", e);
				Assert.fail("修改销售SKU遇到错误: ", e);
			}
		}
	}

	public void indexSkuTestCase02() {
		ReporterCSS.title("测试点: 商品库批量导入新建销售商品");
		String spu_id = null;
		try {
			String user_dir = System.getProperty("user.dir");
			String excel_path = user_dir + "/temp/temp.xlsx";
			File file = new File(excel_path);

			String spu_name = StringUtil.getRandomString(4).toUpperCase();
			List<IndexSkuCreateModel> indexSkuCreateModels = new ArrayList<IndexSkuCreateModel>();
			IndexSkuCreateModel indexSkuCreateModel1 = new IndexSkuCreateModel();
			indexSkuCreateModel1.setCategory1_name("蔬菜");
			indexSkuCreateModel1.setCategory2_name("叶菜");
			indexSkuCreateModel1.setUnit_name("斤");
			indexSkuCreateModel1.setSpu_name(spu_name);
			indexSkuCreateModel1.setOuter_id(StringUtil.getRandomString(6).toUpperCase());
			indexSkuCreateModel1.setDesc("SZ");
			// indexSkuCreateModel1.setPic_name("");
			indexSkuCreateModels.add(indexSkuCreateModel1);

			IndexSkuCreateModel indexSkuCreateModel2 = new IndexSkuCreateModel();
			String spu_name2 = StringUtil.getRandomString(4).toUpperCase();
			indexSkuCreateModel2.setCategory1_name("蔬菜");
			indexSkuCreateModel2.setCategory2_name("叶菜");
			indexSkuCreateModel2.setUnit_name("斤");
			indexSkuCreateModel2.setSpu_name(spu_name2);
			indexSkuCreateModel2.setOuter_id(StringUtil.getRandomString(6).toUpperCase());
			indexSkuCreateModel2.setDesc("SZ");
			indexSkuCreateModel2.setPic_name("");
			indexSkuCreateModels.add(indexSkuCreateModel2);

			String imgZip_path = user_dir + "/image/img.zip";

			EasyExcel.write(file).excelType(ExcelTypeEnum.XLSX).relativeHeadRowIndex(0).head(IndexSkuCreateModel.class)
					.sheet(0, "商品库").doWrite(indexSkuCreateModels);

			String salemenu_id = initData.getSalemenu().getId();
			List<String> salemenu_ids = new ArrayList<String>();
			salemenu_ids.add(salemenu_id);

			Map<String, String> fileMap = new HashMap<String, String>();
			fileMap.put("pic", imgZip_path);
			fileMap.put("excel", excel_path);

			BigDecimal task_id = categoryService.importCreateMerchandise(fileMap, salemenu_ids);
			Assert.assertNotEquals(task_id, null, "批量导入新建商品库异步任务创建失败");

			boolean result = asyncService.getAsyncTaskResult(task_id, "失败0");
			Assert.assertEquals(result, true, "批量导入新建商品库异步任务执行失败");

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setQ(spu_name);
			spuIndexFilterParam.setLimit(10);
			spuIndexFilterParam.setOffset(0);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			SpuIndexBean spuIndex = spuIndexList.stream().filter(s -> s.getSpu_name().equals(spu_name)).findAny()
					.orElse(null);
			Assert.assertNotEquals(spuIndex, null, "批量导入新建的SPU在商品库没有找到");

			spu_id = spuIndex.getSpu_id();

			SpuBean spu = categoryService.getSpuById(spu_id);
			Assert.assertNotEquals(spu, null, "获取批量导入的SPU:" + spu_id + "详细信息失败");

		} catch (Exception e) {
			logger.error("批量导入新建销售商品遇到错误: ", e);
			Assert.fail("批量导入新建销售商品遇到错误: ", e);
		} finally {
			try {
				if (spu_id != null) {
					boolean result = categoryService.deleteSpu(spu_id);
					Assert.assertEquals(result, true, "删除SPU失败");
				}
			} catch (Exception e) {
				logger.error("删除SPU遇到错误: ", e);
				Assert.fail("删除SPU遇到错误: ", e);
			}
		}
	}

	@Test
	public void indexSkuTestCase03() {
		ReporterCSS.title("测试点: 商品库按分类搜索过滤");
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(10);
			spuIndexFilterParam.setOffset(0);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			SpuIndexBean spuIndex = NumberUtil.roundNumberInList(spuIndexList);
			String category1_name = spuIndex.getCategory_name_1();
			String category2_name = spuIndex.getCategory_name_2();
			String pinlei_name = spuIndex.getPinlei_name();

			Category1Bean category1 = categoryService.getCategory1ByName(category1_name);
			Assert.assertNotEquals(category1, null, "获取一级分类信息失败");

			Category2Bean category2 = categoryService.getCategory2ByName(category1.getId(), category2_name);
			Assert.assertNotEquals(category2, null, "获取二级分类信息失败");

			PinleiBean pinlei = categoryService.getPinleiByName(category2.getId(), pinlei_name);
			Assert.assertNotEquals(pinlei, null, "获取品类分类信息失败");

			JSONArray category1_ids = new JSONArray();
			category1_ids.add(category1.getId());
			JSONArray category2_ids = new JSONArray();
			category2_ids.add(category2.getId());
			JSONArray pinlei_ids = new JSONArray();
			pinlei_ids.add(pinlei.getId());

			spuIndexFilterParam.setCategory1_ids(category1_ids);
			spuIndexFilterParam.setCategory2_ids(category2_ids);
			spuIndexFilterParam.setPinlei_ids(pinlei_ids);
			spuIndexFilterParam.setLimit(50);

			spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean s : spuIndexList) {
				if (!s.getCategory_name_1().equals(category1_name)) {
					msg = String.format("商品库搜索过滤,按一级分类[%s]过滤,过滤出了其他的分类[%s]商品数据", category1_name,
							s.getCategory_name_1());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!s.getCategory_name_2().equals(category2_name)) {
					msg = String.format("商品库搜索过滤,按二级分类[%s]过滤,过滤出了其他的分类[%s]商品数据", category2_name,
							s.getCategory_name_2());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!s.getPinlei_name().equals(pinlei_name)) {
					msg = String.format("商品库搜索过滤,按品类分类[%s]过滤,过滤出了其他的分类[%s]商品数据", pinlei_name, s.getPinlei_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "商品库按商品分类过滤,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("商品库按分类搜索过滤遇到错误: ", e);
			Assert.fail("商品库按分类搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase04() {
		ReporterCSS.title("测试点: 商品库按关键词过滤");
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(10);
			spuIndexFilterParam.setOffset(0);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			SpuIndexBean spuIndex = NumberUtil.roundNumberInList(spuIndexList);
			String spu_name = spuIndex.getSpu_name();

			spuIndexFilterParam.setQ(spu_name);
			spuIndexFilterParam.setLimit(50);

			spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean s : spuIndexList) {
				if (!s.getSpu_name().contains(spu_name)) {
					msg = String.format("商品库按关键词[%s]搜索过滤,过滤出了不符合关键词[%s]商品数据", spu_name, s.getSpu_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
			}
			Assert.assertEquals(result, true, "商品库按关键词过滤搜索,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("商品库按关键词搜索过滤遇到错误: ", e);
			Assert.fail("商品库按关键词搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase05() {
		ReporterCSS.title("测试点: 商品库按报价单状态过滤");
		try {
			List<SalemenuBean> salemenuList = salemenuService.getSalemenuList(4, 1);
			Assert.assertNotEquals(salemenuList, null, "获取报价单列表失败");

			List<String> salemenu_ids = salemenuList.stream().map(s -> s.getId()).collect(Collectors.toList());

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(50);
			spuIndexFilterParam.setOffset(0);
			spuIndexFilterParam.setSalemenu_is_active(1);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean spu : spuIndexList) {
				List<SkuBean> skus = spu.getSkus();
				if (skus.size() > 0) {
					for (SkuBean sku : skus) {
						if (!salemenu_ids.contains(sku.getSalemenu_id())) {
							msg = String.format("商品库按报价单状态过滤,只过滤激活报价单里的商品,把未激活的报价单[%s]里的商品[%s]也过滤出来了",
									sku.getSalemenu_id(), sku.getId());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}
			Assert.assertEquals(result, true, "商品库按报价单状态过滤,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("商品库报价单状态过滤遇到错误: ", e);
			Assert.fail("商品库报价单状态过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase06() {
		ReporterCSS.title("测试点: 商品库按图片搜索过滤");
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(50);
			spuIndexFilterParam.setOffset(0);
			spuIndexFilterParam.setHas_images(1);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean spu : spuIndexList) {
				if (!spu.getImage().contains("http")) {
					msg = String.format("商品库按图片过滤,过滤有图片的商品,把没有图片的商品%s也过滤出来了", spu.getSpu_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商品库按状态过滤,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("商品库按有无图片搜索过滤遇到错误: ", e);
			Assert.fail("商品库按有无图片搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase07() {
		ReporterCSS.title("测试点: 商品库按时价状态过滤");
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(50);
			spuIndexFilterParam.setOffset(0);
			spuIndexFilterParam.setIs_price_timing(1);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean spu : spuIndexList) {
				List<SkuBean> skus = spu.getSkus();
				if (skus.size() > 0) {
					for (SkuBean sku : skus) {
						if (sku.getIs_price_timing() != 1) {
							msg = String.format("商品库按时价状态过滤,过滤时价商品,把非时价商品%s也过滤出来了", sku.getId());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}
			Assert.assertEquals(result, true, "商品库按时价状态过滤,过滤出的数据与预期不符");
		} catch (Exception e) {
			logger.error("商品库按关键词搜索过滤遇到错误: ", e);
			Assert.fail("商品库按关键词搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase08() {
		ReporterCSS.title("测试点: 商品库按报价单搜索过滤");
		try {
			List<SalemenuBean> salemenuList = salemenuService.getSalemenuList(4, 1);
			Assert.assertNotEquals(salemenuList, null, "获取报价单列表失败");

			SalemenuBean salemenu = NumberUtil.roundNumberInList(salemenuList);

			String salemenu_id = salemenu.getId();
			JSONArray salemenu_ids = new JSONArray();
			salemenu_ids.add(salemenu_id);

			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(50);
			spuIndexFilterParam.setOffset(0);
			spuIndexFilterParam.setSalemenu_ids(salemenu_ids);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库按报价单搜索过滤失败");

			boolean result = true;
			String msg = null;
			for (SpuIndexBean spu : spuIndexList) {
				List<SkuBean> skus = spu.getSkus();
				if (skus.size() > 0) {
					for (SkuBean sku : skus) {
						if (!sku.getSalemenu_id().equals(salemenu_id)) {
							msg = String.format("商品库按报价单[%s]过滤,过滤出了其他报价单[%s]里的商品", salemenu_id, sku.getSalemenu_id());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}
			Assert.assertEquals(result, true, "商品库按报价单过滤,过滤出的数据与预期不符");

		} catch (Exception e) {
			logger.error("商品库按报价单搜索过滤遇到错误: ", e);
			Assert.fail("商品库按报价单搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void indexSkuTestCase09() {
		ReporterCSS.title("测试点: 商品库翻页");
		try {
			SpuIndexFilterParam spuIndexFilterParam = new SpuIndexFilterParam();
			spuIndexFilterParam.setLimit(10);
			spuIndexFilterParam.setOffset(0);

			List<SpuIndexBean> spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			List<String> spu_ids_1 = spuIndexList.stream().map(s -> s.getSpu_id()).collect(Collectors.toList());

			spuIndexFilterParam.setOffset(10);
			spuIndexList = categoryService.searchSpuIndex(spuIndexFilterParam);
			Assert.assertNotEquals(spuIndexList, null, "商品库搜索过滤失败");

			List<String> spu_ids_2 = spuIndexList.stream().map(s -> s.getSpu_id()).collect(Collectors.toList());
			spu_ids_1.retainAll(spu_ids_2);

			Assert.assertEquals(spu_ids_1.size(), 0, "商品库翻页,出现了重复数据" + spu_ids_1);

		} catch (Exception e) {
			logger.error("商品库按关键词搜索过滤遇到错误: ", e);
			Assert.fail("商品库按关键词搜索过滤遇到错误: ", e);
		}
	}

}
