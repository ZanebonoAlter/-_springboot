package com.judge.demo.Dao;

import com.judge.demo.Entity.TransferRecord;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Mapper
@Component
public interface TransferRecordDao {

    @Insert("insert ignore into transfer_record(transfer_record_flow_id,transfer_record_create_time,transfer_record_pay_time,transfer_record_pay_fee" +
            ",transfer_record_pay_zhifubao,transfer_record_pay_zhifubao_id,transfer_record_pay_name,transfer_record_collection_zhifubao,transfer_record_collection_zhifubao_id" +
            ",transfer_record_collection_name,transfer_record_remark,transfer_record_flow,transfer_record_status,transfer_record_product_name,transfer_record_index) values(" +
            "#{transferRecordFlowId},#{transferRecordCreateTime},#{transferRecordPayTime},#{transferRecordPayFee},#{transferRecordPayZhifubao}" +
            ",#{transferRecordPayZhifubaoId},#{transferRecordPayName},#{transferRecordCollectionZhifubao},#{transferRecordCollectionZhifubaoId}" +
            ",#{transferRecordCollectionName},#{transferRecordRemark},#{transferRecordFlow},#{transferRecordStatus},#{transferRecordProductName}" +
            ",#{transferRecordIndex})")
    @Options(useGeneratedKeys=true, keyProperty="transferRecordId", keyColumn="transfer_record_id")
    int addTransferRecord(TransferRecord record);

    @Select("select  distinct transfer_record_pay_name from transfer_record")
    List<String> selectTransferPayNameList();
    @Select("select distinct transfer_record_collection_name from transfer_record")
    List<String> selectTransferCollectionNameList();

    @Select("select transfer_record_pay_name from transfer_record where transfer_record_collection_name=#{name}")
    List<String> selectPayNameByCollectionName(@Param("name")String name);
    @Select("select transfer_record_collection_name from transfer_record where transfer_record_pay_name=#{name}")
    List<String> selectCollectionameByPayName(@Param("name")String name);

    //@Select("select * from transfer_record where transfer_record_id in (#{Ids}) limit #{pageIndex},#{pageSize}")
    @SelectProvider(type = TransferRecord_Dao.class,method = "selectTransferRecordByIds")
    @Results(id="transferBase",value = {
            @Result(column = "transfer_record_id",property = "transferRecordId",javaType = Long.class),
            @Result(column = "transfer_record_flow_id",property = "transferRecordFlowId",javaType = String.class),
            @Result(column = "transfer_record_create_time",property = "transferRecordCreateTime",javaType = Date.class),
            @Result(column = "transfer_record_pay_time",property = "transferRecordPayTime",javaType = Date.class),
            @Result(column = "transfer_record_pay_fee",property = "transferRecordPayFee",javaType = Double.class),
            @Result(column = "transfer_record_pay_zhifubao",property = "transferRecordPayZhifubao",javaType = String.class),
            @Result(column = "transfer_record_pay_zhifubao_id",property = "transferRecordPayZhifubaoId",javaType = String.class),
            @Result(column = "transfer_record_pay_name",property = "transferRecordPayName",javaType = String.class),
            @Result(column = "transfer_record_collection_zhifubao",property = "transferRecordCollectionZhifubao",javaType = String.class),
            @Result(column = "transfer_record_collection_zhifubao_id",property = "transferRecordCollectionZhifubaoId",javaType = String.class),
            @Result(column = "transfer_record_collection_name",property = "transferRecordCollectionName",javaType = String.class),
            @Result(column = "transfer_record_remark",property = "transferRecordRemark",javaType = String.class),
            @Result(column = "transfer_record_flow",property = "transferRecordFlow",javaType = String.class),
            @Result(column = "transfer_record_status",property = "transferRecordStatus",javaType = String.class),
            @Result(column = "transfer_record_product_name",property = "transferRecordProductName",javaType = String.class),
            @Result(column = "transfer_record_index",property = "transferRecordIndex",javaType = String.class),
    })
    List<TransferRecord> selectTransferRecordByIds(@Param("Ids")List<Long> Ids,@Param("pageIndex")Integer pageIndex,@Param("pageSize")Integer pageSize);

    @Select("select * from transfer_record limit #{pageIndex},#{pageSize}")
    @ResultMap("transferBase")
    List<TransferRecord> selectAllTransferRecord(@Param("pageIndex")Integer pageIndex,@Param("pageSize")Integer pageSize);

    @Select("select count(*) from transfer_record")
    Integer selectAllTransferRecordCount();

    @Select("select count(*) from transfer_record where transfer_record_id in (#{Ids})")
    Integer selectTransferRecordCountByIds(@Param("Ids")List<Long> Ids);

    @Select("select count(*) from transfer_record where transfer_record_pay_time between #{beginDate} and #{endDate}")
    Integer selectAllTransferRecordCountByDate(@Param("beginDate")Date beginDate,@Param("endDate")Date endDate);

    @Select("select count(*) from transfer_record where transfer_record_pay_fee between #{minFee} and #{maxFee}")
    Integer getExceptRecordCount(@Param("minFee")Double minFee,@Param("maxFee")Double maxFee);
}
class TransferRecord_Dao{
    public String selectTransferRecordByIds(@Param("Ids")List<Long> Ids,@Param("pageIndex")Integer pageIndex,@Param("pageSize")Integer pageSize){
        return new SQL(){
            {
                SELECT("*");
                FROM("transfer_record");
                StringBuffer temp = new StringBuffer();
                temp.append("transfer_record_id in (");
                for (Long id:
                     Ids) {
                    temp.append(id+",");
                }
                temp.deleteCharAt(temp.length()-1);
                temp.append(") limit #{pageIndex},#{pageSize}");
                String where = temp.toString();
                WHERE(where);
            }
            }.toString();
        }
}