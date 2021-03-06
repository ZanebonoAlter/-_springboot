package com.judge.demo.Dao;

import com.judge.demo.Entity.BuyRecord;
import com.judge.demo.Entity.BuyRecordExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface BuyRecordMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int countByExample(BuyRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int deleteByExample(BuyRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int deleteByPrimaryKey(Long buyRecordId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int insert(BuyRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int insertSelective(BuyRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    List<BuyRecord> selectByExample(BuyRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    BuyRecord selectByPrimaryKey(Long buyRecordId);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByExampleSelective(@Param("record") BuyRecord record, @Param("example") BuyRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByExample(@Param("record") BuyRecord record, @Param("example") BuyRecordExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByPrimaryKeySelective(BuyRecord record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table buy_record
     *
     * @mbggenerated Wed Oct 24 02:08:34 CST 2018
     */
    int updateByPrimaryKey(BuyRecord record);
}