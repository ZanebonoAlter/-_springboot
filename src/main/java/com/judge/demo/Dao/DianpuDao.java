package com.judge.demo.Dao;

import com.judge.demo.Entity.AliDianpu;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface DianpuDao {

    @Insert("insert ignore into ali_dianpu(id,buy_ni,sell_ni,money,title,num,buy_name,buy_phone,buy_shen,buy_city,buy_region,buy_addres,c_date,index) " +
            "values(#{id},#{buy_ni},#{sell_ni},#{money},#{title},#{num},#{buy_name},#{buy_phone},#{buy_shen},#{buy_city},#{buy_addres},#{c_date},#{index})")
    int addrecord(AliDianpu aliDianpu);
}
