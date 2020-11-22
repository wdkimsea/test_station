package cn.guanmai.bshop.test;

import cn.guanmai.bshop.bean.*;
import cn.guanmai.bshop.bean.order.PayMethod;
import cn.guanmai.bshop.bean.product.BsProductBean;
import cn.guanmai.bshop.bean.product.BsProductBean.Sku;
import cn.guanmai.bshop.impl.BshopPickUpServiceImpl;
import cn.guanmai.bshop.impl.BshopServiceImpl;
import cn.guanmai.bshop.service.BshopPickUpService;
import cn.guanmai.bshop.service.BshopService;
import cn.guanmai.bshop.tools.LoginBshop;
import cn.guanmai.station.bean.delivery.PickUpStationBean;
import cn.guanmai.station.bean.delivery.param.PickUpStationFilterParam;
import cn.guanmai.station.bean.order.OrderBean;
import cn.guanmai.station.bean.order.param.OrderFilterParam;
import cn.guanmai.station.bean.system.AreaBean;
import cn.guanmai.station.bean.system.CustomizedBean;
import cn.guanmai.station.impl.delivery.PickUpStationServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.AreaServiceImpl;
import cn.guanmai.station.impl.system.CustomizedServiceImpl;
import cn.guanmai.station.interfaces.delivery.PickUpStationService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.AreaService;
import cn.guanmai.station.interfaces.system.CustomizedService;
import cn.guanmai.util.LoginUtil;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.TimeUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yangjinhai on 2019/8/28.
 */
public class BshopPickUpStationTest extends LoginBshop {
	private static Logger logger = LoggerFactory.getLogger(BshopPickUpStationTest.class);
	private BshopPickUpService bshopPickUpService;
	private BshopService bshopService;
	private String todayStr = TimeUtil.getCurrentTime("yyyy-MM-dd");

	// station相关
	private PickUpStationService pickUpStationServer;
	private OrderService orderService;
	private List<PickUpStationBean> pickUpStations;

	private String address_id;

	@BeforeClass
	public void initDate() {
		try {
			Map<String, String> bshop_cookie = getBshopCookie();
			address_id = getAddressId();
			bshopPickUpService = new BshopPickUpServiceImpl(bshop_cookie);
			bshopService = new BshopServiceImpl(bshop_cookie);

			Map<String, String> st_cookie = LoginUtil.loginStation();
			Assert.assertNotEquals(st_cookie, null, "ST登录失败");

			orderService = new OrderServiceImpl(st_cookie);
			AreaService areaService = new AreaServiceImpl(st_cookie);
			// 设置收货方式增加自提点
			pickUpStationServer = new PickUpStationServiceImpl(st_cookie);
			CustomizedService customizedService = new CustomizedServiceImpl(st_cookie);
			CustomizedBean customized = customizedService.getCustomized();
			Assert.assertNotEquals(customized, null, "获取店铺运营设置信息失败");

			customized.setOptional_receive_way(10);
			customized.setCustomer_regist_type(3);
			boolean result = customizedService.updateCustomized(customized);
			Assert.assertTrue(result, "系统设置-店铺运营设置收货方式为自提点设置失败！");

			PickUpStationFilterParam pickUpStationFilterParam = new PickUpStationFilterParam();
			pickUpStationFilterParam.setBusiness_status(1);

			pickUpStations = pickUpStationServer.queryPickUpStations(pickUpStationFilterParam);
			Assert.assertNotEquals(pickUpStations, null, "查询过滤自提点列表失败");

			if (pickUpStations.size() == 0) {
				String city_id = null;
				String district_id = null;
				String area_id = null;
				List<AreaBean> citys = areaService.getAreaDict();
				List<AreaBean.District> districts = null;
				List<AreaBean.District.Area> areas = null;
				OK: for (AreaBean city : citys) {
					districts = city.getDistricts();
					if (districts == null || districts.size() == 0) {
						continue;
					}
					for (AreaBean.District district : districts) {
						areas = district.getAreas();
						if (areas == null || areas.size() == 0) {
							continue;
						}
						city_id = city.getCity_id();
						district_id = district.getDistrict_id();

						AreaBean.District.Area area = areas.get(0);
						area_id = area.getArea_id();
						break OK;
					}

				}
				PickUpStationBean pickUpStation = new PickUpStationBean();
				pickUpStation.setName("东街九号自提点");
				pickUpStation.setPrincipal("张三");
				pickUpStation.setPhone("12589658922");
				pickUpStation.setDistrict_code(city_id);
				pickUpStation.setArea_l1(district_id);
				pickUpStation.setArea_l2(area_id);
				pickUpStation.setAddress("东街九号");
				pickUpStation.setBusiness_status("1");
				result = pickUpStationServer.createPickUpStation(pickUpStation);
				Assert.assertTrue(result, "创建自提点失败");

				pickUpStationFilterParam.setSearch_text(pickUpStation.getName());
				pickUpStations = pickUpStationServer.queryPickUpStations(pickUpStationFilterParam);
				Assert.assertNotEquals(pickUpStations, null, "查询过滤自提点列表失败");

				PickUpStationBean tempPickUpStation = pickUpStations.parallelStream()
						.filter(p -> p.getName().equals(pickUpStation.getName())).findAny().orElse(null);
				Assert.assertNotEquals(tempPickUpStation, null, "新建的自提点没有查询到");
			}
		} catch (Exception e) {
			logger.error("初始化数据出现错误", e);
			Assert.fail("初始化数据出现错误", e);
		}

	}

	@Test
	public void bshopPickUpStationTestCase01() {
		try {
			ReporterCSS.log("测试点:验证商户注册类型和收货类型");
			BshopOptionalInfoBean optionalInfo = bshopPickUpService.getOptionInfo();
			Assert.assertNotEquals(optionalInfo, null, "获取店铺注册,收货方式信息接口返回失败");

			boolean result = true;
			String msg = null;
			if (optionalInfo.getCustomer_regist_type() != 3) {
				msg = String.format("商户注册类型对应的值不是预期的3,而是%s", optionalInfo.getCustomer_regist_type());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (optionalInfo.getOptional_receive_way() != 10) {
				msg = String.format("商户收货方式对应的值不是预期的10,而是%s", optionalInfo.getOptional_receive_way());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商户注册类型和收货类型值不是预期值");
		} catch (Exception e) {
			logger.error("验证商户注册类型和收货类型出现错误", e);
			Assert.fail("验证商户注册类型和收货类型出现错误", e);
		}
	}

	@Test
	public void bshopPickUpStationTestCase02() {
		try {
			ReporterCSS.log("测试点:BSHOP拉取自提点列表");
			List<BshopPickUpBean> bshopPickUpList = bshopPickUpService.getPickUps();
			Assert.assertEquals(bshopPickUpList, null, "BSHOP拉取自提点列表失败");

			boolean result = true;
			String msg = null;
			BshopPickUpBean bshopPickUp = null;
			for (PickUpStationBean pickUpStation : pickUpStations) {
				String id = pickUpStation.getId();
				bshopPickUp = bshopPickUpList.parallelStream().filter(p -> p.getId().equals(id)).findAny().orElse(null);
				if (bshopPickUp == null) {
					msg = String.format("站点的自提点%s在BSHOP商城没有拉取到", pickUpStation.getName());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "站点的一些自提点,在BSHOP商城没有拉取到");
		} catch (Exception e) {
			logger.error("BSHOP拉取自提点列表出现错误", e);
			Assert.fail("BSHOP拉取自提点列表出现错误", e);
		}
	}

	@Test
	public void bshopPickUpStationTestCase03() {
		ReporterCSS.title("测试点: BSHOP下单收货地址选择自提点");
		// 订单列表查询订单
		try {
			List<String> serch_texts = Arrays.asList("A", "E", "I", "O", "U");
			Map<String, BigDecimal> skuMap = new HashMap<>();
			List<BsProductBean> productList = null;
			BigDecimal expected_money = BigDecimal.ZERO;
			OK: for (String text : serch_texts) {
				productList = bshopService.searchSaleProducts(text);
				for (BsProductBean product : productList) {
					List<Sku> skus = product.getSkus();
					Sku sku = NumberUtil.roundNumberInList(skus);
					BigDecimal quantity = NumberUtil.getRandomNumber(5, 15, 0);
					if (!skuMap.containsKey(sku.getSku_id())) {
						if (!sku.isIs_price_timing()) {
							expected_money = expected_money.add(sku.getSale_price().multiply(quantity));
						}
						skuMap.put(sku.getSku_id(), quantity);
						if (skuMap.size() >= 20) {
							break OK;
						}
					}
				}
			}

			Assert.assertEquals(skuMap.size() > 0, true, "没有搜索到下单商品,下单集合为空,无法进行下单");

			CartUpdateResult cartUpdateResult = bshopService.updateCart(skuMap);
			Assert.assertNotEquals(cartUpdateResult, null, "商品加入购物车失败");

			boolean result = bshopService.setPaymethod(PayMethod.One);
			Assert.assertEquals(result, true, "设置付款方式失败");

			result = bshopService.setReceiveTime();
			Assert.assertEquals(result, true, "设置收货时间失败");

			List<BshopPickUpBean> bshopPickUpList = bshopPickUpService.getPickUps();
			Assert.assertNotEquals(bshopPickUpList, null, "BSHOP拉取自提点列表失败");

			Assert.assertEquals(bshopPickUpList.size() > 0, true, "BSHOP拉取自提点列表为空");

			BshopPickUpBean bshopPickUp = bshopPickUpList.get(0);
			String pick_up_id = bshopPickUp.getId();

			result = bshopService.setReceiveAddress(address_id, 2, pick_up_id);
			Assert.assertEquals(result, true, "设置收货方式为自提点自取失败");

			List<String> order_ids = bshopService.submitCart(false);
			Assert.assertNotEquals(order_ids, null, "提交提单失败");

			String order_id = order_ids.get(0);
			OrderFilterParam orderFilterParam = new OrderFilterParam();
			orderFilterParam.setStart_date(todayStr);
			orderFilterParam.setEnd_date(todayStr);
			orderFilterParam.setQuery_type(1);
			orderFilterParam.setOffset(0);
			orderFilterParam.setLimit(50);
			orderFilterParam.setSearch_text(order_id);

			List<OrderBean> orders = orderService.searchOrder(orderFilterParam);
			Assert.assertNotEquals(orders, null, "Station列表搜索过滤失败");

			OrderBean order = orders.parallelStream().filter(o -> o.getId().equals(order_id)).findAny().orElse(null);
			Assert.assertNotEquals(order, null, "订单 " + order_id + " 在此站点没有找到,可能生出在分站点");

			String msg = null;
			if (order.getCustomer().getReceive_way() != 2) {
				msg = String.format("订单%s对应的收货方式与预期不符,预期为自取", order_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			} else {
				if (!order.getCustomer().getPick_up_st_name().equals(bshopPickUp.getName())) {
					msg = String.format("订单%s对应的收货自提点与预期不符,预期:%s,实际:%s", order_id, bshopPickUp.getName(),
							order.getCustomer().getPick_up_st_name());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "订单" + order_id + " 中的收货信息与预期不符");
		} catch (Exception e) {
			logger.error("下单选择自提点遇到错误: ", e);
			Assert.fail("下单选择自提点遇到错误: ", e);
		}
	}
}
