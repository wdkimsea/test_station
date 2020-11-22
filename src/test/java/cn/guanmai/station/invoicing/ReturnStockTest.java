package cn.guanmai.station.invoicing;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.SpuStockBean;
import cn.guanmai.station.bean.invoicing.StockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockBatchBean;
import cn.guanmai.station.bean.invoicing.ReturnStockDetailBean;
import cn.guanmai.station.bean.invoicing.ReturnStockSheetBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.invoicing.SupplySkuBean;
import cn.guanmai.station.bean.invoicing.param.ReturnStockCreateParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.ReturnStockSheetImportParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.ReturnStockServiceImpl;
import cn.guanmai.station.impl.invoicing.StockServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.ReturnStockService;
import cn.guanmai.station.interfaces.invoicing.StockService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.JsonUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 27, 2019 11:09:06 AM 
* @des 采购退货相关测试用例
* @version 1.0 
*/
public class ReturnStockTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(ReturnStockTest.class);
	private ReturnStockService returnStockService;
	private StockCheckService stockCheckService;
	private StockService stockService;
	private SupplierService supplierService;
	private InitDataBean initData;
	private String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private String tommrow;
	private String supplier_id;
	private String supplier_name;
	private String customer_id;

	// 进销存类型
	private int stock_type;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		returnStockService = new ReturnStockServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		stockService = new StockServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);

		try {
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录站点信息失败");
			stock_type = loginUserInfo.getStock_method();

			initData = getInitData();
			SupplierDetailBean supplier = initData.getSupplier();
			supplier_id = supplier.getId();
			supplier_name = supplier.getName();
			customer_id = supplier.getCustomer_id();

			tommrow = TimeUtil.calculateTime("yyyy-MM-dd", today, 1, Calendar.DATE);

		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase01() {
		ReporterCSS.title("测试点: 老版本UI,新建采购退货单");
		try {
			List<SupplierDetailBean> supplierList = supplierService.getSettleSupplierList();
			Assert.assertNotEquals(supplierList, null, "获取供应商列表失败");

			Assert.assertEquals(supplierList.size() > 0, true, "站点无供应商,无法进行新建采购退货单");

			SupplierDetailBean supplier = NumberUtil.roundNumberInList(supplierList);
			String sheet_id = returnStockService.createReturnStockSheet(supplier.getId(), supplier.getName());
			Assert.assertNotEquals(sheet_id, null, "新建采购退货单失败");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误: ", e);
			Assert.fail("新建采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase02() {
		ReporterCSS.title("测试点: 老版本UI,新建采购退货单,编辑数据后提交");
		try {
			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单" + sheet_id + "详细信息失败");

			List<SupplySkuBean> supplySkuList = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkuList, null, "搜索退货商品失败");
			Assert.assertEquals(supplySkuList.size() > 0, true, "搜索退货商品无结果,无法进行退货操作");

			supplySkuList = NumberUtil.roundNumberInList(supplySkuList, 2);

			List<ReturnStockDetailBean.Detail> details = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			for (SupplySkuBean supplySku : supplySkuList) {
				String purchase_id = supplySku.getSku_id();
				detail = returnStock.new Detail();
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setId(purchase_id);
				detail.setName(supplySku.getSku_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setStd_unit(supplySku.getStd_unit_name());
				unit_price = NumberUtil.getRandomNumber(4, 8, 1);
				detail.setUnit_price(unit_price);
				detail.setSpu_remark(StringUtil.getRandomString(6));

				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					quantity = returnStockBatch.getRemain();
					detail.setBatch_number(returnStockBatch.getBatch_number());

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setQuantity(quantity);
				money = unit_price.multiply(quantity);
				detail.setMoney(money);
				sku_money = sku_money.add(money);

				details.add(detail);
			}

			returnStock.setSku_money(sku_money);
			returnStock.setSubmit_time(today);
			returnStock.setReturn_sheet_remark(StringUtil.getRandomString(6));
			returnStock.setDetails(details);
			returnStock.setDiscounts(new ArrayList<>());

			boolean result = returnStockService.modifyReturnStockSheet(returnStock);
			Assert.assertEquals(result, true, "采购退货单据" + sheet_id + "提交败");

			ReturnStockDetailBean resultStockReturnDetail = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(resultStockReturnDetail, null, "获取采购退货单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (resultStockReturnDetail.getSku_money().compareTo(sku_money) != 0) {
				msg = String.format("采购退货单%s的退货金额与预期的不一致,预期:%s,实际:%s", sheet_id, sku_money,
						resultStockReturnDetail.getSku_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (resultStockReturnDetail.getStatus() != 2) {
				msg = String.format("采购退货单%s的状态值与预期的不一致,预期:%s,实际:%s", sheet_id, 2, resultStockReturnDetail.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!resultStockReturnDetail.getReturn_sheet_remark().equals(returnStock.getReturn_sheet_remark())) {
				msg = String.format("采购退货单%s的退货备注与预期的不一致,预期:%s,实际:%s", sheet_id, returnStock.getReturn_sheet_remark(),
						resultStockReturnDetail.getReturn_sheet_remark());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<ReturnStockDetailBean.Detail> resultDetails = resultStockReturnDetail.details;
			for (ReturnStockDetailBean.Detail paramDetail : details) {
				ReturnStockDetailBean.Detail resultDetail = resultDetails.stream()
						.filter(d -> d.getId().equals(paramDetail.getId())).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("采购退货单%s里缺少预期退货商品%s", sheet_id, paramDetail.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (paramDetail.getUnit_price().compareTo(resultDetail.getUnit_price()) != 0) {
					msg = String.format("采购退货单%s里商品%s退货单价与预期不同,预期:%s,实际:%s", sheet_id, paramDetail.getId(),
							paramDetail.getUnit_price(), resultDetail.getUnit_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (paramDetail.getQuantity().compareTo(resultDetail.getQuantity()) != 0) {
					msg = String.format("采购退货单%s里商品%s退货数与预期不同,预期:%s,实际:%s", sheet_id, paramDetail.getId(),
							paramDetail.getQuantity(), resultDetail.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!paramDetail.getSpu_remark().equals(resultDetail.getSpu_remark())) {
					msg = String.format("采购退货单%s里商品%s退货商品备注与预期不同,预期:%s,实际:%s", sheet_id, paramDetail.getId(),
							paramDetail.getSpu_remark(), resultDetail.getSpu_remark());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (stock_type == 2) {
					if (!paramDetail.getBatch_number().equals(resultDetail.getBatch_number())) {
						msg = String.format("采购退货单%s里商品%s退货批次与预期不同,预期:%s,实际:%s", sheet_id, paramDetail.getId(),
								paramDetail.getBatch_number(), resultDetail.getBatch_number());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}

			Assert.assertEquals(result, true, "采购退货单保存的数据和预期填写的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误: ", e);
			Assert.fail("新建采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase03() {
		try {
			ReporterCSS.title("测试点: 老版本UI,新增采购退货单,并增加折让金额");

			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单" + sheet_id + "详细信息失败");

			List<SupplySkuBean> supplySkuList = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkuList, null, "搜索退货商品失败");
			Assert.assertEquals(supplySkuList.size() > 0, true, "搜索退货商品无结果,无法进行退货操作");

			List<ReturnStockDetailBean.Detail> details = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			for (SupplySkuBean supplySku : supplySkuList) {
				String purchase_id = supplySku.getSku_id();
				detail = returnStock.new Detail();
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setId(purchase_id);
				detail.setName(supplySku.getSku_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setStd_unit(supplySku.getStd_unit_name());
				unit_price = NumberUtil.getRandomNumber(4, 8, 1);
				detail.setUnit_price(unit_price);
				detail.setSpu_remark(StringUtil.getRandomString(6));

				// 如果是先进先出,则要找对应的批次信息
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					quantity = returnStockBatch.getRemain();
					detail.setBatch_number(returnStockBatch.getBatch_number());

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setQuantity(quantity);
				money = unit_price.multiply(quantity);
				detail.setMoney(money);
				sku_money = sku_money.add(money);

				details.add(detail);
				if (details.size() >= 2) {
					break;
				}
			}

			returnStock.setSku_money(sku_money);
			returnStock.setSubmit_time(today);
			returnStock.setReturn_sheet_remark("自动化测试");
			returnStock.setDetails(details);

			List<ReturnStockDetailBean.Discount> discounts = new ArrayList<>();
			ReturnStockDetailBean.Discount discount = returnStock.new Discount();
			discount.setAction(2);
			discount.setReason(1);
			discount.setOperate_time(today);
			discount.setCreator("AT");
			discount.setRemark(StringUtil.getRandomString(4));
			BigDecimal discount_money1 = new BigDecimal("-3");
			discount.setMoney(discount_money1.abs());
			discounts.add(discount);

			discount = returnStock.new Discount();
			discount.setAction(1);
			discount.setReason(2);
			discount.setOperate_time(today);
			discount.setCreator("AT");
			discount.setRemark(StringUtil.getRandomString(4));
			BigDecimal discount_money2 = new BigDecimal("2");
			discount.setMoney(discount_money2);
			discounts.add(discount);
			returnStock.setDiscounts(discounts);

			BigDecimal discount_money = discount_money1.add(discount_money2);

			boolean result = returnStockService.modifyReturnStockSheet(returnStock);
			Assert.assertEquals(result, true, "采购退货单据" + sheet_id + "提交败");

			ReturnStockDetailBean resultStockReturnDetail = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(resultStockReturnDetail, null, "获取采购退货单 " + sheet_id + " 详细信息失败");

			String msg = null;
			if (resultStockReturnDetail.getDelta_money().compareTo(discount_money) != 0) {
				msg = String.format("采购退货单%s里折让金额预期不同,预期:%s,实际:%s", sheet_id, discount_money,
						resultStockReturnDetail.getDelta_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (resultStockReturnDetail.getSku_money().compareTo(sku_money) != 0) {
				msg = String.format("采购退货单%s里退货商品总金额预期不同,预期:%s,实际:%s", sheet_id, sku_money,
						resultStockReturnDetail.getSku_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<ReturnStockDetailBean.Discount> resultDiscounts = resultStockReturnDetail.getDiscounts();

			if (resultDiscounts.size() != discounts.size()) {
				msg = String.format("采购退货单%s里预期条目数与实际不同,预期条目数:%s,实际条目数:%s", sheet_id, discounts.size(),
						resultDiscounts.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				for (ReturnStockDetailBean.Discount paramDiscount : discounts) {
					ReturnStockDetailBean.Discount result_discount = resultDiscounts.stream()
							.filter(p -> p.getMoney().compareTo(paramDiscount.getMoney()) == 0).findAny().orElse(null);
					if (result_discount == null) {
						msg = String.format("采购退货单里%s金额为%s的折让条目没有找到", sheet_id, paramDiscount.getMoney());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					} else {
						if (!paramDiscount.getRemark().equals(result_discount.getRemark())) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,备注信息与预期不符,预期：%s,实际:%s", sheet_id,
									paramDiscount.getMoney(), paramDiscount.getRemark(), result_discount.getRemark());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						if (paramDiscount.getAction() != result_discount.getAction()) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,折扣类型值与预期不符,预期：%s,实际:%s", sheet_id,
									paramDiscount.getMoney(), paramDiscount.getAction(), result_discount.getAction());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						if (paramDiscount.getReason() != result_discount.getReason()) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,折扣类型值与预期不符,预期：%s,实际:%s", sheet_id,
									paramDiscount.getMoney(), paramDiscount.getReason(), result_discount.getReason());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}
				}
			}

			Assert.assertEquals(result, true, "采购退货单保存的数据和预期填写的不一致");

		} catch (Exception e) {
			logger.error("提交采购退货单遇到错误: ", e);
			Assert.fail("提交采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase04() {
		try {
			ReporterCSS.title("测试点: 老版本UI,提交采购退货单,查看库存和均价");
			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单" + sheet_id + "详细信息失败");

			List<SupplySkuBean> supplySkuList = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkuList, null, "搜索退货商品失败");
			Assert.assertEquals(supplySkuList.size() > 0, true, "搜索退货商品无结果,无法进行退货操作");

			supplySkuList = NumberUtil.roundNumberInList(supplySkuList, 4);

			Map<String, ReturnStockBatchBean> stockBatchMap = new HashMap<String, ReturnStockBatchBean>();

			List<ReturnStockDetailBean.Detail> details = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			for (SupplySkuBean supplySku : supplySkuList) {
				String purchase_id = supplySku.getSku_id();
				detail = returnStock.new Detail();
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setId(purchase_id);
				detail.setName(supplySku.getSku_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setStd_unit(supplySku.getStd_unit_name());
				unit_price = NumberUtil.getRandomNumber(4, 8, 1);
				detail.setUnit_price(unit_price);
				detail.setSpu_remark(StringUtil.getRandomString(6));

				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					String batch_number = returnStockBatch.getBatch_number();
					if (stockBatchMap.containsKey(batch_number)) {
						continue;
					}
					BigDecimal remain = returnStockBatch.getRemain().setScale(2, BigDecimal.ROUND_HALF_DOWN);
					BigDecimal avg_price = returnStockBatch.getAvg_price();
					// 批次剩余库存如果大于10,就取一个[5,10]之间的随机数,要不然就全退了
					if (remain.compareTo(new BigDecimal(10)) > 0) {
						quantity = NumberUtil.getRandomNumber(5, 10, 1);
						avg_price = (remain.multiply(avg_price).subtract(quantity.multiply(unit_price)))
								.divide(remain.subtract(quantity), 2, BigDecimal.ROUND_HALF_UP);
						remain = remain.subtract(quantity);
					} else {
						quantity = remain;
						remain = remain.subtract(quantity);
					}
					returnStockBatch.setRemain(remain);
					// 批次均价单位为分
					returnStockBatch.setAvg_price(avg_price.multiply(new BigDecimal("100")));
					stockBatchMap.put(batch_number, returnStockBatch);

					detail.setBatch_number(batch_number);

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setQuantity(quantity);
				money = unit_price.multiply(quantity);
				detail.setMoney(money);
				sku_money = sku_money.add(money);

				details.add(detail);
			}

			returnStock.setSku_money(sku_money);
			returnStock.setSubmit_time(today);
			returnStock.setReturn_sheet_remark(StringUtil.getRandomString(6));
			returnStock.setDetails(details);
			returnStock.setDiscounts(new ArrayList<>());

			// 采购退货参数构造完毕

			Map<String, List<ReturnStockDetailBean.Detail>> detail_group = details.stream()
					.collect(Collectors.groupingBy(ReturnStockDetailBean.Detail::getSpu_id));

			Map<String, SpuStockBean> spu_stock_map = new HashMap<String, SpuStockBean>();

			for (String spu_id : detail_group.keySet()) {
				List<ReturnStockDetailBean.Detail> temp_details = detail_group.get(spu_id);
				BigDecimal stock_return_value = BigDecimal.ZERO;
				BigDecimal stock_return_amount = BigDecimal.ZERO;

				for (ReturnStockDetailBean.Detail temp_detail : temp_details) {
					stock_return_amount = stock_return_amount.add(temp_detail.getQuantity());
					stock_return_value = stock_return_value
							.add(temp_detail.getQuantity().multiply(temp_detail.getUnit_price()));
				}
				SpuStockBean spuStock = stockCheckService.getSpuStock(spu_id);
				Assert.assertNotEquals(spuStock, null, "获取" + spu_id + "库存信息失败");

				BigDecimal remain = spuStock.getRemain().subtract(stock_return_amount);

				BigDecimal avg_price = (spuStock.getStock_value().subtract(stock_return_amount)).divide(remain, 2,
						BigDecimal.ROUND_HALF_UP);

				spuStock.setRemain(remain);
				spuStock.setAvg_price(avg_price);

			}

			boolean result = returnStockService.modifyReturnStockSheet(returnStock);
			Assert.assertEquals(result, true, "采购退货单据" + sheet_id + "提交败");

			List<String> msgList = new ArrayList<>();
			String msg = null;
			for (String spu_id : spu_stock_map.keySet()) {
				SpuStockBean spuStock = stockCheckService.getSpuStock(spu_id);
				Assert.assertNotEquals(spuStock, null, "获取" + spu_id + "库存信息失败");

				SpuStockBean expetedSpuStock = spu_stock_map.get(spu_id);

				if (!NumberUtil.roundCompare(expetedSpuStock.getAvg_price(), spuStock.getAvg_price(),
						new BigDecimal("0.01"))) {
					msg = String.format("采购退货单%s里商品%s退货后库存均价与预期不同,预期:%s,实际:%s", sheet_id, spu_id,
							expetedSpuStock.getAvg_price(), spuStock.getAvg_price());
					msgList.add(msg);
					result = false;
				}

				if (!NumberUtil.roundCompare(expetedSpuStock.getRemain(), spuStock.getRemain(),
						new BigDecimal("0.01"))) {
					msg = String.format("采购退货单%s里商品%s退货后库存数与预期不同,预期:%s,实际:%s", sheet_id, spu_id,
							expetedSpuStock.getRemain(), spuStock.getRemain());
					msgList.add(msg);
					result = false;
				}
			}

			// 如果是先进先出,还需要判断下批次的库存
			if (stock_type == 2) {
				for (String batch_number : stockBatchMap.keySet()) {
					StockBatchBean stockBatch = stockCheckService.getStockBatch(batch_number);
					Assert.assertNotEquals(stockBatch, null, "获取" + batch_number + "批次信息失败");

					ReturnStockBatchBean returnStockBatch = stockBatchMap.get(batch_number);
					if (!NumberUtil.roundCompare(returnStockBatch.getAvg_price(), stockBatch.getPrice(),
							new BigDecimal("0.01"))) {
						msg = String.format("采购退货单%s提交后,批次%s的库存均价与预期不符,预期:%s,实际:%s", sheet_id, batch_number,
								returnStockBatch.getAvg_price(), stockBatch.getPrice());
						msgList.add(msg);
						result = false;
					}
					if (!NumberUtil.roundCompare(returnStockBatch.getRemain(), stockBatch.getRemain(),
							new BigDecimal("0.01"))) {
						msg = String.format("采购退货单%s提交后,批次%s的剩余库存数与预期不符,预期:%s,实际:%s", sheet_id, batch_number,
								returnStockBatch.getRemain(), stockBatch.getRemain());
						msgList.add(msg);
						result = false;
					}
				}
			}

			msgList.forEach(m -> {
				ReporterCSS.warn(m);
				logger.warn(m);
			});

			Assert.assertEquals(result, true, "采购退货单提交后,商品的库存相关信息与预期不一致");
		} catch (Exception e) {
			logger.error("提交采购退货单遇到错误: ", e);
			Assert.fail("提交采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase05() {
		try {
			ReporterCSS.title("测试点: 冲销采购退货单");
			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			boolean result = returnStockService.cancelReturnStockSheet(sheet_id);
			Assert.assertEquals(result, true, "冲销采购退货单失败");
		} catch (Exception e) {
			logger.error("提交采购退货单遇到错误: ", e);
			Assert.fail("提交采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase06() {
		try {
			ReporterCSS.title("测试点: 导出采购退货单详细信息");
			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单详细信息失败");

			List<SupplySkuBean> supplySkuList = stockService.getSupplySkuList("h", sheet_id);
			Assert.assertNotEquals(supplySkuList, null, "搜索退货商品失败");
			Assert.assertEquals(supplySkuList.size() > 0, true, "搜索退货商品无结果,无法进行退货操作");

			supplySkuList = NumberUtil.roundNumberInList(supplySkuList, 2);

			List<ReturnStockDetailBean.Detail> details = new ArrayList<ReturnStockDetailBean.Detail>();
			ReturnStockDetailBean.Detail detail = null;
			BigDecimal sku_money = new BigDecimal("0");
			BigDecimal money = null;
			BigDecimal unit_price = null;
			BigDecimal quantity = null;
			for (SupplySkuBean supplySku : supplySkuList) {
				String purchase_id = supplySku.getSku_id();
				detail = returnStock.new Detail();
				detail.setCategory(supplySku.getCategory_id_2_name());
				detail.setId(purchase_id);
				detail.setName(supplySku.getSku_name());
				detail.setSpu_id(supplySku.getSpu_id());
				detail.setStd_unit(supplySku.getStd_unit_name());
				unit_price = NumberUtil.getRandomNumber(4, 8, 1);
				detail.setUnit_price(unit_price);
				detail.setSpu_remark(StringUtil.getRandomString(6));

				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					quantity = returnStockBatch.getRemain();
					detail.setBatch_number(returnStockBatch.getBatch_number());

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setQuantity(quantity);
				money = unit_price.multiply(quantity);
				detail.setMoney(money);
				sku_money = sku_money.add(money);

				details.add(detail);
			}

			returnStock.setSku_money(sku_money);
			returnStock.setSubmit_time(today);
			returnStock.setReturn_sheet_remark("导出采购退货单详细信息");
			returnStock.setDetails(details);
			returnStock.setDiscounts(new ArrayList<ReturnStockDetailBean.Discount>());

			boolean result = returnStockService.modifyReturnStockSheet(returnStock);
			Assert.assertEquals(result, true, "提交采购退货单失败");

			result = returnStockService.exportReturnStockDetailInfo(sheet_id);
			Assert.assertEquals(result, true, "导出采购退货单详细信息失败");
		} catch (Exception e) {
			logger.error("提交采购退货单遇到错误: ", e);
			Assert.fail("提交采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase07() {
		ReporterCSS.title("测试点: 新建UI,新建采购退货单");
		try {
			ReturnStockCreateParam returnStockCreateParam = new ReturnStockCreateParam();
			returnStockCreateParam.setSettle_supplier_id(supplier_id);
			returnStockCreateParam.setSupplier_name(supplier_name);
			returnStockCreateParam.setReturn_sheet_remark(StringUtil.getRandomString(12));
			returnStockCreateParam.setSubmit_time(today);
			returnStockCreateParam.setIs_submit(2);

			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "采购退货,搜索退货商品失败");

			// 退货只能退此供应商可供应的商品
			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			Assert.assertEquals(targetSupplySkus.size() > 0, true, "采购退货,搜索无可退商品");

			// 选择最多8个退货商品
			targetSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 8);

			List<ReturnStockCreateParam.Detail> details = new ArrayList<ReturnStockCreateParam.Detail>();
			ReturnStockCreateParam.Detail detail = null;

			BigDecimal quantity = null;
			BigDecimal unit_price = null;
			BigDecimal money = null;
			BigDecimal sku_money = new BigDecimal("0");
			for (SupplySkuBean sku : targetSupplySkus) {
				detail = returnStockCreateParam.new Detail();
				String purchase_id = sku.getSku_id();
				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					quantity = returnStockBatch.getRemain();
					detail.setBatch_number(returnStockBatch.getBatch_number());

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setId(purchase_id);

				detail.setName(sku.getSku_name());
				detail.setSpu_id(sku.getSpu_id());
				detail.setCategory(sku.getCategory_id_1_name() + "/" + sku.getCategory_id_2_name());
				detail.setStd_unit(sku.getStd_unit_name());
				detail.setSpu_remark(StringUtil.getRandomString(6));

				unit_price = NumberUtil.getRandomNumber(2, 5, 1);
				money = quantity.multiply(unit_price);
				sku_money = sku_money.add(money);

				detail.setQuantity(quantity);
				detail.setMoney(money);
				detail.setUnit_price(unit_price);

				details.add(detail);

			}

			returnStockCreateParam.setDetails(details);
			returnStockCreateParam.setSku_money(sku_money);

			List<ReturnStockCreateParam.Discount> discounts = new ArrayList<ReturnStockCreateParam.Discount>();
			ReturnStockCreateParam.Discount discount = returnStockCreateParam.new Discount();
			discount.setReason(1);
			discount.setAction(2);
			discount.setOperate_time(today);
			discount.setRemark(StringUtil.getRandomString(6));
			BigDecimal delta_money1 = new BigDecimal("-2");
			discount.setMoney(delta_money1.abs());
			discounts.add(discount);

			discount = returnStockCreateParam.new Discount();
			discount.setReason(2);
			discount.setAction(1);
			discount.setOperate_time(today);
			discount.setRemark(StringUtil.getRandomString(6));
			BigDecimal delta_money2 = new BigDecimal("1");
			discount.setMoney(delta_money2.abs());
			discounts.add(discount);

			BigDecimal delta_money = delta_money1.add(delta_money2);

			returnStockCreateParam.setDiscount(discounts);
			returnStockCreateParam.setDelta_money(delta_money);

			String stock_return_sheet_id = returnStockService.newCreateReturnStockSheet(returnStockCreateParam);
			Assert.assertNotEquals(stock_return_sheet_id, null, "新建采购退货单失败");

			// 验证返回结果
			ReturnStockDetailBean returnStock = returnStockService.getRetrunStockDetail(stock_return_sheet_id);
			Assert.assertNotEquals(returnStock, null, "获取采购退货单" + stock_return_sheet_id + "详细信息失败");

			String msg = null;
			boolean result = true;
			if (returnStock.getSku_money().compareTo(returnStockCreateParam.getSku_money()) != 0) {
				msg = String.format("新建的采购退货单%s的商品金额与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getSku_money(), returnStock.getSku_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (returnStock.getDelta_money().compareTo(returnStockCreateParam.getDelta_money()) != 0) {
				msg = String.format("新建的采购退货单%s的折让金额与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getDelta_money(), returnStock.getDelta_money());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!returnStock.getSettle_supplier_id().equals(returnStockCreateParam.getSettle_supplier_id())) {
				msg = String.format("新建的采购退货单%s的供应商ID与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getSettle_supplier_id(), returnStock.getSettle_supplier_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!returnStock.getSubmit_time().equals(returnStockCreateParam.getSubmit_time())) {
				msg = String.format("新建的采购退货单%s的退货时间与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getSubmit_time(), returnStock.getSubmit_time());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!returnStock.getReturn_sheet_remark().equals(returnStockCreateParam.getReturn_sheet_remark())) {
				msg = String.format("新建的采购退货单%s的退货备注与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getReturn_sheet_remark(), returnStock.getReturn_sheet_remark());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (returnStock.getStatus() != returnStockCreateParam.getIs_submit()) {
				msg = String.format("新建的采购退货单%s的状态值与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
						returnStockCreateParam.getIs_submit(), returnStock.getStatus());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			List<ReturnStockDetailBean.Detail> resultDetails = returnStock.getDetails();
			List<String> result_sku_ids = resultDetails.stream().map(d -> d.getId()).collect(Collectors.toList());
			List<String> param_sku_ids = new ArrayList<String>();
			ReturnStockDetailBean.Detail resultDetail = null;
			for (ReturnStockCreateParam.Detail paramDetail : details) {
				String sku_id = paramDetail.getId();
				param_sku_ids.add(sku_id);
				resultDetail = resultDetails.stream().filter(d -> d.getId().equals(sku_id)).findAny().orElse(null);
				if (resultDetail == null) {
					msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面没找到", stock_return_sheet_id, sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				} else {
					if (!paramDetail.getName().equals(resultDetail.getName())) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的商品名与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getName(), resultDetail.getName());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (!paramDetail.getCategory().equals(resultDetail.getCategory())) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的商品分类与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getCategory(), resultDetail.getCategory());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (!paramDetail.getSpu_remark().equals(resultDetail.getSpu_remark())) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的商品退货备注与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getSpu_remark(), resultDetail.getSpu_remark());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (paramDetail.getQuantity().compareTo(resultDetail.getQuantity()) != 0) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的下单数与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getQuantity(), resultDetail.getQuantity());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (paramDetail.getUnit_price().compareTo(resultDetail.getUnit_price()) != 0) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的退货单价与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getUnit_price(), resultDetail.getUnit_price());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					if (paramDetail.getMoney().compareTo(resultDetail.getMoney()) != 0) {
						msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的退货金额与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								sku_id, paramDetail.getMoney(), resultDetail.getMoney());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}

					// 先进先出,多判断批次号
					if (stock_type == 2) {
						if (!paramDetail.getBatch_number().equals(resultDetail.getBatch_number())) {
							msg = String.format("新建的采购退货单%s里的商品%s在采购单详情页面显示的退货批次号与预期不符,预期:%s,实际:%s",
									stock_return_sheet_id, sku_id, paramDetail.getBatch_number(),
									resultDetail.getBatch_number());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}

				}
			}

			if (!param_sku_ids.equals(result_sku_ids)) {
				msg = String.format("新建的采购退货单%s里的商品顺序与预期不一致", stock_return_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				ReporterCSS.log("预期:" + param_sku_ids);
				ReporterCSS.log("预期:" + result_sku_ids);
				result = false;
			}

			List<ReturnStockDetailBean.Discount> resultDiscounts = returnStock.getDiscounts();
			if (resultDiscounts.size() != discounts.size()) {
				msg = String.format("新建的采购退货单%s里的折扣在采购单详情页面显示的条目数与预期的不一致,预期:%s,实际:%s", stock_return_sheet_id,
						discounts.size(), resultDiscounts.size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				for (ReturnStockCreateParam.Discount paramDiscount : discounts) {
					ReturnStockDetailBean.Discount result_discount = resultDiscounts.stream()
							.filter(d -> d.getMoney().compareTo(paramDiscount.getMoney()) == 0).findAny().orElse(null);
					if (result_discount == null) {
						msg = String.format("新建的采购退货单%s里的折扣条目:%s 没有找到", stock_return_sheet_id,
								JsonUtil.objectToStr(paramDiscount));
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					} else {
						if (!paramDiscount.getRemark().equals(result_discount.getRemark())) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,备注信息与预期不符,预期：%s,实际:%s", stock_return_sheet_id,
									paramDiscount.getMoney(), paramDiscount.getRemark(), result_discount.getRemark());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						if (paramDiscount.getAction() != result_discount.getAction()) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,折扣类型值与预期不符,预期：%s,实际:%s", stock_return_sheet_id,
									paramDiscount.getMoney(), paramDiscount.getAction(), result_discount.getAction());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}

						if (paramDiscount.getReason() != result_discount.getReason()) {
							msg = String.format("新建的采购退货单%s里金额%s的折扣条目,折扣类型值与预期不符,预期：%s,实际:%s", stock_return_sheet_id,
									paramDiscount.getMoney(), paramDiscount.getReason(), result_discount.getReason());
							ReporterCSS.warn(msg);
							logger.warn(msg);
							result = false;
						}
					}

				}
			}

			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStatus(5);
			filterParam.setStart(today);
			filterParam.setEnd(today);
			filterParam.setSearch_text(stock_return_sheet_id);
			filterParam.setOffset(0);
			filterParam.setLimit(10);

			List<ReturnStockSheetBean> returnStockSheetList = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStockSheetList, null, "搜索查询采购退货单失败");

			ReturnStockSheetBean returnStockSheet = returnStockSheetList.stream()
					.filter(s -> s.getId().equals(stock_return_sheet_id)).findAny().orElse(null);
			if (returnStockSheet == null) {
				msg = String.format("新建的采购退货单%s在采购单据列表页面没有搜索到", stock_return_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				BigDecimal total_money = returnStockSheet.getSku_money().add(returnStockSheet.getDelta_money());
				BigDecimal expected_total_money = sku_money.add(delta_money);
				if (total_money.compareTo(expected_total_money) != 0) {
					msg = String.format("新建的采购退货单%s在采购单据列表页面显示的退货金额与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
							total_money, expected_total_money);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "新建的采购退货单据" + stock_return_sheet_id + "返回的结果信息与新建所填写的不一致");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误: ", e);
			Assert.fail("新建采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase08() {
		ReporterCSS.title("测试点: 新版UI,采购退货单提交后,验证商品库存和库存均价");
		try {
			ReturnStockCreateParam returnStockCreateParam = new ReturnStockCreateParam();
			returnStockCreateParam.setSettle_supplier_id(supplier_id);
			returnStockCreateParam.setSupplier_name(supplier_name);
			returnStockCreateParam.setReturn_sheet_remark(StringUtil.getRandomString(12));
			returnStockCreateParam.setSubmit_time(today);
			returnStockCreateParam.setIs_submit(2);

			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "采购退货,搜索退货商品失败");

			// 退货只能退此供应商可供应的商品
			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			Assert.assertEquals(targetSupplySkus.size() > 0, true, "采购退货,搜索无可退商品");

			// 选择最多4个退货商品
			targetSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 4);

			List<ReturnStockCreateParam.Detail> details = new ArrayList<ReturnStockCreateParam.Detail>();
			ReturnStockCreateParam.Detail detail = null;

			BigDecimal quantity = null;
			BigDecimal unit_price = null;
			BigDecimal money = null;
			BigDecimal sku_money = new BigDecimal("0");

			Map<String, SpuStockBean> spuStockMap = new HashMap<String, SpuStockBean>();
			Map<String, ReturnStockBatchBean> stockBatchMap = new HashMap<String, ReturnStockBatchBean>();
			for (SupplySkuBean sku : targetSupplySkus) {
				detail = returnStockCreateParam.new Detail();
				String spu_id = sku.getSpu_id();
				String purchase_id = sku.getSku_id();
				unit_price = NumberUtil.getRandomNumber(2, 5, 1);
				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);

					String batch_number = returnStockBatch.getBatch_number();
					if (stockBatchMap.containsKey(batch_number)) {
						continue;
					}

					BigDecimal remain = returnStockBatch.getRemain().setScale(2, BigDecimal.ROUND_HALF_DOWN);
					BigDecimal avg_price = returnStockBatch.getAvg_price();
					// 如果批次库存不大于10,就全退了
					if (remain.compareTo(new BigDecimal(10)) > 0) {
						quantity = NumberUtil.getRandomNumber(5, 10, 1);
						avg_price = (remain.multiply(avg_price).subtract(quantity.multiply(unit_price)))
								.divide(remain.subtract(quantity), 2, BigDecimal.ROUND_HALF_UP);
						remain = remain.subtract(quantity);
					} else {
						quantity = remain;
						remain = remain.subtract(quantity);
					}
					returnStockBatch.setRemain(remain);
					// 批次的均价单位为分
					returnStockBatch.setAvg_price(avg_price.multiply(new BigDecimal("100")));

					stockBatchMap.put(batch_number, returnStockBatch);

					detail.setBatch_number(batch_number);

				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				SpuStockBean spuStock = null;
				if (spuStockMap.containsKey(spu_id)) {
					spuStock = spuStockMap.get(spu_id);
				} else {
					spuStock = stockCheckService.getSpuStock(spu_id);
					Assert.assertNotEquals(spuStock, null, "获取商品" + spu_id + "的库存信息与预期失败");
				}

				detail.setId(purchase_id);
				detail.setName(sku.getSku_name());
				detail.setSpu_id(spu_id);
				detail.setCategory(sku.getCategory_id_1_name() + "/" + sku.getCategory_id_2_name());
				detail.setStd_unit(sku.getStd_unit_name());
				detail.setSpu_remark(StringUtil.getRandomString(6));

				money = quantity.multiply(unit_price);
				sku_money = sku_money.add(money);

				BigDecimal remain = spuStock.getRemain().subtract(quantity);
				spuStock.setRemain(remain);
				if (remain.compareTo(new BigDecimal("0")) > 0) {
					BigDecimal avg_price = (spuStock.getStock_value().subtract(money)).divide(remain, 2,
							BigDecimal.ROUND_HALF_UP);
					// SPU均价单位为分
					spuStock.setAvg_price(avg_price.multiply(new BigDecimal("100")));
				}
				spuStockMap.put(spu_id, spuStock);

				detail.setQuantity(quantity);
				detail.setMoney(money);
				detail.setUnit_price(unit_price);

				details.add(detail);
			}

			returnStockCreateParam.setDetails(details);
			returnStockCreateParam.setSku_money(sku_money);

			List<ReturnStockCreateParam.Discount> discounts = new ArrayList<ReturnStockCreateParam.Discount>();
			ReturnStockCreateParam.Discount discount = returnStockCreateParam.new Discount();
			discount.setReason(1);
			discount.setAction(2);
			discount.setOperate_time(today);
			discount.setRemark(StringUtil.getRandomString(6));
			BigDecimal delta_money1 = new BigDecimal("-2");
			discount.setMoney(delta_money1.abs());
			discounts.add(discount);

			discount = returnStockCreateParam.new Discount();
			discount.setReason(2);
			discount.setAction(1);
			discount.setOperate_time(today);
			discount.setRemark(StringUtil.getRandomString(6));
			BigDecimal delta_money2 = new BigDecimal("1");
			discount.setMoney(delta_money2.abs());
			discounts.add(discount);

			BigDecimal delta_money = delta_money1.add(delta_money2);

			returnStockCreateParam.setDiscount(discounts);
			returnStockCreateParam.setDelta_money(delta_money);

			String stock_return_sheet_id = returnStockService.newCreateReturnStockSheet(returnStockCreateParam);
			Assert.assertNotEquals(stock_return_sheet_id, null, "新建采购退货单失败");

			String msg = null;
			boolean result = true;
			for (String spu_id : spuStockMap.keySet()) {
				SpuStockBean spuStock = stockCheckService.getSpuStock(spu_id);
				Assert.assertNotEquals(spuStock, null, "获取商品" + spu_id + "的库存信息与预期失败");

				SpuStockBean expetedSpuStock = spuStockMap.get(spu_id);

				if (spuStock.getRemain().compareTo(expetedSpuStock.getRemain()) != 0) {
					msg = String.format("采购退货单%s提交后,商品%s的剩余库存与预期不符,预期:%s,实际:%s", stock_return_sheet_id, spu_id,
							expetedSpuStock.getRemain(), spuStock.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (spuStock.getAvg_price().compareTo(expetedSpuStock.getAvg_price()) != 0) {
					msg = String.format("采购退货单%s提交后,商品%s的库存均价与预期不符,预期:%s,实际:%s", stock_return_sheet_id, spu_id,
							expetedSpuStock.getAvg_price(), spuStock.getAvg_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			// 如果是先进先出,还需要判断下批次的库存
			if (stock_type == 2) {
				for (String batch_number : stockBatchMap.keySet()) {
					StockBatchBean stockBatch = stockCheckService.getStockBatch(batch_number);
					Assert.assertNotEquals(stockBatch, null, "获取" + batch_number + "批次信息失败");

					ReturnStockBatchBean returnStockBatch = stockBatchMap.get(batch_number);
					if (!NumberUtil.roundCompare(returnStockBatch.getAvg_price(), stockBatch.getPrice(),
							new BigDecimal("0.01"))) {
						msg = String.format("采购退货单%s提交后,批次%s的库存均价与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								batch_number, returnStockBatch.getAvg_price(), stockBatch.getPrice());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
					if (!NumberUtil.roundCompare(returnStockBatch.getRemain(), stockBatch.getRemain(),
							new BigDecimal("0.01"))) {
						msg = String.format("采购退货单%s提交后,批次%s的剩余库存数与预期不符,预期:%s,实际:%s", stock_return_sheet_id,
								batch_number, returnStockBatch.getRemain(), stockBatch.getRemain());
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "采购单据提交后,相关商品的库存信息与预期不符");
		} catch (Exception e) {
			logger.error("新建采购退货单遇到错误: ", e);
			Assert.fail("新建采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase09() {
		ReporterCSS.title("测试点: 采购退货单列表,按建单日期[" + today + "~" + today + "]过滤");
		try {
			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStatus(5);
			filterParam.setStart(today);
			filterParam.setEnd(today);
			filterParam.setSearch_text("");
			filterParam.setOffset(0);
			filterParam.setLimit(50);

			List<ReturnStockSheetBean> returnStockSheetList = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStockSheetList, null, "搜索查询采购退货单失败");

			String msg = null;
			boolean result = true;
			for (ReturnStockSheetBean returnStockSheet : returnStockSheetList) {
				if (!returnStockSheet.getDate_time().equals(today)) {
					msg = String.format("采购退货单列表,按建单日期过滤,过滤出了不符合的单据%s,建单时间%s", returnStockSheet.getId(),
							returnStockSheet.getDate_time());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购退货单列表,按建单日期过滤,过滤出的单据不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购退货单遇到错误: ", e);
			Assert.fail("搜索查询采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase10() {
		ReporterCSS.title("测试点: 采购退货单列表,按退货日期[" + tommrow + "~" + tommrow + "]过滤");
		try {
			ReturnStockCreateParam returnStockCreateParam = new ReturnStockCreateParam();
			returnStockCreateParam.setSettle_supplier_id(supplier_id);
			returnStockCreateParam.setSupplier_name(supplier_name);
			returnStockCreateParam.setReturn_sheet_remark(StringUtil.getRandomString(12));
			returnStockCreateParam.setSubmit_time(tommrow);
			returnStockCreateParam.setIs_submit(2);

			Map<String, List<SupplySkuBean>> supplySkusMap = stockService.newSearchSupplySku("d", supplier_id);
			Assert.assertNotEquals(supplySkusMap, null, "采购退货,搜索退货商品失败");

			// 退货只能退此供应商可供应的商品
			List<SupplySkuBean> targetSupplySkus = supplySkusMap.get("target");
			Assert.assertEquals(targetSupplySkus.size() > 0, true, "采购退货,搜索无可退商品");

			// 选择最多4个退货商品
			targetSupplySkus = NumberUtil.roundNumberInList(targetSupplySkus, 4);

			List<ReturnStockCreateParam.Detail> details = new ArrayList<ReturnStockCreateParam.Detail>();
			ReturnStockCreateParam.Detail detail = null;

			BigDecimal quantity = null;
			BigDecimal unit_price = null;
			BigDecimal money = null;
			BigDecimal sku_money = new BigDecimal("0");
			for (SupplySkuBean sku : targetSupplySkus) {
				detail = returnStockCreateParam.new Detail();
				String purchase_id = sku.getSku_id();
				// 如果是先进先出,则要填写批次
				if (stock_type == 2) {
					List<ReturnStockBatchBean> returnStockBatchList = returnStockService
							.searchReturnStockBatch(purchase_id, supplier_id);
					Assert.assertNotEquals(returnStockBatchList, null, "获取指定商品的批次信息失败");
					if (returnStockBatchList.size() == 0) {
						continue;
					}

					ReturnStockBatchBean returnStockBatch = returnStockBatchList.get(0);
					quantity = returnStockBatch.getRemain();
					detail.setBatch_number(returnStockBatch.getBatch_number());
				} else {
					quantity = NumberUtil.getRandomNumber(4, 8, 1);
				}

				detail.setId(purchase_id);
				detail.setName(sku.getSku_name());
				detail.setSpu_id(sku.getSpu_id());
				detail.setCategory(sku.getCategory_id_1_name() + "/" + sku.getCategory_id_2_name());
				detail.setStd_unit(sku.getStd_unit_name());
				detail.setSpu_remark(StringUtil.getRandomString(6));

				unit_price = NumberUtil.getRandomNumber(2, 5, 1);
				money = quantity.multiply(unit_price);
				sku_money = sku_money.add(money);

				detail.setQuantity(quantity);
				detail.setMoney(money);
				detail.setUnit_price(unit_price);

				details.add(detail);

			}

			returnStockCreateParam.setDetails(details);
			returnStockCreateParam.setSku_money(sku_money);

			returnStockCreateParam.setDiscount(new ArrayList<>());
			returnStockCreateParam.setDelta_money(new BigDecimal("10"));

			String stock_return_sheet_id = returnStockService.newCreateReturnStockSheet(returnStockCreateParam);
			Assert.assertNotEquals(stock_return_sheet_id, null, "新建采购退货单失败");

			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(1);
			filterParam.setStatus(5);
			filterParam.setStart(tommrow);
			filterParam.setEnd(tommrow);
			filterParam.setSearch_text("");
			filterParam.setOffset(0);
			filterParam.setLimit(50);

			List<ReturnStockSheetBean> returnStockSheetList = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStockSheetList, null, "按退货日期搜索查询采购退货单失败");

			String msg = null;
			boolean result = true;

			for (ReturnStockSheetBean returnStockSheet : returnStockSheetList) {
				if (!returnStockSheet.getSubmit_time().equals(tommrow)) {
					msg = String.format("采购退货单列表,按退货日期过滤,过滤出了不符合的单据%s,退货时间%s", returnStockSheet.getId(),
							returnStockSheet.getSubmit_time());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			}

			ReturnStockSheetBean tempStockReturnSheet = returnStockSheetList.stream()
					.filter(s -> s.getId().equals(stock_return_sheet_id)).findAny().orElse(null);

			if (tempStockReturnSheet == null) {
				msg = String.format("采购退货单列表,按退货日期过滤,没有过滤出单据%s", stock_return_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			ReporterCSS.step("测试点: 退货单列表,按退货日期+状态值过滤");
			filterParam.setStatus(2);
			returnStockSheetList = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStockSheetList, null, "按退货日期搜索查询采购退货单失败");

			for (ReturnStockSheetBean returnStockSheet : returnStockSheetList) {
				if (!returnStockSheet.getSubmit_time().equals(tommrow) || returnStockSheet.getStatus() != 2) {
					msg = String.format("采购退货单列表,按退货日期过滤+状态值过滤,过滤出了不符合的单据%s,退货时间%s,状态值:%s", returnStockSheet.getId(),
							returnStockSheet.getSubmit_time(), returnStockSheet.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

			}

			tempStockReturnSheet = returnStockSheetList.stream().filter(s -> s.getId().equals(stock_return_sheet_id))
					.findAny().orElse(null);

			if (tempStockReturnSheet == null) {
				msg = String.format("采购退货单列表,按退货日期过滤+状态值,没有过滤出单据%s", stock_return_sheet_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "采购退货单列表,按退货日期过滤,过滤出的单据不符合过滤条件");
		} catch (Exception e) {
			logger.error("搜索查询采购退货单遇到错误: ", e);
			Assert.fail("搜索查询采购退货单遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase11() {
		ReporterCSS.title("测试点: 采购单据列表,使用退货单据状态过滤");
		try {
			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(2);
			filterParam.setStatus(2);
			filterParam.setStart(today);
			filterParam.setEnd(today);
			filterParam.setSearch_text("");
			filterParam.setOffset(0);
			filterParam.setLimit(50);

			List<ReturnStockSheetBean> returnStockSheetList = returnStockService.searchReturnStockSheet(filterParam);
			Assert.assertNotEquals(returnStockSheetList, null, "按退货日期搜索查询采购退货单失败");

			String msg = null;
			boolean result = true;
			for (ReturnStockSheetBean returnStockSheet : returnStockSheetList) {
				if (!returnStockSheet.getDate_time().equals(today) || returnStockSheet.getStatus() != 2) {
					msg = String.format("采购退货单列表,按状态值过滤,过滤出了不符合的单据%s,状态值%s", returnStockSheet.getId(),
							returnStockSheet.getStatus());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "采购退货单列表,按状态过滤,过滤出的单据不符合过滤条件");
		} catch (Exception e) {
			logger.error("导出采购退货单列表遇到错误: ", e);
			Assert.fail("导出采购退货单列表遇到错误: ", e);
		}

	}

	@Test
	public void returnStockTestCase12() {
		ReporterCSS.title("测试点: 导出采购退货单列表");
		try {
			ReturnStockSheetFilterParam filterParam = new ReturnStockSheetFilterParam();
			filterParam.setType(1);
			filterParam.setStatus(5);
			filterParam.setStart(today);
			filterParam.setEnd(today);
			filterParam.setSearch_text("");
			filterParam.setOffset(0);
			filterParam.setLimit(10);
			filterParam.setExport();

			boolean result = returnStockService.exportReturnStockSheet(filterParam);
			Assert.assertEquals(result, true, "导出采购退货单列表失败");
		} catch (Exception e) {
			logger.error("导出采购退货单列表遇到错误: ", e);
			Assert.fail("导出采购退货单列表遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase13() {
		ReporterCSS.title("测试点: 导出采购退货批量导入模板");
		try {
			boolean result = returnStockService.downReturnStockTemplate();
			Assert.assertEquals(result, true, "导出采购退货批量导入模板失败");
		} catch (Exception e) {
			logger.error("导出采购退货批量导入模板遇到错误: ", e);
			Assert.fail("导出采购退货批量导入模板遇到错误: ", e);
		}
	}

	@Test
	public void returnStockTestCase14() {
		ReporterCSS.title("测试点: 采购退货单批量导入");
		try {
			String sheet_id = returnStockService.createReturnStockSheet(supplier_id, supplier_name);
			Assert.assertNotEquals(sheet_id, null, "创建采购退货单失败");

			List<SupplySkuBean> supplySkus = stockService.getSupplySkuList("d", sheet_id);
			Assert.assertNotEquals(supplySkus, null, "采购退货单搜索退货商品失败");

			Assert.assertEquals(supplySkus.size() >= 1, true, "采购退货单搜索退货商品无结果,与预期不符");

			List<ReturnStockSheetImportParam> paramList = new ArrayList<>();
			ReturnStockSheetImportParam param = null;
			for (SupplySkuBean supplySku : supplySkus) {
				param = new ReturnStockSheetImportParam();
				param.setSupplier_id(customer_id);
				param.setReturn_sku_id(supplySku.getSku_id());
				param.setDifferent_price(BigDecimal.ZERO);
				BigDecimal return_amount = NumberUtil.getRandomNumber(4, 10, 1);
				BigDecimal return_unit_price = NumberUtil.getRandomNumber(4, 8, 1);
				param.setReturn_stock_amount(return_amount);
				param.setUnit_price(return_unit_price);
				param.setSku_money(return_amount.multiply(return_unit_price));
				paramList.add(param);
				if (paramList.size() > 10) {
					break;
				}
			}
			boolean result = returnStockService.importReturnStockSheet(paramList);
			Assert.assertEquals(result, true, "采购退货批量导入失败");
		} catch (Exception e) {
			logger.error("采购退货单批量导入遇到错误: ", e);
			Assert.fail("采购退货单批量导入遇到错误: ", e);
		}
	}
}
