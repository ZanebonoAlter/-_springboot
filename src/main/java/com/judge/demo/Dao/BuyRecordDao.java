package com.judge.demo.Dao;

import com.judge.demo.Entity.BuyRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BuyRecordDao {

    @Insert("insert ignore into buy_record(buy_record_order_id,buy_record_order_fee,buy_record_buyer_account,buy_record_seller_account,buy_record_goods_title" +
            ",buy_record_goods_price,buy_record_goods_num,buy_record_address,buy_record_status,buy_record_time,buy_record_receiver_name,buy_record_receiver_phone,buy_record_index) " +
            "values(#{buyRecordOrderId},#{buyRecordOrderFee},#{buyRecordBuyerAccount},#{buyRecordSellerAccount},#{buyRecordGoodsTitle},#{buyRecordGoodsPrice},#{buyRecordGoodsNum},#{buyRecordAddress}" +
            ",#{buyRecordStatus},#{buyRecordTime},#{buyRecordReceiverName},#{buyRecordReceiverPhone},#{buyRecordIndex})")
    @Options(useGeneratedKeys=true, keyProperty="buyRecordId", keyColumn="buy_record_id")
    int addBuyRecord(BuyRecord buyRecord);

    @Select("select * from buy_record where buy_record_id in #{Ids} limit #{pageIndex},#{pageSize}")
    List<BuyRecord> selectBuyRecordByIds(@Param("Ids") List<Long> Ids,@Param("pageIndex")Integer pageIndex,@Param("PageSize")Integer pageSize);

    @Select("select * from buy_record limit #{pageIndex},#{pageSize}")
    List<BuyRecord> selectAllBuyRecord(@Param("pageIndex")Integer pageIndex,@Param("PageSize")Integer pageSize);

    @Select("select count(*) from buy_record")
    Integer selectAllBuyRecordCount();

    @Select("select count(*) from buy_record where buy_record_id in #{Ids}")
    Integer selectBuyRecordCountByIds(@Param("Ids")List<Long> Ids);

    @Select("select Distinct buy_record_receiver_phone from buy_record where buy_record_receiver_name=#{name}")
    List<String> selecrPhoneByName(@Param("name")String name);

    @Select("select Distinct buy_record_receiver_name from buy_record")
    List<String> selectNameList();

    @Select("select count(*) from buy_record")
    Integer currentCount();
}
