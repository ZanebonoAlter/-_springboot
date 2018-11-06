package com.judge.demo.Service.ServiceImpl;

import com.judge.demo.Dao.*;
import com.judge.demo.Entity.*;
import com.judge.demo.Service.PersonService;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
public class PersonServiceImpl implements PersonService {
    @Resource
    private PersonDao personDao;
    @Resource
    private TransferRecordDao transferRecordDao;
    @Resource
    private BuyRecordDao buyRecordDao;
    @Resource
    private PersonRecordMapper personRecordMapper;

    @Override
    public Map<String,String> selectZhifubaoByName(String name) {
        TransferRecordExample example = new TransferRecordExample();
        TransferRecordExample.Criteria criteria = example.createCriteria();
        criteria.andTransferRecordPayNameEqualTo(name);
        example.setDistinct(true);


        return null;
    }

    @Override
    public List<String> selectPhoneByName(String name) {
        return null;
    }

    @Override
    public Boolean isSearch(String name) {
        if(personDao.isSearch(name)!=0)
            return true;
        return false;
    }

    @Override
    public Integer addName(String name) {
        return  personDao.addName(name);
    }

    @Override
    public void deleteName(String name) {
            personDao.deleteName(name);
    }

    @Override
    public List<PersonRecord> selectAllKeyPerson() {
        PersonRecordExample example = new PersonRecordExample();
        return personRecordMapper.selectByExample(example);
    }

    @Override
    public Map<String, Integer> selectAllPerson(Integer pageIndex, Integer pageSize) {
        List<String> buy = buyRecordDao.selectNameList();
        List<String> pay = transferRecordDao.selectTransferPayNameList();
        List<String> collection = transferRecordDao.selectTransferCollectionNameList();
        List<String> result = new ArrayList<>();
        Map<String,Integer> map = new TreeMap<>();
        for (String s:buy){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:pay){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:collection){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (int i=pageIndex,j=0;i<result.size()&&j<pageSize;i++,j++){//分页
            String s = result.get(i);
            if (personDao.isSearch(s)!=0){//如果是重点
                map.put(s,1);
            }else {
                map.put(s,0);
            }
        }
        return map;
    }

    @Override
    public Integer selectAllPersonCount() {
        List<String> buy = buyRecordDao.selectNameList();
        List<String> pay = transferRecordDao.selectTransferPayNameList();
        List<String> collection = transferRecordDao.selectTransferCollectionNameList();
        List<String> result = new ArrayList<>();
        for (String s:buy){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:pay){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:collection){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        return result.size();
    }

    @Override
    public void updatePerson(PersonRecord personRecord) {
        personRecordMapper.updateByPrimaryKeySelective(personRecord);
    }

    @Override
    public PersonRecord getPersonRecordByName(String name) {
        PersonRecordExample example = new PersonRecordExample();
        PersonRecordExample.Criteria criteria = example.createCriteria();
        criteria.andPNameEqualTo(name);
        List<PersonRecord> personRecord = personRecordMapper.selectByExample(example);
        if (personRecord.isEmpty())
            return null;
        return personRecord.get(0);
    }

    @Override
    public Map<String,Integer> getPersonList(String query) {
        List<String> buy = buyRecordDao.selectNameList();
        List<String> pay = transferRecordDao.selectTransferPayNameList();
        List<String> collection = transferRecordDao.selectTransferCollectionNameList();
        List<String> result = new ArrayList<>();
        for (String s:buy){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:pay){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:collection){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (int i =0;i<result.size();){
            if (result.get(i).indexOf(query)==-1)
                result.remove(i);
            else
                i++;
        }
        Map<String,Integer> map = new TreeMap<>();
       for(String s:result){
           if (personDao.isSearch(s)!=0){//如果是重点
               map.put(s,1);
           }else {
               map.put(s,0);
           }
       }
        return map;
    }

    @Override
    public void saveNameInMemory() {
        List<String> buy = buyRecordDao.selectNameList();
        List<String> pay = transferRecordDao.selectTransferPayNameList();
        List<String> collection = transferRecordDao.selectTransferCollectionNameList();
        List<String> result = new ArrayList<>();
        for (String s:buy){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:pay){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
        for (String s:collection){
            if(s==null)
                continue;
            if (!result.contains(s))
                result.add(s);
        }
//        Person.memory.clear();
        Person.memory=result;
    }


}
