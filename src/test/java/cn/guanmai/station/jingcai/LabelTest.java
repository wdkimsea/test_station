package cn.guanmai.station.jingcai;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.jingcai.LabelBean;
import cn.guanmai.station.impl.jingcai.LabelServiceImpl;
import cn.guanmai.station.interfaces.jingcai.LabelService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年4月28日 上午10:53:18
 * @description:
 * @version: 1.0
 */

public class LabelTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(LabelTest.class);

	private LabelService labelService;

	@BeforeClass
	public void initData() {
		Map<String, String> cookie = getStationCookie();
		labelService = new LabelServiceImpl(cookie);
	}

	@Test
	public void labelTestCase01() {
		ReporterCSS.title("测试点: 新建商品标签");
		try {
			String name = "TMP" + StringUtil.getRandomNumber(4);
			BigDecimal id = labelService.createLabel(name);
			Assert.assertNotEquals(id, null, "新建标签失败");

			List<LabelBean> labels = labelService.searchLabel(name);
			Assert.assertNotEquals(labels, null, "搜索过滤商品标签失败");

			LabelBean label = labels.stream().filter(l -> l.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(label, null, "新建的商品标签没有搜索到");

		} catch (Exception e) {
			logger.error("新建商品标签出现错误: ", e);
			Assert.fail("新建商品标签出现错误: ", e);
		}
	}

	@Test
	public void labelTestCase02() {
		ReporterCSS.title("测试点: 删除商品标签");
		try {
			String name = "TMP" + StringUtil.getRandomNumber(4);
			BigDecimal id = labelService.createLabel(name);
			Assert.assertNotEquals(id, null, "新建标签失败");

			boolean result = labelService.deleteLabel(id);
			Assert.assertEquals(result, true, "删除商品标签失败");

			List<LabelBean> labels = labelService.searchLabel(name);
			Assert.assertNotEquals(labels, null, "搜索过滤商品标签失败");

			LabelBean label = labels.stream().filter(l -> l.getName().equals(name)).findAny().orElse(null);
			Assert.assertEquals(label, null, "删除的商品标签没有真正删除");

		} catch (Exception e) {
			logger.error("删除商品标签出现错误: ", e);
			Assert.fail("删除商品标签出现错误: ", e);
		}
	}

	@Test
	public void labelTestCase03() {
		ReporterCSS.title("测试点: 商品标签搜索过滤");
		try {
			String name = "TMP" + StringUtil.getRandomNumber(4);
			BigDecimal id = labelService.createLabel(name);
			Assert.assertNotEquals(id, null, "新建标签失败");

			List<LabelBean> labels = labelService.searchLabel(name);
			Assert.assertNotEquals(labels, null, "搜索过滤商品标签失败");

			LabelBean label = labels.stream().filter(l -> l.getName().equals(name)).findAny().orElse(null);
			Assert.assertNotEquals(label, null, "新建的商品标签没有搜索到");

			List<String> names = labels.stream().filter(l -> !l.getName().contains(name)).map(l -> l.getName())
					.collect(Collectors.toList());

			Assert.assertEquals(names.size(), 0, "商品标签按关键词" + name + "过滤,过滤出了不符合条件的商品标签" + names + "信息");
		} catch (Exception e) {
			logger.error("搜索商品标签出现错误: ", e);
			Assert.fail("搜索商品标签出现错误: ", e);
		}
	}

	@AfterClass
	public void afterClass() {
		ReporterCSS.title("后置处理: 删除商品标签");
		try {
			List<LabelBean> labels = labelService.searchLabel("TMP");
			Assert.assertNotEquals(labels, null, "搜索过滤商品标签失败");

			for (LabelBean label : labels) {
				BigDecimal id = label.getId();
				boolean result = labelService.deleteLabel(id);
				Assert.assertEquals(result, true, "删除商品标签失败");
			}
		} catch (Exception e) {
			logger.error("删除商品标签出现错误: ", e);
			Assert.fail("删除商品标签出现错误: ", e);
		}
	}
}
