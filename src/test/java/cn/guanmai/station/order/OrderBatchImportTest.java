package cn.guanmai.station.order;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;

import cn.guanmai.station.bean.order.CustomerBean;
import cn.guanmai.station.bean.order.OrderBatchResultBean;
import cn.guanmai.station.bean.order.OrderBatchUploadResultBean;
import cn.guanmai.station.bean.order.OrderImportResultBean;
import cn.guanmai.station.bean.order.OrderReceiveTimeBean;
import cn.guanmai.station.bean.order.param.OrderBatchCreateParam;
import cn.guanmai.station.bean.order.param.OrderSkuModel1;
import cn.guanmai.station.bean.order.param.OrderSkuModel2;
import cn.guanmai.station.bean.order.param.OrderSkuParam;
import cn.guanmai.station.bean.system.OrderImportTemlateBean;
import cn.guanmai.station.bean.system.param.OrderImportTemplateParam;
import cn.guanmai.station.impl.async.AsyncServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.system.TemplateServiceImpl;
import cn.guanmai.station.interfaces.async.AsyncService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.system.TemplateService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;
import cn.guanmai.util.StringUtil;

public class OrderBatchImportTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(OrderBatchImportTest.class);
	private OrderService orderService;
	private TemplateService templateService;
	private AsyncService asyncService;

	private String template_id;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		orderService = new OrderServiceImpl(headers);
		asyncService = new AsyncServiceImpl(headers);
		templateService = new TemplateServiceImpl(headers);
		try {
			List<OrderImportTemlateBean> orderImportTemlates = templateService.getOrderImportTemlateList();
			Assert.assertNotEquals(orderImportTemlates, null, "获取订单导入模板列表失败");

			String template_name = "自动化测试专用模板(请勿修改)";

			OrderImportTemlateBean orderImportTemlate = orderImportTemlates.stream()
					.filter(t -> t.getName().equals(template_name)).findAny().orElse(null);

			if (orderImportTemlate == null) {
				template_id = templateService.createOrderImportTemplate(new OrderImportTemplateParam());
				Assert.assertNotEquals(template_id, null, "新建订单导入模板失败");
			} else {
				template_id = orderImportTemlate.getId();
			}
		} catch (Exception e) {
			logger.error("初始化订单导入模板遇到错误: ", e);
			Assert.fail("初始化订单导入模板遇到错误: ", e);
		}
	}

	@Test
	public void orderBatchImportTestCase01() {
		ReporterCSS.title("测试点: 批量导入订单,单个商户导入");
		InputStream in = null;
		OutputStream out = null;
		try {
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(3);
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

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C", "J", "Y" };
			List<OrderSkuParam> skuArray = orderService.orderSkus(address_id, time_config_id, search_texts, 10);
			Assert.assertEquals(skuArray != null && skuArray.size() > 0, true, "下单搜索搜商品列表为空");

			String save_dir = System.getProperty("user.dir") + "/temp/";
			String order_batch_import_excel_path = save_dir + "batch_import_order.xlsx";

			// 开始处理Excel,读取文件把数据存储到集合中

			OrderSkuModel2 orderSkuModel = null;
			List<OrderSkuModel2> writeDatas = new ArrayList<OrderSkuModel2>();
			for (OrderSkuParam orderSku : skuArray) {
				orderSkuModel = new OrderSkuModel2();
				orderSkuModel.setAddress_id(address_id);
				orderSkuModel.setAddress_name(customer.getResname());
				orderSkuModel.setSku_id(orderSku.getSku_id());
				orderSkuModel.setSale_price(orderSku.getUnit_price().doubleValue());
				orderSkuModel.setQuantity((orderSku.getAmount().doubleValue()));
				orderSkuModel.setRemark(StringUtil.getRandomNumber(6));
				writeDatas.add(orderSkuModel);
			}

			// 开始写文件
			boolean result = true;
			File file = new File(save_dir);
			if (!file.isDirectory()) {
				result = file.mkdir();
				Assert.assertEquals(result, true, "创建临时目录失败");
			}
			file = new File(order_batch_import_excel_path);
			if (file.exists()) {
				result = file.delete();
				Assert.assertEquals(result, true, "删除临时文件失败");
			}
			file.createNewFile();
			Assert.assertEquals(result, true, "创建临时文件失败");
			out = new FileOutputStream(file);

			EasyExcel.write(file, OrderSkuModel2.class).sheet(0, "商品").doWrite(writeDatas);

			// 开始上传文件
			OrderBatchUploadResultBean orderBatchUpdateResul = orderService.orderBatchUpload(time_config_id,
					order_batch_import_excel_path, template_id);

			Assert.assertNotEquals(orderBatchUpdateResul, null, "批量导入订单文件上传失败");

			String task_id = orderBatchUpdateResul.getTask_id();
			String file_name = orderBatchUpdateResul.getFile_name();

			OrderBatchCreateParam orderBatchCreateParam = new OrderBatchCreateParam();
			orderBatchCreateParam.setTask_id(task_id);
			orderBatchCreateParam.setFile_name(file_name);
			orderBatchCreateParam.setTime_config_id(time_config_id);

			List<OrderBatchCreateParam.Data> dataArray = new ArrayList<OrderBatchCreateParam.Data>();
			OrderBatchCreateParam.Data data = orderBatchCreateParam.new Data();
			data.setAddress_id(address_id);
			data.setUid(uid);
			data.setDetails(skuArray);
			data.setReceive_begin_time(receive_begin_time);
			data.setReceive_end_time(receive_end_time);
			// data.setFlagStart(day_span);
			// data.setFlagEnd(day_span);
			data.setTimeStart(receive_begin_time.split(" ")[1]);
			data.setTimeEnd(receive_end_time.split(" ")[1]);
			dataArray.add(data);
			orderBatchCreateParam.setData(dataArray);

			String user_task_id = orderService.orderBatchSubmite(orderBatchCreateParam);
			Assert.assertNotEquals(user_task_id, null, "批量导入创建订单异步任务创建失败");

			result = asyncService.getAsyncTaskResult(user_task_id, "批量导入(成功1)");
			if (!result) {
				ReporterCSS.warn("订单批量导入的异步任务执行失败");
				logger.warn("订单批量导入的异步任务执行失败");
			}

			OrderBatchResultBean orderBatchResult = orderService.getOrderBatchResult(user_task_id);

			if (orderBatchResult == null) {
				ReporterCSS.warn("订单批量导入的异步任务执行结果查看失败");
				logger.warn("订单批量导入的异步任务执行结果查看失败");
			}
			Assert.assertEquals(result && orderBatchResult != null, true);
		} catch (Exception e) {
			logger.error("批量导入订单过程中遇到错误: ", e);
			Assert.fail("批量导入订单过程中遇到错误: ", e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("输入输出流关闭遇到错误: ", e);
				Assert.fail("输入输出流关闭遇到错误:  ", e);
			}
		}
	}

	@Test
	public void orderBatchImportTestCase02() {
		ReporterCSS.title("测试点: 批量导入订单,多个商户导入");
		OutputStream out = null;
		try {
			List<CustomerBean> customerArray = orderService.getOrderCustomerArray(4);
			Assert.assertNotEquals(customerArray, null, "获取下单商户列表信息失败");

			Assert.assertEquals(customerArray.size() >= 2, true, "可用下单商户不足2个,此测试用例无法执行");

			// 寻找有共同运营时间的两个商户
			List<String> time_config_ids = null;
			String time_config_id = null;
			String address_id_1 = null;
			String uid_1 = null;

			String address_id_2 = null;
			String uid_2 = null;
			OrderReceiveTimeBean orderReceiveTime = null;
			OK: for (CustomerBean customer : customerArray) {
				String address_id = customer.getAddress_id();

				List<OrderReceiveTimeBean> orderReceiveTimes = orderService.getCustomerServiceTimeArray(address_id);
				Assert.assertNotEquals(orderReceiveTimes, null, "获取下单商户对应的运营时间失败");

				if (orderReceiveTimes.size() > 0) {
					if (time_config_ids == null) {
						address_id_1 = customer.getAddress_id();
						uid_1 = customer.getId();
						time_config_ids = orderReceiveTimes.stream().map(s -> s.getTime_config_id())
								.collect(Collectors.toList());
					} else {
						List<String> temp_time_config_ids = orderReceiveTimes.stream().map(s -> s.getTime_config_id())
								.collect(Collectors.toList());
						for (String temp_time_config_id : temp_time_config_ids) {
							if (time_config_ids.contains(temp_time_config_id)) {
								orderReceiveTime = orderReceiveTimes.stream()
										.filter(s -> s.getTime_config_id().equals(temp_time_config_id)).findAny()
										.orElse(null);
								time_config_id = temp_time_config_id;
								Assert.assertEquals(orderReceiveTime.getReceive_times().size() > 0, true,
										"受收货自然日限制,运营时间" + time_config_id + "无可用收货日期可选");
								address_id_2 = customer.getAddress_id();
								uid_2 = customer.getId();
								break OK;
							}
						}
					}
				}
			}

			Assert.assertNotEquals(address_id_2, null, "没有找到同属于一个运营时间下的两个商户,无法进行多商户批量导入订单");

			List<OrderReceiveTimeBean.ReceiveTime> receiveTimes = orderReceiveTime.getReceive_times().stream()
					.filter(r -> r.getTimes().size() >= 2).collect(Collectors.toList());
			Assert.assertEquals(receiveTimes.size() > 0, true, "运营时间的收货时间无法取值了(当前时间过了收货时间结束时间或者收货起始和收货结束时间一致)");

			OrderReceiveTimeBean.ReceiveTime receiveTime = NumberUtil.roundNumberInList(receiveTimes);
			List<String> receive_times = receiveTime.getTimes();
			int index = new Random().nextInt(receive_times.size() - 1);
			String receive_begin_time = receive_times.get(index);
			String receive_end_time = receive_times.get(index + 1);

			// 下单商品集合
			String[] search_texts = new String[] { "A", "B", "C", "J", "Y" };
			List<OrderSkuParam> skuArray1 = orderService.orderSkus(address_id_1, time_config_id, search_texts, 4);
			Assert.assertEquals(skuArray1 != null && skuArray1.size() > 0, true, "下单搜索搜商品列表为空");

			List<OrderSkuParam> skuArray2 = orderService.orderSkus(address_id_2, time_config_id, search_texts, 4);
			Assert.assertEquals(skuArray2 != null && skuArray2.size() > 0, true, "下单搜索搜商品列表为空");

			// 开始处理Excel,读取文件把数据存储到集合

			OrderSkuModel2 orderSkuModel = null;
			List<OrderSkuModel2> writeDatas = new ArrayList<OrderSkuModel2>();

			for (OrderSkuParam orderSku : skuArray1) {
				orderSkuModel = new OrderSkuModel2();
				orderSkuModel.setAddress_id(address_id_1);
				orderSkuModel.setSku_id(orderSku.getSku_id());
				orderSkuModel.setSale_price(orderSku.getUnit_price().floatValue());
				orderSkuModel.setQuantity((orderSku.getAmount().floatValue()));
				orderSkuModel.setRemark(StringUtil.getRandomNumber(6));
				writeDatas.add(orderSkuModel);
			}

			for (OrderSkuParam orderSku : skuArray2) {
				orderSkuModel = new OrderSkuModel2();
				orderSkuModel.setAddress_id(address_id_2);
				orderSkuModel.setSku_id(orderSku.getSku_id());
				orderSkuModel.setSale_price(orderSku.getUnit_price().doubleValue());
				orderSkuModel.setQuantity((orderSku.getAmount().doubleValue()));
				orderSkuModel.setRemark(StringUtil.getRandomNumber(6));
				writeDatas.add(orderSkuModel);
			}

			String save_dir = System.getProperty("user.dir") + "/temp/";
			String order_batch_import_excel_path = save_dir + "batch_import_order.xlsx";
			// 开始写文件
			// 开始写文件
			boolean result = true;
			File file = new File(save_dir);
			if (!file.isDirectory()) {
				result = file.mkdir();
				Assert.assertEquals(result, true, "创建临时目录失败");
			}

			file = new File(order_batch_import_excel_path);
			if (file.exists()) {
				result = file.delete();
				Assert.assertEquals(result, true, "删除临时文件失败");
			}
			file.createNewFile();
			Assert.assertEquals(result, true, "创建临时文件失败");
			out = new FileOutputStream(file);

			EasyExcel.write(file, OrderSkuModel2.class).sheet(0, "商品").doWrite(writeDatas);
			// 开始上传文件
			OrderBatchUploadResultBean orderBatchUpdateResul = orderService.orderBatchUpload(time_config_id,
					order_batch_import_excel_path, template_id);

			Assert.assertNotEquals(orderBatchUpdateResul, null, "批量导入订单文件上传失败");

			String task_id = orderBatchUpdateResul.getTask_id();
			String file_name = orderBatchUpdateResul.getFile_name();

			OrderBatchCreateParam orderBatchCreateParam = new OrderBatchCreateParam();
			orderBatchCreateParam.setTask_id(task_id);
			orderBatchCreateParam.setFile_name(file_name);
			orderBatchCreateParam.setTime_config_id(time_config_id);

			List<OrderBatchCreateParam.Data> dataArray = new ArrayList<OrderBatchCreateParam.Data>();
			OrderBatchCreateParam.Data data1 = orderBatchCreateParam.new Data();
			// 商户1
			data1.setAddress_id(address_id_1);
			data1.setUid(uid_1);
			data1.setDetails(skuArray1);
			data1.setReceive_begin_time(receive_begin_time);
			data1.setReceive_end_time(receive_end_time);
			// data1.setFlagStart(day_span);
			// data1.setFlagEnd(day_span);
			data1.setTimeStart(receive_begin_time.split(" ")[1]);
			data1.setTimeEnd(receive_end_time.split(" ")[1]);
			dataArray.add(data1);

			// 商户2
			OrderBatchCreateParam.Data data2 = orderBatchCreateParam.new Data();
			data2.setAddress_id(address_id_2);
			data2.setUid(uid_2);
			data2.setDetails(skuArray2);
			data2.setReceive_begin_time(receive_begin_time);
			data2.setReceive_end_time(receive_end_time);
			// data2.setFlagStart(day_span);
			// data2.setFlagEnd(day_span);
			data2.setTimeStart(receive_begin_time.split(" ")[1]);
			data2.setTimeEnd(receive_end_time.split(" ")[1]);
			dataArray.add(data2);

			orderBatchCreateParam.setData(dataArray);

			String user_task_id = orderService.orderBatchSubmite(orderBatchCreateParam);
			Assert.assertNotEquals(user_task_id, null, "批量导入创建订单异步任务创建失败");

			result = asyncService.getAsyncTaskResult(user_task_id, "批量导入(成功2)");
			if (!result) {
				ReporterCSS.warn("订单批量导入的异步任务执行失败");
				logger.warn("订单批量导入的异步任务执行失败");
			}

			OrderBatchResultBean orderBatchResult = orderService.getOrderBatchResult(user_task_id);

			if (orderBatchResult == null) {
				ReporterCSS.warn("订单批量导入的异步任务执行结果查看失败");
				logger.warn("订单批量导入的异步任务执行结果查看失败");
			}
			Assert.assertEquals(result && orderBatchResult != null, true);
		} catch (

		Exception e) {
			logger.error("批量导入订单过程中遇到错误: ", e);
			Assert.fail("批量导入订单过程中遇到错误: ", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				logger.error("输入输出流关闭遇到错误: ", e);
				Assert.fail("输入输出流关闭遇到错误:  ", e);
			}
		}
	}

	@Test
	public void orderBatchImportTestCase03() {
		ReporterCSS.title("测试点: 订单导入(新建订单页面里的入口)");
		try {
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

			String file_path = orderService.downloadOrderTemplate(address_id, time_config_id);
			List<OrderSkuModel1> orderSkuModel1List = EasyExcelFactory.read(file_path).head(OrderSkuModel1.class)
					.sheet(0).doReadSync();

			orderSkuModel1List = NumberUtil.roundNumberInList(orderSkuModel1List, 6);

			for (OrderSkuModel1 orderSkuModel1 : orderSkuModel1List) {
				orderSkuModel1.setQuantity(NumberUtil.getRandomNumber(5, 10, 1));
				orderSkuModel1.setSale_price(NumberUtil.getRandomNumber(4, 8, 2));
				orderSkuModel1.setRemark(StringUtil.getRandomString(6));
			}

			EasyExcel.write(file_path, OrderSkuModel1.class).sheet(0, "SKU").doWrite(orderSkuModel1List);

			List<OrderImportResultBean> skus = orderService.importOrder(address_id, time_config_id, file_path);
			Assert.assertNotEquals(skus, null, "订单导入商品失败");

			boolean result = true;
			String msg = null;
			for (OrderSkuModel1 orderSkuModel1 : orderSkuModel1List) {
				OrderImportResultBean sku = skus.stream().filter(s -> s.getId().equals(orderSkuModel1.getId()))
						.findAny().orElse(null);
				if (sku == null) {
					msg = String.format("订单导入,Excle中的商品%s导入后没有返回", orderSkuModel1.getId());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
					continue;
				}

				if (sku.getQuantity().compareTo(orderSkuModel1.getQuantity()) != 0) {
					msg = String.format("订单导入,Excle中的商品%s,Excle中记录的下单数和返回的不一致,预期:%s,实际:%s", orderSkuModel1.getId(),
							orderSkuModel1.getQuantity(), sku.getQuantity());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (sku.getSale_price().compareTo(orderSkuModel1.getSale_price()) != 0) {
					msg = String.format("订单导入,Excle中的商品%s,Excle中记录的价格和返回的不一致,预期:%s,实际:%s", orderSkuModel1.getId(),
							orderSkuModel1.getSale_price(), sku.getSale_price());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}

				if (!sku.getSpu_remark().equals(orderSkuModel1.getRemark())) {
					msg = String.format("订单导入,Excle中的商品%s,Excle中记录的备注信息和返回的不一致,预期:%s,实际:%s", orderSkuModel1.getId(),
							orderSkuModel1.getRemark(), sku.getSpu_remark());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}

			Assert.assertEquals(result, true, "订单导入,导入的商品相关信息与返回的不一致");
		} catch (Exception e) {
			logger.error("订单导入遇到错误: ", e);
			Assert.fail("订单导入遇到错误: ", e);
		}
	}
}
