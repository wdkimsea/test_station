package cn.guanmai.station.interfaces.system;

import java.util.List;

import cn.guanmai.station.bean.system.BoxTemplateBean;
import cn.guanmai.station.bean.system.DistributeTemplateBean;
import cn.guanmai.station.bean.system.OrderImportTemlateBean;
import cn.guanmai.station.bean.system.PrintTagTemplateBean;
import cn.guanmai.station.bean.system.PurchaseTemplateBean;
import cn.guanmai.station.bean.system.SettleTemplateBean;
import cn.guanmai.station.bean.system.StockInTemplateBean;
import cn.guanmai.station.bean.system.StockOutTemplateBean;
import cn.guanmai.station.bean.system.param.OrderImportTemplateParam;

/* 
* @author liming 
* @date Jun 20, 2019 7:21:26 PM 
* @des 打印模板相关接口
* @version 1.0 
*/
public interface TemplateService {
	/**
	 * 获取采购模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PurchaseTemplateBean> getPurchaseTemplateList() throws Exception;

	/**
	 * 获取入库模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<StockInTemplateBean> getStockInTemplateList() throws Exception;

	/**
	 * 获取出库模板接口
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<StockOutTemplateBean> getStockOutTemplateList() throws Exception;

	/**
	 * 获取分拣标签列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<PrintTagTemplateBean> getPrintTagTemplateList() throws Exception;

	/**
	 * 获取装箱标签
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<BoxTemplateBean> getBoxTemplateList() throws Exception;

	/**
	 * 获取采购模板详细
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PurchaseTemplateBean.Content getPurchaseTemplateDetail(String id) throws Exception;

	/**
	 * 获取入库单模板
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public StockInTemplateBean.Content getStockInTemplateDetail(String id) throws Exception;

	/**
	 * 获取出库单模板详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public StockOutTemplateBean.Content getStockOutTemplateDetail(String id) throws Exception;

	/**
	 * 获取打印标签模板
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PrintTagTemplateBean.Content getPrintTagTemplateDetail(String id) throws Exception;

	/**
	 * 获取装箱标签详情
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BoxTemplateBean getBoxTemplateDetail(String id) throws Exception;

	/**
	 * 新建批量导入订单模板
	 * 
	 * @param orderImportTemplateParam
	 * @return
	 * @throws Exception
	 */
	public String createOrderImportTemplate(OrderImportTemplateParam orderImportTemplateParam) throws Exception;

	/**
	 * 获取批量导入订单模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<OrderImportTemlateBean> getOrderImportTemlateList() throws Exception;

	/**
	 * 获取指定导入订单模板详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public OrderImportTemlateBean getOrderImportTemlateDetail(String id) throws Exception;

	/**
	 * 获取配送模板列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<DistributeTemplateBean> getDistributeTemplateList() throws Exception;

	/**
	 * 获取配送模板详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public DistributeTemplateBean getDistributeTemplateDetailInfo(String id) throws Exception;

	/**
	 * 获取结款单模板
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SettleTemplateBean> getSettleTemplateList() throws Exception;

	/**
	 * 获取结款单模板详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public SettleTemplateBean.Content getSettleTemplateDetailInfo(String id) throws Exception;

}
