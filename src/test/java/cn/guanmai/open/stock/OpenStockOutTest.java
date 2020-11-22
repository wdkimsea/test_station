package cn.guanmai.open.stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.auth.OpenStationInfoBean;
import cn.guanmai.open.bean.product.OpenSaleSkuBean;
import cn.guanmai.open.bean.product.param.OpenSaleSkuFilterParam;
import cn.guanmai.open.bean.stock.OpenStockOutDetailBean;
import cn.guanmai.open.bean.stock.OpenStockOutSheetBean;
import cn.guanmai.open.bean.stock.param.OpenStockOutCommonParam;
import cn.guanmai.open.bean.stock.param.OpenStockOutSheetFilterParam;
import cn.guanmai.open.impl.auth.OpenAuthServiceImpl;
import cn.guanmai.open.impl.product.OpenCategoryServiceImpl;
import cn.guanmai.open.impl.stock.OpenStockOutServiceImpl;
import cn.guanmai.open.interfaces.auth.OpenAuthService;
import cn.guanmai.open.interfaces.product.OpenCategoryService;
import cn.guanmai.open.interfaces.stock.OpenStockOutService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.DeepCloneUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/**
 * @author liming
 * @date 2019年10月31日
 * @time 下午2:38:09
 * @des TODO
 */

public class OpenStockOutTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenStockInTest.class);
	private OpenStockOutService openStockOutService;
	private OpenCategoryService openCategoryService;
	private OrderTool orderTool;
	private OrderService orderService;
	private String customize_station_id;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openStockOutService = new OpenStockOutServiceImpl(access_token);
		openCategoryService = new OpenCategoryServiceImpl(access_token);
		Map<String, String> st_headers = getSt_headers();
		orderTool = new OrderTool(st_headers);
		orderService = new OrderServiceImpl(st_headers);
		OpenAuthService openAuthService = new OpenAuthServiceImpl(access_token);
		try {
			OpenStationInfoBean openStationInfo = openAuthService.stationInfo();
			Assert.assertNotEquals(openStationInfo, null, "获取登录站点信息失败");

			String station_id = openStationInfo.getStation_id();
			customize_station_id = StringUtil.getCustomizedStationId(station_id);
		} catch (Exception e) {
			logger.error("初始化数据遇到错误:", e);
			Assert.fail("初始化数据遇到错误:", e);
		}
	}

	public OpenStockOutCommonParam initOpenStockOutParam() {
		OpenStockOutCommonParam openStockOutCommonParam = new OpenStockOutCommonParam();
		openStockOutCommonParam.setCustomer_name("OPENAPI");
		openStockOutCommonParam.setOut_stock_sheet_id("OPENAPI" + StringUtil.getRandomString(6).toUpperCase());
		List<OpenStockOutCommonParam.Detail> details = new ArrayList<>();

		try {
			OpenSaleSkuFilterParam openSaleSkuFilterParam = new OpenSaleSkuFilterParam();
			openSaleSkuFilterParam.setOffset(0);
			openSaleSkuFilterParam.setLimit(100);
			List<OpenSaleSkuBean> openSaleSkus = openCategoryService.seachSaleSku(openSaleSkuFilterParam);
			Assert.assertNotEquals(openSaleSkus, null, "搜索SKU失败");

			openSaleSkus = openSaleSkus.stream().filter(s -> s.getState() == 1).collect(Collectors.toList());
			Assert.assertEquals(openSaleSkus.size() > 0, true, "没有销售状态的商品");

			OpenStockOutCommonParam.Detail detail = null;
			for (OpenSaleSkuBean openSaleSku : openSaleSkus) {
				detail = openStockOutCommonParam.new Detail();
				detail.setSku_id(openSaleSku.getId());
				detail.setOut_stock_count(NumberUtil.getRandomNumber(5, 15, 2).toString());
				details.add(detail);
				if (details.size() > 10) {
					break;
				}
			}
			openStockOutCommonParam.setDetails(details);
		} catch (Exception e) {
			logger.error("初始化出库单参数遇到错误", e);
			Assert.fail("初始化出库单参数遇到错误", e);
		}
		return openStockOutCommonParam;

	}

	@Test
	public void openStockOutTestCase01() {
		ReporterCSS.title("测试点: 新建出库单");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			boolean result = compareDetailInfo(openStockOutCreateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("1")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase02() {
		ReporterCSS.title("测试点: 修改出库单,删除条目");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();

			Assert.assertEquals(openStockOutCreateParam.getDetails().size() >= 2, true, "SKU条目数不够2条,无法进行删除操作");

			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			OpenStockOutCommonParam openStockOutUpdateParam = openStockOutCreateParam;
			openStockOutUpdateParam.getDetails().remove(0);

			boolean result = openStockOutService.updateStockOutSheet(openStockOutUpdateParam);
			Assert.assertEquals(result, true, "修改出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			result = compareDetailInfo(openStockOutUpdateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("1")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase03() {
		ReporterCSS.title("测试点: 修改出库单,新增条目");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();

			Assert.assertEquals(openStockOutCreateParam.getDetails().size() >= 2, true, "SKU条目数不够2条,无法进行删除操作");

			OpenStockOutCommonParam openStockOutUpdateParam = DeepCloneUtil.deepClone(openStockOutCreateParam);

			openStockOutCreateParam.getDetails().remove(0);

			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean result = openStockOutService.updateStockOutSheet(openStockOutUpdateParam);
			Assert.assertEquals(result, true, "修改出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			result = compareDetailInfo(openStockOutUpdateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("1")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase04() {
		ReporterCSS.title("测试点: 提交出库单");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean result = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(result, true, "提交出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			result = compareDetailInfo(openStockOutCreateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("2")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase05() {
		ReporterCSS.title("测试点: 冲销出库单(未提交)");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean result = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(result, true, "冲销出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			result = compareDetailInfo(openStockOutCreateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("3")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase06() {
		ReporterCSS.title("测试点: 冲销出库单(已提交)");
		try {
			OpenStockOutCommonParam openStockOutCreateParam = initOpenStockOutParam();
			String stock_out_sheet_id = openStockOutService.createStockOutSheet(openStockOutCreateParam);
			Assert.assertNotEquals(stock_out_sheet_id, null, "新建出库单失败");

			boolean result = openStockOutService.submitStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(result, true, "提交出库单失败");

			result = openStockOutService.revertStockOutSheet(stock_out_sheet_id);
			Assert.assertEquals(result, true, "冲销出库单失败");

			OpenStockOutDetailBean openStockOutDetail = openStockOutService.getStockOutDetail(stock_out_sheet_id);
			Assert.assertNotEquals(openStockOutDetail, null, "获取出库单" + stock_out_sheet_id + "详细信息失败");

			result = compareDetailInfo(openStockOutCreateParam, openStockOutDetail);

			if (!openStockOutDetail.getStatus().equals("3")) {
				String msg = String.format("出库单%s的状态值与预期的不一致,预期:%s,实际:%s", stock_out_sheet_id, 1,
						openStockOutDetail.getStatus());
				ReporterCSS.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "新建的出库单填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("新建出库单遇到错误", e);
			Assert.fail("新建出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase07() {
		ReporterCSS.title("测试点: 搜索过滤出库单");
		try {
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenStockOutSheetFilterParam filterParam = new OpenStockOutSheetFilterParam();
			filterParam.setStart_date(today);
			filterParam.setEnd_date(today);

			List<OpenStockOutSheetBean> stockOutSheets = null;
			for (int i = 1; i <= 3; i++) {
				filterParam.setStatus("" + i);
				stockOutSheets = openStockOutService.queryStockOutSheet(filterParam);
				List<OpenStockOutSheetBean> tempStockOutSheets = stockOutSheets.stream()
						.filter(s -> !s.getStatus().equals(filterParam.getStatus())).collect(Collectors.toList());
				Assert.assertEquals(tempStockOutSheets.size(), 0, "按状态搜索过滤出库单,搜索出了不符合条件的数据");
			}

		} catch (Exception e) {
			logger.error("搜索过滤出库单遇到错误", e);
			Assert.fail("搜索过滤出库单遇到错误", e);
		}
	}

	@Test
	public void openStockOutTestCase08() {
		ReporterCSS.title("测试点: 出库订单生成的出库单");
		try {
			String today = TimeUtil.getCurrentTime("yyyy-MM-dd");
			OpenStockOutSheetFilterParam filterParam = new OpenStockOutSheetFilterParam();
			filterParam.setStart_date(today);
			filterParam.setEnd_date(today);
			filterParam.setOffset("0");
			filterParam.setLimit("50");
			filterParam.setStatus("1");
			List<OpenStockOutSheetBean> stockOutSheets = openStockOutService.queryStockOutSheet(filterParam);
			Assert.assertNotEquals(stockOutSheets, null, "搜索过滤出库单列表失败");

			stockOutSheets = stockOutSheets.stream().filter(s -> s.getOut_stock_sheet_id().startsWith("PL")
					|| s.getOut_stock_sheet_id().startsWith(customize_station_id)).collect(Collectors.toList());

			if (stockOutSheets.size() == 0) {
				String order_id = orderTool.oneStepCreateOrder(6);
				Assert.assertNotEquals(order_id, null, "新建订单失败");

				int i = 10;
				while (i-- > 0) {
					stockOutSheets = openStockOutService.queryStockOutSheet(filterParam);
					Assert.assertNotEquals(stockOutSheets, null, "搜索过滤出库单列表失败");

					stockOutSheets = stockOutSheets.stream()
							.filter(s -> s.getOut_stock_sheet_id().startsWith("PL")
									|| s.getOut_stock_sheet_id().startsWith(customize_station_id))
							.collect(Collectors.toList());
					if (stockOutSheets.size() > 0) {
						break;
					}
					Thread.sleep(3000);
				}

				Assert.assertEquals(stockOutSheets.size() > 0, true, "订单生成后30S,对应的出库单没有生成");
			}
			OpenStockOutSheetBean stockOutSheet = NumberUtil.roundNumberInList(stockOutSheets);
			String out_stock_sheet_id = stockOutSheet.getOut_stock_sheet_id();
			OrderDetailBean orderDetail = orderService.getOrderDetailById(out_stock_sheet_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单详情失败");

			if (orderDetail.getStatus() < 15) {
				boolean result = orderService.updateOrderState(out_stock_sheet_id, 15);
				Assert.assertEquals(result, true, "修改订单 " + out_stock_sheet_id + "订单失败");
			}

			boolean result = openStockOutService.submitStockOutSheet(out_stock_sheet_id);
			Assert.assertEquals(result, true, "提交出库单失败");
		} catch (Exception e) {
			logger.error("搜索过滤出库单遇到错误", e);
			Assert.fail("搜索过滤出库单遇到错误", e);
		}
	}

	public boolean compareDetailInfo(OpenStockOutCommonParam stockOutCommonParam,
			OpenStockOutDetailBean stockOutDetail) {
		String msg = null;
		boolean result = true;
		String stock_out_sheet_id = stockOutCommonParam.getOut_stock_sheet_id();
		if (!stock_out_sheet_id.equals(stockOutDetail.getOut_stock_sheet_id())) {
			msg = String.format("出库单的ID与预期不符,预期:%s,实际:%s", stock_out_sheet_id, stockOutDetail.getOut_stock_sheet_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!stockOutCommonParam.getCustomer_name().equals(stockOutDetail.getCustomer_name())) {
			msg = String.format("出库单的出库对象与预期不符,预期:%s,实际:%s", stockOutCommonParam.getCustomer_name(),
					stockOutDetail.getCustomer_id());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<OpenStockOutCommonParam.Detail> paramDetails = stockOutCommonParam.getDetails();
		List<OpenStockOutDetailBean.Detail> resultDetails = stockOutDetail.getDetails();

		for (OpenStockOutCommonParam.Detail paramDetail : paramDetails) {
			String sku_id = paramDetail.getSku_id();
			OpenStockOutDetailBean.Detail resultDetail = resultDetails.stream()
					.filter(s -> s.getSku_id().equals(sku_id)).findAny().orElse(null);
			if (resultDetail == null) {
				msg = String.format("新建出库单%s填写的销售SKU %s在出库单详情中没有找到", stock_out_sheet_id, sku_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			BigDecimal expected_out_stock_count = new BigDecimal(paramDetail.getOut_stock_count());
			BigDecimal actual_out_stock_count = new BigDecimal(resultDetail.getOut_stock_count());
			if (expected_out_stock_count.compareTo(actual_out_stock_count) != 0) {
				msg = String.format("新建出库单%s填写的销售SKU %s的出库数与出库单详情显示不一致,预期:%s,实际:%s", stock_out_sheet_id, sku_id,
						paramDetail.getOut_stock_count(), resultDetail.getOut_stock_count());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

}
