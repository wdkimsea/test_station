package cn.guanmai.open.Product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.product.OpenSalemenuBean;
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
 * @Date: 2020年2月7日 上午11:16:43
 * @description:
 * @version: 1.0
 */

public class SalemenuTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(SalemenuTest.class);
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
	public void salemenuTestCase01() {
		ReporterCSS.title("测试点: 新建报价单,只填写必填字段");
		try {
			String name = "测试新建报价单";
			String salemenu_outer_name = "对外名称";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(name);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setTime_config_id(time_config_id);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertNotEquals(salemenu_id, null, "新建报价单失败");

			List<OpenSalemenuBean> salemenuList = salemenuService.searchSalemenu(null, 1);
			Assert.assertNotEquals(salemenuList, null, "搜索过滤报价单失败");

			OpenSalemenuBean salemenu = salemenuList.stream().filter(s -> s.getId().equals(salemenu_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(salemenu, null, "新建的报价单 " + salemenu_id + "没有在报价单列表找到");

			boolean result = true;
			String msg = null;
			if (!salemenu.getName().equals(name)) {
				msg = String.format("新建报价单填写的名称和新建后查询到的不一致,预期:%s,实际:%s", name, salemenu.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getTime_config_id().equals(time_config_id)) {
				msg = String.format("新建报价单填写的运营时间ID和新建后查询到的不一致,预期:%s,实际:%s", time_config_id,
						salemenu.getTime_config_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getSalemenu_outer_name().equals(salemenu_outer_name)) {
				msg = String.format("新建报价单填写的对外名称和新建后查询到的不一致,预期:%s,实际:%s", salemenu_outer_name,
						salemenu.getSalemenu_outer_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的报价单填写的信息和新建后查询到的不一致");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuTestCase02() {
		ReporterCSS.title("测试点: 新建报价单,非必填字段也填写");
		try {
			String name = "测试新建报价单";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuCreateParam();
			salemenuCreateParam.setSalemenu_name(name);
			salemenuCreateParam.setTime_config_id(time_config_id);
			String salemenu_outer_name = StringUtil.getRandomString(6).toUpperCase();
			String desc = StringUtil.getRandomString(6);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setDesc(desc);
			salemenuCreateParam.setIs_active(0);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertNotEquals(salemenu_id, null, "新建报价单失败");

			List<OpenSalemenuBean> salemenuList = salemenuService.searchSalemenu(null, null);
			Assert.assertNotEquals(salemenuList, null, "搜索过滤报价单失败");

			OpenSalemenuBean salemenu = salemenuList.stream().filter(s -> s.getId().equals(salemenu_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(salemenu, null, "新建的报价单 " + salemenu_id + "没有在报价单列表找到");

			boolean result = true;
			String msg = null;
			if (!salemenu.getName().equals(name)) {
				msg = String.format("新建报价单填写的名称和新建后查询到的不一致,预期:%s,实际:%s", name, salemenu.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getTime_config_id().equals(time_config_id)) {
				msg = String.format("新建报价单填写的运营时间ID和新建后查询到的不一致,预期:%s,实际:%s", time_config_id,
						salemenu.getTime_config_id());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getDesc().equals(desc)) {
				msg = String.format("新建报价单填写的描述信息和新建后查询到的不一致,预期:%s,实际:%s", desc, salemenu.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (salemenu.getIs_active()) {
				msg = String.format("新建报价单填写的状态信息和新建后查询到的不一致,预期:%s,实际:%s", false, salemenu.getIs_active());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getSalemenu_outer_name().equals(salemenu_outer_name)) {
				msg = String.format("新建报价单填写的对外名称和新建后查询到的不一致,预期:%s,实际:%s", salemenu_outer_name,
						salemenu.getSalemenu_outer_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "新建的报价单填写的信息和新建后查询到的不一致");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

	@Test
	public void salemenuTestCase03() {
		ReporterCSS.title("测试点: 修改报价单");
		try {
			String name = "测试新建报价单";
			OpenSalemenuCreateParam salemenuCreateParam = new OpenSalemenuUpdateParam();
			salemenuCreateParam.setSalemenu_name(name);
			salemenuCreateParam.setTime_config_id(time_config_id);
			String salemenu_outer_name = StringUtil.getRandomString(6).toUpperCase();
			String desc = StringUtil.getRandomString(6);
			salemenuCreateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuCreateParam.setDesc(desc);
			salemenuCreateParam.setIs_active(0);

			String salemenu_id = salemenuService.createSalemenu(salemenuCreateParam);
			Assert.assertNotEquals(salemenu_id, null, "新建报价单失败");

			OpenSalemenuUpdateParam salemenuUpdateParam = (OpenSalemenuUpdateParam) salemenuCreateParam;
			salemenuUpdateParam.setSalemenu_id(salemenu_id);
			name = StringUtil.getRandomString(8);
			salemenu_outer_name = StringUtil.getRandomString(6).toUpperCase();
			desc = StringUtil.getRandomString(8);
			salemenuUpdateParam.setSalemenu_name(name);
			salemenuUpdateParam.setSalemenu_outer_name(salemenu_outer_name);
			salemenuUpdateParam.setDesc(desc);
			salemenuUpdateParam.setIs_active(1);

			OpenServiceTimeBean serviceTime = serviceTimes.stream()
					.filter(s -> !s.getTime_config_id().equals(time_config_id)).findFirst().orElse(null);
			if (serviceTime != null) {
				salemenuUpdateParam.setTime_config_id(serviceTime.getTime_config_id());
			}

			boolean result = salemenuService.updateSalemenu(salemenuUpdateParam);
			Assert.assertEquals(result, true, "修改报价单失败");

			List<OpenSalemenuBean> salemenuList = salemenuService.searchSalemenu(null, null);
			Assert.assertNotEquals(salemenuList, null, "搜索过滤报价单失败");

			OpenSalemenuBean salemenu = salemenuList.stream().filter(s -> s.getId().equals(salemenu_id)).findAny()
					.orElse(null);
			Assert.assertNotEquals(salemenu, null, "新建的报价单 " + salemenu_id + "没有在报价单列表找到");

			String msg = null;
			if (!salemenu.getName().equals(name)) {
				msg = String.format("修改报价单填写的名称和修改后查询到的不一致,预期:%s,实际:%s", name, salemenu.getName());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (serviceTime != null) {
				if (!salemenu.getTime_config_id().equals(serviceTime.getTime_config_id())) {
					msg = String.format("修改报价单填写的运营时间ID和修改后查询到的不一致,预期:%s,实际:%s", serviceTime.getTime_config_id(),
							salemenu.getTime_config_id());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			if (!salemenu.getSalemenu_outer_name().equals(salemenu_outer_name)) {
				msg = String.format("修改报价单填写的对外报价单名称和修改后查询到的不一致,预期:%s,实际:%s", salemenu_outer_name,
						salemenu.getSalemenu_outer_name());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getDesc().equals(desc)) {
				msg = String.format("修改报价单填写的描述信息和修改后查询到的不一致,预期:%s,实际:%s", desc, salemenu.getDesc());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			if (!salemenu.getIs_active()) {
				msg = String.format("修改报价单填写的状态信息和修改后查询到的不一致,预期:%s,实际:%s", true, salemenu.getIs_active());
				ReporterCSS.warn(msg);
				logger.warn(msg);
				result = false;
			}

			Assert.assertEquals(result, true, "修改的报价单填写的信息和修改后查询到的不一致");
		} catch (Exception e) {
			logger.error("新建报价单遇到错误: ", e);
			Assert.fail("新建报价单遇到错误: ", e);
		}
	}

}
