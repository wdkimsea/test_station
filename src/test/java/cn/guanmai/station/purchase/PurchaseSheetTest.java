package cn.guanmai.station.purchase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.bean.order.param.OrderSkuParam;
import cn.guanmai.station.bean.purchase.PurchaseSheetBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetDetailBean;
import cn.guanmai.station.bean.purchase.PurchaseSheetShareBean;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetCreateParam;
import cn.guanmai.station.bean.purchase.param.PurchaseSheetShareParam;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;
import cn.guanmai.station.bean.purchase.param.ReleasePurchaseTaskParam;
import cn.guanmai.station.bean.system.LoginUserInfoBean;
import cn.guanmai.station.bean.system.param.OrderProfileParam;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseSheetServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.impl.system.ProfileServiceImpl;
import cn.guanmai.station.impl.system.LoginUserInfoServiceImpl;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseSheetService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.station.interfaces.system.ProfileService;
import cn.guanmai.station.interfaces.system.LoginUserInfoService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Nov 28, 2018 7:25:20 PM 
* @des 采购单据测试类
* @version 1.0 
*/
public class PurchaseSheetTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseSheetTest.class);
	private OrderService orderService;
	private SimpleDateFormat format;
	private OrderCreateParam orderCreateParam;
	private PurchaseTaskService purchaseTaskService;
	private PurchaserService purchaserService;
	private PurchaseSheetService purchaseSheetService;
	private LoginUserInfoService loginUserInfoService;
	private List<OrderSkuParam> skuArray;
	private String address_id;
	private String time_config_id;
	private String dataStr;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		purchaserService = new PurchaserServiceImpl(headers);
		purchaseSheetService = new PurchaseSheetServiceImpl(headers);
		loginUserInfoService = new LoginUserInfoServiceImpl(headers);
		format = new SimpleDateFormat("yyyy-MM-dd");
		dataStr = format.format(new Date());
		try {
			ProfileService profileService = new ProfileServiceImpl(headers);
			OrderProfileParam orderProfileParam = new OrderProfileParam();
			orderProfileParam.setOrder_create_purchase_task(0);
			boolean result = profileService.updateOrderProfile(orderProfileParam);
			Assert.assertEquals(result, true, "设置订单商品手动进入采购任务失败");

			// 把没有绑定采购员的供应商都绑定采购员
			List<SettleSupplier> settleSupplierArray = purchaserService.getNoBindSettleSupplierArray();
			if (settleSupplierArray.size() > 0) {
				List<String> settle_suppliers = new ArrayList<String>();
				for (SettleSupplier settleSupplier : settleSupplierArray) {
					settle_suppliers.add(settleSupplier.getId());
				}
				List<PurchaserBean> purchasers = purchaserService.searchPurchaser("");
				Assert.assertNotEquals(purchasers, null, "搜索过滤采购员失败");
				if (purchasers.size() > 0) {
					PurchaserBean purchaser = NumberUtil.roundNumberInList(purchasers);
					// 原有绑定的供应商加上没绑定的供应商
					for (SettleSupplier settleSupplier : purchaser.getSettle_suppliers()) {
						settle_suppliers.add(settleSupplier.getId());
					}

					PurchaserParam purchaserParam = new PurchaserParam();
					purchaserParam.setId(purchaser.getId());
					purchaserParam.setName(purchaser.getName());
					purchaserParam.setUsername(purchaser.getUsername());
					purchaserParam.setPhone(purchaser.getPhone());
					purchaserParam.setStatus(purchaser.getStatus());
					purchaserParam.setIs_allow_login(purchaser.getIs_allow_login());
					purchaserParam.setSettle_suppliers(settle_suppliers);

					PurchaserResponseBean purchaserResponse = purchaserService.updatePurchaser(purchaserParam);
					Assert.assertEquals(purchaserResponse.getCode(), 0, "修改采购员信息失败");
				} else {
					String phone = "12" + StringUtil.getRandomNumber(9);
					String username = "AT" + StringUtil.getRandomNumber(6);

					PurchaserParam purchaserParam = new PurchaserParam();
					purchaserParam.setUsername(username);
					purchaserParam.setName(username);
					purchaserParam.setPhone(phone);
					purchaserParam.setPassword("123456");
					purchaserParam.setSettle_suppliers(new ArrayList<>());
					purchaserParam.setIs_allow_login(0);
					purchaserParam.setStatus(1);

					PurchaserResponseBean purchaserResponse = purchaserService.createPurchaser(purchaserParam);
					Assert.assertEquals(purchaserResponse.getMsg(), "ok", "创建采购员失败");
				}
			}
		} catch (Exception e) {
			logger.error("初始化数据失败: ", e);
			Assert.fail("初始化数据失败: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");
			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts, 30);
			Assert.assertEquals(skuArray != null && skuArray.size() > 0, true, "下单搜索搜商品列表为空");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单对象
			orderCreateParam.setAddress_id(address_id);
			orderCreateParam.setUid(uid);
			orderCreateParam.setReceive_begin_time(receive_begin_time);
			orderCreateParam.setReceive_end_time(receive_end_time);
			orderCreateParam.setTime_config_id(time_config_id);
			orderCreateParam.setRemark(StringUtil.getRandomString(6));
			orderCreateParam.setForce(1);

			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);

			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String sku_id = skuObj.getSku_id();
						// 首先找出这个SKU有没有在组合商品里
						List<String> combine_goods_ids = orderSkus.stream()
								.filter(s -> s.getSku_id().equals(sku_id) && s.isIs_combine_goods())
								.map(s -> s.getCombine_goods_id()).distinct().collect(Collectors.toList());

						// 先把这个商品过滤掉
						orderSkus = orderSkus.stream().filter(s -> !s.getSku_id().equals(sku_id))
								.collect(Collectors.toList());

						// 过滤掉组合商品里的其他商品
						for (String combine_goods_id : combine_goods_ids) {
							combine_goods_map.remove(combine_goods_id);
							orderSkus = orderSkus.stream().filter(
									s -> s.isIs_combine_goods() && !s.getCombine_goods_id().equals(combine_goods_id))
									.collect(Collectors.toList());
						}
					}
					Assert.assertEquals(orderSkus.size() > 0, true, "下单商品列表为空,无法进行下单");
					orderCreateParam.setDetails(orderSkus);
					orderResponse = orderService.createOrder(orderCreateParam);
				}
				Assert.assertEquals(orderResponse.getData().getNew_order_ids().size() > 0, true, "正常下单,断言成功");
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}
		} catch (Exception e) {
			logger.error("创建订单遇到错误: ", e);
			Assert.fail("创建订单遇到错误: ", e);
		}
	}

	@Test
	public void purchaseSheetTestCase01() {
		try {
			Reporter.log("发布全部采购任务");

			Thread.sleep(1000);

			String today = format.format(new Date()) + " 00:00:00";

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(today);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			Reporter.log("生成采购单(生成全部)");
			PurchaseSheetCreateParam purchaseSheetParam = new PurchaseSheetCreateParam();
			purchaseSheetParam.setRelease_ids(new ArrayList<>());
			purchaseSheetParam.setBegin_time(today);
			purchaseSheetParam.setEnd_time(today);
			purchaseSheetParam.setQ_type(1);

			JSONArray purchaseSheetIdArray = purchaseTaskService.createPruchaseSheet(purchaseSheetParam);
			Assert.assertEquals(purchaseSheetIdArray.size() > 0, true, "生成采购单(生成全部)失败");

			List<PurchaseSheetBean> purchaseSheetArray = purchaseSheetService.purchaseSheetArray(dataStr, dataStr, "",
					"", "");
			Assert.assertEquals(purchaseSheetArray.size() >= purchaseSheetIdArray.size(), true, "查询到的采购单据数量不对");
		} catch (Exception e) {
			logger.error("验证订单生成的采购任务遇到错误: ", e);
			Assert.fail("验证订单生成的采购任务遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "purchaseSheetTestCase01")
	public void purchaseSheetTestCase02() {
		String dayStr = format.format(new Date());
		try {
			List<PurchaseSheetBean> purchaseSheetArray = purchaseSheetService.purchaseSheetArray(dayStr, dayStr, "", "",
					"3");
			for (PurchaseSheetBean purchaseSheet : purchaseSheetArray) {
				boolean result = purchaseSheetService.deletePurchaseSheet(purchaseSheet.getId());
				Assert.assertEquals(result, true, "删除采购单据失败");
			}
		} catch (Exception e) {
			logger.error("删除采购单据遇到错误: ", e);
			Assert.fail("删除采购单据遇到错误: ", e);
		}
	}

	/**
	 * 先删除所有的采购单据,再发布,就不会重复提交了
	 * 
	 */
	@Test
	public void purchaseSheetTestCase03() {
		try {
			List<PurchaseSheetBean> purchaseSheetArray = purchaseSheetService.purchaseSheetArray(dataStr, dataStr, "",
					"", "3");
			for (PurchaseSheetBean purchaseSheet : purchaseSheetArray) {
				boolean result = purchaseSheetService.deletePurchaseSheet(purchaseSheet.getId());
				Assert.assertEquals(result, true, "删除采购单据失败");
			}

			Reporter.log("发布全部采购任务");
			Thread.sleep(1000);

			String today = format.format(new Date()) + " 00:00:00";

			ReleasePurchaseTaskParam fiterParam = new ReleasePurchaseTaskParam();
			fiterParam.setBegin_time(today);
			fiterParam.setEnd_time(today);
			fiterParam.setTask_ids(new JSONArray());
			fiterParam.setQ_type(1);
			fiterParam.setTask_suggests(new JSONArray());

			boolean result = purchaseTaskService.releasePurchaseTask(fiterParam);
			Assert.assertEquals(result, true, "发布采购任务失败");

			Reporter.log("生成采购单(生成全部)");
			PurchaseSheetCreateParam purchaseSheetParam = new PurchaseSheetCreateParam();
			purchaseSheetParam.setRelease_ids(new ArrayList<>());
			purchaseSheetParam.setBegin_time(today);
			purchaseSheetParam.setEnd_time(today);
			purchaseSheetParam.setQ_type(1);

			JSONArray purchaseSheetIdArray = purchaseTaskService.createPruchaseSheet(purchaseSheetParam);
			Assert.assertEquals(purchaseSheetIdArray.size() > 0, true, "生成采购单(生成全部)失败");

			purchaseSheetArray = purchaseSheetService.purchaseSheetArray(dataStr, dataStr, "", "", "3");
			Assert.assertEquals(purchaseSheetArray.size() >= purchaseSheetIdArray.size(), true, "查询到的采购单据数量不对");

			String sheet_no = purchaseSheetArray != null ? purchaseSheetArray.get(0).getId() : null;
			Assert.assertNotEquals(sheet_no, null, "没有可使用的采购单据");

			PurchaseSheetDetailBean purchaseSheetDetail = purchaseSheetService.getPurchaseSheetDetail(sheet_no);
			Assert.assertNotEquals(purchaseSheetDetail, null, "获取采购单据详情失败");

			result = purchaseSheetService.submitPurchaseSheet(sheet_no);
			Assert.assertEquals(result, true, "提交采购单据失败");
		} catch (Exception e) {
			logger.error("提交采购单据的过程中遇到错误: ", e);
			Assert.fail("提交采购单据的过程中遇到错误: ", e);
		}
	}

	@Test(dependsOnMethods = "purchaseSheetTestCase03")
	public void purchaseSheetTestCase04() {
		try {
			List<PurchaseSheetBean> purchaseSheetArray = purchaseSheetService.purchaseSheetArray(dataStr, dataStr, "",
					"", "2");
			Assert.assertEquals(purchaseSheetArray.size() > 0, true, "查询到的采购单据数量不对");
			String sheet_no = purchaseSheetArray.get(0).getId();
			String token = purchaseSheetService.createSharePurchaseSheet(sheet_no);
			Assert.assertNotEquals(token, null, "分享采购单据失败");

			LoginUserInfoBean loginUserInfo = loginUserInfoService.getLoginUserInfo();
			Assert.assertNotEquals(loginUserInfo, null, "获取登录用户账号相关信息失败");

			String station_id = loginUserInfo.getStation_id();
			int group_id = loginUserInfo.getGroup_id();

			PurchaseSheetShareParam purchaseSheetShareParam = new PurchaseSheetShareParam();
			purchaseSheetShareParam.set__trace_group_id(String.valueOf(group_id));
			purchaseSheetShareParam.setSheet_no(sheet_no);
			purchaseSheetShareParam.setStation_id(station_id);
			purchaseSheetShareParam.setToken(token);

			PurchaseSheetShareBean purchaseSheetShare = purchaseSheetService
					.getSharePurchaseSheet(purchaseSheetShareParam);

			Assert.assertNotEquals(purchaseSheetShare, null, "扫描获取分享的采购单据失败");

		} catch (Exception e) {
			logger.error("分享采购单据的过程中遇到错误: ", e);
			Assert.fail("分享采购单据的过程中遇到错误: ", e);
		}
	}
}
