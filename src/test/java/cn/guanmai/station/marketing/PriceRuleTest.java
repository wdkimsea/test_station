package cn.guanmai.station.marketing;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.marketing.PriceRuleBean;
import cn.guanmai.station.bean.marketing.PriceRuleSkuBean;
import cn.guanmai.station.bean.marketing.param.PriceRuleCreateParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleEditParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleFilterParam;
import cn.guanmai.station.bean.marketing.param.PriceRuleSkuFilterParam;
import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.param.OrderSkuParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.marketing.PriceRuleServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.marketing.PriceRuleService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Feb 19, 2019 5:58:46 PM 
* @des 限时锁价测试类
* @version 1.0 
*/
public class PriceRuleTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PriceRuleTest.class);
	private PriceRuleService priceRuleService;
	private SalemenuService salemenuService;
	private OrderService orderService;
	private CategoryService categoryService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		priceRuleService = new PriceRuleServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
	}

	@Test
	public void priceRuleTestCase01() {
		Reporter.log("测试点: 搜索查询限时锁价规则");
		PriceRuleFilterParam filterParam = new PriceRuleFilterParam(3, 0, 10);
		try {
			List<PriceRuleBean> priceRuleList = priceRuleService.searchPriceRule(filterParam);
			Assert.assertNotEquals(priceRuleList, null, "搜索过滤限时锁价失败");
		} catch (Exception e) {
			logger.error("搜索过滤限时锁价遇到错误: ", e);
			Assert.fail("搜索过滤限时锁价遇到错误: ", e);
		}

	}

	@Test
	public void priceRuleTestCase02() {
		Reporter.log("测试点: 获取指定限时锁价的详细信息");
		PriceRuleFilterParam filterParam = new PriceRuleFilterParam(3, 0, 10);
		try {
			List<PriceRuleBean> priceRuleList = priceRuleService.searchPriceRule(filterParam);
			Assert.assertNotEquals(priceRuleList, null, "搜索过滤限时锁价失败");

			if (priceRuleList.size() > 0) {
				String price_rule_id = priceRuleList.get(0).getId();
				PriceRuleBean priceRule = priceRuleService.getPriceRuleDetailInfo(price_rule_id);
				Assert.assertNotEquals(priceRule, null, "获取指定的限时锁价规则详细信息失败");
			}
		} catch (Exception e) {
			logger.error("搜索过滤限时锁价遇到错误: ", e);
			Assert.fail("搜索过滤限时锁价遇到错误: ", e);
		}
	}

	@Test
	public void priceRuleTestCase03() {
		Reporter.log("测试点: 限时锁价拉取所有的报价单接口测试");
		try {
			JSONArray salemenus = salemenuService.getAllSalemenu();
			Assert.assertNotEquals(salemenus, null, "限时锁价拉取所有的报价单失败");
		} catch (Exception e) {
			logger.error("限时锁价拉取所有的报价单接口调用出现错误: ", e);
			Assert.fail("限时锁价拉取所有的报价单接口调用出现错误: ", e);
		}
	}

	@Test
	public void priceRuleTestCase04() {
		Reporter.log("测试点: 新建锁价规则,搜索商户接口验证");
		try {
			JSONArray salemenus = salemenuService.getAllSalemenu();
			Assert.assertNotEquals(salemenus, null, "限时锁价拉取所有的报价单失败");

			Assert.assertEquals(salemenus.size() > 0, true, "接口拉取报价单列表返回为空");

			Random random = new Random();

			String salemenu_id = salemenus.getJSONObject(random.nextInt(salemenus.size())).getString("salemenu_id");
			List<String> customers = priceRuleService.searchCustomer("分", salemenu_id);
			Assert.assertNotEquals(customers, null, "新建锁价规则,搜索商户接口验证失败");
		} catch (Exception e) {
			logger.error("新建锁价规则,搜索商户接口调用出现错误: ", e);
			Assert.fail("新建锁价规则,搜索商户接口调用出现错误: ", e);
		}
	}

	@Test
	public void priceRuleTestCase05() {
		Reporter.log("测试点: 新建锁价规则,搜索商品接口验证");
		try {
			JSONArray salemenus = salemenuService.getAllSalemenu();
			Assert.assertNotEquals(salemenus, null, "限时锁价拉取所有的报价单失败");

			String salemenu_id = salemenus.getJSONObject(new Random().nextInt(salemenus.size()))
					.getString("salemenu_id");
			List<String> skus = priceRuleService.searchSku(salemenu_id, "C");
			Assert.assertNotEquals(skus, null, "新建锁价规则,搜索商品接口验证失败");
		} catch (Exception e) {
			logger.error("新建锁价规则,搜索商品接口调用出现错误: ", e);
			Assert.fail("新建锁价规则,搜索商品接口调用出现错误: ", e);
		}
	}

	@Test
	public void priceRuleTestCase06() {
		try {
			Reporter.log("测试点: 当站点没有锁价规则的时候,进行修改和创建操作,有则进行修改操作");

			PriceRuleFilterParam filterParam = new PriceRuleFilterParam(3, 0, 10);
			List<PriceRuleBean> priceRuleList = priceRuleService.searchPriceRule(filterParam);
			Assert.assertNotEquals(priceRuleList, null, "搜索过滤限时锁价失败");

			String price_rule_id = null;
			if (priceRuleList.size() > 0) {
				price_rule_id = priceRuleList.get(0).getId();
			} else {
				List<CustomerBean> customerArray = orderService.getOrderCustomerArray(6);
				Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

				Assert.assertEquals(customerArray.size() > 0, true, "无可用下单商户");
				// 随机选取一个正常商户进行下单
				CustomerBean customer = NumberUtil.roundNumberInList(customerArray);
				String address_id = customer.getAddress_id();

				List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
				Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

				Assert.assertEquals(orderReceiveTimes.size() > 0, true, "商户" + address_id + "没有绑定运营时间,无法进行下单操作");
				// 随机取一个绑定的运营时间
				OrderReceiveTimeBean orderReceiveTime = NumberUtil.roundNumberInList(orderReceiveTimes);
				String time_config_id = orderReceiveTime.getTime_config_id();

				Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
						"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");

				// 下单商品集合
				String[] search_texts = new String[] { "A", "B", "C", "E", "J" };
				List<OrderSkuParam> skuArray = orderService.orderSkus(address_id, time_config_id, search_texts, 1);
				Assert.assertEquals(skuArray != null && skuArray.size() > 0, true, "没有找到商户绑定的报价单商品,无法进行限时锁价规则创建");

				OrderSkuParam orderSku = skuArray.get(0);
				String sku_id = orderSku.getSku_id();
				String spu_id = orderSku.getSpu_id();

				SkuBean sku = categoryService.getSaleSkuById(spu_id, sku_id);
				Assert.assertNotEquals(sku, null, "获取SKU详细信息失败");

				String salemenu_id = sku.getSalemenu_id();

				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String today = format.format(new Date());

				Calendar ca = Calendar.getInstance();
				ca.setTime(new Date());
				ca.add(Calendar.DATE, 7);
				String afterWeek = format.format(ca.getTime());

				PriceRuleCreateParam param = new PriceRuleCreateParam();
				param.setBegin(today);
				param.setEnd(afterWeek);
				param.setName("自动化创建");
				param.setSalemenu_id(salemenu_id);
				param.setType("customer");
				JSONArray address_ids = new JSONArray();
				address_ids.add(address_id);
				param.setAddress_ids(address_ids);

				PriceRuleCreateParam.Sku s = param.new Sku();
				s.setSku_id(sku_id);
				s.setRule_type(0);
				s.setYx_price(
						String.valueOf(sku.getSale_price().divide(new BigDecimal("100")).add(new BigDecimal("1"))));

				List<PriceRuleCreateParam.Sku> skus = new ArrayList<PriceRuleCreateParam.Sku>();
				skus.add(s);
				param.setSkus(skus);
				param.setRule_object_type(1);
				price_rule_id = priceRuleService.createPriceRule(param);
				Assert.assertNotEquals(price_rule_id, null, "新建锁价规则失败");
			}

			PriceRuleBean priceRule = priceRuleService.getPriceRuleDetailInfo(price_rule_id);

			PriceRuleEditParam editParam = new PriceRuleEditParam();
			editParam.setPrice_rule_id(price_rule_id);
			editParam.setName(priceRule.getName());
			String begin_date = TimeUtil.getCurrentTime("yyyy-MM-dd");
			String end_date = TimeUtil.calculateTime("yyyy-MM-dd", begin_date, 7, Calendar.DATE);
			editParam.setBegin(begin_date);
			editParam.setEnd(end_date);
			editParam.setStatus(3);

			JSONArray address_ids = new JSONArray();
			JSONArray addresses = priceRule.getAddresses();
			for (Object obj : addresses) {
				address_ids.add(JSONObject.parseObject(obj.toString()).getString("id"));
			}
			editParam.setAddress_ids(address_ids);

			List<PriceRuleEditParam.Sku> paramSkus = new ArrayList<PriceRuleEditParam.Sku>();
			List<PriceRuleBean.Sku> skus = priceRule.getSkus();
			if (skus != null) {
				PriceRuleEditParam.Sku paramSku = null;
				for (PriceRuleBean.Sku sku : skus) {
					paramSku = editParam.new Sku();
					paramSku.setSku_id(sku.getId());
					paramSku.setRule_type(sku.getRule_type());
					paramSku.setYx_price(String.valueOf(sku.getYx_price()));
					paramSkus.add(paramSku);
				}
				editParam.setSkus(paramSkus);
				editParam.setRule_object_type(1);
			}

			List<PriceRuleBean.Category2> category_2_list = priceRule.getCategory_2_list();
			if (category_2_list != null && category_2_list.size() > 0) {
				List<PriceRuleEditParam.Category2> category_2_list_param = new ArrayList<PriceRuleEditParam.Category2>();
				for (PriceRuleBean.Category2 category2 : category_2_list) {
					PriceRuleEditParam.Category2 category2_param = editParam.new Category2();
					category2_param.setCategory_2_id(category2.getCategory_2_id());
					category2_param.setRule_type(category2.getRule_type());
					category2_param.setYx_price(category2.getYx_price());
					category_2_list_param.add(category2_param);
				}
				editParam.setCategory_2_list(category_2_list_param);
				editParam.setRule_object_type(2);
			}

			// 可能商品都删除了,这里修需要再次添加
			if ((category_2_list == null || category_2_list.size() == 0)
					&& (paramSkus == null || paramSkus.size() == 0)) {
				List<String> sku_ids = priceRuleService.searchSku(priceRule.getSalemenu_id(), "a");
				PriceRuleEditParam.Sku paramSku = null;
				for (String id : sku_ids) {
					paramSku = editParam.new Sku();
					paramSku.setSku_id(id);
					paramSku.setRule_type(0);
					paramSku.setYx_price(String.valueOf("290"));
					paramSkus.add(paramSku);
					if (paramSkus.size() >= 2) {
						break;
					}
				}
				editParam.setSkus(paramSkus);
			}

			JSONObject retJson = priceRuleService.editPriceRule(editParam);
			Assert.assertEquals(retJson.getInteger("code") == 0, true, "修改锁价规格失败,失败原因 " + retJson.getString("msg"));
		} catch (Exception e) {
			logger.error("创建锁价规则的过程中遇到错误: ", e);
			Assert.fail("创建锁价规则的过程中遇到错误: ", e);
		}
	}

	@Test
	public void priceRuleTestCase07() {
		PriceRuleSkuFilterParam filterParam = new PriceRuleSkuFilterParam(3, 0, 10,
				PriceRuleSkuFilterParam.Type.customer);
		try {
			List<PriceRuleSkuBean> priceRuleSkuList = priceRuleService.searchPriceRuleSku(filterParam);
			Assert.assertNotEquals(priceRuleSkuList, null, "限时锁价,按商户商品查失败");
		} catch (Exception e) {
			logger.error("限时锁价,按商户商品查看遇到错误: ", e);
			Assert.fail("限时锁价,按商户商品查看遇到错误: ", e);
		}
	}
}
