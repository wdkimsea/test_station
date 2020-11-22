package cn.guanmai.station.interfaces.weight;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

import cn.guanmai.station.bean.share.OrderAndSkuBean;
import cn.guanmai.station.bean.weight.CategoryTree;
import cn.guanmai.station.bean.weight.PdaOrderBean;
import cn.guanmai.station.bean.weight.PdaOrderDetailBean;
import cn.guanmai.station.bean.weight.PdaPatckageInfoBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuBean;
import cn.guanmai.station.bean.weight.PdaWeightSkuDetailBean;
import cn.guanmai.station.bean.weight.PdaWeightSortDetailBean;
import cn.guanmai.station.bean.weight.PreSortingSkuBean;
import cn.guanmai.station.bean.weight.PreSortingSkuPackageBean;
import cn.guanmai.station.bean.weight.WeightBasketBean;
import cn.guanmai.station.bean.weight.WeightCategoryTreeBean;
import cn.guanmai.station.bean.weight.WeightCollectInfoBean;
import cn.guanmai.station.bean.weight.WeightCollectOrderBean;
import cn.guanmai.station.bean.weight.WeightCollectSkuBean;
import cn.guanmai.station.bean.weight.WeightGroupBean;
import cn.guanmai.station.bean.weight.WeightSkuBean;
import cn.guanmai.station.bean.weight.WeightTag;
import cn.guanmai.station.bean.weight.param.BatchOutOfStockParam;
import cn.guanmai.station.bean.weight.param.ChecklistParam;
import cn.guanmai.station.bean.weight.param.DiffOrderWeighParam;
import cn.guanmai.station.bean.weight.param.OldSetWeighParam;
import cn.guanmai.station.bean.weight.param.OutOfStockParam;
import cn.guanmai.station.bean.weight.param.PackWeighDataParam;
import cn.guanmai.station.bean.weight.param.PdaOrderDetailParam;
import cn.guanmai.station.bean.weight.param.PdaOrderFilterParam;
import cn.guanmai.station.bean.weight.param.PdaOutOfStockParam;
import cn.guanmai.station.bean.weight.param.PdaSetWeightParam;
import cn.guanmai.station.bean.weight.param.PdaWeightSkuDetailFilterParam;
import cn.guanmai.station.bean.weight.param.PrintInfoBean;
import cn.guanmai.station.bean.weight.param.SetWeightParam;
import cn.guanmai.station.bean.weight.param.UnionDispatchParam;
import cn.guanmai.station.bean.weight.param.WeighAllDataParam;
import cn.guanmai.station.bean.weight.param.WeighTaskParam;
import cn.guanmai.station.bean.weight.param.WeightCollectOrderFilterParam;
import cn.guanmai.station.bean.weight.param.WeightCollectSkuFilterParam;
import cn.guanmai.station.bean.weight.param.WeightDataFilterParam;

/* 
* @author liming 
* @date Jan 8, 2019 11:49:24 AM 
* @des 称重相关接口
* @version 1.0 
*/
public interface WeightService {

	/**
	 * 新版本称重软件-设置称重员工工号
	 * 
	 * @param employee_name
	 * @return
	 * @throws Exception
	 */
	public boolean setEmployee(String employee_name) throws Exception;

	/**
	 * 新版称重软件-获取称重框列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<WeightBasketBean> getWeightBaskets() throws Exception;

	/**
	 * 新版称重软件-新建称重框
	 * 
	 * @param weightBasket
	 * @return
	 * @throws Exception
	 */
	public boolean createWeightBasket(WeightBasketBean weightBasket) throws Exception;

	/**
	 * 删除称重框
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteWeightBasket(String id) throws Exception;

	/**
	 * 修改称重框
	 * 
	 * @param weightBasket
	 * @return
	 * @throws Exception
	 */
	public boolean updateWeightBasket(WeightBasketBean weightBasket) throws Exception;

	/**
	 * 新版本称重软件-拉取称重数据
	 * 
	 * @param time_config_id
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<WeightSkuBean> getWeightSkus(String time_config_id, String date) throws Exception;

	/**
	 * 新版本称重软件-拉取称重数据
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<WeightSkuBean> getWeightSkus(WeightDataFilterParam filterParam) throws Exception;

	/**
	 * 新版本称重软件-拉取称重订单号
	 * 
	 * @param time_config_id
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<String> getWeightOrders(String time_config_id, String date) throws Exception;

	/**
	 * 分拣任务-分拣明细-批量缺货上报 方式一
	 * 
	 * @param orderSkuList
	 * @return
	 * @throws Exception
	 */
	public boolean batchOutOfStock(List<OrderAndSkuBean> orderSkuList) throws Exception;

	/**
	 * 分拣任务-分拣明细-批量缺货上报 方式二
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean batchOutOfStock(BatchOutOfStockParam paramBean) throws Exception;

	/**
	 * 分拣任务-分拣明细-批量缺货上报 方式三
	 * 
	 * @param weightCollectSkuFilterParam
	 * @return
	 * @throws Exception
	 */
	public boolean batchOutOfStock(WeightCollectSkuFilterParam weightCollectSkuFilterParam) throws Exception;

	/**
	 * 新版称重软件-缺货上报
	 * 
	 * @param outOfStockList
	 * @return
	 * @throws Exception
	 */
	public boolean outOfStock(List<OutOfStockParam> outOfStockList) throws Exception;

	/**
	 * 新版称重软件-取消称重操作
	 * 
	 * @param orderSkuList
	 * @return
	 * @throws Exception
	 */
	public boolean unOutOfStock(List<OutOfStockParam> outOfStockList) throws Exception;

	/**
	 * 新版称重软件-称重
	 * 
	 * @param setWeightParam
	 * @return
	 * @throws Exception
	 */
	public boolean setWeight(SetWeightParam setWeightParam) throws Exception;

	/**
	 * 新版称重软件-获取打印标签信息
	 * 
	 * @param orderAndskuList
	 * @return
	 * @throws Exception
	 */
	public List<PrintInfoBean> getWeightSkuPrintInfo(List<OrderAndSkuBean> orderAndskuList) throws Exception;

	/**
	 * 新版称重软件-打印标签
	 * 
	 * @param printParam
	 * @return
	 * @throws Exception
	 */
	public boolean printSkuWeight(List<OrderAndSkuBean> printParam) throws Exception;

	/**
	 * 新版称重软件-创建称重分组
	 * 
	 * @param name
	 * @param spu_ids
	 * @return
	 * @throws Exception
	 */
	public String createWeightGroup(String name, List<String> spu_ids) throws Exception;

	/**
	 * 新版称重软件-计重商品分拣-分组列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<WeightGroupBean> getWeightGroupList() throws Exception;

	/**
	 * 新版称重软件-获取称重分组的详细信息
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public WeightGroupBean getWeightGroupDetail(String id) throws Exception;

	/**
	 * 新版称重软件-删除称重分组
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteWeightGroup(String id) throws Exception;

	/**
	 * 新版称重软件-修改称重分组
	 * 
	 * @param group_id
	 * @param spu_ids
	 * @return
	 * @throws Exception
	 */
	public boolean updateWeightGroup(String group_id, List<String> spu_ids) throws Exception;

	/**
	 * 新版称重软件-计重商品分拣-拉取称重商品结果树
	 * 
	 * @param date
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public List<WeightCategoryTreeBean> getWeightCategoryTree(String time_config_id, String date, boolean is_weight)
			throws Exception;

	/**
	 * 新版称重软件-未分组的商品树
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<WeightCategoryTreeBean> getWeightCategoryUngroupTree() throws Exception;

	/**
	 * 一步称重完指定订单中的商品
	 * 
	 * @param order_id
	 * @return
	 * @throws Exception
	 */
	public boolean oneStepWeightOrder(String order_id) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 分拣单核查
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean getWeighChecklist(ChecklistParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 统配称重快速打印
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean unionDispath(UnionDispatchParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 获取所有的一二级分类 [对应的接口: /station/weigh/what_can_i_do]
	 * 
	 * @param station_id
	 * @param time_config_id
	 * @param cycle_start_time
	 * @return
	 * @throws Exception
	 */
	public List<CategoryTree> getCategoryTree(String station_id, String time_config_id, String cycle_start_time)
			throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 根据二级分类ID获取SPU集合
	 * 
	 * @param station_id
	 * @param time_config_id
	 * @param cycle_start_time
	 * @param category2_ids
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getSpuByCategory2(String station_id, String time_config_id, String cycle_start_time,
			JSONArray category2_ids) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 获取所有的称重数据
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean getWeighAllData(WeighAllDataParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 差异对比
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean getDiffOrderWeight(DiffOrderWeighParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 获取称重任务
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public List<WeightTag> getWeighTask(WeighTaskParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 生成称重数据
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean packWeighData(PackWeighDataParam paramBean) throws Exception;

	/**
	 * 老版本的称重软件(PC称重) 称重接口
	 * 
	 * @param paramBean
	 * @return
	 * @throws Exception
	 */
	public boolean setWeight(OldSetWeighParam paramBean) throws Exception;

	/**
	 * ST-供应链-分拣-分拣任务-分拣进度
	 * 
	 * @param time_config_id
	 * @param target_date
	 * @return
	 * @throws Exception
	 */
	public WeightCollectInfoBean getWeightCollectInfo(String time_config_id, String target_date) throws Exception;

	/**
	 * ST-供应链-分拣-分拣进度
	 * 
	 * @param time_config_id
	 * @param target_date
	 * @param random_num
	 * @return
	 * @throws Exception
	 */
	public boolean getWeightCollectRandomOrderInfo(String time_config_id, String target_date, int random_num)
			throws Exception;

	/**
	 * ST-供应链-分拣-分拣进度-商品分拣进度
	 * 
	 * @param time_config_id
	 * @param target_date
	 * @param random_num
	 * @return
	 * @throws Exception
	 */
	public boolean getgetWeightCollectRandomSkuInfo(String time_config_id, String target_date, int random_num)
			throws Exception;

	/**
	 * ST-供应链-分拣-分拣明细-按订单分拣
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<WeightCollectOrderBean> getWeightCollectOrderInfo(WeightCollectOrderFilterParam filterParam) throws Exception;

	/**
	 * ST-供应链-分拣-分拣明细-按商品分拣
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<WeightCollectSkuBean> getWeightCollectSkuInfo(WeightCollectSkuFilterParam filterParam) throws Exception;

	/**
	 * ST-供应链-分拣-分拣任务-分拣明细-批量修改缺货
	 * 
	 * @param orderSkuList
	 * @return
	 * @throws Exception
	 */
	public boolean stationBatchOutOfStock(List<OrderAndSkuBean> orderSkuList) throws Exception;

	/**
	 * ST-供应链-分拣-分拣任务-分拣明细-更新出库数
	 * 
	 * @param setWeightParam
	 * @return
	 * @throws Exception
	 */
	public boolean stationSetWeight(SetWeightParam setWeightParam) throws Exception;

	/**********************************************************************************/
	/******************************* 以下是预分拣&PDA相关接口 ***************************/
	/**********************************************************************************/

	/**
	 * 获取预分拣sku
	 * 
	 * @param spu_ids
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
	public List<PreSortingSkuBean> getPreSortingSkuList(List<String> spu_ids, String start_date, String end_date)
			throws Exception;

	/**
	 * 获取要进行预分拣sku的包装详情
	 * 
	 * @param sku_id
	 * @param sku_ids
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
	public PreSortingSkuPackageBean getPreSortingSkuPackage(String sku_id, List<String> sku_ids, String start_date,
			String end_date) throws Exception;

	/**
	 * 预分拣/批量打印标签, 当count=1时,此参数可以不填
	 * 
	 * @param sku_id
	 * @param quantity
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public List<String> createWeightPackage(String sku_id, BigDecimal quantity, Integer count) throws Exception;

	/**
	 * 删除包装
	 * 
	 * @param package_id
	 * @return
	 * @throws Exception
	 */
	public boolean deleteWeightPackage(String package_id) throws Exception;

	/**
	 * 搜索包装编号
	 * 
	 * @param package_id
	 * @param spu_ids
	 * @param start_date
	 * @param end_date
	 * @return
	 * @throws Exception
	 */
	public PreSortingSkuPackageBean searchPreSortingSkuPackage(String package_id, List<String> spu_ids,
			String start_date, String end_date) throws Exception;

	/**
	 * Pda 包装编码查询
	 * 
	 * @param package_id
	 * @return
	 * @throws Exception
	 */
	public PdaPatckageInfoBean searchPackageInPda(String package_id) throws Exception;

	/**
	 * PDA 按商品拣货-商品列表
	 * 
	 * @param search_text
	 * @param date
	 * @param time_config_id
	 * @return
	 * @throws Exception
	 */
	public List<PdaWeightSkuBean> searchPdaWeightSkus(String search_text, String date, String time_config_id)
			throws Exception;

	/**
	 * PDA 按商品拣货-商品详情 分页
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public PdaWeightSkuDetailBean getPdaWeightSkuDetail(PdaWeightSkuDetailFilterParam filterParam) throws Exception;

	/**
	 * PDA 分拣详情页面
	 * 
	 * @param order_id
	 * @param sku_id
	 * @param detail_id
	 * @return
	 * @throws Exception
	 */
	public PdaWeightSortDetailBean getPdaWeightSortDetail(String order_id, String sku_id, BigDecimal detail_id)
			throws Exception;

	/**
	 * PDA 分拣称重
	 * 
	 * @param pdaSetWeightParam
	 * @return
	 * @throws Exception
	 */
	public boolean setWeightOfPda(PdaSetWeightParam pdaSetWeightParam) throws Exception;

	/**
	 * PDA 设置缺货
	 * 
	 * @param pdaOutOfStockParam
	 * @return
	 * @throws Exception
	 */
	public boolean outOfStockOfPda(PdaOutOfStockParam pdaOutOfStockParam) throws Exception;

	/**
	 * PDA 按订单分拣
	 * 
	 * @param filterParam
	 * @return
	 * @throws Exception
	 */
	public List<PdaOrderBean> searchPdaOrders(PdaOrderFilterParam filterParam) throws Exception;

	/**
	 * PDA 获取订单详细
	 * 
	 * @param pdaOrderDetailParam
	 * @return
	 * @throws Exception
	 */
	public PdaOrderDetailBean getPdaOrderDetailBean(PdaOrderDetailParam pdaOrderDetailParam) throws Exception;

}
