package cn.guanmai.station.purchase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cn.guanmai.station.bean.InitDataBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.purchase.PurcahseTaskSupplierBean;
import cn.guanmai.station.bean.purchase.PurchaseSpecSuppliersBean;
import cn.guanmai.station.bean.purchase.param.PurchaseTaskCreateParam;
import cn.guanmai.station.impl.category.CategoryServiceImpl;
import cn.guanmai.station.impl.purchase.PurchaseTaskServiceImpl;
import cn.guanmai.station.interfaces.category.CategoryService;
import cn.guanmai.station.interfaces.purchase.PurchaseTaskService;
import cn.guanmai.station.tools.LoginStation;
import cn.guanmai.util.NumberUtil;
import cn.guanmai.util.ReporterCSS;

/* 
* @author liming 
* @date Nov 28, 2018 2:35:54 PM 
* @des 手工新建采购条目
* @version 1.0 
*/
public class PurchaseTaskCreateTest extends LoginStation {
	private Logger logger = LoggerFactory.getLogger(PurchaseTaskCreateTest.class);
	private CategoryService categoryService;
	private PurchaseTaskService purchaseTaskService;
	private InitDataBean initData;

	@BeforeClass
	public void initData() {
		Map<String, String> headers = getStationCookie();
		purchaseTaskService = new PurchaseTaskServiceImpl(headers);
		categoryService = new CategoryServiceImpl(headers);
		initData = getInitData();
	}

	@Test
	public void purchaseTaskCreateTestCase01() {
		try {
			ReporterCSS.title("测试点: 批量新建采购条目");
			List<String> search_texts = Arrays.asList("a", "b", "c", "e", "j");
			List<PurchaseTaskCreateParam> tasks = new ArrayList<>();

			OK: for (String text : search_texts) {
				List<SpuBean> spuArray = categoryService.branchSpu(text);
				Assert.assertNotEquals(spuArray, null, "新建采购任务,搜索过滤SPU失败");
				for (SpuBean spu : spuArray) {
					List<PurchaseSpecSuppliersBean> purchaseSpecSuppliers = purchaseTaskService
							.searchPurchaseSpecSuppliers(spu.getSpu_id());
					Assert.assertNotEquals(purchaseSpecSuppliers, null, "按SPU ID查找没有采购规格失败");
					for (PurchaseSpecSuppliersBean purchaseSpecSupplier : purchaseSpecSuppliers) {
						if (purchaseSpecSupplier.getSettle_suppliers().size() > 0) {
							PurchaseTaskCreateParam purchaseTaskCreateParam = new PurchaseTaskCreateParam();
							purchaseTaskCreateParam.setSpec_id(purchaseSpecSupplier.getSpec_id());
							purchaseTaskCreateParam.setPlan_purchase_amount(NumberUtil.getRandomNumber(5, 20, 1));

							List<PurcahseTaskSupplierBean> purcahseTaskSuppliers = purchaseTaskService
									.getPurcahseTaskSuppliers(purchaseSpecSupplier.getSpec_id());
							Assert.assertNotEquals(purcahseTaskSuppliers, null, "按采购规格ID查找对应的供应商列表失败");
							PurcahseTaskSupplierBean purcahseTaskSupplier = NumberUtil
									.roundNumberInList(purcahseTaskSuppliers);

							if (purcahseTaskSupplier.getSettle_supplier_id() == null
									|| purcahseTaskSupplier.getDefault_purchaser_id() == null) {
								continue;
							}
							purchaseTaskCreateParam.setSettle_supplier_id(purcahseTaskSupplier.getSettle_supplier_id());
							purchaseTaskCreateParam
									.setPurchaser_id(new BigDecimal(purcahseTaskSupplier.getDefault_purchaser_id()));

							tasks.add(purchaseTaskCreateParam);
							if (tasks.size() >= 6) {
								break OK;
							}
						}
					}
				}
			}
			boolean result = purchaseTaskService.createPurchaseTasks(tasks);
			Assert.assertEquals(result, true, "批量新建采购任务失败");
		} catch (Exception e) {
			logger.error("批量新建采购任务遇到错误: ", e);
			Assert.fail("批量新建采购任务遇到错误: ", e);
		}
	}

	@Test
	public void purchaseTaskCreateTestCase02() {
		ReporterCSS.title("测试点: 批量新建采购条目中间调用到的接口");
		try {
			String settle_supplier_id = initData.getSupplier().getId();
			List<String> purchaser_ids = purchaseTaskService.optionalSupplierSurchasers(settle_supplier_id);
			Assert.assertNotEquals(purchaser_ids, null, "根据供应商ID获取对应的采购员列表失败");
		} catch (Exception e) {
			logger.error("批量新建采购条目中间调用到的接口调用遇到错误: ", e);
			Assert.fail("批量新建采购条目中间调用到的接口调用遇到错误: ", e);
		}
	}

}
