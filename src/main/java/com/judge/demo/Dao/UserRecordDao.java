package com.judge.demo.Dao;

import com.judge.demo.Entity.UserRecord;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserRecordDao {

    @Select("select * from user_record where user_account=#{account} and user_password=#{password}")
    List<UserRecord> getUserBySelect(@Param("account") String account,@Param("password") String password);
}
