package cn.guanmai.station.category;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.category.SpuRemarkBean;
import cn.guanmai.station.bean.category.param.SpuRemarkFiterParam;
import cn.guanmai.station.impl.category.SpuRemarkServiceImpl;
import cn.guanmai.station.interfaces.category.SpuRemarkService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/* 
* @author liming 
* @date Feb 18, 2019 6:57:16 PM 
* @des 商品备注测试
* @version 1.0 
*/
public class SpuRemarkTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(SpuDeleteTest.class);
	private SpuRemarkService spuRemarkService;
	private SpuRemarkFiterParam spuRemarkFiterParam;
	private String address_id;
	Map<String, String> customerMap;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		spuRemarkService = new SpuRemarkServiceImpl(headers);
		try {
			spuRemarkFiterParam = new SpuRemarkFiterParam();
			spuRemarkFiterParam.setLimit(10);
			spuRemarkFiterParam.setOffset(0);
			spuRemarkFiterParam.setSpu_type("all");

			customerMap = spuRemarkService.searchCustomer("", 0, 50);
			Assert.assertEquals(customerMap.size() > 0, true, "查询无商户信息,无法进行添加商品备注操作");

			List<SpuRemarkBean> spuRemarkList = null;
			for (String address_id : customerMap.keySet()) {
				spuRemarkFiterParam.setAddress_id(address_id);
				spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
				Assert.assertNotEquals(spuRemarkList, null, "搜索过滤商品备注失败");
				if (spuRemarkList.size() > 0) {
					this.address_id = address_id;
					break;
				}
			}
			Assert.assertEquals(spuRemarkList.size() > 0, true, "查询无商品信息,无法进行添加商品备注操作");
		} catch (Exception e) {
			logger.error("初始化数据遇到错误: ", e);
			Assert.fail("初始化数据遇到错误: ", e);
		}
	}

	@BeforeMethod
	public void beforeMehtod() {
		spuRemarkFiterParam = new SpuRemarkFiterParam();
		spuRemarkFiterParam.setLimit(50);
		spuRemarkFiterParam.setOffset(0);
		spuRemarkFiterParam.setAddress_id(address_id);
		spuRemarkFiterParam.setSpu_type("all");
	}

	@Test
	public void spuRemarkTestCase01() {
		try {
			ReporterCSS.title("测试点: 为SPU商品添加下单备注信息");

			List<SpuRemarkBean> spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "搜索过滤商品备注失败");

			Assert.assertEquals(spuRemarkList.size() > 0, true, "查询无商品信息,无法进行添加商品备注操作");

			SpuRemarkBean spuRemark = NumberUtil.roundNumberInList(spuRemarkList);
			String spu_id = spuRemark.getSpu_id();

			String remark = StringUtil.getRandomString(6);
			boolean result = spuRemarkService.updateSpuRemark(address_id, spu_id, remark);
			Assert.assertEquals(result, true, "商品添加商品备注信息失败");

			spuRemarkFiterParam.setSpu_search_text(spuRemark.getSpu_name());
			spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "搜索过滤商品备注失败");

			spuRemark = spuRemarkList.stream().filter(s -> s.getSpu_id().equals(spu_id)).findAny().orElse(null);
			Assert.assertNotEquals(spuRemark, null, "为商品添加了备注信息后,查询不到此商品了");

			Assert.assertEquals(spuRemark.getSpu_remark(), remark, "查询到的商品备注与预期不一致");

		} catch (Exception e) {
			logger.error("SPU商品添加备注信息遇到错误: ", e);
			Assert.fail("SPU商品添加备注信息遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase02() {
		try {
			ReporterCSS.title("测试点: 商品备注按备注状态搜索过滤搜");

			spuRemarkFiterParam.setSpu_type("set");
			spuRemarkFiterParam.setLimit(50);
			List<SpuRemarkBean> spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");

			List<String> spu_ids = spuRemarkList.stream()
					.filter(s -> s.getSpu_remark() == null || s.getSpu_remark().equals("")).map(s -> s.getSpu_id())
					.collect(Collectors.toList());

			boolean result = true;
			String msg = null;
			if (spu_ids.size() > 0) {
				msg = String.format("商户%s对应的商品列表,按商品备注[有备注]过滤,过滤出了没有备注的商品%s", address_id, spu_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			spuRemarkFiterParam.setSpu_type("unset");

			spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");

			spu_ids = spuRemarkList.stream().filter(s -> s.getSpu_remark() != null && !s.getSpu_remark().equals(""))
					.map(s -> s.getSpu_id()).collect(Collectors.toList());

			if (spu_ids.size() > 0) {
				msg = String.format("商户%s对应的商品列表,按商品备注[无备注]过滤,过滤出了有备注的商品%s", address_id, spu_ids);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商品备注信息,按备注状态过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase03() {
		try {
			ReporterCSS.title("测试点: 商品备注按商品名称搜索过滤搜");
			List<SpuRemarkBean> spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");

			SpuRemarkBean spuRemark = NumberUtil.roundNumberInList(spuRemarkList);
			String spu_name = spuRemark.getSpu_name();

			spuRemarkFiterParam.setAddress_id(address_id);
			spuRemarkFiterParam.setSpu_search_text(spu_name);
			spuRemarkFiterParam.setLimit(50);
			spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");

			List<String> spu_names = spuRemarkList.stream().filter(s -> !s.getSpu_name().contains(spu_name))
					.map(s -> s.getSpu_name()).collect(Collectors.toList());

			boolean result = true;
			String msg = null;
			if (spu_names.size() > 0) {
				msg = String.format("商户%s对应的商品列表,按商品名称[%s]过滤,过滤出了其他商品%s", spu_name, spu_names);
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}
			Assert.assertEquals(result, true, "商品备注信息,按商品名称过滤,过滤出的信息与预期不符");
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase04() {
		ReporterCSS.title("测试点: 商品备注页面,使用商户名搜索过滤商户");
		try {
			String resname = customerMap.get(address_id);
			Map<String, String> customerMap = spuRemarkService.searchCustomer(resname, 0, 50);
			Assert.assertNotEquals(customerMap, null, "商品备注页面,使用商户名搜索过滤失败");

			String msg = null;
			boolean result = true;
			for (String address_id : customerMap.keySet()) {
				if (!customerMap.get(address_id).contains(resname)) {
					msg = String.format("商品备注页面,使用商户名%s搜索过滤,过滤出了不符合的商户%s", resname, customerMap.get(address_id));
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商品备注页面,搜索过滤商户,过滤出的结果与预期不符");
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase05() {
		ReporterCSS.title("测试点: 商品备注页面,使用商户ID搜索过滤商户");
		try {
			String customer_id = "S" + String.format("%06d", Integer.valueOf(address_id));
			Map<String, String> customerMap = spuRemarkService.searchCustomer(address_id, 0, 50);
			Assert.assertNotEquals(customerMap, null, "商品备注页面,使用商户名搜索过滤失败");

			String msg = null;
			boolean result = true;
			for (String id : customerMap.keySet()) {
				if (customer_id.equals(id)) {
					msg = String.format("商品备注页面,使用商户ID%s搜索过滤,过滤出了不符合的商户%s", customer_id, id);
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "商品备注页面,搜索过滤商户,过滤出的结果与预期不符");
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase06() {
		ReporterCSS.title("测试点: 商品备注页面,商户列表页面翻页");
		try {

			Map<String, String> customerMap1 = spuRemarkService.searchCustomer("", 0, 10);
			Assert.assertNotEquals(customerMap1, null, "商品备注页面,使用商户名搜索过滤失败");

			if (customerMap1.size() >= 10) {
				Map<String, String> customerMap2 = spuRemarkService.searchCustomer("", 10, 10);
				Assert.assertNotEquals(customerMap2, null, "商品备注页面,使用商户名搜索过滤失败");

				customerMap2.keySet().retainAll(customerMap1.keySet());
				Assert.assertEquals(customerMap2.size(), 0, "商品备注页面,商户列表翻页第一页和第二页出现了重复的商户" + customerMap2.keySet());
			}
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

	@Test
	public void spuRemarkTestCase07() {
		ReporterCSS.title("测试点: 商品备注页面,商品备注页面翻页");
		try {
			spuRemarkFiterParam.setLimit(10);
			spuRemarkFiterParam.setOffset(0);
			List<SpuRemarkBean> spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
			Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");
			if (spuRemarkList.size() >= 10) {
				List<String> spu_ids1 = spuRemarkList.stream().map(s -> s.getSpu_id()).collect(Collectors.toList());
				spuRemarkFiterParam.setOffset(10);
				spuRemarkList = spuRemarkService.searchSpuRemark(spuRemarkFiterParam);
				Assert.assertNotEquals(spuRemarkList, null, "获取商户绑定的SPU列表失败");

				List<String> spu_ids2 = spuRemarkList.stream().map(s -> s.getSpu_id()).collect(Collectors.toList());

				spu_ids2.retainAll(spu_ids1);
				Assert.assertEquals(spu_ids2.size(), 0, " 商品备注页面,商品备注页面翻页,第一页和第二页出现了重复的商品%s" + spu_ids2);
			}
		} catch (Exception e) {
			logger.error("商品备注搜索过滤遇到错误: ", e);
			Assert.fail("商品备注搜索过滤遇到错误: ", e);
		}
	}

}
