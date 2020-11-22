package cn.guanmai.station.system;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.system.FreightAddressBean;
import cn.guanmai.station.bean.system.FreightBean;
import cn.guanmai.station.bean.system.FreightDetailBean;
import cn.guanmai.station.bean.system.FreightSaleMenuBean;
import cn.guanmai.station.impl.system.FreightServiceImpl;
import cn.guanmai.station.interfaces.system.FreightService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年5月20日 下午5:22:50
 * @description:
 * @version: 1.0
 */

public class FreightTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(FreightTest.class);
	private FreightService freightService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		freightService = new FreightServiceImpl(headers);
	}

	public FreightDetailBean initFreightDetail() {
		FreightDetailBean freightDetail = new FreightDetailBean();

		FreightDetailBean.DeliveryFreight delivery_freight = freightDetail.new DeliveryFreight();
		delivery_freight.setMin_total_price(new BigDecimal("5"));

		List<FreightDetailBean.DeliveryFreight.Section> delivery_freight_section_list = new ArrayList<>();
		FreightDetailBean.DeliveryFreight.Section delivery_freight_section_1 = delivery_freight.new Section();
		delivery_freight_section_1.setFreight(new BigDecimal("2"));
		delivery_freight_section_1.setMin(new BigDecimal("5"));
		delivery_freight_section_1.setMax(new BigDecimal("10"));

		delivery_freight_section_list.add(delivery_freight_section_1);

		FreightDetailBean.DeliveryFreight.Section delivery_freight_section_2 = delivery_freight.new Section();
		delivery_freight_section_2.setFreight(new BigDecimal("0"));
		delivery_freight_section_2.setMin(new BigDecimal("10"));
		delivery_freight_section_2.setMax(new BigDecimal("0"));

		delivery_freight_section_list.add(delivery_freight_section_2);
		delivery_freight.setSection(delivery_freight_section_list);
		freightDetail.setDelivery_freight(delivery_freight);

		// 自提点
		FreightDetailBean.PickUpFreight pick_up_freight = freightDetail.new PickUpFreight();
		pick_up_freight.setMin_total_price(new BigDecimal("10"));

		List<FreightDetailBean.PickUpFreight.Section> pick_up_freight_section_list = new ArrayList<>();
		FreightDetailBean.PickUpFreight.Section pick_up_freight_section_1 = pick_up_freight.new Section();
		pick_up_freight_section_1.setFreight(new BigDecimal("1"));
		pick_up_freight_section_1.setMin(new BigDecimal("10"));
		pick_up_freight_section_1.setMax(new BigDecimal("20"));

		pick_up_freight_section_list.add(pick_up_freight_section_1);

		FreightDetailBean.PickUpFreight.Section pick_up_freight_section_2 = pick_up_freight.new Section();
		pick_up_freight_section_2.setFreight(new BigDecimal("0"));
		pick_up_freight_section_2.setMin(new BigDecimal("20"));
		pick_up_freight_section_2.setMax(new BigDecimal("0"));

		pick_up_freight_section_list.add(pick_up_freight_section_2);
		pick_up_freight.setSection(pick_up_freight_section_list);
		freightDetail.setPick_up_freight(pick_up_freight);

		return freightDetail;
	}

	@Test
	public void freightTestCase01() {
		ReporterCSS.title("测试点: 拉取运费模板列表");
		try {
			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");
		} catch (Exception e) {
			logger.error("拉取运费模板列表遇到错误: ", e);
			Assert.fail("拉取运费模板列表遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase02() {
		ReporterCSS.title("测试点: 新建运费模板,不配置商户");
		String id = null;
		String name = "AT_" + StringUtil.getRandomNumber(6);
		try {
			FreightDetailBean freightDetail = initFreightDetail();

			freightDetail.setName(name);

			boolean result = freightService.createFreight(freightDetail, new ArrayList<BigDecimal>());
			Assert.assertEquals(result, true, "创建运费模板失败");

			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean freight = freights.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(freight, null, "新建的运费模板:" + name + " 没有找到");

			id = freight.getId();
			FreightDetailBean freightDetailResult = freightService.getFreightDetail(id);
			Assert.assertNotEquals(freightDetailResult, null, "获取运费模板详细信息失败");

			result = compareData(freightDetail, freightDetailResult);
			Assert.assertEquals(result, true, "新建配送模板输入信息与查看到的不一致");
		} catch (Exception e) {
			logger.error("创建运费模板列表遇到错误: ", e);
			Assert.fail("创建运费模板列表遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase03() {
		ReporterCSS.title("测试点: 新建运费模板,并配置商户");
		String name = "AT_" + StringUtil.getRandomNumber(6);
		String id = null;
		try {
			List<FreightAddressBean> freightAddressList = freightService.getFreightAddressList();
			Assert.assertNotEquals(freightAddressList, null, "运费模板商户列表拉取失败");

			FreightAddressBean freightAddress = NumberUtil.roundNumberInList(freightAddressList);
			BigDecimal address_id = freightAddress.getAddress_id();
			List<BigDecimal> address_ids = new ArrayList<BigDecimal>();
			address_ids.add(address_id);

			FreightDetailBean freightDetail = initFreightDetail();
			freightDetail.setName(name);

			boolean result = freightService.createFreight(freightDetail, address_ids);
			Assert.assertEquals(result, true, "创建运费模板失败");

			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean freight = freights.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(freight, null, "新建的运费模板:" + name + " 没有找到");

			id = freight.getId();
			FreightDetailBean freightDetailResult = freightService.getFreightDetail(id);
			Assert.assertNotEquals(freightDetailResult, null, "获取运费模板详细信息失败");

			result = compareData(freightDetail, freightDetailResult);
			Assert.assertEquals(result, true, "新建配送模板输入信息与查看到的不一致");

			freightAddressList = freightService.getFreightAddressList();
			Assert.assertNotEquals(freightAddressList, null, "运费模板商户列表拉取失败");

			freightAddress = freightAddressList.stream().filter(f -> f.getAddress_id().compareTo(address_id) == 0)
					.findAny().orElse(null);

			Assert.assertEquals(freightAddress.getFreight_id(), id, "商户设置的运费模板没有成功");
		} catch (Exception e) {
			logger.error("新建运费模板列表遇到错误: ", e);
			Assert.fail("新建运费模板列表遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase04() {
		ReporterCSS.title("测试点: 修改运费模板,配置商户");
		String name = "AT_" + StringUtil.getRandomNumber(6);
		String id = null;
		try {
			FreightDetailBean freightDetail = new FreightDetailBean();
			freightDetail.setName(name);
			FreightDetailBean.PickUpFreight pickUpFreight = freightDetail.new PickUpFreight();
			FreightDetailBean.DeliveryFreight deliveryFreight = freightDetail.new DeliveryFreight();
			pickUpFreight.setMin_total_price(new BigDecimal("0"));
			deliveryFreight.setMin_total_price(new BigDecimal("0"));

			List<FreightDetailBean.PickUpFreight.Section> pickUpFreightSectionList = new ArrayList<>();
			FreightDetailBean.PickUpFreight.Section pickUpFreightSection = pickUpFreight.new Section();
			pickUpFreightSection.setFreight(new BigDecimal("0"));
			pickUpFreightSection.setMin(new BigDecimal("0"));
			pickUpFreightSection.setMax(new BigDecimal("0"));
			pickUpFreightSectionList.add(pickUpFreightSection);
			pickUpFreight.setSection(pickUpFreightSectionList);

			List<FreightDetailBean.DeliveryFreight.Section> deliveryFreightSectionList = new ArrayList<>();
			FreightDetailBean.DeliveryFreight.Section deliveryFreightSection = deliveryFreight.new Section();
			deliveryFreightSection.setFreight(new BigDecimal("0"));
			deliveryFreightSection.setMin(new BigDecimal("0"));
			deliveryFreightSection.setMax(new BigDecimal("0"));
			deliveryFreightSectionList.add(deliveryFreightSection);
			deliveryFreight.setSection(deliveryFreightSectionList);

			freightDetail.setDelivery_freight(deliveryFreight);
			freightDetail.setPick_up_freight(pickUpFreight);

			boolean result = freightService.createFreight(freightDetail, new ArrayList<BigDecimal>());
			Assert.assertEquals(result, true, "创建运费模板失败");

			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean freight = freights.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(freight, null, "新建的运费模板:" + name + " 没有找到");

			id = freight.getId();
			FreightDetailBean freightDetailUpdate = initFreightDetail();
			freightDetailUpdate.setName(name);
			freightDetailUpdate.setId(id);

			List<FreightAddressBean> freightAddressList = freightService.getFreightAddressList();
			Assert.assertNotEquals(freightAddressList, null, "运费模板商户列表拉取失败");
			FreightAddressBean freightAddress = NumberUtil.roundNumberInList(freightAddressList);
			List<BigDecimal> address_ids = new ArrayList<BigDecimal>();
			BigDecimal address_id = freightAddress.getAddress_id();
			address_ids.add(address_id);

			result = freightService.updateFreight(freightDetailUpdate, address_ids);
			Assert.assertEquals(result, true, "修改运费模板信息失败");

			FreightDetailBean freightDetailResult = freightService.getFreightDetail(id);
			Assert.assertNotEquals(freightDetailResult, null, "获取运费模板详细信息失败");

			result = compareData(freightDetailUpdate, freightDetailResult);

			freightAddressList = freightService.getFreightAddressList();
			Assert.assertNotEquals(freightAddressList, null, "运费模板商户列表拉取失败");
			freightAddress = freightAddressList.stream().filter(f -> f.getAddress_id().compareTo(address_id) == 0)
					.findAny().orElse(null);

			if (!freightAddress.getFreight_id().equals(id)) {
				String msg = String.format("运费模板:%s配置了商户:%s没有生效", name, address_id);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改配送模板输入信息与查看到的不一致");
		} catch (Exception e) {
			logger.error("修改运费模板列表遇到错误: ", e);
			Assert.fail("修改运费模板列表遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase05() {
		ReporterCSS.title("测试点: 切换默认报价单");
		String name = "AT_" + StringUtil.getRandomNumber(6);
		try {
			FreightDetailBean freightDetail = initFreightDetail();

			freightDetail.setName(name);

			boolean result = freightService.createFreight(freightDetail, new ArrayList<BigDecimal>());
			Assert.assertEquals(result, true, "创建运费模板失败");

			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean freight = freights.stream().filter(f -> f.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(freight, null, "新建的运费模板:" + name + " 没有找到");

			FreightBean defaultFreight = freights.stream().filter(f -> f.isIs_default()).findAny().orElse(null);
			Assert.assertNotEquals(freight, null, "默认模板没有找到");

			result = freightService.setDefaultFreight(freight.getId());
			Assert.assertEquals(result, true, "切换默认运费模板失败");

			freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean defaultFreightAfter = freights.stream().filter(f -> f.isIs_default()).findAny().orElse(null);
			Assert.assertNotEquals(defaultFreightAfter, null, "切换默认运费模板,默认模板没有找到");

			Assert.assertEquals(defaultFreightAfter.getId(), freight.getId(), "切换默认运费模板,没有切换成功");

			if (defaultFreight.getId().equals("Y000001")) {
				defaultFreight = freights.stream().filter(f -> f.getName().equals("默认运费模版")).findAny().orElse(null);
				Assert.assertNotEquals(defaultFreight, null, "没有找到名称为: 默认运费模版 的模板");
			}
			result = freightService.setDefaultFreight(defaultFreight.getId());
			Assert.assertEquals(result, true, "切换默认运费模板失败");
		} catch (Exception e) {
			logger.error("切换默认报价单遇到错误: ", e);
			Assert.fail("切换默认报价单遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase06() {
		ReporterCSS.title("测试点: 设置默认生效报价单");
		try {
			List<FreightSaleMenuBean> freightSaleMenuList = freightService.getFreighSaleMenus();
			Assert.assertNotEquals(freightSaleMenuList, null, "获取默认生效报价单列表失败");

			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			FreightBean freight = NumberUtil.roundNumberInList(freights);

			String freight_id = freight.getId();
			List<String> salemenu_ids = new ArrayList<String>();

			FreightSaleMenuBean freightSaleMenu = NumberUtil.roundNumberInList(freightSaleMenuList);
			String sele_menu_id = freightSaleMenu.getId();
			salemenu_ids.add(sele_menu_id);

			boolean result = freightService.updateFreighSaleMenu(freight_id, salemenu_ids);
			Assert.assertEquals(result, true, "设置默认生效报价单失败");

			freightSaleMenuList = freightService.getFreighSaleMenus();
			Assert.assertNotEquals(freightSaleMenuList, null, "获取默认生效报价单列表失败");

			freightSaleMenu = freightSaleMenuList.stream().filter(f -> f.getId().equals(sele_menu_id)).findAny()
					.orElse(null);

			Assert.assertEquals(freightSaleMenu.getFreight_id(), freight_id, "设置的默认生效报价单ID与预期不一致");

		} catch (Exception e) {
			logger.error("设置默认生效报价单遇到错误: ", e);
			Assert.fail("设置默认生效报价单遇到错误: ", e);
		}
	}

	@Test
	public void freightTestCase07() {
		ReporterCSS.title("测试点: 运费模板商户列表");
		try {
			List<FreightAddressBean> freightAddressList = freightService.getFreightAddressList();
			Assert.assertNotEquals(freightAddressList, null, "运费模板商户列表拉取失败");
		} catch (Exception e) {
			logger.error("运费模板商户列表遇到错误: ", e);
			Assert.fail("运费模板商户列表遇到错误: ", e);
		}
	}

	@AfterClass
	public void afterClass() {
		try {
			List<FreightBean> freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			String id = null;
			boolean result = true;
			for (FreightBean freight : freights) {
				if (freight.getName().startsWith("AT_")) {
					id = freight.getId();
					result = freightService.deleteFreight(id);
					Assert.assertEquals(result, true, "删除运费模板失败");
				}

			}

			freights = freightService.getFreights();
			Assert.assertNotEquals(freights, null, "拉取运费模板列表失败");

			List<FreightBean> tempFreights = freights.stream().filter(f -> f.getName().startsWith("AT_"))
					.collect(Collectors.toList());
			Assert.assertEquals(tempFreights.size(), 0, "运费模板没有删除成功");
		} catch (Exception e) {
			logger.error("删除运费模板列表遇到错误: ", e);
			Assert.fail("删除运费模板列表遇到错误: ", e);
		}
	}

	public boolean compareData(FreightDetailBean freightDetailParam, FreightDetailBean freightDetailResult) {
		boolean result = true;
		String msg = null;
		if (!freightDetailParam.getName().equals(freightDetailResult.getName())) {
			msg = String.format("新建运费模板,名称与预期的不一致,预期:%s,实际:%s", freightDetailParam.getName(),
					freightDetailResult.getName());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		FreightDetailBean.DeliveryFreight deliveryFreightParam = freightDetailParam.getDelivery_freight();
		FreightDetailBean.DeliveryFreight deliveryFreightResult = freightDetailResult.getDelivery_freight();

		if (deliveryFreightParam.getMin_total_price().compareTo(deliveryFreightResult.getMin_total_price()) != 0) {
			msg = String.format("新建运费模板,起送金额与预期不一致,预期:%s,实际:%s", deliveryFreightParam.getMin_total_price(),
					deliveryFreightResult.getMin_total_price());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<FreightDetailBean.DeliveryFreight.Section> sectionParamList = deliveryFreightParam.getSection();
		List<FreightDetailBean.DeliveryFreight.Section> sectionResultList = deliveryFreightResult.getSection();

		if (sectionParamList.size() != sectionResultList.size()) {
			msg = String.format("新建运费模板,金额设置条目数与预期不一致,预期:%s,实际:%s", sectionParamList.size(), sectionResultList.size());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		} else {
			for (int i = 0; i < sectionParamList.size(); i++) {
				FreightDetailBean.DeliveryFreight.Section sectionParam = sectionParamList.get(i);
				FreightDetailBean.DeliveryFreight.Section sectionResult = sectionResultList.get(i);

				if (sectionParam.getFreight().compareTo(sectionResult.getFreight()) != 0) {
					msg = String.format("新建运费模板,第%s条配置收费金额不一致,预期:%s,实际:%s", i, sectionParam.getFreight(),
							sectionResult.getFreight());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (sectionParam.getMin().compareTo(sectionResult.getMin()) != 0) {
					msg = String.format("新建运费模板,第%s条配置收费起始金额不一致,预期:%s,实际:%s", i, sectionParam.getMin(),
							sectionResult.getMin());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (sectionParam.getFreight().compareTo(sectionResult.getFreight()) != 0) {
					msg = String.format("新建运费模板,第%s条配置收费结束金额不一致,预期:%s,实际:%s", i, sectionParam.getMax(),
							sectionResult.getMax());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}

		FreightDetailBean.PickUpFreight pickUpFreightParam = freightDetailParam.getPick_up_freight();
		FreightDetailBean.PickUpFreight pickUpFreightResult = freightDetailResult.getPick_up_freight();

		if (pickUpFreightParam.getMin_total_price().compareTo(pickUpFreightResult.getMin_total_price()) != 0) {
			msg = String.format("新建运费模板,自提点起送金额与预期不一致,预期:%s,实际:%s", pickUpFreightParam.getMin_total_price(),
					pickUpFreightResult.getMin_total_price());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		}

		List<FreightDetailBean.PickUpFreight.Section> pickUpFreightSectionParamList = pickUpFreightParam.getSection();
		List<FreightDetailBean.PickUpFreight.Section> pickUpFreightSectionResultList = pickUpFreightResult.getSection();

		if (pickUpFreightSectionParamList.size() != pickUpFreightSectionResultList.size()) {
			msg = String.format("新建运费模板,自提点金额设置条目数与预期不一致,预期:%s,实际:%s", pickUpFreightSectionParamList.size(),
					pickUpFreightSectionResultList.size());
			ReporterCSS.warn(msg);
			logger.warn(msg);
			result = false;
		} else {
			for (int i = 0; i < pickUpFreightSectionParamList.size(); i++) {
				FreightDetailBean.PickUpFreight.Section sectionParam = pickUpFreightSectionParamList.get(i);
				FreightDetailBean.PickUpFreight.Section sectionResult = pickUpFreightSectionResultList.get(i);

				if (sectionParam.getFreight().compareTo(sectionResult.getFreight()) != 0) {
					msg = String.format("新建运费模板,自提点第%s条配置收费金额不一致,预期:%s,实际:%s", i, sectionParam.getFreight(),
							sectionResult.getFreight());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (sectionParam.getMin().compareTo(sectionResult.getMin()) != 0) {
					msg = String.format("新建运费模板,自提点第%s条配置收费起始金额不一致,预期:%s,实际:%s", i, sectionParam.getMin(),
							sectionResult.getMin());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (sectionParam.getFreight().compareTo(sectionResult.getFreight()) != 0) {
					msg = String.format("新建运费模板,自提点第%s条配置收费结束金额不一致,预期:%s,实际:%s", i, sectionParam.getMax(),
							sectionResult.getMax());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
		}
		return result;

	}

}
