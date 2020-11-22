package cn.guanmai.open.purchase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.open.bean.purchase.OpenPurcahserBean;
import cn.guanmai.open.bean.purchase.param.OpenPurchaserFilterParam;
import cn.guanmai.open.bean.stock.OpenSupplierBean;
import cn.guanmai.open.bean.stock.param.OpenSupplierFilterParam;
import cn.guanmai.open.impl.purchase.OpenPurcahseServiceImpl;
import cn.guanmai.open.impl.stock.OpenSupplierServiceImpl;
import cn.guanmai.open.interfaces.purchase.OpenPurcahseService;
import cn.guanmai.open.interfaces.stock.OpenSupplierService;
import cn.guanmai.open.tool.AccessToken;
import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.impl.purchase.PurchaserServiceImpl;
import cn.guanmai.station.interfaces.purchase.PurchaserService;
import cn.guanmai.util.ReporterCSS;

/**
 * @author liming
 * @date 2019年11月12日
 * @time 上午11:20:14
 * @des TODO
 */

public class OpenPurchaserTest extends AccessToken {
	private Logger logger = LoggerFactory.getLogger(OpenPurchaserTest.class);
	private OpenPurcahseService openPurcahseService;
	private OpenSupplierService openSupplierService;
	private PurchaserService purchaserService;

	@BeforeClass
	public void initData() {
		String access_token = getAccess_token();
		openPurcahseService = new OpenPurcahseServiceImpl(access_token);
		openSupplierService = new OpenSupplierServiceImpl(access_token);

		purchaserService = new PurchaserServiceImpl(getSt_headers());
	}

	@Test
	public void openPurchaserTestCase01() {
		ReporterCSS.title("测试点: 根据供应商查询采购员");
		try {
			List<PurchaserBean> purchaserList = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchaserList, null, "ST获取采购员列表失败");

			List<OpenPurcahserBean> openPurcahserList = openPurcahseService
					.queryPurchaser(new OpenPurchaserFilterParam());
			Assert.assertNotEquals(openPurcahserList, null, "查询采购员失败");

			String msg = null;
			boolean result = true;
			for (PurchaserBean purchaser : purchaserList) {
				OpenPurcahserBean openPurcahser = openPurcahserList.stream()
						.filter(p -> p.getPurchaser_id().toString().equals(purchaser.getId())).findAny().orElse(null);
				if (openPurcahser == null) {
					msg = String.format("开放接口拉取的采购员列表少了采购员 %s", purchaser.getName());
					ReporterCSS.warn(msg);
					logger.warn(msg);
					result = false;
				}
			}
			Assert.assertEquals(result, true, "开放平台接口拉取的采购员信息缺失");
		} catch (Exception e) {
			logger.error("查询采购员遇到错误: ", e);
			Assert.fail("查询采购员遇到错误: ", e);
		}
	}

	@Test
	public void openPurchaserTestCase02() {
		ReporterCSS.title("测试点: 根据供应商查询采购员");
		try {
			List<PurchaserBean> purchaserList = purchaserService.searchPurchaser("");
			Assert.assertNotEquals(purchaserList, null, "ST获取采购员列表失败");

			// 供应商 对应 采购员集合
			Map<String, Set<String>> supplierPurchaserMap = new HashMap<String, Set<String>>();
			for (PurchaserBean purchaser : purchaserList) {
				for (PurchaserBean.SettleSupplier settleSupplier : purchaser.getSettle_suppliers()) {
					if (supplierPurchaserMap.containsKey(settleSupplier.getId())) {
						Set<String> purchaserSet = supplierPurchaserMap.get(settleSupplier.getId());
						purchaserSet.add(purchaser.getId());
					} else {
						Set<String> purchaserSet = new HashSet<String>();
						purchaserSet.add(purchaser.getId());
						supplierPurchaserMap.put(settleSupplier.getId(), purchaserSet);
					}
				}
			}

			List<OpenSupplierBean> supplierList = openSupplierService.querySupplier(new OpenSupplierFilterParam());
			String supplier_id = null;

			String msg = null;
			boolean result = true;
			OpenPurchaserFilterParam openPurchaserFilterParam = new OpenPurchaserFilterParam();
			List<OpenPurcahserBean> purcahserList = null;
			for (OpenSupplierBean supplier : supplierList) {
				supplier_id = supplier.getSupplier_id();
				openPurchaserFilterParam.setSupplier_id(supplier_id);
				purcahserList = openPurcahseService.queryPurchaser(openPurchaserFilterParam);
				Assert.assertNotEquals(purcahserList, null, "根据供应商查询采购员失败");

				if (supplierPurchaserMap.containsKey(supplier_id)) {
					Set<String> purchaserSetExpected = supplierPurchaserMap.get(supplier_id);
					Set<String> purchaserSetActual = purcahserList.stream()
							.map(p -> String.valueOf(p.getPurchaser_id())).collect(Collectors.toSet());
					if (!purchaserSetExpected.equals(purchaserSetActual)) {
						msg = String.format("根据供应商查询采购员的结果与预期不一致,供应商%s,预期采购员列表:%s,实际采购员列表:%s", supplier_id,
								purchaserSetExpected, purchaserSetActual);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				} else {
					if (purcahserList.size() != 0) {
						msg = String.format("根据供应商查询采购员的结果与预期不一致,供应商%s对应的采购员个数应改为0", supplier_id);
						ReporterCSS.warn(msg);
						logger.warn(msg);
						result = false;
					}
				}
			}
			Assert.assertEquals(result, true, "根据供应商查询采购员结果与预期不一致");
		} catch (Exception e) {
			logger.error("根据供应商查询采购员遇到错误: ", e);
			Assert.fail("根据供应商查询采购员遇到错误: ", e);
		}
	}
}
