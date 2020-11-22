package cn.guanmai.station.invoicing;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.ShelfBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuBean;
import cn.guanmai.station.bean.invoicing.ShelfSpuStockBean;
import cn.guanmai.station.bean.invoicing.ShelfStockBatchBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.ShelfSpuFilterParam;
import cn.guanmai.station.bean.invoicing.param.ShelfStockBatchFilterParam;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.ShelfServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.ShelfService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public class ShelfTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ShelfTest.class);
	private ShelfService shelfService;
	private StockCheckService stockCheckService;
	private InStockService stockInService;
	private StockService stockService;
	private InitDataBean initData;
	private List<ShelfBean> shelflist;
	private List<ShelfBean.Shelf> shelfDetails;
	private String bash_shelf_name = "冷藏";
	private String bash_shelf_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		initData = getInitData();
		shelfService = new ShelfServiceImpl(headers);
		stockInService = new InStockServiceImpl(headers);
		stockService = new StockServiceImpl(headers);

		stockCheckService = new StockCheckServiceImpl(headers);
		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			JSONArray permissions = loginUserInfo.getUser_permission();

			Assert.assertNotEquals(permissions, null, "获取站点权限信息失败");
			Assert.assertEquals(permissions.contains("get_shelf"), true, "登录用户没有货位管理权限");

			shelflist = shelfService.getShelf();
			Assert.assertNotEquals(shelflist, null, "货位接口信息获取失败");

			if (shelflist.size() == 0) {
				bash_shelf_id = shelfService.addShelf(bash_shelf_name);
				Assert.assertNotEquals(bash_shelf_id, null, "新建货位失败");
			} else {
				ShelfBean shelf = shelflist.stream().filter(s -> s.getLevel() == 1).findAny().orElse(null);
				Assert.assertNotEquals(shelf, null, "货位列表没有level值为1的货位,与预期不符");
				ShelfBean.Shelf shelfDetail = shelf.getShelfs().stream()
						.filter(s -> s.getName().equals(bash_shelf_name)).findAny().orElse(null);
				if (shelfDetail == null) {
					bash_shelf_id = shelfService.addShelf(bash_shelf_name);
					Assert.assertNotEquals(bash_shelf_id, null, "新建货位失败");
				} else {
					bash_shelf_id = shelfDetail.getShelf_id();
				}
			}
		} catch (Exception e) {
			logger.error("货位管理初始化数据遇到错误: ", e);
			Assert.fail("货位管理初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase01() {
		boolean find = false;
		String shelf_id = null;
		try {
			ReporterCSS.title("测试点: 新增元货位");
			String paramName = StringUtil.getRandomString(6);

			shelf_id = shelfService.addShelf(paramName);
			Assert.assertNotEquals(shelf_id, null, "添加货位失败");

			shelflist = shelfService.getShelf();
			Assert.assertNotEquals(shelflist, null, "货位信息获取失败");

			OK: for (ShelfBean shelf : shelflist) {
				if (shelf.getLevel() == 1) {
					shelfDetails = shelf.getShelfs();
					for (ShelfBean.Shelf shelfDetail : shelfDetails) {
						if (shelfDetail.getName().equals(paramName)) {
							shelf_id = shelfDetail.getShelf_id();
							find = true;
							break OK;
						}
					}
				}
			}
			Assert.assertEquals(find, true, "添加货位没有找到");
		} catch (Exception e) {
			logger.error("新增货位遇到错误: ", e);
			Assert.fail("新增货位遇到错误: ", e);
		} finally {
			if (find) {
				if (shelf_id != null) {
					try {
						boolean result = shelfService.deleteShelf(shelf_id);
						Assert.assertEquals(result, true, "后置处理,删除货位失败");
					} catch (Exception e) {
						logger.error("后置处理,删除新增的货位遇到错误: ", e);
						Assert.fail("后置处理,删除新增的货位遇到错误: ", e);
					}
				}
			}
		}
	}

	@Test
	public void shelfTestCase02() {
		String shelf_id = null;
		try {
			ReporterCSS.title("测试点: 新增子货位");
			String shelf_name = StringUtil.getRandomNumber(6);
			shelf_id = shelfService.addShelf(bash_shelf_id, shelf_name);
			Assert.assertNotEquals(shelf_id, null, "新增子货位失败");

			List<ShelfBean> shelfs = shelfService.getShelf();
			Assert.assertNotEquals(shelfs, null, "获取货位列表失败");

			ShelfBean shelf = shelfs.stream().filter(s -> s.getLevel() == 2).findAny().orElse(null);
			Assert.assertNotEquals(shelf, null, "没有货位等级为2的货位列表,与预期不符");

			ShelfBean.Shelf shelfDetail = shelf.getShelfs().stream()
					.filter(s -> s.getName().equals(shelf_name) && s.getParent_id().equals(bash_shelf_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(shelfDetail, null, "新增的子货位没有在货位列表中找到");
		} catch (Exception e) {
			logger.error("新增货位遇到错误: ", e);
			Assert.fail("新增货位遇到错误: ", e);
		} finally {
			if (shelf_id != null) {
				try {
					boolean result = shelfService.deleteShelf(shelf_id);
					Assert.assertEquals(result, true, "后置处理,删除货位失败");
				} catch (Exception e) {
					logger.error("后置处理,删除新增的货位遇到错误: ", e);
					Assert.fail("后置处理,删除新增的货位遇到错误: ", e);
				}
			}

		}
	}

	@Test
	public void shelfTestCase03() {
		ReporterCSS.title("测试点: 新增同级同名货位,断言失败");
		try {
			String shelf_id = shelfService.addShelf(bash_shelf_name);
			Assert.assertEquals(shelf_id, null, "新增同级同名货位,断言失败");
		} catch (Exception e) {
			logger.error("新增货位遇到错误: ", e);
			Assert.fail("新增货位遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase04() {
		ReporterCSS.title("测试点: 修改货位名称");
		String shelf_id = null;
		try {
			String shelf_name = StringUtil.getRandomNumber(6);

			shelf_id = shelfService.addShelf(shelf_name);
			Assert.assertNotEquals(shelf_id, null, "新增货位失败");

			String new_shelf_name = StringUtil.getRandomNumber(6);

			boolean result = shelfService.modifyShelf(new_shelf_name, shelf_id);
			Assert.assertEquals(result, true, "修改货位名称失败");

			List<ShelfBean> shelfs = shelfService.getShelf();
			Assert.assertNotEquals(shelfs, null, "获取货位列表失败");

			ShelfBean shelf = shelfs.stream().filter(s -> s.getLevel() == 1).findAny().orElse(null);
			Assert.assertNotEquals(shelf, null, "没有货位等级为1的货位列表,与预期不符");

			ShelfBean.Shelf shelfDetail = shelf.getShelfs().stream().filter(s -> s.getName().equals(new_shelf_name))
					.findAny().orElse(null);
			Assert.assertNotEquals(shelfDetail, null, "用修改的货位名称搜寻,没有找到修改名称后的货位");
		} catch (Exception e) {
			logger.error("修改货位名称遇到错误: ", e);
			Assert.fail("修改货位名称遇到错误: ", e);
		} finally {
			if (shelf_id != null) {
				try {
					boolean result = shelfService.deleteShelf(shelf_id);
					Assert.assertEquals(result, true, "后置处理,删除货位失败");
				} catch (Exception e) {
					logger.error("删除货位遇到错误: ", e);
					Assert.fail("删除货位遇到错误: ", e);
				}
			}
		}
	}

	@Test
	public void shelfTestCase05() {
		ReporterCSS.title("测试点: 删除货位");
		String shelf_id = null;
		try {
			String shelf_name = StringUtil.getRandomNumber(6);

			shelf_id = shelfService.addShelf(shelf_name);
			Assert.assertNotEquals(shelf_id, null, "新增货位失败");

			boolean result = shelfService.deleteShelf(shelf_id);
			Assert.assertEquals(result, true, "删除货位失败");

			List<ShelfBean> shelfs = shelfService.getShelf();
			Assert.assertNotEquals(shelfs, null, "获取货位列表失败");

			ShelfBean shelf = shelfs.stream().filter(s -> s.getLevel() == 1).findAny().orElse(null);
			Assert.assertNotEquals(shelf, null, "没有货位等级为1的货位列表,与预期不符");

			ShelfBean.Shelf shelfDetail = shelf.getShelfs().stream().filter(s -> s.getName().equals(shelf_name))
					.findAny().orElse(null);
			Assert.assertEquals(shelfDetail, null, "货位没有删除成功,还可以在货位列表找的到");
		} catch (Exception e) {
			logger.error("货位删除遇到错误: ", e);
			Assert.fail("货位删除遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase06() {
		ReporterCSS.title("测试点: 获取未分配货位的商品库存统计");
		try {
			JSONObject retObj = shelfService.getShelfSpuStockSummaryByShelf("-1");
			Assert.assertNotEquals(retObj, null, "获取未分配货位的商品库存统计失败");
		} catch (Exception e) {
			logger.error("获取未分配货位的商品库存统计遇到错误: ", e);
			Assert.fail("获取未分配货位的商品库存统计遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase07() {
		ReporterCSS.title("测试点: 获取未分配货位的正库存商品列表");
		try {
			ShelfSpuFilterParam shelfSpuFilterParam = new ShelfSpuFilterParam();
			shelfSpuFilterParam.setShelf_id("-1");
			shelfSpuFilterParam.setLimit(40);

			List<ShelfSpuBean> shelfSpuList = shelfService.queryShelfSpu(shelfSpuFilterParam);

			Assert.assertNotEquals(shelfSpuList, null, "获取未分配货位的正库存商品列表失败");
		} catch (Exception e) {
			logger.error("获取未分配货位的正库存商品列表遇到错误: ", e);
			Assert.fail("获取未分配货位的正库存商品列表遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase08() {
		ReporterCSS.title("测试点: 获取未分配货位的负库存商品列表");
		try {
			ShelfSpuFilterParam shelfSpuFilterParam = new ShelfSpuFilterParam();
			shelfSpuFilterParam.setShelf_id("-1");
			shelfSpuFilterParam.setLimit(40);

			List<ShelfSpuBean> shelfSpuList = shelfService.queryShelfNegativeSpu(shelfSpuFilterParam);

			Assert.assertNotEquals(shelfSpuList, null, "获取未分配货位的负库存商品列表失败");
		} catch (Exception e) {
			logger.error("获取未分配货位的负库存商品列表遇到错误: ", e);
			Assert.fail("获取未分配货位的负库存商品列表遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase09() {
		ReporterCSS.title("测试点: 获取货位批次库存信息");
		try {
			ShelfSpuFilterParam shelfSpuFilterParam = new ShelfSpuFilterParam();
			shelfSpuFilterParam.setShelf_id("-1");
			shelfSpuFilterParam.setLimit(40);

			List<ShelfSpuBean> shelfSpuList = shelfService.queryShelfSpu(shelfSpuFilterParam);

			Assert.assertNotEquals(shelfSpuList, null, "获取未分配货位的负库存商品列表失败");

			Assert.assertEquals(shelfSpuList.size() >= 1, true, "没有未分配货位的商品列表,无法进行后续操作");

			ShelfSpuBean shelfSpu = NumberUtil.roundNumberInList(shelfSpuList);
			String spu_id = shelfSpu.getSpu_id();

			ShelfStockBatchFilterParam shelfStockBatchFilterParam = new ShelfStockBatchFilterParam();
			shelfStockBatchFilterParam.setSpu_id(spu_id);
			shelfStockBatchFilterParam.setShelf_id("-1");

			List<ShelfStockBatchBean> shelfStockBatchList = shelfService
					.queryShelfStockBatch(shelfStockBatchFilterParam);

			Assert.assertNotEquals(shelfStockBatchList, null, "获取未分配货位的商品批次列表失败");
		} catch (Exception e) {
			logger.error("获取未分配货位的商品批次列表遇到错误: ", e);
			Assert.fail("获取未分配货位的商品批次列表遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase10() {
		ReporterCSS.title("测试点: 获取指定SPU在所有货位上的库存总计");
		try {
			List<SpuStockBean> spuStockList = stockCheckService.searchStockCheck(new StockCheckFilterParam());
			Assert.assertNotEquals(spuStockList, null, "获取SPU库存列表失败");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStockList);

			String spu_id = spuStock.getSpu_id();

			JSONObject retObj = shelfService.getShelfSpuStockSummaryBySpu(spu_id);
			Assert.assertNotEquals(retObj, null, "获取指定SPU在所有货位上的库存总计");
		} catch (Exception e) {
			logger.error("获取指定SPU在所有货位上的库存总计遇到错误: ", e);
			Assert.fail("获取指定SPU在所有货位上的库存总计遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase11() {
		ReporterCSS.title("测试点: 获取指定SPU在货位上的所有批次信息");
		try {
			List<SpuStockBean> spuStockList = stockCheckService.searchStockCheck(new StockCheckFilterParam());
			Assert.assertNotEquals(spuStockList, null, "获取SPU库存列表失败");

			SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStockList);

			String spu_id = spuStock.getSpu_id();

			List<ShelfSpuStockBean> shelfSpuStockList = shelfService.getShelfSpuStockInfo(spu_id);
			Assert.assertNotEquals(shelfSpuStockList, null, "获取指定SPU在所有货位上的库存情况失败");

			if (shelfSpuStockList.size() > 0) {
				ShelfSpuStockBean shelfSpuStock = NumberUtil.roundNumberInList(shelfSpuStockList);
				String shelf_id = shelfSpuStock.getShelf_stock_list().get(0).getShelf_id();

				ShelfStockBatchFilterParam shelfStockBatchFilterParam = new ShelfStockBatchFilterParam();
				shelfStockBatchFilterParam.setSpu_id(spu_id);
				shelfStockBatchFilterParam.setShelf_id(shelf_id);
				List<ShelfStockBatchBean> shelfStockBatchList = shelfService
						.queryShelfStockBatch(shelfStockBatchFilterParam);
				Assert.assertNotEquals(shelfStockBatchList, null, "获取指定SPU在指定货位的所有批次信息失败");
			}
		} catch (Exception e) {
			logger.error("获取未分配货位的商品批次列表遇到错误: ", e);
			Assert.fail("获取未分配货位的商品批次列表遇到错误: ", e);
		}
	}

	@Test
	public void shelfTestCase12() {
		ReporterCSS.title("测试点: 入库商品存放货位");
		try {
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = stockInService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = stockInService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockInDetail.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(shares);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("a", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = stockInService.getSupplieraveragePrice(purchaseSpec_id, supplier_id);
				Assert.assertNotEquals(supplier_avg_price, null,
						"获取采购规格 " + purchaseSpec_id + " 在此供应商 " + supplier_id + " 对应的入库均价失败");

				String parchase_name = supplySku.getSku_name();
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();
				InStockDetailInfoBean.Detail detail = stockInDetail.new Detail();

				detail.setBatch_number(sheet_id + "-" + nf.format(batch_num));
				detail.setName(parchase_name);
				String displayName = parchase_name + "(" + purchase_ratio + parchase_std_unit + "/" + purchase_unit
						+ ")";
				detail.setDisplayName(displayName);
				detail.setId(purchaseSpec_id);
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setPurchase_unit(purchase_unit);
				detail.setStd_unit(parchase_std_unit);
				detail.setRatio(purchase_ratio);
				BigDecimal quantity = NumberUtil.getRandomNumber(10, 20, 1);
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.divide(purchase_ratio, 4, BigDecimal.ROUND_HALF_UP));

				// 有入库均价就取均价,没有就随机取
				BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
						: NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
				detail.setMoney(money);
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);

				// 存放货位
				detail.setShelf_id(new BigDecimal(bash_shelf_id));
				detail.setShelf_name(bash_shelf_name);
				details.add(detail);
				if (batch_num >= 4) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money.setScale(2, BigDecimal.ROUND_HALF_UP));
			stockInDetail.setIs_submit(2);

			boolean result = stockInService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "提交采购入库单失败");
		} catch (Exception e) {
			logger.error("入库商品存放货位遇到错误: ", e);
			Assert.fail("入库商品存放货位遇到错误: ", e);
		}
	}
}
