package com.judge.demo.Dao;

import com.judge.demo.Entity.TransferRecord;
import com.judge.demo.Entity.TransferRecordExample;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TransferRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int countByExample(TransferRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int deleteByExample(TransferRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int deleteByPrimaryKey(Long transferRecordId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int insert(TransferRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int insertSelective(TransferRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    List<TransferRecord> selectByExample(TransferRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    TransferRecord selectByPrimaryKey(Long transferRecordId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByExampleSelective(@Param("record") TransferRecord record, @Param("example") TransferRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByExample(@Param("record") TransferRecord record, @Param("example") TransferRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByPrimaryKeySelective(TransferRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table transfer_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByPrimaryKey(TransferRecord record);

    List<Map<String,Object>> selectiveGroupByPayName(@Param("fee") double fee);
    List<Map<String,Object>> selectiveGroupByCollectionName(@Param("fee") double fee);

    List<Map<String,Object>>  selectNameListGroupByPayName();

    List<Map<String,Object>>  selectNameListGroupByCollectionName();

    List<Map<String,String>> selectMutiZhifubaoWithName(@Param("pageIndex")Integer pageIndex,@Param("pageSize")Integer pageSize,@Param("name")String name);
    Integer selectMutiZhifubaoCountWithName(@Param("name")String name);

    Integer selectCountIn();
    Integer selectCountOut();

    Integer selectCountOutByName(@Param("name")String name);
    Integer selectCountInByName(@Param("name")String name);

   Set<String> selectRelativeNameList(@Param("names")String name);
   Set<String> getZhifubaoIdsByName(@Param("name")String name);
}