package cn.guanmai.station.tools;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.testng.Assert;

import cn.guanmai.station.bean.category.SkuBean;
import cn.guanmai.station.bean.category.SkuMeasurementBean;
import cn.guanmai.station.bean.order.OrderDetailBean;
import cn.guanmai.station.bean.purchase.PrioritySupplierBean;
import cn.guanmai.station.bean.purchase.PurchaseTaskBean.PurchaseTaskData;
import cn.guanmai.station.bean.purchase.PurchaseTaskExpectedBean;
import cn.guanmai.station.bean.purchase.param.PrioritySupplierFilterParam;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskFilterParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.order.OrderServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.order.OrderService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date May 24, 2019 3:34:05 PM 
* @des 采购任务工具类
* @version 1.0 
*/
public class PurchaseTaskTool {
	private PurchaseTaskService purchaseTaskService;
	private CategoryService categoryService;
	private OrderService orderService;

	public PurchaseTaskTool(Map<String, String> headers) {
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		orderService = new OrderServiceImpl(headers);
	}

	public List<PurchaseTaskData> getOrderPurcahseTask(String order_id) throws Exception {
		// 按下单日期
		int offset = 0;
		int limit = 50;
		PurchaseTaskFilterParam purchaseTaskFilterParam = new PurchaseTaskFilterParam();
		String begin_time = TimeUtil.getCurrentTime("yyyy-MM-dd 00:00:00");
		String end_time = TimeUtil.calculateTime("yyyy-MM-dd 00:00:00", begin_time, 1, Calendar.DATE);
		purchaseTaskFilterParam.setQ_type(1);
		purchaseTaskFilterParam.setQ(order_id);
		purchaseTaskFilterParam.setBegin_time(begin_time);
		purchaseTaskFilterParam.setEnd_time(end_time);
		purchaseTaskFilterParam.setLimit(limit);

		List<PurchaseTaskData> purchaseTaskDataList = new ArrayList<PurchaseTaskData>();
		List<PurchaseTaskData> tempPurchaseTaskDateList = null;
		int loop_times = 20;
		while (loop_times-- > 0) {
			purchaseTaskFilterParam.setOffset(offset);
			tempPurchaseTaskDateList = purchaseTaskService.newSearchPurchaseTask(purchaseTaskFilterParam);
			if (tempPurchaseTaskDateList == null) {
				throw new Exception("搜索过滤采购任务失败");
			}
			if (tempPurchaseTaskDateList.size() == 0) {
				Thread.sleep(3000);
				continue;
			}
			purchaseTaskDataList.addAll(tempPurchaseTaskDateList);
			if (tempPurchaseTaskDateList.size() < limit) {
				break;
			}
			offset += limit;
		}
		return purchaseTaskDataList;
	}

	/**
	 * 根据订单获取预期的采购任务
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTaskExpectedBean> getOrderExpectedPurcahseTask(String order_id) throws Exception {
		OrderDetailBean orderDetail = orderService.getOrderDetailById(order_id);
		Assert.assertNotEquals(orderDetail, null, "获取订单 " + order_id + " 详细信息失败");

		Map<String, List<SkuMeasurementBean>> skuMeasurementMap = categoryService.getSkuMeasurementMap();
		Assert.assertNotEquals(skuMeasurementMap, null, "获取单位对应关系列表失败");

		String temp_spu_id = null;
		String temp_sku_id = null;
		SkuBean temp_sku = null;
		BigDecimal quantity = null;

		NumberFormat nf = new DecimalFormat("000000");
		String address_id = "S" + nf.format(Double.valueOf(orderDetail.getCustomer().getAddress_id()));
		List<PrioritySupplierBean> prioritySuppliers = null;
		PrioritySupplierFilterParam prioritySupplierFilterParam = new PrioritySupplierFilterParam();

		String std_unit_name = null;
		PurchaseTaskExpectedBean purchaseTaskExpected = null;
		List<PurchaseTaskExpectedBean> purchaseTaskExpectedList = new ArrayList<PurchaseTaskExpectedBean>();
		for (OrderDetailBean.Detail detail : orderDetail.getDetails()) {
			purchaseTaskExpected = new PurchaseTaskExpectedBean();
			temp_spu_id = detail.getSpu_id();
			temp_sku_id = detail.getSku_id();
			List<String> sku_ids = new ArrayList<String>();
			sku_ids.add(temp_sku_id);
			purchaseTaskExpected.setSku_ids(sku_ids);
			purchaseTaskExpected.setOrder_id(order_id);

			temp_sku = categoryService.getSaleSkuById(temp_spu_id, temp_sku_id);
			Assert.assertNotEquals(temp_sku, null, "获取销售SKU详细信息失败");

			String std_unit_name_forsale = detail.getStd_unit_name_forsale();
			std_unit_name = temp_sku.getStd_unit_name();
			purchaseTaskExpected.setSpu_name(temp_sku.getSpu_name());
			BigDecimal std_ratio = new BigDecimal("1");
			if (skuMeasurementMap.containsKey(std_unit_name)) {
				List<SkuMeasurementBean> skuMeasurementList = skuMeasurementMap.get(std_unit_name);
				SkuMeasurementBean skuMeasurement = skuMeasurementList.stream()
						.filter(s -> s.getStd_unit_name_forsale().equals(std_unit_name_forsale)).findAny().orElse(null);
				std_ratio = skuMeasurement.getStd_ratio();
			}
			// 能分切才有损耗,获取下单基本单位数量,就是采购数
			if (temp_sku.getSlitting() == 1) {
				quantity = detail.getStd_unit_quantity().multiply(std_ratio)
						.multiply(new BigDecimal("1").add(temp_sku.getAttrition_rate().divide(new BigDecimal("100"))))
						.setScale(2, BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
			} else {
				quantity = detail.getStd_unit_quantity().multiply(std_ratio);
			}

			purchaseTaskExpected.setPlan_purchase_amount(quantity);

			// 查看商品有无优先供应商
			prioritySupplierFilterParam.setSku_id(temp_sku_id);
			prioritySuppliers = purchaseTaskService.queryPrioritySupplier(prioritySupplierFilterParam);
			Assert.assertNotEquals(prioritySuppliers, null, "查询优先供应商失败");

			PrioritySupplierBean prioritySupplier = prioritySuppliers.stream()
					.filter(p -> p.getSku_id().equals(prioritySupplierFilterParam.getSku_id())
							&& p.getAddress_id().equals(address_id))
					.findAny().orElse(null);
			String supplier_id = temp_sku.getSupplier_id();

			if (prioritySupplier != null) {
				if (prioritySupplier.getSupplier_id() != null && !prioritySupplier.getSupplier_id().trim().equals("")) {
					supplier_id = prioritySupplier.getSupplier_id();
				}
			}
			String temp_supplier_id = supplier_id == null ? "" : supplier_id;

			purchaseTaskExpected.setSupplier_id(temp_supplier_id);

			// 获取采购规格SKU
			String purchase_spec_id = temp_sku.getPurchase_spec_id();
			purchaseTaskExpected.setSpec_id(purchase_spec_id);

			PurchaseTaskExpectedBean tempPurchaseTaskExpected = purchaseTaskExpectedList.stream()
					.filter(s -> s.getSupplier_id().equals(temp_supplier_id) && s.getSpec_id().equals(purchase_spec_id))
					.findAny().orElse(null);
			if (tempPurchaseTaskExpected != null) {
				tempPurchaseTaskExpected
						.setPlan_purchase_amount(tempPurchaseTaskExpected.getPlan_purchase_amount().add(quantity));
				sku_ids = tempPurchaseTaskExpected.getSku_ids();
				sku_ids.add(temp_sku_id);
			} else {
				purchaseTaskExpectedList.add(purchaseTaskExpected);
			}
		}
		return purchaseTaskExpectedList;
	}
}
