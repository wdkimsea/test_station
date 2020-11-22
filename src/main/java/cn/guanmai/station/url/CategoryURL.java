package cn.guanmai.station.url;

import cn.guanmai.util.ConfigureUtil;

/* 
* @author liming 
* @date Oct 31, 2018 3:20:03 PM 
* @des 商品分类接口
* @version 1.0 
*/
public class CategoryURL {
	private static final String stationUrl = ConfigureUtil.getValueByKey("stationUrl");

	// 站点SPU结果树接口
	public static final String merchandise_tree_url = stationUrl + "/merchandise/get_tree";

	// 创建一级分类接口
	public static final String create_category1_url = stationUrl + "/merchandise/category1/create";

	// 获取一级分类列表接口
	public static final String caterory1_list_url = stationUrl + "/merchandise/category1/get";

	// 删除一级分类接口
	public static final String delete_caterory1_url = stationUrl + "/merchandise/category1/delete";

	// 获取一级分类icon接口
	public static final String get_caterory1_icon_url = stationUrl + "/merchandise/category1/icon";

	// 修改一级分类接口
	public static final String update_caterory1_url = stationUrl + "/merchandise/category1/update";

	// 创建二级分类接口
	public static final String create_category2_url = stationUrl + "/merchandise/category2/create";

	// 修改二级分类接口
	public static final String update_caterory2_url = stationUrl + "/merchandise/category2/update";

	// 获取二级分类列表接口
	public static final String caterory2_list_url = stationUrl + "/merchandise/category2/get";

	// 删除二级分类接口
	public static final String delete_caterory2_url = stationUrl + "/merchandise/category2/delete";

	// 新建品类接口
	public static final String create_pinlei_url = stationUrl + "/merchandise/pinlei/create";

	// 获取品类列表接口
	public static final String pinlei_list_url = stationUrl + "/merchandise/pinlei/get";

	// 更新品类接口
	public static final String update_pinlei_url = stationUrl + "/merchandise/pinlei/update";

	// 删除品类接口
	public static final String delete_pinlei_url = stationUrl + "/merchandise/pinlei/delete";

	// 导出商品分类接口
	public static final String export_merchandise_url = stationUrl + "/merchandise/spu/export";

	// 新建SPU商品接口
	public static final String create_spu_url = stationUrl + "/merchandise/spu/create";

	// 获取SPU商品列表接口
	public static final String spu_list_url = stationUrl + "/merchandise/spu/list";

	// 商品库列表接口
	public static final String spu_index_url = stationUrl + "/merchandise/spu/index";

	// 商户库商品列表导出接口
	public static final String index_sku_export_url = stationUrl + "/product/sku/export";

	// 商品库导入修改销售SKU
	public static final String index_sku_import_update_url = stationUrl + "/product/sku/batch_import_update/async";

	// 商品库导入批量新建接口
	public static final String import_create_merchandise_url = stationUrl + "/merchandise/batch_create/import";

	// 过滤获取SPU列表接口
	public static final String spu_branch_url = stationUrl + "/merchandise/spu/branch";

	// 获取SPU信息接口
	public static final String spu_info_url = stationUrl + "/merchandise/spu/details";

	// 更新SPU商品接口
	public static final String update_spu_url = stationUrl + "/merchandise/spu/update";

	// 删除SPU商品接口
	public static final String delete_spu_url = stationUrl + "/merchandise/spu/delete";

	// SPU搜索
	public static final String merchandise_spu_simple_search_url = stationUrl + "/merchandise/spu/simple_search";

	// 批量删除SPU商品接口
	public static final String batch_delete_spu_url = stationUrl + "/merchandise/spu/batch_delete";

	// 批量SKU对应的详情信息接口
	public static final String batch_sku_details_url = stationUrl + "/product/batchsku/details";

	// 上传SPU商品图片接口
	public static final String upload_spu_image_url = stationUrl + "/image/upload";

	// 创建商品采购规格接口
	public static final String create_purchase_spec_url = stationUrl + "/purchase_spec/create";

	// 修改商品采购规格接口
	public static final String update_purchase_spec_url = stationUrl + "/purchase_spec/update";

	// 删除商品采购规格接口
	public static final String delete_purchase_spec_url = stationUrl + "/purchase_spec/delete";

	// 查询商品采购规格接口
	public static final String search_purchase_spec_url = stationUrl + "/purchase_spec/search";

	// 导出采购规格接口
	public static final String export_purchase_spec_url = stationUrl + "/purchase_spec/export";

	// 采购规格询价导入接口
	public static final String import_purchase_spec_quote_price_url = stationUrl + "/purchase/quote_price/import";

	// 采购规格批量导入修改接口
	public static final String import_batch_edit_purchase_spec_url = stationUrl + "/purchase_spec/batch/import";

	// 销售SKU详细页面拉取对应所有的采购规格接口
	public static final String purchase_spec_array_in_sku = stationUrl + "/product/sku_spec/list";

	// 获取销售对象站点列表接口
	public static final String target_list_url = stationUrl + "/salemenu/sale/targets";

	// 创建销售SKU接口
	public static final String create_sku_url = stationUrl + "/product/sku_sale/create";

	// 批量新建销售SKU接口
	public static final String batch_create_sku_url = stationUrl + "/product/batchsku/create";

	// 删除销售SKU接口
	public static final String delete_sku_url = stationUrl + "/product/sku/delete";

	// 批量删除销售SKU接口
	public static final String batch_delete_sku_url = stationUrl + "/product/sku/batch_delete";

	// 修改销售SKU接口
	public static final String update_sku_url = stationUrl + "/product/sku/update";

	// 获取指定SPU下的SKU列表
	public static final String sku_list_url = stationUrl + "/product/sku_sale/list";

	// 修改销售SKU的销售状态接口
	public static final String update_sku_status_url = stationUrl + "/product/sku/update";

	// 报价单里搜索销售销售SKU接口
	public static final String search_sku_in_salemenu_url = stationUrl + "/product/sku_salemenu/list";

	// 根据SPU获取供应商列表[推荐 and 其他]
	public static final String get_sku_supplier_list_url = stationUrl + "/product/sku_supplier/list_new";

	// 销售SKU搜索接口
	public static final String search_sku_url = stationUrl + "/station/skus";

	// 单位关系对照接口
	public static final String sku_measurement_list = stationUrl + "/product/sku/measurement/list";

	// 获取销售规格对应的所有供应商列表接口
	public static final String sku_supplier_list_url = stationUrl + "/product/sku_supplier/list";

	// 报价单里商品导入批量新建接口
	public static final String import_create_sku_url = stationUrl + "/product/sku/import";

	// 报价单里商品导入批量修改接口
	public static final String import_update_sku_url = stationUrl + "/station/skuproducts/import";

	// 商品备注搜索商户列表接口
	public static final String spu_remark_customer_search_url = stationUrl + "/station/spu_remark/customer_search/";

	// 获取指定商户所见所有SPU接口
	public static final String spu_remark_spu_search_url = stationUrl + "/station/spu_remark/spu_search/";

	// 设置商品备注接口
	public static final String update_spu_remark_url = stationUrl + "/station/spu_remark/update_remark/";

	// 获取税率列表接口
	public static final String get_tax_rule_list_url = stationUrl + "/station/tax/tax_rule/list";

	// 新建税率规则搜索商户接口
	public static final String search_tax_customer_url = stationUrl + "/station/tax/customer/search";

	// 新建税率规则搜索商品接口
	public static final String search_tax_spu_url = stationUrl + "/station/tax/spu/search";

	// 获取税率规则详细信息接口
	public static final String get_tax_rule_url = stationUrl + "/station/tax/tax_rule/get";

	// 新建税率规格接口
	public static final String create_tax_rule_url = stationUrl + "/station/tax/tax_rule/create";

	// 修改税率规则接口
	public static final String edit_tax_rule_url = stationUrl + "/station/tax/tax_rule/edit";

	// 税率规则,按商品查看
	public static final String tax_spu_list_url = stationUrl + "/station/tax/spu/list";

	// 税率根据商户标签查询商户的接口
	public static final String tax_label_address_url = stationUrl + "/station/tax/label/address/list";

	// 新建组合商品接口
	public static final String create_combine_goods_url = stationUrl + "/combine_goods/create";

	// 修改组合商品接口
	public static final String edit_combine_goods_url = stationUrl + "/combine_goods/edit";

	// 修改组合商品状态接口
	public static final String edit_combine_goods_state_url = stationUrl + "/combine_goods/edit_state";

	// 批量修改组合商品
	public static final String batch_edit_combine_goods_url = stationUrl + "/combine_goods/batch_edit";

	// 删除组合商品接口
	public static final String delete_combine_goods_url = stationUrl + "/combine_goods/delete";

	// 获取组合商品详情接口
	public static final String get_combine_goods_detail_url = stationUrl + "/combine_goods/detail";

	// 搜索过滤组合商品接口
	public static final String search_combine_goods_url = stationUrl + "/combine_goods/list";

	// 组合商品批量修改搜索商品
	public static final String batch_search_combine_goods_url = stationUrl + "/combine_goods/batch_search";

	// 导出组合商品
	public static final String export_combine_goods_url = stationUrl + "/combine_goods/export";

	// 营销活动拉取组合商品列表
	public static final String promotion_combine_goods_url = stationUrl + "/station/promotion/combine_goods/list";

	/*************************** 智能菜单相关接口 ***************************/
	// 创建智能菜单
	public static final String create_smart_menu_url = stationUrl + "/station/smart_menu/create";

	// 编辑智能菜单
	public static final String edit_samrt_menu_url = stationUrl + "/station/smart_menu/edit";

	// 智能菜单详情
	public static final String smart_menu_detail_url = stationUrl + "/station/smart_menu/detail";

	// 智能菜单列表接口
	public static final String smart_menu_list_url = stationUrl + "/station/smart_menu/list";

	// 删除智能采购接口
	public static final String delete_smart_meun_url = stationUrl + "/station/smart_menu/delete";

	// 打印智能菜单
	public static final String print_smart_menu_url = stationUrl + "/station/smart_menu/print";
}
