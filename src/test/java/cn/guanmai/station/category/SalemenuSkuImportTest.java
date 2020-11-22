package cn.guanmai.station.category;

import java.io.File;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SalemenuSkuBean;
import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.category.param.SalemenuSkuFilterParam;
import cn.guanmai.station.bean.category.param.SkuCreateModel;
import cn.guanmai.station.bean.category.param.SkuUpdateModel;
import cn.guanmai.station.bean.system.param.MerchandiseProfileParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年3月17日 上午11:12:34
 * @description: 报价单里的商品批量导入新建、修改操作
 * @version: 1.0
 */

public class SalemenuSkuImportTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SalemenuSkuImportTest.class);

	private SalemenuService salemenuService;
	private CategoryService categoryService;
	private SalemenuSkuFilterParam salemenuSkuFilterParam;
	private LoginUserInfoService loginUserInfoService;
	private ProfileService profileService;
	private String station_id;
	private String salemenu_id;
	private String group_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		salemenuService = new SalemenuServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		profileService = new ProfileServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			SalemenuFilterParam filterParam = new SalemenuFilterParam();
			filterParam.setWith_sku_num(1);
			filterParam.setType(-1);
			List<SalemenuBean> salemenus = salemenuService.searchSalemenu(filterParam);
			Assert.assertNotEquals(salemenus, null, "搜索过滤报价单列表失败");

			// 找出商品数最小的报价单
			Optional<SalemenuBean> optionalSalemenu = salemenus.stream().filter(s -> s.getSku_num() > 0)
					.min(Comparator.comparingInt(SalemenuBean::getSku_num));

			SalemenuBean salemenu = optionalSalemenu.get();

			salemenu_id = salemenu.getId();
			salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			station_id = loginUserInfoService.getLoginUserInfo().getStation_id();
			Assert.assertNotEquals(station_id, null, "获取站点ID失败");

			group_id = String.valueOf(loginUserInfoService.getLoginUserInfo().getGroup_id());
		} catch (Exception e) {
			logger.error("搜索过滤报价单遇到错误: ", e);
			Assert.fail("搜索过滤报价单遇到错误: ", e);
		}
	}

	@Test
	public void importUpdateSkusTestCase01() {
		ReporterCSS.title("测试点: 批量导入修改报价单里的商品信息");
		SkuBean sku_before = null;
		try {
			MerchandiseProfileParam merchandiseProfileParam = new MerchandiseProfileParam();
			merchandiseProfileParam.setShow_tax_rate(0);
			merchandiseProfileParam.setShow_sku_outer_id(0);
			merchandiseProfileParam.setShow_tax_rate(1);
			merchandiseProfileParam.setSync_price_timing(0);

			boolean result = profileService.updateMerchandiseProfile(merchandiseProfileParam);
			Assert.assertEquals(result, true, "系统设置-商品设置-设置失败");

			salemenuSkuFilterParam.setExport(1);
			String file_path = categoryService.exportSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(file_path, null, "导出报价单里的商品信息失败");

			File file = new File(file_path);

			List<SkuUpdateModel> skuUpdateModels = EasyExcelFactory.read(file).head(SkuUpdateModel.class).sheet(0)
					.doReadSync();
			Assert.assertEquals(skuUpdateModels.size() > 0, true, "报价单 " + salemenu_id + "里无商品");

			// 取一个销售SKU进行信息修改
			SkuUpdateModel skuUpdateModel = NumberUtil.roundNumberInList(skuUpdateModels);

			String spu_id = skuUpdateModel.getSpu_id();
			String sku_id = skuUpdateModel.getSku_id();
			sku_before = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku_before, null, "获取销售SKU" + sku_id + "详细信息失败");

			String new_sku_name = skuUpdateModel.getSku_name() + "AT";
			skuUpdateModel.setSku_name(new_sku_name);
			String new_desc = StringUtil.getRandomNumber(12);
			skuUpdateModel.setDesc(new_desc);
			skuUpdateModel.setIs_price_timing(1);
			skuUpdateModel.setState(0);
			double new_unit_price = 2.4;
			skuUpdateModel.setUnit_price(new_unit_price);

			double new_attrition_rate = 12;
			skuUpdateModel.setAttrition_rate(new_attrition_rate);

			double new_sale_num_least = 2;
			skuUpdateModel.setSale_num_least(new_sale_num_least);
			skuUpdateModel.setIs_weigh(0);

			String outer_id = "AT" + StringUtil.getRandomNumber(5);
			skuUpdateModel.setOuter_id(outer_id);

			EasyExcel.write(file, SkuUpdateModel.class).sheet(0, "SKU").doWrite(skuUpdateModels);

			JSONObject retJson = categoryService.importUpdateSkus(station_id, salemenu_id, file_path);
			Assert.assertEquals(retJson.getInteger("code") == 0, true, "报价单批量导入修改失败");

			int failed = retJson.getJSONObject("data").getInteger("failed");
			if (failed > 0) {
				String link = retJson.getJSONObject("data").getString("link");
				ReporterCSS.warn("批量导入修改SKU存在失败条目,清下载Excel查看具体原因" + link);
				logger.warn("批量导入修改SKU存在失败条目,清下载Excel查看具体原因" + link);
			}
			Assert.assertEquals(failed, 0, "批量导入修改销售SKU,存在失败条目");

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
			logger.error("批量导入修改报价单商品遇到错误: ", e);
			Assert.fail("批量导入修改报价单商品遇到错误: ", e);
		} finally {
			ReporterCSS.step("后置处理");
			if (sku_before != null) {
				try {
					boolean result = categoryService.updateSaleSku(sku_before);
					Assert.assertEquals(result, true, "修改销售SKU信息失败");
				} catch (Exception e) {
					logger.error("修改销售SKU信息遇到错误: ", e);
					Assert.fail("修改销售SKU信息遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void importCreateSkusTestCase01() {
		ReporterCSS.title("测试点: 报价单里批量导入新建销售SKU");
		List<SalemenuSkuBean> salemenuSkus = null;
		try {
			InitDataBean initData = getInitData();
			String supplier_customer_id = "SKU";
			String spu_id = initData.getSpu().getSpu_id();
			String salemenu_id = initData.getSalemenu().getId();

			SkuCreateModel skuCreateModel = new SkuCreateModel();
			skuCreateModel.setSpu_id(spu_id);
			String outer_id = "AT" + StringUtil.getRandomNumber(5);
			String sku_name = "AT_" + StringUtil.getRandomString(5).toUpperCase();
			String desc = StringUtil.getRandomString(12);
			double unit_price = 2;
			int is_price_timing = 0;
			String sale_unit_name = "包";
			double sale_ratio = 2;
			double attrition_rate = 0.12;
			double sale_num_least = 2;
			int state = 0;
			int is_weigh = 0;
			skuCreateModel.setOuter_id(outer_id);
			skuCreateModel.setSku_name(sku_name);
			skuCreateModel.setDesc(desc);
			skuCreateModel.setUnit_price(unit_price);
			skuCreateModel.setIs_price_timing(is_price_timing);
			skuCreateModel.setSale_unit_name(sale_unit_name);
			skuCreateModel.setSale_ratio(sale_ratio);
			skuCreateModel.setAttrition_rate(attrition_rate);
			skuCreateModel.setSale_num_least(sale_num_least);
			skuCreateModel.setState(state);
			skuCreateModel.setIs_weigh(is_weigh);
			skuCreateModel.setSupplier_customer_id(supplier_customer_id);

			List<SkuCreateModel> skuCreateModels = new ArrayList<SkuCreateModel>();
			skuCreateModels.add(skuCreateModel);

			String dir_path = System.getProperty("user.dir") + "/temp";
			String file_path = dir_path + "/temp.xlsx";
			ReporterCSS.log("文件路径: " + file_path);
			logger.info("文件路径: " + file_path);
			File dir = new File(dir_path);
			File file = new File(file_path);
			if (!dir.exists() && !dir.isDirectory()) {
				boolean result = dir.mkdir();
				Assert.assertEquals(result, true, "创建文件路径 " + dir_path + " 失败");
			}
			if (!file.exists()) {
				boolean result = file.createNewFile();
				Assert.assertEquals(result, true, "创建文件 " + file_path + " 失败");
			}
			EasyExcel.write(file, SkuCreateModel.class).sheet(0, "SKU").doWrite(skuCreateModels);

			boolean result = categoryService.importCreateSkus(salemenu_id, group_id, station_id, file_path);
			Assert.assertEquals(result, true, "批量导入新建销售SKU失败");

			SalemenuSkuFilterParam salemenuSkuFilterParam = new SalemenuSkuFilterParam();
			salemenuSkuFilterParam.setText("AT_");
			salemenuSkuFilterParam.setSalemenu_id(salemenu_id);

			salemenuSkus = categoryService.searchSkuInSalemenu(salemenuSkuFilterParam);
			Assert.assertNotEquals(salemenuSkus, null, "报价单里搜索过滤销售SKU,搜索失败");

			SalemenuSkuBean salemenuSku = salemenuSkus.stream()
					.filter(s -> s.getSku_name().equals(sku_name) && s.getOuter_id().equals(outer_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(salemenuSku, null, "批量导入的销售商品在报价单里没有找到");

			String msg = null;
			String sku_id = salemenuSku.getSku_id();
			SkuBean sku = categoryService.getSaleSkuById(spu_id, sku_id);
			Assert.assertNotEquals(sku, null, "获取销售SKU " + sku_id + "相信信息失败");

			if (sku.getIs_price_timing() != is_price_timing) {
				msg = String.format("批量导入新建销售SKU %s,是否时价状态与预期不符,预期:%s,实际:%s", sku_id, is_price_timing,
						sku.getIs_price_timing());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku.getState() != state) {
				msg = String.format("批量导入新建销售SKU %s,销售状态值与预期不符,预期:%s,实际:%s", sku_id, state, sku.getState());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku.getStd_sale_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP)
					.compareTo(new BigDecimal(String.valueOf(unit_price))) != 0) {
				msg = String.format("批量导入新建销售SKU %s,销售单价与预期不符,预期:%s,实际:%s", sku_id, unit_price,
						sku.getStd_sale_price().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!sku.getDesc().equals(desc)) {
				msg = String.format("批量导入新建销售SKU %s,描述信息与预期不符,预期:%s,实际:%s", sku_id, desc, sku.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku.getAttrition_rate().compareTo(new BigDecimal(String.valueOf(attrition_rate))) != 0) {
				msg = String.format("批量导入新建销售SKU %s,损耗比例与预期不符,预期:%s,实际:%s", sku_id, attrition_rate,
						sku.getAttrition_rate());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku.getSale_num_least().compareTo(new BigDecimal(String.valueOf(sale_num_least))) != 0) {
				msg = String.format("批量导入新建销售SKU %s,最小小单数与预期不符,预期:%s,实际:%s", sku_id, sale_num_least,
						sku.getSale_num_least());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (sku.getIs_weigh() != is_weigh) {
				msg = String.format("批量导入新建销售SKU %s,是否称重与预期不符,预期:%s,实际:%s", sku_id, is_weigh, sku.getIs_weigh());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "批量新建,新建的结果与预期不符");

		} catch (Exception e) {
			logger.error("批量导入新建销售SKU遇到错误: ", e);
			Assert.fail("批量导入新建销售SKU遇到错误: ", e);
		} finally {
			ReporterCSS.step("后置处理,删除新建的销售SKU");

			if (salemenuSkus != null) {
				try {
					String sku_id = null;
					boolean result = true;
					for (SalemenuSkuBean salemenuSku : salemenuSkus) {
						sku_id = salemenuSku.getSku_id();
						result = categoryService.deleteSaleSku(sku_id);
						Assert.assertEquals(result, true, "删除销售SKU" + sku_id + "失败");
					}
				} catch (Exception e) {
					logger.error("删除新建销售SKU遇到错误: ", e);
					Assert.fail("删除新建销售SKU遇到错误: ", e);
				}
			}

		}
	}
}
