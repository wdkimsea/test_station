package cn.guanmai.station.invoicing;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.invoicing.*;
import cn.guanmai.station.bean.invoicing.param.TransferLogFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetCreateParam;
import cn.guanmai.station.bean.invoicing.param.TransferSheetFilterParam;
import cn.guanmai.station.bean.invoicing.param.TransferStockBatchFilterParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.invoicing.ShelfServiceImpl;
import cn.guanmai.station.impl.invoicing.StockCheckServiceImpl;
import cn.guanmai.station.impl.invoicing.StockTransferServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.invoicing.ShelfService;
import cn.guanmai.station.interfaces.invoicing.StockCheckService;
import cn.guanmai.station.interfaces.invoicing.StockTransferService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.InStockTool;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 仓内移库
 * 
 * @author Administrator
 *
 */
public class FIFOStockTransferTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(FIFOStockTransferTest.class);
	private StockTransferService stockTransferService;
	private StockCheckService stockCheckService;
	private ShelfService shelfService;
	private AsyncService asyncService;
	private InitDataBean initDataBean;
	private String spu_id;// 保存入库商品ID
	private String spu_name;// 保存入库商品名字
	private List<ShelfBean> shelfList;
	private String shelf_id1;
	private String shelf_id2;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");
	private TransferSheetFilterParam transferSheetFilterParam;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		stockTransferService = new StockTransferServiceImpl(headers);
		shelfService = new ShelfServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		stockCheckService = new StockCheckServiceImpl(headers);
		try {
			// 判断登录用户有无货位管理权限
			LoginUserInfoService loginUserInfoService = new LoginUserInfoServiceImpl(headers);
			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户相关信息失败");
			JSONArray permissions = loginUserInfo.getUser_permission();

			Assert.assertNotEquals(permissions, null, "获取站点权限信息失败");
			Assert.assertEquals(permissions.contains("get_shelf"), true, "登录用户没有货位管理权限");

			Assert.assertEquals(permissions.contains("get_inner_transfer_sheet"), true, "登录用户没有仓内移库权限");

			initDataBean = getInitData();
			spu_id = initDataBean.getSpu().getId();
			spu_name = initDataBean.getSpu().getName();

			InStockTool stockInTool = new InStockTool(headers);
			SupplierDetailBean supplier = initDataBean.getSupplier();
			String stock_in_sheet_id = stockInTool.oneStepCreateInStockSheet(supplier.getId(),
					new String[] { spu_name, "a" });
			Assert.assertNotEquals(stock_in_sheet_id, null, "采购入库提交操作失败");

			// 货位查询
			shelfList = shelfService.getShelf();
			Assert.assertNotEquals(shelfList, null, "货位接口获取失败");
			if (shelfList.size() == 0) {

				shelf_id1 = shelfService.addShelf("冷藏");
				Assert.assertNotEquals(shelf_id1, null, "添加货位失败");

				shelf_id2 = shelfService.addShelf("冷冻");
				Assert.assertNotEquals(shelf_id2, null, "添加货位失败");

			} else {
				ShelfBean shelf = shelfList.parallelStream().filter(s -> s.getLevel() == 1).findAny().orElse(null);
				if (shelf.getShelfs() == null) {
					shelf_id1 = shelfService.addShelf("冷藏");
					Assert.assertNotEquals(shelf_id1, null, "添加货位失败");

					shelf_id2 = shelfService.addShelf("冷冻");
					Assert.assertNotEquals(shelf_id1, null, "添加货位失败");
				} else {
					List<ShelfBean.Shelf> shelfDetails = shelf.getShelfs();
					ShelfBean.Shelf shelfDetail = shelfDetails.parallelStream().filter(s -> s.getName().equals("冷藏"))
							.findAny().orElse(null);
					if (shelfDetail == null) {
						shelf_id1 = shelfService.addShelf("冷藏");
						Assert.assertNotEquals(shelf_id1, null, "添加货位失败");
					} else {
						shelf_id1 = shelfDetail.getShelf_id();
					}

					shelfDetail = shelfDetails.parallelStream().filter(s -> s.getName().equals("冷冻")).findAny()
							.orElse(null);
					if (shelfDetail == null) {
						shelf_id2 = shelfService.addShelf("冷冻");
						Assert.assertNotEquals(shelf_id1, null, "添加货位失败");
					} else {
						shelf_id2 = shelfDetail.getShelf_id();
					}
				}
			}
		} catch (Exception e) {
			logger.error("初始化站点数据过程中出现错误: ", e);
			Assert.fail("初始化站点数据过程中出现错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		transferSheetFilterParam = new TransferSheetFilterParam();
		transferSheetFilterParam.setBegin(todayStr);
		transferSheetFilterParam.setEnd(todayStr);
	}

	/**
	 * 校验数据
	 * 
	 * @param transferSheetCreateParam
	 * @param transferSheetDetail
	 * @return
	 */
	public boolean checkResult(TransferSheetCreateParam transferSheetCreateParam,
			TransferSheetDetailBean transferSheetDetail) {
		boolean result = true;
		String msg = null;

		if (transferSheetCreateParam.getStatus() != transferSheetDetail.getStatus()) {
			msg = String.format("仓类移库单%s的状态值与预期不一致,预期:%s,实际:%s", transferSheetDetail.getSheet_no(),
					transferSheetCreateParam.getStatus(), transferSheetDetail.getStatus());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		if (!transferSheetDetail.getRemark().equals(transferSheetCreateParam.getRemark())) {
			msg = String.format("仓类移库单%s的单据备注值与预期不一致,预期:%s,实际:%s", transferSheetDetail.getSheet_no(),
					transferSheetCreateParam.getRemark(), transferSheetDetail.getRemark());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		for (TransferSheetCreateParam.Detail c_detail : transferSheetCreateParam.getDetails()) {
			String batch_num = c_detail.getOut_batch_num();
			TransferSheetDetailBean.Detail g_detail = transferSheetDetail.getDetails().parallelStream()
					.filter(t -> t.getOut_batch_num().equals(batch_num)).findAny().orElse(null);

			if (g_detail == null) {
				msg = String.format("仓内移库单少了商品%s-移出批次%s的记录", c_detail.getSpu_id(), batch_num);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
				continue;
			}

			if (transferSheetCreateParam.getStatus() == 4) {
				if (g_detail.getIn_batch_num() == null || !g_detail.getIn_batch_num().contains("CNYK")) {
					msg = String.format("仓内移库单商品%s-移出批次%s没有生成对应的移入批次号", c_detail.getSpu_id(), batch_num);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			List<TransferSheetDetailBean.Detail.InShelf> inShelfs = g_detail.getIn_shelf();
			if (inShelfs == null || inShelfs.size() == 0) {
				msg = String.format("仓内移库单商品%s-移出批次%s的移入货位值为空,预期货位ID:", c_detail.getSpu_id(), batch_num,
						c_detail.getIn_shelf_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				String actual_in_shelf_id = inShelfs.get(inShelfs.size() - 1).getId();
				if (!String.valueOf(c_detail.getIn_shelf_id()).equals(actual_in_shelf_id)) {
					msg = String.format("仓内移库单商品%s-移出批次%s的移入货位与预期不同,预期货位ID:%s,实际货位ID:%s:", c_detail.getSpu_id(),
							batch_num, c_detail.getIn_shelf_id(), actual_in_shelf_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (c_detail.getOut_amount().compareTo(g_detail.getOut_amount()) != 0) {
				msg = String.format("仓内移库单商品%s-移出批次%s的移除库存数与预期不同,预期移除库存:%s,实际移除库存:%s:", c_detail.getSpu_id(), batch_num,
						c_detail.getOut_amount(), g_detail.getOut_amount());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
		}
		return result;
	}

	/**
	 * 创建移库单,状态设置为保存
	 *
	 * @throws Exception
	 */
	@Test(priority = 0)
	public void createTransferSheetTestcase01() {
		try {
			// 搜索spu信息，获取spuID等信息
			ReporterCSS.log("测试点: 创建移库单,状态设置为保存");
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu(spu_name);
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			TransferSpuBean transferSpu = spuTransfers.parallelStream().filter(s -> s.getId().equals(spu_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(transferSpu, null, "仓内移库SPU商品 " + spu_id + " 没有搜索到");

			TransferStockBatchFilterParam transferStockBatchFilterParam = new TransferStockBatchFilterParam();
			transferStockBatchFilterParam.setSpu_id(spu_id);
			transferStockBatchFilterParam.setLimit(20);

			List<TransferStockBatchBean> stockBatchs = stockTransferService
					.searchStockBatchNumber(transferStockBatchFilterParam);
			Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");

			Assert.assertEquals(stockBatchs.size() > 0, true, "商品 " + spu_id + " 批次列表为空,与预期不符");

			TransferStockBatchBean stockBatch = stockBatchs.parallelStream()
					.filter(s -> s.getBatch_number().contains("JHD")).findFirst().orElse(null);

			if (stockBatch == null)
				stockBatch = stockBatchs.get(0);

			TransferSheetCreateParam stockTransferSheet = new TransferSheetCreateParam();
			stockTransferSheet.setStatus(1);// 创建
			stockTransferSheet.setRemark("状态为保存的移库单");

			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息
			TransferSheetCreateParam.Detail innerDetail = stockTransferSheet.new Detail();

			innerDetail.setSpu_id(transferSpu.getId());
			if (stockBatch.getShelf_id() == null || stockBatch.getShelf_id().equals(shelf_id1)) {
				innerDetail.setIn_shelf_id(new BigDecimal(shelf_id2));
			} else {
				innerDetail.setIn_shelf_id(new BigDecimal(shelf_id1));
			}
			innerDetail.setOut_batch_num(stockBatch.getBatch_number());
			innerDetail.setRemark(spu_name + "商品备注");

			BigDecimal batch_stock = stockBatch.getRemain();
			BigDecimal out_amount = null;
			if (batch_stock.compareTo(new BigDecimal("4")) >= 0) {
				out_amount = batch_stock.divide(new BigDecimal("2")).setScale(2);
			} else {
				out_amount = batch_stock;
			}
			innerDetail.setOut_amount(out_amount);

			innerDetails.add(innerDetail);
			stockTransferSheet.setDetails(innerDetails);

			String transfer_sheet_no = stockTransferService.createStockTransfer(stockTransferSheet);
			Assert.assertNotEquals(transfer_sheet_no, null, "仓内移库单创建失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			boolean result = checkResult(stockTransferSheet, transferSheetDetail);
			Assert.assertEquals(result, true, "新建的仓类移库单信息与预期不一致");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 1)
	public void createTransferSheetTestcase02() {
		ReporterCSS.title("测试点: 创建仓内移库单,添加多条移库商品(不同SPU)");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 2, true, "搜索出来的SPU商品数目小于2,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(1);// 创建
			transferSheetCreateParam.setRemark("多条记录");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 4) {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");
			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			boolean result = checkResult(transferSheetCreateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "新建的仓类移库单信息与预期不一致");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 2)
	public void createTransferSheetTestcase03() {
		ReporterCSS.title("测试点: 创建仓内移库单,状态为送审,添加多条移库商品(不同SPU)");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 2, true, "搜索出来的SPU商品数目小于2,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(2);// 创建
			transferSheetCreateParam.setRemark("多条记录");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 4) {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");
			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			boolean result = checkResult(transferSheetCreateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "新建的仓类移库单信息与预期不一致");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 3)
	public void createTransferSheetTestcase04() {
		ReporterCSS.title("测试点: 创建仓内移库单,状态为确认移库,添加多条移库商品(不同SPU)");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 2, true, "搜索出来的SPU商品数目小于2,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(4);// 创建
			transferSheetCreateParam.setRemark("多条记录");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 4) {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");
			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			boolean result = checkResult(transferSheetCreateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "新建的仓类移库单信息与预期不一致");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 4)
	public void createTransferSheetTestcase05() {
		ReporterCSS.title("测试点: 创建仓内移库单,为同一个SPU的多个批次进行移库操作");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目为0,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(4);// 创建
			transferSheetCreateParam.setRemark("同一个SPU的多个批次");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			boolean find = false;
			List<TransferStockBatchBean> stockBatchs = null;
			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				stockBatchs = stockTransferService.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() <= 1) {
					continue;
				} else {
					find = true;
					break;
				}
			}
			Assert.assertEquals(find, true, "没有找到有多条库存批次的SPU,此用例无法执行");

			for (TransferStockBatchBean transferStockBatch : stockBatchs) {
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}
				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() >= 4) {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);

			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			boolean result = checkResult(transferSheetCreateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "新建的仓类移库单信息与预期不一致");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 5)
	public void createTransferSheetTestcase06() {
		ReporterCSS.title("测试点: 仓内移库后,查询原有批次库存以及新建的仓内移库批次库存");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 2, true, "搜索出来的SPU商品数目小于2,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(4);// 创建
			transferSheetCreateParam.setRemark("查询批次库存");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() >= 4) {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			List<TransferSheetDetailBean.Detail> details = transferSheetDetail.getDetails();
			String out_batch_num = null;
			String in_batch_num = null;
			BigDecimal amount = null;
			StockBatchBean outStockBatch = null;
			StockBatchBean inStockBatch = null;
			String msg = null;
			boolean result = true;
			for (TransferSheetDetailBean.Detail detail : details) {
				out_batch_num = detail.getOut_batch_num();
				in_batch_num = detail.getIn_batch_num();
				amount = detail.getOut_amount();
				outStockBatch = stockCheckService.getStockBatch(out_batch_num);
				if (outStockBatch == null) {
					msg = String.format("获取移动货位的批次%s详细信息失败", out_batch_num);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (outStockBatch.getRemain().compareTo(BigDecimal.ZERO) != 0) {
					msg = String.format("移动货位的批次%s剩余库存数与预期不符,预期:0,实际:%s", out_batch_num, outStockBatch.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!in_batch_num.contains("CNYK")) {
					msg = String.format("移动货位的批次%s对应的移入批次号没有生成", out_batch_num);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}
				inStockBatch = stockCheckService.getStockBatch(in_batch_num);

				if (inStockBatch == null) {
					msg = String.format("获取移动货位的批次%s生成的移入批次详细信息失败", out_batch_num, in_batch_num);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (inStockBatch.getRemain().compareTo(amount) != 0) {
					msg = String.format("移动货位生成的移入批次的库存数与预期不符,预期:%s,实际:%s", in_batch_num, amount,
							inStockBatch.getRemain());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "批次移动货位后,相关的批次库存信息与预期不符");
		} catch (Exception e) {
			logger.error("创建仓内移库单遇到错误", e);
			Assert.fail("创建仓内移库单遇到错误", e);
		}
	}

	/**
	 * 修改移库单
	 * 
	 */
	@Test(priority = 6)
	public void updateTransferSheetTestcase01() {
		ReporterCSS.title("测试点: 修改移库单,为移库单新增条目");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目小于1,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(1);// 创建
			transferSheetCreateParam.setRemark("查询批次库存");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息
			List<TransferSheetCreateParam.Detail> updateDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				if (innerDetails.size() < 1) {
					innerDetails.add(detail);
				}
				if (updateDetails.size() < 4) {
					updateDetails.add(detail);
				} else {
					break;
				}
			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			Assert.assertEquals(updateDetails.size() > innerDetails.size(), true, "无多余的修改仓内移动单的商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			// 修改仓内移库单参数
			TransferSheetCreateParam transferSheetUpdateParam = new TransferSheetCreateParam();
			transferSheetUpdateParam.setDetails(updateDetails);
			transferSheetUpdateParam.setRemark("修改保存");
			transferSheetUpdateParam.setStatus(1);
			transferSheetUpdateParam.setSheet_no(transfer_sheet_no);

			boolean result = stockTransferService.updateTransferShee(transferSheetUpdateParam);
			Assert.assertEquals(result, true, "修改仓内移库单失败");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			result = checkResult(transferSheetUpdateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "修改后的仓内移库单相关信息与预期不符");
		} catch (Exception e) {
			logger.error("修改仓内移库单遇到错误", e);
			Assert.fail("修改仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 7)
	public void updateTransferSheetTestcase02() {
		ReporterCSS.title("测试点: 修改移库单,状态值由保存修改为送审");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目小于1,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(1);// 创建
			transferSheetCreateParam.setRemark("查询批次库存");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 2) {
					break;
				}

			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			// 修改仓内移库单参数
			TransferSheetCreateParam transferSheetUpdateParam = new TransferSheetCreateParam();
			transferSheetUpdateParam.setDetails(innerDetails);
			transferSheetUpdateParam.setRemark("修改状态");
			transferSheetUpdateParam.setStatus(2);
			transferSheetUpdateParam.setSheet_no(transfer_sheet_no);

			boolean result = stockTransferService.updateTransferShee(transferSheetUpdateParam);
			Assert.assertEquals(result, true, "修改仓内移库单失败");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			result = checkResult(transferSheetUpdateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "修改后的仓内移库单相关信息与预期不符");
		} catch (Exception e) {
			logger.error("修改仓内移库单遇到错误", e);
			Assert.fail("修改仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 8)
	public void updateTransferSheetTestcase03() {
		ReporterCSS.title("测试点: 修改移库单,状态值由送审修改为移库");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目小于1,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(2);// 创建
			transferSheetCreateParam.setRemark("送审");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 2) {
					break;
				}

			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			// 修改仓内移库单参数
			TransferSheetCreateParam transferSheetUpdateParam = new TransferSheetCreateParam();
			transferSheetUpdateParam.setDetails(innerDetails);
			transferSheetUpdateParam.setRemark("修改状态,状态改为确认移库");
			transferSheetUpdateParam.setStatus(4);
			transferSheetUpdateParam.setSheet_no(transfer_sheet_no);

			boolean result = stockTransferService.updateTransferShee(transferSheetUpdateParam);
			Assert.assertEquals(result, true, "修改仓内移库单失败");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			result = checkResult(transferSheetUpdateParam, transferSheetDetail);
			Assert.assertEquals(result, true, "修改后的仓内移库单相关信息与预期不符");
		} catch (Exception e) {
			logger.error("修改仓内移库单遇到错误", e);
			Assert.fail("修改仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 9)
	public void updateTransferSheetTestcase04() {
		ReporterCSS.title("测试点: 修改移库单,状态值改为审核不通过");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目小于1,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(2);// 创建
			transferSheetCreateParam.setRemark("送审");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 2) {
					break;
				}

			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			// 修改仓内移库单参数
			TransferSheetCreateParam transferSheetUpdateParam = new TransferSheetCreateParam();
			transferSheetUpdateParam.setSheet_no(transfer_sheet_no);
			transferSheetUpdateParam.setStatus(3);

			boolean result = stockTransferService.updateTransferShee(transferSheetUpdateParam);
			Assert.assertEquals(result, true, "修改仓内移库单失败");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			Assert.assertEquals(transferSheetDetail.getStatus(), transferSheetUpdateParam.getStatus(),
					"仓内移库单状态改为审核不通过,状态值与预期不符");
		} catch (Exception e) {
			logger.error("修改仓内移库单遇到错误", e);
			Assert.fail("修改仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 10)
	public void updateTransferSheetTestcase05() {
		ReporterCSS.title("测试点: 修改移库单,状态值改为冲销");
		try {
			List<TransferSpuBean> spuTransfers = stockTransferService.searchTransferSpu("a");
			Assert.assertNotEquals(spuTransfers, null, "仓内移库搜索SPU商品失败");

			Assert.assertEquals(spuTransfers.size() >= 1, true, "搜索出来的SPU商品数目小于1,不具备往下执行的条件");

			String spu_id = null;
			TransferStockBatchFilterParam transferStockBatchFilterParam = null;

			TransferSheetCreateParam transferSheetCreateParam = new TransferSheetCreateParam();
			transferSheetCreateParam.setStatus(2);// 创建
			transferSheetCreateParam.setRemark("送审");
			List<TransferSheetCreateParam.Detail> innerDetails = new ArrayList<>();// 保存details信息

			for (TransferSpuBean spuTransfer : spuTransfers) {
				spu_id = spuTransfer.getId();
				transferStockBatchFilterParam = new TransferStockBatchFilterParam();
				transferStockBatchFilterParam.setSpu_id(spu_id);
				transferStockBatchFilterParam.setLimit(20);

				List<TransferStockBatchBean> stockBatchs = stockTransferService
						.searchStockBatchNumber(transferStockBatchFilterParam);
				Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
				if (stockBatchs.size() == 0) {
					continue;
				}
				TransferStockBatchBean transferStockBatch = stockBatchs.get(0);
				TransferSheetCreateParam.Detail detail = transferSheetCreateParam.new Detail();
				if (transferStockBatch.getShelf_id() == null || transferStockBatch.getShelf_id().equals(shelf_id1)) {
					detail.setIn_shelf_id(new BigDecimal(shelf_id2));
				} else {
					detail.setIn_shelf_id(new BigDecimal(shelf_id1));
				}

				detail.setOut_amount(transferStockBatch.getRemain());
				detail.setOut_batch_num(transferStockBatch.getBatch_number());
				detail.setRemark(StringUtil.getRandomString(6));
				detail.setSpu_id(spu_id);
				innerDetails.add(detail);
				if (innerDetails.size() > 2) {
					break;
				}

			}
			Assert.assertEquals(innerDetails.size() > 0, true, "无可用移动商品批次");

			transferSheetCreateParam.setDetails(innerDetails);
			String transfer_sheet_no = stockTransferService.createStockTransfer(transferSheetCreateParam);
			Assert.assertNotEquals(transfer_sheet_no, null, "创建仓内移库单失败");

			// 根据创建成功返回的sheet_no 搜索移库列表,验证是否创建成功
			transferSheetFilterParam.setQ(transfer_sheet_no);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			TransferSheetBean transferSheet = transferSheets.parallelStream()
					.filter(tr -> tr.getSheet_no().equals(transfer_sheet_no)).findAny().orElse(null);
			Assert.assertNotEquals(transferSheet, null, "新建的仓内移库单 " + transfer_sheet_no + " ,在仓类移库中没有找到");

			// 修改仓内移库单参数
			TransferSheetCreateParam transferSheetUpdateParam = new TransferSheetCreateParam();
			transferSheetUpdateParam.setSheet_no(transfer_sheet_no);
			transferSheetUpdateParam.setStatus(5);

			boolean result = stockTransferService.updateTransferShee(transferSheetUpdateParam);
			Assert.assertEquals(result, true, "修改仓内移库单失败");

			TransferSheetDetailBean transferSheetDetail = stockTransferService
					.getTransferSheetDetail(transfer_sheet_no);
			Assert.assertNotEquals(transferSheetDetail, null, "获取仓内移库单 " + transfer_sheet_no + " 详细信息失败");

			Assert.assertEquals(transferSheetDetail.getStatus(), transferSheetUpdateParam.getStatus(),
					"仓内移库单状态改为冲销,状态值与预期不符");
		} catch (Exception e) {
			logger.error("修改仓内移库单遇到错误", e);
			Assert.fail("修改仓内移库单遇到错误", e);
		}
	}

	@Test(priority = 11)
	public void searchTransferStockBatchTest01() {
		ReporterCSS.title("测试点: 仓内移库,搜索过滤批次信息");
		try {
			TransferStockBatchFilterParam transferStockBatchFilterParam = new TransferStockBatchFilterParam();
			transferStockBatchFilterParam.setSpu_id(spu_id);
			transferStockBatchFilterParam.setShelf_id(shelf_id1);
			transferStockBatchFilterParam.setLimit(20);

			List<TransferStockBatchBean> stockBatchs = stockTransferService
					.searchStockBatchNumber(transferStockBatchFilterParam);
			Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");

			String msg = null;
			boolean result = true;
			for (TransferStockBatchBean stockBatch : stockBatchs) {
				if (stockBatch.getShelf_id() != null && stockBatch.getShelf_id().equals(shelf_id1)) {
					continue;
				} else {
					msg = String.format("仓内移库按货位过滤,过滤出了不符合过滤条件的数据");
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			transferStockBatchFilterParam = new TransferStockBatchFilterParam();
			transferStockBatchFilterParam.setSpu_id(spu_id);
			transferStockBatchFilterParam.setLimit(20);
			stockBatchs = stockTransferService.searchStockBatchNumber(transferStockBatchFilterParam);
			Assert.assertNotEquals(stockBatchs, null, "仓内移库搜索对应SPU商品库存批次失败");
			if (stockBatchs.size() > 0) {
				String supplier_id = stockBatchs.get(0).getSettle_supplier_id();
				String batch_id = stockBatchs.get(0).getBatch_number();
				transferStockBatchFilterParam.setQ(supplier_id);

				stockBatchs = stockTransferService.searchStockBatchNumber(transferStockBatchFilterParam);
				for (TransferStockBatchBean stockBatch : stockBatchs) {
					if (!stockBatch.getSettle_supplier_id().equals(supplier_id)) {
						msg = String.format("仓内移库按供应商过滤,过滤出了不符合过滤条件的数据");
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}

				transferStockBatchFilterParam.setQ(batch_id);
				stockBatchs = stockTransferService.searchStockBatchNumber(transferStockBatchFilterParam);
				for (TransferStockBatchBean stockBatch : stockBatchs) {
					if (!stockBatch.getSettle_supplier_id().equals(supplier_id)) {
						msg = String.format("仓内移库按库存批次过滤,过滤出了不符合过滤条件的数据");
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "仓内移库,搜索过滤批次,过滤结果与过滤条件不符");
		} catch (Exception e) {
			logger.error("仓内移库,搜索过滤批次信息遇到错误", e);
			Assert.fail("仓内移库,搜索过滤批次信息遇到错误", e);
		}
	}

	@Test(priority = 12)
	public void searchTransferSheetTest01() {
		ReporterCSS.title("测试点: 仓内移库,搜索过滤移库单,按状态过滤");
		try {
			transferSheetFilterParam.setStatus(1);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			List<TransferSheetBean> tempTransferSheets = transferSheets.parallelStream().filter(t -> t.getStatus() != 1)
					.collect(Collectors.toList());

			Assert.assertEquals(tempTransferSheets.size(), 0, "搜索过滤仓内移库单,搜索未移库的仓内移库单,过滤出了不符合过滤条件的移库单");

		} catch (Exception e) {
			logger.error("仓内移库,搜索过滤移库单遇到错误", e);
			Assert.fail("仓内移库,搜索过滤移库单遇到错误", e);
		}
	}

	@Test(priority = 13)
	public void searchTransferSheetTest02() {
		ReporterCSS.title("测试点: 仓内移库,搜索过滤移库单,按状态过滤");
		try {
			transferSheetFilterParam.setStatus(4);
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			List<TransferSheetBean> tempTransferSheets = transferSheets.parallelStream().filter(t -> t.getStatus() != 4)
					.collect(Collectors.toList());

			Assert.assertEquals(tempTransferSheets.size(), 0, "搜索过滤仓内移库单,搜索已移库的仓内移库单,过滤出了不符合过滤条件的移库单");

		} catch (Exception e) {
			logger.error("仓内移库,搜索过滤移库单遇到错误", e);
			Assert.fail("仓内移库,搜索过滤移库单遇到错误", e);
		}
	}

	@Test(priority = 13)
	public void searchTransferSheetTest03() {
		ReporterCSS.title("测试点: 仓内移库,搜索过滤移库单,按移库单号");
		try {
			List<TransferSheetBean> transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
			Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");

			if (transferSheets.size() > 0) {
				String sheet_no = transferSheets.get(0).getSheet_no();
				transferSheetFilterParam.setQ(sheet_no);
				transferSheets = stockTransferService.searchTransferSheet(transferSheetFilterParam);
				Assert.assertNotEquals(transferSheets, null, "仓内移库列表搜索过滤失败");
				List<TransferSheetBean> tempTransferSheets = transferSheets.parallelStream()
						.filter(t -> !t.getSheet_no().equals(sheet_no)).collect(Collectors.toList());

				Assert.assertEquals(tempTransferSheets.size(), 0, "搜索过滤仓内移库单,按移库单号过滤,过滤出了不符合过滤条件的移库单");
			}
		} catch (Exception e) {
			logger.error("仓内移库,搜索过滤移库单遇到错误", e);
			Assert.fail("仓内移库,搜索过滤移库单遇到错误", e);
		}
	}

	/**
	 * 异步任务,导出仓内移库
	 *
	 * @throws Exception
	 */
	@Test(priority = 90)
	public void exportStockInTransferTestcase13() {
		try {
			ReporterCSS.title("测试点: 导出仓内移库列表");
			BigDecimal task_id = stockTransferService.exportTransferSheets(transferSheetFilterParam);
			Assert.assertNotEquals(task_id, null, "导出仓内移库列表,异步任务创建失败");
			boolean reuslt = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(reuslt, true, "导出仓内移库列表,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出仓内移库列表遇到错误: ", e);
			Assert.fail("导出仓内移库列表遇到错误: ", e);
		}
	}

	@Test(priority = 95)
	public void searchTransferLogTest01() {
		try {
			ReporterCSS.title("测试点: 查询仓内移库记录");
			TransferLogFilterParam transferLogFilterParam = new TransferLogFilterParam();
			transferLogFilterParam.setBegin(todayStr);
			transferLogFilterParam.setEnd(todayStr);
			List<TransferLogBean> transferLogs = stockTransferService.searchTransferLog(transferLogFilterParam);

			Assert.assertNotEquals(transferLogs, null, "查询仓内移库记录失败");
		} catch (Exception e) {
			logger.error("导出仓内移库列表遇到错误: ", e);
			Assert.fail("导出仓内移库列表遇到错误: ", e);
		}
	}

	@Test(priority = 96)
	public void searchTransferLogTest02() {
		try {
			ReporterCSS.title("测试点: 查询仓内移库记录");
			TransferLogFilterParam transferLogFilterParam = new TransferLogFilterParam();
			transferLogFilterParam.setBegin(todayStr);
			transferLogFilterParam.setEnd(todayStr);
			transferLogFilterParam.setQ("茼蒿");
			List<TransferLogBean> transferLogs = stockTransferService.searchTransferLog(transferLogFilterParam);

			Assert.assertNotEquals(transferLogs, null, "查询仓内移库记录失败");
		} catch (Exception e) {
			logger.error("导出仓内移库列表遇到错误: ", e);
			Assert.fail("导出仓内移库列表遇到错误: ", e);
		}
	}

	@Test(priority = 97)
	public void exportTransferLogTest01() {
		try {
			ReporterCSS.title("测试点: 导出仓内移库记录");
			TransferLogFilterParam transferLogFilterParam = new TransferLogFilterParam();
			transferLogFilterParam.setBegin(todayStr);
			transferLogFilterParam.setEnd(todayStr);
			BigDecimal task_id = stockTransferService.exportTransferLogs(transferLogFilterParam);
			Assert.assertNotEquals(task_id, null, "导出仓类移库记录异步任务创建失败");

			boolean reuslt = asyncService.getAsyncTaskResult(task_id, "导出成功");
			Assert.assertEquals(reuslt, true, "导出仓内移库列表,异步任务执行失败");
		} catch (Exception e) {
			logger.error("导出仓内移库列表遇到错误: ", e);
			Assert.fail("导出仓内移库列表遇到错误: ", e);
		}
	}
}
