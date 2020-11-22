package cn.guanmai.open.url;

import cn.guanmai.util.ConfigureUtil;

public class OpenProductURL {

	private static final String openUrl = ConfigureUtil.getValueByKey("openUrl");

	// 获取报价单列表
	public static final String salemenu_list_url = openUrl + "/product/salemenu/list/1.0";

	// 新建报价单
	public static final String salemenu_create_url = openUrl + "/product/salemenu/create/1.0";

	// 修改报价单
	public static final String salemenu_update_url = openUrl + "/product/salemenu/update/1.0";

	// 获取一级分类列表
	public static final String category1_list_url = openUrl + "/product/category1/list/1.0";

	// 新建一级分类
	public static final String category1_create_url = openUrl + "/product/category1/create/1.0";

	// 修改一级分类
	public static final String category1_update_url = openUrl + "/product/category1/update/1.0";

	// 删除一级分类
	public static final String category1_delete_url = openUrl + "/product/category1/delete/1.0";

	// 获取二级分类列表
	public static final String category2_list_url = openUrl + "/product/category2/list/1.0";

	// 新建二级分类
	public static final String category2_create_url = openUrl + "/product/category2/create/1.0";

	// 修改二级分类
	public static final String category2_update_url = openUrl + "/product/category2/update/1.0";

	// 删除二级分类
	public static final String category2_delete_url = openUrl + "/product/category2/delete/1.0";

	// 获取品类列表
	public static final String pinlei_list_url = openUrl + "/product/pinlei/list/1.0";

	// 新建品类
	public static final String pinlei_create_url = openUrl + "/product/pinlei/create/1.0";

	// 修改品类
	public static final String pinlei_update_url = openUrl + "/product/pinlei/update/1.0";

	// 删除品类
	public static final String pinlei_delete_url = openUrl + "/product/pinlei/delete/1.0";

	// 获取SPU列表
	public static final String spu_list_url = openUrl + "/product/spu/list/1.0";

	// 获取指定SPU
	public static final String spu_detail_url = openUrl + "/product/spu/get/1.0";

	// 新建SPU接口
	public static final String spu_create_url = openUrl + "/product/spu/create/1.0";

	// 修改SPU接口
	public static final String spu_update_url = openUrl + "/product/spu/update/1.0";

	// 删除SPU接口
	public static final String spu_delete_url = openUrl + "/product/spu/delete/1.0";

	// 获取SPU基本单位列表
	public static final String spu_std_unit_name_List_url = openUrl + "/product/unit_name/list/1.0";

	// 新建销售SKU接口
	public static final String sale_sku_create_url = openUrl + "/product/sku/create/1.0";

	// 获取销售SKU详情接口
	public static final String sale_sku_detail_url = openUrl + "/product/sku/get/1.0";

	// 修改销售SKU接口
	public static final String sale_sku_update_url = openUrl + "/product/sku/update/1.0";

	// 删除销售SKU接口
	public static final String sale_sku_delete_url = openUrl + "/product/sku/delete/1.0";

	// 搜索过滤销售SKU接口
	public static final String sale_sku_search_url = openUrl + "/product/sku/list/1.0";

	// 获取运营时间收货接口
	public static final String receive_time_url = openUrl + "/product/receive_time/get/1.0";

	// 获取运营时间列表接口
	public static final String service_time_list_url = openUrl + "/product/station_service_time/list/1.0";

	// 搜索过滤采购规格列表
	public static final String query_purchase_spec_url = openUrl + "/purchase/spec/list/1.0";

	// 新建采购规格
	public static final String purchase_spec_craeta_url = openUrl + "/purchase/spec/create/1.0";

	// 修改采购规格
	public static final String purchase_spec_update_url = openUrl + "/purchase/spec/update/1.0";

	// 删除采购规格
	public static final String purchase_spec_delete_url = openUrl + "/purchase/spec/delete/1.0";

	// 采购规格询价
	public static final String purchase_spec_quote_price_edit_url = openUrl + "/purchase/spec/quote_price/edit/1.0";

}
