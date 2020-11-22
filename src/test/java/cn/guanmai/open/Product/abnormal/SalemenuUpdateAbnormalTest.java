package cn.guanmai.open.Product.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenServiceTimeBean;
import cn.guanmai.open.bean.product.param.OpenSalemenuCreateParam;
import cn.guanmai.open.bean.product.param.OpenSalemenuUpdateParam;
import cn.guanmai.open.impl.product.OpenSalemenuServiceImpl;
import cn.guanmai.open.impl.product.OpenServiceTimeServiceImpl;
import cn.guanmai.open.interfaces.product.OpenSalemenuService;
import cn.guanmai.open.interfaces.product.OpenServiceTimeService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

/**
 * @author: liming
 * @Date: 2020年2月7日 下午3:09:51
 * @description:
 * @version: 1.0
 */

public class SalemenuUpdateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuUpdateAbnormalTest.class);
	private OpenSalemenuService salemenuService;
	private OpenServiceTimeService serviceTimeService;
	private String time_config_id;
	private String salemenu_id;
	private List<OpenServiceTimeBean> serviceTimes;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();

		salemenuService = new OpenSalemenuServiceImpl(access_token);
		serviceTimeService = new OpenServiceTimeServiceImpl(access_token);
		try {
			serviceTimes = serviceTimeService.getServiceTimes();
			Assert.assertNotEquals(serviceTimes, null, "获取运营时间列表信息失败");

			Assert.assertEquals(serviceTimes.size() > 0, true, "此站点无运营时间");

			time_config_id = NumberUtil.roundNumberInList(serviceTimes).getTime_config_id();

			String name = "测试新建报价单";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertNotEquals(salemenu_id, null, "新建报价单失败");
		} catch (Exception e) {
			logger.error("获取运营时间信息遇到错误: ", e);
			Assert.fail("获取运营时间信息遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase01() {
		ReporterCSS.title("测试点:异常修改报价单,不传入报价单ID");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_name("New");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,不传入报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase02() {
		ReporterCSS.title("测试点:异常修改报价单,传入空的报价单ID");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id("");
			salemenuUpdateParam.setSalemenu_name("New");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入空的报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase03() {
		ReporterCSS.title("测试点:异常修改报价单,传入错误的报价单ID");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id("S11100");
			salemenuUpdateParam.setSalemenu_name("New");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入空的报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase04() {
		ReporterCSS.title("测试点:异常修改报价单,传入空的报价单名称");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setSalemenu_name("");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入空的报价单ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase05() {
		ReporterCSS.title("测试点:异常修改报价单,传入过长的报价单名称");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setSalemenu_name(StringUtil.getRandomString(13));
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入过长的报价单名称,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase06() {
		ReporterCSS.title("测试点:异常修改报价单,传入空的运营时间ID");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setTime_config_id("");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入空的运营时间ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase07() {
		ReporterCSS.title("测试点:异常修改报价单,传入错误的运营时间ID");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setTime_config_id("ST111");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入错误的运营时间ID,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase08() {
		ReporterCSS.title("测试点:异常修改报价单,传入空的对外名称");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setSalemenu_outer_name("");
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入空的对外名称,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase09() {
		ReporterCSS.title("测试点:异常修改报价单,传入过长的对外名称");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setSalemenu_outer_name(StringUtil.getRandomString(7));
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入过长的对外名称,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase10() {
		ReporterCSS.title("测试点:异常修改报价单,传入过长的描述信息");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setDesc(StringUtil.getRandomString(33));
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入过长的描述信息,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuUpdateAbnormalTestCase11() {
		ReporterCSS.title("测试点:异常修改报价单,传入非候选值的报价单状态");
		try {
			OpenSalemenuUpdateParam salemenuUpdateParam = new OpenSalemenuUpdateParam();
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			salemenuUpdateParam.setIs_active(2);
			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, false, "异常修改报价单,传入非候选值的报价单状态,断言失败");
		} catch (Exception e) {
			logger.error("修改报价单遇到错误: ", e);
			Assert.fail("修改报价单遇到错误: ", e);
		}
	}

}
