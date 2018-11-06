package com.judge.demo.Service.ServiceImpl;

import com.judge.demo.Dao.UserRecordDao;
import com.judge.demo.Service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserRecordDao userRecordDao;

    @Override
    public Boolean Login(String account, String password) {
        if(userRecordDao.getUserBySelect(account,password).isEmpty())
            return false;
        return true;
    }
}
