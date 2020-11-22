package cn.guanmai.station.interfaces.purchase;

import java.util.List;

import cn.guanmai.station.bean.purchase.PurchaserBean;
import cn.guanmai.station.bean.purchase.PurchaserResponseBean;
import cn.guanmai.station.bean.purchase.PurchaserBean.SettleSupplier;
import cn.guanmai.station.bean.purchase.param.PurchaserParam;

/* 
* @author liming 
* @date Nov 26, 2018 7:57:18 PM 
* @des 采购员相关接口实现
* @version 1.0 
*/
public interface PurchaserService {
	/**
	 * 搜索查询采购员
	 * 
	 * @param search_text
	 * @return
	 * @throws Exception
	 */
	public List<PurchaserBean> searchPurchaser(String search_text) throws Exception;

	/**
	 * 获取采购员详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PurchaserBean getPurchaserDetail(String id) throws Exception;

	/**
	 * 新建一个采购员
	 * 
	 * @param purchaserParam
	 * @return
	 * @throws Exception
	 */
	public PurchaserResponseBean createPurchaser(PurchaserParam purchaserParam) throws Exception;

	/**
	 * 删除指定ID的采购员
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deletePurchaser(String id) throws Exception;

	/**
	 * 修改采购员信息
	 * 
	 * @param purchaser
	 * @return
	 * @throws Exception
	 */
	public PurchaserResponseBean updatePurchaser(PurchaserParam purchaserParam) throws Exception;

	/**
	 * 采购员登录Station
	 * 
	 * @param phone
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public boolean purchaserLoginStation(String phone, String pwd) throws Exception;

	/**
	 * 获取还没有绑定采购员的供应商列表
	 * 
	 * @return
	 */
	public List<SettleSupplier> getNoBindSettleSupplierArray() throws Exception;

}
