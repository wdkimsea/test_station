package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.InStockDetailInfoBean;
import cn.guanmai.station.bean.invoicing.InStockSheetBean;
import cn.guanmai.station.bean.invoicing.StockSettleSupplierBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.StockCheckFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.InStockSheetImportParam;
import cn.guanmai.station.bean.purchase.PurchaseSpecRefPriceBean;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.InStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.InStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.purchase.PurchaseService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 18, 2019 3:43:45 PM 
* @des 进销存成品入库相关测试用例
* @version 1.0 
*/
public class InStockTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(InStockTest.class);
	private Map<String, String> headers;
	private InStockService inStockService;
	private StockService stockService;
	private StockCheckService stockCheckService;
	private LoginUserInfoService loginUserInfoService;
	private SupplierService supplierService;
	private PurchaseService purchaseService;
	private InitDataBean initData;
	private String station_id;
	private InStockSheetFilterParam inStockSheetFilterParam;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String start_date_new;
	private String end_date_new;

	@BeforeClass
	public void initData() {
		headers = getStationCookie();
		inStockService = new InStockServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		purchaseService = new PurchaseServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		try {
			start_date_new = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00");
			end_date_new = TimeUtil.calculateTime("yyyy-MM-dd 00:00", start_date_new, 1, Calendar.DATE);
			initData = getInitData();

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录账户信息失败");

			station_id = loginUserInfo.getStation_id();

			Assert.assertNotEquals(initData, null, "初始化站点数据失败");
		} catch (Exception e) {
			logger.error("初始化站点数据遇到错误: ", e);
			Assert.fail("初始化站点数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		inStockSheetFilterParam = new InStockSheetFilterParam();
		inStockSheetFilterParam.setType(2);
		inStockSheetFilterParam.setStatus(5);
		inStockSheetFilterParam.setStart(todayStr);
		inStockSheetFilterParam.setEnd(todayStr);
		inStockSheetFilterParam.setOffset(0);
		inStockSheetFilterParam.setLimit(10);
	}

	@Test
	public void createInStockSheetTestCase01() {
		try {
			ReporterCSS.title("测试点: 创建采购入库单");
			List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(supplierList, null, "查询站点供应商列表遇到错误");
			Assert.assertEquals(supplierList.size() > 0, true, "此站点无供应商,无法进行创建采购入库单操作");

			SupplierDetailBean supplier = NumberUtil.roundNumberInList(supplierList);
			String id = inStockService.createInStockSheet(supplier.getId(), supplier.getName());
			Assert.assertNotEquals(id, null, "创建采购入库单失败");

			inStockSheetFilterParam.setSearch_text(id);
			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertNotEquals(inStockSheetList, null, "搜索过滤采购入库单失败");

			InStockSheetBean stockInSheet = inStockSheetList.stream().filter(s -> s.getId().equals(id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(stockInSheet, null, "新建的采购入库单搜索过滤没有找到");
		} catch (Exception e) {
			logger.error("创建采购入库单的时候遇到错误: ", e);
			Assert.fail("创建采购入库单的时候遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase02() {
		try {
			ReporterCSS.title("测试点: 编辑提交采购入库单");
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockInDetail.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(shares);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id, supplier_id);
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

				// 有入库均价就去均价,没有就随机取
				BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
						: NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
				detail.setMoney(money);
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
				if (batch_num >= 10) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money.setScale(2, BigDecimal.ROUND_HALF_UP));
			stockInDetail.setIs_submit(2);

			boolean result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "提交采购入库单失败");

			InStockDetailInfoBean temp_stock_in_detail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(temp_stock_in_detail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (temp_stock_in_detail.getSku_money().compareTo(stockInDetail.getSku_money()) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", stockInDetail.getSku_money(),
						temp_stock_in_detail.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (temp_stock_in_detail.getStatus() != 2) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 2, temp_stock_in_detail.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (!temp_stock_in_detail.getRemark().equals(stockInDetail.getRemark())) {
				msg = String.format("采购入库单统计的备注与预期的不一致,预期:%s,实际:%s", stockInDetail.getRemark(),
						temp_stock_in_detail.getRemark());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Detail> detailResults = temp_stock_in_detail.getDetails();

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> actual_skus = detailResults.stream().map(d -> d.getId()).collect(Collectors.toList());
			if (!expected_skus.equals(actual_skus)) {
				msg = String.format("采购入库单入库商品排序与预期不一致,预期:%s,实际:%s", expected_skus, actual_skus);
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockDetailInfoBean.Detail detail : details) {
				String purchase_id = detail.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (detail.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getUnit_price().compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id,
							detail.getUnit_price(), detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getMoney().compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getMoney(),
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "创建采购入库单编辑填写的信息与创建后查询到的不一致");
		} catch (Exception e) {
			logger.error("创建采购入库单+验证信息的过程中遇到错误: ", e);
			Assert.fail("创建采购入库单+验证信息的过程中遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase03() {
		try {
			ReporterCSS.title("测试点: 编辑提交采购入库单,并加入费用分摊");
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockInDetail.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> sharesList = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(sharesList);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();
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
				BigDecimal unit_price = NumberUtil.getRandomNumber(5, 10, 1);
				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity));
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(quantity.multiply(unit_price));
				details.add(detail);
				if (batch_num >= 10) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setIs_submit(1);
			stockInDetail.setStatus(0);
			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money);

			boolean result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "采购入库单保存草稿失败");

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());

			InStockDetailInfoBean.Share share = stockInDetail.new Share();
			share.setAction(1);
			share.setMethod(1);
			share.setReason(1);
			BigDecimal share_money = new BigDecimal("20");
			share.setMoney(share_money);
			share.setIn_sku_logs(JSONArray.parseArray(JSON.toJSONString(expected_skus)));
			sharesList.add(share);
			result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "采购入库添加费用分摊失败");

			stockInDetail.setSku_money(sku_money.add(share_money));
			stockInDetail.setIs_submit(2);

			result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "采购入库单添加费用分摊后提交保存失败");

			InStockDetailInfoBean temp_stock_in_detail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(temp_stock_in_detail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (temp_stock_in_detail.getSku_money().compareTo(sku_money.add(share_money)) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", sku_money.add(share_money),
						temp_stock_in_detail.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (temp_stock_in_detail.getStatus() != 2) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 2, temp_stock_in_detail.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Share> temp_share_list = temp_stock_in_detail.getShares();
			if (temp_share_list.size() != 1) {
				msg = String.format("采购入库单添加的费用分摊在获取的采购入库单详细中没有找到");
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			} else {
				InStockDetailInfoBean.Share temp_share = temp_share_list.get(0);
				if (temp_share.getAction() != share.getAction()) {
					msg = String.format("采购入库单添加的费用分摊类型与预期的不一致,预期:%s,实际:%s", share.getAction(), temp_share.getAction());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_share.getMethod() != share.getMethod()) {
					msg = String.format("采购入库单添加的费用分摊方式与预期的不一致,预期:%s,实际:%s", share.getMethod(), temp_share.getMethod());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_share.getReason() != share.getReason()) {
					msg = String.format("采购入库单添加的费用分摊原因与预期的不一致,预期:%s,实际:%s", share.getReason(), temp_share.getReason());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (!temp_share.getIn_sku_logs().toString().equals(share.getIn_sku_logs().toString())) {
					msg = String.format("采购入库单添加的费用分摊商品列表与预期的不一致,预期:%s,实际:%s", share.getIn_sku_logs().toString(),
							temp_share.getIn_sku_logs().toString());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			List<InStockDetailInfoBean.Detail> detailResults = temp_stock_in_detail.getDetails();
			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockDetailInfoBean.Detail detail : details) {
				String purchase_id = detail.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (detail.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				BigDecimal expected_money = detail.getMoney()
						.add(share_money.multiply(detail.getMoney().divide(sku_money, 6, BigDecimal.ROUND_HALF_UP))
								.setScale(2, BigDecimal.ROUND_HALF_UP));

				if (expected_money.compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, expected_money,
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				BigDecimal expected_unit_price = expected_money.divide(detail.getQuantity(), 2,
						BigDecimal.ROUND_HALF_UP);

				if (expected_unit_price.compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id, expected_unit_price,
							detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "采购入库单添加费用分摊后显示的数据与预期的不一致");
		} catch (Exception e) {
			logger.error("创建采购入库单+验证信息的过程中遇到错误: ", e);
			Assert.fail("创建采购入库单+验证信息的过程中遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase04() {
		try {
			ReporterCSS.title("测试点: 编辑提交采购入库单,并添加金额折让");
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			stockInDetail.setIs_submit(2);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(shares);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();

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

				// 有入库均价就去均价,没有就随机取
				BigDecimal unit_price = NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP));
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(quantity.multiply(unit_price));
				details.add(detail);
				if (batch_num >= 10) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money.setScale(2, BigDecimal.ROUND_HALF_UP));

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			InStockDetailInfoBean.Discount discount = stockInDetail.new Discount();
			discount.setAction(1);
			BigDecimal discount_money = new BigDecimal("20");
			discount.setMoney(discount_money);
			discount.setReason(1);
			discount.setRemark(StringUtil.getRandomString(6));
			discounts.add(discount);
			stockInDetail.setDiscounts(discounts);
			stockInDetail.setDelta_money(discount_money);

			boolean result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "提交采购入库单失败");

			InStockDetailInfoBean temp_stock_in_detail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(temp_stock_in_detail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (temp_stock_in_detail.getSku_money().compareTo(stockInDetail.getSku_money()) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", stockInDetail.getSku_money(),
						temp_stock_in_detail.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (temp_stock_in_detail.getStatus() != 2) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 2, temp_stock_in_detail.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (!temp_stock_in_detail.getRemark().equals(stockInDetail.getRemark())) {
				msg = String.format("采购入库单的备注与预期的不一致,预期:%s,实际:%s", stockInDetail.getRemark(),
						temp_stock_in_detail.getRemark());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (temp_stock_in_detail.getDelta_money().compareTo(stockInDetail.getDelta_money()) != 0) {
				msg = String.format("采购入库单的折让金额与预期的不一致,预期:%s,实际:%s", stockInDetail.getDelta_money(),
						temp_stock_in_detail.getDelta_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Discount> temp_discounts = temp_stock_in_detail.getDiscounts();
			if (temp_discounts.size() != 1) {
				msg = String.format("采购入库单添加的金额折让在采购入库单详细页面没有查询到");
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			} else {
				InStockDetailInfoBean.Discount temp_discount = temp_discounts.get(0);
				if (temp_discount.getAction() != discount.getAction()) {
					msg = String.format("采购入库单添加的费用分摊的折让类型与预期不一致,预期:%s,实际:%s", discount.getAction(),
							temp_discount.getAction());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_discount.getReason() != discount.getReason()) {
					msg = String.format("采购入库单添加的费用分摊的折让原因与预期不一致,预期:%s,实际:%s", discount.getReason(),
							temp_discount.getReason());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_discount.getMoney().compareTo(discount.getMoney()) != 0) {
					msg = String.format("采购入库单添加的费用分摊的折让金额与预期不一致,预期:%s,实际:%s", discount.getMoney(),
							temp_discount.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (!temp_discount.getRemark().equals(discount.getRemark())) {
					msg = String.format("采购入库单添加的费用分摊的折让备注与预期不一致,预期:%s,实际:%s", discount.getRemark(),
							temp_discount.getRemark());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

			}

			List<InStockDetailInfoBean.Detail> detailResults = temp_stock_in_detail.getDetails();

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> actual_skus = detailResults.stream().map(d -> d.getId()).collect(Collectors.toList());
			if (!expected_skus.equals(actual_skus)) {
				msg = String.format("采购入库单入库商品排序与预期不一致,预期:%s,实际:%s", expected_skus, actual_skus);
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockDetailInfoBean.Detail detail : details) {
				String purchase_id = detail.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (detail.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getUnit_price().compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id,
							detail.getUnit_price(), detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getMoney().compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getMoney(),
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "创建采购入库单编辑填写的信息与创建后查询到的不一致");
		} catch (Exception e) {
			logger.error("创建采购入库单+验证信息的过程中遇到错误: ", e);
			Assert.fail("创建采购入库单+验证信息的过程中遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase05() {
		try {
			ReporterCSS.title("测试点: 入库单审核不通过");

			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			stockInDetail.setIs_submit(2);

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockInDetail.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(shares);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id, supplier_id);
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

				// 有入库均价就去均价,没有就随机取
				BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
						: NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
				detail.setMoney(money);
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
				if (batch_num >= 5) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money.setScale(2, BigDecimal.ROUND_HALF_UP));

			boolean result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "提交采购入库单失败");

			result = inStockService.reviewInStockSheet(sheet_id);
			Assert.assertEquals(result, true, "采购入库单审核不通过失败");

			InStockDetailInfoBean temp_stock_in_detail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(temp_stock_in_detail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (temp_stock_in_detail.getSku_money().compareTo(stockInDetail.getSku_money()) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", stockInDetail.getSku_money(),
						temp_stock_in_detail.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (temp_stock_in_detail.getStatus() != 0) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 0, temp_stock_in_detail.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (!temp_stock_in_detail.getRemark().equals(stockInDetail.getRemark())) {
				msg = String.format("采购入库单统计的备注与预期的不一致,预期:%s,实际:%s", stockInDetail.getRemark(),
						temp_stock_in_detail.getRemark());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Detail> detailResults = temp_stock_in_detail.getDetails();

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> actual_skus = detailResults.stream().map(d -> d.getId()).collect(Collectors.toList());
			if (!expected_skus.equals(actual_skus)) {
				msg = String.format("采购入库单入库商品排序与预期不一致,预期:%s,实际:%s", expected_skus, actual_skus);
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockDetailInfoBean.Detail detail : details) {
				String purchase_id = detail.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				if (detail.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getUnit_price().compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id,
							detail.getUnit_price(), detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getMoney().compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getMoney(),
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detail.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}
			Assert.assertEquals(result, true, "创建采购入库单编辑填写的信息与创建后查询到的不一致");
		} catch (Exception e) {
			logger.error("入库单审核不通过操作过程中遇到错误: ", e);
			Assert.fail("入库单审核不通过操作过程中遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase06() {
		try {
			ReporterCSS.title("测试点: 采购入库单冲销");
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockInDetail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			stockInDetail.setIs_submit(2);

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockInDetail.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockInDetail.setShares(shares);

			stockInDetail.setRemark(StringUtil.getRandomString(10));

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			int batch_num = 1;
			BigDecimal sku_money = BigDecimal.ZERO;
			NumberFormat nf = new DecimalFormat("00000");
			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			for (SupplySkuBean supplySku : supplySkus) {
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id, supplier_id);
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

				// 有入库均价就去均价,没有就随机取
				BigDecimal unit_price = supplier_avg_price.compareTo(BigDecimal.ZERO) == 1 ? supplier_avg_price
						: NumberUtil.getRandomNumber(5, 10, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				detail.setMoney(unit_price.multiply(quantity));
				detail.setOperator("自动化");
				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(quantity.multiply(unit_price));
				details.add(detail);
				if (batch_num >= 5) {
					break;
				}
				batch_num += 1;
			}

			stockInDetail.setDetails(details);
			stockInDetail.setSku_money(sku_money);

			boolean result = inStockService.modifyInStockSheet(stockInDetail);
			Assert.assertEquals(result, true, "提交采购入库单失败");

			result = inStockService.cancelInStockSheet(sheet_id);
			Assert.assertEquals(result, true, "采购入库单冲销失败");

			InStockDetailInfoBean temp_stock_in_detail = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(temp_stock_in_detail, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			Assert.assertEquals(temp_stock_in_detail.getStatus(), -1, "冲销的采购入库单状态值与预期的不一致");

		} catch (Exception e) {
			logger.error("入库单审核不通过操作过程中遇到错误: ", e);
			Assert.fail("入库单审核不通过操作过程中遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase07() {
		try {
			ReporterCSS.title("测试点: 打印和导出采购入库单");
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			InStockDetailInfoBean stockIn = new InStockDetailInfoBean();
			stockIn.setId(sheet_id);
			stockIn.setCreator("自动化");
			stockIn.setRemark("自动化创建");
			stockIn.setDate_time(TimeUtil.getCurrentTime("yyyy-MM-dd'T'HH:mm:ss.SSS"));
			stockIn.setSubmit_time(TimeUtil.getCurrentTime("yyyy-MM-dd"));
			stockIn.setSettle_supplier_id(supplier_id);
			stockIn.setSupplier_name(supplier_name);
			stockIn.setSupplier_customer_id(supplier.getCustomer_id());
			stockIn.setType(1);
			stockIn.setStatus(1);
			stockIn.setIs_submit(2);
			stockIn.setStation_id(station_id);
			stockIn.setSku_money(new BigDecimal("0"));
			stockIn.setDelta_money(new BigDecimal("0"));

			// 金额折让
			List<InStockDetailInfoBean.Discount> discounts = new ArrayList<InStockDetailInfoBean.Discount>();
			stockIn.setDiscounts(discounts);

			// 费用分摊
			List<InStockDetailInfoBean.Share> shares = new ArrayList<InStockDetailInfoBean.Share>();
			stockIn.setShares(shares);

			PurchaseSpecBean purchaseSpec = initData.getPurchaseSpec();
			String purchaseSpec_id = purchaseSpec.getId();
			String parchase_name = purchaseSpec.getName();

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("大叶茼蒿", sheet_id);
			Assert.assertEquals(supplySkus != null && supplySkus.size() >= 1, true, "入库单搜索入库商品无结果,与预期不符");

			SupplySkuBean supplySku = supplySkus.get(0);

			String parchase_std_unit = supplySku.getStd_unit_name();
			BigDecimal purchase_ratio = supplySku.getSale_ratio();
			String purchase_unit = supplySku.getSale_unit_name();
			InStockDetailInfoBean.Detail detail = stockIn.new Detail();

			NumberFormat nf = new DecimalFormat("00000");

			detail.setBatch_number(sheet_id + "-" + nf.format(1));
			detail.setName(purchaseSpec.getName());
			String displayName = parchase_name + "(" + purchase_ratio + parchase_std_unit + "/" + purchase_unit + ")";
			detail.setDisplayName(displayName);
			detail.setId(purchaseSpec_id);
			detail.setCategory(supplySku.getCategory_id_2_name());
			detail.setSpu_id(supplySku.getSpu_id());
			detail.setPurchase_unit(purchase_unit);
			detail.setStd_unit(parchase_std_unit);
			detail.setRatio(purchase_ratio);
			BigDecimal quantity = new BigDecimal("2");
			detail.setQuantity(quantity);
			detail.setPurchase_unit_quantity(quantity.multiply(purchase_ratio));
			BigDecimal unit_price = new BigDecimal("5");
			detail.setUnit_price(unit_price);
			detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
			detail.setMoney(unit_price.multiply(quantity));
			detail.setOperator("自动化");

			List<InStockDetailInfoBean.Detail> details = new ArrayList<InStockDetailInfoBean.Detail>();
			details.add(detail);
			stockIn.setDetails(details);

			boolean result = inStockService.modifyInStockSheet(stockIn);
			Assert.assertEquals(result, true, "提交采购入库单失败");

			List<String> sheet_ids = new ArrayList<String>();
			sheet_ids.add(sheet_id);
			result = inStockService.createInStockSheetPrintLog(sheet_ids);
			Assert.assertEquals(result, true, "新增打印入库单日志信息失败");

			List<InStockDetailInfoBean> inStockSheetDetails = inStockService.printInStockSheetDetail(sheet_ids);
			Assert.assertNotEquals(inStockSheetDetails, null, "打印采购入库单拉取数据接口调用失败");

			result = inStockService.exportInStockSheetDetail(sheet_id);
			Assert.assertEquals(result, true, "导出采购入库单失败");
		} catch (Exception e) {
			logger.error("打印和导出采购入库单的时候遇到错误: ", e);
			Assert.fail("打印和导出采购入库单的时候遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase08() {
		ReporterCSS.title("测试点: 新版采购入库,一步入库操作");
		try {
			SupplierDetailBean supplier = initData.getSupplier();
			String settle_supplier_id = supplier.getId();
			String settle_supplier_name = supplier.getName();

			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", settle_supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "搜索入库商品失败");

			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			List<SupplySkuBean> otherSupplySkus = supplySkusMap.get("other");

			targetSupplySkus.addAll(otherSupplySkus);

			Assert.assertEquals(targetSupplySkus.size() >= 1, true, "搜索入库商品结果为空,无法进行入库操作");

			List<SupplySkuBean> selectedSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 8);

			InStockCreateParam inStockCreateParam = new InStockCreateParam();

			List<InStockCreateParam.Detail> details = new ArrayList<InStockCreateParam.Detail>();
			InStockCreateParam.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			for (SupplySkuBean supplySku : selectedSupplySkus) {
				detail = inStockCreateParam.new Detail();
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id,
						settle_supplier_id);

				PurchaseSpecRefPriceBean purchaseSpecRefPrice = purchaseService
						.getPurchaseSpecRefPrice(supplySku.getSpu_id(), purchaseSpec_id, settle_supplier_id);
				Assert.assertNotEquals(purchaseSpecRefPrice, null, "获取商品最近价格失败");

				Assert.assertNotEquals(supplier_avg_price, null,
						"获取采购规格 " + purchaseSpec_id + " 在此供应商 " + settle_supplier_id + " 对应的入库均价失败");

				String parchase_name = supplySku.getSku_name();
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();

				detail.setBatch_number(StringUtil.getRandomString(12).toUpperCase());
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

				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
			}

			inStockCreateParam.setDetails(details);
			inStockCreateParam.setSku_money(sku_money);
			inStockCreateParam.setSettle_supplier_id(settle_supplier_id);
			inStockCreateParam.setSupplier_name(settle_supplier_name);
			inStockCreateParam.setSubmit_time_new(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
			inStockCreateParam.setDiscount(new ArrayList<>());
			inStockCreateParam.setShare(new ArrayList<>());
			inStockCreateParam.setDelta_money(new BigDecimal("0"));
			inStockCreateParam.setIs_submit(2);
			inStockCreateParam.setRemark(StringUtil.getRandomString(6));
			String sheet_id = inStockService.createInStockSheet(inStockCreateParam);
			Assert.assertNotEquals(sheet_id, null, "新版UI,新建采购入库单失败");

			InStockDetailInfoBean stockInDetailInfo = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (stockInDetailInfo.getSku_money().compareTo(inStockCreateParam.getSku_money()) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", inStockCreateParam.getSku_money(),
						stockInDetailInfo.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (stockInDetailInfo.getStatus() != 2) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 2, stockInDetailInfo.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (!stockInDetailInfo.getRemark().equals(inStockCreateParam.getRemark())) {
				msg = String.format("采购入库单统计的备注与预期的不一致,预期:%s,实际:%s", inStockCreateParam.getRemark(),
						stockInDetailInfo.getRemark());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Detail> detailResults = stockInDetailInfo.getDetails();

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> actual_skus = detailResults.stream().map(d -> d.getId()).collect(Collectors.toList());
			if (!expected_skus.equals(actual_skus)) {
				msg = String.format("采购入库单入库商品排序与预期不一致,预期:%s,实际:%s", expected_skus, actual_skus);
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockCreateParam.Detail detailParam : details) {
				String purchase_id = detailParam.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				String actual_display_name = detailResult.getName() + "(" + detailResult.getRatio()
						+ detailResult.getStd_unit() + "/" + detailResult.getPurchase_unit() + ")";

				if (!detailParam.getDisplayName().equals(actual_display_name)) {
					msg = String.format("采购入库单中的商品%s显示的名称与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getDisplayName(),
							actual_display_name);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getUnit_price().compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id,
							detail.getUnit_price(), detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getMoney().compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getMoney(),
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "创建采购入库单编辑填写的信息与创建后查询到的不一致");
		} catch (Exception e) {
			logger.error("新版UI,采购入库操作遇到错误: ", e);
			Assert.fail("新版UI,采购入库操作遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase09() {
		ReporterCSS.title("测试点: 新版采购入库,一步入库操作,添加金额折让");
		try {
			List<StockSettleSupplierBean> stockSettleSuppliers = stockService.getStockSettleSuppliers();
			Assert.assertNotEquals(stockSettleSuppliers, null, "采购入库获取供应商列表失败");

			StockSettleSupplierBean stockSettleSupplier = NumberUtil.roundNumberInList(stockSettleSuppliers);

			String settle_supplier_id = stockSettleSupplier.getSettle_supplier_id();
			String settle_supplier_name = stockSettleSupplier.getName();

			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", settle_supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "搜索入库商品失败");

			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			List<SupplySkuBean> otherSupplySkus = supplySkusMap.get("other");

			targetSupplySkus.addAll(otherSupplySkus);

			Assert.assertEquals(targetSupplySkus.size() >= 1, true, "搜索入库商品结果为空,无法进行入库操作");

			List<SupplySkuBean> selectedSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 8);

			InStockCreateParam inStockCreateParam = new InStockCreateParam();

			List<InStockCreateParam.Detail> details = new ArrayList<InStockCreateParam.Detail>();
			InStockCreateParam.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			for (SupplySkuBean supplySku : selectedSupplySkus) {
				detail = inStockCreateParam.new Detail();
				String purchaseSpec_id = supplySku.getSku_id();

				// 获取指定供应商和指定采购规格的入库均价
				BigDecimal supplier_avg_price = inStockService.getSupplieraveragePrice(purchaseSpec_id,
						settle_supplier_id);
				Assert.assertNotEquals(supplier_avg_price, null,
						"获取采购规格 " + purchaseSpec_id + " 在此供应商 " + settle_supplier_id + " 对应的入库均价失败");

				String parchase_name = supplySku.getSku_name();
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();

				detail.setBatch_number(StringUtil.getRandomString(12).toUpperCase());
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

				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
			}

			inStockCreateParam.setDetails(details);
			inStockCreateParam.setSku_money(sku_money);
			inStockCreateParam.setSettle_supplier_id(settle_supplier_id);
			inStockCreateParam.setSupplier_name(settle_supplier_name);
			inStockCreateParam.setSubmit_time_new(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
			inStockCreateParam.setDiscount(new ArrayList<>());
			inStockCreateParam.setShare(new ArrayList<>());
			inStockCreateParam.setDelta_money(new BigDecimal("0"));
			inStockCreateParam.setIs_submit(2);
			inStockCreateParam.setRemark(StringUtil.getRandomString(6));

			// 金额折让
			List<InStockCreateParam.Discount> discounts = new ArrayList<>();
			InStockCreateParam.Discount discount = inStockCreateParam.new Discount();
			discount.setAction(1);
			BigDecimal discount_money = new BigDecimal("8");
			discount.setMoney(discount_money);
			discount.setReason(1);
			discount.setRemark(StringUtil.getRandomString(6));
			discounts.add(discount);
			inStockCreateParam.setDiscount(discounts);
			inStockCreateParam.setDelta_money(discount_money);

			String sheet_id = inStockService.createInStockSheet(inStockCreateParam);
			Assert.assertNotEquals(sheet_id, null, "新版UI,新建采购入库单失败");

			InStockDetailInfoBean stockInDetailInfo = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			String msg = null;
			boolean result = true;
			if (stockInDetailInfo.getSku_money().compareTo(inStockCreateParam.getSku_money()) != 0) {
				msg = String.format("采购入库单统计的入库金额与预期的不一致,预期:%s,实际:%s", inStockCreateParam.getSku_money(),
						stockInDetailInfo.getSku_money());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (stockInDetailInfo.getStatus() != 2) {
				msg = String.format("采购入库单对应的状态值与预期的不一致,预期:%s,实际:%s", 2, stockInDetailInfo.getStatus());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			if (!stockInDetailInfo.getRemark().equals(inStockCreateParam.getRemark())) {
				msg = String.format("采购入库单统计的备注与预期的不一致,预期:%s,实际:%s", inStockCreateParam.getRemark(),
						stockInDetailInfo.getRemark());
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			List<InStockDetailInfoBean.Detail> detailResults = stockInDetailInfo.getDetails();

			List<String> expected_skus = details.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> actual_skus = detailResults.stream().map(d -> d.getId()).collect(Collectors.toList());
			if (!expected_skus.equals(actual_skus)) {
				msg = String.format("采购入库单入库商品排序与预期不一致,预期:%s,实际:%s", expected_skus, actual_skus);
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			}

			// 商品判断
			InStockDetailInfoBean.Detail detailResult = null;
			for (InStockCreateParam.Detail detailParam : details) {
				String purchase_id = detailParam.getId();
				detailResult = detailResults.stream().filter(d -> d.getId().equals(purchase_id)).findAny().orElse(null);
				if (detailResult == null) {
					msg = String.format("编辑填写的入库商品[%s:%s]在采购入库单中没有找到", purchase_id, detail.getName());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
					continue;
				}

				String actual_display_name = detailResult.getName() + "(" + detailResult.getRatio()
						+ detailResult.getStd_unit() + "/" + detailResult.getPurchase_unit() + ")";

				if (!detailParam.getDisplayName().equals(actual_display_name)) {
					msg = String.format("采购入库单中的商品%s显示的名称与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getDisplayName(),
							actual_display_name);
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getQuantity().compareTo(detailResult.getQuantity()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库数(采购单位)与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getQuantity(),
							detailResult.getQuantity());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getUnit_price().compareTo(detailResult.getUnit_price()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库单价(基本单位)与预期的不一致,预期:%s,实际:%s", purchase_id,
							detail.getUnit_price(), detailResult.getUnit_price());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getMoney().compareTo(detailResult.getMoney()) != 0) {
					msg = String.format("采购入库单中的商品%s显示的入库金额与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getMoney(),
							detailResult.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (detailParam.getIs_arrival() != detailResult.getIs_arrival()) {
					msg = String.format("采购入库单中的商品%s显示的到货状态与预期的不一致,预期:%s,实际:%s", purchase_id, detail.getIs_arrival(),
							detailResult.getIs_arrival());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			List<InStockDetailInfoBean.Discount> temp_discounts = stockInDetailInfo.getDiscounts();
			if (temp_discounts.size() != 1) {
				msg = String.format("采购入库单添加的金额折让在采购入库单详细页面没有查询到");
				result = false;
				ReporterCSS.warn(msg);
				logger.warn(msg);
			} else {
				InStockDetailInfoBean.Discount temp_discount = temp_discounts.get(0);
				if (temp_discount.getAction() != discount.getAction()) {
					msg = String.format("采购入库单添加的费用分摊的折让类型与预期不一致,预期:%s,实际:%s", discount.getAction(),
							temp_discount.getAction());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_discount.getReason() != discount.getReason()) {
					msg = String.format("采购入库单添加的费用分摊的折让原因与预期不一致,预期:%s,实际:%s", discount.getReason(),
							temp_discount.getReason());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (temp_discount.getMoney().compareTo(discount.getMoney()) != 0) {
					msg = String.format("采购入库单添加的费用分摊的折让金额与预期不一致,预期:%s,实际:%s", discount.getMoney(),
							temp_discount.getMoney());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}

				if (!temp_discount.getRemark().equals(discount.getRemark())) {
					msg = String.format("采购入库单添加的费用分摊的折让备注与预期不一致,预期:%s,实际:%s", discount.getRemark(),
							temp_discount.getRemark());
					result = false;
					ReporterCSS.warn(msg);
					logger.warn(msg);
				}
			}

			Assert.assertEquals(result, true, "创建采购入库单编辑填写的信息与创建后查询到的不一致");
		} catch (Exception e) {
			logger.error("新版UI,采购入库操作遇到错误: ", e);
			Assert.fail("新版UI,采购入库操作遇到错误: ", e);
		}
	}

	@Test
	public void createInStockSheetTestCase10() {
		ReporterCSS.title("测试点: 采购入库后,验证库存均价(特殊逻辑:负库存入库SPU均价直接取此次入库价格)");
		try {
			String settle_supplier_id = initData.getSupplier().getId();
			String settle_supplier_name = initData.getSupplier().getName();

			// 取一个库存为负的SPU
			StockCheckFilterParam stockCheckFilterParam = new StockCheckFilterParam();
			stockCheckFilterParam.setRemain_status(3);
			stockCheckFilterParam.setLimit(10);
			stockCheckFilterParam.setOffset(0);

			List<SpuStockBean> spuStocks = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStocks, null, "库存盘点搜索库存小于0的商品失败");

			List<SupplySkuBean> inStockSkus = new ArrayList<SupplySkuBean>();
			List<SpuStockBean> oldSpuStocks = new ArrayList<SpuStockBean>();

			if (spuStocks.size() > 0) {
				SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStocks);
				String spu_name = spuStock.getName();
				String spu_id = spuStock.getSpu_id();
				oldSpuStocks.add(spuStock);

				Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku(spu_name,
						settle_supplier_id);
				Assert.assertNotEquals(supplySkusMap, null, "搜索入库商品失败");

				List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");

				SupplySkuBean supplySku = targetSupplySkus.stream().filter(s -> s.getSpu_id().equals(spu_id))
						.findFirst().orElse(null);
				if (supplySku != null) {
					inStockSkus.add(supplySku);
				}
			}

			// 取一个库存为正的SPU
			stockCheckFilterParam.setRemain_status(1);
			spuStocks = stockCheckService.searchStockCheck(stockCheckFilterParam);
			Assert.assertNotEquals(spuStocks, null, "库存盘点搜索库存小于的商品失败");

			if (spuStocks.size() > 0) {
				SpuStockBean spuStock = NumberUtil.roundNumberInList(spuStocks);
				String spu_name = spuStock.getName();
				String spu_id = spuStock.getSpu_id();
				oldSpuStocks.add(spuStock);

				Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku(spu_name,
						settle_supplier_id);
				Assert.assertNotEquals(supplySkusMap, null, "搜索入库商品失败");

				List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");

				SupplySkuBean supplySku = targetSupplySkus.stream().filter(s -> s.getSpu_id().equals(spu_id))
						.findFirst().orElse(null);
				if (supplySku != null) {
					inStockSkus.add(supplySku);
				}
			}

			Assert.assertEquals(inStockSkus.size() > 0, true, "没有搜索到符合条件的入库商品,此用例不往下再执行");

			InStockCreateParam inStockCreateParam = new InStockCreateParam();

			List<InStockCreateParam.Detail> details = new ArrayList<InStockCreateParam.Detail>();
			InStockCreateParam.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			for (SupplySkuBean supplySku : inStockSkus) {
				detail = inStockCreateParam.new Detail();
				String purchaseSpec_id = supplySku.getSku_id();

				String parchase_name = supplySku.getSku_name();
				String parchase_std_unit = supplySku.getStd_unit_name();
				BigDecimal purchase_ratio = supplySku.getSale_ratio();
				String purchase_unit = supplySku.getSale_unit_name();

				detail.setBatch_number(StringUtil.getRandomString(12).toUpperCase());
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
				BigDecimal quantity = NumberUtil.getRandomNumber(10, 20, 0);
				detail.setQuantity(quantity);
				detail.setPurchase_unit_quantity(quantity.divide(purchase_ratio, 4, BigDecimal.ROUND_HALF_UP));

				BigDecimal unit_price = NumberUtil.getRandomNumber(4, 8, 1);

				detail.setUnit_price(unit_price);
				detail.setPurchase_unit_price(unit_price.multiply(purchase_ratio));
				BigDecimal money = unit_price.multiply(quantity).setScale(2, BigDecimal.ROUND_HALF_UP);
				detail.setMoney(money);

				detail.setIs_arrival(NumberUtil.roundNumberInList(Arrays.asList(0, 1)));
				sku_money = sku_money.add(money);
				details.add(detail);
			}

			inStockCreateParam.setDetails(details);
			inStockCreateParam.setSku_money(sku_money);
			inStockCreateParam.setSettle_supplier_id(settle_supplier_id);
			inStockCreateParam.setSupplier_name(settle_supplier_name);
			inStockCreateParam.setSubmit_time_new(TimeUtil.getCurrentTime("yyyy-MM-dd HH:mm"));
			inStockCreateParam.setDiscount(new ArrayList<>());
			inStockCreateParam.setShare(new ArrayList<>());
			inStockCreateParam.setDelta_money(new BigDecimal("0"));
			inStockCreateParam.setIs_submit(2);
			inStockCreateParam.setRemark(StringUtil.getRandomString(6));

			// 金额折让
			List<InStockCreateParam.Discount> discounts = new ArrayList<>();
			inStockCreateParam.setDiscount(discounts);
			inStockCreateParam.setDelta_money(new BigDecimal("0"));

			String sheet_id = inStockService.createInStockSheet(inStockCreateParam);
			Assert.assertNotEquals(sheet_id, null, "新版UI,新建采购入库单失败");

			InStockDetailInfoBean stockInDetailInfo = inStockService.getInStockSheetDetail(sheet_id);
			Assert.assertNotEquals(stockInDetailInfo, null, "获取采购入库单 " + sheet_id + " 详细信息失败");

			List<InStockDetailInfoBean.Detail> resultDetails = stockInDetailInfo.getDetails();
			BigDecimal new_stock = null;
			BigDecimal new_avg_price = null;
			String msg = null;
			boolean result = true;
			for (InStockDetailInfoBean.Detail resultDetail : resultDetails) {
				String spu_id = resultDetail.getSpu_id();
				SpuStockBean oldSpuStock = oldSpuStocks.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny()
						.orElse(null);
				new_stock = oldSpuStock.getRemain().add(resultDetail.getQuantity());
				if (oldSpuStock.getRemain().compareTo(new BigDecimal("0")) < 0) {
					new_avg_price = resultDetail.getUnit_price();
				} else {
					new_avg_price = (oldSpuStock.getStock_value().add(resultDetail.getMoney())).divide(new_stock, 2,
							BigDecimal.ROUND_HALF_UP);
				}

				SpuStockBean newSpuStock = stockCheckService.getSpuStock(spu_id);
				Assert.assertNotEquals(newSpuStock, null, "库存盘点,获取商品 " + spu_id + "库存信息失败");

				if (newSpuStock.getAvg_price().compareTo(new_avg_price) != 0) {
					msg = String.format("商品%s的库存均价与预期不符,预期:%s,实际:%s", spu_id, new_avg_price,
							newSpuStock.getAvg_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (newSpuStock.getRemain().compareTo(new_stock) != 0) {
					msg = String.format("商品%s的库存数与预期不符,预期:%s,实际:%s", spu_id, new_stock, newSpuStock.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购入库后,库存信息与预期不符");
		} catch (Exception e) {
			logger.error("新版UI,采购入库操作遇到错误: ", e);
			Assert.fail("新版UI,采购入库操作遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase01() {
		try {
			List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(supplierList, null, "查询站点供应商列表遇到错误");
			Assert.assertEquals(supplierList.size() > 0, true, "此站点无供应商,无法进行创建采购入库单操作");

			SupplierDetailBean supplier = NumberUtil.roundNumberInList(supplierList);
			String sheet_id = inStockService.createInStockSheet(supplier.getId(), supplier.getName());
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			// 按建单日期

			inStockSheetFilterParam.setSearch_text(sheet_id);

			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			Assert.assertEquals(inStockSheetList.size() == 1 && inStockSheetList.get(0).getId().equals(sheet_id), true,
					"搜索结果与过滤结果不相符");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase02() {
		try {
			// 按建单日期
			inStockSheetFilterParam.setType(1);
			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			List<InStockSheetBean> filterInStockSheetList = inStockSheetList.stream()
					.filter(s -> !s.getSubmit_time().substring(0, 10).equals(todayStr)).collect(Collectors.toList());
			Assert.assertEquals(filterInStockSheetList.size(), 0, "按入库时间搜索,搜索到了不符合过滤条件的采购入库单");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase03() {
		try {
			// 按建单日期
			inStockSheetFilterParam.setStatus(2);
			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			List<InStockSheetBean> filterInStockSheetList = inStockSheetList.stream().filter(s -> s.getStatus() != 2)
					.collect(Collectors.toList());
			Assert.assertEquals(filterInStockSheetList.size(), 0, "按入库状态筛选,搜索到了不符合过滤条件的采购入库单");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase04() {
		try {
			ReporterCSS.title("测试点: 导出采购入库单列表");
			InStockSheetFilterParam filterParam = new InStockSheetFilterParam();
			// 按建单日期
			filterParam.setType(2);
			filterParam.setStatus(5);
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			filterParam.setStart(today);
			filterParam.setEnd(today);
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setExport();

			boolean result = inStockService.exportInStockSheet(filterParam);
			Assert.assertEquals(result, true, "导出采购入库单列表失败");
		} catch (Exception e) {
			logger.error("导出采购入库单列表遇到错误: ", e);
			Assert.fail("导出采购入库单列表遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase05() {
		try {
			ReporterCSS.title("测试点: 下载采购入库单批量导入模板");
			boolean result = inStockService.downloadInStockSheetTemplate();
			Assert.assertEquals(result, true, "下载采购入库单批量导入模板失败");
		} catch (Exception e) {
			logger.error("下载采购入库单批量导入模板遇到错误: ", e);
			Assert.fail("下载采购入库单批量导入模板遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase06() {
		try {
			ReporterCSS.title("测试点: 新版UI,采购入库单按入库时间搜索过滤");
			List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(supplierList, null, "查询站点供应商列表遇到错误");
			Assert.assertEquals(supplierList.size() > 0, true, "此站点无供应商,无法进行创建采购入库单操作");

			SupplierDetailBean supplier = NumberUtil.roundNumberInList(supplierList);
			String sheet_id = inStockService.createInStockSheet(supplier.getId(), supplier.getName());
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			// 按建单日期
			inStockSheetFilterParam = new InStockSheetFilterParam();
			inStockSheetFilterParam.setSearch_type(1);
			inStockSheetFilterParam.setType(2);
			inStockSheetFilterParam.setStart_date_new(start_date_new);
			inStockSheetFilterParam.setEnd_date_new(end_date_new);
			inStockSheetFilterParam.setSearch_text(sheet_id);

			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			Assert.assertEquals(inStockSheetList.size() == 1 && inStockSheetList.get(0).getId().equals(sheet_id), true,
					"搜索结果与过滤结果不相符");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase07() {
		try {
			ReporterCSS.title("测试点: 新版UI,按入库时间搜索过滤采购入库单");
			// 按入库日期
			inStockSheetFilterParam = new InStockSheetFilterParam();
			inStockSheetFilterParam.setSearch_type(1);
			inStockSheetFilterParam.setType(1);
			inStockSheetFilterParam.setStart_date_new(start_date_new);
			inStockSheetFilterParam.setEnd_date_new(end_date_new);

			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			List<InStockSheetBean> filterInStockSheetList = inStockSheetList.stream()
					.filter(s -> !s.getSubmit_time().substring(0, 10).equals(start_date_new.substring(0, 10)))
					.collect(Collectors.toList());
			Assert.assertEquals(filterInStockSheetList.size(), 0, "按入库时间搜索,搜索到了不符合过滤条件的采购入库单");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase08() {
		try {
			ReporterCSS.title("测试点: 新版UI,按建单时间+状态值搜索过滤采购入库单");
			inStockSheetFilterParam = new InStockSheetFilterParam();
			inStockSheetFilterParam.setSearch_type(1);
			inStockSheetFilterParam.setType(2);
			inStockSheetFilterParam.setStart_date_new(start_date_new);
			inStockSheetFilterParam.setEnd_date_new(end_date_new);
			inStockSheetFilterParam.setStatus(2);

			List<InStockSheetBean> inStockSheetList = inStockService.searchInStockSheet(inStockSheetFilterParam);
			Assert.assertEquals(inStockSheetList != null, true, "搜索过滤采购入库单失败");

			List<InStockSheetBean> filterInStockSheetList = inStockSheetList.stream().filter(s -> s.getStatus() != 2)
					.collect(Collectors.toList());
			Assert.assertEquals(filterInStockSheetList.size(), 0, "按入库状态筛选,搜索到了不符合过滤条件的采购入库单");
		} catch (Exception e) {
			logger.error("搜索过滤采购入库单遇到错误: ", e);
			Assert.fail("搜索过滤采购入库单遇到错误: ", e);
		}
	}

	@Test
	public void stockInSheetFilterTestCase09() {
		try {
			ReporterCSS.title("测试点: 新版UI,导出采购入库单列表");
			InStockSheetFilterParam filterParam = new InStockSheetFilterParam();
			// 按建单日期
			filterParam.setSearch_type(1);
			filterParam.setType(2);
			filterParam.setStatus(5);
			filterParam.setStart_date_new(start_date_new);
			filterParam.setEnd_date_new(end_date_new);
			filterParam.setExport();

			boolean result = inStockService.exportInStockSheet(filterParam);
			Assert.assertEquals(result, true, "导出采购入库单列表失败");
		} catch (Exception e) {
			logger.error("导出采购入库单列表遇到错误: ", e);
			Assert.fail("导出采购入库单列表遇到错误: ", e);
		}
	}

	@Test
	public void stockInImportSheetTestCase01() {
		ReporterCSS.title("测试点: 采购入库单批量导入");
		try {
			SupplierDetailBean supplier = initData.getSupplier();
			String supplier_id = supplier.getId();
			String supplier_name = supplier.getName();
			String customer_id = supplier.getCustomer_id();

			String sheet_id = inStockService.createInStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购入库单失败");

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购入库单搜索入库商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购入库单搜索入库商品无结果,与预期不符");

			List<InStockSheetImportParam> inStockSheetImportParamList = new ArrayList<>();
			InStockSheetImportParam inStockSheetImportParam = null;
			for (int i = 0; i < supplySkus.size(); i++) {
				inStockSheetImportParam = new InStockSheetImportParam();
				SupplySkuBean supplySku = supplySkus.get(i);
				inStockSheetImportParam.setSupplier_id(customer_id);
				inStockSheetImportParam.setPurchase_sku_id(supplySku.getSku_id());
				inStockSheetImportParam.setSpec_id(supplySku.getSpu_id());
				BigDecimal stock_amount = NumberUtil.getRandomNumber(5, 10, 2);

				BigDecimal unit_price = NumberUtil.getRandomNumber(3, 8, 1);

				if (i % 2 == 0) {
					inStockSheetImportParam.setIn_stock_amount(stock_amount);
					inStockSheetImportParam.setUnit_price(unit_price);

				} else {
					inStockSheetImportParam.setPurchase_amount(stock_amount);
					inStockSheetImportParam.setPurchase_price(unit_price);

				}

				inStockSheetImportParam.setSku_money(stock_amount.multiply(unit_price));
				inStockSheetImportParam.setRemark(StringUtil.getRandomString(6));
				inStockSheetImportParamList.add(inStockSheetImportParam);

				if (inStockSheetImportParamList.size() >= 5) {
					break;
				}
			}

			boolean result = inStockService.importInStockSheet(inStockSheetImportParamList);
			Assert.assertEquals(result, true, "采购入库单批量导入失败");
		} catch (Exception e) {
			logger.error("采购入库批量导入遇到错误: ", e);
			Assert.fail("采购入库批量导入遇到错误: ", e);
		}
	}

}
