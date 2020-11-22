package cn.guanmai.station.category;

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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.guanmai.station.bean.category.TaxRuleBean;
import cn.guanmai.station.impl.category.SpuRemarkServiceImpl;
import cn.guanmai.station.impl.category.TaxRuleServiceImpl;
import cn.guanmai.station.interfaces.category.SpuRemarkService;
import cn.guanmai.station.interfaces.category.TaxRuleService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Feb 18, 2019 7:42:39 PM 
* @des 税率测试
* @version 1.0 
*/
public class TaxRuleTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(TaxRuleTest.class);
	private TaxRuleService taxRuleService;
	private SpuRemarkService spuRemarkService;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		taxRuleService = new TaxRuleServiceImpl(headers);
		spuRemarkService = new SpuRemarkServiceImpl(headers);
	}

	@Test
	public void taxRuleTestCase01() {
		ReporterCSS.title("测试点: 获取税率规则列表");
		try {
			List<TaxRuleBean> taxRuleList = taxRuleService.getTaxRuleList(null, "");
			Assert.assertNotEquals(taxRuleList, null, "获取税率规则列表失败");
		} catch (Exception e) {
			logger.error("获取税率规则列表出现错误: ", e);
			Assert.fail("获取税率规则列表出现错误: ", e);
		}
	}

	@Test
	public void taxRuleTestCase02() {
		ReporterCSS.title("测试点: 查看税率规则详情");
		try {
			List<TaxRuleBean> taxRuleList = taxRuleService.getTaxRuleList(null, "");
			Assert.assertNotEquals(taxRuleList, null, "获取税率规则列表失败");

			// 如果站点已经存在税率规则,则直接获取
			if (taxRuleList.size() > 0) {
				String tax_id = taxRuleList.get(0).getTax_rule_id();
				TaxRuleBean taxRule = taxRuleService.getTaxRuleDetailInfo(tax_id);
				Assert.assertNotEquals(taxRule, null, "获取税率规则详细信息失败");
			}
		} catch (Exception e) {
			logger.error("获取税率规则列表出现错误: ", e);
			Assert.fail("获取税率规则列表出现错误: ", e);
		}
	}

	public void taxRuleTestCase03() {
		try {
			ReporterCSS.title("测试点: 新建税率规则,查询商户的接口调用");
			Map<String, String> customerMap = spuRemarkService.getCustomerMap();
			String customer_id = null;
			for (String key : customerMap.keySet()) {
				customer_id = key;
			}

			StringBuffer address_id = new StringBuffer();
			address_id.append("S");
			if (customer_id.length() < 6) {
				for (int i = 0; i < 6 - customer_id.length(); i++) {
					address_id.append("0");
				}
			}
			address_id.append(customer_id);

			List<String> customerIdList = taxRuleService.searchTaxCustomer(address_id.toString());
			Assert.assertEquals(customerIdList != null && customerIdList.size() == 1, true, "新建税率规则,搜索商户的结果与预期的不符");
		} catch (Exception e) {
			logger.error("新建税率规则,搜索商户接口调用遇到错误: ", e);
			Assert.fail("新建税率规则,搜索商户接口调用遇到错误: ", e);
		}
	}

	@Test
	public void taxRuleTestCase04() {
		try {
			ReporterCSS.title("测试点: 新建税率规则,查询商品的接口调用");

			Map<String, String> spuMap = taxRuleService.searchTaxSpu("C");
			Assert.assertEquals(spuMap != null, true, "新建税率规则,查询商品的接口调用失败");

		} catch (Exception e) {
			logger.error("新建税率规则,查询商品的接口调用遇到错误: ", e);
			Assert.fail("新建税率规则,查询商品的接口调用遇到错误: ", e);
		}
	}

	@Test
	public void taxRuleTestCase05() {
		try {
			ReporterCSS.title("测试点: 新建税率规则,如果存在已有税率规则,则进行修改操作");
			List<TaxRuleBean> taxRuleList = taxRuleService.getTaxRuleList(null, "");
			Assert.assertNotEquals(taxRuleList, null, "获取税率规则列表失败");

			if (taxRuleList.size() == 0) {
				String tax_rule_name = "自动化创建";
				int status = 1;
				TaxRuleBean taxRule = new TaxRuleBean();
				TaxRuleBean.Address address = taxRule.new Address();

				Map<String, String> customerMap = spuRemarkService.getCustomerMap();
				String address_id = null;
				for (String key : customerMap.keySet()) {
					address_id = key;
				}
				String address_name = customerMap.get(address_id);
				address.setAddress_id(new BigDecimal(address_id));
				address.setAddress_name(address_name);

				List<TaxRuleBean.Address> addressList = new ArrayList<TaxRuleBean.Address>();
				addressList.add(address);

				Map<String, String> spuMap = taxRuleService.searchTaxSpu("C");
				Assert.assertEquals(spuMap.size() > 0, true, "新建税率规则,没有查询到商品,无法进行创建操作");

				TaxRuleBean.Spu spu = null;
				List<TaxRuleBean.Spu> spus = new ArrayList<TaxRuleBean.Spu>();
				for (String spu_id : spuMap.keySet()) {
					spu = taxRule.new Spu();
					spu.setSpu_id(spu_id);
					spu.setTax_rate(new BigDecimal("2"));
					spu.setSpu_name(spuMap.get(spu_id));
					spus.add(spu);
					if (spus.size() >= 5) {
						break;
					}
				}

				taxRule.setTax_rule_name(tax_rule_name);
				taxRule.setStatus(status);
				taxRule.setAddress(addressList);
				taxRule.setSpu(spus);

				boolean result = taxRuleService.createTaxRule(taxRule);
				Assert.assertEquals(result, true, "新建税率规则,新建失败");
			}

			Map<BigDecimal, String> addressMap = taxRuleService.searchAddressByLabel("-1");
			Assert.assertNotEquals(addressMap, null, "税率规则,按商户标签获取商户列表失败");

			taxRuleList = taxRuleService.getTaxRuleList(null, "");
			Assert.assertNotEquals(taxRuleList, null, "获取税率规则列表失败");
			String tax_id = taxRuleList.get(0).getTax_rule_id();
			TaxRuleBean taxRule = taxRuleService.getTaxRuleDetailInfo(tax_id);
			Assert.assertNotEquals(taxRule, null, "获取税率规则详细信息失败");
			List<TaxRuleBean.Spu> spuList = taxRule.getSpu();
			for (TaxRuleBean.Spu spu : spuList) {
				spu.setTax_rate(new BigDecimal("2"));
			}
			taxRule.setTax_rule_id(tax_id);
			taxRule.setStatus(0);
			JSONObject retJson = taxRuleService.editTaxRule(taxRule);
			if (retJson.getInteger("code") != 0) {
				String msg = retJson.getString("msg");
				if (msg.contains("存在无效商品:")) {
					String spu_ids = msg.split("存在无效商品:")[1];
					JSONArray spu_ids_array = JSONArray.parseArray(spu_ids);
					for (Object obj : spu_ids_array) {
						TaxRuleBean.Spu spu = spuList.stream().filter(s -> s.getSpu_id().equals(String.valueOf(obj)))
								.findAny().orElse(null);
						if (spu != null) {
							spuList.remove(spu);
						}
					}
					taxRule.setSpu(spuList);
				} else if (msg.contains("存在无效商户")) {
					List<TaxRuleBean.Address> addressList = taxRule.getAddress();
					String ids = msg.split("存在无效商户:")[1].split("\"")[0];
					JSONArray addressArray = JSONArray.parseArray(ids);
					List<BigDecimal> address_ids = new ArrayList<BigDecimal>();
					for (Object obj : addressArray) {
						String id = String.valueOf(obj);
						address_ids.add(new BigDecimal(id));
					}
					addressList = addressList.stream().filter(s -> !address_ids.contains(s.getAddress_id()))
							.collect(Collectors.toList());

					if (addressList.size() == 0) {
						TaxRuleBean.Address address = null;
						for (BigDecimal address_id : addressMap.keySet()) {
							address = taxRule.new Address();
							address.setAddress_id(address_id);
							address.setAddress_name(addressMap.get(address_id));
							address.setTax_rate_id(tax_id);
							addressList.add(address);
							if (addressList.size() >= 2) {
								break;
							}
						}
					}
					Assert.assertEquals(addressList.size() > 0, true, "税率规则商户列表为空");
					taxRule.setAddress(addressList);
				}
				retJson = taxRuleService.editTaxRule(taxRule);
				Assert.assertEquals(retJson.getInteger("code") == 0, true, "修改税率规则失败" + retJson.getString("msg"));
			}
		} catch (Exception e) {
			logger.error("新建或修改税率规则的过程中遇到错误: ", e);
			Assert.fail("新建或修改税率规则的过程中遇到错误: ", e);
		}
	}

	@Test
	public void taxRuleTestCase06() {
		ReporterCSS.title("测试点: 税率规则,按商户商品查看");
		try {
			boolean result = taxRuleService.taxSpuList("", null);
			Assert.assertEquals(result, true, "税率规则,按商户商品查看失败");
		} catch (Exception e) {
			logger.error("税率规则,按商户商品查看遇到错误: ", e);
			Assert.fail("税率规则,按商户商品查看遇到错误: ", e);
		}
	}

}
