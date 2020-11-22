package cn.guanmai.station.delivery;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.delivery.LedgerBean;
import cn.guanmai.station.bean.delivery.param.DeliverySettingParam;
import cn.guanmai.station.bean.delivery.param.LegerAddSkuParam;
import cn.guanmai.station.bean.delivery.param.LegerDeleteSkuParam;
import cn.guanmai.station.bean.delivery.param.LegerUpdateSkuParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.OrderResponseBean;
import cn.guanmai.station.bean.order.OrderResponseBean.Data.NotEnoughInventories;
import cn.guanmai.station.bean.order.param.OrderCreateParam;
import cn.guanmai.station.impl.delivery.DistributeServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.delivery.DistributeService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.station.tools.OrderTool;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Jun 18, 2019 5:00:42 PM 
* @des 套账测试
* @version 1.0 
*/
public class LedgerTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(LedgerTest.class);
	private DistributeService distributeService;
	private OrderService orderService;
	private OrderDetailBean orderDetail;
	private OrderTool orderTool;
	private String order_id;
	private DeliverySettingParam deliverySettingParam;

	@BeforeClass
	public void initData() {
		try {
			Map<String, String> headers = getStationCookie();
			orderTool = new OrderTool(headers);
			orderService = new OrderServiceImpl(headers);
			distributeService = new DistributeServiceImpl(headers);
		} catch (Exception e) {
			logger.error("初始化配送数据遇到错误: ", e);
			Assert.fail("初始化配送数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		try {
			deliverySettingParam = new DeliverySettingParam();
			deliverySettingParam.setSync_add_sku(1);
			deliverySettingParam.setSync_del_order(1);
			deliverySettingParam.setSync_del_sku(1);
			deliverySettingParam.setSync_quantity_from(0);
		} catch (Exception e) {
			logger.error("创建订单遇到错误: ", e);
			Assert.fail("创建订单遇到错误: ", e);
		}
	}

	@Test
	public void ledgerTestCase01() {
		ReporterCSS.title("测试点: 编辑配送单-设置订单同步方式后获取套账单详情并验证信息是否正确");
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			deliverySettingParam.setOrder_id(order_id);

			boolean result = distributeService.submitDeliverySetting(deliverySettingParam);
			Assert.assertEquals(result, true, "编辑配送单-保存订单同步设置失败");
			LedgerBean ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			String msg = null;
			if (!ledger.getId().equals(order_id)) {
				msg = String.format("套账单的单号和订单详情单号不一致,套账单号:%s,订单号:%s", ledger.getId(), order_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_resname = orderDetail.getCustomer().getExtender().getResname();
			if (!ledger.getResname().equals(expected_resname)) {
				msg = String.format("套账单的商户名称和订单详情里的商户名称不一致,套账单:%s,订单详情:%s", ledger.getResname(), expected_resname);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getTotal_price().compareTo(orderDetail.getTotal_price()) != 0) {
				msg = String.format("套账单的下单金额和订单详情里的下单金额不一致 ,套账单:%s,订单详情:%s", ledger.getTotal_price(),
						orderDetail.getTotal_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getReal_price().compareTo(orderDetail.getReal_price()) != 0) {
				msg = String.format("套账单的出库金额和订单详情里的出库金额不一致 ,套账单:%s,订单详情:%s", ledger.getReal_price(),
						orderDetail.getReal_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_address = orderDetail.getCustomer().getAddress();
			if (!ledger.getAddress().equals(expected_address)) {
				msg = String.format("套账单的收货地址和订单详情里的收货地址不一致 ,套账单:%s,订单详情:%s", ledger.getAddress(), expected_address);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getTotal_pay().add(ledger.getFreight()).compareTo(orderDetail.getTotal_pay()) != 0) {
				msg = String.format("套账单的销售额和订单详情里的销售额不一致 ,套账单出:%s,订单详情:%s",
						ledger.getTotal_pay().add(ledger.getFreight()), orderDetail.getTotal_pay());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_receive_begin_time = orderDetail.getCustomer().getReceive_begin_time();
			String actual_receive_begin_time = ledger.getReceive_begin_time().replaceAll("T", " ").substring(0, 16);
			if (!actual_receive_begin_time.equals(expected_receive_begin_time)) {
				msg = String.format("套账单的收货起始时间和订单详情里的收货起始时间不一致 ,套账单:%s,订单详情:%s", actual_receive_begin_time,
						expected_receive_begin_time);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_receive_end_time = orderDetail.getCustomer().getReceive_end_time();
			String actual_receive_end_time = ledger.getReceive_end_time().replaceAll("T", " ").substring(0, 16);
			if (!actual_receive_end_time.equals(expected_receive_end_time)) {
				msg = String.format("套账单的收货结束时间和订单详情里的收货结束始时间不一致 ,套账单:%s,订单详情:%s", actual_receive_end_time,
						expected_receive_end_time);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getFreight().compareTo(orderDetail.getFreight()) != 0) {
				msg = String.format("套账单的运费和订单详情里的运费不一致 ,套账单:%s,订单详情:%s", ledger.getFreight(),
						orderDetail.getFreight());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getSync_add_sku() != 1) {
				msg = String.format("套账单的sync_add_sku对应的值和预期的不一致 ,预期:%s,实际:%s", 1, ledger.getSync_add_sku());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getSync_del_order() != 1) {
				msg = String.format("套账单的sync_del_order对应的值和预期的不一致 ,预期:%s,实际:%s", 1, ledger.getSync_del_order());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getSync_del_sku() != 1) {
				msg = String.format("套账单的sync_del_sku对应的值和预期的不一致 ,预期:%s,实际:%s", 1, ledger.getSync_del_sku());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (ledger.getDetails().size() != orderDetail.getDetails().size()) {
				msg = String.format("套账单的商品总数和订单详情里的商品总数不一致 ,套账单:%s,订单详情:%s", ledger.getDetails().size(),
						orderDetail.getDetails().size());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			for (OrderDetailBean.Detail o_detail : orderDetail.getDetails()) {
				String sku_id = o_detail.getSku_id();
				LedgerBean.Detail l_detail = ledger.getDetails().stream().filter(d -> d.getSku_id().equals(sku_id))
						.findAny().orElse(null);
				if (l_detail == null) {
					msg = String.format("订单%s里的商品%s在套账单里没有找到", order_id, sku_id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (!o_detail.getSku_name().equals(l_detail.getSku_name())) {
					msg = String.format("订单%s里的商品%s名称与套账单里的商品名称不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							o_detail.getSku_name(), l_detail.getSku_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!o_detail.getCategory_title_1().equals(l_detail.getCategory_title_1())) {
					msg = String.format("订单%s里的商品%s所属分类与套账单里的商品所属分类不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							o_detail.getCategory_title_1(), l_detail.getCategory_title_1());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				String expected_specification = o_detail.getSale_ratio() + o_detail.getStd_unit_name_forsale() + "/"
						+ o_detail.getSale_unit_name();
				String actual_specification = l_detail.getSale_ratio() + l_detail.getStd_unit_name() + "/"
						+ l_detail.getSale_unit_name();
				if (!expected_specification.equals(actual_specification)) {
					msg = String.format("订单%s里的商品%s售卖规格与套账单里的售卖规格不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							expected_specification, actual_specification);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (o_detail.getSale_price().compareTo(l_detail.getSale_price()) != 0) {
					msg = String.format("订单%s里的商品%s销售单价与套账单里的销售单价不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							o_detail.getSale_price(), l_detail.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (o_detail.getQuantity().compareTo(l_detail.getQuantity()) != 0) {
					msg = String.format("订单%s里的商品%s下单数与套账单里的下单数不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							o_detail.getQuantity(), l_detail.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				BigDecimal order_std_unit_quantity = o_detail.getReal_quantity().multiply(o_detail.getSale_ratio())
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				if (order_std_unit_quantity.compareTo(l_detail.getReal_weight()) != 0) {
					msg = String.format("订单%s里的商品%s出库数与套账单里的出库数不一致,订单详情:%s,套账单:%s", order_id, sku_id,
							order_std_unit_quantity, l_detail.getReal_weight());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单详细信息和套账单详细信息不一致");
		} catch (Exception e) {
			logger.error("编辑配送单-获取订单同步设置并保存设置过程中遇到错误: ", e);
			Assert.fail("编辑配送单-获取订单同步设置并保存设置过程中遇到错误: ", e);
		}
	}

	@Test
	public void ledgerTestCase02() {
		ReporterCSS.title("测试点: 更新套账单-修改商品");
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			deliverySettingParam.setOrder_id(order_id);

			boolean result = distributeService.submitDeliverySetting(deliverySettingParam);
			Assert.assertEquals(result, true, "编辑配送单-保存订单同步设置失败");
			LedgerBean ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			LedgerBean.Detail detail = NumberUtil.roundNumberInList(ledger.getDetails());

			BigDecimal before_quantity = detail.getQuantity();
			BigDecimal after_quantity = before_quantity.add(new BigDecimal("2.8"));

			BigDecimal before_sale_price = detail.getSale_price();
			BigDecimal after_sale_price = before_sale_price.add(new BigDecimal("2.6"));

			BigDecimal before_real_weight = detail.getReal_weight();
			BigDecimal after_real_weight = before_real_weight.add(new BigDecimal("2.4"));

			BigDecimal raw_id = detail.getRaw_id();
			LegerUpdateSkuParam legerUpdateParam = new LegerUpdateSkuParam();
			legerUpdateParam.setId(detail.getSku_id());
			legerUpdateParam.setType(1);
			legerUpdateParam.setRaw_id(raw_id);
			legerUpdateParam.setOp_type(2);
			legerUpdateParam.setQuantity(after_quantity);
			legerUpdateParam.setQuantity_lock(1);
			legerUpdateParam.setSale_price(after_sale_price);
			legerUpdateParam.setSale_price_lock(1);
			legerUpdateParam.setReal_weight(after_real_weight);
			legerUpdateParam.setReal_weight_lock(1);
			legerUpdateParam
					.setReal_item_price(after_sale_price.multiply(after_real_weight).multiply(detail.getSale_ratio()));

			List<LegerUpdateSkuParam> legerUpdateParamList = Arrays.asList(legerUpdateParam);

			String delivery_id = ledger.getDelivery_id();
			result = distributeService.updateLedgerSku(delivery_id, legerUpdateParamList);
			Assert.assertEquals(result, true, "更新套账单-修改商品失败");

			ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			detail = ledger.getDetails().stream().filter(d -> d.getRaw_id().compareTo(raw_id) == 0).findAny()
					.orElse(null);

			String sku_id = detail.getSku_id();
			String msg = null;
			if (detail.getSale_price().compareTo(after_sale_price) != 0) {
				msg = String.format("套账单里的商品%s,修改后的单价与预期不一致,预期:%s,实际:%s", sku_id, after_sale_price,
						detail.getSale_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detail.getQuantity().compareTo(after_quantity) != 0) {
				msg = String.format("套账单里的商品%s,修改后的下单数与预期不一致,预期:%s,实际:%s", sku_id, after_quantity,
						detail.getQuantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detail.getReal_weight().compareTo(after_real_weight) != 0) {
				msg = String.format("套账单里的商品%s,修改后的下单数与预期不一致,预期:%s,实际:%s", sku_id, detail.getReal_weight(),
						after_real_weight);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改的套账单信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("更新套账单遇到错误: ", e);
			Assert.fail("更新套账单遇到错误: ", e);
		}
	}

	@Test
	public void ledgerTestCase03() {
		ReporterCSS.title("测试点: 更新套账单-删除商品");
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			deliverySettingParam.setOrder_id(order_id);

			boolean result = distributeService.submitDeliverySetting(deliverySettingParam);
			Assert.assertEquals(result, true, "编辑配送单-保存订单同步设置失败");
			LedgerBean ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			LedgerBean.Detail detail = NumberUtil.roundNumberInList(ledger.getDetails());
			BigDecimal raw_id = detail.getRaw_id();
			String delivery_id = ledger.getDelivery_id();

			LegerDeleteSkuParam legerDeleteParam = new LegerDeleteSkuParam();
			legerDeleteParam.setId(detail.getSku_id());
			legerDeleteParam.setOp_type(3);
			legerDeleteParam.setRaw_id(raw_id);
			legerDeleteParam.setType(1);

			List<LegerDeleteSkuParam> legerDeleteParamList = Arrays.asList(legerDeleteParam);
			result = distributeService.deleteLedgerSku(delivery_id, legerDeleteParamList);
			Assert.assertEquals(result, true, "更新套账单-删除商品失败");

			ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			detail = ledger.getDetails().stream().filter(d -> d.getRaw_id().compareTo(raw_id) == 0).findAny()
					.orElse(null);

			Assert.assertEquals(detail, null, "套账单里被删除的商品实际没有删除");
		} catch (Exception e) {
			logger.error("更新套账单遇到错误: ", e);
			Assert.fail("更新套账单遇到错误: ", e);
		}
	}

	@Test
	public void ledgerTestCase04() {
		ReporterCSS.title("测试点: 更新套账单-新增商品");
		try {
			order_id = orderTool.oneStepCreateOrder(4);
			orderDetail = orderService.getOrderDetailById(order_id);
			Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 订单详细信息失败");

			deliverySettingParam.setOrder_id(order_id);

			boolean result = distributeService.submitDeliverySetting(deliverySettingParam);
			Assert.assertEquals(result, true, "编辑配送单-保存订单同步设置失败");
			LedgerBean ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			LedgerBean.Detail detail = NumberUtil.roundNumberInList(ledger.getDetails());
			BigDecimal raw_id = detail.getRaw_id();
			String delivery_id = ledger.getDelivery_id();

			LegerAddSkuParam ledgerAddParam = new LegerAddSkuParam();
			String sku_id = detail.getSku_id();
			ledgerAddParam.setId(sku_id);
			ledgerAddParam.setName(detail.getSku_name());
			ledgerAddParam.setCategory_title_1(detail.getCategory_title_1());
			ledgerAddParam.setSale_unit_name(detail.getSale_unit_name());
			ledgerAddParam.setStd_unit_name(detail.getStd_unit_name());
			ledgerAddParam.setSale_ratio(detail.getSale_ratio());
			BigDecimal quantity = NumberUtil.getRandomNumber(4, 8, 2);
			ledgerAddParam.setQuantity(quantity);
			BigDecimal sale_price = NumberUtil.getRandomNumber(2, 6, 2);
			ledgerAddParam.setSale_price(sale_price);
			BigDecimal real_weight = quantity.multiply(detail.getSale_price()).add(new BigDecimal("0.8"));
			ledgerAddParam.setReal_weight(real_weight);
			ledgerAddParam.setReal_item_price(
					sale_price.divide(detail.getSale_ratio(), 2, BigDecimal.ROUND_HALF_UP).multiply(real_weight));
			ledgerAddParam.setType(2);
			ledgerAddParam.setOp_type(1);

			List<LegerAddSkuParam> legerAddSkuParamList = Arrays.asList(ledgerAddParam);

			result = distributeService.addLedgerSku(delivery_id, legerAddSkuParamList);
			Assert.assertEquals(result, true, "更新套账单-新增商品失败");

			ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			detail = ledger.getDetails().stream()
					.filter(d -> d.getRaw_id().compareTo(raw_id) != 0 && d.getSku_id().equals(sku_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(detail, null, "更新套账单-新增商品,新增的商品没有在套账单中找到");

			String msg = null;
			if (!detail.getSku_name().equals(ledgerAddParam.getName())) {
				msg = String.format("套账单里新增的商品%s名称和输入的不一致,预期%s,实际:%s", sku_id, ledgerAddParam.getName(),
						detail.getSku_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!detail.getCategory_title_1().equals(ledgerAddParam.getCategory_title_1())) {
				msg = String.format("套账单里新增的商品%s所属分类和输入的不一致,预期%s,实际:%s", sku_id, ledgerAddParam.getCategory_title_1(),
						ledgerAddParam.getCategory_title_1(), detail.getSku_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			String expected_specification = ledgerAddParam.getSale_ratio() + ledgerAddParam.getStd_unit_name() + "/"
					+ ledgerAddParam.getSale_unit_name();
			String actual_specification = detail.getSale_ratio() + detail.getStd_unit_name() + "/"
					+ detail.getSale_unit_name();
			if (!expected_specification.equals(actual_specification)) {
				msg = String.format("套账单里新增的商品%s售卖规格与输入的不一致,预期:%s,实际:%s", sku_id, expected_specification,
						actual_specification);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detail.getSale_price().compareTo(ledgerAddParam.getSale_price()) != 0) {
				msg = String.format("套账单里新增的商品%s销售单价与输入的不一致,预期:%s,实际:%s", sku_id, ledgerAddParam.getSale_price(),
						detail.getSale_price());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (detail.getQuantity().compareTo(ledgerAddParam.getQuantity()) != 0) {
				msg = String.format("套账单里新增的商品%s下单数与输入的不一致,预期:%s,实际:%s", sku_id, ledgerAddParam.getQuantity(),
						detail.getQuantity());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (real_weight.compareTo(detail.getReal_weight()) != 0) {
				msg = String.format("套账单里新增的商品%s出库数与输入的不一致,预期:%s,实际:%s", sku_id, real_weight, detail.getReal_weight());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "更新套账单-新增商品,新增商品填写的信息与查询到的信息不一致");
		} catch (Exception e) {
			logger.error("更新套账单遇到错误: ", e);
			Assert.fail("更新套账单遇到错误: ", e);
		}
	}

	@Test
	public void ledgerTestCase05() {
		ReporterCSS.title("测试点: 套账单拉取预下单数");
		try {
			Map<String, BigDecimal> fake_quntity_map = new HashMap<String, BigDecimal>();
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");

			// 随机选取一个正常商户进行下单
			CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
			String address_id = customer.getAddress_id();
			String uid = customer.getId();

			List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
			Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

			Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
			// 随机取一个绑定的运营时间
			OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
			String time_config_id = orderReceiveTime.getTime_config_id();

			Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
					"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C" };
			OrderCreateParam orderCreateParam = orderService.searchOrderSkus(address_id, time_config_id, search_texts,
					10);
			Assert.assertEquals(orderCreateParam != null && orderCreateParam.getDetails().size() > 0, true,
					"下单搜索搜商品列表为空");

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

			List<OrderCreateParam.OrderSku> orderSkus = orderCreateParam.getDetails();
			// 改变下单参数中的 fake_quantity 对应的值(排除组合商品)
			BigDecimal fake_quantity = null;
			for (OrderCreateParam.OrderSku orderSku : orderSkus) {
				String sku_id = orderSku.getSku_id();
				if (orderSku.isIs_combine_goods()) {
					fake_quantity = orderSku.getAmount();
					orderSku.setFake_quantity(fake_quantity);
				} else {
					fake_quantity = orderSku.getAmount().multiply(new BigDecimal("2"));
					orderSku.setFake_quantity(fake_quantity);
				}
				if (fake_quntity_map.containsKey(sku_id)) {
					BigDecimal temp_fake_quntity = fake_quntity_map.get(sku_id);
					fake_quntity_map.put(sku_id, temp_fake_quntity.add(fake_quantity));
				} else {
					fake_quntity_map.put(sku_id, fake_quantity);
				}
			}

			String order_id = null;
			OrderResponseBean orderResponse = orderService.createOrder(orderCreateParam);
			if (orderResponse.getCode() == 0) {
				// 判断下单商品中有无库存不足的商品,有就移除后再下单
				List<NotEnoughInventories> notEnoughInventoriesList = orderResponse.getData()
						.getNot_enough_inventories();
				if (notEnoughInventoriesList.size() > 0) {
					Map<String, OrderCreateParam.CombineGoods> combine_goods_map = orderCreateParam
							.getCombine_goods_map();
					orderSkus = orderCreateParam.getDetails();
					for (NotEnoughInventories skuObj : notEnoughInventoriesList) {
						String sku_id = skuObj.getSku_id();
						fake_quntity_map.remove(sku_id);

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
				order_id = orderResponse.getData().getNew_order_ids().getString(0);
			} else {
				Assert.assertEquals(orderResponse.getMsg(), "ok", "下单失败");
			}

			deliverySettingParam.setOrder_id(order_id);
			deliverySettingParam.setSync_quantity_from(1);

			boolean result = distributeService.submitDeliverySetting(deliverySettingParam);
			Assert.assertEquals(result, true, "编辑配送单-保存订单同步设置失败");
			LedgerBean ledger = distributeService.getLedger(order_id, 2, true);
			Assert.assertNotEquals(ledger, null, "编辑配送单-获取套账单详情失败");

			List<LedgerBean.Detail> ledgerDetails = ledger.getDetails();
			String msg = null;
			BigDecimal expected_fake_quantity = null;
			BigDecimal actual_fake_quantity = null;
			for (LedgerBean.Detail ledgerDetail : ledgerDetails) {
				String sku_id = ledgerDetail.getSku_id();
				expected_fake_quantity = fake_quntity_map.get(sku_id);
				actual_fake_quantity = ledgerDetail.getQuantity();
				if (expected_fake_quantity.compareTo(actual_fake_quantity) != 0) {
					msg = String.format("套账单%s里的商品%s下单数与预期不一致,预期:%s,实际:%s", order_id, sku_id, expected_fake_quantity,
							actual_fake_quantity);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		} catch (Exception e) {
			logger.error("下单过程中遇到错误: ", e);
			Assert.fail("下单过程中遇到错误: ", e);
		}
	}

}
