package cn.guanmai.bshop.bean.invoicing;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yangjinhai on 2019/8/19.
 */
public class BatchBean {

    List<BatchBeanList> batchBeanLists;

    public List<BatchBeanList> getBatchBeanLists() {
        return batchBeanLists;
    }

    public void setBatchBeanLists(List<BatchBeanList> batchBeanLists) {
        this.batchBeanLists = batchBeanLists;
    }

  public class BatchBeanList
  {
      String spu_id;
      String order_id;
      String sku_id;
      BigDecimal std_quantity;
      BigDecimal std_unit_price;

      public BatchBeanList()
      {

      }
      public BatchBeanList(String order_id, String sku_id, BigDecimal std_quantity, BigDecimal std_unit_price) {
          this.order_id = order_id;
          this.sku_id = sku_id;
          this.std_quantity = std_quantity;
          this.std_unit_price = std_unit_price;
      }


      public String getSpu_id() {
          return spu_id;
      }

      public void setSpu_id(String spu_id) {
          this.spu_id = spu_id;
      }

      public String getOrder_id() {
          return order_id;
      }

      public void setOrder_id(String order_id) {
          this.order_id = order_id;
      }

      public String getSku_id() {
          return sku_id;
      }

      public void setSku_id(String sku_id) {
          this.sku_id = sku_id;
      }

      public BigDecimal getStd_quantity() {
          return std_quantity;
      }

      public void setStd_quantity(BigDecimal std_quantity) {
          this.std_quantity = std_quantity;
      }

      public BigDecimal getStd_unit_price() {
          return std_unit_price;
      }

      public void setStd_unit_price(BigDecimal std_unit_price) {
          this.std_unit_price = std_unit_price;
      }
  }
}
