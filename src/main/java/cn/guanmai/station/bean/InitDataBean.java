package cn.guanmai.station.bean;

import cn.guanmai.station.bean.category.Category1Bean;
import cn.guanmai.station.bean.category.Category2Bean;
import cn.guanmai.station.bean.category.PinleiBean;
import cn.guanmai.station.bean.category.PurchaseSpecBean;
import cn.guanmai.station.bean.category.SalemenuBean;
import cn.guanmai.station.bean.category.SpuBean;
import cn.guanmai.station.bean.invoicing.SupplierDetailBean;

/* 
* @author liming 
* @date Nov 9, 2018 10:45:14 AM 
* @des 初始化数据类
* @version 1.0 
*/
public class InitDataBean {
	private Category1Bean category1;
	private Category2Bean category2;
	private PinleiBean pinlei;
	private SpuBean spu;
	private PurchaseSpecBean purchaseSpec;
	private SupplierDetailBean supplier;
	private SalemenuBean salemenu;

	/**
	 * @return the category1
	 */
	public Category1Bean getCategory1() {
		return category1;
	}

	/**
	 * @param category1
	 *            the category1 to set
	 */
	public void setCategory1(Category1Bean category1) {
		this.category1 = category1;
	}

	/**
	 * @return the category2
	 */
	public Category2Bean getCategory2() {
		return category2;
	}

	/**
	 * @param category2
	 *            the category2 to set
	 */
	public void setCategory2(Category2Bean category2) {
		this.category2 = category2;
	}

	/**
	 * @return the pinlei
	 */
	public PinleiBean getPinlei() {
		return pinlei;
	}

	/**
	 * @param pinlei
	 *            the pinlei to set
	 */
	public void setPinlei(PinleiBean pinlei) {
		this.pinlei = pinlei;
	}

	/**
	 * @return the spu
	 */
	public SpuBean getSpu() {
		return spu;
	}

	/**
	 * @param spu
	 *            the spu to set
	 */
	public void setSpu(SpuBean spu) {
		this.spu = spu;
	}

	/**
	 * @return the purchaseSpec
	 */
	public PurchaseSpecBean getPurchaseSpec() {
		return purchaseSpec;
	}

	/**
	 * @param purchaseSpec
	 *            the purchaseSpec to set
	 */
	public void setPurchaseSpec(PurchaseSpecBean purchaseSpec) {
		this.purchaseSpec = purchaseSpec;
	}

	/**
	 * @return the supplier
	 */
	public SupplierDetailBean getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            the supplier to set
	 */
	public void setSupplier(SupplierDetailBean supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return the salemenu
	 */
	public SalemenuBean getSalemenu() {
		return salemenu;
	}

	/**
	 * @param salemenu
	 *            the salemenu to set
	 */
	public void setSalemenu(SalemenuBean salemenu) {
		this.salemenu = salemenu;
	}

	public InitDataBean(Category1Bean category1, Category2Bean category2, PinleiBean pinlei, SpuBean spu,
			PurchaseSpecBean purchaseSpec, SupplierDetailBean supplier, SalemenuBean salemenu) {
		super();
		this.category1 = category1;
		this.category2 = category2;
		this.pinlei = pinlei;
		this.spu = spu;
		this.purchaseSpec = purchaseSpec;
		this.supplier = supplier;
		this.salemenu = salemenu;
	}

}
