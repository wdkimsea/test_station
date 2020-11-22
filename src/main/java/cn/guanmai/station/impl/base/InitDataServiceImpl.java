package cn.guanmai.station.impl.base;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.category.param.SalemenuFilterParam;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;
import cn.guanmai.station.bean.system.ServiceTimeBean;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.category.SalemenuServiceImpl;
import cn.guanmai.station.impl.invoicing.SupplierServiceImpl;
import cn.guanmai.station.impl.system.ServiceTimeServiceImpl;
import cn.guanmai.station.interfaces.base.InitDataService;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.category.SalemenuService;
import cn.guanmai.station.interfaces.invoicing.SupplierService;
import cn.guanmai.station.interfaces.system.ServiceTimeService;
import cn.guanmai.util.StringUtil;
import cn.guanmai.util.TimeUtil;

/* 
* @author liming 
* @date Nov 9, 2018 10:49:44 AM 
* @des 初始化数据工具实现类
* @version 1.0 
*/
public class InitDataServiceImpl implements InitDataService {
	private CategoryService categoryService;
	private SupplierService supplierService;
	private ServiceTimeService serviceTimeService;
	private SalemenuService salemenuService;

	public InitDataServiceImpl(Map<String, String> headers) {
		categoryService = new CategoryServiceImpl(headers);
		supplierService = new SupplierServiceImpl(headers);
		serviceTimeService = new ServiceTimeServiceImpl(headers);
		salemenuService = new SalemenuServiceImpl(headers);
	}

	@Override
	public InitDataBean getInitData() throws Exception {
		InitDataBean initData = null;

		String category1_id = null;
		String category1_name = "蔬菜";
		String category2_id = null;
		String category2_name = "叶菜";
		String pinlei_id = null;
		String pinlei_name = "茼蒿";
		String spu_id = null;
		String spu_name = "大叶茼蒿";

		Category1Bean category1 = categoryService.getCategory1ByName(category1_name);
		if (category1 == null) {
			category1 = new Category1Bean(category1_name, 1);
			category1_id = categoryService.createCategory1(category1);
			if (category1_id == null) {
				throw new Exception("创建一级分类失败");
			}
			category1 = categoryService.getCategory1ById(category1_id);
			if (category1 == null) {
				throw new Exception("获取一级分类列表失败");
			}
		} else {
			category1_id = category1.getId();
		}

		// 判断二级分类是否存在,不存在就新建
		Category2Bean category2 = categoryService.getCategory2ByName(category1_id, category2_name);
		if (category2 == null) {
			category2 = new Category2Bean(category1_id, category2_name);
			category2_id = categoryService.createCategory2(category2);
			if (category2_id == null) {
				throw new Exception("创建二级分类失败");
			} else {
				category2 = categoryService.getCategory2ById(category2_id);
				if (category2 == null) {
					throw new Exception("获取二级分类列表失败");
				}
			}
		} else {
			category2_id = category2.getId();
		}

		PinleiBean pinlei = categoryService.getPinleiByName(category2_id, pinlei_name);
		if (pinlei == null) {
			pinlei = new PinleiBean(category2_id, pinlei_name);
			pinlei_id = categoryService.createPinlei(pinlei);
			if (pinlei_id == null) {
				throw new Exception("创建品类分类失败");
			} else {
				pinlei = categoryService.getPinleiById(pinlei_id);
				if (pinlei == null) {
					throw new Exception("获取品类分类列表失败");
				}
			}
		} else {
			pinlei_id = pinlei.getId();
		}

		SpuBean spu = categoryService.getSpuByName(pinlei_id, spu_name);
		if (spu == null) {
			spu = new SpuBean(spu_name, pinlei_id, "描述", new JSONArray(), 0, "斤", new JSONArray(), 2, 0);
			spu_id = categoryService.createSpu(spu);
			if (spu_id == null) {
				throw new Exception("创建商品SPU失败");
			} else {
				spu = categoryService.getSpuById(spu_id);
				if (spu == null || spu.getId() == null) {
					throw new Exception("获取商品SPU信息失败");
				}
			}
		} else {
			spu_id = spu.getId();
			if (spu.getId() == null) {
				throw new Exception("获取商品SPU信息失败");
			}
		}

		// 判断供应商是否存在
		String customer_id = "SKU";
		String supplier_id = null;
		SupplierDetailBean supplier = null;
		List<SupplierDetailBean> supplierList = supplierService.getSupplierByCustomerId(customer_id);
		List<Category2Bean> category2List = categoryService.getCategory2List();
		if (category2List == null) {
			throw new Exception("获取二级分类列表失败");
		}

		JSONArray merchandise = JSONArray
				.parseArray(JSON.toJSONString(category2List.stream().map(c -> c.getId()).collect(Collectors.toList())));

		if (supplierList != null) {
			if (supplierList.size() > 0) {
				supplier = supplierList.stream().filter(s -> s.getCustomer_id().equals(customer_id)).findAny()
						.orElse(null);
				supplier_id = supplier.getSupplier_id();
				supplier = supplierService.getSupplierById(supplier_id);
				if (supplier == null) {
					throw new Exception("获取供应商" + supplier_id + "详细信息失败");
				}
				supplier.setMerchandise(merchandise);

				boolean result = supplierService.updateSupplier(supplier);
				if (!result) {
					throw new Exception("修改供应商信息失败");
				}
			} else {
				supplier = new SupplierDetailBean(customer_id, "自动化创建的供应商", merchandise, 1);
				supplier_id = supplierService.createSupplier(supplier);
				if (supplier_id == null) {
					throw new Exception("创建供应商失败");
				}
				supplier.setId(supplier_id);
			}
		} else {
			throw new Exception("获取供应商列表失败");
		}

		// 采购规格,没有就新建
		List<PurchaseSpecBean> purchaseSpecArray = categoryService.getPurchaseSpecArray(spu_id, supplier_id);
		PurchaseSpecBean purchaseSpec = null;
		String purchaseSpec_id = null;
		if (purchaseSpecArray == null || purchaseSpecArray.size() == 0) {
			purchaseSpec = new PurchaseSpecBean(spu_name + "|斤", StringUtil.getRandomString(6),
					String.valueOf(TimeUtil.getLongTime()), "斤", new BigDecimal(1), category1_id, category2_id,
					pinlei_id, spu_id);
			purchaseSpec_id = categoryService.createPurchaseSpec(purchaseSpec);
			if (purchaseSpec_id == null) {
				throw new Exception("创建采购规格失败");
			}

		} else {
			purchaseSpec_id = purchaseSpecArray.get(0).getId();
		}
		purchaseSpec = categoryService.getPurchaseSpecById(purchaseSpec_id);
		if (purchaseSpec == null) {
			throw new Exception("获取采购规格详情信息失败");
		}

		// 判断报价单是否存在,不存在则创建
		String salemenu_name = "[自动化] 代码创建的报价单";

		SalemenuFilterParam param = new SalemenuFilterParam("", null, null, salemenu_name);
		List<SalemenuBean> salemenuArray = salemenuService.searchSalemenu(param);

		SalemenuBean salemenu = salemenuArray.stream().filter(s -> s.getName().equals(salemenu_name)).findAny()
				.orElse(null);

		if (salemenu == null) {
			List<ServiceTimeBean> serviceTimeList = serviceTimeService.serviceTimeList();
			if (serviceTimeList == null || serviceTimeList.size() == 0) {
				ServiceTimeBean serviceTime = new ServiceTimeBean();
				serviceTime.setName("自动化创建");
				serviceTime.setDesc("自动化创建的运营时间");
				serviceTime.setFinal_distribute_time("06:00");
				serviceTime.setFinal_distribute_time_span(1);
				ServiceTimeBean.OrderTimeLimit orderTimeLimit = serviceTime.new OrderTimeLimit(0, "06:00", "23:00");
				serviceTime.setOrder_time_limit(orderTimeLimit);

				ServiceTimeBean.ReceiveTimeLimit receiveTimeLimit = serviceTime.new ReceiveTimeLimit();
				receiveTimeLimit.setE_span_time(1);
				receiveTimeLimit.setEnd("12:00");
				receiveTimeLimit.setE_span_time(1);
				receiveTimeLimit.setStart("06:00");
				receiveTimeLimit.setReceiveTimeSpan(30);
				receiveTimeLimit.setWeekdays(127);
				serviceTime.setReceive_time_limit(receiveTimeLimit);

				boolean result = serviceTimeService.createServiceTime(serviceTime);
				if (result == false) {
					throw new Exception("创建运营时间失败");
				}
			}

			serviceTimeList = serviceTimeService.serviceTimeList();
			if (serviceTimeList == null) {
				throw new Exception("获取运营时间列表失败");
			}

			salemenu = new SalemenuBean(salemenu_name, serviceTimeList.get(0).getId(), 1, new JSONArray(), "自动化", "");
			String salemenu_id = salemenuService.createSalemenu(salemenu);
			if (salemenu_id == null) {
				throw new Exception("创建销售报价单失败");
			} else {
				salemenu = salemenuService.getSalemenuById(salemenu_id);
				if (salemenu == null) {
					throw new Exception("获取报价单详情信息失败");
				}
			}
		}
		initData = new InitDataBean(category1, category2, pinlei, spu, purchaseSpec, supplier, salemenu);
		return initData;
	}

}
