package cn.guanmai.bshop.url;

import cn.guanmai.util.ConfigureUtil;

/**
 * 
 * @author Administrator
 * @des 商城进销存相关接口
 */
public class BsInvoigingURL {

	public static final String bshopUrl = ConfigureUtil.getValueByKey("bshopUrl");

	// 商城进销存近7天统计数据
	public static final String stock_spu_count_url = bshopUrl + "/stock/spu/count";

	// 商城进销存获取spu统计数据展示
	public static final String spu_stock_list = bshopUrl + "/stock/spu/list";

	// 商城进销存获取sku统计数据
	public static final String stock_sku_count = bshopUrl + "/stock/sku/wait_in_stock/count";

	// 商城进销存获取sku列表数据
	public static final String stock_sku_list = bshopUrl + "/stock/sku/wait_in_stock/list";

	// 商城进销存添加入库数据
	public static final String spu_in_stock_create_url = bshopUrl + "/stock/spu/in_stock/create";

	// 商城进销存进行出库操作
	public static final String create_spu_stock_output_url = bshopUrl + "/stock/spu/out_stock/create";

	// 查询出入库明细
	public static final String query_stock_details_spu = bshopUrl + "/stock/spu/stock/change_log/list";

	// 选定商户的商品列表
	public static final String address_spu_stock_list_url = bshopUrl + "/stock/address/spu/list";

}
