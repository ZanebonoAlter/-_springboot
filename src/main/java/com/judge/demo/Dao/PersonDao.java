package com.judge.demo.Dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface PersonDao {

    @Select("select count(*) from person_record where p_name=#{name}")
    Integer isSearch(@Param("name")String name);

    @Insert("insert into person_record(p_name) values(#{name})")
    Integer addName(@Param("name")String name);

    @Delete("delete from person_record where p_name=#{name}")
    void  deleteName(@Param("name")String name);
}
