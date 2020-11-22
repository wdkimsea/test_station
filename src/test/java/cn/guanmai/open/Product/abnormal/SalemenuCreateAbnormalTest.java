package cn.guanmai.open.Product.abnormal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenServiceTimeBean;
import cn.guanmai.open.bean.product.param.OpenSalemenuCreateParam;
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
 * @Date: 2020年2月7日 下午2:32:41
 * @description:
 * @version: 1.0
 */

public class SalemenuCreateAbnormalTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuCreateAbnormalTest.class);
	private OpenSalemenuService salemenuService;
	private OpenServiceTimeService serviceTimeService;
	private String time_config_id;
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
		} catch (Exception e) {
			logger.error("获取运营时间信息遇到错误: ", e);
			Assert.fail("获取运营时间信息遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase01() {
		ReporterCSS.title("测试点: 新建报价单异常测试,不传入名称");
		try {
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,不传入名称,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase02() {
		ReporterCSS.title("测试点: 新建报价单异常测试,名称传入为空");
		try {
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name("");
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,名称传入为空");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase03() {
		ReporterCSS.title("测试点: 新建报价单异常测试,名称传入字符超过最大限制");
		try {
			String salemenu_name = StringUtil.getRandomString(13);
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,名称传入为空");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase04() {
		ReporterCSS.title("测试点: 新建报价单异常测试,不传入运营时间参数");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			// salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,不传入运营时间参数,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase05() {
		ReporterCSS.title("测试点: 新建报价单异常测试,传入空的运营时间ID");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id("");

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,传入空的运营时间ID,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase06() {
		ReporterCSS.title("测试点: 新建报价单异常测试,传入错误的运营时间ID");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id("ST100");

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,传入错误的运营时间ID,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase07() {
		ReporterCSS.title("测试点: 新建报价单异常测试,不传入对外名称");
		try {
			String salemenu_name = "测试新建(可以删除)";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,不传入对外名称,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase08() {
		ReporterCSS.title("测试点: 新建报价单异常测试,传入空的对外名称");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,传入空的对外名称,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase09() {
		ReporterCSS.title("测试点: 新建报价单异常测试,传入过长的对外名称(大于6个字符)");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = StringUtil.getRandomString(7).toUpperCase();
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,传入空的对外名称,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase10() {
		ReporterCSS.title("测试点: 新建报价单异常测试,传入过长的描述信息(大于32个字符)");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);
			salemenuCreateParam.setDesc(StringUtil.getRandomString(33));

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,传入过长的描述信息(大于32个字符),断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuCreateAbnormalTestCase11() {
		ReporterCSS.title("测试点: 新建报价单异常测试,报价单状态传入非候选值");
		try {
			String salemenu_name = "测试新建(可以删除)";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(salemenu_name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);
			salemenuCreateParam.setIs_active(2);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertEquals(salemenu_id, null, "新建报价单异常测试,报价单状态传入非候选值,断言失败");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

}
